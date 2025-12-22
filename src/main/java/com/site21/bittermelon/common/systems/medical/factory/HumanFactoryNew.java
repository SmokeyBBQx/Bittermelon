package com.site21.bittermelon.common.systems.medical.factory;

import com.site21.bittermelon.common.systems.character.Character;
import com.site21.bittermelon.common.systems.medical.blood.BloodType;
import com.site21.bittermelon.common.systems.medical.compartment.Compartment;
import com.site21.bittermelon.common.systems.medical.compartment.CompartmentInstance;
import com.site21.bittermelon.common.systems.medical.compartment.layer.LayerData;
import com.site21.bittermelon.common.systems.medical.compartment.layer.SlotPoint;
import com.site21.bittermelon.common.systems.medical.compartment.layer.SlotType;
import com.site21.bittermelon.common.systems.medical.medicalstats.AnimalMedicalStats;
import com.site21.bittermelon.common.systems.medical.medicalstats.MedicalStats;
import com.site21.bittermelon.init.custom.Anatomies;
import net.minecraft.core.component.PatchedDataComponentMap;
import net.neoforged.neoforge.registries.DeferredHolder;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.site21.bittermelon.init.custom.Compartments.DEBUG_COMPARTMENT;
import static com.site21.bittermelon.init.custom.Compartments.GALLBLADDER;
import static com.site21.bittermelon.init.neoforge.BitterDataComponents.LAYERS;

public class HumanFactoryNew implements AnatomyFactory {
    private CompartmentInstance wholeBody;
    private List<CompartmentInstance> compartments;
    private static final int version = 0;

