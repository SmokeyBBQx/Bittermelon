package com.site21.bittermelon.common.content.blocks.electronics.largeslidingdoor;

import com.site21.bittermelon.common.systems.electronics.ElectronicBlockEntity;
import com.site21.bittermelon.common.content.blocks.electronics.largeslidingdoor.networking.PlaySlidingDoorStuckSound;
import com.site21.bittermelon.common.content.blocks.electronics.largeslidingdoor.networking.UpdateSlidingDoorProgress;
import com.site21.bittermelon.common.content.blocks.electronics.largeslidingdoor.networking.UpdateSlidingDoorState;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.neoforged.neoforge.client.network.ClientPacketDistributor;
import org.jetbrains.annotations.NotNull;

import static com.site21.bittermelon.common.content.blocks.electronics.largeslidingdoor.LargeSlidingDoorBlock.*;
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
                ClientPacketDistributor.sendToServer(new UpdateSlidingDoorState(worldPosition, State.OPEN));
            }
        } else if (state == State.CLOSING) {
            this.doorProgress = Math.max(0.0f, this.doorProgress - ANIMATION_SPEED);
            if (doorProgress <= 0) {
                ClientPacketDistributor.sendToServer(new UpdateSlidingDoorState(worldPosition, State.CLOSED));
            }
        }
    }

    private void handleStuckAnimation() {
        // TODO: Weird stuck animation spam glitch - possibly due to data not being saved server-side?

        if (!reverseStuckAnimation) {
            this.doorProgress = Math.min(1f, this.doorProgress + ANIMATION_SPEED);
            ClientPacketDistributor.sendToServer(new UpdateSlidingDoorProgress(doorProgress, worldPosition));
            if (doorProgress >= 1f) {
                reverseStuckAnimation = true;
            }
        } else {
            this.doorProgress = Math.max(0.3f, this.doorProgress - ANIMATION_SPEED);
            ClientPacketDistributor.sendToServer(new UpdateSlidingDoorProgress(doorProgress, worldPosition));
            if (doorProgress <= 0.3f) {
                reverseStuckAnimation = false;
                ClientPacketDistributor.sendToServer(new PlaySlidingDoorStuckSound(worldPosition));
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
    protected void saveAdditional(@NotNull ValueOutput output) {
        super.saveAdditional(output);

        output.putFloat("doorProgress", doorProgress);
    }

    @Override
    protected void loadAdditional(@NotNull ValueInput input) {
        super.loadAdditional(input);

        doorProgress = input.getFloatOr("doorProgress", 0.0f);
    }
}
