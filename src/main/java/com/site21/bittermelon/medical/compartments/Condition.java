package com.site21.bittermelon.medical.compartments;

import com.site21.bittermelon.character.Character;
import com.site21.bittermelon.medical.medicalstats.MedicalStats;
import net.minecraft.world.entity.LivingEntity;

import java.util.EnumSet;
import java.util.Set;

public abstract class Condition extends Compartment {
    protected final LivingEntity entity;
    protected final Character character;

    public Condition(EnumSet<CompartmentType> types, String name, Compartment owner, float maxHealth, Character character, LivingEntity entity) {
        super(types, name, owner, maxHealth);
        this.character = character;
        this.entity = entity;
    }

    @Override
    public void onDeath(MedicalStats mammalMedicalStats) {
       mammalMedicalStats.removeCompartment(this);
    }

    @Override
    public float getHealth() {
        float totalHealth = this.health;
        for (Compartment child : children) {
            if (!(child instanceof Condition)) {
                totalHealth += child.getAttribute(FunctionType.FUNCTION);
            }
        }
        return totalHealth;
    }

    public void effects() {
    }
}
