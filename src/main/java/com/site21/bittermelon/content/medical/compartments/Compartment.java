package com.site21.bittermelon.content.medical.compartments;

import com.site21.bittermelon.Bittermelon;
import com.site21.bittermelon.content.medical.medicalstats.MedicalStats;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

import java.util.*;

import static com.site21.bittermelon.init.neoforge.BitterRegistries.COMPARTMENT_REGISTRY;

public class Compartment {
    protected final String id;
    protected EnumSet<CompartmentTag> defaultTags;
    protected boolean doesBleed = false;

    public Compartment(String id, EnumSet<CompartmentTag> defaultTags) {
        this.id = id;
        this.defaultTags = defaultTags;
    }

    public Compartment(String id) {
        this(id, EnumSet.noneOf(CompartmentTag.class));
    }

    public void tick(MedicalStats medicalStats, @NotNull CompartmentInstance instance) {
        float functionMultiplier = 1;
        for (UUID childID : instance.getChildren()) {
            CompartmentInstance child = medicalStats.getCompartment(childID);
            instance.setHealth(Math.max(0, Math.min(instance.getMaxHealth(), instance.getHealth() +
                    child.getAttribute(FunctionType.DAMAGE))));
            functionMultiplier *= child.getAttribute(FunctionType.FUNCTION);
        }
        instance.updateFunction(functionMultiplier);
    }

    public void onExtract(MedicalStats medicalStats, CompartmentInstance instance) {

    }

    public boolean canExtract(CompartmentInstance instance) {
        return false;
    }

    public EnumMap<FunctionType, Float> makeAttributes(float maxHealth) {
        return new EnumMap<>(FunctionType.class);
    }

    public EnumSet<CompartmentTag> getDefaultTags() {
        return defaultTags;
    }

    public boolean doesBleed() {
        return doesBleed;
    }

    public Holder<Compartment> builtInRegistryHolder() {
        return COMPARTMENT_REGISTRY.getHolder(ResourceLocation.fromNamespaceAndPath(Bittermelon.MOD_ID, id)).get();
    }
}
