package com.site21.bittermelon.datagen;

import com.site21.bittermelon.init.neoforge.BitterEntities;
import com.site21.bittermelon.init.neoforge.BitterItems;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.loot.EntityLootSubProvider;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.EnchantedCountIncreaseFunction;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;

import java.util.stream.Stream;

import static com.site21.bittermelon.init.neoforge.BitterEntities.*;

public class BitterEntityLootProvider extends EntityLootSubProvider {
    public BitterEntityLootProvider(HolderLookup.Provider registries) {
        super(FeatureFlags.DEFAULT_FLAGS, registries);
    }

    @Override
    protected Stream<EntityType<?>> getKnownEntityTypes() {
        return BitterEntities.ENTITY_TYPES.getEntries()
                .stream()
                .map(e -> (EntityType<?>) e.get());
    }

    @Override
    public void generate() {
        add(SCP_939.get(), LootTable.lootTable());
        add(CHICKEN.get(), LootTable.lootTable());
        add(SCP_650.get(), LootTable.lootTable());
        add(SCP_131.get(), LootTable.lootTable());
        add(SCP_1507.get(), LootTable.lootTable()
                .withPool(LootPool.lootPool()
                        .setRolls(ConstantValue.exactly(1.0f))
                        .add(LootItem.lootTableItem(BitterItems.PINK_PLASTIC_SCRAP)
                                .apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0f, 3.0f)))
                                .apply(EnchantedCountIncreaseFunction.lootingMultiplier(registries, UniformGenerator.between(0.0f, 1.0f)))
                        )
                )
                .withPool(LootPool.lootPool()
                        .setRolls(ConstantValue.exactly(1.0f))
                        .add(LootItem.lootTableItem(BitterItems.METAL_ROD)
                                .apply(SetItemCountFunction.setCount(UniformGenerator.between(0.0f, 2.0f)))
                        )
                )
        );
        add(SCP_548.get(), LootTable.lootTable());
        add(SEA_MONKEY.get(), LootTable.lootTable());
        add(SCP_718.get(), LootTable.lootTable());
        add(SCP_025_FR.get(), LootTable.lootTable());
    }
}
