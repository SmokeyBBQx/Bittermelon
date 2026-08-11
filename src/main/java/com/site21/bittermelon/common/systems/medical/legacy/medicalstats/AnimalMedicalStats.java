package com.site21.bittermelon.common.systems.medical.legacy.medicalstats;

import com.site21.bittermelon.common.systems.medical.legacy.anatomy.Anatomy;
import com.site21.bittermelon.common.systems.medical.legacy.anatomy.AnatomyModel;
import com.site21.bittermelon.common.systems.medical.legacy.blood.BloodType;
import com.site21.bittermelon.common.systems.medical.legacy.compartment.CompartmentInstance;
import com.site21.bittermelon.common.systems.medical.legacy.compartment.MedicalAttribute;
import net.minecraft.core.Holder;
import net.minecraft.core.component.PatchedDataComponentMap;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * To be moved into anatomy. There should only be one MedicalStats class.
 */
@Deprecated
public class AnimalMedicalStats extends MedicalStats {
    private BloodType bloodType = BloodType.A_PLUS;
    private float bloodVolume = 100;
    private float hematocrit = 50;
    private float oxygenSaturation = 100;
    private int heartRate;

    private static final int HYPOXIA_THRESHOLD = 80;
    private static final int BLOOD_LOSS_THRESHOLD = 60;
    private static final float HYPOXIA_DAMAGE = 0.01f;
    private static final float ASPHYXIATION_RATE = 0.05f;

    public AnimalMedicalStats(Holder<Anatomy> anatomy, int version, @NotNull List<CompartmentInstance> compartments,
                              UUID mainCompartmentId, Map<MedicalAttribute, MedicalAttributeInstance> attributes,
                              AnatomyModel anatomyModel, PatchedDataComponentMap components) {
        super(anatomy, version, compartments, mainCompartmentId, attributes, anatomyModel, components);
    }

//        updateCardiopulmonary();
//        simulateBleed(level);

//    private void updateCardiopulmonary() {
//        bloodVolume = Math.min(100, bloodVolume + getAttribute(MedicalAttribute.BLOOD_REGENERATION) * getCirculation());
//        oxygenSaturation = Mth.clamp(oxygenSaturation + getRespirationAmount(), 0, 100);
//
//        if (oxygenSaturation < HYPOXIA_THRESHOLD) {
//            for (CompartmentInstance instance : compartments.values()) {
//                if (instance.hasTag(CompartmentTag.BODY_PART)) {
//                    instance.setHealth(instance.getHealth() - HYPOXIA_DAMAGE);
//                }
//            }
//        }
//    }
//
//    private float getRespirationAmount() {
//        if (entity.getAirSupply() <= 0) return -ASPHYXIATION_RATE;
//        if (!entity.getEyeInFluidType().isAir()) return 0;
//
//        AtmosInstance atmosInstance = AtmosHandler.getAtmosInstanceAt(entity.level(), entity.getOnPos().above());
//        if (atmosInstance != null) {
//            if (!atmosInstance.containsGas(OXYGEN.get())) return 0;
//        }
//
//        return getRespiration();
//    }
//
//    @Override
//    protected void handleMobEffects() {
//        super.handleMobEffects();
//
//        if (bloodVolume < BLOOD_LOSS_THRESHOLD || oxygenSaturation < HYPOXIA_THRESHOLD) {
//            entity.addEffect(new MobEffectInstance(ASPHYXIATION, 2, 0, true, false, false));
//        }
//
//        if (getPain() > 0) {
//            entity.addEffect(new MobEffectInstance(PAIN, 2, (int) getPain(), true, false, false));
//        }
//    }

//    public void transfuseBlood(@NotNull BloodInstance instance) {
//        instance.data().drugs().forEach(this::addDrug);
//        modifyBloodVolume(instance.volume());
//        if (!bloodType.isBloodTypeCompatible(instance.data().bloodType())) {
//            float immuneResponse = getAttribute(MedicalAttribute.IMMUNITY) * getCirculation();
//
//        }
//    }

//    public BloodInstance drawBlood(float volume) {
//        modifyBloodVolume(-volume);
//
//        // TODO: Split drugs
//        return BloodInstance.of(volume, bloodType, getActiveDrugs());
//    }
//
//    public void simulateBleed(@NotNull Level level) {
//        if (level.getGameTime() % 20 != 0) return;
//
//        for (CompartmentInstance instance : compartments.values()) {
//            for (LayerData layer : instance.getLayers()) {
//                LayerSlot[][] grid = layer.getGrid();
//                int height = grid.length;
//                if (height == 0) continue;
//                int width = grid[0].length;
//
//                for (Map.Entry<Point, UUID> entry : layer.getCompartments().entrySet()) {
//                    CompartmentInstance targetInstance = getCompartment(entry.getValue());
//                    if (targetInstance.getAttribute(MedicalAttribute.BLEED) <= 0) continue;
//
//                    Point point = entry.getKey();
//                    int x = point.x();
//                    int y = point.y();
//
//                    grid[y][x].updateBloodLevel(0.1f);
//
//                    // Spread to adjacent horizontal slots
//                    if (x > 0) grid[y][x - 1].updateBloodLevel(0.05f);
//                    if (x < width - 1) grid[y][x + 1].updateBloodLevel(0.05f);
//                    if (y > 0) grid[y - 1][x].updateBloodLevel(0.05f);
//                    if (y < height - 1) grid[y + 1][x].updateBloodLevel(0.05f);
//                }
//            }
//        }
//    }

    public void modifyOxygenSaturation(float delta) {
        oxygenSaturation += delta;
    }

    public void modifyBloodVolume(float bloodVolume) {
        this.bloodVolume += bloodVolume;
    }

    public float getPain() {
        return getAttribute(MedicalAttribute.NERVOUS) * getAttribute(MedicalAttribute.PAIN) * getConsciousness();
    }

    public float getRespiration() {
        return getAttribute(MedicalAttribute.RESPIRATION) * getAttribute(MedicalAttribute.BRAIN_VITALS);
    }

    public BloodType getBloodType() {
        return bloodType;
    }
}
