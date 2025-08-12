package com.site21.bittermelon.content.personnel;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

import java.util.*;

public class PrivilegeGroup {
    public static final Codec<PrivilegeGroup> CODEC;
    public static final StreamCodec<ByteBuf, PrivilegeGroup> STREAM_CODEC;

    private final String name;
    private final Map<String, Boolean> privileges;
    private final Set<String> parents;

    public PrivilegeGroup(String name, Map<String, Boolean> privileges, Set<String> parents) {
        this.name = name;
        this.privileges = privileges;
        this.parents = parents;
    }

    public PrivilegeGroup(String name) {
        this.name = name;
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

    public boolean removePrivilege(String privilege) {
        if (!privileges.containsKey(privilege)) return false;

        privileges.remove(privilege);
        return true;
    }

    public boolean removeParent(String parent) {
        return parents.remove(parent);
    }

    static {
        CODEC = RecordCodecBuilder.create(instance -> instance.group(
                        Codec.STRING.fieldOf("name").forGetter(PrivilegeGroup::getName),
                        Codec.unboundedMap(Codec.STRING, Codec.BOOL).fieldOf("privileges").forGetter(PrivilegeGroup::getPrivileges),
                        Codec.list(Codec.STRING).fieldOf("parents").forGetter((group) -> new ArrayList<>(group.getParents()))
                ).apply(instance, (name, privileges, parents)
                        -> new PrivilegeGroup(name, privileges, new HashSet<>(parents)))
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
