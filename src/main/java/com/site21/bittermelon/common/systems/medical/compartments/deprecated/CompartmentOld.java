package com.site21.bittermelon.common.systems.medical.compartments.deprecated;

import com.site21.bittermelon.common.systems.medical.compartments.CompartmentTag;
import com.site21.bittermelon.common.systems.medical.compartments.MedicalAttribute;
import com.site21.bittermelon.common.systems.medical.medicalstats.deprecated.MedicalStatsOld;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

public class CompartmentOld {
    protected final EnumSet<CompartmentTag> types;
    protected String name;
    protected transient CopyOnWriteArrayList<CompartmentOld> children;
    protected EnumMap<MedicalAttribute, Float> attributes;

    protected transient CompartmentOld owner;
    protected float maxHealth;
    protected final float trueMaxHealth;
    protected float health;
    protected ItemStack item;
    protected ResourceLocation icon;
    protected boolean hidden;
    protected boolean obscured = false;

    public CompartmentOld(EnumSet<CompartmentTag> types, String name, float maxHealth) {
        this.types = types;
        this.name = name;
        this.maxHealth = maxHealth;
        this.trueMaxHealth = maxHealth;
        this.health = maxHealth;
        this.children = new CopyOnWriteArrayList<>();
        this.attributes = new EnumMap<>(MedicalAttribute.class);
        this.hidden = true;
    }

    public CompartmentOld(EnumSet<CompartmentTag> types, String name, CompartmentOld owner, float maxHealth) {
        this(types, name, maxHealth);
        initializeWithOwner(owner);
    }

    public CompartmentOld(EnumSet<CompartmentTag> types, String name, CompartmentOld owner, float maxHealth, boolean hidden) {
        this(types, name, maxHealth);
        this.hidden = hidden;
        initializeWithOwner(owner);
    }

    protected void initializeChildren() {
        if (children == null) {
            children = new CopyOnWriteArrayList<>();
        }
    }

    protected void initializeAttributes() {
        if (attributes == null) {
            attributes = new EnumMap<>(MedicalAttribute.class);
        }
    }

    public void initializeWithOwner(CompartmentOld owner) {
        this.owner = owner;
        if (owner != null) {
            owner.addChild(this);
        }
    }

    public void update(MedicalStatsOld medicalStats) {
        if (health <= 0) {
            onDeath(medicalStats);
        }
    }

    public void modifyHealth(float amount) {
        this.health = Math.max(0, Math.min(maxHealth, health + amount));
    }

    public float getHealth() {
        float totalHealth = this.health;
        for (CompartmentOld child : children) {
            totalHealth *= child.getAttribute(MedicalAttribute.FUNCTION);
        }
        return totalHealth;
    }

    public void kill() {
        health = 0;
    }

    public void onDeath(MedicalStatsOld medicalStats) {

    }

    public void onExtract(MedicalStatsOld medicalStats) {

    }

    public void setAttribute(MedicalAttribute medicalAttribute, Float value) {
        initializeAttributes();
        attributes.put(medicalAttribute, value);
    }

    public void setAttributes(Map<MedicalAttribute, Float> newAttributes) {
        initializeAttributes();
        attributes.clear();
        attributes.putAll(newAttributes);
    }

    public float getAttribute(MedicalAttribute type) {
        if (attributes == null) return 0f;
        float healthPercentage = getHealth() / maxHealth;
        return attributes.getOrDefault(type, 0f) * healthPercentage;
    }

    public EnumMap<MedicalAttribute, Float> getAttributes() {
        initializeAttributes();
        return attributes;
    }

    public void addChild(CompartmentOld compartment) {
        initializeChildren();
        children.add(compartment);
    }

    public boolean areChildrenEmpty() {
        if (children == null) return true;
        return children.isEmpty() || children.stream().allMatch(CompartmentOld::isHidden);
    }

    public boolean isHidden() {
        if (owner == null) return false;
        return owner.isHidden() || hidden;
    }

    public void setHidden(boolean hidden) {
        this.hidden = hidden;
    }

    public void reveal() {
        hidden = false;
    }

    public boolean hasType(CompartmentTag type) {
        return types.contains(type);
    }
    public EnumSet<CompartmentTag> getTypes() {
        return types;
    }

    public CompartmentOld getOwner() {
        return owner;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public List<CompartmentOld> getChildren() {
        return children;
    }

    public float getMaxHealth() {
        return maxHealth;
    }

    public void modifyMaxHealth(float delta) {
        this.maxHealth = Math.max(0, Math.min(maxHealth + delta, trueMaxHealth));
    }

    public ItemStack getItem() {
        return item;
    }

    public void saveItem(ItemStack item) {
        this.item = item;
    }

    public void defaultItem(@NotNull Item item) {
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

