package com.site21.bittermelon.content.items.containers.substance;

import com.site21.bittermelon.content.items.base.BaseItem;
import com.site21.bittermelon.content.items.base.ItemWeight;
import com.site21.bittermelon.content.items.containers.substance.data.SubstanceContents;
import com.site21.bittermelon.content.substance.SubstanceStack;
import com.site21.bittermelon.content.substance.reactions.ReactionContainer;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import static com.site21.bittermelon.init.neoforge.BitterDataComponents.LAST_UPDATED;
import static com.site21.bittermelon.init.neoforge.BitterDataComponents.SUBSTANCE_CONTENTS;

public class SubstanceContainerItem extends BaseItem implements ReactionContainer {
    protected final int capacity;
    private List<SubstanceStack> initialSubstances = new ArrayList<>();

    public SubstanceContainerItem(Properties properties, int width, int height, ItemWeight itemWeight, int capacity) {
        super(properties, width, height, itemWeight);
        this.capacity = capacity;
        initializeSubstances();
    }

    private void initializeSubstances() {
        for (SubstanceStack substance : initialSubstances) {
        }
    }

    public SubstanceContainerItem addInitialSubstance(SubstanceStack substance) {
        this.initialSubstances.add(substance);
        return this;
    }

    public void setInitialSubstances(List<SubstanceStack> initialSubstances) {
        this.initialSubstances = initialSubstances;
    }

    public SubstanceContents getSubstanceData(ItemStack stack) {
        return stack.getOrDefault(SUBSTANCE_CONTENTS.get(), SubstanceContents.EMPTY);
    }

    public void setSubstanceDataFromMutable(ItemStack stack, SubstanceContents.Mutable mutableData) {
        stack.set(SUBSTANCE_CONTENTS.get(), mutableData.toImmutable());
        updateVisuals(stack);
    }

    public SubstanceContents.Mutable getMutableSubstanceData(ItemStack stack) {
        SubstanceContents substanceContents = stack.getOrDefault(SUBSTANCE_CONTENTS.get(), SubstanceContents.EMPTY);
        return substanceContents.toMutable();
    }

    public List<SubstanceStack> getContents(ItemStack stack) {
        return getSubstanceData(stack).substances;
    }

    public float getTotalAmount(ItemStack stack) {
        return getSubstanceData(stack).getTotalAmount();
    }

    public float getTotalVolume(ItemStack stack) {
        return getSubstanceData(stack).getTotalVolume();
    }

    public void updateSubstance(ItemStack itemStack, SubstanceStack substanceStack) {
        if (substanceStack != null) {
            SubstanceContents.Mutable mutableData = getMutableSubstanceData(itemStack);
            mutableData.updateSubstance(substanceStack);
            setSubstanceDataFromMutable(itemStack, mutableData);
        }
    }

    public int getCapacity() {
        return capacity;
    }

    public int getCapacity(@NotNull ItemStack stack) {
        if (stack.getItem() instanceof SubstanceContainerItem item) {
            return item.getCapacity();
        }

        return 0;
    }

    public boolean isContainerEmpty(ItemStack stack) {
        return getContents(stack).isEmpty();
    }

    @Override
    public boolean isBarVisible(@NotNull ItemStack stack) {
        return true;
    }

    @Override
    public int getBarWidth(@NotNull ItemStack stack) {
        return (int) ((long) getSubstanceData(stack).getTotalVolume() * MAX_BAR_WIDTH / capacity);
    }

    @Override
    public int getBarColor(@NotNull ItemStack stack) {
        float fillPercentage = getSubstanceData(stack).getTotalVolume() / capacity;
        if (fillPercentage < 0.5f) {
            return 0xFF0000 | (Math.round(510 * fillPercentage) << 8); // Red to Yellow
        } else {
            return 0x00FF00 | (Math.round(510 * (1 - fillPercentage)) << 16);  // Yellow to Green
        }
    }

    protected Component getFlavorMessageComponent(ItemStack stack) {
        List<SubstanceStack> substances = getContents(stack);
        float totalAmount = getSubstanceData(stack).getTotalVolume();

        if (totalAmount == 0 || substances.isEmpty()) {
            return Component.literal("No discernible flavor.");
        }

        if (substances.size() == 1) {
//            Map.Entry<ResourceLocation, Integer> entry = substances.entrySet().iterator().next();
//            ResourceLocation substance = (ResourceLocation) entry.getKey();
//
//            String flavorDescription = "Tastes " + substance.getFlavor() + ".";
//
//            return Component.literal(flavorDescription).withStyle(ChatFormatting.GREEN);
        }
        return null;
    }

    private void updateVisuals(@NotNull ItemStack stack) {
        stack.set(LAST_UPDATED.get(), System.currentTimeMillis());
    }

    @Override
    public void inventoryTick(@NotNull ItemStack stack, @NotNull Level level, @NotNull Entity entity, int slotId, boolean isSelected) {
        if (stack.getItem() instanceof ReactionContainer container) {
//            ReactionHandler.getInstance().handleReactions(getContents(stack), container);
        }
    }

    @Override
    public float getTemperature() {
        return 273.15f;
    }

    @Override
    public float getHeatCapacity() {
        return 0;
    }

    @Override
    public void modifyTemperature(float temperature) {

    }

    @Override
    public void updateSubstance(SubstanceStack stack) {

    }
}
