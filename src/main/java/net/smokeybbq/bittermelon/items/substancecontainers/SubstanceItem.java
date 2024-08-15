package net.smokeybbq.bittermelon.items.substancecontainers;

import net.minecraft.ChatFormatting;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.HoverEvent;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.smokeybbq.bittermelon.init.ModCapabilities;
import net.smokeybbq.bittermelon.items.base.BaseItem;
import net.smokeybbq.bittermelon.substances.Substance;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.Map;
import java.util.stream.Collectors;

public abstract class SubstanceItem extends BaseItem {
    private final int capacity;
    public SubstanceItem(Properties pProperties, int capacity) {
        super(pProperties);
        this.capacity = capacity;
    }

    protected LazyOptional<ISubstanceContainer> getSubstanceContainer(ItemStack stack) {
        return stack.getCapability(ModCapabilities.SUBSTANCE_CONTAINER_CAPABILITY);
    }

    @Override
    public @Nullable ICapabilityProvider initCapabilities(ItemStack stack, @Nullable CompoundTag nbt) {
        return new ICapabilitySerializable<CompoundTag>() {
            final LazyOptional<ISubstanceContainer> container = LazyOptional.of(() -> new SubstanceContainerCapability(capacity));

            @Override
            public <T> @NotNull LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
                return ModCapabilities.SUBSTANCE_CONTAINER_CAPABILITY.orEmpty(cap, container);
            }

            @Override
            public CompoundTag serializeNBT() {
                return container.map(ISubstanceContainer::serializeNBT).orElse(new CompoundTag());
            }

            @Override
            public void deserializeNBT(CompoundTag nbt) {
                container.ifPresent(c -> c.deserializeNBT(nbt));
            }
        };
    }

    public void updateSubstance(ItemStack stack, Substance substance, int amount) {
        getSubstanceContainer(stack).ifPresent(cap -> {
            boolean updated = false;
            for (Map.Entry<Substance, Integer> entry : cap.getSubstances().entrySet()) {
                if (entry.getKey().getName().equals(substance.getName())) {
                    cap.updateSubstance(entry.getKey(), amount);
                    updated = true;
                    break;
                }
            }
            if (!updated) {
                cap.updateSubstance(substance, amount);
            }
            updateVisuals(stack);
        });
    }

    public Map<Substance, Integer> getContents(ItemStack stack) {
        return getSubstanceContainer(stack)
                .map(ISubstanceContainer::getSubstances)
                .orElse(Collections.emptyMap());
    }

    protected void updateVisuals(ItemStack stack) {
        CompoundTag tag = stack.getOrCreateTag();
        tag.putLong("LastUpdate", System.currentTimeMillis());
    }

    @Override
    public boolean isBarVisible(@NotNull ItemStack stack) {
        return true;
    }

    @Override
    public int getBarWidth(@NotNull ItemStack stack) {
        return getSubstanceContainer(stack)
                .map(cap -> (int) ((long) cap.getTotalAmount() * MAX_BAR_WIDTH / capacity))
                .orElse(0);
    }

    @Override
    public int getBarColor(@NotNull ItemStack stack) {
        return getSubstanceContainer(stack)
                .map(cap -> {
                    float fillPercentage = (float) cap.getTotalAmount() / capacity;
                    if (fillPercentage < 0.5f) {
                        return 0xFF0000 | (Math.round(510 * fillPercentage) << 8);  // Red to Yellow
                    } else {
                        return 0x00FF00 | (Math.round(510 * (1 - fillPercentage)) << 16);  // Yellow to Green
                    }
                })
                .orElse(0xFF0000);  // Default to red if capability is not present
    }

    protected Component getFlavorMessageComponent(ItemStack itemStack) {
        return getSubstanceContainer(itemStack).map(cap -> {
            Map<Substance, Integer> substances = cap.getSubstances();
            int totalAmount = cap.getTotalAmount();

            if (totalAmount == 0 || substances.isEmpty()) {
                return Component.literal("No discernible flavor.");
            }

            if (substances.size() == 1) {
                Map.Entry<Substance, Integer> entry = substances.entrySet().iterator().next();
                Substance substance = entry.getKey();

                String flavorDescription = "Tastes " + substance.getFlavor() + ".";

                return Component.literal(flavorDescription).withStyle(ChatFormatting.GREEN);
            }

            MutableComponent mainComponent = Component.literal("Tastes like...").withStyle(ChatFormatting.GREEN);

            String hoverText = substances.entrySet().stream()
                    .map(entry -> {
                        Substance substance = entry.getKey();
                        int amount = entry.getValue();
                        float percentageAmount = (float) amount / totalAmount * 100;
                        String flavorDescription;

                        if (percentageAmount <= 35) {
                            flavorDescription = "Has a faint hint of " + substance.getFlavor() + " tones.";
                        } else if (percentageAmount <= 65) {
                            flavorDescription = "Tastes like a noticeable blend of " + substance.getFlavor() + " notes.";
                        } else {
                            flavorDescription = "Tastes strongly " + substance.getFlavor() + ".";
                        }

                        return flavorDescription;
                    })
                    .collect(Collectors.joining("\n"));

            return mainComponent.setStyle(mainComponent.getStyle().withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, Component.literal(hoverText))));
        }).orElse(Component.literal("Unable to determine flavor."));
    }
}
