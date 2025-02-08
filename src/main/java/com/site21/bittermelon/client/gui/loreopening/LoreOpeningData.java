package com.site21.bittermelon.client.gui.loreopening;

import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.datafix.DataFixTypes;
import net.minecraft.world.level.saveddata.SavedData;
import org.jetbrains.annotations.NotNull;

public class LoreOpeningData extends SavedData {
    private String message = "BITTERMELON, OFFICIAL MOD OF SITE-21";
    private static final String DATA_NAME = "lore_opening_message";

    public LoreOpeningData() {
    }

    @Override
    public @NotNull CompoundTag save(@NotNull CompoundTag compoundTag, HolderLookup.@NotNull Provider provider) {
        compoundTag.putString("Message", message);
        return compoundTag;
    }

    public static @NotNull LoreOpeningData load(@NotNull CompoundTag compoundTag, HolderLookup.@NotNull Provider provider) {
        LoreOpeningData data = new LoreOpeningData();
        data.setMessage(compoundTag.getString("Message"));
        return data;
    }

    public void setMessage(String newMessage) {
        this.message = newMessage;
        this.setDirty();
    }

    public String getMessage() {
        return this.message;
    }

    public static @NotNull LoreOpeningData get(@NotNull ServerLevel level) {
        return level.getDataStorage().computeIfAbsent(
                new SavedData.Factory<>(
                        LoreOpeningData::new,
                        LoreOpeningData::load,
                        DataFixTypes.LEVEL
                ),
                DATA_NAME
        );
    }
}
