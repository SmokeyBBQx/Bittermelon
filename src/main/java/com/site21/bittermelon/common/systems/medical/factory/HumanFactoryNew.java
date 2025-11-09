package com.site21.bittermelon.common.systems.medical.factory;

import com.site21.bittermelon.common.systems.character.Character;
import com.site21.bittermelon.common.systems.medical.blood.BloodType;
import com.site21.bittermelon.common.systems.medical.compartments.Compartment;
import com.site21.bittermelon.common.systems.medical.compartments.CompartmentInstance;
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

    @Override
    public MedicalStats build(BloodType bloodType, @NotNull Character character) {
        compartments = new ArrayList<>();
        wholeBody = addCompartment(WHOLE_BODY);
        buildHead();
        buildAbdomen();
        addCompartment(SCALPEL, wholeBody, 0).getVisualData().x(50).y(60);

        return new AnimalMedicalStats(compartments, wholeBody.getUUID(), character.getUUID());
    }

    private void buildHead() {
        CompartmentInstance head = addCompartment(HEAD, wholeBody, 0);
        head.getVisualData().x(20).y(30);
        CompartmentInstance brain = addCompartment(BRAIN, head, 3);
        addCompartment(FRONTAL_LOBE, brain, 1);
        addCompartment(PARIETAL_LOBE, brain, 1);
        addCompartment(BRAINSTEM, brain, 1);
    }

    private void buildAbdomen() {
        CompartmentInstance abdomen = addCompartment(ABDOMEN, wholeBody, 0);
        abdomen.getVisualData().x(50).y(30);
        addCompartment(STOMACH, abdomen, 4);
        addCompartment(SMALL_INTESTINE, abdomen, 4);
        addCompartment(COLON, abdomen, 4);
        addCompartment(LIVER, abdomen, 4);
        addCompartment(GALLBLADDER, abdomen, 4);
    }

    private CompartmentInstance addCompartment(@NotNull DeferredHolder<Compartment, Compartment> holder) {
        CompartmentInstance instance = holder.get().toInstance();
        compartments.add(instance);
        return instance;
    }

    private CompartmentInstance addCompartment(@NotNull DeferredHolder<Compartment, Compartment> holder, @NotNull CompartmentInstance parent, int layer) {
        CompartmentInstance instance = holder.get().toInstance();
        compartments.add(instance);
        parent.tryToInsert(layer, instance);
        return instance;
    }
}
