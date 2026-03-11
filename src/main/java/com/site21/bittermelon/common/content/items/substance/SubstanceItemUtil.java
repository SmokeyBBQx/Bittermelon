package com.site21.bittermelon.common.content.items.substance;

import com.site21.bittermelon.common.systems.component.SubstanceContents;
import com.site21.bittermelon.common.systems.substance.SubstanceStack;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;
import java.util.List;

import static com.site21.bittermelon.init.neoforge.BitterDataComponents.LAST_UPDATED;
import static com.site21.bittermelon.init.neoforge.BitterDataComponents.SUBSTANCE_CONTENTS;

public class SubstanceItemUtil {
    /**
     * Consumes substances from the container based on the consume rate and the proportion of each substance.
     * @param stack The ItemStack representing the substance container.
     * @param consumeRate The total amount to consume from the container.
     * @param entity The LivingEntity consuming the substances.
     * @return The updated ItemStack after consumption.
     */
    public static ItemStack consumeSubstances(ItemStack stack, int consumeRate, LivingEntity entity) {
        int totalAmount = getTotalVolume(stack);
        SubstanceContents.Mutable mutableData = getMutableSubstanceData(stack);
        Iterator<SubstanceStack> iterator = mutableData.substances.iterator();

        while (iterator.hasNext()) {
            SubstanceStack substance = iterator.next();

            int proportion = totalAmount > 0 ? substance.getVolume() / totalAmount : 0;
            int consumeAmount = Math.min(consumeRate * proportion, substance.getVolume());

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

    public static SubstanceContents.@NotNull Mutable getMutableSubstanceData(@NotNull ItemStack stack) {
        SubstanceContents substanceContents = stack.getOrDefault(SUBSTANCE_CONTENTS.get(), SubstanceContents.EMPTY);
        return substanceContents.toMutable();
    }

    public static List<SubstanceStack> getContents(ItemStack stack) {
        return getSubstanceData(stack).substances();
    }

    public static void setSubstanceDataFromMutable(@NotNull ItemStack stack, SubstanceContents.@NotNull Mutable mutableData) {
        stack.set(SUBSTANCE_CONTENTS.get(), mutableData.toImmutable());
        stack.set(LAST_UPDATED.get(), System.currentTimeMillis());
    }

    public static @NotNull SubstanceContents getSubstanceData(@NotNull ItemStack stack) {
        return stack.getOrDefault(SUBSTANCE_CONTENTS.get(), SubstanceContents.EMPTY);
    }
    public static int getTotalVolume(ItemStack stack) {
        return getSubstanceData(stack).getTotalVolume();
    }

}
