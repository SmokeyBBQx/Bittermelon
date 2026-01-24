package com.site21.bittermelon.init.custom;

import com.site21.bittermelon.Bittermelon;
import com.site21.bittermelon.common.content.compartments.SharpObject;
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
                    .layers(
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
                            )),
                            new LayerData("Second Layer", 18, 13, List.of(
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
                                    new SlotPoint(10, 1, SlotType.ORGAN),
                                    new SlotPoint(11, 1, SlotType.ORGAN),
                                    new SlotPoint(12, 1, SlotType.ORGAN),
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
                                    new SlotPoint(9, 2, SlotType.ORGAN),
                                    new SlotPoint(10, 2, SlotType.ORGAN),
                                    new SlotPoint(11, 2, SlotType.ORGAN),
                                    new SlotPoint(12, 2, SlotType.ORGAN),
                                    new SlotPoint(13, 2, SlotType.ORGAN),
                                    new SlotPoint(14, 2, SlotType.FAT),
                                    new SlotPoint(15, 2, SlotType.ORGAN),
                                    new SlotPoint(16, 2, SlotType.ORGAN),
                                    new SlotPoint(17, 2, SlotType.ORGAN),
                                    new SlotPoint(2, 3, SlotType.ORGAN),
                                    new SlotPoint(3, 3, SlotType.FAT),
                                    new SlotPoint(4, 3, SlotType.FAT),
                                    new SlotPoint(5, 3, SlotType.FAT),
                                    new SlotPoint(6, 3, SlotType.ORGAN),
                                    new SlotPoint(7, 3, SlotType.ORGAN),
                                    new SlotPoint(8, 3, SlotType.FAT),
                                    new SlotPoint(9, 3, SlotType.FAT),
                                    new SlotPoint(10, 3, SlotType.FAT),
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
                                    new SlotPoint(8, 4, SlotType.FAT),
                                    new SlotPoint(9, 4, SlotType.BRAIN_TISSUE),
                                    new SlotPoint(10, 4, SlotType.FAT),
                                    new SlotPoint(11, 4, SlotType.FAT),
                                    new SlotPoint(12, 4, SlotType.FAT),
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
                    )
                    .shape(List.of(
                                    new Point(5, 0),
                                    new Point(6, 0),
                                    new Point(7, 0),
                                    new Point(8, 0),
                                    new Point(9, 0),
                                    new Point(10, 0),
                                    new Point(11, 0),
                                    new Point(12, 0),
                                    new Point(13, 0),
                                    new Point(14, 0),
                                    new Point(15, 0),
                                    new Point(16, 0),
                                    new Point(17, 0),
                                    new Point(3, 1),
                                    new Point(4, 1),
                                    new Point(5, 1),
                                    new Point(6, 1),
                                    new Point(7, 1),
                                    new Point(8, 1),
                                    new Point(9, 1),
                                    new Point(10, 1),
                                    new Point(11, 1),
                                    new Point(12, 1),
                                    new Point(13, 1),
                                    new Point(14, 1),
                                    new Point(15, 1),
                                    new Point(16, 1),
                                    new Point(17, 1),
                                    new Point(2, 2),
                                    new Point(3, 2),
                                    new Point(4, 2),
                                    new Point(5, 2),
                                    new Point(6, 2),
                                    new Point(7, 2),
                                    new Point(8, 2),
                                    new Point(9, 2),
                                    new Point(10, 2),
                                    new Point(11, 2),
                                    new Point(12, 2),
                                    new Point(13, 2),
                                    new Point(14, 2),
                                    new Point(15, 2),
                                    new Point(16, 2),
                                    new Point(17, 2),
                                    new Point(2, 3),
                                    new Point(3, 3),
                                    new Point(4, 3),
                                    new Point(5, 3),
                                    new Point(6, 3),
                                    new Point(7, 3),
                                    new Point(8, 3),
                                    new Point(9, 3),
                                    new Point(10, 3),
                                    new Point(11, 3),
                                    new Point(12, 3),
                                    new Point(13, 3),
                                    new Point(14, 3),
                                    new Point(15, 3),
                                    new Point(16, 3),
                                    new Point(1, 4),
                                    new Point(2, 4),
                                    new Point(3, 4),
                                    new Point(4, 4),
                                    new Point(5, 4),
                                    new Point(6, 4),
                                    new Point(7, 4),
                                    new Point(8, 4),
                                    new Point(9, 4),
                                    new Point(10, 4),
                                    new Point(11, 4),
                                    new Point(12, 4),
                                    new Point(13, 4),
                                    new Point(14, 4),
                                    new Point(15, 4),
                                    new Point(0, 5),
                                    new Point(1, 5),
                                    new Point(2, 5),
                                    new Point(3, 5),
                                    new Point(4, 5),
                                    new Point(5, 5),
                                    new Point(6, 5),
                                    new Point(7, 5),
                                    new Point(8, 5),
                                    new Point(9, 5),
                                    new Point(10, 5),
                                    new Point(11, 5),
                                    new Point(12, 5),
                                    new Point(13, 5),
                                    new Point(14, 5),
                                    new Point(0, 6),
                                    new Point(1, 6),
                                    new Point(2, 6),
                                    new Point(3, 6),
                                    new Point(4, 6),
                                    new Point(5, 6),
                                    new Point(6, 6),
                                    new Point(7, 6),
                                    new Point(8, 6),
                                    new Point(9, 6),
                                    new Point(10, 6),
                                    new Point(11, 6),
                                    new Point(0, 7),
                                    new Point(1, 7),
                                    new Point(2, 7),
                                    new Point(3, 7),
                                    new Point(4, 7),
                                    new Point(5, 7),
                                    new Point(6, 7),
                                    new Point(7, 7),
                                    new Point(8, 7),
                                    new Point(9, 7),
                                    new Point(10, 7),
                                    new Point(0, 8),
                                    new Point(1, 8),
                                    new Point(2, 8),
                                    new Point(3, 8),
                                    new Point(4, 8),
                                    new Point(5, 8),
                                    new Point(6, 8),
                                    new Point(7, 8),
                                    new Point(8, 8),
                                    new Point(0, 9),
                                    new Point(1, 9),
                                    new Point(2, 9),
                                    new Point(3, 9),
                                    new Point(4, 9),
                                    new Point(5, 9),
                                    new Point(6, 9),
                                    new Point(7, 9),
                                    new Point(0, 10),
                                    new Point(1, 10),
                                    new Point(2, 10),
                                    new Point(3, 10),
                                    new Point(4, 10),
                                    new Point(5, 10),
                                    new Point(0, 11),
                                    new Point(1, 11),
                                    new Point(2, 11),
                                    new Point(0, 12),
                                    new Point(1, 12)
                            )
                    )
                    .visualData(VisualData.empty().withIcon("anatomical_liver").withWidth(18).withHeight(13))
            )
    );

    public static final DeferredHolder<Compartment, Compartment> WHOLE_BODY = COMPARTMENTS.register("whole_body",
            () -> new Compartment("whole_body", new Compartment.Properties()
                    .defaultHealth(100)
                    .layers(
                            new LayerData("Body", 14, 32, List.of(
                                    new SlotPoint(4, 0, SlotType.CAVITY),
                                    new SlotPoint(5, 0, SlotType.CAVITY),
                                    new SlotPoint(6, 0, SlotType.CAVITY),
                                    new SlotPoint(7, 0, SlotType.CAVITY),
                                    new SlotPoint(8, 0, SlotType.CAVITY),
                                    new SlotPoint(9, 0, SlotType.CAVITY),
                                    new SlotPoint(4, 1, SlotType.CAVITY),
                                    new SlotPoint(5, 1, SlotType.CAVITY),
                                    new SlotPoint(6, 1, SlotType.CAVITY),
                                    new SlotPoint(7, 1, SlotType.CAVITY),
                                    new SlotPoint(8, 1, SlotType.CAVITY),
                                    new SlotPoint(9, 1, SlotType.CAVITY),
                                    new SlotPoint(4, 2, SlotType.CAVITY),
                                    new SlotPoint(5, 2, SlotType.CAVITY),
                                    new SlotPoint(6, 2, SlotType.CAVITY),
                                    new SlotPoint(7, 2, SlotType.CAVITY),
                                    new SlotPoint(8, 2, SlotType.CAVITY),
                                    new SlotPoint(9, 2, SlotType.CAVITY),
                                    new SlotPoint(4, 3, SlotType.CAVITY),
                                    new SlotPoint(5, 3, SlotType.CAVITY),
                                    new SlotPoint(6, 3, SlotType.CAVITY),
                                    new SlotPoint(7, 3, SlotType.CAVITY),
                                    new SlotPoint(8, 3, SlotType.CAVITY),
                                    new SlotPoint(9, 3, SlotType.CAVITY),
                                    new SlotPoint(4, 4, SlotType.CAVITY),
                                    new SlotPoint(5, 4, SlotType.CAVITY),
                                    new SlotPoint(6, 4, SlotType.CAVITY),
                                    new SlotPoint(7, 4, SlotType.CAVITY),
                                    new SlotPoint(8, 4, SlotType.CAVITY),
                                    new SlotPoint(9, 4, SlotType.CAVITY),
                                    new SlotPoint(4, 5, SlotType.CAVITY),
                                    new SlotPoint(5, 5, SlotType.CAVITY),
                                    new SlotPoint(6, 5, SlotType.CAVITY),
                                    new SlotPoint(7, 5, SlotType.CAVITY),
                                    new SlotPoint(8, 5, SlotType.CAVITY),
                                    new SlotPoint(9, 5, SlotType.CAVITY),
                                    new SlotPoint(5, 6, SlotType.CAVITY),
                                    new SlotPoint(6, 6, SlotType.CAVITY),
                                    new SlotPoint(7, 6, SlotType.CAVITY),
                                    new SlotPoint(8, 6, SlotType.CAVITY),
                                    new SlotPoint(0, 7, SlotType.CAVITY),
                                    new SlotPoint(1, 7, SlotType.CAVITY),
                                    new SlotPoint(2, 7, SlotType.CAVITY),
                                    new SlotPoint(3, 7, SlotType.CAVITY),
                                    new SlotPoint(4, 7, SlotType.CAVITY),
                                    new SlotPoint(5, 7, SlotType.CAVITY),
                                    new SlotPoint(6, 7, SlotType.CAVITY),
                                    new SlotPoint(7, 7, SlotType.CAVITY),
                                    new SlotPoint(8, 7, SlotType.CAVITY),
                                    new SlotPoint(9, 7, SlotType.CAVITY),
                                    new SlotPoint(10, 7, SlotType.CAVITY),
                                    new SlotPoint(11, 7, SlotType.CAVITY),
                                    new SlotPoint(12, 7, SlotType.CAVITY),
                                    new SlotPoint(13, 7, SlotType.CAVITY),
                                    new SlotPoint(0, 8, SlotType.CAVITY),
                                    new SlotPoint(1, 8, SlotType.CAVITY),
                                    new SlotPoint(2, 8, SlotType.CAVITY),
                                    new SlotPoint(3, 8, SlotType.CAVITY),
                                    new SlotPoint(4, 8, SlotType.CAVITY),
                                    new SlotPoint(5, 8, SlotType.CAVITY),
                                    new SlotPoint(6, 8, SlotType.CAVITY),
                                    new SlotPoint(7, 8, SlotType.CAVITY),
                                    new SlotPoint(8, 8, SlotType.CAVITY),
                                    new SlotPoint(9, 8, SlotType.CAVITY),
                                    new SlotPoint(10, 8, SlotType.CAVITY),
                                    new SlotPoint(11, 8, SlotType.CAVITY),
                                    new SlotPoint(12, 8, SlotType.CAVITY),
                                    new SlotPoint(13, 8, SlotType.CAVITY),
                                    new SlotPoint(0, 9, SlotType.CAVITY),
                                    new SlotPoint(1, 9, SlotType.CAVITY),
                                    new SlotPoint(2, 9, SlotType.CAVITY),
                                    new SlotPoint(3, 9, SlotType.CAVITY),
                                    new SlotPoint(4, 9, SlotType.CAVITY),
                                    new SlotPoint(5, 9, SlotType.CAVITY),
                                    new SlotPoint(6, 9, SlotType.CAVITY),
                                    new SlotPoint(7, 9, SlotType.CAVITY),
                                    new SlotPoint(8, 9, SlotType.CAVITY),
                                    new SlotPoint(9, 9, SlotType.CAVITY),
                                    new SlotPoint(10, 9, SlotType.CAVITY),
                                    new SlotPoint(11, 9, SlotType.CAVITY),
                                    new SlotPoint(12, 9, SlotType.CAVITY),
                                    new SlotPoint(13, 9, SlotType.CAVITY),
                                    new SlotPoint(0, 10, SlotType.CAVITY),
                                    new SlotPoint(1, 10, SlotType.CAVITY),
                                    new SlotPoint(2, 10, SlotType.CAVITY),
                                    new SlotPoint(3, 10, SlotType.CAVITY),
                                    new SlotPoint(4, 10, SlotType.CAVITY),
                                    new SlotPoint(5, 10, SlotType.CAVITY),
                                    new SlotPoint(6, 10, SlotType.CAVITY),
                                    new SlotPoint(7, 10, SlotType.CAVITY),
                                    new SlotPoint(8, 10, SlotType.CAVITY),
                                    new SlotPoint(9, 10, SlotType.CAVITY),
                                    new SlotPoint(10, 10, SlotType.CAVITY),
                                    new SlotPoint(11, 10, SlotType.CAVITY),
                                    new SlotPoint(12, 10, SlotType.CAVITY),
                                    new SlotPoint(13, 10, SlotType.CAVITY),
                                    new SlotPoint(0, 11, SlotType.CAVITY),
                                    new SlotPoint(1, 11, SlotType.CAVITY),
                                    new SlotPoint(2, 11, SlotType.CAVITY),
                                    new SlotPoint(3, 11, SlotType.CAVITY),
                                    new SlotPoint(4, 11, SlotType.CAVITY),
                                    new SlotPoint(5, 11, SlotType.CAVITY),
                                    new SlotPoint(6, 11, SlotType.CAVITY),
                                    new SlotPoint(7, 11, SlotType.CAVITY),
                                    new SlotPoint(8, 11, SlotType.CAVITY),
                                    new SlotPoint(9, 11, SlotType.CAVITY),
                                    new SlotPoint(10, 11, SlotType.CAVITY),
                                    new SlotPoint(11, 11, SlotType.CAVITY),
                                    new SlotPoint(12, 11, SlotType.CAVITY),
                                    new SlotPoint(13, 11, SlotType.CAVITY),
                                    new SlotPoint(0, 12, SlotType.CAVITY),
                                    new SlotPoint(1, 12, SlotType.CAVITY),
                                    new SlotPoint(2, 12, SlotType.CAVITY),
                                    new SlotPoint(3, 12, SlotType.CAVITY),
                                    new SlotPoint(4, 12, SlotType.CAVITY),
                                    new SlotPoint(5, 12, SlotType.CAVITY),
                                    new SlotPoint(6, 12, SlotType.CAVITY),
                                    new SlotPoint(7, 12, SlotType.CAVITY),
                                    new SlotPoint(8, 12, SlotType.CAVITY),
                                    new SlotPoint(9, 12, SlotType.CAVITY),
                                    new SlotPoint(10, 12, SlotType.CAVITY),
                                    new SlotPoint(11, 12, SlotType.CAVITY),
                                    new SlotPoint(12, 12, SlotType.CAVITY),
                                    new SlotPoint(13, 12, SlotType.CAVITY),
                                    new SlotPoint(0, 13, SlotType.CAVITY),
                                    new SlotPoint(1, 13, SlotType.CAVITY),
                                    new SlotPoint(2, 13, SlotType.CAVITY),
                                    new SlotPoint(3, 13, SlotType.CAVITY),
                                    new SlotPoint(4, 13, SlotType.CAVITY),
                                    new SlotPoint(5, 13, SlotType.CAVITY),
                                    new SlotPoint(6, 13, SlotType.CAVITY),
                                    new SlotPoint(7, 13, SlotType.CAVITY),
                                    new SlotPoint(8, 13, SlotType.CAVITY),
                                    new SlotPoint(9, 13, SlotType.CAVITY),
                                    new SlotPoint(10, 13, SlotType.CAVITY),
                                    new SlotPoint(11, 13, SlotType.CAVITY),
                                    new SlotPoint(12, 13, SlotType.CAVITY),
                                    new SlotPoint(13, 13, SlotType.CAVITY),
                                    new SlotPoint(0, 14, SlotType.CAVITY),
                                    new SlotPoint(1, 14, SlotType.CAVITY),
                                    new SlotPoint(2, 14, SlotType.CAVITY),
                                    new SlotPoint(3, 14, SlotType.CAVITY),
                                    new SlotPoint(4, 14, SlotType.CAVITY),
                                    new SlotPoint(5, 14, SlotType.CAVITY),
                                    new SlotPoint(6, 14, SlotType.CAVITY),
                                    new SlotPoint(7, 14, SlotType.CAVITY),
                                    new SlotPoint(8, 14, SlotType.CAVITY),
                                    new SlotPoint(9, 14, SlotType.CAVITY),
                                    new SlotPoint(10, 14, SlotType.CAVITY),
                                    new SlotPoint(11, 14, SlotType.CAVITY),
                                    new SlotPoint(12, 14, SlotType.CAVITY),
                                    new SlotPoint(13, 14, SlotType.CAVITY),
                                    new SlotPoint(0, 15, SlotType.CAVITY),
                                    new SlotPoint(1, 15, SlotType.CAVITY),
                                    new SlotPoint(2, 15, SlotType.CAVITY),
                                    new SlotPoint(3, 15, SlotType.CAVITY),
                                    new SlotPoint(4, 15, SlotType.CAVITY),
                                    new SlotPoint(5, 15, SlotType.CAVITY),
                                    new SlotPoint(6, 15, SlotType.CAVITY),
                                    new SlotPoint(7, 15, SlotType.CAVITY),
                                    new SlotPoint(8, 15, SlotType.CAVITY),
                                    new SlotPoint(9, 15, SlotType.CAVITY),
                                    new SlotPoint(10, 15, SlotType.CAVITY),
                                    new SlotPoint(11, 15, SlotType.CAVITY),
                                    new SlotPoint(12, 15, SlotType.CAVITY),
                                    new SlotPoint(13, 15, SlotType.CAVITY),
                                    new SlotPoint(0, 16, SlotType.CAVITY),
                                    new SlotPoint(1, 16, SlotType.CAVITY),
                                    new SlotPoint(2, 16, SlotType.CAVITY),
                                    new SlotPoint(3, 16, SlotType.CAVITY),
                                    new SlotPoint(4, 16, SlotType.CAVITY),
                                    new SlotPoint(5, 16, SlotType.CAVITY),
                                    new SlotPoint(6, 16, SlotType.CAVITY),
                                    new SlotPoint(7, 16, SlotType.CAVITY),
                                    new SlotPoint(8, 16, SlotType.CAVITY),
                                    new SlotPoint(9, 16, SlotType.CAVITY),
                                    new SlotPoint(10, 16, SlotType.CAVITY),
                                    new SlotPoint(11, 16, SlotType.CAVITY),
                                    new SlotPoint(12, 16, SlotType.CAVITY),
                                    new SlotPoint(13, 16, SlotType.CAVITY),
                                    new SlotPoint(0, 17, SlotType.CAVITY),
                                    new SlotPoint(1, 17, SlotType.CAVITY),
                                    new SlotPoint(2, 17, SlotType.CAVITY),
                                    new SlotPoint(3, 17, SlotType.CAVITY),
                                    new SlotPoint(4, 17, SlotType.CAVITY),
                                    new SlotPoint(5, 17, SlotType.CAVITY),
                                    new SlotPoint(6, 17, SlotType.CAVITY),
                                    new SlotPoint(7, 17, SlotType.CAVITY),
                                    new SlotPoint(8, 17, SlotType.CAVITY),
                                    new SlotPoint(9, 17, SlotType.CAVITY),
                                    new SlotPoint(10, 17, SlotType.CAVITY),
                                    new SlotPoint(11, 17, SlotType.CAVITY),
                                    new SlotPoint(12, 17, SlotType.CAVITY),
                                    new SlotPoint(13, 17, SlotType.CAVITY),
                                    new SlotPoint(0, 18, SlotType.CAVITY),
                                    new SlotPoint(1, 18, SlotType.CAVITY),
                                    new SlotPoint(2, 18, SlotType.CAVITY),
                                    new SlotPoint(3, 18, SlotType.CAVITY),
                                    new SlotPoint(4, 18, SlotType.CAVITY),
                                    new SlotPoint(5, 18, SlotType.CAVITY),
                                    new SlotPoint(6, 18, SlotType.CAVITY),
                                    new SlotPoint(7, 18, SlotType.CAVITY),
                                    new SlotPoint(8, 18, SlotType.CAVITY),
                                    new SlotPoint(9, 18, SlotType.CAVITY),
                                    new SlotPoint(10, 18, SlotType.CAVITY),
                                    new SlotPoint(11, 18, SlotType.CAVITY),
                                    new SlotPoint(12, 18, SlotType.CAVITY),
                                    new SlotPoint(13, 18, SlotType.CAVITY),
                                    new SlotPoint(0, 19, SlotType.CAVITY),
                                    new SlotPoint(1, 19, SlotType.CAVITY),
                                    new SlotPoint(2, 19, SlotType.CAVITY),
                                    new SlotPoint(3, 19, SlotType.CAVITY),
                                    new SlotPoint(4, 19, SlotType.CAVITY),
                                    new SlotPoint(5, 19, SlotType.CAVITY),
                                    new SlotPoint(6, 19, SlotType.CAVITY),
                                    new SlotPoint(7, 19, SlotType.CAVITY),
                                    new SlotPoint(8, 19, SlotType.CAVITY),
                                    new SlotPoint(9, 19, SlotType.CAVITY),
                                    new SlotPoint(10, 19, SlotType.CAVITY),
                                    new SlotPoint(11, 19, SlotType.CAVITY),
                                    new SlotPoint(12, 19, SlotType.CAVITY),
                                    new SlotPoint(13, 19, SlotType.CAVITY),
                                    new SlotPoint(3, 20, SlotType.CAVITY),
                                    new SlotPoint(4, 20, SlotType.CAVITY),
                                    new SlotPoint(5, 20, SlotType.CAVITY),
                                    new SlotPoint(6, 20, SlotType.CAVITY),
                                    new SlotPoint(7, 20, SlotType.CAVITY),
                                    new SlotPoint(8, 20, SlotType.CAVITY),
                                    new SlotPoint(9, 20, SlotType.CAVITY),
                                    new SlotPoint(10, 20, SlotType.CAVITY),
                                    new SlotPoint(3, 21, SlotType.CAVITY),
                                    new SlotPoint(4, 21, SlotType.CAVITY),
                                    new SlotPoint(5, 21, SlotType.CAVITY),
                                    new SlotPoint(6, 21, SlotType.CAVITY),
                                    new SlotPoint(7, 21, SlotType.CAVITY),
                                    new SlotPoint(8, 21, SlotType.CAVITY),
                                    new SlotPoint(9, 21, SlotType.CAVITY),
                                    new SlotPoint(10, 21, SlotType.CAVITY),
                                    new SlotPoint(3, 22, SlotType.CAVITY),
                                    new SlotPoint(4, 22, SlotType.CAVITY),
                                    new SlotPoint(5, 22, SlotType.CAVITY),
                                    new SlotPoint(6, 22, SlotType.CAVITY),
                                    new SlotPoint(7, 22, SlotType.CAVITY),
                                    new SlotPoint(8, 22, SlotType.CAVITY),
                                    new SlotPoint(9, 22, SlotType.CAVITY),
                                    new SlotPoint(10, 22, SlotType.CAVITY),
                                    new SlotPoint(3, 23, SlotType.CAVITY),
                                    new SlotPoint(4, 23, SlotType.CAVITY),
                                    new SlotPoint(5, 23, SlotType.CAVITY),
                                    new SlotPoint(6, 23, SlotType.CAVITY),
                                    new SlotPoint(7, 23, SlotType.CAVITY),
                                    new SlotPoint(8, 23, SlotType.CAVITY),
                                    new SlotPoint(9, 23, SlotType.CAVITY),
                                    new SlotPoint(10, 23, SlotType.CAVITY),
                                    new SlotPoint(3, 24, SlotType.CAVITY),
                                    new SlotPoint(4, 24, SlotType.CAVITY),
                                    new SlotPoint(5, 24, SlotType.CAVITY),
                                    new SlotPoint(6, 24, SlotType.CAVITY),
                                    new SlotPoint(7, 24, SlotType.CAVITY),
                                    new SlotPoint(8, 24, SlotType.CAVITY),
                                    new SlotPoint(9, 24, SlotType.CAVITY),
                                    new SlotPoint(10, 24, SlotType.CAVITY),
                                    new SlotPoint(3, 25, SlotType.CAVITY),
                                    new SlotPoint(4, 25, SlotType.CAVITY),
                                    new SlotPoint(5, 25, SlotType.CAVITY),
                                    new SlotPoint(6, 25, SlotType.CAVITY),
                                    new SlotPoint(7, 25, SlotType.CAVITY),
                                    new SlotPoint(8, 25, SlotType.CAVITY),
                                    new SlotPoint(9, 25, SlotType.CAVITY),
                                    new SlotPoint(10, 25, SlotType.CAVITY),
                                    new SlotPoint(3, 26, SlotType.CAVITY),
                                    new SlotPoint(4, 26, SlotType.CAVITY),
                                    new SlotPoint(5, 26, SlotType.CAVITY),
                                    new SlotPoint(6, 26, SlotType.CAVITY),
                                    new SlotPoint(7, 26, SlotType.CAVITY),
                                    new SlotPoint(8, 26, SlotType.CAVITY),
                                    new SlotPoint(9, 26, SlotType.CAVITY),
                                    new SlotPoint(10, 26, SlotType.CAVITY),
                                    new SlotPoint(3, 27, SlotType.CAVITY),
                                    new SlotPoint(4, 27, SlotType.CAVITY),
                                    new SlotPoint(5, 27, SlotType.CAVITY),
                                    new SlotPoint(6, 27, SlotType.CAVITY),
                                    new SlotPoint(7, 27, SlotType.CAVITY),
                                    new SlotPoint(8, 27, SlotType.CAVITY),
                                    new SlotPoint(9, 27, SlotType.CAVITY),
                                    new SlotPoint(10, 27, SlotType.CAVITY),
                                    new SlotPoint(3, 28, SlotType.CAVITY),
                                    new SlotPoint(4, 28, SlotType.CAVITY),
                                    new SlotPoint(5, 28, SlotType.CAVITY),
                                    new SlotPoint(6, 28, SlotType.CAVITY),
                                    new SlotPoint(7, 28, SlotType.CAVITY),
                                    new SlotPoint(8, 28, SlotType.CAVITY),
                                    new SlotPoint(9, 28, SlotType.CAVITY),
                                    new SlotPoint(10, 28, SlotType.CAVITY),
                                    new SlotPoint(3, 29, SlotType.CAVITY),
                                    new SlotPoint(4, 29, SlotType.CAVITY),
                                    new SlotPoint(5, 29, SlotType.CAVITY),
                                    new SlotPoint(6, 29, SlotType.CAVITY),
                                    new SlotPoint(7, 29, SlotType.CAVITY),
                                    new SlotPoint(8, 29, SlotType.CAVITY),
                                    new SlotPoint(9, 29, SlotType.CAVITY),
                                    new SlotPoint(10, 29, SlotType.CAVITY),
                                    new SlotPoint(3, 30, SlotType.CAVITY),
                                    new SlotPoint(4, 30, SlotType.CAVITY),
                                    new SlotPoint(5, 30, SlotType.CAVITY),
                                    new SlotPoint(6, 30, SlotType.CAVITY),
                                    new SlotPoint(7, 30, SlotType.CAVITY),
                                    new SlotPoint(8, 30, SlotType.CAVITY),
                                    new SlotPoint(9, 30, SlotType.CAVITY),
                                    new SlotPoint(10, 30, SlotType.CAVITY),
                                    new SlotPoint(3, 31, SlotType.CAVITY),
                                    new SlotPoint(4, 31, SlotType.CAVITY),
                                    new SlotPoint(5, 31, SlotType.CAVITY),
                                    new SlotPoint(6, 31, SlotType.CAVITY),
                                    new SlotPoint(7, 31, SlotType.CAVITY),
                                    new SlotPoint(8, 31, SlotType.CAVITY),
                                    new SlotPoint(9, 31, SlotType.CAVITY),
                                    new SlotPoint(10, 31, SlotType.CAVITY)
                            ))
                    )
                    .item(BitterItems.BODY_PART)
            )
    );

    public static final DeferredHolder<Compartment, Compartment> HEAD = COMPARTMENTS.register("head",
            () -> new Compartment("head", new Compartment.Properties()
                    .defaultHealth(100)
                    .layers(
                            LayerData.fromRegularShape("Scalp", 32, 32, SlotType.SKIN),
                            LayerData.fromRegularShape("Skull", 32, 32, SlotType.BONE),
                            LayerData.fromRegularShape("Brain Cavity", 32, 32, SlotType.CAVITY)
                    )
                    .item(Holder.direct(RED_WOOL))
            )
    );

    public static final DeferredHolder<Compartment, Compartment> TORSO = COMPARTMENTS.register("torso",
            () -> new Compartment("torso", new Compartment.Properties()
                    .defaultHealth(150)
                    .layers(
                            LayerData.fromRegularShape("Skin", 48, 64, SlotType.SKIN),
                            LayerData.fromRegularShape("Ribcage", 48, 64, SlotType.BONE),
                            LayerData.fromRegularShape("Thoracic Cavity", 48, 64, SlotType.CAVITY)
                    )
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

    public static final DeferredHolder<Compartment, Compartment> ARM = COMPARTMENTS.register("arm",
            () -> new Compartment("arm", new Compartment.Properties()
                    .defaultHealth(80)
                    .addAttribute(MedicalAttribute.MANIPULATION)
                    .layers(
                            LayerData.fromRegularShape("Skin", 16, 3, SlotType.SKIN),
                            LayerData.fromRegularShape("Fat", 16, 3, SlotType.FAT),
                            LayerData.fromRegularShape("Muscle", 16, 3, SlotType.MUSCLE),
                            new LayerData("Bone", 16, 3, List.of(
                                    new SlotPoint(0, 0, SlotType.BONE),
                                    new SlotPoint(1, 0, SlotType.BONE),
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
                                    new SlotPoint(7, 1, SlotType.MUSCLE),
                                    new SlotPoint(8, 1, SlotType.BONE),
                                    new SlotPoint(9, 1, SlotType.BONE),
                                    new SlotPoint(10, 1, SlotType.BONE),
                                    new SlotPoint(11, 1, SlotType.BONE),
                                    new SlotPoint(12, 1, SlotType.BONE),
                                    new SlotPoint(13, 1, SlotType.BONE),
                                    new SlotPoint(14, 1, SlotType.BONE),
                                    new SlotPoint(15, 1, SlotType.BONE),
                                    new SlotPoint(0, 2, SlotType.MUSCLE),
                                    new SlotPoint(1, 2, SlotType.MUSCLE),
                                    new SlotPoint(2, 2, SlotType.MUSCLE),
                                    new SlotPoint(3, 2, SlotType.MUSCLE),
                                    new SlotPoint(4, 2, SlotType.MUSCLE),
                                    new SlotPoint(5, 2, SlotType.MUSCLE),
                                    new SlotPoint(6, 2, SlotType.MUSCLE),
                                    new SlotPoint(7, 2, SlotType.BONE),
                                    new SlotPoint(8, 2, SlotType.MUSCLE),
                                    new SlotPoint(9, 2, SlotType.MUSCLE),
                                    new SlotPoint(10, 2, SlotType.MUSCLE),
                                    new SlotPoint(11, 2, SlotType.MUSCLE),
                                    new SlotPoint(12, 2, SlotType.MUSCLE),
                                    new SlotPoint(13, 2, SlotType.MUSCLE),
                                    new SlotPoint(14, 2, SlotType.MUSCLE),
                                    new SlotPoint(15, 2, SlotType.MUSCLE)
                            ))
                    )
                    .shapeOf(3, 10)
                    .item(BitterItems.BODY_PART)
                    .visualData(VisualData.empty().withIcon("arm").withWidth(3).withHeight(10))
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
                    .visualData(VisualData.empty()
                            .withIcon("anatomical_stomach"))
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
                    .layers(
                            new LayerData("Liver Tissue", 19, 14, List.of(
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
                                    new SlotPoint(6, 1, SlotType.ORGAN),
                                    new SlotPoint(7, 1, SlotType.ORGAN),
                                    new SlotPoint(8, 1, SlotType.ORGAN),
                                    new SlotPoint(9, 1, SlotType.ORGAN),
                                    new SlotPoint(10, 1, SlotType.ORGAN),
                                    new SlotPoint(11, 1, SlotType.ORGAN),
                                    new SlotPoint(12, 1, SlotType.ORGAN),
                                    new SlotPoint(13, 1, SlotType.ORGAN),
                                    new SlotPoint(14, 1, SlotType.ORGAN),
                                    new SlotPoint(15, 1, SlotType.ORGAN),
                                    new SlotPoint(16, 1, SlotType.ORGAN),
                                    new SlotPoint(17, 1, SlotType.ORGAN),
                                    new SlotPoint(2, 2, SlotType.ORGAN),
                                    new SlotPoint(3, 2, SlotType.ORGAN),
                                    new SlotPoint(4, 2, SlotType.ORGAN),
                                    new SlotPoint(5, 2, SlotType.ORGAN),
                                    new SlotPoint(6, 2, SlotType.ORGAN),
                                    new SlotPoint(7, 2, SlotType.ORGAN),
                                    new SlotPoint(8, 2, SlotType.ORGAN),
                                    new SlotPoint(9, 2, SlotType.ORGAN),
                                    new SlotPoint(10, 2, SlotType.ORGAN),
                                    new SlotPoint(11, 2, SlotType.ORGAN),
                                    new SlotPoint(12, 2, SlotType.ORGAN),
                                    new SlotPoint(13, 2, SlotType.ORGAN),
                                    new SlotPoint(14, 2, SlotType.ORGAN),
                                    new SlotPoint(15, 2, SlotType.ORGAN),
                                    new SlotPoint(16, 2, SlotType.ORGAN),
                                    new SlotPoint(17, 2, SlotType.ORGAN),
                                    new SlotPoint(2, 3, SlotType.ORGAN),
                                    new SlotPoint(3, 3, SlotType.ORGAN),
                                    new SlotPoint(4, 3, SlotType.ORGAN),
                                    new SlotPoint(5, 3, SlotType.ORGAN),
                                    new SlotPoint(6, 3, SlotType.ORGAN),
                                    new SlotPoint(7, 3, SlotType.ORGAN),
                                    new SlotPoint(8, 3, SlotType.ORGAN),
                                    new SlotPoint(9, 3, SlotType.ORGAN),
                                    new SlotPoint(10, 3, SlotType.ORGAN),
                                    new SlotPoint(11, 3, SlotType.ORGAN),
                                    new SlotPoint(12, 3, SlotType.ORGAN),
                                    new SlotPoint(13, 3, SlotType.ORGAN),
                                    new SlotPoint(14, 3, SlotType.ORGAN),
                                    new SlotPoint(15, 3, SlotType.ORGAN),
                                    new SlotPoint(16, 3, SlotType.ORGAN),
                                    new SlotPoint(1, 4, SlotType.ORGAN),
                                    new SlotPoint(2, 4, SlotType.ORGAN),
                                    new SlotPoint(3, 4, SlotType.ORGAN),
                                    new SlotPoint(4, 4, SlotType.ORGAN),
                                    new SlotPoint(5, 4, SlotType.ORGAN),
                                    new SlotPoint(6, 4, SlotType.ORGAN),
                                    new SlotPoint(7, 4, SlotType.ORGAN),
                                    new SlotPoint(8, 4, SlotType.ORGAN),
                                    new SlotPoint(9, 4, SlotType.ORGAN),
                                    new SlotPoint(10, 4, SlotType.ORGAN),
                                    new SlotPoint(11, 4, SlotType.ORGAN),
                                    new SlotPoint(12, 4, SlotType.ORGAN),
                                    new SlotPoint(13, 4, SlotType.ORGAN),
                                    new SlotPoint(14, 4, SlotType.ORGAN),
                                    new SlotPoint(15, 4, SlotType.ORGAN),
                                    new SlotPoint(0, 5, SlotType.ORGAN),
                                    new SlotPoint(1, 5, SlotType.ORGAN),
                                    new SlotPoint(2, 5, SlotType.ORGAN),
                                    new SlotPoint(3, 5, SlotType.ORGAN),
                                    new SlotPoint(4, 5, SlotType.ORGAN),
                                    new SlotPoint(5, 5, SlotType.ORGAN),
                                    new SlotPoint(6, 5, SlotType.ORGAN),
                                    new SlotPoint(7, 5, SlotType.ORGAN),
                                    new SlotPoint(8, 5, SlotType.ORGAN),
                                    new SlotPoint(9, 5, SlotType.ORGAN),
                                    new SlotPoint(10, 5, SlotType.ORGAN),
                                    new SlotPoint(11, 5, SlotType.ORGAN),
                                    new SlotPoint(12, 5, SlotType.ORGAN),
                                    new SlotPoint(13, 5, SlotType.ORGAN),
                                    new SlotPoint(14, 5, SlotType.ORGAN),
                                    new SlotPoint(0, 6, SlotType.ORGAN),
                                    new SlotPoint(1, 6, SlotType.ORGAN),
                                    new SlotPoint(2, 6, SlotType.ORGAN),
                                    new SlotPoint(3, 6, SlotType.ORGAN),
                                    new SlotPoint(4, 6, SlotType.ORGAN),
                                    new SlotPoint(5, 6, SlotType.ORGAN),
                                    new SlotPoint(6, 6, SlotType.ORGAN),
                                    new SlotPoint(7, 6, SlotType.ORGAN),
                                    new SlotPoint(8, 6, SlotType.ORGAN),
                                    new SlotPoint(9, 6, SlotType.ORGAN),
                                    new SlotPoint(10, 6, SlotType.ORGAN),
                                    new SlotPoint(11, 6, SlotType.ORGAN),
                                    new SlotPoint(0, 7, SlotType.ORGAN),
                                    new SlotPoint(1, 7, SlotType.ORGAN),
                                    new SlotPoint(2, 7, SlotType.ORGAN),
                                    new SlotPoint(3, 7, SlotType.ORGAN),
                                    new SlotPoint(4, 7, SlotType.ORGAN),
                                    new SlotPoint(5, 7, SlotType.ORGAN),
                                    new SlotPoint(6, 7, SlotType.ORGAN),
                                    new SlotPoint(7, 7, SlotType.ORGAN),
                                    new SlotPoint(8, 7, SlotType.ORGAN),
                                    new SlotPoint(9, 7, SlotType.ORGAN),
                                    new SlotPoint(10, 7, SlotType.ORGAN),
                                    new SlotPoint(0, 8, SlotType.ORGAN),
                                    new SlotPoint(1, 8, SlotType.ORGAN),
                                    new SlotPoint(2, 8, SlotType.ORGAN),
                                    new SlotPoint(3, 8, SlotType.ORGAN),
                                    new SlotPoint(4, 8, SlotType.ORGAN),
                                    new SlotPoint(5, 8, SlotType.ORGAN),
                                    new SlotPoint(6, 8, SlotType.ORGAN),
                                    new SlotPoint(7, 8, SlotType.ORGAN),
                                    new SlotPoint(8, 8, SlotType.ORGAN),
                                    new SlotPoint(0, 9, SlotType.ORGAN),
                                    new SlotPoint(1, 9, SlotType.ORGAN),
                                    new SlotPoint(2, 9, SlotType.ORGAN),
                                    new SlotPoint(3, 9, SlotType.ORGAN),
                                    new SlotPoint(4, 9, SlotType.ORGAN),
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
                    )
                    .visualData(VisualData.empty()
                            .withIcon("anatomical_liver"))
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

    public static final DeferredHolder<Compartment, Compartment> SCALPEL = COMPARTMENTS.register("scalpel",
            () -> new SharpObject("scalpel", new Compartment.Properties().item(BitterItems.SCALPEL)));

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
}
