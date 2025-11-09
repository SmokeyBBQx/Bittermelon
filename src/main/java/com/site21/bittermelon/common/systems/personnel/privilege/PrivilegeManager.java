package com.site21.bittermelon.common.systems.personnel.privilege;

import com.site21.bittermelon.common.systems.personnel.privilege.networking.AddPrivilege;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.*;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.datafix.DataFixTypes;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.saveddata.SavedData;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.network.PacketDistributor;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class PrivilegeManager extends SavedData {
    private static PrivilegeManager clientInstance;
    private final Map<String, PrivilegeGroup> privilegeGroups = new HashMap<>();
    private final Set<String> privileges = new HashSet<>();

    public static PrivilegeManager get(@NotNull Level level) {
        if (level.isClientSide()) {
            return getClient();
        } else {
            ServerLevel overworld = Objects.requireNonNull(level.getServer()).getLevel(Level.OVERWORLD);
            assert overworld != null;
            return overworld.getDataStorage().computeIfAbsent(
                    new SavedData.Factory<>(
                            PrivilegeManager::new,
                            PrivilegeManager::load,
                            DataFixTypes.LEVEL
                    ),
                    "privileges"
            );
        }
    }

    // Alternative static getter that doesn't require a level
    public static @NotNull PrivilegeManager get(@NotNull MinecraftServer server) {
        return Objects.requireNonNull(server.getLevel(Level.OVERWORLD)).getDataStorage().computeIfAbsent(
                new SavedData.Factory<>(
                        PrivilegeManager::new,
                        PrivilegeManager::load,
                        DataFixTypes.LEVEL
                ),
                "privileges"
        );
    }


    @OnlyIn(Dist.CLIENT)
    private static PrivilegeManager getClient() {
        if (clientInstance == null) {
            clientInstance = new PrivilegeManager();
        }
        return clientInstance;
    }

    @OnlyIn(Dist.CLIENT)
    public static void clearClientData() {
        if (clientInstance != null) {
            clientInstance.privilegeGroups.clear();
        }
    }

    public PrivilegeGroup getPrivilegeGroup(String name) {
        return privilegeGroups.get(name);
    }

    public Map<String, PrivilegeGroup> getPrivilegeGroups() {
        return privilegeGroups;
    }

    public boolean addPrivilegeGroup(@NotNull PrivilegeGroup entry) {
        if (privileges.contains(entry.getName()) || privilegeGroups.containsKey(entry.getName())) return false;
        privilegeGroups.put(entry.getName(), entry);
        setDirty();
        return true;
    }

    public void removePrivilegeGroup(String name) {
        name = name.toLowerCase();
        privilegeGroups.remove(name);
        setDirty();
    }

    public Set<String> getPrivileges() {
        return privileges;
    }

    public boolean addPrivilege(String privilege) {
        privilege = privilege.toLowerCase();
        if (privileges.contains(privilege) || privilegeGroups.containsKey(privilege)) return false;

        privileges.add(privilege);
        setDirty();
        PacketDistributor.sendToAllPlayers(new AddPrivilege(privilege));
        return true;
    }

    public boolean removePrivilege(String privilege) {
        privilege = privilege.toLowerCase();
        if (!privileges.contains(privilege)) return false;

        privileges.remove(privilege);
        setDirty();
        return true;
    }

    public boolean privilegeExists(String privilege) {
        privilege = privilege.toLowerCase();
        return privileges.contains(privilege) || privilegeGroups.containsKey(privilege);
    }

    @OnlyIn(Dist.CLIENT)
    public void addPrivilegeFromServer(String privilege) {
        privileges.add(privilege);
    }

    public static @NotNull PrivilegeManager load(@NotNull CompoundTag tag, HolderLookup.Provider lookupProvider) {
        PrivilegeManager data = new PrivilegeManager();

        if (tag.contains("privilegeGroups", 9)) {
            ListTag groupsTag = tag.getList("privilegeGroups", 10);
            for (Tag value : groupsTag) {
                PrivilegeGroup.CODEC.parse(NbtOps.INSTANCE, value)
                        .resultOrPartial(error -> {
                        }).ifPresent(group -> data.privilegeGroups.put(group.getName(), group));
            }
        }

        if (tag.contains("privileges", 9)) {
            ListTag privilegesTag = tag.getList("privileges", 8);
            for (int i = 0; i < privilegesTag.size(); i++) {
                data.privileges.add(privilegesTag.getString(i));
            }
        }

        return data;
    }

    @Override
    public @NotNull CompoundTag save(@NotNull CompoundTag tag, HolderLookup.@NotNull Provider provider) {
        ListTag groupsTag = new ListTag();
        for (PrivilegeGroup group : privilegeGroups.values()) {
            PrivilegeGroup.CODEC.encodeStart(NbtOps.INSTANCE, group)
                    .resultOrPartial(error -> {})
                    .ifPresent(groupsTag::add);
        }
        tag.put("privilegeGroups", groupsTag);

        ListTag privilegesTag = new ListTag();
        for (String privilege : privileges) {
            privilegesTag.add(StringTag.valueOf(privilege));
        }
        tag.put("privileges", privilegesTag);

        return tag;
    }
}
