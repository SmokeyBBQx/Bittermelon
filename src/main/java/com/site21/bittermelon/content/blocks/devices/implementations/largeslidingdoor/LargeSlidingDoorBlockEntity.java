package com.site21.bittermelon.content.blocks.devices.implementations.largeslidingdoor;

import com.site21.bittermelon.content.blocks.devices.ElectronicBlockEntity;
import com.site21.bittermelon.content.blocks.devices.implementations.largeslidingdoor.networking.PlaySlidingDoorStuckSound;
import com.site21.bittermelon.content.blocks.devices.implementations.largeslidingdoor.networking.UpdateSlidingDoorProgress;
import com.site21.bittermelon.content.blocks.devices.implementations.largeslidingdoor.networking.UpdateSlidingDoorState;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.util.Mth;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.network.PacketDistributor;
import org.jetbrains.annotations.NotNull;

import static com.site21.bittermelon.content.blocks.devices.implementations.largeslidingdoor.LargeSlidingDoorBlock.*;
import static com.site21.bittermelon.init.neoforge.BitterBlockEntities.LARGE_SLIDING_DOOR_BLOCK_ENTITY;
import static com.site21.bittermelon.init.neoforge.BitterBlocks.LARGE_SLIDING_DOOR;

public class LargeSlidingDoorBlockEntity extends ElectronicBlockEntity {
    private static final float ANIMATION_SPEED = 0.05f;

    private float doorProgress = 0.0f;
    private float lastProgress = 0.0f;
    private boolean reverseStuckAnimation = false;

    public LargeSlidingDoorBlockEntity(BlockPos pos, BlockState blockState) {
        super(LARGE_SLIDING_DOOR_BLOCK_ENTITY.get(), pos, blockState);
    }

    public void clientTick() {
        if (level == null) return;
        this.lastProgress = this.doorProgress;
        State state = getBlockState().getValue(STATE);

        if (state == State.STUCK) {
            handleStuckAnimation();
        } else if (state == State.OPENING) {
            this.doorProgress = Math.min(1.0f, this.doorProgress + ANIMATION_SPEED);
            if (doorProgress >= 1) {
                PacketDistributor.sendToServer(new UpdateSlidingDoorState(worldPosition, State.OPEN));
            }
        } else if (state == State.CLOSING) {
            this.doorProgress = Math.max(0.0f, this.doorProgress - ANIMATION_SPEED);
            if (doorProgress <= 0) {
                PacketDistributor.sendToServer(new UpdateSlidingDoorState(worldPosition, State.CLOSED));
            }
        }
    }

    private void handleStuckAnimation() {
        // TODO: Weird stuck animation spam glitch - possibly due to data not being saved server-side?

        if (!reverseStuckAnimation) {
            this.doorProgress = Math.min(1f, this.doorProgress + ANIMATION_SPEED);
            PacketDistributor.sendToServer(new UpdateSlidingDoorProgress(doorProgress, worldPosition));
            if (doorProgress >= 1f) {
                reverseStuckAnimation = true;
            }
        } else {
            this.doorProgress = Math.max(0.3f, this.doorProgress - ANIMATION_SPEED);
            PacketDistributor.sendToServer(new UpdateSlidingDoorProgress(doorProgress, worldPosition));
            if (doorProgress <= 0.3f) {
                reverseStuckAnimation = false;
                PacketDistributor.sendToServer(new PlaySlidingDoorStuckSound(worldPosition));
            }
        }
    }

    public float getDoorOpenAmount(float partialTick) {
        return Mth.lerp(partialTick, this.lastProgress, this.doorProgress);
    }


    public void tick() {
        if (level == null) return;
        if (getBlockState().getValue(STATE) == State.STUCK) {
            if (getBlockState().getBlock() instanceof LargeSlidingDoorBlock block && block.canClose(level, worldPosition)) {
                level.setBlock(worldPosition, getBlockState().setValue(STATE, State.CLOSING), 3);
                boolean zAxis = getBlockState().getValue(Z_AXIS);
                level.setBlock(worldPosition.below(), LARGE_SLIDING_DOOR.get().defaultBlockState().setValue(Z_AXIS, zAxis), 3);
                level.setBlock(worldPosition.below(2), LARGE_SLIDING_DOOR.get().defaultBlockState().setValue(Z_AXIS, zAxis), 3);
            }
        }
    }

    public float getDoorProgress() {
        return doorProgress;
    }

    public float getLastProgress() {
        return lastProgress;
    }

    public void setDoorProgress(float doorProgress) {
        this.doorProgress = Math.max(0, Math.min(1, doorProgress));
        setChanged();
    }

    @Override
    protected void saveAdditional(@NotNull CompoundTag tag, HolderLookup.@NotNull Provider registries) {
        super.saveAdditional(tag, registries);
        tag.putFloat("doorProgress", doorProgress);
    }

    @Override
    public void loadAdditional(@NotNull CompoundTag tag, HolderLookup.@NotNull Provider registries) {
        super.loadAdditional(tag, registries);
        doorProgress = tag.getFloat("doorProgress");
    }


    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }
}
