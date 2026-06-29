package com.site21.bittermelon.common.systems.personnel.privilege;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.site21.bittermelon.Bittermelon;
import com.site21.bittermelon.common.systems.personnel.privilege.networking.AddPrivilege;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraft.world.level.saveddata.SavedDataType;
import net.neoforged.neoforge.network.PacketDistributor;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class PrivilegeManager extends SavedData {
    public static final SavedDataType<PrivilegeManager> TYPE;
    private static PrivilegeManager clientInstance;
    private final Map<String, PrivilegeGroup> privilegeGroups = new HashMap<>();
    private final Set<String> privileges = new HashSet<>();

    public static PrivilegeManager get(@NotNull Level level) {
        if (level.isClientSide()) {
            return getClient();
        } else {
            ServerLevel overworld = Objects.requireNonNull(level.getServer()).getLevel(Level.OVERWORLD);
            assert overworld != null;
            return overworld.getDataStorage().computeIfAbsent(TYPE);
        }
    }

    // Alternative static getter that doesn't require a level
    public static @NotNull PrivilegeManager get(@NotNull MinecraftServer server) {
        return Objects.requireNonNull(server.getLevel(Level.OVERWORLD)).getDataStorage().computeIfAbsent(TYPE);
    }


    
    private static PrivilegeManager getClient() {
        if (clientInstance == null) {
            clientInstance = new PrivilegeManager();
        }
        return clientInstance;
    }

    
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

    
    public void addPrivilegeFromServer(String privilege) {
        privileges.add(privilege);
    }

    static {
        TYPE = new SavedDataType<>(
                Bittermelon.identifier("privileges"),
                PrivilegeManager::new,
                RecordCodecBuilder.create(instance -> instance.group(
                        PrivilegeGroup.CODEC.listOf().fieldOf("privilegeGroups")
                                .forGetter(pm -> new ArrayList<>(pm.privilegeGroups.values())),
                        Codec.STRING.listOf().fieldOf("privileges")
                                .forGetter(pm -> new ArrayList<>(pm.privileges))
                ).apply(instance, (List<PrivilegeGroup> groups, List<String> privileges) -> {
                            PrivilegeManager pm = new PrivilegeManager();
                            for (PrivilegeGroup group : groups) {
                                pm.privilegeGroups.put(group.getName(), group);
                            }
                            pm.privileges.addAll(privileges);
                            return pm;
                        }
                ))
        );
    }
}
