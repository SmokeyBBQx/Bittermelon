package com.site21.bittermelon.common.systems.medical.compartment;

import com.site21.bittermelon.Bittermelon;
import com.site21.bittermelon.common.systems.medical.client.screen.widget.CompartmentSpaceWidget;
import com.site21.bittermelon.common.systems.medical.medicalstats.MedicalStats;
import com.site21.bittermelon.init.neoforge.BitterDataComponents;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.jetbrains.annotations.NotNull;

import java.util.*;

import static com.site21.bittermelon.init.neoforge.BitterItems.BODY_PART;
import static com.site21.bittermelon.init.neoforge.BitterRegistries.COMPARTMENT_REGISTRY;

public class Compartment {
    protected final String id;
    protected final Properties properties;

    public Compartment(String id, Properties properties) {
        this.id = id;
        this.properties = properties;
    }

    public CompartmentInstance toInstance() {
        List<HashSet<UUID>> layers = new ArrayList<>();
        for (int i = 0; i < properties.layers.length; ++i) {
            layers.add(new HashSet<>());
        }

        VisualData visualData = properties.visualData;

        return new CompartmentInstance(
                this,
                UUID.randomUUID(),
                layers,
                properties.defaultHealth,
                properties.defaultHealth,
                properties.defaultAttributes,
                properties.defaultTags,
                id,
                new VisualData(visualData.x, visualData.y, visualData.z, visualData.scale, visualData.width, visualData.height, visualData.icon)
        );
    }

    public void tick(MedicalStats medicalStats, @NotNull CompartmentInstance instance) {}

    public void onExtract(MedicalStats medicalStats, CompartmentInstance instance) {}

    public boolean canExtract(CompartmentInstance instance, MedicalStats medicalStats) {
        return false;
    }

    public boolean tryToInsert(@NotNull CompartmentInstance instance, CompartmentInstance input, int layer) {
        if (properties.layers == null || properties.layers[layer] == null) return false;

        instance.addCompartment(layer, input);
        return true;
    }

    public ItemStack createItemStack(@NotNull CompartmentInstance instance) {
        ItemStack stack = properties.item.value().getDefaultInstance();
        stack.set(BitterDataComponents.COMPARTMENT, instance.toData());
        return stack;
    }

    public void performAction(@NotNull CompartmentSpaceWidget widget, double mouseX, double mouseY, int button) {
        widget.handleCompartmentPlacement(mouseX, mouseY);
    }

    public void performActionOn(@NotNull CompartmentSpaceWidget widget, CompartmentInstance target, CompartmentInstance instance, double mouseX, double mouseY, int button) {
        widget.handleCompartmentPlacement(mouseX, mouseY);
    }

    public Holder<Compartment> builtInRegistryHolder() {
        return COMPARTMENT_REGISTRY.get(ResourceLocation.fromNamespaceAndPath(Bittermelon.MOD_ID, id)).orElseThrow();
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

    public static class Properties {
        EnumSet<CompartmentTag> defaultTags = EnumSet.noneOf(CompartmentTag.class);
        EnumMap<MedicalAttribute, Float> defaultAttributes = new EnumMap<>(MedicalAttribute.class);
        LayerData[] layers = new LayerData[]{new LayerData(ResourceLocation.withDefaultNamespace("textures/block/stone.png"), "Compartment", 0, 0)};
        Holder<Item> item = Items.AIR.builtInRegistryHolder();
        float defaultHealth = 0;
        VisualData visualData = VisualData.empty().width(200).height(200);
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
