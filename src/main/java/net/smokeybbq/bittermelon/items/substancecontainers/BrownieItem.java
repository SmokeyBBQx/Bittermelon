package net.smokeybbq.bittermelon.items.substancecontainers;

import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.smokeybbq.bittermelon.init.ModCapabilities;
import net.smokeybbq.bittermelon.substances.Substance;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;

import static net.smokeybbq.bittermelon.init.SubstanceInit.*;

public class BrownieItem extends SubstanceSolidItem {
    private static final int capacity = 50;
    public BrownieItem(Properties pProperties) {
        super(pProperties, capacity);
    }

    @Override
    public @Nullable ICapabilityProvider initCapabilities(ItemStack stack, @Nullable CompoundTag nbt) {
        return new ICapabilitySerializable<CompoundTag>() {
            private final LazyOptional<ISubstanceContainer> container = LazyOptional.of(() -> {
                ISubstanceContainer cont = new SubstanceContainerCapability(capacity);
                if (nbt == null) {
                    // Initialize with default values if no NBT data
                    cont.updateSubstance(BROWNIE_MIXTURE.get(), 40);
                    cont.updateSubstance(HASHISH.get(), 10);
                }
                return cont;
            });

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

}