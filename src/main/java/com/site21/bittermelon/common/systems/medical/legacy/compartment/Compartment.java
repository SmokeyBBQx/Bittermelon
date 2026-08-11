package com.site21.bittermelon.common.systems.medical.legacy.compartment;

import com.mojang.serialization.Codec;
import com.site21.bittermelon.Bittermelon;
import com.site21.bittermelon.common.systems.medical.legacy.compartment.layer.LayerData;
import com.site21.bittermelon.common.systems.medical.legacy.compartment.layer.Point;
import com.site21.bittermelon.common.systems.medical.legacy.medicalstats.MedicalStats;
import com.site21.bittermelon.init.custom.Compartments;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.component.PatchedDataComponentMap;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.neoforged.neoforge.common.CommonHooks;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.UUID;
import java.util.function.Supplier;

import static com.site21.bittermelon.init.neoforge.BitterDataComponents.*;
import static com.site21.bittermelon.init.neoforge.BitterRegistries.COMPARTMENT_REGISTRY;
import static com.site21.bittermelon.init.neoforge.BitterRegistries.COMPARTMENT_REGISTRY_KEY;

public class Compartment {
    public static final Codec<Holder<Compartment>> CODEC = COMPARTMENT_REGISTRY.holderByNameCodec();
    public static final StreamCodec<RegistryFriendlyByteBuf, Holder<Compartment>> STREAM_CODEC = ByteBufCodecs.holderRegistry(COMPARTMENT_REGISTRY_KEY);

    private final String id;
    private final Properties properties;
    private final DataComponentMap components;

    public Compartment(String id, @NotNull Properties properties) {
        this.id = id;
        this.properties = properties.build();
        properties.components.set(DISPLAY_NAME, StringUtils.capitalize(id.replace("_", " ")));
        components = properties.components.build();
    }

    public CompartmentInstance toInstance() {
        CompartmentInstance instance = new CompartmentInstance(
                this,
                UUID.randomUUID(),
                id,
                new PatchedDataComponentMap(components())
        );

        instance.set(LAYERS, properties.defaultLayers.get());
        return instance;
    }

    protected boolean shouldTick(MedicalStats medicalStats, CompartmentInstance instance, long gameTime) {
        return false;
    }

    public void tick(MedicalStats medicalStats, @NotNull CompartmentInstance instance) {
    }

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
        return COMPARTMENT_REGISTRY.get(Identifier.fromNamespaceAndPath(Bittermelon.MOD_ID, id)).orElseThrow();
    }

    public DataComponentMap components() {
        return components;
    }

    public static class Properties {
        Holder<Item> item = Items.AIR.builtInRegistryHolder();
        Supplier<List<LayerData>> defaultLayers = List::of;
        private final EnumMap<MedicalAttribute, Float> attributes = new EnumMap<>(MedicalAttribute.class);
        private final DataComponentMap.Builder components = DataComponentMap.builder();

        public Properties defaultAttributes(EnumMap<MedicalAttribute, Float> defaultAttributes) {
            components.set(MEDICAL_ATTRIBUTES, defaultAttributes);
            return this;
        }

        public Properties addAttribute(MedicalAttribute attribute, float value) {
            this.attributes.put(attribute, value);
            return this;
        }

        public Properties addAttribute(MedicalAttribute attribute) {
            this.attributes.put(attribute, 1.0f);
            return this;
        }

        public Properties layers(LayerData... layers) {
            defaultLayers = () -> List.of(layers);
            return this;
        }

        public Properties shape(List<Point> shape) {
            components.set(SHAPE, shape);
            return this;
        }

        public Properties shapeOf(int width, int height) {
            List<Point> shape = new ArrayList<>();
            for (int i = 0; i < height; ++i) {
                for (int j = 0; j < width; ++j) {
                    shape.add(new Point(j, i));
                }
            }
            return shape(shape);
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
            // TODO: Implement
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

        public Properties build() {
            if (!attributes.isEmpty()) {
                components.set(MEDICAL_ATTRIBUTES, attributes);
            }
            return this;
        }
    }
}
