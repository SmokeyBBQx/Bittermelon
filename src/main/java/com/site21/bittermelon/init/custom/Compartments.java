package com.site21.bittermelon.init.custom;

import com.site21.bittermelon.Bittermelon;
import com.site21.bittermelon.common.systems.medical.compartment.Compartment;
import com.site21.bittermelon.common.systems.medical.compartment.MedicalAttribute;
import com.site21.bittermelon.common.systems.medical.compartment.VisualData;
import com.site21.bittermelon.common.systems.medical.compartment.layer.LayerData;
import com.site21.bittermelon.common.systems.medical.compartment.layer.Point;
import com.site21.bittermelon.common.systems.medical.compartment.layer.SlotPoint;
import com.site21.bittermelon.common.systems.medical.compartment.layer.SlotType;
import com.site21.bittermelon.init.neoforge.BitterDataComponents;
import com.site21.bittermelon.init.neoforge.BitterItems;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.List;

import static com.site21.bittermelon.init.neoforge.BitterRegistries.COMPARTMENT_REGISTRY_KEY;
import static net.minecraft.world.item.Items.RED_WOOL;

public class Compartments {
    public static final DeferredRegister<Compartment> COMPARTMENTS = DeferredRegister.create(COMPARTMENT_REGISTRY_KEY, Bittermelon.MOD_ID);

    /**
     * Debug compartment used for testing purposes. Has all necessary attributes.
     */
    public static final DeferredHolder<Compartment, Compartment> DEBUG_COMPARTMENT = COMPARTMENTS.register("debug_compartment",
            () -> new Compartment("liver", new Compartment.Properties()
                    .defaultHealth(40)
                    .item(BitterItems.LIVER)
                    .addAttribute(MedicalAttribute.ELIMINATION)
                    .addAttribute(MedicalAttribute.BRAIN_MOTOR_ABILITY)
                    .addAttribute(MedicalAttribute.BRAIN_CONSCIOUSNESS)
                    .addAttribute(MedicalAttribute.CIRCULATION)
                    .addAttribute(MedicalAttribute.MOVEMENT)
                    .addAttribute(MedicalAttribute.MANIPULATION)
                    .addAttribute(MedicalAttribute.RESPIRATION)
                    .layers(new LayerData("Liver Tissue", 14, 10, List.of(new SlotPoint(3, 0, SlotType.ORGAN), new SlotPoint(4, 0, SlotType.ORGAN), new SlotPoint(5, 0, SlotType.ORGAN), new SlotPoint(6, 0, SlotType.ORGAN), new SlotPoint(7, 0, SlotType.ORGAN), new SlotPoint(8, 0, SlotType.ORGAN), new SlotPoint(9, 0, SlotType.ORGAN), new SlotPoint(10, 0, SlotType.ORGAN), new SlotPoint(11, 0, SlotType.ORGAN), new SlotPoint(12, 0, SlotType.ORGAN), new SlotPoint(13, 0, SlotType.ORGAN), new SlotPoint(2, 1, SlotType.ORGAN), new SlotPoint(3, 1, SlotType.ORGAN), new SlotPoint(4, 1, SlotType.ORGAN), new SlotPoint(5, 1, SlotType.ORGAN), new SlotPoint(6, 1, SlotType.ORGAN), new SlotPoint(7, 1, SlotType.ORGAN), new SlotPoint(8, 1, SlotType.ORGAN), new SlotPoint(9, 1, SlotType.ORGAN), new SlotPoint(10, 1, SlotType.ORGAN), new SlotPoint(11, 1, SlotType.ORGAN), new SlotPoint(12, 1, SlotType.ORGAN), new SlotPoint(13, 1, SlotType.ORGAN), new SlotPoint(1, 2, SlotType.ORGAN), new SlotPoint(2, 2, SlotType.ORGAN), new SlotPoint(3, 2, SlotType.ORGAN), new SlotPoint(4, 2, SlotType.ORGAN), new SlotPoint(5, 2, SlotType.ORGAN), new SlotPoint(6, 2, SlotType.ORGAN), new SlotPoint(7, 2, SlotType.ORGAN), new SlotPoint(8, 2, SlotType.ORGAN), new SlotPoint(9, 2, SlotType.ORGAN), new SlotPoint(10, 2, SlotType.ORGAN), new SlotPoint(11, 2, SlotType.ORGAN), new SlotPoint(12, 2, SlotType.ORGAN), new SlotPoint(13, 2, SlotType.ORGAN), new SlotPoint(1, 3, SlotType.ORGAN), new SlotPoint(2, 3, SlotType.ORGAN), new SlotPoint(3, 3, SlotType.ORGAN), new SlotPoint(4, 3, SlotType.ORGAN), new SlotPoint(5, 3, SlotType.ORGAN), new SlotPoint(6, 3, SlotType.ORGAN), new SlotPoint(7, 3, SlotType.ORGAN), new SlotPoint(8, 3, SlotType.ORGAN), new SlotPoint(9, 3, SlotType.ORGAN), new SlotPoint(10, 3, SlotType.ORGAN), new SlotPoint(11, 3, SlotType.ORGAN), new SlotPoint(12, 3, SlotType.ORGAN), new SlotPoint(1, 4, SlotType.ORGAN), new SlotPoint(2, 4, SlotType.ORGAN), new SlotPoint(3, 4, SlotType.ORGAN), new SlotPoint(4, 4, SlotType.ORGAN), new SlotPoint(5, 4, SlotType.ORGAN), new SlotPoint(6, 4, SlotType.ORGAN), new SlotPoint(7, 4, SlotType.ORGAN), new SlotPoint(8, 4, SlotType.ORGAN), new SlotPoint(9, 4, SlotType.ORGAN), new SlotPoint(10, 4, SlotType.ORGAN), new SlotPoint(11, 4, SlotType.ORGAN), new SlotPoint(0, 5, SlotType.ORGAN), new SlotPoint(1, 5, SlotType.ORGAN), new SlotPoint(2, 5, SlotType.ORGAN), new SlotPoint(3, 5, SlotType.ORGAN), new SlotPoint(4, 5, SlotType.ORGAN), new SlotPoint(5, 5, SlotType.ORGAN), new SlotPoint(6, 5, SlotType.ORGAN), new SlotPoint(7, 5, SlotType.ORGAN), new SlotPoint(8, 5, SlotType.ORGAN), new SlotPoint(9, 5, SlotType.ORGAN), new SlotPoint(10, 5, SlotType.ORGAN), new SlotPoint(0, 6, SlotType.ORGAN), new SlotPoint(1, 6, SlotType.ORGAN), new SlotPoint(2, 6, SlotType.ORGAN), new SlotPoint(3, 6, SlotType.ORGAN), new SlotPoint(4, 6, SlotType.ORGAN), new SlotPoint(5, 6, SlotType.ORGAN), new SlotPoint(6, 6, SlotType.ORGAN), new SlotPoint(7, 6, SlotType.ORGAN), new SlotPoint(0, 7, SlotType.ORGAN), new SlotPoint(1, 7, SlotType.ORGAN), new SlotPoint(2, 7, SlotType.ORGAN), new SlotPoint(3, 7, SlotType.ORGAN), new SlotPoint(4, 7, SlotType.ORGAN), new SlotPoint(5, 7, SlotType.ORGAN), new SlotPoint(0, 8, SlotType.ORGAN), new SlotPoint(1, 8, SlotType.ORGAN), new SlotPoint(2, 8, SlotType.ORGAN), new SlotPoint(0, 9, SlotType.ORGAN), new SlotPoint(1, 9, SlotType.ORGAN) )))
                    .shape(List.of(new Point(3, 0), new Point(4, 0), new Point(5, 0), new Point(6, 0), new Point(7, 0), new Point(8, 0), new Point(9, 0), new Point(10, 0), new Point(11, 0), new Point(12, 0), new Point(13, 0), new Point(2, 1), new Point(3, 1), new Point(4, 1), new Point(5, 1), new Point(6, 1), new Point(7, 1), new Point(8, 1), new Point(9, 1), new Point(10, 1), new Point(11, 1), new Point(12, 1), new Point(13, 1), new Point(1, 2), new Point(2, 2), new Point(3, 2), new Point(4, 2), new Point(5, 2), new Point(6, 2), new Point(7, 2), new Point(8, 2), new Point(9, 2), new Point(10, 2), new Point(11, 2), new Point(12, 2), new Point(13, 2), new Point(1, 3), new Point(2, 3), new Point(3, 3), new Point(4, 3), new Point(5, 3), new Point(6, 3), new Point(7, 3), new Point(8, 3), new Point(9, 3), new Point(10, 3), new Point(11, 3), new Point(12, 3), new Point(1, 4), new Point(2, 4), new Point(3, 4), new Point(4, 4), new Point(5, 4), new Point(6, 4), new Point(7, 4), new Point(8, 4), new Point(9, 4), new Point(10, 4), new Point(11, 4), new Point(0, 5), new Point(1, 5), new Point(2, 5), new Point(3, 5), new Point(4, 5), new Point(5, 5), new Point(6, 5), new Point(7, 5), new Point(8, 5), new Point(9, 5), new Point(10, 5), new Point(0, 6), new Point(1, 6), new Point(2, 6), new Point(3, 6), new Point(4, 6), new Point(5, 6), new Point(6, 6), new Point(7, 6), new Point(0, 7), new Point(1, 7), new Point(2, 7), new Point(3, 7), new Point(4, 7), new Point(5, 7), new Point(0, 8), new Point(1, 8), new Point(2, 8), new Point(0, 9), new Point(1, 9) ))
                    .visualData(VisualData.empty().withWidth(14).withHeight(10).withIcon("anatomical_liver_small"))
            )
    );

