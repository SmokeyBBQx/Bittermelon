package com.site21.bittermelon.common.content.items;

import com.site21.bittermelon.common.systems.component.temperature.HeatBehavior;
import com.site21.bittermelon.init.neoforge.BitterDataComponents;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static com.site21.bittermelon.init.neoforge.BitterDataComponents.HEAT_BEHAVIOR;

public class TestHeatedItem extends Item {
    public TestHeatedItem(Properties properties) {
        super(properties);
    }

    @Override
    public void inventoryTick(@NotNull ItemStack stack, @NotNull ServerLevel level, @NotNull Entity entity, @Nullable EquipmentSlot slot) {
        stack.getOrDefault(HEAT_BEHAVIOR, HeatBehavior.DEFAULT).inventoryTick(stack, level, entity, slot);
    }

    @Override
    public boolean onEntityItemUpdate(@NotNull ItemStack stack, @NotNull ItemEntity entity) {
        stack.getOrDefault(HEAT_BEHAVIOR, HeatBehavior.DEFAULT).onEntityItemUpdate(stack, entity, entity.level());
        return false;
    }
}
