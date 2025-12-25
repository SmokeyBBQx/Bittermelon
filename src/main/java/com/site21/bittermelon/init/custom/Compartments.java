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
                    .visualData(VisualData.empty()
                            .withIcon("anatomical_liver"))
            )
    );

    public static final DeferredHolder<Compartment, Compartment> WHOLE_BODY = COMPARTMENTS.register("whole_body",
            () -> new Compartment("whole_body", new Compartment.Properties()
                    .defaultHealth(100)
                    .layers(
                            LayerData.fromRegularShape("Body", 64, 128, SlotType.CAVITY)
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

    public static final DeferredHolder<Compartment, Compartment> LIMB = COMPARTMENTS.register("limb",
            () -> new Compartment("limb", new Compartment.Properties()
                    .defaultHealth(80)
                    .layers(
                            LayerData.fromRegularShape("Skin", 16, 48, SlotType.SKIN),
                            LayerData.fromRegularShape("Muscle", 16, 48, SlotType.MUSCLE),
                            LayerData.fromRegularShape("Bone", 16, 48, SlotType.BONE)
                    )
                    .item(BitterItems.BODY_PART)
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
}