    public static final DeferredHolder<Compartment, Compartment> WHOLE_BODY = COMPARTMENTS.register("whole_body",
            () -> new Compartment("whole_body", new Compartment.Properties()
                    .defaultHealth(100)
                    .layers(
                            new LayerData("Body", 14, 32, List.of( new SlotPoint(4, 0, SlotType.CAVITY), new SlotPoint(5, 0, SlotType.CAVITY), new SlotPoint(6, 0, SlotType.CAVITY), new SlotPoint(7, 0, SlotType.CAVITY), new SlotPoint(8, 0, SlotType.CAVITY), new SlotPoint(9, 0, SlotType.CAVITY), new SlotPoint(4, 1, SlotType.CAVITY), new SlotPoint(5, 1, SlotType.CAVITY), new SlotPoint(6, 1, SlotType.CAVITY), new SlotPoint(7, 1, SlotType.CAVITY), new SlotPoint(8, 1, SlotType.CAVITY), new SlotPoint(9, 1, SlotType.CAVITY), new SlotPoint(4, 2, SlotType.CAVITY), new SlotPoint(5, 2, SlotType.CAVITY), new SlotPoint(6, 2, SlotType.CAVITY), new SlotPoint(7, 2, SlotType.CAVITY), new SlotPoint(8, 2, SlotType.CAVITY), new SlotPoint(9, 2, SlotType.CAVITY), new SlotPoint(4, 3, SlotType.CAVITY), new SlotPoint(5, 3, SlotType.CAVITY), new SlotPoint(6, 3, SlotType.CAVITY), new SlotPoint(7, 3, SlotType.CAVITY), new SlotPoint(8, 3, SlotType.CAVITY), new SlotPoint(9, 3, SlotType.CAVITY), new SlotPoint(4, 4, SlotType.CAVITY), new SlotPoint(5, 4, SlotType.CAVITY), new SlotPoint(6, 4, SlotType.CAVITY), new SlotPoint(7, 4, SlotType.CAVITY), new SlotPoint(8, 4, SlotType.CAVITY), new SlotPoint(9, 4, SlotType.CAVITY), new SlotPoint(4, 5, SlotType.CAVITY), new SlotPoint(5, 5, SlotType.CAVITY), new SlotPoint(6, 5, SlotType.CAVITY), new SlotPoint(7, 5, SlotType.CAVITY), new SlotPoint(8, 5, SlotType.CAVITY), new SlotPoint(9, 5, SlotType.CAVITY), new SlotPoint(5, 6, SlotType.CAVITY), new SlotPoint(6, 6, SlotType.CAVITY), new SlotPoint(7, 6, SlotType.CAVITY), new SlotPoint(8, 6, SlotType.CAVITY), new SlotPoint(0, 7, SlotType.CAVITY), new SlotPoint(1, 7, SlotType.CAVITY), new SlotPoint(2, 7, SlotType.CAVITY), new SlotPoint(3, 7, SlotType.CAVITY), new SlotPoint(4, 7, SlotType.CAVITY), new SlotPoint(5, 7, SlotType.CAVITY), new SlotPoint(6, 7, SlotType.CAVITY), new SlotPoint(7, 7, SlotType.CAVITY), new SlotPoint(8, 7, SlotType.CAVITY), new SlotPoint(9, 7, SlotType.CAVITY), new SlotPoint(10, 7, SlotType.CAVITY), new SlotPoint(11, 7, SlotType.CAVITY), new SlotPoint(12, 7, SlotType.CAVITY), new SlotPoint(13, 7, SlotType.CAVITY), new SlotPoint(0, 8, SlotType.CAVITY), new SlotPoint(1, 8, SlotType.CAVITY), new SlotPoint(2, 8, SlotType.CAVITY), new SlotPoint(3, 8, SlotType.CAVITY), new SlotPoint(4, 8, SlotType.CAVITY), new SlotPoint(5, 8, SlotType.CAVITY), new SlotPoint(6, 8, SlotType.CAVITY), new SlotPoint(7, 8, SlotType.CAVITY), new SlotPoint(8, 8, SlotType.CAVITY), new SlotPoint(9, 8, SlotType.CAVITY), new SlotPoint(10, 8, SlotType.CAVITY), new SlotPoint(11, 8, SlotType.CAVITY), new SlotPoint(12, 8, SlotType.CAVITY), new SlotPoint(13, 8, SlotType.CAVITY), new SlotPoint(0, 9, SlotType.CAVITY), new SlotPoint(1, 9, SlotType.CAVITY), new SlotPoint(2, 9, SlotType.CAVITY), new SlotPoint(3, 9, SlotType.CAVITY), new SlotPoint(4, 9, SlotType.CAVITY), new SlotPoint(5, 9, SlotType.CAVITY), new SlotPoint(6, 9, SlotType.CAVITY), new SlotPoint(7, 9, SlotType.CAVITY), new SlotPoint(8, 9, SlotType.CAVITY), new SlotPoint(9, 9, SlotType.CAVITY), new SlotPoint(10, 9, SlotType.CAVITY), new SlotPoint(11, 9, SlotType.CAVITY), new SlotPoint(12, 9, SlotType.CAVITY), new SlotPoint(13, 9, SlotType.CAVITY), new SlotPoint(0, 10, SlotType.CAVITY), new SlotPoint(1, 10, SlotType.CAVITY), new SlotPoint(2, 10, SlotType.CAVITY), new SlotPoint(3, 10, SlotType.CAVITY), new SlotPoint(4, 10, SlotType.CAVITY), new SlotPoint(5, 10, SlotType.CAVITY), new SlotPoint(6, 10, SlotType.CAVITY), new SlotPoint(7, 10, SlotType.CAVITY), new SlotPoint(8, 10, SlotType.CAVITY), new SlotPoint(9, 10, SlotType.CAVITY), new SlotPoint(10, 10, SlotType.CAVITY), new SlotPoint(11, 10, SlotType.CAVITY), new SlotPoint(12, 10, SlotType.CAVITY), new SlotPoint(13, 10, SlotType.CAVITY), new SlotPoint(0, 11, SlotType.CAVITY), new SlotPoint(1, 11, SlotType.CAVITY), new SlotPoint(2, 11, SlotType.CAVITY), new SlotPoint(3, 11, SlotType.CAVITY), new SlotPoint(4, 11, SlotType.CAVITY), new SlotPoint(5, 11, SlotType.CAVITY), new SlotPoint(6, 11, SlotType.CAVITY), new SlotPoint(7, 11, SlotType.CAVITY), new SlotPoint(8, 11, SlotType.CAVITY), new SlotPoint(9, 11, SlotType.CAVITY), new SlotPoint(10, 11, SlotType.CAVITY), new SlotPoint(11, 11, SlotType.CAVITY), new SlotPoint(12, 11, SlotType.CAVITY), new SlotPoint(13, 11, SlotType.CAVITY), new SlotPoint(0, 12, SlotType.CAVITY), new SlotPoint(1, 12, SlotType.CAVITY), new SlotPoint(2, 12, SlotType.CAVITY), new SlotPoint(3, 12, SlotType.CAVITY), new SlotPoint(4, 12, SlotType.CAVITY), new SlotPoint(5, 12, SlotType.CAVITY), new SlotPoint(6, 12, SlotType.CAVITY), new SlotPoint(7, 12, SlotType.CAVITY), new SlotPoint(8, 12, SlotType.CAVITY), new SlotPoint(9, 12, SlotType.CAVITY), new SlotPoint(10, 12, SlotType.CAVITY), new SlotPoint(11, 12, SlotType.CAVITY), new SlotPoint(12, 12, SlotType.CAVITY), new SlotPoint(13, 12, SlotType.CAVITY), new SlotPoint(0, 13, SlotType.CAVITY), new SlotPoint(1, 13, SlotType.CAVITY), new SlotPoint(2, 13, SlotType.CAVITY), new SlotPoint(3, 13, SlotType.CAVITY), new SlotPoint(4, 13, SlotType.CAVITY), new SlotPoint(5, 13, SlotType.CAVITY), new SlotPoint(6, 13, SlotType.CAVITY), new SlotPoint(7, 13, SlotType.CAVITY), new SlotPoint(8, 13, SlotType.CAVITY), new SlotPoint(9, 13, SlotType.CAVITY), new SlotPoint(10, 13, SlotType.CAVITY), new SlotPoint(11, 13, SlotType.CAVITY), new SlotPoint(12, 13, SlotType.CAVITY), new SlotPoint(13, 13, SlotType.CAVITY), new SlotPoint(0, 14, SlotType.CAVITY), new SlotPoint(1, 14, SlotType.CAVITY), new SlotPoint(2, 14, SlotType.CAVITY), new SlotPoint(3, 14, SlotType.CAVITY), new SlotPoint(4, 14, SlotType.CAVITY), new SlotPoint(5, 14, SlotType.CAVITY), new SlotPoint(6, 14, SlotType.CAVITY), new SlotPoint(7, 14, SlotType.CAVITY), new SlotPoint(8, 14, SlotType.CAVITY), new SlotPoint(9, 14, SlotType.CAVITY), new SlotPoint(10, 14, SlotType.CAVITY), new SlotPoint(11, 14, SlotType.CAVITY), new SlotPoint(12, 14, SlotType.CAVITY), new SlotPoint(13, 14, SlotType.CAVITY), new SlotPoint(0, 15, SlotType.CAVITY), new SlotPoint(1, 15, SlotType.CAVITY), new SlotPoint(2, 15, SlotType.CAVITY), new SlotPoint(3, 15, SlotType.CAVITY), new SlotPoint(4, 15, SlotType.CAVITY), new SlotPoint(5, 15, SlotType.CAVITY), new SlotPoint(6, 15, SlotType.CAVITY), new SlotPoint(7, 15, SlotType.CAVITY), new SlotPoint(8, 15, SlotType.CAVITY), new SlotPoint(9, 15, SlotType.CAVITY), new SlotPoint(10, 15, SlotType.CAVITY), new SlotPoint(11, 15, SlotType.CAVITY), new SlotPoint(12, 15, SlotType.CAVITY), new SlotPoint(13, 15, SlotType.CAVITY), new SlotPoint(0, 16, SlotType.CAVITY), new SlotPoint(1, 16, SlotType.CAVITY), new SlotPoint(2, 16, SlotType.CAVITY), new SlotPoint(3, 16, SlotType.CAVITY), new SlotPoint(4, 16, SlotType.CAVITY), new SlotPoint(5, 16, SlotType.CAVITY), new SlotPoint(6, 16, SlotType.CAVITY), new SlotPoint(7, 16, SlotType.CAVITY), new SlotPoint(8, 16, SlotType.CAVITY), new SlotPoint(9, 16, SlotType.CAVITY), new SlotPoint(10, 16, SlotType.CAVITY), new SlotPoint(11, 16, SlotType.CAVITY), new SlotPoint(12, 16, SlotType.CAVITY), new SlotPoint(13, 16, SlotType.CAVITY), new SlotPoint(0, 17, SlotType.CAVITY), new SlotPoint(1, 17, SlotType.CAVITY), new SlotPoint(2, 17, SlotType.CAVITY), new SlotPoint(3, 17, SlotType.CAVITY), new SlotPoint(4, 17, SlotType.CAVITY), new SlotPoint(5, 17, SlotType.CAVITY), new SlotPoint(6, 17, SlotType.CAVITY), new SlotPoint(7, 17, SlotType.CAVITY), new SlotPoint(8, 17, SlotType.CAVITY), new SlotPoint(9, 17, SlotType.CAVITY), new SlotPoint(10, 17, SlotType.CAVITY), new SlotPoint(11, 17, SlotType.CAVITY), new SlotPoint(12, 17, SlotType.CAVITY), new SlotPoint(13, 17, SlotType.CAVITY), new SlotPoint(0, 18, SlotType.CAVITY), new SlotPoint(1, 18, SlotType.CAVITY), new SlotPoint(2, 18, SlotType.CAVITY), new SlotPoint(3, 18, SlotType.CAVITY), new SlotPoint(4, 18, SlotType.CAVITY), new SlotPoint(5, 18, SlotType.CAVITY), new SlotPoint(6, 18, SlotType.CAVITY), new SlotPoint(7, 18, SlotType.CAVITY), new SlotPoint(8, 18, SlotType.CAVITY), new SlotPoint(9, 18, SlotType.CAVITY), new SlotPoint(10, 18, SlotType.CAVITY), new SlotPoint(11, 18, SlotType.CAVITY), new SlotPoint(12, 18, SlotType.CAVITY), new SlotPoint(13, 18, SlotType.CAVITY), new SlotPoint(0, 19, SlotType.CAVITY), new SlotPoint(1, 19, SlotType.CAVITY), new SlotPoint(2, 19, SlotType.CAVITY), new SlotPoint(3, 19, SlotType.CAVITY), new SlotPoint(4, 19, SlotType.CAVITY), new SlotPoint(5, 19, SlotType.CAVITY), new SlotPoint(6, 19, SlotType.CAVITY), new SlotPoint(7, 19, SlotType.CAVITY), new SlotPoint(8, 19, SlotType.CAVITY), new SlotPoint(9, 19, SlotType.CAVITY), new SlotPoint(10, 19, SlotType.CAVITY), new SlotPoint(11, 19, SlotType.CAVITY), new SlotPoint(12, 19, SlotType.CAVITY), new SlotPoint(13, 19, SlotType.CAVITY), new SlotPoint(3, 20, SlotType.CAVITY), new SlotPoint(4, 20, SlotType.CAVITY), new SlotPoint(5, 20, SlotType.CAVITY), new SlotPoint(6, 20, SlotType.CAVITY), new SlotPoint(7, 20, SlotType.CAVITY), new SlotPoint(8, 20, SlotType.CAVITY), new SlotPoint(9, 20, SlotType.CAVITY), new SlotPoint(10, 20, SlotType.CAVITY), new SlotPoint(3, 21, SlotType.CAVITY), new SlotPoint(4, 21, SlotType.CAVITY), new SlotPoint(5, 21, SlotType.CAVITY), new SlotPoint(6, 21, SlotType.CAVITY), new SlotPoint(7, 21, SlotType.CAVITY), new SlotPoint(8, 21, SlotType.CAVITY), new SlotPoint(9, 21, SlotType.CAVITY), new SlotPoint(10, 21, SlotType.CAVITY), new SlotPoint(3, 22, SlotType.CAVITY), new SlotPoint(4, 22, SlotType.CAVITY), new SlotPoint(5, 22, SlotType.CAVITY), new SlotPoint(6, 22, SlotType.CAVITY), new SlotPoint(7, 22, SlotType.CAVITY), new SlotPoint(8, 22, SlotType.CAVITY), new SlotPoint(9, 22, SlotType.CAVITY), new SlotPoint(10, 22, SlotType.CAVITY), new SlotPoint(3, 23, SlotType.CAVITY), new SlotPoint(4, 23, SlotType.CAVITY), new SlotPoint(5, 23, SlotType.CAVITY), new SlotPoint(6, 23, SlotType.CAVITY), new SlotPoint(7, 23, SlotType.CAVITY), new SlotPoint(8, 23, SlotType.CAVITY), new SlotPoint(9, 23, SlotType.CAVITY), new SlotPoint(10, 23, SlotType.CAVITY), new SlotPoint(3, 24, SlotType.CAVITY), new SlotPoint(4, 24, SlotType.CAVITY), new SlotPoint(5, 24, SlotType.CAVITY), new SlotPoint(6, 24, SlotType.CAVITY), new SlotPoint(7, 24, SlotType.CAVITY), new SlotPoint(8, 24, SlotType.CAVITY), new SlotPoint(9, 24, SlotType.CAVITY), new SlotPoint(10, 24, SlotType.CAVITY), new SlotPoint(3, 25, SlotType.CAVITY), new SlotPoint(4, 25, SlotType.CAVITY), new SlotPoint(5, 25, SlotType.CAVITY), new SlotPoint(6, 25, SlotType.CAVITY), new SlotPoint(7, 25, SlotType.CAVITY), new SlotPoint(8, 25, SlotType.CAVITY), new SlotPoint(9, 25, SlotType.CAVITY), new SlotPoint(10, 25, SlotType.CAVITY), new SlotPoint(3, 26, SlotType.CAVITY), new SlotPoint(4, 26, SlotType.CAVITY), new SlotPoint(5, 26, SlotType.CAVITY), new SlotPoint(6, 26, SlotType.CAVITY), new SlotPoint(7, 26, SlotType.CAVITY), new SlotPoint(8, 26, SlotType.CAVITY), new SlotPoint(9, 26, SlotType.CAVITY), new SlotPoint(10, 26, SlotType.CAVITY), new SlotPoint(3, 27, SlotType.CAVITY), new SlotPoint(4, 27, SlotType.CAVITY), new SlotPoint(5, 27, SlotType.CAVITY), new SlotPoint(6, 27, SlotType.CAVITY), new SlotPoint(7, 27, SlotType.CAVITY), new SlotPoint(8, 27, SlotType.CAVITY), new SlotPoint(9, 27, SlotType.CAVITY), new SlotPoint(10, 27, SlotType.CAVITY), new SlotPoint(3, 28, SlotType.CAVITY), new SlotPoint(4, 28, SlotType.CAVITY), new SlotPoint(5, 28, SlotType.CAVITY), new SlotPoint(6, 28, SlotType.CAVITY), new SlotPoint(7, 28, SlotType.CAVITY), new SlotPoint(8, 28, SlotType.CAVITY), new SlotPoint(9, 28, SlotType.CAVITY), new SlotPoint(10, 28, SlotType.CAVITY), new SlotPoint(3, 29, SlotType.CAVITY), new SlotPoint(4, 29, SlotType.CAVITY), new SlotPoint(5, 29, SlotType.CAVITY), new SlotPoint(6, 29, SlotType.CAVITY), new SlotPoint(7, 29, SlotType.CAVITY), new SlotPoint(8, 29, SlotType.CAVITY), new SlotPoint(9, 29, SlotType.CAVITY), new SlotPoint(10, 29, SlotType.CAVITY), new SlotPoint(3, 30, SlotType.CAVITY), new SlotPoint(4, 30, SlotType.CAVITY), new SlotPoint(5, 30, SlotType.CAVITY), new SlotPoint(6, 30, SlotType.CAVITY), new SlotPoint(7, 30, SlotType.CAVITY), new SlotPoint(8, 30, SlotType.CAVITY), new SlotPoint(9, 30, SlotType.CAVITY), new SlotPoint(10, 30, SlotType.CAVITY), new SlotPoint(3, 31, SlotType.CAVITY), new SlotPoint(4, 31, SlotType.CAVITY), new SlotPoint(5, 31, SlotType.CAVITY), new SlotPoint(6, 31, SlotType.CAVITY), new SlotPoint(7, 31, SlotType.CAVITY), new SlotPoint(8, 31, SlotType.CAVITY), new SlotPoint(9, 31, SlotType.CAVITY), new SlotPoint(10, 31, SlotType.CAVITY) ))
                    )
                    .item(BitterItems.BODY_PART)
            )
    );

    public static final DeferredHolder<Compartment, Compartment> HEAD = COMPARTMENTS.register("head",
            () -> new Compartment("head", new Compartment.Properties()
                    .defaultHealth(100)
                    .layers(
                            LayerData.fromRegularShape("Scalp", 8, 8, SlotType.SKIN),
                            LayerData.fromRegularShape("Skull", 8, 8, SlotType.BONE),
                            LayerData.fromRegularShape("Brain Cavity", 8, 8, SlotType.CAVITY)
                    )
                    .item(Holder.direct(RED_WOOL))
            )
    );

