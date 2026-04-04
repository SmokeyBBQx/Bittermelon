package com.site21.bittermelon.common.systems.fluid.substance;

import com.site21.bittermelon.common.systems.substance.SubstanceMixture;
import com.site21.bittermelon.common.systems.substance.SubstanceStack;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.util.Mth;
import net.minecraft.util.profiling.Profiler;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.jetbrains.annotations.NotNull;

import java.util.List;

import static com.site21.bittermelon.init.neoforge.BitterBlockEntities.SUBSTANCE_FLUID_BLOCK_ENTITY;
import static com.site21.bittermelon.init.neoforge.BitterFluids.SUBSTANCE_FLUID;
import static net.minecraft.world.level.block.Block.UPDATE_ALL;
import static net.minecraft.world.level.block.Block.UPDATE_CLIENTS;

public class SubstanceFluidBlockEntity extends BlockEntity {
    private SubstanceMixture mixture;

    public SubstanceFluidBlockEntity(BlockPos pos, BlockState blockState) {
        super(SUBSTANCE_FLUID_BLOCK_ENTITY.get(), pos, blockState);

        Runnable mixtureChangedCallback = () -> {
            setChanged();
            updateFluidState();
        };
        mixture = new SubstanceMixture(mixtureChangedCallback);
        mixture.setTemperature(500);
    }

    public void tickReactions() {
        mixture.tickReactions(level, worldPosition);
    }

    public void updateSubstance(SubstanceStack substance) {
        mixture.updateSubstance(substance);
    }

    public void updateSubstanceNoUpdate(SubstanceStack substance) {
        mixture.updateSubstanceNoUpdate(substance);
    }

    public void transferSubstances(@NotNull List<SubstanceStack> substances) {
        mixture.transferSubstances(substances);
    }

    public void removeSubstance(SubstanceStack substance, int amount) {
        mixture.removeSubstance(substance, amount);
    }

    public void removeSubstanceNoUpdate(SubstanceStack substance, int amount) {
        mixture.removeSubstance(substance, amount);
    }

    public void removeSubstances(@NotNull List<SubstanceStack> substances) {
        mixture.removeSubstances(substances);
    }

    public void removeSubstances(List<SubstanceStack> substances, int multiplier) {
        mixture.removeSubstances(substances, multiplier);
    }

    public void removeSubstancesNoUpdate(List<SubstanceStack> substances, int multiplier) {
        mixture.removeSubstances(substances, multiplier);
    }

    public List<SubstanceStack> getSubstances() {
        return mixture.getSubstances();
    }

    public void setSubstances(List<SubstanceStack> newSubstances) {
        mixture.setSubstances(newSubstances);
    }

    public void mergeSubstances(@NotNull List<SubstanceStack> substances, SubstanceStack stack) {
        mixture.mergeSubstances(substances, stack);
    }

    public void setSubstancesQuiet(List<SubstanceStack> stacks) {
        mixture.setSubstances(stacks);
        setChanged();
    }

    public void updateFluidState() {
        if (level == null) return;
        if (level.getBlockState(worldPosition).isAir()) return;

        Profiler.get().push("updateFluidState");

        int fluidLevel = Math.max(1, Mth.clamp(getVolume() / 50, 1, 19));
        int currentFluidLevel = level.getFluidState(worldPosition).getAmount();
        if (currentFluidLevel != 20 && currentFluidLevel != fluidLevel) {
            BlockState currentState = level.getBlockState(worldPosition);
            BlockState newState = currentState.setValue(SubstanceFluidBlock.LEVEL, fluidLevel);

            level.setBlock(worldPosition, newState, UPDATE_ALL);
        }

        level.scheduleTick(worldPosition, SUBSTANCE_FLUID.get(), SUBSTANCE_FLUID.get().getTickDelay(level));

        Profiler.get().pop();
    }

    public void updateFluidStateOld() {
        if (level == null) return;
        if (level.getBlockState(worldPosition).isAir()) return;

        Profiler.get().push("updateFluidState");

        int fluidLevel = Math.max(1, Mth.clamp(getVolume() / 50, 1, 19));

        BlockState currentState = level.getBlockState(worldPosition);
        BlockState newState = currentState.setValue(SubstanceFluidBlock.LEVEL, fluidLevel);

        level.setBlock(worldPosition, newState, UPDATE_ALL);
        level.scheduleTick(worldPosition, SUBSTANCE_FLUID.get(), SUBSTANCE_FLUID.get().getTickDelay(level));

        Profiler.get().pop();
    }

    public int getVolume() {
        return mixture.getVolume();
    }

    public float getTemperature() {
        return mixture.getTemperature();
    }

    public int getColor() {
        return mixture.getColor();
    }

    public int getViscosity() {
        return mixture.getViscosity();
    }

    /**
     * Calculates the pressure based on the fluid's volume.
     * The pressure is defined as the difference between the full block volume and the current volume of the fluid.
     *
     * @return The calculated pressure value.
     */
    public int getPressure() {
        return getVolume() - SubstanceFluid.FULL_BLOCK_VOLUME;
    }

    public String getContentsDescription() {
        return mixture.getContentsDescription();
    }

    @Override
    protected void saveAdditional(@NotNull ValueOutput output) {
        super.saveAdditional(output);

        output.store("mixture", SubstanceMixture.CODEC, mixture);
        output.putInt("cachedColor", mixture.getColor());
    }

    @Override
    protected void loadAdditional(@NotNull ValueInput input) {
        super.loadAdditional(input);

        input.read("mixture", SubstanceMixture.CODEC).ifPresent(mixture -> this.mixture = mixture);
    }

    @Override
    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public @NotNull CompoundTag getUpdateTag(HolderLookup.@NotNull Provider registries) {
        return saveCustomOnly(registries);
    }

    @Override
    public void setChanged() {
        super.setChanged();
        if (level != null) {
            level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), UPDATE_CLIENTS);
        }
    }
}
