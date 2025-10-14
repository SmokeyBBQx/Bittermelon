package com.site21.bittermelon.content.blocks.devices.implementations.speaker;

import com.site21.bittermelon.content.blocks.devices.ElectronicDevice;
import com.site21.bittermelon.content.blocks.devices.ElectronicBlockEntity;
import com.site21.bittermelon.content.blocks.devices.wiring.*;
import com.site21.bittermelon.content.syncsound.SyncSoundEvent;
import com.site21.bittermelon.content.syncsound.SyncSoundType;
import com.site21.bittermelon.util.LocalMessageHelper;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.neoforged.neoforge.common.NeoForge;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

import static com.site21.bittermelon.init.neoforge.BitterBlockEntities.SPEAKER_BLOCK_ENTITY;

public class SpeakerBlockEntity extends ElectronicBlockEntity implements ElectronicDevice {
    private int speakerRadius = 16;
    private final Map<String, InputPort> inputPorts;

    public SpeakerBlockEntity(BlockPos pos, BlockState blockState) {
        super(SPEAKER_BLOCK_ENTITY.get(), pos, blockState);

        inputPorts = Map.of(
                "BROADCAST", new InputPort("BROADCAST", this::broadcast, worldPosition)
        );
    }

    private void broadcast(Signal signal) {
        if (level == null || level.isClientSide) return;

        SyncSoundEvent event;

        if (signal.value() instanceof SyncSoundEvent receivedEvent) {
            event = receivedEvent;
        } else {
            event = new SyncSoundEvent(level, getBlockPos(), SyncSoundType.SPEAKER, Component.literal(("Bzzzz..")).withStyle(ChatFormatting.ITALIC).withStyle(ChatFormatting.GRAY), speakerRadius);
        }

        Component speakerMessage = Component.literal("[SPEAKER]: ").append(event.getSoundDescription());

        NeoForge.EVENT_BUS.post(new SyncSoundEvent(level, getBlockPos(), SyncSoundType.SPEAKER, speakerMessage, speakerRadius, event.getSoundEvent()));
        if (event.getSoundEvent() != null) {
            level.playSound(null, worldPosition, event.getSoundEvent(), SoundSource.NEUTRAL, 0.1f, 1);
        }
        LocalMessageHelper.sendLocalMessage(level, getBlockPos(), speakerRadius, speakerMessage);
    }

    @Override
    public Map<String, InputPort> getInputPorts() {
        return inputPorts;
    }

    @Override
    protected void saveAdditional(@NotNull ValueOutput output) {
        super.saveAdditional(output);

        saveInputPorts(output);
    }

    @Override
    protected void loadAdditional(@NotNull ValueInput input) {
        super.loadAdditional(input);

        loadInputPorts(input);
    }
}
