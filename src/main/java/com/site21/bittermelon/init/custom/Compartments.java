package com.site21.bittermelon.init.custom;

import com.site21.bittermelon.Bittermelon;
import com.site21.bittermelon.common.content.compartments.SharpObject;
import com.site21.bittermelon.common.systems.medical.compartment.Compartment;
import com.site21.bittermelon.common.systems.medical.compartment.LayerData;
import com.site21.bittermelon.common.systems.medical.compartment.MedicalAttribute;
import com.site21.bittermelon.common.systems.medical.compartment.VisualData;
import com.site21.bittermelon.init.neoforge.BitterItems;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import static com.site21.bittermelon.init.neoforge.BitterRegistries.COMPARTMENT_REGISTRY_KEY;
import static net.minecraft.world.item.Items.RED_WOOL;

public class Compartments {
    public static final DeferredRegister<Compartment> COMPARTMENTS = DeferredRegister.create(COMPARTMENT_REGISTRY_KEY, Bittermelon.MOD_ID);

    public static final DeferredHolder<Compartment, Compartment> WHOLE_BODY = COMPARTMENTS.register("whole_body",
            () -> new Compartment("whole_body", new Compartment.Properties()
                    .defaultHealth(100)
                    .layers(
                            LayerData.defaultSize(ResourceLocation.withDefaultNamespace("textures/block/stone.png"), "Body")
                    )
                    .item(BitterItems.BODY_PART.get())
            )
    );

    public static final DeferredHolder<Compartment, Compartment> HEAD = COMPARTMENTS.register("head",
            () -> new Compartment("head", new Compartment.Properties()
                    .defaultHealth(100)
                    .layers(
                            LayerData.defaultSize(ResourceLocation.withDefaultNamespace("textures/block/white_terracotta.png"), "Scalp"),
                            LayerData.defaultSize(ResourceLocation.withDefaultNamespace("textures/block/bone_block_side.png"), "Skull"),
                            LayerData.defaultSize(ResourceLocation.withDefaultNamespace("textures/block/black_concrete.png"), "Brain Cavity"),
                            LayerData.defaultSize(ResourceLocation.withDefaultNamespace("textures/block/smooth_stone.png"), "Facial Structure")
                    )
                    .item(RED_WOOL)
            )
    );

    public static final DeferredHolder<Compartment, Compartment> TORSO = COMPARTMENTS.register("torso",
            () -> new Compartment("torso", new Compartment.Properties()
                    .defaultHealth(150)
                    .layers(
                            LayerData.defaultSize(ResourceLocation.withDefaultNamespace("textures/block/white_terracotta.png"), "Skin"),
                            LayerData.defaultSize(ResourceLocation.withDefaultNamespace("textures/block/bone_block_side.png"), "Ribcage"),
                            LayerData.defaultSize(ResourceLocation.withDefaultNamespace("textures/block/red_concrete.png"), "Thoracic Cavity")
                    )
                    .item(RED_WOOL)
            )
    );

    public static final DeferredHolder<Compartment, Compartment> ABDOMEN = COMPARTMENTS.register("abdomen",
            () -> new Compartment("abdomen", new Compartment.Properties()
                    .defaultHealth(100)
                    .layers(
                            LayerData.defaultSize(ResourceLocation.withDefaultNamespace("textures/block/white_terracotta.png"), "Skin"),
                            LayerData.defaultSize(ResourceLocation.withDefaultNamespace("textures/block/horn_coral_block.png"), "Fat"),
                            LayerData.defaultSize(ResourceLocation.withDefaultNamespace("textures/block/netherrack.png"), "Muscle"),
                            LayerData.defaultSize(ResourceLocation.withDefaultNamespace("textures/block/dead_brain_coral_block.png"), "Peritoneum"),
                            LayerData.defaultSize(ResourceLocation.withDefaultNamespace("textures/block/netherrack.png"), "Abdominal Cavity"),
                            LayerData.defaultSize(ResourceLocation.withDefaultNamespace("textures/block/brown_concrete.png"), "Retroperitoneal Space")
                    )
                    .item(RED_WOOL)
            )
    );

    public static final DeferredHolder<Compartment, Compartment> LIMB = COMPARTMENTS.register("limb",
            () -> new Compartment("limb", new Compartment.Properties()
                    .defaultHealth(80)
                    .layers(
                            new LayerData(ResourceLocation.withDefaultNamespace("textures/block/white_terracotta.png"), "Skin", 0, 0),
                            new LayerData(ResourceLocation.withDefaultNamespace("textures/block/netherrack.png"), "Muscle", 0, 0),
                            new LayerData(ResourceLocation.withDefaultNamespace("textures/block/bone_block_side.png"), "Bone", 0, 0)
                    )
                    .item(BitterItems.BODY_PART.get())
            )
    );

    // ==================== Tissue Types ====================
    public static final DeferredHolder<Compartment, Compartment> SOFT_TISSUE = COMPARTMENTS.register("soft_tissue",
            () -> new Compartment("soft_tissue", new Compartment.Properties()
                    .defaultHealth(50)
                    .layers() // No layers - leaf node
                    .item(BitterItems.BODY_PART.get())
            )
    );

    public static final DeferredHolder<Compartment, Compartment> SKIN = COMPARTMENTS.register("skin",
            () -> new Compartment("skin", new Compartment.Properties()
                    .defaultHealth(40)
                    .layers()
                    .item(BitterItems.BODY_PART.get())
            )
    );

