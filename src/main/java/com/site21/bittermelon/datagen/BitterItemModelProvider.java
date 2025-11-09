package com.site21.bittermelon.datagen;

import com.site21.bittermelon.Bittermelon;
import com.site21.bittermelon.init.neoforge.BitterBlocks;
import com.site21.bittermelon.init.neoforge.BitterItems;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.SpawnEggItem;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.client.model.generators.ItemModelProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.registries.DeferredHolder;

import static com.site21.bittermelon.init.neoforge.BitterItems.*;

public class BitterItemModelProvider extends ItemModelProvider {
    public BitterItemModelProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, Bittermelon.MOD_ID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        // Items
        basicItem(BitterItems.BITTERMELON.get());
        basicItem(BitterItems.TASER_CARTRIDGE.get());
        basicItem(BitterItems.SCP_018.get());
        handheldItem(BitterItems.SYRINGE.get());
        basicItem(BitterItems.FORTUNE_COOKIE.get());
        basicItem(BitterItems.CRACKED_FORTUNE_COOKIE.get());
        basicItem(BitterItems.SCP_377_1.get());
        basicItem(BitterItems.WIRE.get());
        handheldItem(BitterItems.HANDHELD_SYSTEM_INTERFACE.get());
        handheldItem(BitterItems.SCREWDRIVER.get());
        basicItem(BitterItems.LARGE_SLIDING_DOOR.get());
        basicItem(BitterItems.SECURE_DOOR.get());
        basicItem(BitterItems.SLIDING_DOOR.get());
        basicItem(BitterItems.STICKY_NOTE.get());
        basicItem(BitterItems.PEN.get());
        basicItem(BitterItems.WIRE_CUTTERS.get());
        basicItem(BitterItems.SCP_151.get());
        basicItem(YELLOW_INSPECTION_POSTER.get());
        basicItem(SCP_2398.get());
        basicItem(SCP_109.get());

        // Block Items
        generateBlockItemModels();

        // Special Cases
        cageLampItem(BitterBlocks.EMERGENCY_EXIT_LAMP.get());
        cageLampItem(BitterBlocks.CAGE_LAMP.get());
        cageLampItem(BitterBlocks.RED_CAGE_LAMP.get());
        cageLampItem(BitterBlocks.BLUE_CAGE_LAMP.get());
        cageLampItem(BitterBlocks.GREEN_CAGE_LAMP.get());
        cageLampItem(BitterBlocks.ORANGE_CAGE_LAMP.get());
        cageLampItem(BitterBlocks.YELLOW_CAGE_LAMP.get());
        cageLampItem(BitterBlocks.PURPLE_CAGE_LAMP.get());
        cageLampItem(BitterBlocks.LIME_CAGE_LAMP.get());
        cageLampItem(BitterBlocks.PINK_CAGE_LAMP.get());
        cageLampItem(BitterBlocks.MAGENTA_CAGE_LAMP.get());
        cageLampItem(BitterBlocks.CYAN_CAGE_LAMP.get());
        cageLampItem(BitterBlocks.LIME_CAGE_LAMP.get());
        cageLampItem(BitterBlocks.LIGHT_BLUE_CAGE_LAMP.get());
        simpleBlockItem(BitterBlocks.KEYCARD_READER.get(), "_right");
        simpleBlockItem(BitterBlocks.REDSTONE_DEVICE.get(), "_off");
        simpleBlockItem(BitterBlocks.DISTRIBUTION_BOARD.get(), "_bottom");
        simpleBlockItem(BitterBlocks.INTERCOM.get());
        simpleBlockItem(BitterBlocks.CONTAINMENT_PANEL.get());
    }

    private void generateBlockItemModels() {
        for (DeferredHolder<Item, ? extends Item> itemHolder : BitterItems.ITEMS.getEntries()) {
            Item item = itemHolder.get();
            if (item instanceof BlockItem blockItem) {
                ResourceLocation resourceLocation = BuiltInRegistries.BLOCK.getKey(blockItem.getBlock());
                if (existingFileHelper.exists(resourceLocation, PackType.CLIENT_RESOURCES)) {
                    simpleBlockItem(resourceLocation);
                }
            } else if (item instanceof SpawnEggItem) {
                spawnEggItem(item);
            }
        }
    }

    public void simpleBlockItem(Block block, String suffix) {
        ResourceLocation location = BuiltInRegistries.BLOCK.getKey(block);
        withExistingParent(location.toString(), ResourceLocation.fromNamespaceAndPath(Bittermelon.MOD_ID, "block/" + location.getPath() + suffix));
    }

    public void cageLampItem(Block block) {
        simpleBlockItem(block, "_bottom_on");
    }
}
