package com.site21.bittermelon.content.personnel.registry;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.site21.bittermelon.content.character.Character;
import com.site21.bittermelon.content.personnel.privilege.PrivilegeOwner;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.UUIDUtil;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class PersonnelEntry implements PrivilegeOwner {
    public static final Codec<PersonnelEntry> CODEC;
    public static final StreamCodec<ByteBuf, PersonnelEntry> STREAM_CODEC;

    private final int id;
    private final UUID playerUUID;
    private final UUID characterUUID;
    private String name;
    private String occupation;
    private String department;
    private String notes;
    //    private final int fingerprint;
    private Map<String, Boolean> privileges;

    public PersonnelEntry(int id, UUID playerUUID, UUID characterUUID, String name, String occupation, String department, String notes, Map<String, Boolean> privileges) {
        this.id = id;
        this.playerUUID = playerUUID;
        this.characterUUID = characterUUID;
        this.name = name;
        this.occupation = occupation;
        this.department = department;
        this.notes = notes;
        this.privileges = new HashMap<>(privileges);
    }

    public PersonnelEntry(UUID playerUUID, UUID characterUUID, String name, String occupation, String notes) {
        Random random = new Random();
        this.id = random.nextInt((int) Math.pow(10, 6));
        this.playerUUID = playerUUID;
        this.characterUUID = characterUUID;
        this.name = name;
        this.occupation = occupation;
        this.notes = notes;
        this.department = "";
        this.privileges = new HashMap<>();
    }

    public PersonnelEntry(@NotNull Player player, @NotNull Character character, String department, String occupation) {
        this(player.getUUID(), character.getUUID(), character.getName(), occupation, "");
        this.department = department;
    }

    public int getId() {
        return id;
    }

    public UUID getPlayerUUID() {
        return playerUUID;
    }

    public UUID getCharacterUUID() {
        return characterUUID;
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
                UUIDUtil.CODEC.fieldOf("characterUUID").forGetter(PersonnelEntry::getCharacterUUID),
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
                UUID characterUUID = UUIDUtil.STREAM_CODEC.decode(byteBuf);
                String name = ByteBufCodecs.STRING_UTF8.decode(byteBuf);
                String occupation = ByteBufCodecs.STRING_UTF8.decode(byteBuf);
                String department = ByteBufCodecs.STRING_UTF8.decode(byteBuf);
                String notes = ByteBufCodecs.STRING_UTF8.decode(byteBuf);
                Map<String, Boolean> privileges = ByteBufCodecs.map(
                                HashMap::new,
                                ByteBufCodecs.STRING_UTF8,
                                ByteBufCodecs.BOOL)
                        .decode(byteBuf);
                return new PersonnelEntry(id, playerUUID, characterUUID, name, occupation, department, notes, privileges);
            }

            @Override
            public void encode(@NotNull ByteBuf byteBuf, @NotNull PersonnelEntry entry) {
                byteBuf.writeInt(entry.getId());
                UUIDUtil.STREAM_CODEC.encode(byteBuf, entry.getPlayerUUID());
                UUIDUtil.STREAM_CODEC.encode(byteBuf, entry.getCharacterUUID());
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