    public static final DeferredHolder<Compartment, Compartment> FAT = COMPARTMENTS.register("fat",
            () -> new Compartment("fat", new Compartment.Properties()
                    .defaultHealth(60)
                    .layers()
                    .item(BitterItems.BODY_PART.get())
            )
    );

    public static final DeferredHolder<Compartment, Compartment> MUSCLE = COMPARTMENTS.register("muscle",
            () -> new Compartment("muscle", new Compartment.Properties()
                    .defaultHealth(100)
                    .layers()
                    .item(BitterItems.BODY_PART.get())
            )
    );

    public static final DeferredHolder<Compartment, Compartment> MEMBRANE = COMPARTMENTS.register("membrane",
            () -> new Compartment("membrane", new Compartment.Properties()
                    .defaultHealth(5)
                    .layers()
                    .item(BitterItems.BODY_PART.get())
            )
    );

    public static final DeferredHolder<Compartment, Compartment> BRAIN = COMPARTMENTS.register("brain",
            () -> new Compartment("brain", new Compartment.Properties()
                    .defaultHealth(20)
                    .layers(
                            LayerData.defaultSize(ResourceLocation.withDefaultNamespace("textures/block/dead_brain_coral_block.png"), "Meninges"),
                            LayerData.defaultSize(ResourceLocation.withDefaultNamespace("textures/block/brain_coral_block.png"), "Brain")
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
                            new LayerData(ResourceLocation.withDefaultNamespace("textures/block/pink_terracotta.png"), "Pericardium", 0, 0),
                            new LayerData(ResourceLocation.withDefaultNamespace("textures/block/red_concrete.png"), "Cardiac Chambers", 0, 0)
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
                    .item(BitterItems.KIDNEY.get())
            )
    );

    public static final DeferredHolder<Compartment, Compartment> STOMACH = COMPARTMENTS.register("stomach",
            () -> new Compartment("stomach", new Compartment.Properties()
                    .defaultHealth(30)
                    .item(BitterItems.STOMACH.get())
                    .addAttribute(MedicalAttribute.DIGESTION)
                    .visualData(VisualData.empty()
                            .x(20)
                            .y(0)
                            .z(2)
                            .width(15)
                            .height(14)
                            .scale(5)
                            .icon("anatomical_stomach"))
            )
    );

    public static final DeferredHolder<Compartment, Compartment> SMALL_INTESTINE = COMPARTMENTS.register("small_intestine",
            () -> new Compartment("small_intestine", new Compartment.Properties()
                    .defaultHealth(40)
                    .visualData(VisualData.empty()
                            .x(5)
                            .y(40)
                            .z(0)
                            .width(17)
                            .height(19)
                            .scale(5)
                            .icon("anatomical_small_intestine"))
                    .addAttribute(MedicalAttribute.CIRCULATION)
                    .addAttribute(MedicalAttribute.MOVEMENT)
                    .addAttribute(MedicalAttribute.MANIPULATION)
                    .addAttribute(MedicalAttribute.RESPIRATION)
            )
    );

    public static final DeferredHolder<Compartment, Compartment> COLON = COMPARTMENTS.register("colon",
            () -> new Compartment("colon", new Compartment.Properties()
                    .defaultHealth(40)
                    .item(BitterItems.COLON.get())
                    .visualData(VisualData.empty()
                            .x(-5)
                            .y(45)
                            .z(1)
                            .width(22)
                            .height(19)
                            .scale(5)
                            .icon("anatomical_colon"))
            )
    );

    public static final DeferredHolder<Compartment, Compartment> LIVER = COMPARTMENTS.register("liver",
            () -> new Compartment("liver", new Compartment.Properties()
                    .defaultHealth(40)
                    .item(BitterItems.LIVER.get())
                    .addAttribute(MedicalAttribute.ELIMINATION)
                    .visualData(VisualData.empty()
                            .x(0)
                            .y(0)
                            .z(4)
                            .width(19)
                            .height(14)
                            .scale(5)
                            .icon("anatomical_liver"))
            )
    );

    public static final DeferredHolder<Compartment, Compartment> GALLBLADDER = COMPARTMENTS.register("gallbladder",
            () -> new Compartment("gallbladder", new Compartment.Properties()
                    .defaultHealth(40)
                    .item(BitterItems.GALLBLADDER.get())
                    .visualData(VisualData.empty()
                            .x(25)
                            .y(50)
                            .z(3)
                            .width(3)
                            .height(3)
                            .scale(5)
                            .icon("anatomical_gallbladder"))
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

    public static final DeferredHolder<Compartment, Compartment> TRAY = COMPARTMENTS.register("tray",
            () -> new Compartment("tray", new Compartment.Properties()
                    .layers(
                            new LayerData(ResourceLocation.withDefaultNamespace("textures/block/iron_block.png"), "Surgical Tray", 100, 80)
                    )
            ));

    public static final DeferredHolder<Compartment, Compartment> SCALPEL = COMPARTMENTS.register("scalpel",
            () -> new SharpObject("scalpel", new Compartment.Properties().item(BitterItems.SCALPEL.get())));

    public static final DeferredHolder<Compartment, Compartment> CUT = COMPARTMENTS.register("cut",
            () -> new Compartment("cut", new Compartment.Properties().visualData(VisualData.empty().icon("cut"))));
}
