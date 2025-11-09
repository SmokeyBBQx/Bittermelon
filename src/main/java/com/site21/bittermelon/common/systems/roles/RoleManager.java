package com.site21.bittermelon.common.systems.roles;

import com.site21.bittermelon.init.neoforge.BitterRegistries;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.*;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.datafix.DataFixTypes;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.saveddata.SavedData;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class RoleManager extends SavedData {
    private final Map<Holder<Role>, Set<UUID>> whitelists = new HashMap<>();
    private static final String DATA_NAME = "role_data";

    public static @NotNull RoleManager get(@NotNull MinecraftServer server) {
        return server.getLevel(Level.OVERWORLD).getDataStorage().computeIfAbsent(
                new SavedData.Factory<>(
                        RoleManager::new,
                        RoleManager::load,
                        DataFixTypes.LEVEL
                ),
                DATA_NAME
        );
    }

    public static @NotNull RoleManager load(@NotNull CompoundTag tag, HolderLookup.@NotNull Provider lookupProvider) {
        RoleManager manager = new RoleManager();

        CompoundTag whitelistsTag = tag.getCompound("whitelists");
        HolderLookup.RegistryLookup<Role> roleLookup = lookupProvider.lookupOrThrow(BitterRegistries.ROLE_REGISTRY_KEY);

        for (String roleKey : whitelistsTag.getAllKeys()) {
            ResourceLocation roleLocation = ResourceLocation.parse(roleKey);
            ResourceKey<Role> roleResourceKey = ResourceKey.create(BitterRegistries.ROLE_REGISTRY_KEY, roleLocation);
            Optional<Holder.Reference<Role>> roleHolder = roleLookup.get(roleResourceKey);

            if (roleHolder.isPresent()) {
                ListTag uuidList = whitelistsTag.getList(roleKey, Tag.TAG_INT_ARRAY);
                Set<UUID> uuidSet = new HashSet<>();

                for (Tag uuidTag : uuidList) {
                    IntArrayTag intArray = (IntArrayTag) uuidTag;
                    UUID uuid = NbtUtils.loadUUID(intArray);
                    uuidSet.add(uuid);
                }

                manager.whitelists.put(roleHolder.get(), uuidSet);
            }
        }

        return manager;
    }

    @Override
    public @NotNull CompoundTag save(@NotNull CompoundTag compoundTag, HolderLookup.@NotNull Provider provider) {
        CompoundTag whitelistsTag = new CompoundTag();

        for (Map.Entry<Holder<Role>, Set<UUID>> entry : whitelists.entrySet()) {
            Holder<Role> roleHolder = entry.getKey();
            Set<UUID> uuids = entry.getValue();

            Optional<ResourceKey<Role>> roleKey = roleHolder.unwrapKey();
            if (roleKey.isPresent()) {
                ListTag uuidList = new ListTag();
                for (UUID uuid : uuids) {
                    uuidList.add(NbtUtils.createUUID(uuid));
                }
                whitelistsTag.put(roleKey.get().location().toString(), uuidList);
            }
        }

        compoundTag.put("whitelists", whitelistsTag);
        return compoundTag;
    }
}
