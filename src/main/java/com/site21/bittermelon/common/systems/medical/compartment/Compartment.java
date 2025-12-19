package com.site21.bittermelon.common.systems.medical.compartment;

import com.mojang.serialization.Codec;
import com.site21.bittermelon.Bittermelon;
import com.site21.bittermelon.common.systems.medical.compartment.layer.LayerData;
import com.site21.bittermelon.common.systems.medical.compartment.layer.Point;
import com.site21.bittermelon.common.systems.medical.medicalstats.MedicalStats;
import com.site21.bittermelon.init.neoforge.BitterDataComponents;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.component.PatchedDataComponentMap;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.jetbrains.annotations.NotNull;

import java.util.*;

import static com.site21.bittermelon.init.neoforge.BitterRegistries.COMPARTMENT_REGISTRY;
import static com.site21.bittermelon.init.neoforge.BitterRegistries.COMPARTMENT_REGISTRY_KEY;

public class Compartment {
    public static final Codec<Holder<Compartment>> CODEC = COMPARTMENT_REGISTRY.holderByNameCodec();
    public static final StreamCodec<RegistryFriendlyByteBuf, Holder<Compartment>> STREAM_CODEC = ByteBufCodecs.holderRegistry(COMPARTMENT_REGISTRY_KEY);
    protected final String id;
    protected final Properties properties;

    public Compartment(String id, Properties properties) {
        this.id = id;
        this.properties = properties;
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
        return true;
    }

    public ItemStack createItemStack(@NotNull CompartmentInstance instance) {
        ItemStack stack = properties.item.value().getDefaultInstance();
        stack.set(BitterDataComponents.COMPARTMENT, instance.toData());
        return stack;
    }

    public Holder<Compartment> builtInRegistryHolder() {
        return COMPARTMENT_REGISTRY.get(ResourceLocation.fromNamespaceAndPath(Bittermelon.MOD_ID, id)).orElseThrow();
    }

    public DataComponentMap components() {
        return DataComponentMap.EMPTY;
    }

    public LayerData[] getLayers() {
        return properties.layers;
    }

    public EnumSet<CompartmentTag> getDefaultTags() {
        return properties.defaultTags;
    }

    public Item getItem() {
        return properties.item.value();
    }

    public List<Point> getShape() {
        return properties.shape;
    }

    public Point getPivot() {
        return properties.pivot;
    }

    public static class Properties {
        EnumSet<CompartmentTag> defaultTags = EnumSet.noneOf(CompartmentTag.class);
        EnumMap<MedicalAttribute, Float> defaultAttributes = new EnumMap<>(MedicalAttribute.class);
        LayerData[] layers = new LayerData[]{};
        List<Point> shape = new ArrayList<>();
        Point pivot = new Point(0, 0);
        Holder<Item> item = Items.AIR.builtInRegistryHolder();
        float defaultHealth = 0;
        VisualData visualData = VisualData.empty();
        String modelPart = "";

        public Properties defaultTags(EnumSet<CompartmentTag> defaultTags) {
            this.defaultTags = defaultTags;
            return this;
        }

        public Properties defaultTags(CompartmentTag @NotNull ... tags) {
            this.defaultTags = tags.length > 0 ? EnumSet.of(tags[0], tags) : EnumSet.noneOf(CompartmentTag.class);
            return this;
        }

        public Properties defaultAttributes(EnumMap<MedicalAttribute, Float> defaultAttributes) {
            this.defaultAttributes = defaultAttributes;
            return this;
        }

        public Properties addAttribute(MedicalAttribute attribute, float value) {
            this.defaultAttributes.put(attribute, value);
            return this;
        }

        public Properties addAttribute(MedicalAttribute attribute) {
            this.defaultAttributes.put(attribute, 1.0f);
            return this;
        }

        public Properties layers(LayerData... layers) {
            this.layers = layers;
            return this;
        }

        public Properties shape(List<Point> shape) {
            this.shape = shape;
            return this;
        }

        public Properties pivot(Point pivot) {
            this.pivot = pivot;
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
            this.visualData = visualData;
            return this;
        }

        public Properties modelPart(String modelPart) {
            this.modelPart = modelPart;
            return this;
        }
    }
}
