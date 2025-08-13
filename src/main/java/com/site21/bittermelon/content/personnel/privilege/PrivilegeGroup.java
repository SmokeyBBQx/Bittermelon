package com.site21.bittermelon.content.personnel.privilege;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class PrivilegeGroup implements PrivilegeUser {
    public static final Codec<PrivilegeGroup> CODEC;
    public static final StreamCodec<ByteBuf, PrivilegeGroup> STREAM_CODEC;

    private final String name;
    private final Map<String, Boolean> privileges;
    private final Set<String> parents;

    public PrivilegeGroup(String name, Map<String, Boolean> privileges, Collection<String> parents) {
        this.name = name;
        this.privileges = new HashMap<>(privileges);
        this.parents = new HashSet<>(parents);
    }

    public PrivilegeGroup(@NotNull String name) {
        this.name = name.contains("group") ? name.toLowerCase() : "group." + name.toLowerCase();
        this.privileges = new HashMap<>();
        this.parents = new HashSet<>();
        parents.add("default");
    }

    public String getName() {
        return name;
    }

    public Map<String, Boolean> getPrivileges() {
        return privileges;
    }

    public Set<String> getParents() {
        return parents;
    }

    public boolean addPrivilege(String privilege) {
        privileges.put(privilege, true);
        return true;
    }

    public boolean setPrivilege(String privilege, boolean value) {
        privileges.put(privilege, value);
        return true;
    }

    public boolean removePrivilege(String privilege) {
        if (!privileges.containsKey(privilege)) return false;

        privileges.remove(privilege);
        return true;
    }

    public boolean addParent(String parent) {
        return parents.add(parent);
    }

    public boolean removeParent(String parent) {
        return parents.remove(parent);
    }

    static {
        CODEC = RecordCodecBuilder.create(instance -> instance.group(
                        Codec.STRING.fieldOf("name").forGetter(PrivilegeGroup::getName),
                        Codec.unboundedMap(Codec.STRING, Codec.BOOL).fieldOf("privileges").forGetter(PrivilegeGroup::getPrivileges),
                        Codec.list(Codec.STRING).fieldOf("parents").forGetter((group) -> new ArrayList<>(group.getParents()))
                ).apply(instance, PrivilegeGroup::new)
        );

        STREAM_CODEC = StreamCodec.composite(
                ByteBufCodecs.STRING_UTF8,
                PrivilegeGroup::getName,
                ByteBufCodecs.map(
                        HashMap::new,
                        ByteBufCodecs.STRING_UTF8,
                        ByteBufCodecs.BOOL
                ),
                PrivilegeGroup::getPrivileges,
                ByteBufCodecs.STRING_UTF8.apply(ByteBufCodecs.collection(HashSet::new)),
                PrivilegeGroup::getParents,
                PrivilegeGroup::new
        );
    }
}
