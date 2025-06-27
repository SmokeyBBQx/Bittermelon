package com.site21.bittermelon.datagen;

import com.site21.bittermelon.Bittermelon;
import com.site21.bittermelon.content.blocks.poster.SmallPosterBlock;
import com.site21.bittermelon.content.blocks.powergrid.distributionboard.DistributionBoardBlock;
import com.site21.bittermelon.content.blocks.scp.scp151.SCP151Block;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.client.model.generators.ModelFile;
import net.neoforged.neoforge.client.model.generators.VariantBlockStateBuilder;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.registries.DeferredHolder;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

import static com.site21.bittermelon.init.neoforge.BitterBlocks.*;

public class BitterBlockStateProvider extends net.neoforged.neoforge.client.model.generators.BlockStateProvider {
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
    }

    private void createSmallPosterBlockState(Block block, String path) {
        VariantBlockStateBuilder builder = getVariantBuilder(block);

        for (Direction direction : Direction.values()) {
            if (direction.getAxis().isHorizontal()) {
                builder.partialState()
                        .with(SmallPosterBlock.FACING, direction)
                        .with(SmallPosterBlock.PLACEMENT, SmallPosterBlock.Placement.RIGHT)
                        .modelForState()
                        .modelFile(models().withExistingParent(path + "_right", modLoc("block/small_poster_right"))
                                .texture("texture", modLoc(path)))
                        .rotationY(getYRotation(direction))
                        .addModel();

                builder.partialState()
                        .with(SmallPosterBlock.FACING, direction)
                        .with(SmallPosterBlock.PLACEMENT, SmallPosterBlock.Placement.LEFT)
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

    @Contract(pure = true)
    private int getYRotation(@NotNull Direction direction) {
        return switch (direction) {
            case SOUTH -> 180;
            case WEST -> 270;
            case EAST -> 90;
            default -> 0;
        };
    }
}
