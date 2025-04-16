package com.site21.bittermelon.content.medical.compartments;

import com.site21.bittermelon.Bittermelon;
import com.site21.bittermelon.content.medical.medicalstats.MedicalStats;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.function.Consumer;

import static com.site21.bittermelon.init.neoforge.BitterRegistries.COMPARTMENT_REGISTRY;

public class Compartment {
    protected final String id;
    protected final EnumSet<CompartmentTag> defaultTags;
    protected final Consumer<CompartmentInstance> attributeInitializer;

    public Compartment(String id, EnumSet<CompartmentTag> defaultTags, Consumer<CompartmentInstance> attributeInitializer) {
        this.id = id;
        this.defaultTags = defaultTags;
        this.attributeInitializer = attributeInitializer;
    }

    public Compartment(String id, EnumSet<CompartmentTag> defaultTags) {
        this(id, defaultTags, compartmentInstance -> {});
    }

    public void tick(MedicalStats medicalStats, @NotNull CompartmentInstance instance) {
        float functionMultiplier = 1;
        for (UUID childID : instance.getChildren()) {
            CompartmentInstance child = medicalStats.getCompartment(childID);
            functionMultiplier *= child.getAttribute(FunctionType.FUNCTION);
        }
        instance.updateFunction(functionMultiplier, medicalStats);
    }

    public void onExtract(MedicalStats medicalStats, CompartmentInstance instance) {

    }

    public boolean canExtract(CompartmentInstance instance, MedicalStats medicalStats) {
        return false;
    }

    public EnumSet<CompartmentTag> getDefaultTags() {
        return defaultTags;
    }

    public Holder<Compartment> builtInRegistryHolder() {
        return COMPARTMENT_REGISTRY.getHolder(ResourceLocation.fromNamespaceAndPath(Bittermelon.MOD_ID, id)).get();
    }
}
