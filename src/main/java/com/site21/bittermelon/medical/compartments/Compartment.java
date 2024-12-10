package com.site21.bittermelon.medical.compartments;

import com.site21.bittermelon.medical.medicalstats.MedicalStats;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.*;

public class Compartment {
    protected final EnumSet<CompartmentType> types;
    protected final String name;
    protected transient final List<Compartment> children;
    protected final EnumMap<FunctionType, Float> attributes;

    protected transient Compartment owner;
    protected float maxHealth;
    protected float health;
    protected ItemStack item;
    protected ResourceLocation icon;
    protected boolean hidden;
    protected boolean obscured = false;

    public Compartment(EnumSet<CompartmentType> types, String name, float maxHealth) {
        this.types = types;
        this.name = name;
        this.maxHealth = maxHealth;
        this.health = maxHealth;
        this.children = new ArrayList<>();
        this.attributes = new EnumMap<>(FunctionType.class);
        this.hidden = true;
    }

    public Compartment(EnumSet<CompartmentType> types, String name, Compartment owner, float maxHealth) {
        this(types, name, maxHealth);
        initializeWithOwner(owner);
    }

    public Compartment(EnumSet<CompartmentType> types, String name, Compartment owner, float maxHealth, boolean hidden) {
        this(types, name, maxHealth);
        this.hidden = hidden;
        initializeWithOwner(owner);
    }

    public void initializeWithOwner(Compartment owner) {
        this.owner = owner;
        if (owner != null) {
            owner.addChild(this);
        }
    }

    public void update(MedicalStats medicalStats) {
        if (health <= 0) {
            onDeath(medicalStats);
        }
    }

    public void modifyHealth(float amount) {
        this.health = Math.max(0, Math.min(maxHealth, health + amount));
    }

    public float getHealth() {
        float totalHealth = this.health;
        for (Compartment child : children) {
            totalHealth += child.getAttribute(FunctionType.FUNCTION);
        }
        return totalHealth;
    }

    public void kill() {
        health = 0;
    }

    public void onDeath(MedicalStats medicalStats) {

    }

    public void onExtract(MedicalStats medicalStats) {

    }

    public void setAttribute(FunctionType functionType, Float value) {
        this.attributes.put(functionType, value);
    }

    public void setAttributes(Map<FunctionType, Float> newAttributes) {
        this.attributes.clear();
        this.attributes.putAll(newAttributes);
    }

    public float getAttribute(FunctionType type) {
        float healthPercentage = getHealth() / maxHealth;
        return attributes.getOrDefault(type, 0f) * healthPercentage;
    }

    public EnumMap<FunctionType, Float> getAttributes() {
        return attributes;
    }

    public void addChild(Compartment compartment) {
        children.add(compartment);
    }

    public boolean areChildrenEmpty() {
        return children.isEmpty() || children.stream().allMatch(Compartment::isHidden);
    }

    public boolean isHidden() {
        return owner != null && owner.isHidden() || hidden;
    }
    public void setHidden(boolean hidden) {
        this.hidden = hidden;
    }

    public void reveal() {
        hidden = false;
    }

    public boolean hasType(CompartmentType type) {
        return types.contains(type);
    }

    public Compartment getOwner() {
        return owner;
    }

    public String getName() {
        return name;
    }

    public List<Compartment> getChildren() {
        return children;
    }

    public float getMaxHealth() {
        return maxHealth;
    }

    public ItemStack getItem() {
        return item;
    }

    public void saveItem(ItemStack item) {
        this.item = item;
    }

    public void defaultItem(Item item) {
        this.item = item.getDefaultInstance();
    }

    public void setIcon(ResourceLocation resourceLocation) {
        this.icon = resourceLocation;
    }

    public ResourceLocation getIcon() {
        return icon;
    }

    public boolean canExtract() {
        return false;
    }

    public boolean isObscured() {
        return obscured;
    }

    public void setObscured(boolean obscured) {
        this.obscured = obscured;
    }
}

