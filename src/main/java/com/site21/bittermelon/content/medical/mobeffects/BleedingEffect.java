package com.site21.bittermelon.content.medical.mobeffects;

import com.site21.bittermelon.content.blocks.substance.fluid.FluidBlock;
import com.site21.bittermelon.content.blocks.substance.fluid.FluidBlockEntity;
import com.site21.bittermelon.content.character.Character;
import com.site21.bittermelon.content.character.CharacterManager;
import com.site21.bittermelon.content.medical.blood.BloodData;
import com.site21.bittermelon.content.medical.medicalstats.AnimalMedicalStats;
import com.site21.bittermelon.content.substance.SubstanceStack;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.block.CarpetBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

import static com.site21.bittermelon.init.custom.Substances.BLOOD;
import static com.site21.bittermelon.init.neoforge.BitterBlocks.FLUID;
import static com.site21.bittermelon.init.neoforge.BitterDataComponents.BLOOD_DATA;
import static net.minecraft.world.level.block.Block.UPDATE_ALL_IMMEDIATE;

public class BleedingEffect extends MobEffect {
    public BleedingEffect() {
        super(MobEffectCategory.HARMFUL, 0);
    }

    @Override
    public boolean shouldApplyEffectTickThisTick(int duration, int amplifier) {
        return duration % (100 - amplifier * 5) == 0;
    }

    @Override
    public boolean applyEffectTick(@NotNull ServerLevel level, @NotNull LivingEntity entity, int amplifier) {
        BlockPos pos = entity.getOnPos().above();

        BlockState existingState = level.getBlockState(pos);
        if (level.getBlockState(pos.below()).getBlock() instanceof CarpetBlock) return false;

        SubstanceStack stack = new SubstanceStack(BLOOD.get(), 0);
        stack.setVolume(amplifier);

        Character character = CharacterManager.get(entity.level()).getActiveCharacter(entity);
        if (character != null) {
            if (character.getMedicalStats() instanceof AnimalMedicalStats medicalStats) {
                stack.set(BLOOD_DATA, new BloodData(medicalStats.getBloodType(), medicalStats.getActiveDrugs()));
                medicalStats.modifyBloodVolume(-amplifier);
            }
        }

        if (!(existingState.getBlock() instanceof FluidBlock) && existingState.canBeReplaced()) {
            level.setBlock(pos, FLUID.get().defaultBlockState(), UPDATE_ALL_IMMEDIATE);
        }

        if (level.getBlockEntity(pos) instanceof FluidBlockEntity fluid) {
            fluid.updateSubstance(stack);
        }

        return true;
    }
}
