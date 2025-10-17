package com.site21.bittermelon.datagen;

import com.site21.bittermelon.Bittermelon;
import com.site21.bittermelon.common.content.blocks.SmallPosterBlock;
import com.site21.bittermelon.common.content.blocks.dirtyfloor.DirtyFloorBlock;
import com.site21.bittermelon.common.content.blocks.electronics.keycardreader.KeycardReaderBlock;
import com.site21.bittermelon.common.content.blocks.electronics.redstonedevice.RedstoneDeviceBlock;
import com.site21.bittermelon.common.content.blocks.properties.Placement;
import com.site21.bittermelon.common.content.blocks.substance.fluid.FluidBlock;
import com.site21.bittermelon.init.neoforge.BitterBlocks;
import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.client.data.models.ModelProvider;
import net.minecraft.client.data.models.MultiVariant;
import net.minecraft.client.data.models.blockstates.MultiPartGenerator;
import net.minecraft.client.data.models.blockstates.MultiVariantGenerator;
import net.minecraft.client.data.models.blockstates.PropertyDispatch;
import net.minecraft.client.data.models.model.*;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.properties.AttachFace;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DoorHingeSide;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

import static com.site21.bittermelon.init.neoforge.BitterBlocks.DIRTY_FLOOR;
import static com.site21.bittermelon.init.neoforge.BitterBlocks.FLUID;
import static com.site21.bittermelon.init.neoforge.BitterBlocks.LARGE_SLIDING_DOOR;
import static com.site21.bittermelon.init.neoforge.BitterItems.*;
import static net.minecraft.client.data.models.BlockModelGenerators.*;

public class BitterModelProvider extends ModelProvider {
    public static final ModelTemplate SLIDING_DOOR_BOTTOM_LEFT = new ModelTemplate(
            Optional.of(ResourceLocation.fromNamespaceAndPath(Bittermelon.MOD_ID, "block/sliding_door_bottom_left")),
            Optional.of("_bottom_left"),
            TextureSlot.TOP, TextureSlot.BOTTOM
    );

    public static final ModelTemplate SLIDING_DOOR_BOTTOM_RIGHT = new ModelTemplate(
            Optional.of(ResourceLocation.fromNamespaceAndPath(Bittermelon.MOD_ID, "block/sliding_door_bottom_right")),
            Optional.of("_bottom_right"),
            TextureSlot.TOP, TextureSlot.BOTTOM
    );

    public static final ModelTemplate SLIDING_DOOR_TOP_LEFT = new ModelTemplate(
            Optional.of(ResourceLocation.fromNamespaceAndPath(Bittermelon.MOD_ID, "block/sliding_door_top_left")),
            Optional.of("_top_left"),
            TextureSlot.TOP, TextureSlot.BOTTOM
    );

    public static final ModelTemplate SLIDING_DOOR_TOP_RIGHT = new ModelTemplate(
            Optional.of(ResourceLocation.fromNamespaceAndPath(Bittermelon.MOD_ID, "block/sliding_door_top_right")),
            Optional.of("_top_right"),
            TextureSlot.TOP, TextureSlot.BOTTOM
    );

    public static final ModelTemplate BUTTON_LIKE = new ModelTemplate(
            Optional.of(ResourceLocation.fromNamespaceAndPath(Bittermelon.MOD_ID, "block/button")),
            Optional.of(""),
            TextureSlot.TEXTURE
    );

    public static final ModelTemplate SMALL_POSTER_LEFT = new ModelTemplate(
            Optional.of(ResourceLocation.fromNamespaceAndPath(Bittermelon.MOD_ID, "block/small_poster_left")),
            Optional.of("_left"),
            TextureSlot.TEXTURE
    );

    public static final ModelTemplate SMALL_POSTER_RIGHT = new ModelTemplate(
            Optional.of(ResourceLocation.fromNamespaceAndPath(Bittermelon.MOD_ID, "block/small_poster_right")),
            Optional.of("_right"),
            TextureSlot.TEXTURE
    );

