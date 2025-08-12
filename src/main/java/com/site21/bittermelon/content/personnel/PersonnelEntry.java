package com.site21.bittermelon.content.personnel;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.UUIDUtil;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.ExtraCodecs;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class PersonnelEntry {
    public static final Codec<PersonnelEntry> CODEC;
    public static final StreamCodec<ByteBuf, PersonnelEntry> STREAM_CODEC;

    private final int id;
    private final UUID playerUUID;
    private String name;
    private String occupation;
    private String department;
    private String notes;
    //    private final int fingerprint;
    private Map<String, Boolean> privileges;

    public PersonnelEntry(int id, UUID playerUUID, String name, String occupation, String department, String notes, Map<String, Boolean> privileges) {
        this.id = id;
        this.playerUUID = playerUUID;
        this.name = name;
        this.occupation = occupation;
        this.department = department;
        this.notes = notes;
        this.privileges = privileges;
    }

    public PersonnelEntry(UUID playerUUID, String name, String occupation, String notes) {
        Random random = new Random();
        this.id = random.nextInt((int) Math.pow(10, 6));
        this.playerUUID = playerUUID;
        this.name = name;
        this.occupation = occupation;
        this.notes = notes;
        this.department = "";
        this.privileges = new HashMap<>();
    }

    public int getId() {
        return id;
    }

    public UUID getPlayerUUID() {
        return playerUUID;
    }

    public String getName() {
        return name;
    }

    public String getOccupation() {
        return occupation;
    }

    public String getDepartment() {
        return department;
    }

    public String getNotes() {
        return notes;
    }

    public Map<String, Boolean> getPrivileges() {
        return privileges;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setOccupation(String occupation) {
        this.occupation = occupation;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public void setPrivileges(Map<String, Boolean> privileges) {
        this.privileges = privileges;
    }

    public void addPrivilege(String privilege) {
        privileges.put(privilege, true);
    }

    public void setPrivilege(String privilege, boolean value) {
        privileges.put(privilege, value);
    }

    public void removePrivilege(String privilege) {
        privileges.remove(privilege);
    }

    static {
        CODEC = RecordCodecBuilder.create(instance -> instance.group(
                Codec.INT.fieldOf("id").forGetter(PersonnelEntry::getId),
                UUIDUtil.CODEC.fieldOf("playerUUID").forGetter(PersonnelEntry::getPlayerUUID),
                Codec.STRING.fieldOf("name").forGetter(PersonnelEntry::getName),
                Codec.STRING.fieldOf("occupation").forGetter(PersonnelEntry::getOccupation),
                Codec.STRING.optionalFieldOf("department", "").forGetter(PersonnelEntry::getDepartment),
                Codec.STRING.fieldOf("notes").forGetter(PersonnelEntry::getNotes),
                Codec.unboundedMap(Codec.STRING, Codec.BOOL).fieldOf("privileges").forGetter(PersonnelEntry::getPrivileges)
        ).apply(instance, PersonnelEntry::new));

        STREAM_CODEC = new StreamCodec<>() {
            @Override
            public @NotNull PersonnelEntry decode(@NotNull ByteBuf byteBuf) {
                int id = byteBuf.readInt();
                UUID playerUUID = UUIDUtil.STREAM_CODEC.decode(byteBuf);
                String name = ByteBufCodecs.STRING_UTF8.decode(byteBuf);
                String occupation = ByteBufCodecs.STRING_UTF8.decode(byteBuf);
                String department = ByteBufCodecs.STRING_UTF8.decode(byteBuf);
                String notes = ByteBufCodecs.STRING_UTF8.decode(byteBuf);
                Map<String, Boolean> privileges = ByteBufCodecs.map(
                                HashMap::new,
                                ByteBufCodecs.STRING_UTF8,
                                ByteBufCodecs.BOOL)
                        .decode(byteBuf);
                return new PersonnelEntry(id, playerUUID, name, occupation, department, notes, privileges);
            }

            @Override
            public void encode(@NotNull ByteBuf byteBuf, @NotNull PersonnelEntry entry) {
                byteBuf.writeInt(entry.getId());
                UUIDUtil.STREAM_CODEC.encode(byteBuf, entry.getPlayerUUID());
                ByteBufCodecs.STRING_UTF8.encode(byteBuf, entry.getName());
                ByteBufCodecs.STRING_UTF8.encode(byteBuf, entry.getOccupation());
                ByteBufCodecs.STRING_UTF8.encode(byteBuf, entry.getDepartment());
                ByteBufCodecs.STRING_UTF8.encode(byteBuf, entry.getNotes());
                ByteBufCodecs.map(HashMap::new, ByteBufCodecs.STRING_UTF8, ByteBufCodecs.BOOL)
                        .encode(byteBuf, new HashMap<>(entry.getPrivileges()));
            }
        };
    }
}

