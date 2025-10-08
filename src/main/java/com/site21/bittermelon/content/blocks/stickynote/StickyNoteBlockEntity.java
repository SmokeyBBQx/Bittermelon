package com.site21.bittermelon.content.blocks.stickynote;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

import static com.site21.bittermelon.init.neoforge.BitterBlockEntities.STICKY_NOTE_BLOCK_ENTITY;

public class StickyNoteBlockEntity extends BlockEntity {
    private final String[] notes = new String[4];

    public StickyNoteBlockEntity(BlockPos pos, BlockState blockState) {
        super(STICKY_NOTE_BLOCK_ENTITY.get(), pos, blockState);
    }

    public String[] getNotes() {
        return notes;
    }

    public void setNote(int index, String note) {
        notes[index] = note;
        setChanged();
    }

    @Override
    protected void saveAdditional(@NotNull CompoundTag tag, HolderLookup.@NotNull Provider registries) {
        super.saveAdditional(tag, registries);
        for (int i = 0; i < notes.length; i++) {
            if (notes[i] != null) {
                tag.putString("note_" + i, notes[i]);
            }
        }
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.@NotNull Provider registries) {
        super.loadAdditional(tag, registries);
        for (int i = 0; i < notes.length; i++) {
            if (tag.contains("note_" + i)) {
                notes[i] = tag.getString("note_" + i);
            }
        }
    }

    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    public @NotNull CompoundTag getUpdateTag(HolderLookup.@NotNull Provider registries) {
        return saveCustomOnly(registries);
    }
}
