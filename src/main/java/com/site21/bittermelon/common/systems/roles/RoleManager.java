package com.site21.bittermelon.common.systems.roles;

import net.minecraft.world.level.saveddata.SavedData;

public class RoleManager extends SavedData {
//    public static final SavedDataType<RoleManager> TYPE;
//    private final Map<Holder<Role>, Set<UUID>> whitelists = new HashMap<>();
//
//    public static @NotNull RoleManager get(@NotNull MinecraftServer server) {
//        return server.getLevel(Level.OVERWORLD).getDataStorage().computeIfAbsent(TYPE);
//    }
//
//    public static @NotNull RoleManager load(@NotNull CompoundTag tag, HolderLookup.@NotNull Provider lookupProvider) {
//        RoleManager manager = new RoleManager();
//
//        CompoundTag whitelistsTag = tag.getCompound("whitelists");
//        HolderLookup.RegistryLookup<Role> roleLookup = lookupProvider.lookupOrThrow(BitterRegistries.ROLE_REGISTRY_KEY);
//
//        for (String roleKey : whitelistsTag.getAllKeys()) {
//            Identifier roleLocation = Identifier.parse(roleKey);
//            ResourceKey<Role> roleResourceKey = ResourceKey.create(BitterRegistries.ROLE_REGISTRY_KEY, roleLocation);
//            Optional<Holder.Reference<Role>> roleHolder = roleLookup.get(roleResourceKey);
//
//            if (roleHolder.isPresent()) {
//                ListTag uuidList = whitelistsTag.getList(roleKey, Tag.TAG_INT_ARRAY);
//                Set<UUID> uuidSet = new HashSet<>();
//
//                for (Tag uuidTag : uuidList) {
//                    IntArrayTag intArray = (IntArrayTag) uuidTag;
//                    UUID uuid = NbtUtils.loadUUID(intArray);
//                    uuidSet.add(uuid);
//                }
//
//                manager.whitelists.put(roleHolder.get(), uuidSet);
//            }
//        }
//
//        return manager;
//    }
//
//    @Override
//    public @NotNull CompoundTag save(@NotNull CompoundTag compoundTag, HolderLookup.@NotNull Provider provider) {
//        CompoundTag whitelistsTag = new CompoundTag();
//
//        for (Map.Entry<Holder<Role>, Set<UUID>> entry : whitelists.entrySet()) {
//            Holder<Role> roleHolder = entry.getKey();
//            Set<UUID> uuids = entry.getValue();
//
//            Optional<ResourceKey<Role>> roleKey = roleHolder.unwrapKey();
//            if (roleKey.isPresent()) {
//                ListTag uuidList = new ListTag();
//                for (UUID uuid : uuids) {
//                    uuidList.add(NbtUtils.createUUID(uuid));
//                }
//                whitelistsTag.put(roleKey.get().location().toString(), uuidList);
//            }
//        }
//
//        compoundTag.put("whitelists", whitelistsTag);
//        return compoundTag;
//    }
}
