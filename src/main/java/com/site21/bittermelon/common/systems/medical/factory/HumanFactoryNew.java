package com.site21.bittermelon.common.systems.medical.factory;

import com.site21.bittermelon.common.systems.character.Character;
import com.site21.bittermelon.common.systems.medical.blood.BloodInfo;
import com.site21.bittermelon.common.systems.medical.blood.BloodType;
import com.site21.bittermelon.common.systems.medical.compartment.Compartment;
import com.site21.bittermelon.common.systems.medical.compartment.CompartmentInstance;
import com.site21.bittermelon.common.systems.medical.medicalstats.AnimalMedicalStats;
import com.site21.bittermelon.common.systems.medical.medicalstats.MedicalStats;
import com.site21.bittermelon.init.custom.Anatomies;
import com.site21.bittermelon.init.neoforge.BitterDataComponents;
import net.minecraft.core.component.PatchedDataComponentMap;
import net.neoforged.neoforge.registries.DeferredHolder;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.site21.bittermelon.init.custom.Compartments.*;

public class HumanFactoryNew implements AnatomyFactory {
    private CompartmentInstance wholeBody;
    private List<CompartmentInstance> compartments;
    private static final int version = 0;

    @Override
    public MedicalStats build(BloodType bloodType, @NotNull Character character) {
        compartments = new ArrayList<>();
        wholeBody = addCompartment(WHOLE_BODY);
//        var debugLiver = addCompartment(DEBUG_COMPARTMENT, wholeBody, 0, 0, 0);
        var leftArm = addCompartment(ARM, wholeBody, 0, 7, 0);
        var rightArm = addCompartment(ARM, wholeBody, 11, 7, 0);

        CompartmentInstance gallBladder = GALLBLADDER.get().toInstance();

//        addCompartment(gallBladder, wholeBody, 10, 1, 0);

//
//        buildHead();
//        buildAbdomen();
//        addCompartment(SCALPEL, wholeBody, 0).getVisualData().x(50).y(60);

        MedicalStats stats = new AnimalMedicalStats(Anatomies.HUMAN_ANATOMY, version, compartments, wholeBody.getId(),
                character.getId(), new HashMap<>(), new PatchedDataComponentMap(PatchedDataComponentMap.EMPTY));

        stats.set(BitterDataComponents.BLOOD_INFO, new BloodInfo(bloodType));

        return stats;
    }

    private void buildHead() {
//        CompartmentInstance head = addCompartment(HEAD, wholeBody, 0);
//        head.getVisualData().x(20).y(30);
//        CompartmentInstance brain = addCompartment(BRAIN, head, 3);
//        addCompartment(FRONTAL_LOBE, brain, 1);
//        addCompartment(PARIETAL_LOBE, brain, 1);
//        addCompartment(BRAINSTEM, brain, 1);
    }

    private void buildAbdomen() {
//        CompartmentInstance abdomen = addCompartment(ABDOMEN, wholeBody, 0);
//        abdomen.getVisualData().x(50).y(30);
//        CompartmentInstance cut = addCompartment(CUT, abdomen, 0);
//        cut.getVisualData().x(20).y(0).scale(2);
//        CompartmentInstance cut1 = addCompartment(CUT, abdomen, 1);
//        cut1.getVisualData().x(20).y(0);
//        CompartmentInstance cut2 = addCompartment(CUT, abdomen, 2);
//        cut2.getVisualData().x(20).y(0);
//        CompartmentInstance cut3 = addCompartment(CUT, abdomen, 3);
//        cut3.getVisualData().x(20).y(0);
//        cut.addTag(CompartmentTag.CUT);
//        cut1.addTag(CompartmentTag.CUT);
//        cut2.addTag(CompartmentTag.CUT);
//        cut3.addTag(CompartmentTag.CUT);
//        addCompartment(STOMACH, abdomen, 4);
//        addCompartment(SMALL_INTESTINE, abdomen, 4);
//        addCompartment(COLON, abdomen, 4);
//        addCompartment(LIVER, abdomen, 4);
//        addCompartment(GALLBLADDER, abdomen, 4);
    }

    private CompartmentInstance addCompartment(@NotNull DeferredHolder<Compartment, Compartment> holder) {
        CompartmentInstance instance = holder.get().toInstance();
        compartments.add(instance);
        return instance;
    }

    private CompartmentInstance addCompartment(@NotNull DeferredHolder<Compartment, Compartment> holder, @NotNull CompartmentInstance parent, int x, int y, int layer) {
        CompartmentInstance instance = holder.get().toInstance();
        compartments.add(instance);
        parent.tryToInsert(instance, layer, x, y);
        return instance;
    }

    private void addCompartment(CompartmentInstance instance, @NotNull CompartmentInstance parent, int x, int y, int layer) {
        compartments.add(instance);
        parent.tryToInsert(instance, layer, x, y);
    }
}
