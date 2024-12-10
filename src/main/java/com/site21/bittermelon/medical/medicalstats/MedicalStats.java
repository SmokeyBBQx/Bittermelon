package com.site21.bittermelon.medical.medicalstats;

import com.site21.bittermelon.character.Character;
import com.site21.bittermelon.medical.blood.BloodType;
import com.site21.bittermelon.medical.compartments.Compartment;
import com.site21.bittermelon.medical.compartments.Condition;
import com.site21.bittermelon.medical.compartments.Injury;
import com.site21.bittermelon.medical.compartments.FunctionType;
import com.site21.bittermelon.medical.compartments.conditions.ForeignSubstance;
import com.site21.bittermelon.medical.compartments.conditions.Infection;
import com.site21.bittermelon.medical.organs.HeartRhythm;
import com.site21.bittermelon.util.ServerUtil;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;

import java.util.*;

public class MedicalStats {
    private final List<Compartment> compartments;
    private final BloodType bloodType;
    private float oxygenSaturation;
    private float bloodVolume;
    private float BPM;
    private float bloodPressureSystolic;
    private float bloodPressureDiastolic;
    private float temperature;
    private HeartRhythm heartRhythm;
    private final EnumMap<FunctionType, Float> stats = new EnumMap<>(FunctionType.class);
    private final Map<UUID, Float> immunity = new HashMap<>();
    private final Character character;
    private transient LivingEntity entity;

    public MedicalStats(BloodType bloodType, List<Compartment> compartments, Character character) {
        this.bloodType = bloodType;
        this.compartments = compartments;
        this.character = character;

        this.entity = ServerUtil.getLivingEntity(character.getEntityUUID());

        for (FunctionType type : FunctionType.values()) {
            stats.put(type, 0f);
        }
    }

    public void update() {
        List<Compartment> compartmentsCopy = new ArrayList<>(compartments);
        for (Compartment compartment : compartmentsCopy) {
            if (!compartments.contains(compartment)) {
                continue;
            }
            compartment.update(this);
            if (stats.get(FunctionType.BRAIN_VITALS) > 0) {
                respiration(compartment);
            }
            if (compartment instanceof Condition) {
                immuneSystem(compartment);
                elimination(compartment);
                heal(compartment);
            }
        }
        if (entity != null) {
            movement();
            manipulation();
        }
        updateStats();
    }

    public void removeCompartment(Compartment compartment) {
//        if (compartment instanceof BodyPart) {
//            compartment.kill();
//            // TODO: Proper removal implementation
//        } else {
        compartment.getOwner().getChildren().remove(compartment);
        compartments.remove(compartment);
        List<Compartment> children = compartment.getChildren();
        for (Compartment child : children) {
            compartments.remove(child);
        }
    }

    public void extractCompartment(Compartment compartment) {
        compartment.onExtract(this);
        removeCompartment(compartment);
    }

    public void addCompartment(Compartment compartment) {
        compartments.add(compartment);
    }

    public List<Compartment> getCompartments() {
        return compartments;
    }

    public void modifyBloodVolume(float amount) {
        bloodVolume = Math.max(0, Math.min(amount + bloodVolume, 100));
    }

    public void modifyOxygenSaturation(float amount) {
        oxygenSaturation = Math.max(0, Math.min(amount + oxygenSaturation, 100));
    }

    private void updateStats() {
        EnumMap<FunctionType, Float> statsCopy = new EnumMap<>(FunctionType.class);
        for (FunctionType type : FunctionType.values()) {
            statsCopy.put(type, 0f);
        }

        for (Compartment compartment : compartments) {
            for (FunctionType stat : FunctionType.values()) {
                float attribute = compartment.getAttribute(stat);
                if (attribute > 0) {
                    float currentValue = statsCopy.get(stat);
                    statsCopy.put(stat, currentValue + attribute);
                }
            }
        }

        stats.putAll(statsCopy);
    }

    private void updateCardiopulmonary() {
        modifyBloodVolume(0.01f);
        modifyOxygenSaturation(-0.01f);

        // TODO: Random heart state depending on heart health
    }