    public static final DeferredHolder<Compartment, Compartment> TORSO = COMPARTMENTS.register("torso",
            () -> new Compartment("torso", new Compartment.Properties()
                    .defaultHealth(150)
                    .layers(
                            LayerData.fromRegularShape("Skin", 25, 45, 2, SlotType.SKIN),
                            LayerData.fromRegularShape("Fat", 25, 45, SlotType.FAT),
                            LayerData.fromRegularShape("Muscle", 25, 45, SlotType.MUSCLE),
                            new LayerData("Ribcage", 25, 45, List.of( new SlotPoint(5, 0, SlotType.BONE), new SlotPoint(6, 0, SlotType.BONE), new SlotPoint(7, 0, SlotType.BONE), new SlotPoint(8, 0, SlotType.BONE), new SlotPoint(16, 0, SlotType.BONE), new SlotPoint(17, 0, SlotType.BONE), new SlotPoint(18, 0, SlotType.BONE), new SlotPoint(19, 0, SlotType.BONE), new SlotPoint(4, 1, SlotType.BONE), new SlotPoint(5, 1, SlotType.MUSCLE), new SlotPoint(6, 1, SlotType.MUSCLE), new SlotPoint(7, 1, SlotType.MUSCLE), new SlotPoint(8, 1, SlotType.MUSCLE), new SlotPoint(9, 1, SlotType.BONE), new SlotPoint(10, 1, SlotType.BONE), new SlotPoint(11, 1, SlotType.BONE), new SlotPoint(12, 1, SlotType.BONE), new SlotPoint(13, 1, SlotType.BONE), new SlotPoint(14, 1, SlotType.BONE), new SlotPoint(15, 1, SlotType.BONE), new SlotPoint(16, 1, SlotType.MUSCLE), new SlotPoint(17, 1, SlotType.MUSCLE), new SlotPoint(18, 1, SlotType.MUSCLE), new SlotPoint(19, 1, SlotType.MUSCLE), new SlotPoint(20, 1, SlotType.BONE), new SlotPoint(3, 2, SlotType.BONE), new SlotPoint(4, 2, SlotType.MUSCLE), new SlotPoint(5, 2, SlotType.MUSCLE), new SlotPoint(6, 2, SlotType.MUSCLE), new SlotPoint(7, 2, SlotType.MUSCLE), new SlotPoint(8, 2, SlotType.MUSCLE), new SlotPoint(9, 2, SlotType.MUSCLE), new SlotPoint(10, 2, SlotType.MUSCLE), new SlotPoint(11, 2, SlotType.BONE), new SlotPoint(12, 2, SlotType.BONE), new SlotPoint(13, 2, SlotType.BONE), new SlotPoint(14, 2, SlotType.MUSCLE), new SlotPoint(15, 2, SlotType.MUSCLE), new SlotPoint(16, 2, SlotType.MUSCLE), new SlotPoint(17, 2, SlotType.MUSCLE), new SlotPoint(18, 2, SlotType.MUSCLE), new SlotPoint(19, 2, SlotType.MUSCLE), new SlotPoint(20, 2, SlotType.MUSCLE), new SlotPoint(21, 2, SlotType.BONE), new SlotPoint(2, 3, SlotType.BONE), new SlotPoint(3, 3, SlotType.MUSCLE), new SlotPoint(4, 3, SlotType.BONE), new SlotPoint(5, 3, SlotType.BONE), new SlotPoint(6, 3, SlotType.MUSCLE), new SlotPoint(7, 3, SlotType.MUSCLE), new SlotPoint(8, 3, SlotType.MUSCLE), new SlotPoint(9, 3, SlotType.BONE), new SlotPoint(10, 3, SlotType.BONE), new SlotPoint(11, 3, SlotType.BONE), new SlotPoint(12, 3, SlotType.BONE), new SlotPoint(13, 3, SlotType.BONE), new SlotPoint(14, 3, SlotType.BONE), new SlotPoint(15, 3, SlotType.BONE), new SlotPoint(16, 3, SlotType.MUSCLE), new SlotPoint(17, 3, SlotType.MUSCLE), new SlotPoint(18, 3, SlotType.MUSCLE), new SlotPoint(19, 3, SlotType.BONE), new SlotPoint(20, 3, SlotType.BONE), new SlotPoint(21, 3, SlotType.MUSCLE), new SlotPoint(22, 3, SlotType.BONE), new SlotPoint(1, 4, SlotType.BONE), new SlotPoint(2, 4, SlotType.MUSCLE), new SlotPoint(3, 4, SlotType.MUSCLE), new SlotPoint(4, 4, SlotType.MUSCLE), new SlotPoint(5, 4, SlotType.MUSCLE), new SlotPoint(6, 4, SlotType.BONE), new SlotPoint(7, 4, SlotType.BONE), new SlotPoint(8, 4, SlotType.BONE), new SlotPoint(9, 4, SlotType.MUSCLE), new SlotPoint(10, 4, SlotType.MUSCLE), new SlotPoint(11, 4, SlotType.BONE), new SlotPoint(12, 4, SlotType.BONE), new SlotPoint(13, 4, SlotType.BONE), new SlotPoint(14, 4, SlotType.MUSCLE), new SlotPoint(15, 4, SlotType.MUSCLE), new SlotPoint(16, 4, SlotType.BONE), new SlotPoint(17, 4, SlotType.BONE), new SlotPoint(18, 4, SlotType.BONE), new SlotPoint(19, 4, SlotType.MUSCLE), new SlotPoint(20, 4, SlotType.MUSCLE), new SlotPoint(21, 4, SlotType.MUSCLE), new SlotPoint(22, 4, SlotType.MUSCLE), new SlotPoint(23, 4, SlotType.BONE), new SlotPoint(1, 5, SlotType.BONE), new SlotPoint(2, 5, SlotType.BONE), new SlotPoint(3, 5, SlotType.BONE), new SlotPoint(4, 5, SlotType.MUSCLE), new SlotPoint(5, 5, SlotType.MUSCLE), new SlotPoint(6, 5, SlotType.MUSCLE), new SlotPoint(7, 5, SlotType.MUSCLE), new SlotPoint(8, 5, SlotType.MUSCLE), new SlotPoint(9, 5, SlotType.MUSCLE), new SlotPoint(10, 5, SlotType.MUSCLE), new SlotPoint(11, 5, SlotType.BONE), new SlotPoint(12, 5, SlotType.BONE), new SlotPoint(13, 5, SlotType.BONE), new SlotPoint(14, 5, SlotType.MUSCLE), new SlotPoint(15, 5, SlotType.MUSCLE), new SlotPoint(16, 5, SlotType.MUSCLE), new SlotPoint(17, 5, SlotType.MUSCLE), new SlotPoint(18, 5, SlotType.MUSCLE), new SlotPoint(19, 5, SlotType.MUSCLE), new SlotPoint(20, 5, SlotType.MUSCLE), new SlotPoint(21, 5, SlotType.BONE), new SlotPoint(22, 5, SlotType.BONE), new SlotPoint(23, 5, SlotType.BONE), new SlotPoint(1, 6, SlotType.BONE), new SlotPoint(2, 6, SlotType.MUSCLE), new SlotPoint(3, 6, SlotType.MUSCLE), new SlotPoint(4, 6, SlotType.BONE), new SlotPoint(5, 6, SlotType.BONE), new SlotPoint(6, 6, SlotType.MUSCLE), new SlotPoint(7, 6, SlotType.MUSCLE), new SlotPoint(8, 6, SlotType.MUSCLE), new SlotPoint(9, 6, SlotType.MUSCLE), new SlotPoint(10, 6, SlotType.BONE), new SlotPoint(11, 6, SlotType.BONE), new SlotPoint(12, 6, SlotType.BONE), new SlotPoint(13, 6, SlotType.BONE), new SlotPoint(14, 6, SlotType.BONE), new SlotPoint(15, 6, SlotType.MUSCLE), new SlotPoint(16, 6, SlotType.MUSCLE), new SlotPoint(17, 6, SlotType.MUSCLE), new SlotPoint(18, 6, SlotType.MUSCLE), new SlotPoint(19, 6, SlotType.BONE), new SlotPoint(20, 6, SlotType.BONE), new SlotPoint(21, 6, SlotType.MUSCLE), new SlotPoint(22, 6, SlotType.MUSCLE), new SlotPoint(23, 6, SlotType.BONE), new SlotPoint(1, 7, SlotType.BONE), new SlotPoint(2, 7, SlotType.MUSCLE), new SlotPoint(3, 7, SlotType.MUSCLE), new SlotPoint(4, 7, SlotType.MUSCLE), new SlotPoint(5, 7, SlotType.MUSCLE), new SlotPoint(6, 7, SlotType.BONE), new SlotPoint(7, 7, SlotType.BONE), new SlotPoint(8, 7, SlotType.BONE), new SlotPoint(9, 7, SlotType.BONE), new SlotPoint(10, 7, SlotType.MUSCLE), new SlotPoint(11, 7, SlotType.BONE), new SlotPoint(12, 7, SlotType.BONE), new SlotPoint(13, 7, SlotType.BONE), new SlotPoint(14, 7, SlotType.MUSCLE), new SlotPoint(15, 7, SlotType.BONE), new SlotPoint(16, 7, SlotType.BONE), new SlotPoint(17, 7, SlotType.BONE), new SlotPoint(18, 7, SlotType.BONE), new SlotPoint(19, 7, SlotType.MUSCLE), new SlotPoint(20, 7, SlotType.MUSCLE), new SlotPoint(21, 7, SlotType.MUSCLE), new SlotPoint(22, 7, SlotType.MUSCLE), new SlotPoint(23, 7, SlotType.BONE), new SlotPoint(1, 8, SlotType.BONE), new SlotPoint(2, 8, SlotType.BONE), new SlotPoint(3, 8, SlotType.MUSCLE), new SlotPoint(4, 8, SlotType.MUSCLE), new SlotPoint(5, 8, SlotType.MUSCLE), new SlotPoint(6, 8, SlotType.MUSCLE), new SlotPoint(7, 8, SlotType.MUSCLE), new SlotPoint(8, 8, SlotType.MUSCLE), new SlotPoint(9, 8, SlotType.MUSCLE), new SlotPoint(10, 8, SlotType.MUSCLE), new SlotPoint(11, 8, SlotType.BONE), new SlotPoint(12, 8, SlotType.BONE), new SlotPoint(13, 8, SlotType.BONE), new SlotPoint(14, 8, SlotType.MUSCLE), new SlotPoint(15, 8, SlotType.MUSCLE), new SlotPoint(16, 8, SlotType.MUSCLE), new SlotPoint(17, 8, SlotType.MUSCLE), new SlotPoint(18, 8, SlotType.MUSCLE), new SlotPoint(19, 8, SlotType.MUSCLE), new SlotPoint(20, 8, SlotType.MUSCLE), new SlotPoint(21, 8, SlotType.MUSCLE), new SlotPoint(22, 8, SlotType.BONE), new SlotPoint(23, 8, SlotType.BONE), new SlotPoint(0, 9, SlotType.BONE), new SlotPoint(1, 9, SlotType.MUSCLE), new SlotPoint(2, 9, SlotType.MUSCLE), new SlotPoint(3, 9, SlotType.BONE), new SlotPoint(4, 9, SlotType.BONE), new SlotPoint(5, 9, SlotType.MUSCLE), new SlotPoint(6, 9, SlotType.MUSCLE), new SlotPoint(7, 9, SlotType.MUSCLE), new SlotPoint(8, 9, SlotType.MUSCLE), new SlotPoint(9, 9, SlotType.MUSCLE), new SlotPoint(10, 9, SlotType.BONE), new SlotPoint(11, 9, SlotType.BONE), new SlotPoint(12, 9, SlotType.BONE), new SlotPoint(13, 9, SlotType.BONE), new SlotPoint(14, 9, SlotType.BONE), new SlotPoint(15, 9, SlotType.MUSCLE), new SlotPoint(16, 9, SlotType.MUSCLE), new SlotPoint(17, 9, SlotType.MUSCLE), new SlotPoint(18, 9, SlotType.MUSCLE), new SlotPoint(19, 9, SlotType.MUSCLE), new SlotPoint(20, 9, SlotType.BONE), new SlotPoint(21, 9, SlotType.BONE), new SlotPoint(22, 9, SlotType.MUSCLE), new SlotPoint(23, 9, SlotType.MUSCLE), new SlotPoint(24, 9, SlotType.BONE), new SlotPoint(0, 10, SlotType.BONE), new SlotPoint(1, 10, SlotType.MUSCLE), new SlotPoint(2, 10, SlotType.MUSCLE), new SlotPoint(3, 10, SlotType.MUSCLE), new SlotPoint(4, 10, SlotType.MUSCLE), new SlotPoint(5, 10, SlotType.BONE), new SlotPoint(6, 10, SlotType.BONE), new SlotPoint(7, 10, SlotType.BONE), new SlotPoint(8, 10, SlotType.BONE), new SlotPoint(9, 10, SlotType.BONE), new SlotPoint(10, 10, SlotType.MUSCLE), new SlotPoint(11, 10, SlotType.BONE), new SlotPoint(12, 10, SlotType.BONE), new SlotPoint(13, 10, SlotType.BONE), new SlotPoint(14, 10, SlotType.MUSCLE), new SlotPoint(15, 10, SlotType.BONE), new SlotPoint(16, 10, SlotType.BONE), new SlotPoint(17, 10, SlotType.BONE), new SlotPoint(18, 10, SlotType.BONE), new SlotPoint(19, 10, SlotType.BONE), new SlotPoint(20, 10, SlotType.MUSCLE), new SlotPoint(21, 10, SlotType.MUSCLE), new SlotPoint(22, 10, SlotType.MUSCLE), new SlotPoint(23, 10, SlotType.MUSCLE), new SlotPoint(24, 10, SlotType.BONE), new SlotPoint(0, 11, SlotType.BONE), new SlotPoint(1, 11, SlotType.BONE), new SlotPoint(2, 11, SlotType.MUSCLE), new SlotPoint(3, 11, SlotType.MUSCLE), new SlotPoint(4, 11, SlotType.MUSCLE), new SlotPoint(5, 11, SlotType.MUSCLE), new SlotPoint(6, 11, SlotType.MUSCLE), new SlotPoint(7, 11, SlotType.MUSCLE), new SlotPoint(8, 11, SlotType.MUSCLE), new SlotPoint(9, 11, SlotType.MUSCLE), new SlotPoint(10, 11, SlotType.BONE), new SlotPoint(11, 11, SlotType.BONE), new SlotPoint(12, 11, SlotType.BONE), new SlotPoint(13, 11, SlotType.BONE), new SlotPoint(14, 11, SlotType.BONE), new SlotPoint(15, 11, SlotType.MUSCLE), new SlotPoint(16, 11, SlotType.MUSCLE), new SlotPoint(17, 11, SlotType.MUSCLE), new SlotPoint(18, 11, SlotType.MUSCLE), new SlotPoint(19, 11, SlotType.MUSCLE), new SlotPoint(20, 11, SlotType.MUSCLE), new SlotPoint(21, 11, SlotType.MUSCLE), new SlotPoint(22, 11, SlotType.MUSCLE), new SlotPoint(23, 11, SlotType.BONE), new SlotPoint(24, 11, SlotType.BONE), new SlotPoint(0, 12, SlotType.BONE), new SlotPoint(1, 12, SlotType.MUSCLE), new SlotPoint(2, 12, SlotType.BONE), new SlotPoint(3, 12, SlotType.BONE), new SlotPoint(4, 12, SlotType.MUSCLE), new SlotPoint(5, 12, SlotType.MUSCLE), new SlotPoint(6, 12, SlotType.MUSCLE), new SlotPoint(7, 12, SlotType.MUSCLE), new SlotPoint(8, 12, SlotType.BONE), new SlotPoint(9, 12, SlotType.BONE), new SlotPoint(10, 12, SlotType.MUSCLE), new SlotPoint(11, 12, SlotType.BONE), new SlotPoint(12, 12, SlotType.BONE), new SlotPoint(13, 12, SlotType.BONE), new SlotPoint(14, 12, SlotType.MUSCLE), new SlotPoint(15, 12, SlotType.BONE), new SlotPoint(16, 12, SlotType.BONE), new SlotPoint(17, 12, SlotType.MUSCLE), new SlotPoint(18, 12, SlotType.MUSCLE), new SlotPoint(19, 12, SlotType.MUSCLE), new SlotPoint(20, 12, SlotType.MUSCLE), new SlotPoint(21, 12, SlotType.BONE), new SlotPoint(22, 12, SlotType.BONE), new SlotPoint(23, 12, SlotType.MUSCLE), new SlotPoint(24, 12, SlotType.BONE), new SlotPoint(0, 13, SlotType.BONE), new SlotPoint(1, 13, SlotType.MUSCLE), new SlotPoint(2, 13, SlotType.MUSCLE), new SlotPoint(3, 13, SlotType.MUSCLE), new SlotPoint(4, 13, SlotType.BONE), new SlotPoint(5, 13, SlotType.BONE), new SlotPoint(6, 13, SlotType.BONE), new SlotPoint(7, 13, SlotType.BONE), new SlotPoint(8, 13, SlotType.MUSCLE), new SlotPoint(9, 13, SlotType.MUSCLE), new SlotPoint(10, 13, SlotType.MUSCLE), new SlotPoint(11, 13, SlotType.BONE), new SlotPoint(12, 13, SlotType.BONE), new SlotPoint(13, 13, SlotType.BONE), new SlotPoint(14, 13, SlotType.MUSCLE), new SlotPoint(15, 13, SlotType.MUSCLE), new SlotPoint(16, 13, SlotType.MUSCLE), new SlotPoint(17, 13, SlotType.BONE), new SlotPoint(18, 13, SlotType.BONE), new SlotPoint(19, 13, SlotType.BONE), new SlotPoint(20, 13, SlotType.BONE), new SlotPoint(21, 13, SlotType.MUSCLE), new SlotPoint(22, 13, SlotType.MUSCLE), new SlotPoint(23, 13, SlotType.MUSCLE), new SlotPoint(24, 13, SlotType.BONE), new SlotPoint(0, 14, SlotType.BONE), new SlotPoint(1, 14, SlotType.BONE), new SlotPoint(2, 14, SlotType.MUSCLE), new SlotPoint(3, 14, SlotType.MUSCLE), new SlotPoint(4, 14, SlotType.MUSCLE), new SlotPoint(5, 14, SlotType.MUSCLE), new SlotPoint(6, 14, SlotType.MUSCLE), new SlotPoint(7, 14, SlotType.MUSCLE), new SlotPoint(8, 14, SlotType.MUSCLE), new SlotPoint(9, 14, SlotType.MUSCLE), new SlotPoint(10, 14, SlotType.MUSCLE), new SlotPoint(11, 14, SlotType.BONE), new SlotPoint(12, 14, SlotType.BONE), new SlotPoint(13, 14, SlotType.BONE), new SlotPoint(14, 14, SlotType.MUSCLE), new SlotPoint(15, 14, SlotType.MUSCLE), new SlotPoint(16, 14, SlotType.MUSCLE), new SlotPoint(17, 14, SlotType.MUSCLE), new SlotPoint(18, 14, SlotType.MUSCLE), new SlotPoint(19, 14, SlotType.MUSCLE), new SlotPoint(20, 14, SlotType.MUSCLE), new SlotPoint(21, 14, SlotType.MUSCLE), new SlotPoint(22, 14, SlotType.MUSCLE), new SlotPoint(23, 14, SlotType.BONE), new SlotPoint(24, 14, SlotType.BONE), new SlotPoint(0, 15, SlotType.BONE), new SlotPoint(1, 15, SlotType.MUSCLE), new SlotPoint(2, 15, SlotType.BONE), new SlotPoint(3, 15, SlotType.BONE), new SlotPoint(4, 15, SlotType.MUSCLE), new SlotPoint(5, 15, SlotType.MUSCLE), new SlotPoint(6, 15, SlotType.MUSCLE), new SlotPoint(7, 15, SlotType.MUSCLE), new SlotPoint(8, 15, SlotType.MUSCLE), new SlotPoint(9, 15, SlotType.BONE), new SlotPoint(10, 15, SlotType.BONE), new SlotPoint(11, 15, SlotType.CAVITY), new SlotPoint(12, 15, SlotType.BONE), new SlotPoint(13, 15, SlotType.CAVITY), new SlotPoint(14, 15, SlotType.BONE), new SlotPoint(15, 15, SlotType.BONE), new SlotPoint(16, 15, SlotType.MUSCLE), new SlotPoint(17, 15, SlotType.MUSCLE), new SlotPoint(18, 15, SlotType.MUSCLE), new SlotPoint(19, 15, SlotType.MUSCLE), new SlotPoint(20, 15, SlotType.MUSCLE), new SlotPoint(21, 15, SlotType.BONE), new SlotPoint(22, 15, SlotType.BONE), new SlotPoint(23, 15, SlotType.MUSCLE), new SlotPoint(24, 15, SlotType.BONE), new SlotPoint(0, 16, SlotType.BONE), new SlotPoint(1, 16, SlotType.MUSCLE), new SlotPoint(2, 16, SlotType.MUSCLE), new SlotPoint(3, 16, SlotType.MUSCLE), new SlotPoint(4, 16, SlotType.BONE), new SlotPoint(5, 16, SlotType.BONE), new SlotPoint(6, 16, SlotType.BONE), new SlotPoint(7, 16, SlotType.BONE), new SlotPoint(8, 16, SlotType.BONE), new SlotPoint(9, 16, SlotType.CAVITY), new SlotPoint(10, 16, SlotType.CAVITY), new SlotPoint(11, 16, SlotType.CAVITY), new SlotPoint(12, 16, SlotType.BONE), new SlotPoint(13, 16, SlotType.CAVITY), new SlotPoint(14, 16, SlotType.CAVITY), new SlotPoint(15, 16, SlotType.CAVITY), new SlotPoint(16, 16, SlotType.BONE), new SlotPoint(17, 16, SlotType.BONE), new SlotPoint(18, 16, SlotType.BONE), new SlotPoint(19, 16, SlotType.BONE), new SlotPoint(20, 16, SlotType.BONE), new SlotPoint(21, 16, SlotType.MUSCLE), new SlotPoint(22, 16, SlotType.MUSCLE), new SlotPoint(23, 16, SlotType.MUSCLE), new SlotPoint(24, 16, SlotType.BONE), new SlotPoint(0, 17, SlotType.BONE), new SlotPoint(1, 17, SlotType.BONE), new SlotPoint(2, 17, SlotType.MUSCLE), new SlotPoint(3, 17, SlotType.MUSCLE), new SlotPoint(4, 17, SlotType.MUSCLE), new SlotPoint(5, 17, SlotType.MUSCLE), new SlotPoint(6, 17, SlotType.BONE), new SlotPoint(7, 17, SlotType.BONE), new SlotPoint(8, 17, SlotType.CAVITY), new SlotPoint(9, 17, SlotType.CAVITY), new SlotPoint(10, 17, SlotType.CAVITY), new SlotPoint(11, 17, SlotType.CAVITY), new SlotPoint(12, 17, SlotType.CAVITY), new SlotPoint(13, 17, SlotType.CAVITY), new SlotPoint(14, 17, SlotType.CAVITY), new SlotPoint(15, 17, SlotType.CAVITY), new SlotPoint(16, 17, SlotType.CAVITY), new SlotPoint(17, 17, SlotType.BONE), new SlotPoint(18, 17, SlotType.BONE), new SlotPoint(19, 17, SlotType.MUSCLE), new SlotPoint(20, 17, SlotType.MUSCLE), new SlotPoint(21, 17, SlotType.MUSCLE), new SlotPoint(22, 17, SlotType.MUSCLE), new SlotPoint(23, 17, SlotType.BONE), new SlotPoint(24, 17, SlotType.BONE), new SlotPoint(0, 18, SlotType.BONE), new SlotPoint(1, 18, SlotType.MUSCLE), new SlotPoint(2, 18, SlotType.BONE), new SlotPoint(3, 18, SlotType.BONE), new SlotPoint(4, 18, SlotType.BONE), new SlotPoint(5, 18, SlotType.BONE), new SlotPoint(6, 18, SlotType.BONE), new SlotPoint(7, 18, SlotType.CAVITY), new SlotPoint(8, 18, SlotType.CAVITY), new SlotPoint(9, 18, SlotType.CAVITY), new SlotPoint(10, 18, SlotType.CAVITY), new SlotPoint(11, 18, SlotType.CAVITY), new SlotPoint(12, 18, SlotType.CAVITY), new SlotPoint(13, 18, SlotType.CAVITY), new SlotPoint(14, 18, SlotType.CAVITY), new SlotPoint(15, 18, SlotType.CAVITY), new SlotPoint(16, 18, SlotType.CAVITY), new SlotPoint(17, 18, SlotType.CAVITY), new SlotPoint(18, 18, SlotType.BONE), new SlotPoint(19, 18, SlotType.BONE), new SlotPoint(20, 18, SlotType.BONE), new SlotPoint(21, 18, SlotType.BONE), new SlotPoint(22, 18, SlotType.BONE), new SlotPoint(23, 18, SlotType.MUSCLE), new SlotPoint(24, 18, SlotType.BONE), new SlotPoint(0, 19, SlotType.BONE), new SlotPoint(1, 19, SlotType.MUSCLE), new SlotPoint(2, 19, SlotType.MUSCLE), new SlotPoint(3, 19, SlotType.MUSCLE), new SlotPoint(4, 19, SlotType.MUSCLE), new SlotPoint(5, 19, SlotType.BONE), new SlotPoint(6, 19, SlotType.CAVITY), new SlotPoint(7, 19, SlotType.CAVITY), new SlotPoint(8, 19, SlotType.CAVITY), new SlotPoint(9, 19, SlotType.CAVITY), new SlotPoint(10, 19, SlotType.CAVITY), new SlotPoint(11, 19, SlotType.CAVITY), new SlotPoint(12, 19, SlotType.CAVITY), new SlotPoint(13, 19, SlotType.CAVITY), new SlotPoint(14, 19, SlotType.CAVITY), new SlotPoint(15, 19, SlotType.CAVITY), new SlotPoint(16, 19, SlotType.CAVITY), new SlotPoint(17, 19, SlotType.CAVITY), new SlotPoint(18, 19, SlotType.CAVITY), new SlotPoint(19, 19, SlotType.BONE), new SlotPoint(20, 19, SlotType.MUSCLE), new SlotPoint(21, 19, SlotType.MUSCLE), new SlotPoint(22, 19, SlotType.MUSCLE), new SlotPoint(23, 19, SlotType.MUSCLE), new SlotPoint(24, 19, SlotType.BONE), new SlotPoint(1, 20, SlotType.BONE), new SlotPoint(2, 20, SlotType.MUSCLE), new SlotPoint(3, 20, SlotType.BONE), new SlotPoint(4, 20, SlotType.BONE), new SlotPoint(5, 20, SlotType.BONE), new SlotPoint(6, 20, SlotType.CAVITY), new SlotPoint(7, 20, SlotType.CAVITY), new SlotPoint(8, 20, SlotType.CAVITY), new SlotPoint(9, 20, SlotType.CAVITY), new SlotPoint(10, 20, SlotType.CAVITY), new SlotPoint(11, 20, SlotType.CAVITY), new SlotPoint(12, 20, SlotType.CAVITY), new SlotPoint(13, 20, SlotType.CAVITY), new SlotPoint(14, 20, SlotType.CAVITY), new SlotPoint(15, 20, SlotType.CAVITY), new SlotPoint(16, 20, SlotType.CAVITY), new SlotPoint(17, 20, SlotType.CAVITY), new SlotPoint(18, 20, SlotType.CAVITY), new SlotPoint(19, 20, SlotType.BONE), new SlotPoint(20, 20, SlotType.BONE), new SlotPoint(21, 20, SlotType.BONE), new SlotPoint(22, 20, SlotType.MUSCLE), new SlotPoint(23, 20, SlotType.BONE), new SlotPoint(1, 21, SlotType.BONE), new SlotPoint(2, 21, SlotType.BONE), new SlotPoint(3, 21, SlotType.MUSCLE), new SlotPoint(4, 21, SlotType.MUSCLE), new SlotPoint(5, 21, SlotType.BONE), new SlotPoint(6, 21, SlotType.CAVITY), new SlotPoint(7, 21, SlotType.CAVITY), new SlotPoint(8, 21, SlotType.CAVITY), new SlotPoint(9, 21, SlotType.CAVITY), new SlotPoint(10, 21, SlotType.CAVITY), new SlotPoint(11, 21, SlotType.CAVITY), new SlotPoint(12, 21, SlotType.CAVITY), new SlotPoint(13, 21, SlotType.CAVITY), new SlotPoint(14, 21, SlotType.CAVITY), new SlotPoint(15, 21, SlotType.CAVITY), new SlotPoint(16, 21, SlotType.CAVITY), new SlotPoint(17, 21, SlotType.CAVITY), new SlotPoint(18, 21, SlotType.CAVITY), new SlotPoint(19, 21, SlotType.BONE), new SlotPoint(20, 21, SlotType.MUSCLE), new SlotPoint(21, 21, SlotType.MUSCLE), new SlotPoint(22, 21, SlotType.BONE), new SlotPoint(23, 21, SlotType.BONE), new SlotPoint(1, 22, SlotType.BONE), new SlotPoint(2, 22, SlotType.MUSCLE), new SlotPoint(3, 22, SlotType.MUSCLE), new SlotPoint(4, 22, SlotType.BONE), new SlotPoint(5, 22, SlotType.CAVITY), new SlotPoint(6, 22, SlotType.CAVITY), new SlotPoint(7, 22, SlotType.CAVITY), new SlotPoint(8, 22, SlotType.CAVITY), new SlotPoint(9, 22, SlotType.CAVITY), new SlotPoint(10, 22, SlotType.CAVITY), new SlotPoint(11, 22, SlotType.CAVITY), new SlotPoint(12, 22, SlotType.CAVITY), new SlotPoint(13, 22, SlotType.CAVITY), new SlotPoint(14, 22, SlotType.CAVITY), new SlotPoint(15, 22, SlotType.CAVITY), new SlotPoint(16, 22, SlotType.CAVITY), new SlotPoint(17, 22, SlotType.CAVITY), new SlotPoint(18, 22, SlotType.CAVITY), new SlotPoint(19, 22, SlotType.CAVITY), new SlotPoint(20, 22, SlotType.BONE), new SlotPoint(21, 22, SlotType.MUSCLE), new SlotPoint(22, 22, SlotType.MUSCLE), new SlotPoint(23, 22, SlotType.BONE), new SlotPoint(1, 23, SlotType.BONE), new SlotPoint(2, 23, SlotType.BONE), new SlotPoint(3, 23, SlotType.BONE), new SlotPoint(4, 23, SlotType.BONE), new SlotPoint(5, 23, SlotType.CAVITY), new SlotPoint(6, 23, SlotType.CAVITY), new SlotPoint(7, 23, SlotType.CAVITY), new SlotPoint(8, 23, SlotType.CAVITY), new SlotPoint(9, 23, SlotType.CAVITY), new SlotPoint(10, 23, SlotType.CAVITY), new SlotPoint(11, 23, SlotType.CAVITY), new SlotPoint(12, 23, SlotType.CAVITY), new SlotPoint(13, 23, SlotType.CAVITY), new SlotPoint(14, 23, SlotType.CAVITY), new SlotPoint(15, 23, SlotType.CAVITY), new SlotPoint(16, 23, SlotType.CAVITY), new SlotPoint(17, 23, SlotType.CAVITY), new SlotPoint(18, 23, SlotType.CAVITY), new SlotPoint(19, 23, SlotType.CAVITY), new SlotPoint(20, 23, SlotType.BONE), new SlotPoint(21, 23, SlotType.BONE), new SlotPoint(22, 23, SlotType.BONE), new SlotPoint(23, 23, SlotType.BONE), new SlotPoint(1, 24, SlotType.BONE), new SlotPoint(2, 24, SlotType.MUSCLE), new SlotPoint(3, 24, SlotType.BONE), new SlotPoint(4, 24, SlotType.CAVITY), new SlotPoint(5, 24, SlotType.CAVITY), new SlotPoint(6, 24, SlotType.CAVITY), new SlotPoint(7, 24, SlotType.CAVITY), new SlotPoint(8, 24, SlotType.CAVITY), new SlotPoint(9, 24, SlotType.CAVITY), new SlotPoint(10, 24, SlotType.CAVITY), new SlotPoint(11, 24, SlotType.CAVITY), new SlotPoint(12, 24, SlotType.CAVITY), new SlotPoint(13, 24, SlotType.CAVITY), new SlotPoint(14, 24, SlotType.CAVITY), new SlotPoint(15, 24, SlotType.CAVITY), new SlotPoint(16, 24, SlotType.CAVITY), new SlotPoint(17, 24, SlotType.CAVITY), new SlotPoint(18, 24, SlotType.CAVITY), new SlotPoint(19, 24, SlotType.CAVITY), new SlotPoint(20, 24, SlotType.CAVITY), new SlotPoint(21, 24, SlotType.BONE), new SlotPoint(22, 24, SlotType.MUSCLE), new SlotPoint(23, 24, SlotType.BONE), new SlotPoint(1, 25, SlotType.BONE), new SlotPoint(2, 25, SlotType.BONE), new SlotPoint(3, 25, SlotType.CAVITY), new SlotPoint(4, 25, SlotType.CAVITY), new SlotPoint(5, 25, SlotType.CAVITY), new SlotPoint(6, 25, SlotType.CAVITY), new SlotPoint(7, 25, SlotType.CAVITY), new SlotPoint(8, 25, SlotType.CAVITY), new SlotPoint(9, 25, SlotType.CAVITY), new SlotPoint(10, 25, SlotType.CAVITY), new SlotPoint(11, 25, SlotType.CAVITY), new SlotPoint(12, 25, SlotType.CAVITY), new SlotPoint(13, 25, SlotType.CAVITY), new SlotPoint(14, 25, SlotType.CAVITY), new SlotPoint(15, 25, SlotType.CAVITY), new SlotPoint(16, 25, SlotType.CAVITY), new SlotPoint(17, 25, SlotType.CAVITY), new SlotPoint(18, 25, SlotType.CAVITY), new SlotPoint(19, 25, SlotType.CAVITY), new SlotPoint(20, 25, SlotType.CAVITY), new SlotPoint(21, 25, SlotType.CAVITY), new SlotPoint(22, 25, SlotType.BONE), new SlotPoint(23, 25, SlotType.BONE), new SlotPoint(1, 26, SlotType.BONE), new SlotPoint(2, 26, SlotType.CAVITY), new SlotPoint(3, 26, SlotType.CAVITY), new SlotPoint(4, 26, SlotType.CAVITY), new SlotPoint(5, 26, SlotType.CAVITY), new SlotPoint(6, 26, SlotType.CAVITY), new SlotPoint(7, 26, SlotType.CAVITY), new SlotPoint(8, 26, SlotType.CAVITY), new SlotPoint(9, 26, SlotType.CAVITY), new SlotPoint(10, 26, SlotType.CAVITY), new SlotPoint(11, 26, SlotType.CAVITY), new SlotPoint(12, 26, SlotType.CAVITY), new SlotPoint(13, 26, SlotType.CAVITY), new SlotPoint(14, 26, SlotType.CAVITY), new SlotPoint(15, 26, SlotType.CAVITY), new SlotPoint(16, 26, SlotType.CAVITY), new SlotPoint(17, 26, SlotType.CAVITY), new SlotPoint(18, 26, SlotType.CAVITY), new SlotPoint(19, 26, SlotType.CAVITY), new SlotPoint(20, 26, SlotType.CAVITY), new SlotPoint(21, 26, SlotType.CAVITY), new SlotPoint(22, 26, SlotType.CAVITY), new SlotPoint(23, 26, SlotType.BONE), new SlotPoint(1, 27, SlotType.CAVITY), new SlotPoint(2, 27, SlotType.CAVITY), new SlotPoint(3, 27, SlotType.CAVITY), new SlotPoint(4, 27, SlotType.CAVITY), new SlotPoint(5, 27, SlotType.CAVITY), new SlotPoint(6, 27, SlotType.CAVITY), new SlotPoint(7, 27, SlotType.CAVITY), new SlotPoint(8, 27, SlotType.CAVITY), new SlotPoint(9, 27, SlotType.CAVITY), new SlotPoint(10, 27, SlotType.CAVITY), new SlotPoint(11, 27, SlotType.CAVITY), new SlotPoint(12, 27, SlotType.CAVITY), new SlotPoint(13, 27, SlotType.CAVITY), new SlotPoint(14, 27, SlotType.CAVITY), new SlotPoint(15, 27, SlotType.CAVITY), new SlotPoint(16, 27, SlotType.CAVITY), new SlotPoint(17, 27, SlotType.CAVITY), new SlotPoint(18, 27, SlotType.CAVITY), new SlotPoint(19, 27, SlotType.CAVITY), new SlotPoint(20, 27, SlotType.CAVITY), new SlotPoint(21, 27, SlotType.CAVITY), new SlotPoint(22, 27, SlotType.CAVITY), new SlotPoint(23, 27, SlotType.CAVITY), new SlotPoint(1, 28, SlotType.CAVITY), new SlotPoint(2, 28, SlotType.CAVITY), new SlotPoint(3, 28, SlotType.CAVITY), new SlotPoint(4, 28, SlotType.CAVITY), new SlotPoint(5, 28, SlotType.CAVITY), new SlotPoint(6, 28, SlotType.CAVITY), new SlotPoint(7, 28, SlotType.CAVITY), new SlotPoint(8, 28, SlotType.CAVITY), new SlotPoint(9, 28, SlotType.CAVITY), new SlotPoint(10, 28, SlotType.CAVITY), new SlotPoint(11, 28, SlotType.CAVITY), new SlotPoint(12, 28, SlotType.CAVITY), new SlotPoint(13, 28, SlotType.CAVITY), new SlotPoint(14, 28, SlotType.CAVITY), new SlotPoint(15, 28, SlotType.CAVITY), new SlotPoint(16, 28, SlotType.CAVITY), new SlotPoint(17, 28, SlotType.CAVITY), new SlotPoint(18, 28, SlotType.CAVITY), new SlotPoint(19, 28, SlotType.CAVITY), new SlotPoint(20, 28, SlotType.CAVITY), new SlotPoint(21, 28, SlotType.CAVITY), new SlotPoint(22, 28, SlotType.CAVITY), new SlotPoint(23, 28, SlotType.CAVITY), new SlotPoint(1, 29, SlotType.CAVITY), new SlotPoint(2, 29, SlotType.CAVITY), new SlotPoint(3, 29, SlotType.CAVITY), new SlotPoint(4, 29, SlotType.CAVITY), new SlotPoint(5, 29, SlotType.CAVITY), new SlotPoint(6, 29, SlotType.CAVITY), new SlotPoint(7, 29, SlotType.CAVITY), new SlotPoint(8, 29, SlotType.CAVITY), new SlotPoint(9, 29, SlotType.CAVITY), new SlotPoint(10, 29, SlotType.CAVITY), new SlotPoint(11, 29, SlotType.CAVITY), new SlotPoint(12, 29, SlotType.CAVITY), new SlotPoint(13, 29, SlotType.CAVITY), new SlotPoint(14, 29, SlotType.CAVITY), new SlotPoint(15, 29, SlotType.CAVITY), new SlotPoint(16, 29, SlotType.CAVITY), new SlotPoint(17, 29, SlotType.CAVITY), new SlotPoint(18, 29, SlotType.CAVITY), new SlotPoint(19, 29, SlotType.CAVITY), new SlotPoint(20, 29, SlotType.CAVITY), new SlotPoint(21, 29, SlotType.CAVITY), new SlotPoint(22, 29, SlotType.CAVITY), new SlotPoint(23, 29, SlotType.CAVITY), new SlotPoint(1, 30, SlotType.CAVITY), new SlotPoint(2, 30, SlotType.CAVITY), new SlotPoint(3, 30, SlotType.CAVITY), new SlotPoint(4, 30, SlotType.CAVITY), new SlotPoint(5, 30, SlotType.CAVITY), new SlotPoint(6, 30, SlotType.CAVITY), new SlotPoint(7, 30, SlotType.CAVITY), new SlotPoint(8, 30, SlotType.CAVITY), new SlotPoint(9, 30, SlotType.CAVITY), new SlotPoint(10, 30, SlotType.CAVITY), new SlotPoint(11, 30, SlotType.CAVITY), new SlotPoint(12, 30, SlotType.CAVITY), new SlotPoint(13, 30, SlotType.CAVITY), new SlotPoint(14, 30, SlotType.CAVITY), new SlotPoint(15, 30, SlotType.CAVITY), new SlotPoint(16, 30, SlotType.CAVITY), new SlotPoint(17, 30, SlotType.CAVITY), new SlotPoint(18, 30, SlotType.CAVITY), new SlotPoint(19, 30, SlotType.CAVITY), new SlotPoint(20, 30, SlotType.CAVITY), new SlotPoint(21, 30, SlotType.CAVITY), new SlotPoint(22, 30, SlotType.CAVITY), new SlotPoint(23, 30, SlotType.CAVITY), new SlotPoint(1, 31, SlotType.CAVITY), new SlotPoint(2, 31, SlotType.CAVITY), new SlotPoint(3, 31, SlotType.CAVITY), new SlotPoint(4, 31, SlotType.CAVITY), new SlotPoint(5, 31, SlotType.CAVITY), new SlotPoint(6, 31, SlotType.CAVITY), new SlotPoint(7, 31, SlotType.CAVITY), new SlotPoint(8, 31, SlotType.CAVITY), new SlotPoint(9, 31, SlotType.CAVITY), new SlotPoint(10, 31, SlotType.CAVITY), new SlotPoint(11, 31, SlotType.CAVITY), new SlotPoint(12, 31, SlotType.CAVITY), new SlotPoint(13, 31, SlotType.CAVITY), new SlotPoint(14, 31, SlotType.CAVITY), new SlotPoint(15, 31, SlotType.CAVITY), new SlotPoint(16, 31, SlotType.CAVITY), new SlotPoint(17, 31, SlotType.CAVITY), new SlotPoint(18, 31, SlotType.CAVITY), new SlotPoint(19, 31, SlotType.CAVITY), new SlotPoint(20, 31, SlotType.CAVITY), new SlotPoint(21, 31, SlotType.CAVITY), new SlotPoint(22, 31, SlotType.CAVITY), new SlotPoint(23, 31, SlotType.CAVITY), new SlotPoint(1, 32, SlotType.CAVITY), new SlotPoint(2, 32, SlotType.CAVITY), new SlotPoint(3, 32, SlotType.CAVITY), new SlotPoint(4, 32, SlotType.CAVITY), new SlotPoint(5, 32, SlotType.CAVITY), new SlotPoint(6, 32, SlotType.CAVITY), new SlotPoint(7, 32, SlotType.CAVITY), new SlotPoint(8, 32, SlotType.CAVITY), new SlotPoint(9, 32, SlotType.CAVITY), new SlotPoint(10, 32, SlotType.CAVITY), new SlotPoint(11, 32, SlotType.CAVITY), new SlotPoint(12, 32, SlotType.CAVITY), new SlotPoint(13, 32, SlotType.CAVITY), new SlotPoint(14, 32, SlotType.CAVITY), new SlotPoint(15, 32, SlotType.CAVITY), new SlotPoint(16, 32, SlotType.CAVITY), new SlotPoint(17, 32, SlotType.CAVITY), new SlotPoint(18, 32, SlotType.CAVITY), new SlotPoint(19, 32, SlotType.CAVITY), new SlotPoint(20, 32, SlotType.CAVITY), new SlotPoint(21, 32, SlotType.CAVITY), new SlotPoint(22, 32, SlotType.CAVITY), new SlotPoint(23, 32, SlotType.CAVITY), new SlotPoint(1, 33, SlotType.CAVITY), new SlotPoint(2, 33, SlotType.CAVITY), new SlotPoint(3, 33, SlotType.CAVITY), new SlotPoint(4, 33, SlotType.CAVITY), new SlotPoint(5, 33, SlotType.CAVITY), new SlotPoint(6, 33, SlotType.CAVITY), new SlotPoint(7, 33, SlotType.CAVITY), new SlotPoint(8, 33, SlotType.CAVITY), new SlotPoint(9, 33, SlotType.CAVITY), new SlotPoint(10, 33, SlotType.CAVITY), new SlotPoint(11, 33, SlotType.CAVITY), new SlotPoint(12, 33, SlotType.CAVITY), new SlotPoint(13, 33, SlotType.CAVITY), new SlotPoint(14, 33, SlotType.CAVITY), new SlotPoint(15, 33, SlotType.CAVITY), new SlotPoint(16, 33, SlotType.CAVITY), new SlotPoint(17, 33, SlotType.CAVITY), new SlotPoint(18, 33, SlotType.CAVITY), new SlotPoint(19, 33, SlotType.CAVITY), new SlotPoint(20, 33, SlotType.CAVITY), new SlotPoint(21, 33, SlotType.CAVITY), new SlotPoint(22, 33, SlotType.CAVITY), new SlotPoint(23, 33, SlotType.CAVITY), new SlotPoint(1, 34, SlotType.CAVITY), new SlotPoint(2, 34, SlotType.CAVITY), new SlotPoint(3, 34, SlotType.CAVITY), new SlotPoint(4, 34, SlotType.CAVITY), new SlotPoint(5, 34, SlotType.CAVITY), new SlotPoint(6, 34, SlotType.CAVITY), new SlotPoint(7, 34, SlotType.CAVITY), new SlotPoint(8, 34, SlotType.CAVITY), new SlotPoint(9, 34, SlotType.CAVITY), new SlotPoint(10, 34, SlotType.CAVITY), new SlotPoint(11, 34, SlotType.CAVITY), new SlotPoint(12, 34, SlotType.CAVITY), new SlotPoint(13, 34, SlotType.CAVITY), new SlotPoint(14, 34, SlotType.CAVITY), new SlotPoint(15, 34, SlotType.CAVITY), new SlotPoint(16, 34, SlotType.CAVITY), new SlotPoint(17, 34, SlotType.CAVITY), new SlotPoint(18, 34, SlotType.CAVITY), new SlotPoint(19, 34, SlotType.CAVITY), new SlotPoint(20, 34, SlotType.CAVITY), new SlotPoint(21, 34, SlotType.CAVITY), new SlotPoint(22, 34, SlotType.CAVITY), new SlotPoint(23, 34, SlotType.CAVITY), new SlotPoint(1, 35, SlotType.CAVITY), new SlotPoint(2, 35, SlotType.CAVITY), new SlotPoint(3, 35, SlotType.CAVITY), new SlotPoint(4, 35, SlotType.CAVITY), new SlotPoint(5, 35, SlotType.CAVITY), new SlotPoint(6, 35, SlotType.CAVITY), new SlotPoint(7, 35, SlotType.CAVITY), new SlotPoint(8, 35, SlotType.CAVITY), new SlotPoint(9, 35, SlotType.CAVITY), new SlotPoint(10, 35, SlotType.CAVITY), new SlotPoint(11, 35, SlotType.CAVITY), new SlotPoint(12, 35, SlotType.CAVITY), new SlotPoint(13, 35, SlotType.CAVITY), new SlotPoint(14, 35, SlotType.CAVITY), new SlotPoint(15, 35, SlotType.CAVITY), new SlotPoint(16, 35, SlotType.CAVITY), new SlotPoint(17, 35, SlotType.CAVITY), new SlotPoint(18, 35, SlotType.CAVITY), new SlotPoint(19, 35, SlotType.CAVITY), new SlotPoint(20, 35, SlotType.CAVITY), new SlotPoint(21, 35, SlotType.CAVITY), new SlotPoint(22, 35, SlotType.CAVITY), new SlotPoint(23, 35, SlotType.CAVITY), new SlotPoint(1, 36, SlotType.CAVITY), new SlotPoint(2, 36, SlotType.CAVITY), new SlotPoint(3, 36, SlotType.CAVITY), new SlotPoint(4, 36, SlotType.CAVITY), new SlotPoint(5, 36, SlotType.CAVITY), new SlotPoint(6, 36, SlotType.CAVITY), new SlotPoint(7, 36, SlotType.CAVITY), new SlotPoint(8, 36, SlotType.CAVITY), new SlotPoint(9, 36, SlotType.CAVITY), new SlotPoint(10, 36, SlotType.CAVITY), new SlotPoint(11, 36, SlotType.CAVITY), new SlotPoint(12, 36, SlotType.CAVITY), new SlotPoint(13, 36, SlotType.CAVITY), new SlotPoint(14, 36, SlotType.CAVITY), new SlotPoint(15, 36, SlotType.CAVITY), new SlotPoint(16, 36, SlotType.CAVITY), new SlotPoint(17, 36, SlotType.CAVITY), new SlotPoint(18, 36, SlotType.CAVITY), new SlotPoint(19, 36, SlotType.CAVITY), new SlotPoint(20, 36, SlotType.CAVITY), new SlotPoint(21, 36, SlotType.CAVITY), new SlotPoint(22, 36, SlotType.CAVITY), new SlotPoint(23, 36, SlotType.CAVITY), new SlotPoint(1, 37, SlotType.CAVITY), new SlotPoint(2, 37, SlotType.CAVITY), new SlotPoint(3, 37, SlotType.CAVITY), new SlotPoint(4, 37, SlotType.CAVITY), new SlotPoint(5, 37, SlotType.CAVITY), new SlotPoint(6, 37, SlotType.CAVITY), new SlotPoint(7, 37, SlotType.CAVITY), new SlotPoint(8, 37, SlotType.CAVITY), new SlotPoint(9, 37, SlotType.CAVITY), new SlotPoint(10, 37, SlotType.CAVITY), new SlotPoint(11, 37, SlotType.CAVITY), new SlotPoint(12, 37, SlotType.CAVITY), new SlotPoint(13, 37, SlotType.CAVITY), new SlotPoint(14, 37, SlotType.CAVITY), new SlotPoint(15, 37, SlotType.CAVITY), new SlotPoint(16, 37, SlotType.CAVITY), new SlotPoint(17, 37, SlotType.CAVITY), new SlotPoint(18, 37, SlotType.CAVITY), new SlotPoint(19, 37, SlotType.CAVITY), new SlotPoint(20, 37, SlotType.CAVITY), new SlotPoint(21, 37, SlotType.CAVITY), new SlotPoint(22, 37, SlotType.CAVITY), new SlotPoint(23, 37, SlotType.CAVITY), new SlotPoint(1, 38, SlotType.CAVITY), new SlotPoint(2, 38, SlotType.CAVITY), new SlotPoint(3, 38, SlotType.CAVITY), new SlotPoint(4, 38, SlotType.CAVITY), new SlotPoint(5, 38, SlotType.CAVITY), new SlotPoint(6, 38, SlotType.CAVITY), new SlotPoint(7, 38, SlotType.CAVITY), new SlotPoint(8, 38, SlotType.CAVITY), new SlotPoint(9, 38, SlotType.CAVITY), new SlotPoint(10, 38, SlotType.CAVITY), new SlotPoint(11, 38, SlotType.CAVITY), new SlotPoint(12, 38, SlotType.CAVITY), new SlotPoint(13, 38, SlotType.CAVITY), new SlotPoint(14, 38, SlotType.CAVITY), new SlotPoint(15, 38, SlotType.CAVITY), new SlotPoint(16, 38, SlotType.CAVITY), new SlotPoint(17, 38, SlotType.CAVITY), new SlotPoint(18, 38, SlotType.CAVITY), new SlotPoint(19, 38, SlotType.CAVITY), new SlotPoint(20, 38, SlotType.CAVITY), new SlotPoint(21, 38, SlotType.CAVITY), new SlotPoint(22, 38, SlotType.CAVITY), new SlotPoint(23, 38, SlotType.CAVITY), new SlotPoint(1, 39, SlotType.CAVITY), new SlotPoint(2, 39, SlotType.CAVITY), new SlotPoint(3, 39, SlotType.CAVITY), new SlotPoint(4, 39, SlotType.CAVITY), new SlotPoint(5, 39, SlotType.CAVITY), new SlotPoint(6, 39, SlotType.CAVITY), new SlotPoint(7, 39, SlotType.CAVITY), new SlotPoint(8, 39, SlotType.CAVITY), new SlotPoint(9, 39, SlotType.CAVITY), new SlotPoint(10, 39, SlotType.CAVITY), new SlotPoint(11, 39, SlotType.CAVITY), new SlotPoint(12, 39, SlotType.CAVITY), new SlotPoint(13, 39, SlotType.CAVITY), new SlotPoint(14, 39, SlotType.CAVITY), new SlotPoint(15, 39, SlotType.CAVITY), new SlotPoint(16, 39, SlotType.CAVITY), new SlotPoint(17, 39, SlotType.CAVITY), new SlotPoint(18, 39, SlotType.CAVITY), new SlotPoint(19, 39, SlotType.CAVITY), new SlotPoint(20, 39, SlotType.CAVITY), new SlotPoint(21, 39, SlotType.CAVITY), new SlotPoint(22, 39, SlotType.CAVITY), new SlotPoint(23, 39, SlotType.CAVITY), new SlotPoint(1, 40, SlotType.CAVITY), new SlotPoint(2, 40, SlotType.CAVITY), new SlotPoint(3, 40, SlotType.CAVITY), new SlotPoint(4, 40, SlotType.CAVITY), new SlotPoint(5, 40, SlotType.CAVITY), new SlotPoint(6, 40, SlotType.CAVITY), new SlotPoint(7, 40, SlotType.CAVITY), new SlotPoint(8, 40, SlotType.CAVITY), new SlotPoint(9, 40, SlotType.CAVITY), new SlotPoint(10, 40, SlotType.CAVITY), new SlotPoint(11, 40, SlotType.CAVITY), new SlotPoint(12, 40, SlotType.CAVITY), new SlotPoint(13, 40, SlotType.CAVITY), new SlotPoint(14, 40, SlotType.CAVITY), new SlotPoint(15, 40, SlotType.CAVITY), new SlotPoint(16, 40, SlotType.CAVITY), new SlotPoint(17, 40, SlotType.CAVITY), new SlotPoint(18, 40, SlotType.CAVITY), new SlotPoint(19, 40, SlotType.CAVITY), new SlotPoint(20, 40, SlotType.CAVITY), new SlotPoint(21, 40, SlotType.CAVITY), new SlotPoint(22, 40, SlotType.CAVITY), new SlotPoint(23, 40, SlotType.CAVITY), new SlotPoint(1, 41, SlotType.CAVITY), new SlotPoint(2, 41, SlotType.CAVITY), new SlotPoint(3, 41, SlotType.CAVITY), new SlotPoint(4, 41, SlotType.CAVITY), new SlotPoint(5, 41, SlotType.CAVITY), new SlotPoint(6, 41, SlotType.CAVITY), new SlotPoint(7, 41, SlotType.CAVITY), new SlotPoint(8, 41, SlotType.CAVITY), new SlotPoint(9, 41, SlotType.CAVITY), new SlotPoint(10, 41, SlotType.CAVITY), new SlotPoint(11, 41, SlotType.CAVITY), new SlotPoint(12, 41, SlotType.CAVITY), new SlotPoint(13, 41, SlotType.CAVITY), new SlotPoint(14, 41, SlotType.CAVITY), new SlotPoint(15, 41, SlotType.CAVITY), new SlotPoint(16, 41, SlotType.CAVITY), new SlotPoint(17, 41, SlotType.CAVITY), new SlotPoint(18, 41, SlotType.CAVITY), new SlotPoint(19, 41, SlotType.CAVITY), new SlotPoint(20, 41, SlotType.CAVITY), new SlotPoint(21, 41, SlotType.CAVITY), new SlotPoint(22, 41, SlotType.CAVITY), new SlotPoint(23, 41, SlotType.CAVITY), new SlotPoint(1, 42, SlotType.CAVITY), new SlotPoint(2, 42, SlotType.CAVITY), new SlotPoint(3, 42, SlotType.CAVITY), new SlotPoint(4, 42, SlotType.CAVITY), new SlotPoint(5, 42, SlotType.CAVITY), new SlotPoint(6, 42, SlotType.CAVITY), new SlotPoint(7, 42, SlotType.CAVITY), new SlotPoint(8, 42, SlotType.CAVITY), new SlotPoint(9, 42, SlotType.CAVITY), new SlotPoint(10, 42, SlotType.CAVITY), new SlotPoint(11, 42, SlotType.CAVITY), new SlotPoint(12, 42, SlotType.CAVITY), new SlotPoint(13, 42, SlotType.CAVITY), new SlotPoint(14, 42, SlotType.CAVITY), new SlotPoint(15, 42, SlotType.CAVITY), new SlotPoint(16, 42, SlotType.CAVITY), new SlotPoint(17, 42, SlotType.CAVITY), new SlotPoint(18, 42, SlotType.CAVITY), new SlotPoint(19, 42, SlotType.CAVITY), new SlotPoint(20, 42, SlotType.CAVITY), new SlotPoint(21, 42, SlotType.CAVITY), new SlotPoint(22, 42, SlotType.CAVITY), new SlotPoint(23, 42, SlotType.CAVITY), new SlotPoint(1, 43, SlotType.CAVITY), new SlotPoint(2, 43, SlotType.CAVITY), new SlotPoint(3, 43, SlotType.CAVITY), new SlotPoint(4, 43, SlotType.CAVITY), new SlotPoint(5, 43, SlotType.CAVITY), new SlotPoint(6, 43, SlotType.CAVITY), new SlotPoint(7, 43, SlotType.CAVITY), new SlotPoint(8, 43, SlotType.CAVITY), new SlotPoint(9, 43, SlotType.CAVITY), new SlotPoint(10, 43, SlotType.CAVITY), new SlotPoint(11, 43, SlotType.CAVITY), new SlotPoint(12, 43, SlotType.CAVITY), new SlotPoint(13, 43, SlotType.CAVITY), new SlotPoint(14, 43, SlotType.CAVITY), new SlotPoint(15, 43, SlotType.CAVITY), new SlotPoint(16, 43, SlotType.CAVITY), new SlotPoint(17, 43, SlotType.CAVITY), new SlotPoint(18, 43, SlotType.CAVITY), new SlotPoint(19, 43, SlotType.CAVITY), new SlotPoint(20, 43, SlotType.CAVITY), new SlotPoint(21, 43, SlotType.CAVITY), new SlotPoint(22, 43, SlotType.CAVITY), new SlotPoint(23, 43, SlotType.CAVITY), new SlotPoint(1, 44, SlotType.CAVITY), new SlotPoint(2, 44, SlotType.CAVITY), new SlotPoint(3, 44, SlotType.CAVITY), new SlotPoint(4, 44, SlotType.CAVITY), new SlotPoint(5, 44, SlotType.CAVITY), new SlotPoint(6, 44, SlotType.CAVITY), new SlotPoint(7, 44, SlotType.CAVITY), new SlotPoint(8, 44, SlotType.CAVITY), new SlotPoint(9, 44, SlotType.CAVITY), new SlotPoint(10, 44, SlotType.CAVITY), new SlotPoint(11, 44, SlotType.CAVITY), new SlotPoint(12, 44, SlotType.CAVITY), new SlotPoint(13, 44, SlotType.CAVITY), new SlotPoint(14, 44, SlotType.CAVITY), new SlotPoint(15, 44, SlotType.CAVITY), new SlotPoint(16, 44, SlotType.CAVITY), new SlotPoint(17, 44, SlotType.CAVITY), new SlotPoint(18, 44, SlotType.CAVITY), new SlotPoint(19, 44, SlotType.CAVITY), new SlotPoint(20, 44, SlotType.CAVITY), new SlotPoint(21, 44, SlotType.CAVITY), new SlotPoint(22, 44, SlotType.CAVITY), new SlotPoint(23, 44, SlotType.CAVITY) ))
                    )
                    .shapeOf(8, 11)
                    .visualData(VisualData.empty().withIcon("torso").withWidth(8).withHeight(11))
                    .item(Holder.direct(RED_WOOL))
            )
    );

