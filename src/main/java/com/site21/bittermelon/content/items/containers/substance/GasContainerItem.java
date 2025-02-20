package com.site21.bittermelon.content.items.containers.substance;

import com.site21.bittermelon.content.atmosphere.AtmosHandler;
import com.site21.bittermelon.content.atmosphere.AtmosInstance;
import com.site21.bittermelon.content.items.base.ItemWeight;
import com.site21.bittermelon.content.items.containers.substance.data.SubstanceContents;
import com.site21.bittermelon.content.substance.SubstanceStack;
import com.site21.bittermelon.util.SubstanceUtils;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;
import java.util.List;

import static com.site21.bittermelon.init.BitterDataComponents.RELEASE_PRESSURE;
import static com.site21.bittermelon.init.Substances.GASEOUS_OXYGEN;

public class GasContainerItem extends SubstanceContainerItem {
    private final int maxPressure;
    private final int maxReleasePressure;

    public GasContainerItem(Properties properties, int width, int height, ItemWeight itemWeight, int capacity, int maxPressure, int maxReleasePressure) {
        super(properties, width, height, itemWeight, capacity);
        this.maxPressure = maxPressure;
        this.maxReleasePressure = maxReleasePressure;
    }

    public int getReleasePressure(@NotNull ItemStack stack) {
        return stack.getOrDefault(RELEASE_PRESSURE.get(), 0);
    }

    public void setReleasePressure(@NotNull ItemStack stack, int releasePressure) {
        stack.set(RELEASE_PRESSURE.get(), Mth.clamp(releasePressure, 0, maxReleasePressure));
    }

    public int getMaxReleasePressure() {
        return maxReleasePressure;
    }

    @Override
    public int getCapacity() {
        return maxPressure;
    }

    @Override
    public int getCapacity(@NotNull ItemStack stack) {
        if (stack.getItem() instanceof SubstanceContainerItem item) {
            return maxPressure;
        }

        return 0;
    }

    public float getPressure(ItemStack stack) {
        return SubstanceUtils.getPressure(getSubstanceData(stack).substances, capacity, getTemperature());
    }

    @Override
    public int getBarWidth(@NotNull ItemStack stack) {
        return Math.min((int) ((long) getPressure(stack) * MAX_BAR_WIDTH / getCapacity()), MAX_BAR_WIDTH);
    }

    @Override
    public int getBarColor(@NotNull ItemStack stack) {
        float pressure = getPressure(stack);
        float fillPercentage = pressure / getCapacity();

        if (pressure > getCapacity()) {
            // Purple/magenta color
            return 0xFF00FF;
        }

        if (fillPercentage < 0.5f) {
            return 0xFF0000 | (Math.round(510 * fillPercentage) << 8); // Red to Yellow
        } else {
            return 0x00FF00 | (Math.round(510 * (1 - fillPercentage)) << 16); // Yellow to Green
        }
    }

    @Override
    public void inventoryTick(@NotNull ItemStack stack, @NotNull Level level, @NotNull Entity entity, int slotId, boolean isSelected) {
        super.inventoryTick(stack, level, entity, slotId, isSelected);

        if (level.isClientSide) return;

        if (getPressure(stack) > maxPressure * 1.10f) {
            level.explode(entity, entity.getX(), entity.getY(), entity.getZ(),
                    2.0f,
                    Level.ExplosionInteraction.TNT);

            AtmosHandler.releaseGas(level, entity.getOnPos(), getContents(stack));
            stack.setCount(0);
        }

        if (getReleasePressure(stack) > 0 && getTotalAmount(stack) > 0) {
            transferGas(stack, level, entity);
        }
    }

    public void transferGas(ItemStack stack, @NotNull Level level, @NotNull Entity entity) {
        if (level.isClientSide) return;

        AtmosInstance atmosInstance = AtmosHandler.getAtmosInstanceAt(level, entity.getOnPos());
        if (atmosInstance == null) {
            AtmosHandler.addAtmosphere(level, entity.getOnPos(), 293.15f, List.of(new SubstanceStack(GASEOUS_OXYGEN.get(), 1)));
            atmosInstance = AtmosHandler.getAtmosInstanceAt(level, entity.getOnPos());
        }

        if (atmosInstance == null || atmosInstance.getPressure() <= 0 || getReleasePressure(stack) < atmosInstance.getPressure()) return;

        float totalVolume = getTotalVolume(stack);
        float transferVolume = ((getReleasePressure(stack) * totalVolume) / atmosInstance.getPressure()) / 100;

        // TODO: Propulsion when transfer volume is too high

        SubstanceContents.Mutable mutableData = getMutableSubstanceData(stack);
        Iterator<SubstanceStack> iterator = mutableData.substances.iterator();

        while (iterator.hasNext()) {
            SubstanceStack substance = iterator.next();
            if (substance == null) continue;

            float proportion = totalVolume > 0 ? substance.getVolume() / totalVolume : 0;
            float proportionalTransferVolume = Math.min(transferVolume * proportion, substance.getVolume());

            if (proportionalTransferVolume > 0) {
                SubstanceStack transferredSubstance = substance.copy();
                transferredSubstance.setVolume(proportionalTransferVolume);
                atmosInstance.updateGas(transferredSubstance, entity.level());

                substance.modifyVolume(-proportionalTransferVolume);
                if (substance.getVolume() <= 0.001f) {
                    iterator.remove();
                }
            }
        }

        setSubstanceDataFromMutable(stack, mutableData);
    }
}