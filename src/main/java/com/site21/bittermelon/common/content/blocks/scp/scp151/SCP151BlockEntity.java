package com.site21.bittermelon.common.content.blocks.scp.scp151;

import com.site21.bittermelon.init.neoforge.BitterSounds;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundSoundPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.AttachFace;
import net.minecraft.world.phys.*;
import org.jetbrains.annotations.NotNull;

import static com.site21.bittermelon.init.neoforge.BitterBlockEntities.SCP151_BLOCK_ENTITY;
import static com.site21.bittermelon.init.neoforge.BitterMobEffects.DROWNING;

public class SCP151BlockEntity extends BlockEntity {
    private static final int RADIUS = 40;
    private static final int UPDATE_DELAY = 20;

    private int updateTimer = 0;

    public SCP151BlockEntity(BlockPos pos, BlockState blockState) {
        super(SCP151_BLOCK_ENTITY.get(), pos, blockState);
    }

    public void tick() {
        if (level == null || level.isClientSide) return;

        updateTimer++;
        if (updateTimer >= UPDATE_DELAY) {
            updateTimer = 0;
            checkForOnlookers();
        }
    }

    private void checkForOnlookers() {
        AABB searchArea = new AABB(worldPosition).inflate(RADIUS);

        assert level != null;
        for (Player player : level.players()) {
            if (searchArea.contains(player.getX(), player.getY(), player.getZ())) {
                if (isPlayerLookingAtBlock(player) && !player.hasEffect(DROWNING)) {
                    player.addEffect(new MobEffectInstance(DROWNING, 24000, 0, true, false, false));
                    player.hurtMarked = true;
                    Component message = Component.literal("You notice a peculiar blue painting. It resembles the sea.")
                            .withStyle(ChatFormatting.RED).withStyle(ChatFormatting.ITALIC);
                    player.displayClientMessage(message, false);
                    if (player instanceof ServerPlayer serverPlayer) {
                        serverPlayer.connection.send(new ClientboundSoundPacket(
                                BitterSounds.SCARE_1,
                                SoundSource.AMBIENT,
                                player.getX(),
                                player.getY(),
                                player.getZ(),
                                0.3f,
                                1,
                                level.getRandom().nextLong()));
                    }
                }
            }
        }
    }

    private boolean isPlayerLookingAtBlock(@NotNull Player player) {
        Vec3 eyePosition = player.getEyePosition();
        Vec3 lookVector = player.getViewVector(1.0F).normalize();
        Vec3 endPoint = eyePosition.add(lookVector.scale(player.blockInteractionRange() * 2));

        Vec3 blockCenter = Vec3.atCenterOf(worldPosition);
        Vec3 toBlock = blockCenter.subtract(eyePosition).normalize();

        double dotProduct = lookVector.dot(toBlock);

        if (dotProduct < Math.cos(Math.toRadians(60))) {
            return false;
        }

        ClipContext clipContext = new ClipContext(
                eyePosition,
                endPoint,
                ClipContext.Block.OUTLINE,
                ClipContext.Fluid.NONE,
                player
        );

        BlockHitResult hitResult = level.clip(clipContext);
        if (hitResult.getType() != HitResult.Type.BLOCK || !hitResult.getBlockPos().equals(worldPosition)) {
            return false;
        }

        BlockState state = level.getBlockState(worldPosition);
        AttachFace face = state.getValue(SCP151Block.FACE);

        Vec3 expectedDirection;
        if (face == AttachFace.CEILING) {
            expectedDirection = new Vec3(0, 1, 0);
        } else if (face == AttachFace.FLOOR) {
            expectedDirection = new Vec3(0, -1, 0);
        } else {
            Direction facing = state.getValue(SCP151Block.FACING).getOpposite();
//            expectedDirection = Vec3.atLowerCornerOf(facing.getNormal());
            expectedDirection = Vec3.atLowerCornerOf(facing.getUnitVec3i());
        }

        double facingDot = toBlock.dot(expectedDirection);
        return facingDot > 0.1;
    }
}