    public static final DeferredHolder<Compartment, Compartment> ABDOMEN = COMPARTMENTS.register("abdomen",
            () -> new Compartment("abdomen", new Compartment.Properties()
                    .defaultHealth(100)
                    .layers(
                            LayerData.fromRegularShape("Skin", 48, 48, SlotType.SKIN),
                            LayerData.fromRegularShape("Fat", 48, 48, SlotType.FAT),
                            LayerData.fromRegularShape("Muscle", 48, 48, SlotType.MUSCLE),
                            LayerData.fromRegularShape("Peritoneum", 48, 48, SlotType.MEMBRANE),
                            LayerData.fromRegularShape("Abdominal Cavity", 48, 48, SlotType.CAVITY),
                            LayerData.fromRegularShape("Retroperitoneal Space", 48, 48, SlotType.CAVITY)
                    )
                    .item(Holder.direct(RED_WOOL))
            )
    );

    public static final DeferredHolder<Compartment, Compartment> UPPER_ARM = COMPARTMENTS.register("upper_arm",
            () -> new Compartment("upper_arm", new Compartment.Properties()
                    .defaultHealth(80)
                    .addAttribute(MedicalAttribute.MANIPULATION)
                    .layers(
                            LayerData.fromRegularShape("Skin", 16, 5, SlotType.SKIN),
                            LayerData.fromRegularShape("Fat", 16, 5, SlotType.FAT),
                            LayerData.fromRegularShape("Muscle", 16, 5, SlotType.MUSCLE),
                            new LayerData("Bone", 16, 5, List.of(new SlotPoint(0, 0, SlotType.BONE), new SlotPoint(1, 0, SlotType.BONE), new SlotPoint(2, 0, SlotType.MUSCLE), new SlotPoint(3, 0, SlotType.MUSCLE), new SlotPoint(4, 0, SlotType.MUSCLE), new SlotPoint(5, 0, SlotType.MUSCLE), new SlotPoint(6, 0, SlotType.MUSCLE), new SlotPoint(7, 0, SlotType.MUSCLE), new SlotPoint(8, 0, SlotType.MUSCLE), new SlotPoint(9, 0, SlotType.MUSCLE), new SlotPoint(10, 0, SlotType.MUSCLE), new SlotPoint(11, 0, SlotType.MUSCLE), new SlotPoint(12, 0, SlotType.MUSCLE), new SlotPoint(13, 0, SlotType.MUSCLE), new SlotPoint(14, 0, SlotType.MUSCLE), new SlotPoint(15, 0, SlotType.BONE), new SlotPoint(0, 1, SlotType.BONE), new SlotPoint(1, 1, SlotType.BONE), new SlotPoint(2, 1, SlotType.BONE), new SlotPoint(3, 1, SlotType.BONE), new SlotPoint(4, 1, SlotType.BONE), new SlotPoint(5, 1, SlotType.BONE), new SlotPoint(6, 1, SlotType.BONE), new SlotPoint(7, 1, SlotType.BONE), new SlotPoint(8, 1, SlotType.BONE), new SlotPoint(9, 1, SlotType.BONE), new SlotPoint(10, 1, SlotType.BONE), new SlotPoint(11, 1, SlotType.BONE), new SlotPoint(12, 1, SlotType.BONE), new SlotPoint(13, 1, SlotType.BONE), new SlotPoint(14, 1, SlotType.BONE), new SlotPoint(15, 1, SlotType.BONE), new SlotPoint(0, 2, SlotType.BONE), new SlotPoint(1, 2, SlotType.BONE), new SlotPoint(2, 2, SlotType.BONE), new SlotPoint(3, 2, SlotType.BONE), new SlotPoint(4, 2, SlotType.BONE), new SlotPoint(5, 2, SlotType.BONE), new SlotPoint(6, 2, SlotType.BONE), new SlotPoint(7, 2, SlotType.BONE), new SlotPoint(8, 2, SlotType.BONE), new SlotPoint(9, 2, SlotType.BONE), new SlotPoint(10, 2, SlotType.BONE), new SlotPoint(11, 2, SlotType.BONE), new SlotPoint(12, 2, SlotType.BONE), new SlotPoint(13, 2, SlotType.BONE), new SlotPoint(14, 2, SlotType.BONE), new SlotPoint(15, 2, SlotType.BONE), new SlotPoint(0, 3, SlotType.BONE), new SlotPoint(1, 3, SlotType.BONE), new SlotPoint(2, 3, SlotType.BONE), new SlotPoint(3, 3, SlotType.MUSCLE), new SlotPoint(4, 3, SlotType.MUSCLE), new SlotPoint(5, 3, SlotType.MUSCLE), new SlotPoint(6, 3, SlotType.MUSCLE), new SlotPoint(7, 3, SlotType.MUSCLE), new SlotPoint(8, 3, SlotType.MUSCLE), new SlotPoint(9, 3, SlotType.MUSCLE), new SlotPoint(10, 3, SlotType.MUSCLE), new SlotPoint(11, 3, SlotType.MUSCLE), new SlotPoint(12, 3, SlotType.MUSCLE), new SlotPoint(13, 3, SlotType.MUSCLE), new SlotPoint(14, 3, SlotType.BONE), new SlotPoint(15, 3, SlotType.BONE), new SlotPoint(0, 4, SlotType.BONE), new SlotPoint(1, 4, SlotType.BONE), new SlotPoint(2, 4, SlotType.MUSCLE), new SlotPoint(3, 4, SlotType.MUSCLE), new SlotPoint(4, 4, SlotType.MUSCLE), new SlotPoint(5, 4, SlotType.MUSCLE), new SlotPoint(6, 4, SlotType.MUSCLE), new SlotPoint(7, 4, SlotType.MUSCLE), new SlotPoint(8, 4, SlotType.MUSCLE), new SlotPoint(9, 4, SlotType.MUSCLE), new SlotPoint(10, 4, SlotType.MUSCLE), new SlotPoint(11, 4, SlotType.MUSCLE), new SlotPoint(12, 4, SlotType.MUSCLE), new SlotPoint(13, 4, SlotType.MUSCLE), new SlotPoint(14, 4, SlotType.MUSCLE), new SlotPoint(15, 4, SlotType.MUSCLE) ))
                    )
                    .shapeOf(3, 5)
                    .item(BitterItems.BODY_PART)
                    .visualData(VisualData.empty().withIcon("arm").withWidth(3).withHeight(5))
            )
    );

