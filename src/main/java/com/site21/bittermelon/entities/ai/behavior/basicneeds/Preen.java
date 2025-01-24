package com.site21.bittermelon.entities.ai.behavior.basicneeds;

import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.jetbrains.annotations.NotNull;

public class Preen<E extends Mob & HasBasicNeeds> extends Groom<E> {

    public Preen(int delayTicks) {
        super(delayTicks);
    }

    @Override
    protected void doDelayedAction(@NotNull E entity) {
        super.doDelayedAction(entity);

        if (entity.getRandom().nextFloat() > 0.75) {
            ItemStack feather = new ItemStack(Items.FEATHER);
            ItemEntity itemEntity = new ItemEntity(entity.level(), entity.getX(), entity.getY(), entity.getZ(), feather);
            entity.level().addFreshEntity(itemEntity);
        }
    }
}
