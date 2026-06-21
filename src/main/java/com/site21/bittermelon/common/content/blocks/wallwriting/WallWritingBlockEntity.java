package com.site21.bittermelon.common.content.blocks.wallwriting;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.SignText;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;
import java.util.function.UnaryOperator;

import static com.site21.bittermelon.init.neoforge.BitterBlockEntities.WALL_WRITING_BLOCK_ENTITY;

public class WallWritingBlockEntity extends BlockEntity {
    private SignText text;

    public WallWritingBlockEntity(BlockPos pos, BlockState blockState) {
        super(WALL_WRITING_BLOCK_ENTITY.get(), pos, blockState);
        text = new SignText();
    }

    public SignText getText() {
        return text;
    }

    public void setText(SignText text) {
        this.text = text;
    }

    public void updateText(@NotNull UnaryOperator<SignText> updater) {
        setText(updater.apply(text));
    }

    public void updateText(String @NotNull [] text) {
        for (int i = 0; i < text.length; ++i) {
            Style style = this.text.getMessage(i, false).getStyle();
            this.text = this.text.setMessage(i, Component.literal(text[i]).withStyle(style));
        }
        setChanged();
    }

    public boolean playerIsTooFarAwayToEdit(UUID uuid) {
        Player player = level.getPlayerByUUID(uuid);
        return player == null || !player.isWithinBlockInteractionRange(getBlockPos(), 4.0f);
    }

    @Override
    protected void saveAdditional(@NotNull ValueOutput output) {
        super.saveAdditional(output);

        output.store("text", SignText.DIRECT_CODEC, text);
    }

    @Override
    protected void loadAdditional(@NotNull ValueInput input) {
        super.loadAdditional(input);

        text = input.read("text", SignText.DIRECT_CODEC).orElse(new SignText());
    }

    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    public @NotNull CompoundTag getUpdateTag(HolderLookup.@NotNull Provider registries) {
        return saveCustomOnly(registries);
    }
}