    private void respiration(Compartment compartment) {
        modifyOxygenSaturation(compartment.getAttribute(FunctionType.RESPIRATORY));
    }

    private void immuneSystem(Compartment compartment) {
        if (compartment instanceof Infection infection) {
            float immunityRate = stats.get(FunctionType.IMMUNITY);
            Compartment owner = infection.getOwner();
//                addCompartment(new Inflammation(owner.getName() + "inflammation", owner, infection.getMaxHealth()));
            // TODO: Add inflammation
            float memory = immunity.get(infection.getOrganism());
            infection.modifyHealth(-immunityRate * memory / 10);
            float currentImmunity = immunity.get(infection.getOrganism());
            immunity.put(infection.getOrganism(), currentImmunity + immunityRate / 10);
        }
    }

    private void elimination(Compartment compartment) {
        if (compartment instanceof ForeignSubstance foreignSubstance) {
            float eliminationRate = stats.get(FunctionType.ELIMINATION);
            foreignSubstance.modifyHealth(-eliminationRate / 10);
            // TODO: Separate metabolism and elimination?
        }
    }

    private void heal(Compartment compartment) {
        if (compartment instanceof Injury injury) {
            float healRate = stats.get(FunctionType.HEALING);
            injury.modifyHealth(-healRate);
        }
    }

    protected void sight() {
        if (stats.get(FunctionType.BRAIN_SIGHT) <= 10) {

        }

        if (stats.get(FunctionType.SIGHT) <= 10) {

        }
    }

    protected void movement() {
        float brainMotorAbility = stats.get(FunctionType.BRAIN_MOTOR_ABILITY);
        float movementAbility = stats.get(FunctionType.MOVEMENT);

        float movementCapability = (brainMotorAbility * movementAbility);
        double currentBaseValue = entity.getAttributeBaseValue(Attributes.MOVEMENT_SPEED);

//        System.out.println(movementCapability);

        if (movementCapability != currentBaseValue) {
            Objects.requireNonNull(entity.getAttribute(Attributes.MOVEMENT_SPEED)).setBaseValue(movementCapability);
            Objects.requireNonNull(entity.getAttribute(Attributes.JUMP_STRENGTH)).setBaseValue(movementCapability * 4.2);
        }
    }

    protected void manipulation() {
        float brainMotorAbility = stats.get(FunctionType.BRAIN_MOTOR_ABILITY);
        float manipulationAbility = stats.get(FunctionType.MANIPULATION);

        float movementCapability = (brainMotorAbility * manipulationAbility);
        double currentBaseValue = entity.getAttributeBaseValue(Attributes.ATTACK_SPEED);
        if (movementCapability != currentBaseValue) {
            Objects.requireNonNull(entity.getAttribute(Attributes.ATTACK_SPEED)).setBaseValue(movementCapability);
            Objects.requireNonNull(entity.getAttribute(Attributes.ATTACK_DAMAGE)).setBaseValue(movementCapability);
            Objects.requireNonNull(entity.getAttribute(Attributes.BLOCK_BREAK_SPEED)).setBaseValue(movementCapability);
            Objects.requireNonNull(entity.getAttribute(Attributes.BLOCK_INTERACTION_RANGE)).setBaseValue(movementCapability);
            Objects.requireNonNull(entity.getAttribute(Attributes.ENTITY_INTERACTION_RANGE)).setBaseValue(movementCapability);
        }
    }

    public float getTasteAbility() {
        return stats.get(FunctionType.BRAIN_TASTE) * stats.get(FunctionType.TASTE);
    }

    public float getHearingAbility() {
        return stats.get(FunctionType.BRAIN_HEARING) * stats.get(FunctionType.HEARING);
    }

    public float getLanguageComprehension() {
        return stats.get(FunctionType.BRAIN_LANGUAGE);
    }

    public float getTremor() {
        return stats.get(FunctionType.TREMOR);
    }

    public BloodType getBloodType() {
        return bloodType;
    }
}
