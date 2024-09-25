package com.site21.bittermelon.substance;

import com.site21.bittermelon.Bittermelon;
import com.site21.bittermelon.substance.reactions.Reaction;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.resources.ResourceLocation;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static com.site21.bittermelon.init.ModRegistries.SUBSTANCE_REGISTRY;

public abstract class Substance {
    public final SubstanceProperties properties;

    public abstract float getMolarMass();

    public abstract float getDensity();

    public abstract float getSpecificVolume();

    public abstract float getHeatCapacity();

    public String getName() {
        return properties.getName();
    }

    public Integer getColor() {
        return properties.getLiquidColor();
    }

    public Substance(SubstanceProperties properties) {
        this.properties = properties;
    }

    public void addReaction(Reaction reaction) {
        properties.getReactions().add(reaction);
    }

    public DataComponentMap components() {
        return DataComponentMap.EMPTY;
    }

    public Holder<Substance> builtInRegistryHolder() {
        return SUBSTANCE_REGISTRY.getHolder(ResourceLocation.fromNamespaceAndPath(Bittermelon.MOD_ID, getName().toLowerCase())).get();
//        return this.holder;
    }

    public List<Reaction> checkForReactions(Set<Substance> mixture) {
        List<Reaction> possibleReactions = new ArrayList<>();

        for (Reaction reaction : properties.getReactions()) {
            if (!mixture.containsAll(reaction.getReactants().keySet())) {
                break;
            }
            System.out.println(getName() + "contains reaction");
            possibleReactions.add(reaction);
        }
        return possibleReactions;
    }
}