    @Override
    public MedicalStats build(BloodType bloodType, @NotNull Character character) {
        compartments = new ArrayList<>();
        wholeBody = addCompartment(DEBUG_COMPARTMENT);

        // TODO: This only works if I set the LAYERS data component after creating the compartment instance. Why??
        // TODO: Perhaps LayerData needs to be immutable
        wholeBody.set(LAYERS, List.of(
                new LayerData("Liver Tissue", 18, 13, List.of(
                        new SlotPoint(5, 0, SlotType.ORGAN),
                        new SlotPoint(6, 0, SlotType.ORGAN),
                        new SlotPoint(7, 0, SlotType.ORGAN),
                        new SlotPoint(8, 0, SlotType.ORGAN),
                        new SlotPoint(9, 0, SlotType.ORGAN),
                        new SlotPoint(10, 0, SlotType.ORGAN),
                        new SlotPoint(11, 0, SlotType.ORGAN),
                        new SlotPoint(12, 0, SlotType.ORGAN),
                        new SlotPoint(13, 0, SlotType.ORGAN),
                        new SlotPoint(14, 0, SlotType.ORGAN),
                        new SlotPoint(15, 0, SlotType.ORGAN),
                        new SlotPoint(16, 0, SlotType.ORGAN),
                        new SlotPoint(17, 0, SlotType.ORGAN),
                        new SlotPoint(3, 1, SlotType.ORGAN),
                        new SlotPoint(4, 1, SlotType.ORGAN),
                        new SlotPoint(5, 1, SlotType.ORGAN),
                        new SlotPoint(6, 1, SlotType.FAT),
                        new SlotPoint(7, 1, SlotType.ORGAN),
                        new SlotPoint(8, 1, SlotType.ORGAN),
                        new SlotPoint(9, 1, SlotType.ORGAN),
                        new SlotPoint(10, 1, SlotType.BRAIN_TISSUE),
                        new SlotPoint(11, 1, SlotType.BRAIN_TISSUE),
                        new SlotPoint(12, 1, SlotType.BRAIN_TISSUE),
                        new SlotPoint(13, 1, SlotType.ORGAN),
                        new SlotPoint(14, 1, SlotType.MUSCLE),
                        new SlotPoint(15, 1, SlotType.ORGAN),
                        new SlotPoint(16, 1, SlotType.MUSCLE),
                        new SlotPoint(17, 1, SlotType.ORGAN),
                        new SlotPoint(2, 2, SlotType.ORGAN),
                        new SlotPoint(3, 2, SlotType.FAT),
                        new SlotPoint(4, 2, SlotType.FAT),
                        new SlotPoint(5, 2, SlotType.FAT),
                        new SlotPoint(6, 2, SlotType.FAT),
                        new SlotPoint(7, 2, SlotType.ORGAN),
                        new SlotPoint(8, 2, SlotType.ORGAN),
                        new SlotPoint(9, 2, SlotType.BRAIN_TISSUE),
                        new SlotPoint(10, 2, SlotType.BRAIN_TISSUE),
                        new SlotPoint(11, 2, SlotType.BRAIN_TISSUE),
                        new SlotPoint(12, 2, SlotType.ORGAN),
                        new SlotPoint(13, 2, SlotType.ORGAN),
                        new SlotPoint(14, 2, SlotType.ORGAN),
                        new SlotPoint(15, 2, SlotType.ORGAN),
                        new SlotPoint(16, 2, SlotType.ORGAN),
                        new SlotPoint(17, 2, SlotType.ORGAN),
                        new SlotPoint(2, 3, SlotType.ORGAN),
                        new SlotPoint(3, 3, SlotType.FAT),
                        new SlotPoint(4, 3, SlotType.FAT),
                        new SlotPoint(5, 3, SlotType.FAT),
                        new SlotPoint(6, 3, SlotType.ORGAN),
                        new SlotPoint(7, 3, SlotType.ORGAN),
                        new SlotPoint(8, 3, SlotType.BRAIN_TISSUE),
                        new SlotPoint(9, 3, SlotType.BRAIN_TISSUE),
                        new SlotPoint(10, 3, SlotType.BRAIN_TISSUE),
                        new SlotPoint(11, 3, SlotType.ORGAN),
                        new SlotPoint(12, 3, SlotType.ORGAN),
                        new SlotPoint(13, 3, SlotType.ORGAN),
                        new SlotPoint(14, 3, SlotType.MUSCLE),
                        new SlotPoint(15, 3, SlotType.ORGAN),
                        new SlotPoint(16, 3, SlotType.MUSCLE),
                        new SlotPoint(1, 4, SlotType.ORGAN),
                        new SlotPoint(2, 4, SlotType.ORGAN),
                        new SlotPoint(3, 4, SlotType.SKIN),
                        new SlotPoint(4, 4, SlotType.SKIN),
                        new SlotPoint(5, 4, SlotType.SKIN),
                        new SlotPoint(6, 4, SlotType.ORGAN),
                        new SlotPoint(7, 4, SlotType.BRAIN_TISSUE),
                        new SlotPoint(8, 4, SlotType.BRAIN_TISSUE),
                        new SlotPoint(9, 4, SlotType.BRAIN_TISSUE),
                        new SlotPoint(10, 4, SlotType.MEMBRANE),
                        new SlotPoint(11, 4, SlotType.MEMBRANE),
                        new SlotPoint(12, 4, SlotType.MEMBRANE),
                        new SlotPoint(13, 4, SlotType.ORGAN),
                        new SlotPoint(14, 4, SlotType.ORGAN),
                        new SlotPoint(15, 4, SlotType.MUSCLE),
                        new SlotPoint(0, 5, SlotType.ORGAN),
                        new SlotPoint(1, 5, SlotType.ORGAN),
                        new SlotPoint(2, 5, SlotType.ORGAN),
                        new SlotPoint(3, 5, SlotType.SKIN),
                        new SlotPoint(4, 5, SlotType.SKIN),
                        new SlotPoint(5, 5, SlotType.BRAIN_TISSUE),
                        new SlotPoint(6, 5, SlotType.BRAIN_TISSUE),
                        new SlotPoint(7, 5, SlotType.BRAIN_TISSUE),
                        new SlotPoint(8, 5, SlotType.ORGAN),
                        new SlotPoint(9, 5, SlotType.MEMBRANE),
                        new SlotPoint(10, 5, SlotType.MEMBRANE),
                        new SlotPoint(11, 5, SlotType.MEMBRANE),
                        new SlotPoint(12, 5, SlotType.MEMBRANE),
                        new SlotPoint(13, 5, SlotType.ORGAN),
                        new SlotPoint(14, 5, SlotType.ORGAN),
                        new SlotPoint(0, 6, SlotType.ORGAN),
                        new SlotPoint(1, 6, SlotType.ORGAN),
                        new SlotPoint(2, 6, SlotType.ORGAN),
                        new SlotPoint(3, 6, SlotType.ORGAN),
                        new SlotPoint(4, 6, SlotType.BRAIN_TISSUE),
                        new SlotPoint(5, 6, SlotType.BRAIN_TISSUE),
                        new SlotPoint(6, 6, SlotType.ORGAN),
                        new SlotPoint(7, 6, SlotType.ORGAN),
                        new SlotPoint(8, 6, SlotType.ORGAN),
                        new SlotPoint(9, 6, SlotType.ORGAN),
                        new SlotPoint(10, 6, SlotType.MEMBRANE),
                        new SlotPoint(11, 6, SlotType.MEMBRANE),
                        new SlotPoint(0, 7, SlotType.ORGAN),
                        new SlotPoint(1, 7, SlotType.ORGAN),
                        new SlotPoint(2, 7, SlotType.BRAIN_TISSUE),
                        new SlotPoint(3, 7, SlotType.BRAIN_TISSUE),
                        new SlotPoint(4, 7, SlotType.BRAIN_TISSUE),
                        new SlotPoint(5, 7, SlotType.BONE),
                        new SlotPoint(6, 7, SlotType.BONE),
                        new SlotPoint(7, 7, SlotType.ORGAN),
                        new SlotPoint(8, 7, SlotType.ORGAN),
                        new SlotPoint(9, 7, SlotType.MEMBRANE),
                        new SlotPoint(10, 7, SlotType.MEMBRANE),
                        new SlotPoint(0, 8, SlotType.ORGAN),
                        new SlotPoint(1, 8, SlotType.ORGAN),
                        new SlotPoint(2, 8, SlotType.ORGAN),
                        new SlotPoint(3, 8, SlotType.BONE),
                        new SlotPoint(4, 8, SlotType.BONE),
                        new SlotPoint(5, 8, SlotType.BONE),
                        new SlotPoint(6, 8, SlotType.ORGAN),
                        new SlotPoint(7, 8, SlotType.ORGAN),
                        new SlotPoint(8, 8, SlotType.ORGAN),
                        new SlotPoint(0, 9, SlotType.ORGAN),
                        new SlotPoint(1, 9, SlotType.ORGAN),
                        new SlotPoint(2, 9, SlotType.ORGAN),
                        new SlotPoint(3, 9, SlotType.BONE),
                        new SlotPoint(4, 9, SlotType.BONE),
                        new SlotPoint(5, 9, SlotType.ORGAN),
                        new SlotPoint(6, 9, SlotType.ORGAN),
                        new SlotPoint(7, 9, SlotType.ORGAN),
                        new SlotPoint(0, 10, SlotType.ORGAN),
                        new SlotPoint(1, 10, SlotType.ORGAN),
                        new SlotPoint(2, 10, SlotType.ORGAN),
                        new SlotPoint(3, 10, SlotType.ORGAN),
                        new SlotPoint(4, 10, SlotType.ORGAN),
                        new SlotPoint(5, 10, SlotType.ORGAN),
                        new SlotPoint(0, 11, SlotType.ORGAN),
                        new SlotPoint(1, 11, SlotType.ORGAN),
                        new SlotPoint(2, 11, SlotType.ORGAN),
                        new SlotPoint(0, 12, SlotType.ORGAN),
                        new SlotPoint(1, 12, SlotType.ORGAN)
                ))
        ));

        CompartmentInstance gallBladder = GALLBLADDER.get().toInstance();

        addCompartment(gallBladder, wholeBody, 10, 1, 0);

//
//        buildHead();
//        buildAbdomen();
//        addCompartment(SCALPEL, wholeBody, 0).getVisualData().x(50).y(60);

        return new AnimalMedicalStats(Anatomies.HUMAN_ANATOMY, version, compartments, wholeBody.getId(),
                character.getId(), new HashMap<>(), new PatchedDataComponentMap(PatchedDataComponentMap.EMPTY));
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