    public static final DeferredHolder<Compartment, Compartment> FOREARM = COMPARTMENTS.register("forearm",
            () -> new Compartment("forearm", new Compartment.Properties()
                    .defaultHealth(80)
                    .addAttribute(MedicalAttribute.MANIPULATION)
                    .layers(
                            LayerData.fromRegularShape("Skin", 16, 5, SlotType.SKIN),
                            LayerData.fromRegularShape("Fat", 16, 5, SlotType.FAT),
                            LayerData.fromRegularShape("Muscle", 16, 5, SlotType.MUSCLE),
                            new LayerData("Bone", 16, 5, List.of(
                                    new SlotPoint(0, 0, SlotType.SKIN),
                                    new SlotPoint(1, 0, SlotType.MUSCLE),
                                    new SlotPoint(2, 0, SlotType.MUSCLE),
                                    new SlotPoint(3, 0, SlotType.MUSCLE),
                                    new SlotPoint(4, 0, SlotType.MUSCLE),
                                    new SlotPoint(5, 0, SlotType.MUSCLE),
                                    new SlotPoint(6, 0, SlotType.MUSCLE),
                                    new SlotPoint(7, 0, SlotType.MUSCLE),
                                    new SlotPoint(8, 0, SlotType.MUSCLE),
                                    new SlotPoint(9, 0, SlotType.MUSCLE),
                                    new SlotPoint(10, 0, SlotType.MUSCLE),
                                    new SlotPoint(11, 0, SlotType.MUSCLE),
                                    new SlotPoint(12, 0, SlotType.MUSCLE),
                                    new SlotPoint(13, 0, SlotType.MUSCLE),
                                    new SlotPoint(14, 0, SlotType.MUSCLE),
                                    new SlotPoint(15, 0, SlotType.MUSCLE),
                                    new SlotPoint(0, 1, SlotType.BONE),
                                    new SlotPoint(1, 1, SlotType.BONE),
                                    new SlotPoint(2, 1, SlotType.BONE),
                                    new SlotPoint(3, 1, SlotType.BONE),
                                    new SlotPoint(4, 1, SlotType.BONE),
                                    new SlotPoint(5, 1, SlotType.BONE),
                                    new SlotPoint(6, 1, SlotType.BONE),
                                    new SlotPoint(7, 1, SlotType.BONE),
                                    new SlotPoint(8, 1, SlotType.BONE),
                                    new SlotPoint(9, 1, SlotType.BONE),
                                    new SlotPoint(10, 1, SlotType.BONE),
                                    new SlotPoint(11, 1, SlotType.BONE),
                                    new SlotPoint(12, 1, SlotType.BONE),
                                    new SlotPoint(13, 1, SlotType.BONE),
                                    new SlotPoint(14, 1, SlotType.BONE),
                                    new SlotPoint(15, 1, SlotType.BONE),
                                    new SlotPoint(0, 2, SlotType.BONE),
                                    new SlotPoint(1, 2, SlotType.BONE),
                                    new SlotPoint(2, 2, SlotType.BONE),
                                    new SlotPoint(3, 2, SlotType.BONE),
                                    new SlotPoint(4, 2, SlotType.BONE),
                                    new SlotPoint(5, 2, SlotType.MUSCLE),
                                    new SlotPoint(6, 2, SlotType.MUSCLE),
                                    new SlotPoint(7, 2, SlotType.MUSCLE),
                                    new SlotPoint(8, 2, SlotType.MUSCLE),
                                    new SlotPoint(9, 2, SlotType.MUSCLE),
                                    new SlotPoint(10, 2, SlotType.MUSCLE),
                                    new SlotPoint(11, 2, SlotType.MUSCLE),
                                    new SlotPoint(12, 2, SlotType.MUSCLE),
                                    new SlotPoint(13, 2, SlotType.MUSCLE),
                                    new SlotPoint(14, 2, SlotType.BONE),
                                    new SlotPoint(15, 2, SlotType.BONE),
                                    new SlotPoint(0, 3, SlotType.SKIN),
                                    new SlotPoint(1, 3, SlotType.SKIN),
                                    new SlotPoint(2, 3, SlotType.MUSCLE),
                                    new SlotPoint(3, 3, SlotType.MUSCLE),
                                    new SlotPoint(4, 3, SlotType.MUSCLE),
                                    new SlotPoint(5, 3, SlotType.BONE),
                                    new SlotPoint(6, 3, SlotType.BONE),
                                    new SlotPoint(7, 3, SlotType.BONE),
                                    new SlotPoint(8, 3, SlotType.BONE),
                                    new SlotPoint(9, 3, SlotType.BONE),
                                    new SlotPoint(10, 3, SlotType.BONE),
                                    new SlotPoint(11, 3, SlotType.BONE),
                                    new SlotPoint(12, 3, SlotType.BONE),
                                    new SlotPoint(13, 3, SlotType.BONE),
                                    new SlotPoint(14, 3, SlotType.BONE),
                                    new SlotPoint(15, 3, SlotType.MUSCLE),
                                    new SlotPoint(0, 4, SlotType.MUSCLE),
                                    new SlotPoint(1, 4, SlotType.MUSCLE),
                                    new SlotPoint(2, 4, SlotType.MUSCLE),
                                    new SlotPoint(3, 4, SlotType.MUSCLE),
                                    new SlotPoint(4, 4, SlotType.MUSCLE),
                                    new SlotPoint(5, 4, SlotType.MUSCLE),
                                    new SlotPoint(6, 4, SlotType.MUSCLE),
                                    new SlotPoint(7, 4, SlotType.MUSCLE),
                                    new SlotPoint(8, 4, SlotType.MUSCLE),
                                    new SlotPoint(9, 4, SlotType.MUSCLE),
                                    new SlotPoint(10, 4, SlotType.MUSCLE),
                                    new SlotPoint(11, 4, SlotType.MUSCLE),
                                    new SlotPoint(12, 4, SlotType.MUSCLE),
                                    new SlotPoint(13, 4, SlotType.MUSCLE),
                                    new SlotPoint(14, 4, SlotType.MUSCLE),
                                    new SlotPoint(15, 4, SlotType.MUSCLE)
                            ))
                    )
                    .shapeOf(3, 5)
                    .item(BitterItems.BODY_PART)
                    .visualData(VisualData.empty().withIcon("arm").withWidth(3).withHeight(5))
            )
    );

