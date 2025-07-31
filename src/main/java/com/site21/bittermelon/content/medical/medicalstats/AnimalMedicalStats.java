package com.site21.bittermelon.content.medical.medicalstats;

import com.site21.bittermelon.content.atmosphere.AtmosHandler;
import com.site21.bittermelon.content.atmosphere.AtmosInstance;
import com.site21.bittermelon.content.medical.blood.BloodInstance;
import com.site21.bittermelon.content.medical.blood.BloodType;
import com.site21.bittermelon.content.medical.compartments.CompartmentInstance;
import com.site21.bittermelon.content.medical.compartments.CompartmentTag;
import com.site21.bittermelon.content.medical.compartments.MedicalAttribute;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.UUID;

import static com.site21.bittermelon.init.custom.Substances.OXYGEN;
import static com.site21.bittermelon.init.neoforge.BitterMobEffects.*;

public class AnimalMedicalStats extends MedicalStats {
    private BloodType bloodType = BloodType.A_PLUS;
    private float bloodVolume = 100;
    private float hematocrit = 50;
    private float oxygenSaturation = 100;

    private static final int HYPOXIA_THRESHOLD = 80;
    private static final int BLOOD_LOSS_THRESHOLD = 60;
    private static final float HYPOXIA_DAMAGE = 0.01f;
    private static final float ASPHYXIATION_RATE = 0.05f;

    public AnimalMedicalStats(List<CompartmentInstance> compartments, UUID mainCompartmentID, UUID characterID) {
        super(compartments, mainCompartmentID, characterID);
    }

    @Override
    public void update(@NotNull Level level) {
        super.update(level);

        if (entity == null) {
            return;
        }

        updateCardiopulmonary();
    }

    private void updateCardiopulmonary() {
        bloodVolume = Math.min(100, bloodVolume + medicalAttributes.get(MedicalAttribute.BLOOD_REGENERATION) * getCirculation());
        oxygenSaturation = Mth.clamp(oxygenSaturation + getRespirationAmount(), 0, 100);

        if (oxygenSaturation < HYPOXIA_THRESHOLD) {
            for (CompartmentInstance instance : compartments.values()) {
                if (instance.hasTag(CompartmentTag.BODY_PART)) {
                    instance.setHealth(instance.getHealth() - HYPOXIA_DAMAGE);
                }
            }
        }

        System.out.println(oxygenSaturation);
    }

    private float getRespirationAmount() {
        if (entity.getAirSupply() <= 0) return -ASPHYXIATION_RATE;
        if (!entity.getEyeInFluidType().isAir()) return 0;

        AtmosInstance atmosInstance = AtmosHandler.getAtmosInstanceAt(entity.level(), entity.getOnPos().above());
        if (atmosInstance != null) {
            if (!atmosInstance.containsGas(OXYGEN.get())) return 0;
        }

        return getRespiration();
    }

    @Override
    protected void handleMobEffects() {
        super.handleMobEffects();

        if (bloodVolume < BLOOD_LOSS_THRESHOLD || oxygenSaturation < HYPOXIA_THRESHOLD) {
            entity.addEffect(new MobEffectInstance(ASPHYXIATION, 2, 0, true, false, false));
        }

        if (getPain() > 0) {
            entity.addEffect(new MobEffectInstance(PAIN, 2, (int) getPain(), true, false, false));
        }
    }

    public void transfuseBlood(@NotNull BloodInstance instance) {
        instance.data().drugs().forEach(this::addDrug);
        modifyBloodVolume(instance.volume());
        if (!bloodType.isBloodTypeCompatible(instance.data().bloodType())) {
            float immuneResponse = medicalAttributes.get(MedicalAttribute.IMMUNITY) * getCirculation();

        }
    }

    public BloodInstance drawBlood(float volume) {
        modifyBloodVolume(-volume);

        // TODO: Split drugs
        return BloodInstance.of(volume, bloodType, getActiveDrugs());
    }

    public void modifyOxygenSaturation(float delta) {
        oxygenSaturation += delta;
    }

    public void modifyBloodVolume(float bloodVolume) {
        this.bloodVolume += bloodVolume;
    }

    public float getPain() {
        return medicalAttributes.get(MedicalAttribute.NERVOUS) * medicalAttributes.get(MedicalAttribute.PAIN) * getConsciousness();
    }

    public float getRespiration() {
        return medicalAttributes.get(MedicalAttribute.RESPIRATION) * medicalAttributes.get(MedicalAttribute.BRAIN_VITALS);
    }

    public BloodType getBloodType() {
        return bloodType;
    }
}
