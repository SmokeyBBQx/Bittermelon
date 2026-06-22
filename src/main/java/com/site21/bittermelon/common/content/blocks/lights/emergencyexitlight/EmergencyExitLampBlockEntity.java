package com.site21.bittermelon.common.content.blocks.lights.emergencyexitlight;

import com.site21.bittermelon.init.neoforge.BitterSounds;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.Vec3;

import static com.site21.bittermelon.init.neoforge.BitterBlockEntities.EMERGENCY_EXIT_LAMP_BLOCK_ENTITY;

public class EmergencyExitLampBlockEntity extends BlockEntity {
    public EmergencyExitLampBlockEntity(BlockPos pos, BlockState blockState) {
        super(EMERGENCY_EXIT_LAMP_BLOCK_ENTITY.get(), pos, blockState);
    }

    public void tick() {
        if (!getBlockState().getValue(BlockStateProperties.LIT)) return;

        if (level != null && !level.isClientSide()) {
            if (level.getGameTime() % 20 == 0) {
                double range = 3.5;
                Vec3 pos = Vec3.atCenterOf(worldPosition);

                for (Player player : level.players()) {
                    if (player.position().distanceTo(pos) <= range) {
                        // TODO: Find alternative for playNotifySound that only plays sound to specific player
                        level.playPlayerSound(BitterSounds.SCANNER_BEEP.value(), SoundSource.BLOCKS, 0.005f, 1.2f);
                    }
                }
            }
        }
    }
}
