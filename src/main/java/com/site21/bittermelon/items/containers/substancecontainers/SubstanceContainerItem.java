package com.site21.bittermelon.items.containers.substancecontainers;

import com.site21.bittermelon.items.base.BaseItem;
import com.site21.bittermelon.items.base.ItemSize;
import com.site21.bittermelon.items.base.ItemWeight;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

import static com.site21.bittermelon.init.DataComponentsInit.SUBSTANCE_CONTAINER_DATA;

public class SubstanceContainerItem extends BaseItem {
    private final int capacity;
    private final Map<Supplier<ResourceLocation>, Float> initialSubstances;

    public SubstanceContainerItem(Properties properties, ItemSize itemSize, ItemWeight itemWeight, int capacity) {
        super(properties, itemSize, itemWeight);
        this.capacity = capacity;
        this.initialSubstances = new HashMap<>();
        initializeSubstances();
    }

    private void initializeSubstances() {
        for (Map.Entry<Supplier<ResourceLocation>, Float> entry : initialSubstances.entrySet()) {
        }
    }

    public SubstanceContainerItem addInitialSubstance(Supplier<ResourceLocation> substance, float amount) {
        this.initialSubstances.put(substance, amount);
        return this;
    }

    public static SubstanceContainerData getSubstanceData(ItemStack stack) {
        return stack.getOrDefault(SUBSTANCE_CONTAINER_DATA.get(), SubstanceContainerData.empty());
    }

    public void updateSubstance(ItemStack stack, ResourceLocation substance, float amount) {
        getSubstanceData(stack).addAmount(substance, amount);
    }

    public Map<ResourceLocation, Float> getContents(ItemStack stack) {
        return getSubstanceData(stack).substances();
    }

    @Override
    public boolean isBarVisible(@NotNull ItemStack stack) {return true;}

    @Override
    public int getBarWidth(@NotNull ItemStack stack) {
        return (int) ((long) getSubstanceData(stack).getTotalAmount() * MAX_BAR_WIDTH / capacity);
    }

    @Override
    public int getBarColor(@NotNull ItemStack stack) {
        float fillPercentage = getSubstanceData(stack).getTotalAmount() / capacity;
        if (fillPercentage < 0.5f) {
            return 0xFF0000 | (Math.round(510 * fillPercentage) << 8); // Red to Yellow
        } else {
            return 0x00FF00 | (Math.round(510 * (1 - fillPercentage)) << 16);  // Yellow to Green
        }
    }

    protected Component getFlavorMessageComponent(ItemStack stack) {
        Map<ResourceLocation, Float> substances = getSubstanceData(stack).substances();
        float totalAmount = getSubstanceData(stack).getTotalAmount();

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
}
