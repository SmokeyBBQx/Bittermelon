package com.site21.bittermelon.common.systems.fluid;

import com.site21.bittermelon.common.systems.substance.SubstanceStack;
import com.site21.bittermelon.util.ColorUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.util.Mth;
import net.minecraft.world.level.block.LiquidBlockContainer;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.site21.bittermelon.init.neoforge.BitterBlockEntities.SUBSTANCE_FLUID_BLOCK_ENTITY;
import static com.site21.bittermelon.init.neoforge.BitterFluids.SUBSTANCE_FLUID;
import static net.minecraft.world.level.block.Block.UPDATE_ALL;

public class SubstanceFluidBlockEntity extends BlockEntity {
    private final List<SubstanceStack> substances;
    private float cachedVolume;
    private int cachedColor;

    public SubstanceFluidBlockEntity(BlockPos pos, BlockState blockState) {
        super(SUBSTANCE_FLUID_BLOCK_ENTITY.get(), pos, blockState);

        substances = new ArrayList<>();
        cachedVolume = -1f;
        cachedColor = -1;
    }

    public void updateSubstance(SubstanceStack substance) {
        // Merge with existing substances if possible
        for (SubstanceStack stack : substances) {
            if (stack.canMergeWith(substance)) {
                stack.modifyAmount(substance.getAmount());
                updateFluidState();
                setChanged();
                return;
            }
        }

        // Otherwise, add as a new substance
        substances.add(substance);
        setChanged();
        updateFluidState();
    }

    public void transferSubstances(@NotNull List<SubstanceStack> substances) {
        for (SubstanceStack stack : substances) {
            updateSubstance(stack.copy());
        }
    }

    public void removeSubstance(SubstanceStack substance, float amount) {
        for (SubstanceStack stack : substances) {
            if (substance.canMergeWith(stack)) {
                stack.modifyAmount(-amount);
                if (stack.getAmount() <= 0) {
                    substances.remove(stack);
                }
                setChanged();
                updateFluidState();
                return;
            }
        }
    }

    public List<SubstanceStack> getSubstances() {
        return substances;
    }

    public void setSubstances(List<SubstanceStack> newSubstances) {
        substances.clear();
        substances.addAll(newSubstances);
        setChanged();
        updateFluidState();
    }

    public void mergeSubstances(@NotNull List<SubstanceStack> substances, SubstanceStack stack) {
        for (SubstanceStack substance : substances) {
            if (stack.canMergeWith(substance)) {
                substance.modifyVolume(stack.getVolume());
                return;
            }
        }

        substances.add(stack);
    }

    private void updateFluidState() {
        if (level == null) return;
        if (level.getBlockState(worldPosition).isAir()) return;
        int fluidLevel = Math.max(1, (int) Mth.clamp(getVolume(), 1, 15));

        BlockState currentState = level.getBlockState(worldPosition);
        BlockState newState = currentState.setValue(SubstanceFluidBlock.LEVEL, fluidLevel);

        level.setBlock(worldPosition, newState, UPDATE_ALL);
        level.scheduleTick(worldPosition, SUBSTANCE_FLUID.get(), SUBSTANCE_FLUID.get().getTickDelay(level));
    }

    public float getVolume() {
        if (cachedVolume == -1f) {
            cachedVolume = substances.stream()
                    .map(SubstanceStack::getVolume)
                    .reduce(0f, Float::sum);
        }

        return cachedVolume;
    }

    public int getColor() {
        if (cachedColor == -1) {
            if (substances.isEmpty()) {
                cachedColor = 0xFFAAD5DB; // Default color if no substances
            } else {
                Map<Integer, Float> colors = new HashMap<>();
                // Create a copy to avoid concurrent modification
                List<SubstanceStack> substancesCopy = new ArrayList<>(substances);
                for (SubstanceStack stack : substancesCopy) {
                    colors.put(stack.getSubstance().getColor(), stack.getVolume());
                }
                cachedColor = ColorUtil.mixColors(colors);
            }
        }
        return cachedColor;
    }

    public String getContentsDescription() {
        if (substances.isEmpty()) {
            return "Empty";
        }

        return substances.stream()
                .map(entry -> String.format("%s: %f", entry.getSubstance().getName(), entry.getAmount()))
                .collect(Collectors.joining(", "));
    }

    @Override
    protected void saveAdditional(@NotNull ValueOutput output) {
        super.saveAdditional(output);

        ValueOutput.TypedOutputList<SubstanceStack> substancesList = output.list("substances", SubstanceStack.CODEC);
        for (SubstanceStack stack : substances) {
            substancesList.add(stack);
        }
        output.putInt("cachedColor", cachedColor);
    }

    @Override
    protected void loadAdditional(@NotNull ValueInput input) {
        super.loadAdditional(input);

        substances.clear();
        ValueInput.TypedInputList<SubstanceStack> substancesList = input.listOrEmpty("substances", SubstanceStack.CODEC);
        for (SubstanceStack stack : substancesList) {
            substances.add(stack);
        }
        cachedColor = input.getIntOr("cachedColor", -1);
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
        cachedVolume = -1f;
        cachedColor = -1;
        requestModelDataUpdate();
    }
}
