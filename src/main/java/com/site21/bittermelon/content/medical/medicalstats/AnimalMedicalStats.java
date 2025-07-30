package com.site21.bittermelon.content.medical.medicalstats;

import com.site21.bittermelon.content.medical.compartments.CompartmentInstance;
import com.site21.bittermelon.content.medical.compartments.CompartmentTag;
import com.site21.bittermelon.content.medical.compartments.MedicalAttribute;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.UUID;

import static com.site21.bittermelon.init.neoforge.BitterMobEffects.*;

public class AnimalMedicalStats extends MedicalStats {
    private float bloodVolume = 100;
    private float oxygenSaturation = 100;

    private static final int HYPOXIA_THRESHOLD = 80;
    private static final int BLOOD_LOSS_THRESHOLD = 60;
    private static final float HYPOXIA_DAMAGE = 0.01f;

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
        handleMobEffects();
    }

    private void updateCardiopulmonary() {
        bloodVolume = Math.min(100, bloodVolume + medicalAttributes.get(MedicalAttribute.BLOOD_REGENERATION) * getCirculation());
//        oxygenSaturation = (float) entity.getAirSupply() / entity.getMaxAirSupply();
        // TODO: Random heart state depending on heart health

        if (oxygenSaturation < HYPOXIA_THRESHOLD) {
            for (CompartmentInstance instance : compartments.values()) {
                if (instance.hasTag(CompartmentTag.BODY_PART)) {
                    instance.setHealth(instance.getHealth() - HYPOXIA_DAMAGE);
                }
            }
        }
    }

    private void handleMobEffects() {
        if (bloodVolume < BLOOD_LOSS_THRESHOLD || oxygenSaturation < HYPOXIA_THRESHOLD) {
            entity.addEffect(new MobEffectInstance(ASPHYXIATION, 10, 0, true, false, false));
        }

        if (getPain() > 0) {
            entity.addEffect(new MobEffectInstance(PAIN, 10, (int) getPain(), true, false, false));
        }

        if (getConsciousness() <= 0) {
            entity.addEffect(new MobEffectInstance(UNCONSCIOUS, 2, 0, true, false, false));
        } else if (getConsciousness() < 1) {
            entity.addEffect(new MobEffectInstance(FAINTING, 2, (int) ((1 - getConsciousness()) * 100), true, false, false));
        }
    }

    public void modifyOxygenSaturation(float delta) {
        oxygenSaturation += delta;
    }
}
