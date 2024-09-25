package com.site21.bittermelon.substance;

import com.site21.bittermelon.substance.reactions.Reaction;

import java.util.HashSet;
import java.util.Set;

public class SubstanceProperties {
    private final String name;
    private final int solidColor;
    private final int liquidColor;
    private final int gasColor;
    private final int plasmaColor;
    private final String flavor;
    private final float freezingTemperature;
    private final float boilingTemperature;
    private final float plasmaTemperature;
    private final boolean isGasVisible;
    private final Set<Reaction> reactions;

    private SubstanceProperties(Builder builder) {
        this.name = builder.name;
        this.solidColor = builder.solidColor;
        this.liquidColor = builder.liquidColor;
        this.gasColor = builder.gasColor;
        this.plasmaColor = builder.plasmaColor;
        this.flavor = builder.flavor;
        this.freezingTemperature = builder.freezingTemperature;
        this.boilingTemperature = builder.boilingTemperature;
        this.plasmaTemperature = builder.plasmaTemperature;
        this.isGasVisible = builder.isGasVisible;
        this.reactions = builder.reactions;
    }

    public String getName() {
        return name;
    }

    public int getSolidColor() {
        return solidColor;
    }

    public int getLiquidColor() {
        return liquidColor;
    }

    public int getGasColor() {
        return gasColor;
    }

    public int getPlasmaColor() {
        return plasmaColor;
    }

    public String getFlavor() {
        return flavor;
    }

    public float getFreezingTemperature() {
        return freezingTemperature;
    }

    public float getBoilingTemperature() {
        return boilingTemperature;
    }

    public float getPlasmaTemperature() {
        return plasmaTemperature;
    }

    public boolean isGasVisible() {
        return isGasVisible;
    }

    public Set<Reaction> getReactions() {
        return new HashSet<>(reactions);
    }

    public static class Builder {
        private String name;
        private int solidColor;
        private int liquidColor;
        private int gasColor;
        private int plasmaColor;
        private String flavor;
        private float freezingTemperature;
        private float boilingTemperature;
        private float plasmaTemperature;
        private boolean isGasVisible;
        private Set<Reaction> reactions = new HashSet<>();

        public Builder(String name) {
            this.name = name;
        }

        public Builder solidColor(int solidColor) {
            this.solidColor = solidColor;
            return this;
        }

        public Builder liquidColor(int liquidColor) {
            this.liquidColor = liquidColor;
            return this;
        }

        public Builder gasColor(int gasColor) {
            this.gasColor = gasColor;
            return this;
        }

        public Builder plasmaColor(int plasmaColor) {
            this.plasmaColor = plasmaColor;
            return this;
        }

        public Builder flavor(String flavor) {
            this.flavor = flavor;
            return this;
        }

        public Builder freezingTemperature(float freezingTemperature) {
            this.freezingTemperature = freezingTemperature;
            return this;
        }

        public Builder boilingTemperature(float boilingTemperature) {
            this.boilingTemperature = boilingTemperature;
            return this;
        }

        public Builder plasmaTemperature(float plasmaTemperature) {
            this.plasmaTemperature = plasmaTemperature;
            return this;
        }

        public Builder isGasVisible(boolean isGasVisible) {
            this.isGasVisible = isGasVisible;
            return this;
        }

        public Builder addReaction(Reaction reaction) {
            this.reactions.add(reaction);
            return this;
        }

        public SubstanceProperties build() {
            return new SubstanceProperties(this);
        }
    }
}