    public static final ModelTemplate PAINTING_SIDE = new ModelTemplate(
            Optional.of(ResourceLocation.fromNamespaceAndPath(Bittermelon.MOD_ID, "block/painting_side")),
            Optional.of("_side"),
            TextureSlot.TEXTURE
    );

    public static final ModelTemplate PAINTING_TOP = new ModelTemplate(
            Optional.of(ResourceLocation.fromNamespaceAndPath(Bittermelon.MOD_ID, "block/painting_top")),
            Optional.of("_top"),
            TextureSlot.TEXTURE
    );

    public static final ModelTemplate PAINTING_BOTTOM = new ModelTemplate(
            Optional.of(ResourceLocation.fromNamespaceAndPath(Bittermelon.MOD_ID, "block/painting_bottom")),
            Optional.of("_bottom"),
            TextureSlot.TEXTURE
    );

    public static final ModelTemplate TRAPDOOR_SHAPED_SIDE = new ModelTemplate(
            Optional.of(ResourceLocation.withDefaultNamespace("block/template_orientable_trapdoor_open")),
            Optional.of("_side"),
            TextureSlot.TEXTURE
    );

    public static final ModelTemplate TRAPDOOR_SHAPED_TOP = new ModelTemplate(
            Optional.of(ResourceLocation.withDefaultNamespace("block/template_orientable_trapdoor_top")),
            Optional.of("_top"),
            TextureSlot.TEXTURE
    );

    public static final ModelTemplate TRAPDOOR_SHAPED_BOTTOM = new ModelTemplate(
            Optional.of(ResourceLocation.withDefaultNamespace("block/template_orientable_trapdoor_bottom")),
            Optional.of("_bottom"),
            TextureSlot.TEXTURE
    );

    public BitterModelProvider(PackOutput output) {
        super(output, Bittermelon.MOD_ID);
    }

