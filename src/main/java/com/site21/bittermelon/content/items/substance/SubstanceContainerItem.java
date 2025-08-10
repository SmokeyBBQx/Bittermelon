package com.site21.bittermelon.content.items.substance;

import com.site21.bittermelon.content.items.base.BaseItem;
import com.site21.bittermelon.content.items.base.ItemWeight;
import com.site21.bittermelon.content.items.substance.data.SubstanceContents;
import com.site21.bittermelon.content.substance.Substance;
import com.site21.bittermelon.content.substance.SubstanceStack;
import com.site21.bittermelon.content.substance.reactions.ReactionContainer;
import com.site21.bittermelon.util.ColorUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.HoverEvent;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.site21.bittermelon.init.neoforge.BitterDataComponents.*;

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

    public SubstanceContents getSubstanceData(@NotNull ItemStack stack) {
        return stack.getOrDefault(SUBSTANCE_CONTENTS.get(), SubstanceContents.EMPTY);
    }

    public void setSubstanceDataFromMutable(@NotNull ItemStack stack, SubstanceContents.@NotNull Mutable mutableData) {
        stack.set(SUBSTANCE_CONTENTS.get(), mutableData.toImmutable());
        updateVisuals(stack);
    }

    public SubstanceContents.Mutable getMutableSubstanceData(@NotNull ItemStack stack) {
        SubstanceContents substanceContents = stack.getOrDefault(SUBSTANCE_CONTENTS.get(), SubstanceContents.EMPTY);
        return substanceContents.toMutable();
    }

    public List<SubstanceStack> getContents(ItemStack stack) {
        return getSubstanceData(stack).substances();
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

    public ItemStack consumeSubstances(ItemStack stack, float consumeRate, LivingEntity entity) {
        float totalAmount = getTotalVolume(stack);
        SubstanceContents.Mutable mutableData = getMutableSubstanceData(stack);
        Iterator<SubstanceStack> iterator = mutableData.substances.iterator();

        while (iterator.hasNext()) {
            SubstanceStack substance = iterator.next();

            float proportion = totalAmount > 0 ? substance.getVolume() / totalAmount : 0;
            float consumeAmount = Math.min(consumeRate * proportion, substance.getVolume());

            SubstanceStack consumedSubstance = substance.copy();
            consumedSubstance.setVolume(consumeAmount);
            consumedSubstance.getSubstance().onConsume(consumedSubstance, entity);

            substance.modifyVolume(-consumeAmount);

            if (substance.getVolume() <= consumeAmount) {
                iterator.remove();
            }
        }

        setSubstanceDataFromMutable(stack, mutableData);
        return stack;
    }

    public int getColor(@NotNull ItemStack stack) {
        int color = stack.getOrDefault(COLOR, -1);

        if (color != -1) return color;

        return updateColor(stack);
    }

    public int updateColor(ItemStack stack) {
        int color;

        Map<Integer, Float> colors = new HashMap<>();
        for (SubstanceStack substance : getContents(stack)) {
            colors.put(substance.getSubstance().getColor(), substance.getAmount());
        }

        color = colors.isEmpty() ? 0xFFFFFFFF : ColorUtil.mixColors(colors);
        stack.set(COLOR, color);

        return color;
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
        return getSensoryMessageComponent(
                stack,
                "Tastes",
                "Tastes like...",
                "Tastes like a noticeable blend of",
                "notes.",
                "Tastes strongly",
                Substance::getFlavor);
    }

    protected Component getSmellMessageComponent(ItemStack stack) {
        return getSensoryMessageComponent(
                stack,
                "Smells",
                "Smells...",
                "Smells noticeably",
                "",
                "Smells strongly",
                Substance::getSmell);
    }

    private Component getSensoryMessageComponent(ItemStack stack, String singleVerb, String multipleMain, String mediumPrefix, String mediumSuffix, String strongPrefix, Function<Substance, String> propertyGetter) {
        List<SubstanceStack> substances = getContents(stack);
        float totalVolume = getSubstanceData(stack).getTotalVolume();

        if (totalVolume == 0 || substances.isEmpty()) {
            return Component.empty();
        }

        if (substances.size() == 1) {
            String property = propertyGetter.apply(substances.getFirst().getSubstance());
            if (property.isEmpty()) return Component.empty();

            String description = singleVerb + " " + property + ".";
            return Component.literal(description).withStyle(ChatFormatting.GREEN);
        }

        MutableComponent mainComponent = Component.literal(multipleMain).withStyle(ChatFormatting.GREEN);

        String hoverText = substances.stream()
                .filter(substance -> {
                    String property = propertyGetter.apply(substance.getSubstance());
                    return property != null && !property.trim().isEmpty();
                })
                .map(substance -> {
                    float volume = substance.getVolume();
                    float percentageAmount = volume / totalVolume * 100;
                    String property = propertyGetter.apply(substance.getSubstance());

                    if (percentageAmount <= 35) {
                        return "Has a faint hint of " + property + " tones.";
                    } else if (percentageAmount <= 65) {
                        return mediumPrefix + " " + property + " " + mediumSuffix;
                    } else {
                        return strongPrefix + " " + property + ".";
                    }
                })
                .collect(Collectors.joining("\n"));

        return mainComponent.setStyle(mainComponent.getStyle().withHoverEvent(
                new HoverEvent(HoverEvent.Action.SHOW_TEXT, Component.literal(hoverText))));
    }

    private void updateVisuals(@NotNull ItemStack stack) {
        updateColor(stack);
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
