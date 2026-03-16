package com.site21.bittermelon.common.systems.fluid.substance;

import com.site21.bittermelon.common.systems.substance.Substance;
import com.site21.bittermelon.common.systems.substance.SubstanceStack;
import com.site21.bittermelon.common.systems.substance.reactions.Reaction;
import com.site21.bittermelon.common.systems.substance.reactions.ReactionManager;
import com.site21.bittermelon.common.systems.substance.reactions.Reactor;
import com.site21.bittermelon.util.ColorUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.util.Mth;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.stream.Collectors;

import static com.site21.bittermelon.init.neoforge.BitterBlockEntities.SUBSTANCE_FLUID_BLOCK_ENTITY;
import static com.site21.bittermelon.init.neoforge.BitterFluids.SUBSTANCE_FLUID;
import static net.minecraft.world.level.block.Block.UPDATE_ALL;

public class SubstanceFluidBlockEntity extends BlockEntity implements Reactor {
    private final List<SubstanceStack> substances;
    private final List<Reaction> activeReactions;
    private final List<Reaction> cachedReactions;
    private boolean reactionsDirty;
    private boolean searchDirty;
    private int cachedVolume;
    private int cachedColor;
    private int cachedAmount;
    private int cachedViscosity;

    public SubstanceFluidBlockEntity(BlockPos pos, BlockState blockState) {
        super(SUBSTANCE_FLUID_BLOCK_ENTITY.get(), pos, blockState);

        substances = new ArrayList<>();
        activeReactions = new ArrayList<>();
        cachedReactions = new ArrayList<>();
        reactionsDirty = false;
        cachedVolume = -1;
        cachedColor = -1;
        cachedAmount = -1;
        cachedViscosity = -1;
    }

    public void tickReactions() {
        if (searchDirty) {
            searchReactions();
            searchDirty = false;
            reactionsDirty = false;
        } else if (reactionsDirty) {
            refreshReactions();
            reactionsDirty = false;
        }

        Iterator<Reaction> iterator = activeReactions.iterator();
        while (iterator.hasNext()) {
            Reaction reaction = iterator.next();
            if (!reaction.react(this, level, worldPosition)) {
                cachedReactions.add(reaction);
                iterator.remove();
            }
        }
    }

    /**
     * Reactions are cached when they fail to react, which can happen if their conditions are no longer met.
     * This method moves all cached reactions back to the active list, allowing them to be re-evaluated in the next tick.
     * This is important to ensure that reactions can occur again if the conditions become favorable,
     * without needing to be re-searched.
     */
    public void refreshReactions() {
        activeReactions.addAll(cachedReactions);
        cachedReactions.clear();
    }

    public void searchReactions() {
        activeReactions.clear();
        cachedReactions.clear();
        if (substances.isEmpty()) return;

        ReactionManager reactionManager = ReactionManager.getInstance();
        activeReactions.addAll(reactionManager.findMatch(this, level, worldPosition));
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
        searchDirty = true;
    }

    public void updateSubstanceNoUpdate(SubstanceStack substance) {
        // Merge with existing substances if possible
        for (SubstanceStack stack : substances) {
            if (stack.canMergeWith(substance)) {
                stack.modifyAmount(substance.getAmount());
                return;
            }
        }

        // Otherwise, add as a new substance
        substances.add(substance);
        searchDirty = true;
    }

    public void transferSubstances(@NotNull List<SubstanceStack> substances) {
        for (SubstanceStack stack : substances) {
            updateSubstanceNoUpdate(stack.copy());
        }
        updateFluidState();
        setChanged();
    }

    public void removeSubstance(SubstanceStack substance, int amount) {
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

    public void removeSubstanceNoUpdate(SubstanceStack substance, int amount) {
        for (SubstanceStack stack : substances) {
            if (substance.canMergeWith(stack)) {
                stack.modifyAmount(-amount);
                if (stack.getAmount() <= 0) {
                    substances.remove(stack);
                }
                return;
            }
        }
    }

    public void removeSubstances(@NotNull List<SubstanceStack> substances) {
        for (SubstanceStack stack : substances) {
            removeSubstanceNoUpdate(stack, stack.getAmount());
        }
        setChanged();
        updateFluidState();
    }

    public void removeSubstances(List<SubstanceStack> substances, int multiplier) {
        for (SubstanceStack stack : substances) {
            removeSubstanceNoUpdate(stack, stack.getAmount() * multiplier);
        }
        setChanged();
        updateFluidState();
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

    public void updateFluidState() {
        if (level == null) return;
        if (level.getBlockState(worldPosition).isAir()) return;
        int fluidLevel = Math.max(1, Mth.clamp(getVolume() / 50, 1, 19));

        BlockState currentState = level.getBlockState(worldPosition);
        BlockState newState = currentState.setValue(SubstanceFluidBlock.LEVEL, fluidLevel);

        level.setBlock(worldPosition, newState, UPDATE_ALL);
        level.scheduleTick(worldPosition, SUBSTANCE_FLUID.get(), SUBSTANCE_FLUID.get().getTickDelay(level));
    }

    public int getVolume() {
        if (cachedVolume == -1f) {
            cachedVolume = 0;
            for (SubstanceStack stack : substances) {
                cachedVolume += stack.getVolume();
            }
        }

        return cachedVolume;
    }

    public int getAmount() {
        if (cachedAmount == -1f) {
            cachedAmount = 0;
            for (SubstanceStack stack : substances) {
                cachedAmount += stack.getAmount();
            }
        }

        return cachedAmount;
    }

    public int getColor() {
        if (cachedColor == -1) {
            if (substances.isEmpty()) {
                cachedColor = Substance.DEFAULT_COLOR;
            } else {
                Map<Integer, Integer> colors = new HashMap<>(substances.size());
                // Create a copy to avoid concurrent modification
                List<SubstanceStack> substancesCopy = new ArrayList<>(substances);
                for (SubstanceStack stack : substancesCopy) {
                    if (stack == null) continue;
                    colors.put(stack.getSubstance().getColor(), stack.getVolume());
                }
                cachedColor = ColorUtil.mixColors(colors);
            }
        }
        return cachedColor;
    }

    public int getViscosity() {
        if (cachedViscosity != -1) return cachedViscosity;

        int weightedSum = 0;
        int totalVolume = 0;

        for (SubstanceStack stack : substances) {
            int volume = stack.getVolume();
            int viscosity = stack.getSubstance().getViscosity();
            weightedSum += viscosity * volume;
            totalVolume += volume;
        }

        if (totalVolume <= 0) return 1000;


        cachedViscosity = weightedSum / totalVolume;
        return cachedViscosity;
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
        if (substances.isEmpty()) {
            return "Empty";
        }

        return substances.stream()
                .map(entry -> String.format("%s: %d", entry.getSubstance().getName(), entry.getAmount()))
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
    public void onLoad() {
        searchReactions();
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
        cachedVolume = -1;
        cachedColor = -1;
        cachedAmount = -1;
        cachedViscosity = -1;
        requestModelDataUpdate();
        reactionsDirty = true;
    }
}
