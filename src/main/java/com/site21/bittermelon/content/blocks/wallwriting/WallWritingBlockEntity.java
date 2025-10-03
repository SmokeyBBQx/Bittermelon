package com.site21.bittermelon.content.blocks.wallwriting;

import com.mojang.serialization.DataResult;
import com.site21.bittermelon.Bittermelon;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.SignText;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
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
        level.playSound(null, worldPosition, SoundEvents.VILLAGER_WORK_CARTOGRAPHER, SoundSource.BLOCKS, 1.0f, 1.0f);
    }

    public boolean playerIsTooFarAwayToEdit(UUID uuid) {
        Player player = level.getPlayerByUUID(uuid);
        return player == null || !player.canInteractWithBlock(this.getBlockPos(), 4.0f);
    }

    @Override
    protected void saveAdditional(@NotNull CompoundTag tag, HolderLookup.@NotNull Provider registries) {
        super.saveAdditional(tag, registries);

        DataResult<Tag> result = SignText.DIRECT_CODEC.encodeStart(NbtOps.INSTANCE, text);
        result.resultOrPartial(error ->
                        Bittermelon.LOGGER.error("Wall writing failed to save at {}", worldPosition))
                .ifPresent(encodedTag -> tag.put("text", encodedTag));
    }

    @Override
    protected void loadAdditional(@NotNull CompoundTag tag, HolderLookup.@NotNull Provider registries) {
        super.loadAdditional(tag, registries);

        if (tag.contains("text")) {
            DataResult<SignText> result = SignText.DIRECT_CODEC.parse(NbtOps.INSTANCE, tag.get("text"));
            result.resultOrPartial(error ->
                            Bittermelon.LOGGER.error("Wall writing failed to load at {}", worldPosition))
                    .ifPresent(loadedText -> text = loadedText);
        }
    }

    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    public @NotNull CompoundTag getUpdateTag(HolderLookup.@NotNull Provider registries) {
        return saveCustomOnly(registries);
    }
}