    public static final DeferredHolder<Compartment, Compartment> HAND = COMPARTMENTS.register("hand",
            () -> new Compartment("hand", new Compartment.Properties()
                    .defaultHealth(50)
                    .addAttribute(MedicalAttribute.MANIPULATION)
                    .layers(
                            new LayerData("Skin", 16, 10, List.of(
                                    new SlotPoint(7, 0, SlotType.SKIN),
                                    new SlotPoint(8, 0, SlotType.SKIN),
                                    new SlotPoint(9, 0, SlotType.SKIN),
                                    new SlotPoint(10, 0, SlotType.SKIN),
                                    new SlotPoint(5, 1, SlotType.SKIN),
                                    new SlotPoint(6, 1, SlotType.SKIN),
                                    new SlotPoint(7, 1, SlotType.SKIN),
                                    new SlotPoint(8, 1, SlotType.SKIN),
                                    new SlotPoint(3, 2, SlotType.SKIN),
                                    new SlotPoint(4, 2, SlotType.SKIN),
                                    new SlotPoint(5, 2, SlotType.SKIN),
                                    new SlotPoint(6, 2, SlotType.SKIN),
                                    new SlotPoint(2, 3, SlotType.SKIN),
                                    new SlotPoint(3, 3, SlotType.SKIN),
                                    new SlotPoint(4, 3, SlotType.SKIN),
                                    new SlotPoint(5, 3, SlotType.SKIN),
                                    new SlotPoint(6, 3, SlotType.SKIN),
                                    new SlotPoint(7, 3, SlotType.SKIN),
                                    new SlotPoint(8, 3, SlotType.SKIN),
                                    new SlotPoint(9, 3, SlotType.SKIN),
                                    new SlotPoint(10, 3, SlotType.SKIN),
                                    new SlotPoint(11, 3, SlotType.SKIN),
                                    new SlotPoint(12, 3, SlotType.SKIN),
                                    new SlotPoint(13, 3, SlotType.SKIN),
                                    new SlotPoint(14, 3, SlotType.SKIN),
                                    new SlotPoint(0, 4, SlotType.SKIN),
                                    new SlotPoint(1, 4, SlotType.SKIN),
                                    new SlotPoint(2, 4, SlotType.SKIN),
                                    new SlotPoint(3, 4, SlotType.SKIN),
                                    new SlotPoint(4, 4, SlotType.SKIN),
                                    new SlotPoint(5, 4, SlotType.SKIN),
                                    new SlotPoint(6, 4, SlotType.SKIN),
                                    new SlotPoint(7, 4, SlotType.SKIN),
                                    new SlotPoint(8, 4, SlotType.SKIN),
                                    new SlotPoint(9, 4, SlotType.SKIN),
                                    new SlotPoint(0, 5, SlotType.SKIN),
                                    new SlotPoint(1, 5, SlotType.SKIN),
                                    new SlotPoint(2, 5, SlotType.SKIN),
                                    new SlotPoint(3, 5, SlotType.SKIN),
                                    new SlotPoint(4, 5, SlotType.SKIN),
                                    new SlotPoint(5, 5, SlotType.SKIN),
                                    new SlotPoint(6, 5, SlotType.SKIN),
                                    new SlotPoint(7, 5, SlotType.SKIN),
                                    new SlotPoint(8, 5, SlotType.SKIN),
                                    new SlotPoint(9, 5, SlotType.SKIN),
                                    new SlotPoint(10, 5, SlotType.SKIN),
                                    new SlotPoint(11, 5, SlotType.SKIN),
                                    new SlotPoint(12, 5, SlotType.SKIN),
                                    new SlotPoint(13, 5, SlotType.SKIN),
                                    new SlotPoint(14, 5, SlotType.SKIN),
                                    new SlotPoint(15, 5, SlotType.SKIN),
                                    new SlotPoint(0, 6, SlotType.SKIN),
                                    new SlotPoint(1, 6, SlotType.SKIN),
                                    new SlotPoint(2, 6, SlotType.SKIN),
                                    new SlotPoint(3, 6, SlotType.SKIN),
                                    new SlotPoint(4, 6, SlotType.SKIN),
                                    new SlotPoint(5, 6, SlotType.SKIN),
                                    new SlotPoint(6, 6, SlotType.SKIN),
                                    new SlotPoint(7, 6, SlotType.SKIN),
                                    new SlotPoint(8, 6, SlotType.SKIN),
                                    new SlotPoint(9, 6, SlotType.SKIN),
                                    new SlotPoint(0, 7, SlotType.SKIN),
                                    new SlotPoint(1, 7, SlotType.SKIN),
                                    new SlotPoint(2, 7, SlotType.SKIN),
                                    new SlotPoint(3, 7, SlotType.SKIN),
                                    new SlotPoint(4, 7, SlotType.SKIN),
                                    new SlotPoint(5, 7, SlotType.SKIN),
                                    new SlotPoint(6, 7, SlotType.SKIN),
                                    new SlotPoint(7, 7, SlotType.SKIN),
                                    new SlotPoint(8, 7, SlotType.SKIN),
                                    new SlotPoint(9, 7, SlotType.SKIN),
                                    new SlotPoint(10, 7, SlotType.SKIN),
                                    new SlotPoint(11, 7, SlotType.SKIN),
                                    new SlotPoint(12, 7, SlotType.SKIN),
                                    new SlotPoint(13, 7, SlotType.SKIN),
                                    new SlotPoint(3, 8, SlotType.SKIN),
                                    new SlotPoint(4, 8, SlotType.SKIN),
                                    new SlotPoint(5, 8, SlotType.SKIN),
                                    new SlotPoint(6, 8, SlotType.SKIN),
                                    new SlotPoint(7, 8, SlotType.SKIN),
                                    new SlotPoint(8, 8, SlotType.SKIN),
                                    new SlotPoint(6, 9, SlotType.SKIN),
                                    new SlotPoint(7, 9, SlotType.SKIN),
                                    new SlotPoint(8, 9, SlotType.SKIN),
                                    new SlotPoint(9, 9, SlotType.SKIN),
                                    new SlotPoint(10, 9, SlotType.SKIN),
                                    new SlotPoint(11, 9, SlotType.SKIN)
                            )),
                            new LayerData("Muscle", 16, 10, List.of(
                                    new SlotPoint(7, 0, SlotType.MUSCLE),
                                    new SlotPoint(8, 0, SlotType.MUSCLE),
                                    new SlotPoint(9, 0, SlotType.MUSCLE),
                                    new SlotPoint(10, 0, SlotType.MUSCLE),
                                    new SlotPoint(5, 1, SlotType.MUSCLE),
                                    new SlotPoint(6, 1, SlotType.MUSCLE),
                                    new SlotPoint(7, 1, SlotType.MUSCLE),
                                    new SlotPoint(8, 1, SlotType.MUSCLE),
                                    new SlotPoint(3, 2, SlotType.MUSCLE),
                                    new SlotPoint(4, 2, SlotType.MUSCLE),
                                    new SlotPoint(5, 2, SlotType.MUSCLE),
                                    new SlotPoint(6, 2, SlotType.MUSCLE),
                                    new SlotPoint(2, 3, SlotType.MUSCLE),
                                    new SlotPoint(3, 3, SlotType.MUSCLE),
                                    new SlotPoint(4, 3, SlotType.MUSCLE),
                                    new SlotPoint(5, 3, SlotType.MUSCLE),
                                    new SlotPoint(6, 3, SlotType.MUSCLE),
                                    new SlotPoint(7, 3, SlotType.MUSCLE),
                                    new SlotPoint(8, 3, SlotType.MUSCLE),
                                    new SlotPoint(9, 3, SlotType.MUSCLE),
                                    new SlotPoint(10, 3, SlotType.MUSCLE),
                                    new SlotPoint(11, 3, SlotType.MUSCLE),
                                    new SlotPoint(12, 3, SlotType.MUSCLE),
                                    new SlotPoint(13, 3, SlotType.MUSCLE),
                                    new SlotPoint(14, 3, SlotType.MUSCLE),
                                    new SlotPoint(0, 4, SlotType.MUSCLE),
                                    new SlotPoint(1, 4, SlotType.MUSCLE),
                                    new SlotPoint(2, 4, SlotType.MUSCLE),
                                    new SlotPoint(3, 4, SlotType.MUSCLE),
                                    new SlotPoint(4, 4, SlotType.MUSCLE),
                                    new SlotPoint(5, 4, SlotType.MUSCLE),
                                    new SlotPoint(6, 4, SlotType.MUSCLE),
                                    new SlotPoint(7, 4, SlotType.MUSCLE),
                                    new SlotPoint(8, 4, SlotType.MUSCLE),
                                    new SlotPoint(9, 4, SlotType.MUSCLE),
                                    new SlotPoint(0, 5, SlotType.MUSCLE),
                                    new SlotPoint(1, 5, SlotType.MUSCLE),
                                    new SlotPoint(2, 5, SlotType.MUSCLE),
                                    new SlotPoint(3, 5, SlotType.MUSCLE),
                                    new SlotPoint(4, 5, SlotType.MUSCLE),
                                    new SlotPoint(5, 5, SlotType.MUSCLE),
                                    new SlotPoint(6, 5, SlotType.MUSCLE),
                                    new SlotPoint(7, 5, SlotType.MUSCLE),
                                    new SlotPoint(8, 5, SlotType.MUSCLE),
                                    new SlotPoint(9, 5, SlotType.MUSCLE),
                                    new SlotPoint(10, 5, SlotType.MUSCLE),
                                    new SlotPoint(11, 5, SlotType.MUSCLE),
                                    new SlotPoint(12, 5, SlotType.MUSCLE),
                                    new SlotPoint(13, 5, SlotType.MUSCLE),
                                    new SlotPoint(14, 5, SlotType.MUSCLE),
                                    new SlotPoint(15, 5, SlotType.MUSCLE),
                                    new SlotPoint(0, 6, SlotType.MUSCLE),
                                    new SlotPoint(1, 6, SlotType.MUSCLE),
                                    new SlotPoint(2, 6, SlotType.MUSCLE),
                                    new SlotPoint(3, 6, SlotType.MUSCLE),
                                    new SlotPoint(4, 6, SlotType.MUSCLE),
                                    new SlotPoint(5, 6, SlotType.MUSCLE),
                                    new SlotPoint(6, 6, SlotType.MUSCLE),
                                    new SlotPoint(7, 6, SlotType.MUSCLE),
                                    new SlotPoint(8, 6, SlotType.MUSCLE),
                                    new SlotPoint(9, 6, SlotType.MUSCLE),
                                    new SlotPoint(0, 7, SlotType.MUSCLE),
                                    new SlotPoint(1, 7, SlotType.MUSCLE),
                                    new SlotPoint(2, 7, SlotType.MUSCLE),
                                    new SlotPoint(3, 7, SlotType.MUSCLE),
                                    new SlotPoint(4, 7, SlotType.MUSCLE),
                                    new SlotPoint(5, 7, SlotType.MUSCLE),
                                    new SlotPoint(6, 7, SlotType.MUSCLE),
                                    new SlotPoint(7, 7, SlotType.MUSCLE),
                                    new SlotPoint(8, 7, SlotType.MUSCLE),
                                    new SlotPoint(9, 7, SlotType.MUSCLE),
                                    new SlotPoint(10, 7, SlotType.MUSCLE),
                                    new SlotPoint(11, 7, SlotType.MUSCLE),
                                    new SlotPoint(12, 7, SlotType.MUSCLE),
                                    new SlotPoint(13, 7, SlotType.MUSCLE),
                                    new SlotPoint(3, 8, SlotType.MUSCLE),
                                    new SlotPoint(4, 8, SlotType.MUSCLE),
                                    new SlotPoint(5, 8, SlotType.MUSCLE),
                                    new SlotPoint(6, 8, SlotType.MUSCLE),
                                    new SlotPoint(7, 8, SlotType.MUSCLE),
                                    new SlotPoint(8, 8, SlotType.MUSCLE),
                                    new SlotPoint(6, 9, SlotType.MUSCLE),
                                    new SlotPoint(7, 9, SlotType.MUSCLE),
                                    new SlotPoint(8, 9, SlotType.MUSCLE),
                                    new SlotPoint(9, 9, SlotType.MUSCLE),
                                    new SlotPoint(10, 9, SlotType.MUSCLE),
                                    new SlotPoint(11, 9, SlotType.MUSCLE)
                            )),
                            new LayerData("Bone", 16, 10, List.of(
                                    new SlotPoint(8, 0, SlotType.BONE),
                                    new SlotPoint(9, 0, SlotType.BONE),
                                    new SlotPoint(10, 0, SlotType.BONE),
                                    new SlotPoint(6, 1, SlotType.BONE),
                                    new SlotPoint(7, 1, SlotType.BONE),
                                    new SlotPoint(8, 1, SlotType.BONE),
                                    new SlotPoint(4, 2, SlotType.BONE),
                                    new SlotPoint(5, 2, SlotType.BONE),
                                    new SlotPoint(3, 3, SlotType.BONE),
                                    new SlotPoint(4, 3, SlotType.BONE),
                                    new SlotPoint(5, 3, SlotType.BONE),
                                    new SlotPoint(6, 3, SlotType.BONE),
                                    new SlotPoint(7, 3, SlotType.BONE),
                                    new SlotPoint(8, 3, SlotType.BONE),
                                    new SlotPoint(9, 3, SlotType.BONE),
                                    new SlotPoint(10, 3, SlotType.BONE),
                                    new SlotPoint(11, 3, SlotType.BONE),
                                    new SlotPoint(12, 3, SlotType.BONE),
                                    new SlotPoint(13, 3, SlotType.BONE),
                                    new SlotPoint(0, 4, SlotType.BONE),
                                    new SlotPoint(1, 4, SlotType.BONE),
                                    new SlotPoint(2, 4, SlotType.BONE),
                                    new SlotPoint(3, 4, SlotType.BONE),
                                    new SlotPoint(4, 4, SlotType.MUSCLE),
                                    new SlotPoint(5, 4, SlotType.MUSCLE),
                                    new SlotPoint(6, 4, SlotType.MUSCLE),
                                    new SlotPoint(7, 4, SlotType.BONE),
                                    new SlotPoint(8, 4, SlotType.BONE),
                                    new SlotPoint(0, 5, SlotType.BONE),
                                    new SlotPoint(1, 5, SlotType.BONE),
                                    new SlotPoint(2, 5, SlotType.MUSCLE),
                                    new SlotPoint(3, 5, SlotType.MUSCLE),
                                    new SlotPoint(4, 5, SlotType.BONE),
                                    new SlotPoint(5, 5, SlotType.BONE),
                                    new SlotPoint(6, 5, SlotType.BONE),
                                    new SlotPoint(7, 5, SlotType.BONE),
                                    new SlotPoint(8, 5, SlotType.BONE),
                                    new SlotPoint(9, 5, SlotType.BONE),
                                    new SlotPoint(10, 5, SlotType.BONE),
                                    new SlotPoint(11, 5, SlotType.BONE),
                                    new SlotPoint(12, 5, SlotType.BONE),
                                    new SlotPoint(13, 5, SlotType.BONE),
                                    new SlotPoint(14, 5, SlotType.BONE),
                                    new SlotPoint(15, 5, SlotType.BONE),
                                    new SlotPoint(0, 6, SlotType.MUSCLE),
                                    new SlotPoint(1, 6, SlotType.MUSCLE),
                                    new SlotPoint(2, 6, SlotType.MUSCLE),
                                    new SlotPoint(3, 6, SlotType.BONE),
                                    new SlotPoint(4, 6, SlotType.MUSCLE),
                                    new SlotPoint(5, 6, SlotType.MUSCLE),
                                    new SlotPoint(6, 6, SlotType.MUSCLE),
                                    new SlotPoint(7, 6, SlotType.BONE),
                                    new SlotPoint(8, 6, SlotType.BONE),
                                    new SlotPoint(0, 7, SlotType.BONE),
                                    new SlotPoint(1, 7, SlotType.BONE),
                                    new SlotPoint(2, 7, SlotType.BONE),
                                    new SlotPoint(3, 7, SlotType.BONE),
                                    new SlotPoint(4, 7, SlotType.BONE),
                                    new SlotPoint(5, 7, SlotType.BONE),
                                    new SlotPoint(6, 7, SlotType.BONE),
                                    new SlotPoint(7, 7, SlotType.BONE),
                                    new SlotPoint(8, 7, SlotType.BONE),
                                    new SlotPoint(9, 7, SlotType.BONE),
                                    new SlotPoint(10, 7, SlotType.BONE),
                                    new SlotPoint(11, 7, SlotType.BONE),
                                    new SlotPoint(12, 7, SlotType.BONE),
                                    new SlotPoint(13, 7, SlotType.BONE),
                                    new SlotPoint(5, 8, SlotType.BONE),
                                    new SlotPoint(6, 8, SlotType.BONE),
                                    new SlotPoint(7, 8, SlotType.BONE),
                                    new SlotPoint(7, 9, SlotType.BONE),
                                    new SlotPoint(8, 9, SlotType.BONE),
                                    new SlotPoint(9, 9, SlotType.BONE),
                                    new SlotPoint(10, 9, SlotType.BONE),
                                    new SlotPoint(11, 9, SlotType.BONE)
                            ))
                    )
                    .shapeOf(3, 3)
                    .item(BitterItems.BODY_PART)
                    .visualData(VisualData.empty().withIcon("hand").withWidth(3).withHeight(3))
            )
    );

