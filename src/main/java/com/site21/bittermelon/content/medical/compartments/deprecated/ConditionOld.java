package com.site21.bittermelon.content.medical.compartments.deprecated;

import com.site21.bittermelon.content.character.Character;
import com.site21.bittermelon.content.medical.compartments.CompartmentTag;
import com.site21.bittermelon.content.medical.compartments.FunctionType;
import com.site21.bittermelon.content.medical.medicalstats.MedicalStatsOld;
import net.minecraft.world.entity.LivingEntity;
import org.jetbrains.annotations.NotNull;

import java.util.EnumSet;

public abstract class ConditionOld extends CompartmentOld {
    protected final LivingEntity entity;
    protected final Character character;

    public ConditionOld(EnumSet<CompartmentTag> types, String name, CompartmentOld owner, float maxHealth, Character character, LivingEntity entity) {
        super(types, name, owner, maxHealth);
        this.character = character;
        this.entity = entity;
    }

    @Override
    public void onDeath(@NotNull MedicalStatsOld mammalMedicalStats) {
       mammalMedicalStats.removeCompartment(this);
    }

    @Override
    public float getHealth() {
        float totalHealth = this.health;
        for (CompartmentOld child : children) {
            if (!(child instanceof ConditionOld)) {
                totalHealth += child.getAttribute(FunctionType.FUNCTION);
            }
        }
        return totalHealth;
    }

    public void effects() {
    }

    public Character getCharacter() { return character; }
    public LivingEntity getEntity() { return entity; }
}
