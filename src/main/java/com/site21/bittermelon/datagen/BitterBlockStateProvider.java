package com.site21.bittermelon.datagen;

import com.site21.bittermelon.Bittermelon;
import com.site21.bittermelon.content.blocks.poster.SmallPosterBlock;
import net.minecraft.core.Direction;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.client.model.generators.VariantBlockStateBuilder;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

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
    }

    private void createSmallPosterBlockState(Block block, String path) {
        VariantBlockStateBuilder builder = getVariantBuilder(block);

        for (Direction direction : Direction.values()) {
            if (direction.getAxis().isHorizontal()) {
                int yRotation = switch (direction) {
                    case EAST -> 90;
                    case SOUTH -> 180;
                    case WEST -> 270;
                    default -> 0;
                };

                builder.partialState()
                        .with(SmallPosterBlock.FACING, direction)
                        .with(SmallPosterBlock.PLACEMENT, SmallPosterBlock.Placement.RIGHT)
                        .modelForState()
                        .modelFile(models().withExistingParent(path + "_right", modLoc("block/small_poster_right"))
                                .texture("texture", modLoc(path)))
                        .rotationY(yRotation)
                        .addModel();

                builder.partialState()
                        .with(SmallPosterBlock.FACING, direction)
                        .with(SmallPosterBlock.PLACEMENT, SmallPosterBlock.Placement.LEFT)
                        .modelForState()
                        .modelFile(models().withExistingParent(path + "_left", modLoc("block/small_poster_left"))
                                .texture("texture", modLoc(path)))
                        .rotationY(yRotation)
                        .addModel();
            }
        }
    }
}