    // ==================== Tissue Types ====================
    public static final DeferredHolder<Compartment, Compartment> SOFT_TISSUE = COMPARTMENTS.register("soft_tissue",
            () -> new Compartment("soft_tissue", new Compartment.Properties()
                    .defaultHealth(50)
                    .layers() // No layers - leaf node
                    .item(BitterItems.BODY_PART)
            )
    );

    public static final DeferredHolder<Compartment, Compartment> SKIN = COMPARTMENTS.register("skin",
            () -> new Compartment("skin", new Compartment.Properties()
                    .defaultHealth(40)
                    .layers()
                    .item(BitterItems.BODY_PART)
            )
    );

    public static final DeferredHolder<Compartment, Compartment> FAT = COMPARTMENTS.register("fat",
            () -> new Compartment("fat", new Compartment.Properties()
                    .defaultHealth(60)
                    .layers()
                    .item(BitterItems.BODY_PART)
            )
    );

    public static final DeferredHolder<Compartment, Compartment> MUSCLE = COMPARTMENTS.register("muscle",
            () -> new Compartment("muscle", new Compartment.Properties()
                    .defaultHealth(100)
                    .layers()
                    .item(BitterItems.BODY_PART)
            )
    );

    public static final DeferredHolder<Compartment, Compartment> MEMBRANE = COMPARTMENTS.register("membrane",
            () -> new Compartment("membrane", new Compartment.Properties()
                    .defaultHealth(5)
                    .layers()
                    .item(BitterItems.BODY_PART)
            )
    );

    public static final DeferredHolder<Compartment, Compartment> BRAIN = COMPARTMENTS.register("brain",
            () -> new Compartment("brain", new Compartment.Properties()
                    .defaultHealth(20)
                    .layers(
                            LayerData.fromRegularShape("Meninges", 16, 16, SlotType.MEMBRANE),
                            LayerData.fromRegularShape("Brain", 16, 16, SlotType.BRAIN_TISSUE)
                    )
            )
    );

    public static final DeferredHolder<Compartment, Compartment> FRONTAL_LOBE = COMPARTMENTS.register("frontal_lobe",
            () -> new Compartment("frontal_lobe", new Compartment.Properties()
                    .defaultHealth(10)
                    .addAttribute(MedicalAttribute.BRAIN_MOTOR_ABILITY)
                    .addAttribute(MedicalAttribute.BRAIN_CONSCIOUSNESS)
            )
    );

    public static final DeferredHolder<Compartment, Compartment> PARIETAL_LOBE = COMPARTMENTS.register("parietal_lobe",
            () -> new Compartment("parietal_lobe", new Compartment.Properties()
                    .defaultHealth(10)
                    .addAttribute(MedicalAttribute.NERVOUS)
            )
    );

    public static final DeferredHolder<Compartment, Compartment> BRAINSTEM = COMPARTMENTS.register("brainstem",
            () -> new Compartment("brainstem", new Compartment.Properties()
                    .defaultHealth(10)
                    .addAttribute(MedicalAttribute.BRAIN_VITALS)
            )
    );

    public static final DeferredHolder<Compartment, Compartment> HEART = COMPARTMENTS.register("heart",
            () -> new Compartment("heart", new Compartment.Properties()
                    .defaultHealth(40)
                    .layers(
                            LayerData.fromRegularShape("Pericardium", 16, 16, SlotType.MEMBRANE),
                            LayerData.fromRegularShape("Cardiac Chambers", 16, 16, SlotType.MUSCLE)
                    )
            )
    );

    public static final DeferredHolder<Compartment, Compartment> LUNG = COMPARTMENTS.register("lung",
            () -> new Compartment("lung", new Compartment.Properties()
                    .defaultHealth(35)
            )
    );

    public static final DeferredHolder<Compartment, Compartment> KIDNEY = COMPARTMENTS.register("kidney",
            () -> new Compartment("kidney", new Compartment.Properties()
                    .defaultHealth(30)
                    .item(BitterItems.KIDNEY)
            )
    );

