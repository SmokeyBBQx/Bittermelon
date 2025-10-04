package com.site21.bittermelon.datagen;

import com.site21.bittermelon.Bittermelon;
import com.site21.bittermelon.init.neoforge.BitterItems;
import net.minecraft.data.PackOutput;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.SpawnEggItem;
import net.neoforged.neoforge.client.model.generators.ItemModelProvider;
import net.neoforged.neoforge.common.DeferredSpawnEggItem;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.registries.DeferredHolder;

public class BitterItemModelProvider extends ItemModelProvider {
    public BitterItemModelProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, Bittermelon.MOD_ID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
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

        generateBlockItemModels();
    }

    private void generateBlockItemModels() {
        for (DeferredHolder<Item, ? extends Item> itemHolder : BitterItems.ITEMS.getEntries()) {
            Item item = itemHolder.get();
            if (item instanceof BlockItem) {
                String blockName = itemHolder.getId().getPath();

                if (existingFileHelper.exists(modLoc("block/" + blockName), net.minecraft.server.packs.PackType.CLIENT_RESOURCES, ".json", "models")) {
                    withExistingParent(blockName, modLoc("block/" + blockName));
                }
            } else if (item instanceof SpawnEggItem) {
                spawnEggItem(item);
            }
        }
    }
}