    @Override
    protected void registerModels(@NotNull BlockModelGenerators blockModels, @NotNull ItemModelGenerators itemModels) {
        createFluid(blockModels);
        blockModels.createTrivialCube(BitterBlocks.SMALL_CARDBOARD_BOX.get());
        blockModels.createTrivialCube(BitterBlocks.STRUCTURAL_BLOCK.get());
        blockModels.createTrivialCube(BitterBlocks.ATM.get());
        createIndentedSmallBlock(blockModels, BitterBlocks.CONTAINMENT_PANEL.get());
        blockModels.createDoor(BitterBlocks.SECURE_DOOR.get());
        blockModels.createDoor(BitterBlocks.KEYCARD_READER_SECURE_DOOR.get());
        blockModels.createTrivialCube(LARGE_SLIDING_DOOR.get());
        createSlidingDoor(blockModels, BitterBlocks.WINDOWED_SLIDING_DOOR.get());
        createTrapdoorShaped(blockModels, BitterBlocks.DISTRIBUTION_BOARD.get());
        blockModels.registerSimpleFlatItemModel(BitterBlocks.DISTRIBUTION_BOARD.get());
        createDirtyFloor(blockModels);
        createButtonLike(blockModels, BitterBlocks.THERMOMETER.get());
        createIndentedSmallBlock(blockModels, BitterBlocks.INTERCOM.get());
        blockModels.createTrivialCube(BitterBlocks.ENVIRONMENT_SENSOR.get());
        blockModels.createTrivialCube(BitterBlocks.CONTAINMENT_ALARM.get());
        blockModels.createTrivialCube(BitterBlocks.DETONATOR.get());
        blockModels.createTrivialCube(BitterBlocks.SPEAKER.get());
        createSmallPoster(blockModels, BitterBlocks.YELLOW_INSPECTION_POSTER.get());
        createPainting(blockModels, BitterBlocks.SCP_151.get());
        blockModels.createTrivialCube(BitterBlocks.PERSONNEL_TERMINAL.get());
        blockModels.createTrivialCube(BitterBlocks.KEYCARD_PRINTER.get());
        blockModels.createAirLikeBlock(BitterBlocks.WALL_WRITING.get(), ResourceLocation.withDefaultNamespace("white_concrete_powder"));
        blockModels.createTrivialCube(BitterBlocks.STICKY_NOTE.get());
        createKeycardReader(blockModels);
        createRedstoneDevice(blockModels, BitterBlocks.REDSTONE_DEVICE.get(), TexturedModel.ORIENTABLE);

        // Fluid Containers
        itemModels.generateFlatItem(BEER_BOTTLE.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(WHISKEY_BOTTLE.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(SCP_109.get(), ModelTemplates.FLAT_ITEM);

        // Medical Items
        itemModels.generateFlatItem(SYRINGE.get(), ModelTemplates.FLAT_HANDHELD_ITEM);
        itemModels.generateFlatItem(SCALPEL.get(), ModelTemplates.FLAT_HANDHELD_ITEM);
        itemModels.generateFlatItem(HEMOSTAT.get(), ModelTemplates.FLAT_HANDHELD_ITEM);
        itemModels.generateFlatItem(RETRACTOR.get(), ModelTemplates.FLAT_HANDHELD_ITEM);
        itemModels.generateFlatItem(CAUTERY.get(), ModelTemplates.FLAT_HANDHELD_ITEM);
        itemModels.generateFlatItem(SURGICAL_SPONGE.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(BANDAGE.get(), ModelTemplates.FLAT_ITEM);

        // Body Parts
        itemModels.generateFlatItem(KIDNEY.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(LIVER.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(STOMACH.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(GALLBLADDER.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(PELVIS.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(BLADDER.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(COLON.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(BODY_PART.get(), ModelTemplates.FLAT_ITEM);

        // SCPs
        itemModels.generateFlatItem(SCP_018.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(SCP_377.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(SCP_377_1.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(SCP_2398.get(), ModelTemplates.FLAT_ITEM);

        // Tools and Equipment
        itemModels.generateFlatItem(MOP.get(), ModelTemplates.FLAT_HANDHELD_ITEM);
        itemModels.generateFlatItem(LASER_DESIGNATOR.get(), ModelTemplates.FLAT_HANDHELD_ITEM);
        itemModels.generateFlatItem(TASER.get(), ModelTemplates.FLAT_HANDHELD_ITEM);
        itemModels.generateFlatItem(SCREWDRIVER.get(), ModelTemplates.FLAT_HANDHELD_ITEM);
        itemModels.generateFlatItem(WIRE_CUTTERS.get(), ModelTemplates.FLAT_HANDHELD_ITEM);

        // Writing Utensils
        itemModels.generateFlatItem(PEN.get(), ModelTemplates.FLAT_HANDHELD_ITEM);
        itemModels.generateFlatItem(CHALK.get(), ModelTemplates.FLAT_HANDHELD_ITEM);
        itemModels.generateFlatItem(HIGHLIGHTER.get(), ModelTemplates.FLAT_HANDHELD_ITEM);

        // Consumables
        itemModels.generateFlatItem(CIGARETTE.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(CIGARETTE_BUTT.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(POWDER.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(PILL.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(FORTUNE_COOKIE.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(CRACKED_FORTUNE_COOKIE.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(BITTERMELON.get(), ModelTemplates.FLAT_ITEM);

        // Miscellaneous Items
        itemModels.generateFlatItem(GLASS_SHARD.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(GAS_CYLINDER.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(INTERCOM_PHONE.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(NETWORK_CABLE.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(WIRE.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(HELLO_KITTY_CELLPHONE.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(BASEBALL.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(KEYCARD.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(WRITABLE_PAPER.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(TASER_CARTRIDGE.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(STICKY_NOTE.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(HANDHELD_SYSTEM_INTERFACE.get(), ModelTemplates.FLAT_ITEM);
    }

    public void createFluid(@NotNull BlockModelGenerators blockModels) {
        blockModels.blockStateOutput.accept(
                MultiPartGenerator.multiPart(FLUID.get())
                        // Level 0
                        .with(BlockModelGenerators.condition()
                                        .term(FluidBlock.LEVEL, 0)
                                        .term(FluidBlock.NORTH, false)
                                        .term(FluidBlock.EAST, false)
                                        .term(FluidBlock.SOUTH, false)
                                        .term(FluidBlock.WEST, false)
                                        .term(FluidBlock.FLOATING, false),
                                plainVariant(ResourceLocation.fromNamespaceAndPath(Bittermelon.MOD_ID, "block/fluid/fluid_0")))
                        // Level 1
                        .with(BlockModelGenerators.condition()
                                        .term(FluidBlock.LEVEL, 1)
                                        .term(FluidBlock.NORTH, false)
                                        .term(FluidBlock.EAST, false)
                                        .term(FluidBlock.SOUTH, false)
                                        .term(FluidBlock.WEST, false)
                                        .term(FluidBlock.FLOATING, false),
                                plainVariant(ResourceLocation.fromNamespaceAndPath(Bittermelon.MOD_ID, "block/fluid/fluid_1")))
                        // Levels 3-10
                        .with(BlockModelGenerators.condition().term(FluidBlock.LEVEL, 3).term(FluidBlock.FLOATING, false),
                                plainVariant(ResourceLocation.fromNamespaceAndPath(Bittermelon.MOD_ID, "block/fluid/fluid_2")))
                        .with(BlockModelGenerators.condition().term(FluidBlock.LEVEL, 4).term(FluidBlock.FLOATING, false),
                                plainVariant(ResourceLocation.fromNamespaceAndPath(Bittermelon.MOD_ID, "block/fluid/fluid_3")))
                        .with(BlockModelGenerators.condition().term(FluidBlock.LEVEL, 5).term(FluidBlock.FLOATING, false),
                                plainVariant(ResourceLocation.fromNamespaceAndPath(Bittermelon.MOD_ID, "block/fluid/fluid_4")))
                        .with(BlockModelGenerators.condition().term(FluidBlock.LEVEL, 6).term(FluidBlock.FLOATING, false),
                                plainVariant(ResourceLocation.fromNamespaceAndPath(Bittermelon.MOD_ID, "block/fluid/fluid_5")))
                        .with(BlockModelGenerators.condition().term(FluidBlock.LEVEL, 7).term(FluidBlock.FLOATING, false),
                                plainVariant(ResourceLocation.fromNamespaceAndPath(Bittermelon.MOD_ID, "block/fluid/fluid_6")))
                        .with(BlockModelGenerators.condition().term(FluidBlock.LEVEL, 8).term(FluidBlock.FLOATING, false),
                                plainVariant(ResourceLocation.fromNamespaceAndPath(Bittermelon.MOD_ID, "block/fluid/fluid_7")))
                        .with(BlockModelGenerators.condition().term(FluidBlock.LEVEL, 9).term(FluidBlock.FLOATING, false),
                                plainVariant(ResourceLocation.fromNamespaceAndPath(Bittermelon.MOD_ID, "block/fluid/fluid_8")))
                        .with(BlockModelGenerators.condition().term(FluidBlock.LEVEL, 10).term(FluidBlock.FLOATING, false),
                                plainVariant(ResourceLocation.fromNamespaceAndPath(Bittermelon.MOD_ID, "block/fluid/fluid_9")))
                        // Floating variants
                        .with(BlockModelGenerators.condition()
                                        .term(FluidBlock.NORTH, false)
                                        .term(FluidBlock.EAST, false)
                                        .term(FluidBlock.SOUTH, false)
                                        .term(FluidBlock.WEST, false)
                                        .term(FluidBlock.FLOATING, true),
                                plainVariant(ResourceLocation.fromNamespaceAndPath(Bittermelon.MOD_ID, "block/fluid/fluid_floating")))
                        .with(BlockModelGenerators.condition()
                                        .term(FluidBlock.NORTH, true)
                                        .term(FluidBlock.EAST, false)
                                        .term(FluidBlock.SOUTH, false)
                                        .term(FluidBlock.WEST, false)
                                        .term(FluidBlock.FLOATING, true),
                                plainVariant(ResourceLocation.fromNamespaceAndPath(Bittermelon.MOD_ID, "block/fluid/fluid_floating_n")))
                        .with(BlockModelGenerators.condition()
                                        .term(FluidBlock.NORTH, false)
                                        .term(FluidBlock.EAST, true)
                                        .term(FluidBlock.SOUTH, false)
                                        .term(FluidBlock.WEST, false)
                                        .term(FluidBlock.FLOATING, true),
                                plainVariant(ResourceLocation.fromNamespaceAndPath(Bittermelon.MOD_ID, "block/fluid/fluid_floating_e")))
                        .with(BlockModelGenerators.condition()
                                        .term(FluidBlock.NORTH, false)
                                        .term(FluidBlock.EAST, false)
                                        .term(FluidBlock.SOUTH, true)
                                        .term(FluidBlock.WEST, false)
                                        .term(FluidBlock.FLOATING, true),
                                plainVariant(ResourceLocation.fromNamespaceAndPath(Bittermelon.MOD_ID, "block/fluid/fluid_floating_s")))
                        .with(BlockModelGenerators.condition()
                                        .term(FluidBlock.NORTH, false)
                                        .term(FluidBlock.EAST, false)
                                        .term(FluidBlock.SOUTH, false)
                                        .term(FluidBlock.WEST, true)
                                        .term(FluidBlock.FLOATING, true),
                                plainVariant(ResourceLocation.fromNamespaceAndPath(Bittermelon.MOD_ID, "block/fluid/fluid_floating_w")))
                        // Level 2 base
                        .with(BlockModelGenerators.condition()
                                        .term(FluidBlock.LEVEL, 2)
                                        .term(FluidBlock.NORTH, false)
                                        .term(FluidBlock.EAST, false)
                                        .term(FluidBlock.SOUTH, false)
                                        .term(FluidBlock.WEST, false)
                                        .term(FluidBlock.FLOATING, false),
                                plainVariant(ResourceLocation.fromNamespaceAndPath(Bittermelon.MOD_ID, "block/fluid/fluid")))
                        // Direction variants
                        .with(BlockModelGenerators.condition().term(FluidBlock.NORTH, true).term(FluidBlock.EAST, false).term(FluidBlock.SOUTH, false).term(FluidBlock.WEST, false).term(FluidBlock.FLOATING, false),
                                plainVariant(ResourceLocation.fromNamespaceAndPath(Bittermelon.MOD_ID, "block/fluid/fluid_n")))
                        .with(BlockModelGenerators.condition().term(FluidBlock.NORTH, false).term(FluidBlock.EAST, true).term(FluidBlock.SOUTH, false).term(FluidBlock.WEST, false).term(FluidBlock.FLOATING, false),
                                plainVariant(ResourceLocation.fromNamespaceAndPath(Bittermelon.MOD_ID, "block/fluid/fluid_e")))
                        .with(BlockModelGenerators.condition().term(FluidBlock.NORTH, false).term(FluidBlock.EAST, false).term(FluidBlock.SOUTH, true).term(FluidBlock.WEST, false).term(FluidBlock.FLOATING, false),
                                plainVariant(ResourceLocation.fromNamespaceAndPath(Bittermelon.MOD_ID, "block/fluid/fluid_s")))
                        .with(BlockModelGenerators.condition().term(FluidBlock.NORTH, false).term(FluidBlock.EAST, false).term(FluidBlock.SOUTH, false).term(FluidBlock.WEST, true).term(FluidBlock.FLOATING, false),
                                plainVariant(ResourceLocation.fromNamespaceAndPath(Bittermelon.MOD_ID, "block/fluid/fluid_w")))
                        .with(BlockModelGenerators.condition().term(FluidBlock.NORTH, true).term(FluidBlock.EAST, true).term(FluidBlock.SOUTH, false).term(FluidBlock.WEST, false).term(FluidBlock.FLOATING, false),
                                plainVariant(ResourceLocation.fromNamespaceAndPath(Bittermelon.MOD_ID, "block/fluid/fluid_ne")))
                        .with(BlockModelGenerators.condition().term(FluidBlock.NORTH, true).term(FluidBlock.EAST, false).term(FluidBlock.SOUTH, true).term(FluidBlock.WEST, false).term(FluidBlock.FLOATING, false),
                                plainVariant(ResourceLocation.fromNamespaceAndPath(Bittermelon.MOD_ID, "block/fluid/fluid_ns")))
                        .with(BlockModelGenerators.condition().term(FluidBlock.NORTH, true).term(FluidBlock.EAST, false).term(FluidBlock.SOUTH, false).term(FluidBlock.WEST, true).term(FluidBlock.FLOATING, false),
                                plainVariant(ResourceLocation.fromNamespaceAndPath(Bittermelon.MOD_ID, "block/fluid/fluid_nw")))
                        .with(BlockModelGenerators.condition().term(FluidBlock.NORTH, false).term(FluidBlock.EAST, true).term(FluidBlock.SOUTH, true).term(FluidBlock.WEST, false).term(FluidBlock.FLOATING, false),
                                plainVariant(ResourceLocation.fromNamespaceAndPath(Bittermelon.MOD_ID, "block/fluid/fluid_es")))
                        .with(BlockModelGenerators.condition().term(FluidBlock.NORTH, false).term(FluidBlock.EAST, true).term(FluidBlock.SOUTH, false).term(FluidBlock.WEST, true).term(FluidBlock.FLOATING, false),
                                plainVariant(ResourceLocation.fromNamespaceAndPath(Bittermelon.MOD_ID, "block/fluid/fluid_ew")))
                        .with(BlockModelGenerators.condition().term(FluidBlock.NORTH, false).term(FluidBlock.EAST, false).term(FluidBlock.SOUTH, true).term(FluidBlock.WEST, true).term(FluidBlock.FLOATING, false),
                                plainVariant(ResourceLocation.fromNamespaceAndPath(Bittermelon.MOD_ID, "block/fluid/fluid_sw")))
                        .with(BlockModelGenerators.condition().term(FluidBlock.NORTH, true).term(FluidBlock.EAST, true).term(FluidBlock.SOUTH, true).term(FluidBlock.WEST, false).term(FluidBlock.FLOATING, false),
                                plainVariant(ResourceLocation.fromNamespaceAndPath(Bittermelon.MOD_ID, "block/fluid/fluid_nes")))
                        .with(BlockModelGenerators.condition().term(FluidBlock.NORTH, true).term(FluidBlock.EAST, true).term(FluidBlock.SOUTH, false).term(FluidBlock.WEST, true).term(FluidBlock.FLOATING, false),
                                plainVariant(ResourceLocation.fromNamespaceAndPath(Bittermelon.MOD_ID, "block/fluid/fluid_new")))
                        .with(BlockModelGenerators.condition().term(FluidBlock.NORTH, true).term(FluidBlock.EAST, false).term(FluidBlock.SOUTH, true).term(FluidBlock.WEST, true).term(FluidBlock.FLOATING, false),
                                plainVariant(ResourceLocation.fromNamespaceAndPath(Bittermelon.MOD_ID, "block/fluid/fluid_nsw")))
                        .with(BlockModelGenerators.condition().term(FluidBlock.NORTH, false).term(FluidBlock.EAST, true).term(FluidBlock.SOUTH, true).term(FluidBlock.WEST, true).term(FluidBlock.FLOATING, false),
                                plainVariant(ResourceLocation.fromNamespaceAndPath(Bittermelon.MOD_ID, "block/fluid/fluid_esw")))
                        .with(BlockModelGenerators.condition().term(FluidBlock.NORTH, true).term(FluidBlock.EAST, true).term(FluidBlock.SOUTH, true).term(FluidBlock.WEST, true).term(FluidBlock.FLOATING, false),
                                plainVariant(ResourceLocation.fromNamespaceAndPath(Bittermelon.MOD_ID, "block/fluid/fluid_nesw")))
        );
    }


    public void createSlidingDoor(@NotNull BlockModelGenerators blockModels, Block doorBlock) {
        TextureMapping textureMapping = TextureMapping.door(doorBlock);

        MultiVariant bottomLeft = plainVariant(SLIDING_DOOR_BOTTOM_LEFT.create(doorBlock, textureMapping, blockModels.modelOutput));
        MultiVariant bottomRight = plainVariant(SLIDING_DOOR_BOTTOM_RIGHT.create(doorBlock, textureMapping, blockModels.modelOutput));
        MultiVariant topLeft = plainVariant(SLIDING_DOOR_TOP_LEFT.create(doorBlock, textureMapping, blockModels.modelOutput));
        MultiVariant topRight = plainVariant(SLIDING_DOOR_TOP_RIGHT.create(doorBlock, textureMapping, blockModels.modelOutput));

        blockModels.registerSimpleFlatItemModel(doorBlock.asItem());

        blockModels.blockStateOutput.accept(
                MultiVariantGenerator.dispatch(doorBlock).with(
                                PropertyDispatch.initial(
                                        BlockStateProperties.DOOR_HINGE,
                                        BlockStateProperties.OPEN,
                                        BlockStateProperties.DOUBLE_BLOCK_HALF
                                ).generate((hinge, open, half) -> {
                                    if (half == DoubleBlockHalf.LOWER) {
                                        return hinge == DoorHingeSide.LEFT ? bottomLeft : bottomRight;
                                    } else {
                                        return hinge == DoorHingeSide.LEFT ? topLeft : topRight;
                                    }
                                }))
                        .with(ROTATION_HORIZONTAL_FACING)
        );
    }

    public void createDirtyFloor(@NotNull BlockModelGenerators blockModels) {
        blockModels.blockStateOutput.accept(
                MultiVariantGenerator.dispatch(DIRTY_FLOOR.get()).with(
                                PropertyDispatch.initial(DirtyFloorBlock.DIRTINESS).generate((dirtiness) ->
                                        plainVariant(ResourceLocation.fromNamespaceAndPath(Bittermelon.MOD_ID, "block/dirty_floor_" + dirtiness))))
                        .with(ROTATION_HORIZONTAL_FACING));
    }

    public void createButtonLike(@NotNull BlockModelGenerators blockModels, Block block) {
        blockModels.blockStateOutput.accept(
                MultiVariantGenerator.dispatch(block,
                                plainVariant(BUTTON_LIKE.create(block, TextureMapping.defaultTexture(block), blockModels.modelOutput)))
                        .with(ROTATION_HORIZONTAL_FACING));
    }


    public void createIndentedSmallBlock(@NotNull BlockModelGenerators blockModels, Block block) {
        blockModels.blockStateOutput.accept(
                MultiVariantGenerator.dispatch(block,
                                plainVariant(ResourceLocation.fromNamespaceAndPath(Bittermelon.MOD_ID, "block/indented_small_block")))
                        .with(ROTATION_HORIZONTAL_FACING));
    }

    public void createKeycardReader(@NotNull BlockModelGenerators blockModels) {
        blockModels.registerSimpleItemModel(KEYCARD_READER.get().asItem(), modLocation("item/keycard_reader_left"));
        MultiVariant leftVariant = plainVariant(modLocation("block/keycard_reader_left"));
        MultiVariant rightVariant = plainVariant(modLocation("block/keycard_reader_right"));

        blockModels.blockStateOutput.accept(
                MultiVariantGenerator.dispatch(BitterBlocks.KEYCARD_READER.get())
                        .with(PropertyDispatch.initial(KeycardReaderBlock.PLACEMENT)
                                .select(Placement.LEFT, leftVariant)
                                .select(Placement.RIGHT, rightVariant))
                        .with(ROTATION_HORIZONTAL_FACING));
    }

    public void createRedstoneDevice(@NotNull BlockModelGenerators blockModels, Block block, TexturedModel.@NotNull Provider modelProvider) {
        MultiVariant offVariant = plainVariant(modelProvider.create(block, blockModels.modelOutput));
        ResourceLocation resourcelocation = TextureMapping.getBlockTexture(block, "_front_on");
        MultiVariant onVariant = plainVariant(modelProvider.get(block)
                .updateTextures(mapping -> mapping.put(TextureSlot.FRONT, resourcelocation))
                .createWithSuffix(block, "_on", blockModels.modelOutput));

        blockModels.blockStateOutput.accept(
                MultiVariantGenerator.dispatch(block)
                        .with(createBooleanModelDispatch(RedstoneDeviceBlock.POWERED, onVariant, offVariant))
                        .with(ROTATION_HORIZONTAL_FACING));
    }

    public void createSmallPoster(@NotNull BlockModelGenerators blockModels, @NotNull Block block) {
        blockModels.registerSimpleFlatItemModel(block.asItem());
        MultiVariant leftVariant = plainVariant(SMALL_POSTER_LEFT.create(block, TextureMapping.defaultTexture(block), blockModels.modelOutput));
        MultiVariant rightVariant = plainVariant(SMALL_POSTER_RIGHT.create(block, TextureMapping.defaultTexture(block), blockModels.modelOutput));

        blockModels.blockStateOutput.accept(
                MultiVariantGenerator.dispatch(block)
                        .with(PropertyDispatch.initial(SmallPosterBlock.PLACEMENT)
                                .select(Placement.LEFT, leftVariant)
                                .select(Placement.RIGHT, rightVariant))
                        .with(ROTATION_HORIZONTAL_FACING));
    }

    public void createPainting(@NotNull BlockModelGenerators blockModels, @NotNull Block block) {
        blockModels.registerSimpleFlatItemModel(block.asItem());
        MultiVariant sideVariant = plainVariant(PAINTING_SIDE.create(block, TextureMapping.defaultTexture(block), blockModels.modelOutput));
        MultiVariant topVariant = plainVariant(PAINTING_TOP.create(block, TextureMapping.defaultTexture(block), blockModels.modelOutput));
        MultiVariant bottomVariant = plainVariant(PAINTING_BOTTOM.create(block, TextureMapping.defaultTexture(block), blockModels.modelOutput));

        blockModels.blockStateOutput.accept(
                MultiVariantGenerator.dispatch(block)
                        .with(PropertyDispatch.initial(BlockStateProperties.ATTACH_FACE)
                                .select(AttachFace.WALL, sideVariant)
                                .select(AttachFace.CEILING, topVariant)
                                .select(AttachFace.FLOOR, bottomVariant))
                        .with(ROTATION_HORIZONTAL_FACING)
        );
    }

    public void createTrapdoorShaped(@NotNull BlockModelGenerators blockModels, Block block) {
        MultiVariant sideVariant = plainVariant(TRAPDOOR_SHAPED_SIDE.create(block, TextureMapping.defaultTexture(block), blockModels.modelOutput));
        MultiVariant topVariant = plainVariant(TRAPDOOR_SHAPED_TOP.create(block, TextureMapping.defaultTexture(block), blockModels.modelOutput));
        MultiVariant bottomVariant = plainVariant(TRAPDOOR_SHAPED_BOTTOM.create(block, TextureMapping.defaultTexture(block), blockModels.modelOutput));

        blockModels.blockStateOutput.accept(
                MultiVariantGenerator.dispatch(block)
                        .with(PropertyDispatch.initial(BlockStateProperties.ATTACH_FACE)
                                .select(AttachFace.WALL, sideVariant)
                                .select(AttachFace.CEILING, topVariant)
                                .select(AttachFace.FLOOR, bottomVariant))
                        .with(ROTATION_HORIZONTAL_FACING)
        );
    }
}