    public static final DeferredHolder<Compartment, Compartment> STOMACH = COMPARTMENTS.register("stomach",
            () -> new Compartment("stomach", new Compartment.Properties()
                    .defaultHealth(30)
                    .item(BitterItems.STOMACH)
                    .addAttribute(MedicalAttribute.DIGESTION)
                    .layers(new LayerData("", 14, 16, List.of( new SlotPoint(6, 0, SlotType.ORGAN), new SlotPoint(5, 1, SlotType.ORGAN), new SlotPoint(6, 1, SlotType.ORGAN), new SlotPoint(7, 1, SlotType.ORGAN), new SlotPoint(10, 1, SlotType.ORGAN), new SlotPoint(11, 1, SlotType.ORGAN), new SlotPoint(5, 2, SlotType.ORGAN), new SlotPoint(6, 2, SlotType.ORGAN), new SlotPoint(7, 2, SlotType.ORGAN), new SlotPoint(9, 2, SlotType.ORGAN), new SlotPoint(10, 2, SlotType.ORGAN), new SlotPoint(11, 2, SlotType.ORGAN), new SlotPoint(12, 2, SlotType.ORGAN), new SlotPoint(5, 3, SlotType.ORGAN), new SlotPoint(6, 3, SlotType.ORGAN), new SlotPoint(7, 3, SlotType.ORGAN), new SlotPoint(8, 3, SlotType.ORGAN), new SlotPoint(9, 3, SlotType.ORGAN), new SlotPoint(10, 3, SlotType.ORGAN), new SlotPoint(11, 3, SlotType.ORGAN), new SlotPoint(12, 3, SlotType.ORGAN), new SlotPoint(13, 3, SlotType.ORGAN), new SlotPoint(6, 4, SlotType.ORGAN), new SlotPoint(7, 4, SlotType.ORGAN), new SlotPoint(8, 4, SlotType.ORGAN), new SlotPoint(9, 4, SlotType.ORGAN), new SlotPoint(10, 4, SlotType.ORGAN), new SlotPoint(11, 4, SlotType.ORGAN), new SlotPoint(12, 4, SlotType.ORGAN), new SlotPoint(13, 4, SlotType.ORGAN), new SlotPoint(7, 5, SlotType.ORGAN), new SlotPoint(8, 5, SlotType.ORGAN), new SlotPoint(9, 5, SlotType.ORGAN), new SlotPoint(10, 5, SlotType.ORGAN), new SlotPoint(11, 5, SlotType.ORGAN), new SlotPoint(12, 5, SlotType.ORGAN), new SlotPoint(13, 5, SlotType.ORGAN), new SlotPoint(7, 6, SlotType.ORGAN), new SlotPoint(8, 6, SlotType.ORGAN), new SlotPoint(9, 6, SlotType.ORGAN), new SlotPoint(10, 6, SlotType.ORGAN), new SlotPoint(11, 6, SlotType.ORGAN), new SlotPoint(12, 6, SlotType.ORGAN), new SlotPoint(13, 6, SlotType.ORGAN), new SlotPoint(7, 7, SlotType.ORGAN), new SlotPoint(8, 7, SlotType.ORGAN), new SlotPoint(9, 7, SlotType.ORGAN), new SlotPoint(10, 7, SlotType.ORGAN), new SlotPoint(11, 7, SlotType.ORGAN), new SlotPoint(12, 7, SlotType.ORGAN), new SlotPoint(2, 8, SlotType.ORGAN), new SlotPoint(3, 8, SlotType.ORGAN), new SlotPoint(4, 8, SlotType.ORGAN), new SlotPoint(6, 8, SlotType.ORGAN), new SlotPoint(7, 8, SlotType.ORGAN), new SlotPoint(8, 8, SlotType.ORGAN), new SlotPoint(9, 8, SlotType.ORGAN), new SlotPoint(10, 8, SlotType.ORGAN), new SlotPoint(11, 8, SlotType.ORGAN), new SlotPoint(12, 8, SlotType.ORGAN), new SlotPoint(1, 9, SlotType.ORGAN), new SlotPoint(2, 9, SlotType.ORGAN), new SlotPoint(3, 9, SlotType.ORGAN), new SlotPoint(4, 9, SlotType.ORGAN), new SlotPoint(5, 9, SlotType.ORGAN), new SlotPoint(6, 9, SlotType.ORGAN), new SlotPoint(7, 9, SlotType.ORGAN), new SlotPoint(8, 9, SlotType.ORGAN), new SlotPoint(9, 9, SlotType.ORGAN), new SlotPoint(10, 9, SlotType.ORGAN), new SlotPoint(11, 9, SlotType.ORGAN), new SlotPoint(0, 10, SlotType.ORGAN), new SlotPoint(1, 10, SlotType.ORGAN), new SlotPoint(2, 10, SlotType.ORGAN), new SlotPoint(3, 10, SlotType.ORGAN), new SlotPoint(4, 10, SlotType.ORGAN), new SlotPoint(5, 10, SlotType.ORGAN), new SlotPoint(6, 10, SlotType.ORGAN), new SlotPoint(7, 10, SlotType.ORGAN), new SlotPoint(8, 10, SlotType.ORGAN), new SlotPoint(9, 10, SlotType.ORGAN), new SlotPoint(10, 10, SlotType.ORGAN), new SlotPoint(11, 10, SlotType.ORGAN), new SlotPoint(0, 11, SlotType.ORGAN), new SlotPoint(1, 11, SlotType.ORGAN), new SlotPoint(2, 11, SlotType.ORGAN), new SlotPoint(4, 11, SlotType.ORGAN), new SlotPoint(5, 11, SlotType.ORGAN), new SlotPoint(6, 11, SlotType.ORGAN), new SlotPoint(7, 11, SlotType.ORGAN), new SlotPoint(8, 11, SlotType.ORGAN), new SlotPoint(9, 11, SlotType.ORGAN), new SlotPoint(10, 11, SlotType.ORGAN), new SlotPoint(0, 12, SlotType.ORGAN), new SlotPoint(1, 12, SlotType.ORGAN), new SlotPoint(2, 12, SlotType.ORGAN), new SlotPoint(6, 12, SlotType.ORGAN), new SlotPoint(7, 12, SlotType.ORGAN), new SlotPoint(8, 12, SlotType.ORGAN), new SlotPoint(1, 13, SlotType.ORGAN), new SlotPoint(2, 13, SlotType.ORGAN), new SlotPoint(3, 13, SlotType.ORGAN), new SlotPoint(2, 14, SlotType.ORGAN), new SlotPoint(3, 14, SlotType.ORGAN), new SlotPoint(4, 14, SlotType.ORGAN), new SlotPoint(3, 15, SlotType.ORGAN))).setTexture(Bittermelon.resource("textures/gui/organs/anatomical_stomach_small.png")))
                    .shape(List.of(new Point(6, 0), new Point(5, 1), new Point(6, 1), new Point(7, 1), new Point(10, 1), new Point(11, 1), new Point(5, 2), new Point(6, 2), new Point(7, 2), new Point(9, 2), new Point(10, 2), new Point(11, 2), new Point(12, 2), new Point(5, 3), new Point(6, 3), new Point(7, 3), new Point(8, 3), new Point(9, 3), new Point(10, 3), new Point(11, 3), new Point(12, 3), new Point(13, 3), new Point(6, 4), new Point(7, 4), new Point(8, 4), new Point(9, 4), new Point(10, 4), new Point(11, 4), new Point(12, 4), new Point(13, 4), new Point(7, 5), new Point(8, 5), new Point(9, 5), new Point(10, 5), new Point(11, 5), new Point(12, 5), new Point(13, 5), new Point(7, 6), new Point(8, 6), new Point(9, 6), new Point(10, 6), new Point(11, 6), new Point(12, 6), new Point(13, 6), new Point(7, 7), new Point(8, 7), new Point(9, 7), new Point(10, 7), new Point(11, 7), new Point(12, 7), new Point(2, 8), new Point(3, 8), new Point(4, 8), new Point(6, 8), new Point(7, 8), new Point(8, 8), new Point(9, 8), new Point(10, 8), new Point(11, 8), new Point(12, 8), new Point(1, 9), new Point(2, 9), new Point(3, 9), new Point(4, 9), new Point(5, 9), new Point(6, 9), new Point(7, 9), new Point(8, 9), new Point(9, 9), new Point(10, 9), new Point(11, 9), new Point(0, 10), new Point(1, 10), new Point(2, 10), new Point(3, 10), new Point(4, 10), new Point(5, 10), new Point(6, 10), new Point(7, 10), new Point(8, 10), new Point(9, 10), new Point(10, 10), new Point(11, 10), new Point(0, 11), new Point(1, 11), new Point(2, 11), new Point(4, 11), new Point(5, 11), new Point(6, 11), new Point(7, 11), new Point(8, 11), new Point(9, 11), new Point(10, 11), new Point(0, 12), new Point(1, 12), new Point(2, 12), new Point(6, 12), new Point(7, 12), new Point(8, 12), new Point(1, 13), new Point(2, 13), new Point(3, 13), new Point(2, 14), new Point(3, 14), new Point(4, 14), new Point(3, 15) ))
                    .visualData(VisualData.empty()
                            .withWidth(14)
                            .withHeight(16)
                            .withIcon("anatomical_stomach_small"))
            )
    );

    public static final DeferredHolder<Compartment, Compartment> SMALL_INTESTINE = COMPARTMENTS.register("small_intestine",
            () -> new Compartment("small_intestine", new Compartment.Properties()
                    .defaultHealth(40)
                    .visualData(VisualData.empty()
                            .withIcon("anatomical_small_intestine"))
                    .addAttribute(MedicalAttribute.CIRCULATION)
                    .addAttribute(MedicalAttribute.MOVEMENT)
                    .addAttribute(MedicalAttribute.MANIPULATION)
                    .addAttribute(MedicalAttribute.RESPIRATION)
            )
    );

    public static final DeferredHolder<Compartment, Compartment> COLON = COMPARTMENTS.register("colon",
            () -> new Compartment("colon", new Compartment.Properties()
                    .defaultHealth(40)
                    .item(BitterItems.COLON)
                    .visualData(VisualData.empty()
                            .withIcon("anatomical_colon"))
            )
    );

    public static final DeferredHolder<Compartment, Compartment> LIVER = COMPARTMENTS.register("liver",
            () -> new Compartment("liver", new Compartment.Properties()
                    .defaultHealth(40)
                    .item(BitterItems.LIVER)
                    .addAttribute(MedicalAttribute.ELIMINATION)
                    .addAttribute(MedicalAttribute.BRAIN_MOTOR_ABILITY)
                    .addAttribute(MedicalAttribute.BRAIN_CONSCIOUSNESS)
                    .addAttribute(MedicalAttribute.CIRCULATION)
                    .addAttribute(MedicalAttribute.MOVEMENT)
                    .addAttribute(MedicalAttribute.MANIPULATION)
                    .addAttribute(MedicalAttribute.RESPIRATION)
                    .layers(new LayerData("", 14, 10, List.of(new SlotPoint(3, 0, SlotType.ORGAN), new SlotPoint(4, 0, SlotType.ORGAN), new SlotPoint(5, 0, SlotType.ORGAN), new SlotPoint(6, 0, SlotType.ORGAN), new SlotPoint(7, 0, SlotType.ORGAN), new SlotPoint(8, 0, SlotType.ORGAN), new SlotPoint(9, 0, SlotType.ORGAN), new SlotPoint(10, 0, SlotType.ORGAN), new SlotPoint(11, 0, SlotType.ORGAN), new SlotPoint(12, 0, SlotType.ORGAN), new SlotPoint(13, 0, SlotType.ORGAN), new SlotPoint(2, 1, SlotType.ORGAN), new SlotPoint(3, 1, SlotType.ORGAN), new SlotPoint(4, 1, SlotType.ORGAN), new SlotPoint(5, 1, SlotType.ORGAN), new SlotPoint(6, 1, SlotType.ORGAN), new SlotPoint(7, 1, SlotType.ORGAN), new SlotPoint(8, 1, SlotType.ORGAN), new SlotPoint(9, 1, SlotType.ORGAN), new SlotPoint(10, 1, SlotType.ORGAN), new SlotPoint(11, 1, SlotType.ORGAN), new SlotPoint(12, 1, SlotType.ORGAN), new SlotPoint(13, 1, SlotType.ORGAN), new SlotPoint(1, 2, SlotType.ORGAN), new SlotPoint(2, 2, SlotType.ORGAN), new SlotPoint(3, 2, SlotType.ORGAN), new SlotPoint(4, 2, SlotType.ORGAN), new SlotPoint(5, 2, SlotType.ORGAN), new SlotPoint(6, 2, SlotType.ORGAN), new SlotPoint(7, 2, SlotType.ORGAN), new SlotPoint(8, 2, SlotType.ORGAN), new SlotPoint(9, 2, SlotType.ORGAN), new SlotPoint(10, 2, SlotType.ORGAN), new SlotPoint(11, 2, SlotType.ORGAN), new SlotPoint(12, 2, SlotType.ORGAN), new SlotPoint(13, 2, SlotType.ORGAN), new SlotPoint(1, 3, SlotType.ORGAN), new SlotPoint(2, 3, SlotType.ORGAN), new SlotPoint(3, 3, SlotType.ORGAN), new SlotPoint(4, 3, SlotType.ORGAN), new SlotPoint(5, 3, SlotType.ORGAN), new SlotPoint(6, 3, SlotType.ORGAN), new SlotPoint(7, 3, SlotType.ORGAN), new SlotPoint(8, 3, SlotType.ORGAN), new SlotPoint(9, 3, SlotType.ORGAN), new SlotPoint(10, 3, SlotType.ORGAN), new SlotPoint(11, 3, SlotType.ORGAN), new SlotPoint(12, 3, SlotType.ORGAN), new SlotPoint(1, 4, SlotType.ORGAN), new SlotPoint(2, 4, SlotType.ORGAN), new SlotPoint(3, 4, SlotType.ORGAN), new SlotPoint(4, 4, SlotType.ORGAN), new SlotPoint(5, 4, SlotType.ORGAN), new SlotPoint(6, 4, SlotType.ORGAN), new SlotPoint(7, 4, SlotType.ORGAN), new SlotPoint(8, 4, SlotType.ORGAN), new SlotPoint(9, 4, SlotType.ORGAN), new SlotPoint(10, 4, SlotType.ORGAN), new SlotPoint(11, 4, SlotType.ORGAN), new SlotPoint(0, 5, SlotType.ORGAN), new SlotPoint(1, 5, SlotType.ORGAN), new SlotPoint(2, 5, SlotType.ORGAN), new SlotPoint(3, 5, SlotType.ORGAN), new SlotPoint(4, 5, SlotType.ORGAN), new SlotPoint(5, 5, SlotType.ORGAN), new SlotPoint(6, 5, SlotType.ORGAN), new SlotPoint(7, 5, SlotType.ORGAN), new SlotPoint(8, 5, SlotType.ORGAN), new SlotPoint(9, 5, SlotType.ORGAN), new SlotPoint(10, 5, SlotType.ORGAN), new SlotPoint(0, 6, SlotType.ORGAN), new SlotPoint(1, 6, SlotType.ORGAN), new SlotPoint(2, 6, SlotType.ORGAN), new SlotPoint(3, 6, SlotType.ORGAN), new SlotPoint(4, 6, SlotType.ORGAN), new SlotPoint(5, 6, SlotType.ORGAN), new SlotPoint(6, 6, SlotType.ORGAN), new SlotPoint(7, 6, SlotType.ORGAN), new SlotPoint(0, 7, SlotType.ORGAN), new SlotPoint(1, 7, SlotType.ORGAN), new SlotPoint(2, 7, SlotType.ORGAN), new SlotPoint(3, 7, SlotType.ORGAN), new SlotPoint(4, 7, SlotType.ORGAN), new SlotPoint(5, 7, SlotType.ORGAN), new SlotPoint(0, 8, SlotType.ORGAN), new SlotPoint(1, 8, SlotType.ORGAN), new SlotPoint(2, 8, SlotType.ORGAN), new SlotPoint(0, 9, SlotType.ORGAN), new SlotPoint(1, 9, SlotType.ORGAN))).setTexture(Bittermelon.resource("textures/gui/organs/anatomical_liver_small.png")))
                    .shape(List.of(new Point(3, 0), new Point(4, 0), new Point(5, 0), new Point(6, 0), new Point(7, 0), new Point(8, 0), new Point(9, 0), new Point(10, 0), new Point(11, 0), new Point(12, 0), new Point(13, 0), new Point(2, 1), new Point(3, 1), new Point(4, 1), new Point(5, 1), new Point(6, 1), new Point(7, 1), new Point(8, 1), new Point(9, 1), new Point(10, 1), new Point(11, 1), new Point(12, 1), new Point(13, 1), new Point(1, 2), new Point(2, 2), new Point(3, 2), new Point(4, 2), new Point(5, 2), new Point(6, 2), new Point(7, 2), new Point(8, 2), new Point(9, 2), new Point(10, 2), new Point(11, 2), new Point(12, 2), new Point(13, 2), new Point(1, 3), new Point(2, 3), new Point(3, 3), new Point(4, 3), new Point(5, 3), new Point(6, 3), new Point(7, 3), new Point(8, 3), new Point(9, 3), new Point(10, 3), new Point(11, 3), new Point(12, 3), new Point(1, 4), new Point(2, 4), new Point(3, 4), new Point(4, 4), new Point(5, 4), new Point(6, 4), new Point(7, 4), new Point(8, 4), new Point(9, 4), new Point(10, 4), new Point(11, 4), new Point(0, 5), new Point(1, 5), new Point(2, 5), new Point(3, 5), new Point(4, 5), new Point(5, 5), new Point(6, 5), new Point(7, 5), new Point(8, 5), new Point(9, 5), new Point(10, 5), new Point(0, 6), new Point(1, 6), new Point(2, 6), new Point(3, 6), new Point(4, 6), new Point(5, 6), new Point(6, 6), new Point(7, 6), new Point(0, 7), new Point(1, 7), new Point(2, 7), new Point(3, 7), new Point(4, 7), new Point(5, 7), new Point(0, 8), new Point(1, 8), new Point(2, 8), new Point(0, 9), new Point(1, 9) ))
                    .visualData(VisualData.empty().withWidth(14).withHeight(10).withIcon("anatomical_liver_small"))
            )
    );

    public static final DeferredHolder<Compartment, Compartment> GALLBLADDER = COMPARTMENTS.register("gallbladder",
            () -> new Compartment("gallbladder", new Compartment.Properties()
                    .defaultHealth(40)
                    .item(BitterItems.GALLBLADDER)
                    .layers(new LayerData("Gallbladder Tissue", 3, 3, List.of(
                            new SlotPoint(1, 0, SlotType.ORGAN),
                            new SlotPoint(2, 0, SlotType.ORGAN),
                            new SlotPoint(0, 1, SlotType.ORGAN),
                            new SlotPoint(1, 1, SlotType.ORGAN),
                            new SlotPoint(2, 1, SlotType.ORGAN),
                            new SlotPoint(1, 2, SlotType.ORGAN)
                    )))
                    .shape(List.of(
                            new Point(1, 0),
                            new Point(2, 0),
                            new Point(0, 1),
                            new Point(1, 1),
                            new Point(2, 1),
                            new Point(1, 2)
                    ))
                    .visualData(VisualData.empty()
                            .withIcon("anatomical_gallbladder")
                            .withHeight(3)
                            .withWidth(3))
            )
    );

    public static final DeferredHolder<Compartment, Compartment> PANCREAS = COMPARTMENTS.register("pancreas",
            () -> new Compartment("pancreas", new Compartment.Properties()
                    .defaultHealth(25)
            )
    );

    public static final DeferredHolder<Compartment, Compartment> SPLEEN = COMPARTMENTS.register("spleen",
            () -> new Compartment("spleen", new Compartment.Properties()
                    .defaultHealth(20)
            )
    );

    public static final DeferredHolder<Compartment, Compartment> EYE = COMPARTMENTS.register("eye",
            () -> new Compartment("eye", new Compartment.Properties()
                    .defaultHealth(10)
            )
    );

    public static final DeferredHolder<Compartment, Compartment> CUT = COMPARTMENTS.register("cut",
            () -> new Compartment("cut", new Compartment.Properties()
                    .shape(List.of(new Point(0, 0)))
                    .visualData(VisualData.empty().withIcon("cut"))
                    .component(BitterDataComponents.REVEAL_DISTANCE.get(), 1)));

    public static final DeferredHolder<Compartment, Compartment> FRACTURE = COMPARTMENTS.register("fracture",
            () -> new Compartment("fracture", new Compartment.Properties()
                    .shape(List.of(new Point(0, 0)))
                    .visualData(VisualData.empty().withIcon(ResourceLocation.withDefaultNamespace("textures/block/destroy_stage_9.png")))
                    .component(BitterDataComponents.REVEAL_DISTANCE.get(), 1)));

    public static final DeferredHolder<Compartment, Compartment> BULLET = COMPARTMENTS.register("bullet",
            () -> new Compartment("bullet", new Compartment.Properties()
                    .shape(List.of(new Point(0, 0)))
                    .visualData(VisualData.empty().withIcon(ResourceLocation.withDefaultNamespace("textures/block/gold_block.png")))));
}
