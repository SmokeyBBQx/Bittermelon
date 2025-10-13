package com.site21.bittermelon.datagen;

import com.site21.bittermelon.Bittermelon;
import com.site21.bittermelon.content.blocks.poster.SmallPosterBlock;
import com.site21.bittermelon.content.blocks.powergrid.distributionboard.DistributionBoardBlock;
import com.site21.bittermelon.content.blocks.properties.Placement;
import com.site21.bittermelon.content.blocks.scp.scp151.SCP151Block;
import com.site21.bittermelon.content.blocks.stickynote.StickyNoteBlock;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.properties.AttachFace;
import net.neoforged.neoforge.client.model.generators.BlockStateProvider;
import net.neoforged.neoforge.client.model.generators.ConfiguredModel;
import net.neoforged.neoforge.client.model.generators.ModelFile;
import net.neoforged.neoforge.client.model.generators.VariantBlockStateBuilder;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.registries.DeferredHolder;
import org.checkerframework.checker.units.qual.C;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

import static com.site21.bittermelon.init.neoforge.BitterBlocks.*;

public class BitterBlockStateProvider extends BlockStateProvider {
    public BitterBlockStateProvider(PackOutput output, ExistingFileHelper exFileHelper) {
        super(output, Bittermelon.MOD_ID, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        doorBlockWithRenderType(SECURE_DOOR.get(),
                ResourceLocation.fromNamespaceAndPath(Bittermelon.MOD_ID, "block/secure_door_bottom"),
                ResourceLocation.fromNamespaceAndPath(Bittermelon.MOD_ID, "block/secure_door_top"),
                "minecraft:cutout");

        doorBlockWithRenderType(KEYCARD_READER_SECURE_DOOR.get(),
                ResourceLocation.fromNamespaceAndPath(Bittermelon.MOD_ID, "block/secure_door_bottom"),
                ResourceLocation.fromNamespaceAndPath(Bittermelon.MOD_ID, "block/keycard_reader_secure_door_top"),
                "minecraft:cutout");

        createSmallPosterBlockState(YELLOW_INSPECTION_POSTER.get(), "block/yellow_inspection_poster");
        createCustomTrapdoorBlockState(DISTRIBUTION_BOARD.get(), "block/distribution_board");
        createPaintingBlockState(SCP151.get(), "block/scp151");
        createStickyNoteBlock(STICKY_NOTE.get());
        createKeycardReader(KEYCARD_READER.get());
    }

    private void createSmallPosterBlockState(Block block, String path) {
        VariantBlockStateBuilder builder = getVariantBuilder(block);

        for (Direction direction : Direction.values()) {
            if (direction.getAxis().isHorizontal()) {
                builder.partialState()
                        .with(SmallPosterBlock.FACING, direction)
                        .with(SmallPosterBlock.PLACEMENT, Placement.RIGHT)
                        .modelForState()
                        .modelFile(models().withExistingParent(path + "_right", modLoc("block/small_poster_right"))
                                .texture("texture", modLoc(path)))
                        .rotationY(getYRotation(direction))
                        .addModel();

                builder.partialState()
                        .with(SmallPosterBlock.FACING, direction)
                        .with(SmallPosterBlock.PLACEMENT, Placement.LEFT)
                        .modelForState()
                        .modelFile(models().withExistingParent(path + "_left", modLoc("block/small_poster_left"))
                                .texture("texture", modLoc(path)))
                        .rotationY(getYRotation(direction))
                        .addModel();
            }
        }
    }

    private void createCustomTrapdoorBlockState(Block block, String path) {
        VariantBlockStateBuilder variantBuilder = getVariantBuilder(block);

        for (Direction direction : Direction.values()) {
            if (direction.getAxis().isHorizontal()) {
                ModelFile topModel = models().withExistingParent(path + "_top", mcLoc("block/template_orientable_trapdoor_top"))
                        .texture("texture", path);
                ModelFile bottomModel = models().withExistingParent(path + "_bottom", mcLoc("block/template_orientable_trapdoor_bottom"))
                        .texture("texture", path);
                ModelFile sideModel = models().withExistingParent(path + "_side", mcLoc("block/template_orientable_trapdoor_open"))
                        .texture("texture", path);

                variantBuilder.partialState()
                        .with(DistributionBoardBlock.TYPE, DistributionBoardBlock.Type.TOP)
                        .with(DistributionBoardBlock.FACING, direction)
                        .modelForState()
                        .modelFile(topModel)
                        .rotationY(getYRotation(direction))
                        .addModel();

                variantBuilder.partialState()
                        .with(DistributionBoardBlock.TYPE, DistributionBoardBlock.Type.BOTTOM)
                        .with(DistributionBoardBlock.FACING, direction)
                        .modelForState()
                        .modelFile(bottomModel)
                        .rotationY(getYRotation(direction))
                        .addModel();

                variantBuilder.partialState()
                        .with(DistributionBoardBlock.TYPE, DistributionBoardBlock.Type.SIDE)
                        .with(DistributionBoardBlock.FACING, direction)
                        .modelForState()
                        .modelFile(sideModel)
                        .rotationY(getYRotation(direction))
                        .addModel();
            }
        }
    }

    private void createPaintingBlockState(Block block, String path) {
        VariantBlockStateBuilder variantBuilder = getVariantBuilder(block);

        for (Direction direction : Direction.values()) {
            if (direction.getAxis().isHorizontal()) {
                ModelFile topModel = models().withExistingParent(path + "_top", modLoc("block/painting_top"))
                        .texture("texture", path);
                ModelFile bottomModel = models().withExistingParent(path + "_bottom", modLoc("block/painting_bottom"))
                        .texture("texture", path);
                ModelFile sideModel = models().withExistingParent(path, modLoc("block/painting_side"))
                        .texture("texture", path);

                variantBuilder.partialState()
                        .with(SCP151Block.TYPE, SCP151Block.Type.TOP)
                        .with(SCP151Block.FACING, direction)
                        .modelForState()
                        .modelFile(topModel)
                        .rotationY(getYRotation(direction))
                        .addModel();

                variantBuilder.partialState()
                        .with(SCP151Block.TYPE, SCP151Block.Type.BOTTOM)
                        .with(SCP151Block.FACING, direction)
                        .modelForState()
                        .modelFile(bottomModel)
                        .rotationY(getYRotation(direction))
                        .addModel();

                variantBuilder.partialState()
                        .with(SCP151Block.TYPE, SCP151Block.Type.SIDE)
                        .with(SCP151Block.FACING, direction)
                        .modelForState()
                        .modelFile(sideModel)
                        .rotationY(getYRotation(direction))
                        .addModel();
            }
        }
    }

    private void createStickyNoteBlock(@NotNull Block block) {
        String blockName = block.getName().getString();

        ModelFile[] models = new ModelFile[16];
        for (int i = 0; i < 16; i++) {
            String binaryString = String.format("%04d",
                    Integer.parseInt(Integer.toBinaryString(i)));
            models[i] = createStickyNoteModel(blockName, binaryString);
        }

        getVariantBuilder(block).forAllStates(state -> {
            AttachFace face = state.getValue(StickyNoteBlock.FACE);
            Direction facing = state.getValue(StickyNoteBlock.FACING);
            boolean topLeft = state.getValue(StickyNoteBlock.TOP_LEFT);
            boolean topRight = state.getValue(StickyNoteBlock.TOP_RIGHT);
            boolean bottomLeft = state.getValue(StickyNoteBlock.BOTTOM_LEFT);
            boolean bottomRight = state.getValue(StickyNoteBlock.BOTTOM_RIGHT);

            int modelIndex = (topLeft ? 8 : 0) + (topRight ? 4 : 0) +
                    (bottomLeft ? 2 : 0) + (bottomRight ? 1 : 0);

            int xRot = 0;
            int yRot = switch (face) {
                case CEILING -> {
                    xRot = 90;
                    yield getYRotationInverted(facing);
                }
                case FLOOR -> {
                    xRot = 270;
                    yield getYRotationInverted(facing);
                }
                case WALL -> getYRotationInverted(facing);
            };

            return ConfiguredModel.builder()
                    .modelFile(models[modelIndex])
                    .rotationX(xRot)
                    .rotationY(yRot)
                    .build();
        });
    }

    private @NotNull ModelFile createStickyNoteModel(String blockName, String binaryPattern) {
        ResourceLocation texture = modLoc("block/sticky_note_" + binaryPattern);

        return models().withExistingParent(blockName + "_" + binaryPattern, modLoc("block/sticky_note"))
                .texture("texture", texture)
                .renderType("cutout");
    }

    private void createKeycardReader(Block block) {
        VariantBlockStateBuilder builder = getVariantBuilder(block);

        for (Direction direction : Direction.values()) {
            if (direction.getAxis().isHorizontal()) {
                builder.partialState()
                        .with(SmallPosterBlock.FACING, direction)
                        .with(SmallPosterBlock.PLACEMENT, Placement.RIGHT)
                        .modelForState()
                        .modelFile(models().getExistingFile(modLoc("block/keycard_reader_right")))
                        .rotationY(getYRotation(direction))
                        .addModel();

                builder.partialState()
                        .with(SmallPosterBlock.FACING, direction)
                        .with(SmallPosterBlock.PLACEMENT, Placement.LEFT)
                        .modelForState()
                        .modelFile(models().getExistingFile(modLoc("block/keycard_reader_left")))
                        .rotationY(getYRotation(direction))
                        .addModel();
            }
        }
    }

    @Contract(pure = true)
    private int getYRotation(@NotNull Direction direction) {
        return switch (direction) {
            case SOUTH -> 180;
            case WEST -> 270;
            case EAST -> 90;
            default -> 0;
        };
    }

    @Contract(pure = true)
    private int getYRotationInverted(@NotNull Direction facing) {
        return switch (facing) {
            case EAST -> 90;
            case SOUTH -> 180;
            case WEST -> 270;
            default -> 0;
        };
    }
}
