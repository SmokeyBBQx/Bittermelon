package com.site21.bittermelon.common.systems.medical.factory;

import com.site21.bittermelon.common.systems.character.Character;
import com.site21.bittermelon.common.systems.medical.blood.BloodType;
import com.site21.bittermelon.common.systems.medical.compartment.Compartment;
import com.site21.bittermelon.common.systems.medical.compartment.CompartmentInstance;
import com.site21.bittermelon.common.systems.medical.compartment.CompartmentTag;
import com.site21.bittermelon.common.systems.medical.medicalstats.AnimalMedicalStats;
import com.site21.bittermelon.common.systems.medical.medicalstats.MedicalStats;
import net.neoforged.neoforge.registries.DeferredHolder;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import static com.site21.bittermelon.init.custom.Compartments.*;

public class HumanFactoryNew implements AnatomyFactory {
    private CompartmentInstance wholeBody;
    private List<CompartmentInstance> compartments;
    private static final int version = 0;

    @Override
    public MedicalStats build(BloodType bloodType, @NotNull Character character) {
        compartments = new ArrayList<>();
        wholeBody = addCompartment(LIVER);
        addCompartment(GALLBLADDER, wholeBody, 10, 1, 0);
//
//        buildHead();
//        buildAbdomen();
//        addCompartment(SCALPEL, wholeBody, 0).getVisualData().x(50).y(60);

        return new AnimalMedicalStats(version, compartments, wholeBody.getId(), character.getUUID());
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
        parent.tryToInsert(layer, x, y, instance);
        return instance;
    }
}
