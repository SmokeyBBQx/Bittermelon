package com.site21.bittermelon.common.systems.medical.compartment;

import com.mojang.serialization.Codec;
import com.site21.bittermelon.Bittermelon;
import com.site21.bittermelon.common.systems.medical.compartment.layer.LayerData;
import com.site21.bittermelon.common.systems.medical.compartment.layer.Point;
import com.site21.bittermelon.common.systems.medical.medicalstats.MedicalStats;
import com.site21.bittermelon.init.custom.Compartments;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.component.PatchedDataComponentMap;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.neoforged.neoforge.common.CommonHooks;
import org.jetbrains.annotations.NotNull;

import java.util.EnumMap;
import java.util.EnumSet;
import java.util.List;
import java.util.UUID;

import static com.site21.bittermelon.init.neoforge.BitterDataComponents.*;
import static com.site21.bittermelon.init.neoforge.BitterRegistries.COMPARTMENT_REGISTRY;
import static com.site21.bittermelon.init.neoforge.BitterRegistries.COMPARTMENT_REGISTRY_KEY;

public class Compartment {
    public static final Codec<Holder<Compartment>> CODEC = COMPARTMENT_REGISTRY.holderByNameCodec();
    public static final StreamCodec<RegistryFriendlyByteBuf, Holder<Compartment>> STREAM_CODEC = ByteBufCodecs.holderRegistry(COMPARTMENT_REGISTRY_KEY);

    private final String id;
    private final DataComponentMap components;

    public Compartment(String id, @NotNull Properties properties) {
        this.id = id;
        components = properties.components.build();
    }

    public CompartmentInstance toInstance() {
        return new CompartmentInstance(
                this,
                UUID.randomUUID(),
                id,
                new PatchedDataComponentMap(components())
        );
    }

    public void tick(MedicalStats medicalStats, @NotNull CompartmentInstance instance) {}

    public void onExtract(MedicalStats medicalStats, CompartmentInstance instance) {}

    public boolean canExtract(CompartmentInstance instance, MedicalStats medicalStats) {
        return instance.getCompartment() != Compartments.CUT.get();
    }

//    public ItemStack createItemStack(@NotNull CompartmentInstance instance) {
//        ItemStack stack = properties.item.value().getDefaultInstance();
//        stack.set(BitterDataComponents.COMPARTMENT, instance.toData());
//        return stack;
//    }

    public Holder<Compartment> builtInRegistryHolder() {
        return COMPARTMENT_REGISTRY.get(ResourceLocation.fromNamespaceAndPath(Bittermelon.MOD_ID, id)).orElseThrow();
    }

    public DataComponentMap components() {
        return components;
    }

    public static class Properties {
        EnumSet<CompartmentTag> defaultTags = EnumSet.noneOf(CompartmentTag.class);
        Holder<Item> item = Items.AIR.builtInRegistryHolder();
        float defaultHealth = 0;
        private final DataComponentMap.Builder components = DataComponentMap.builder();

        public Properties defaultTags(EnumSet<CompartmentTag> defaultTags) {
            this.defaultTags = defaultTags;
            return this;
        }

        public Properties defaultTags(CompartmentTag @NotNull ... tags) {
            this.defaultTags = tags.length > 0 ? EnumSet.of(tags[0], tags) : EnumSet.noneOf(CompartmentTag.class);
            return this;
        }

        public Properties defaultAttributes(EnumMap<MedicalAttribute, Float> defaultAttributes) {
            components.set(MEDICAL_ATTRIBUTES, defaultAttributes);
            return this;
        }

        public Properties addAttribute(MedicalAttribute attribute, float value) {
            // TODO: Add to components
            return this;
        }

        public Properties addAttribute(MedicalAttribute attribute) {
//            this.defaultAttributes.put(attribute, 1.0f);
            // TODO: Add to components
            return this;
        }

        public Properties layers(LayerData... layers) {
            components.set(LAYERS, List.of(layers));
            return this;
        }

        public Properties shape(List<Point> shape) {
            components.set(SHAPE, shape);
            return this;
        }

        public Properties pivot(Point pivot) {
            components.set(PIVOT, pivot);
            return this;
        }

        public Properties item(Holder<Item> item) {
            this.item = item;
            return this;
        }

        public Properties defaultHealth(float defaultHealth) {
            this.defaultHealth = defaultHealth;
            return this;
        }

        public Properties visualData(VisualData visualData) {
            components.set(VISUAL_DATA, visualData);
            return this;
        }

        public <T> Properties component(DataComponentType<T> component, T value) {
            CommonHooks.validateComponent(component);
            components.set(component, value);
            return this;
        }
    }
}
