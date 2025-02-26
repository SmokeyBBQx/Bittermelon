package com.site21.bittermelon.content.blocks.devices.implementations.speaker;

import com.site21.bittermelon.content.blocks.devices.IElectronic;
import com.site21.bittermelon.content.blocks.devices.connection.*;
import com.site21.bittermelon.content.syncsound.SyncSoundEvent;
import com.site21.bittermelon.content.syncsound.SyncSoundType;
import com.site21.bittermelon.util.LocalMessageHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.common.NeoForge;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

import static com.site21.bittermelon.init.neoforge.BitterBlockEntities.SPEAKER_BLOCK_ENTITY;

public class SpeakerBlockEntity extends BlockEntity implements IElectronic {
    private String address = "";
    private int speakerRadius = 16;

    public SpeakerBlockEntity(BlockPos pos, BlockState blockState) {
        super(SPEAKER_BLOCK_ENTITY.get(), pos, blockState);
        address = generateAddress("SPE");
    }

    private void broadcast(Signal signal) {
        if (level == null || level.isClientSide) return;

        Component intercomMessage = Component.literal("[SPEAKER]: ").append((Component) signal.value());

        NeoForge.EVENT_BUS.post(new SyncSoundEvent(level, getBlockPos(), SyncSoundType.SPEAKER, intercomMessage, speakerRadius));
        LocalMessageHelper.sendLocalMessage(level, getBlockPos(), speakerRadius, intercomMessage);
    }

    @Override
    public Map<String, InputPort> getInputPorts() {
        return Map.of(
                "BROADCAST", new InputPort("BROADCAST", this::broadcast, worldPosition)
        );
    }

    @Override
    public String getAddress() {
        return address;
    }

    @Override
    protected void saveAdditional(@NotNull CompoundTag tag, HolderLookup.@NotNull Provider registries) {
        super.saveAdditional(tag, registries);
        saveInputPorts(tag);
    }

    @Override
    public void loadAdditional(@NotNull CompoundTag tag, HolderLookup.@NotNull Provider registries) {
        super.loadAdditional(tag, registries);
        loadInputPorts(tag, level);
    }

    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    public @NotNull CompoundTag getUpdateTag(HolderLookup.@NotNull Provider registries) {
        return this.saveCustomOnly(registries);
    }

    @Override
    public void setChanged() {
        super.setChanged();
        syncToClient();
    }

    public void syncToClient() {
        if (level != null && !level.isClientSide) {
            level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), 3);
        }
    }

}
