package com.site21.bittermelon.datagen;

import com.site21.bittermelon.Bittermelon;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

import static com.site21.bittermelon.init.neoforge.BitterBlocks.KEYCARD_READER_SECURE_DOOR;
import static com.site21.bittermelon.init.neoforge.BitterBlocks.SECURE_DOOR;

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
    }
}
