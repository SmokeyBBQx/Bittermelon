package com.site21.bittermelon.common.content.mobeffects.amnesia;

import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntList;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AmnesiaEffect extends MobEffect {
    public AmnesiaEffect() {
        super(MobEffectCategory.HARMFUL, 0xA000000);
    }

    @Override
    public boolean shouldApplyEffectTickThisTick(int tickCount, int amplification) {
        return tickCount % Math.max(1200, (6000 - 1200 * amplification)) == 0;
    }

    @Override
    public boolean applyEffectTick(ServerLevel serverLevel, LivingEntity mob, int amplification) {
        if (mob instanceof ServerPlayer player) {
            shuffleInventory(player);
        }
        return true;
    }

    private static void shuffleInventory(ServerPlayer player) {
        Inventory inventory = player.getInventory();
        IntList slots = new IntArrayList();
        List<ItemStack> stacks = new ArrayList<>();
        for (int i = 0; i < Inventory.INVENTORY_SIZE; i++) {
            ItemStack stack = inventory.getItem(i);
            if (!stack.isEmpty()) {
                slots.add(i);
                stacks.add(stack);
            }
        }

        if (stacks.size() < 2) {
            return;
        }

        for (int i = stacks.size() - 1; i > 0; i--) {
            Collections.swap(stacks, i, player.level().getRandom().nextInt(i + 1));
        }

        for (int i = 0; i < slots.size(); i++) {
            int slot = slots.getInt(i);
            inventory.setItem(slot, stacks.get(i));
        }
    }
}
