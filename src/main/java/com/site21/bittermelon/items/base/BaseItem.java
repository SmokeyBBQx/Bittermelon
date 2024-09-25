package com.site21.bittermelon.items.base;

import com.site21.bittermelon.entities.miscellaneous.ThrownItemProjectile;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.damagesource.DamageSources;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class BaseItem extends Item {
    protected ItemSize itemSize;
    protected ItemWeight itemWeight;

    public BaseItem(Properties properties, ItemSize itemSize, ItemWeight itemWeight) {
        super(properties);
        this.itemSize = itemSize;
        this.itemWeight = itemWeight;
    }

    public ItemSize getItemSize() {
        return itemSize;
    }

    public ItemWeight getItemWeight() {
        return itemWeight;
    }

    public void projectileHitBlock(ItemStack stack, Level level, BlockPos pos) {
        ItemEntity itemEntity = new ItemEntity(level, pos.getX(), pos.getY(), pos.getZ(), stack);
        level.addFreshEntity(itemEntity);
    }

    public void projectileHitEntity(ItemStack stack, Entity entity, DamageSources damageSources, ThrownItemProjectile thrownItemProjectile, Entity owner) {
        float dmg = 0;
//        dmg += (float) item.getAttributeModifiers().modifiers().get(EquipmentSlot.MAINHAND.getIndex()).attribute(). TODO: Figure out how to get damage of item
        dmg += itemWeight.value - 1;
//        dmg *= item.getCount();
//        dmg /= item.getMaxStackSize() / 4f;
        entity.hurt(damageSources.thrown(thrownItemProjectile, owner), dmg);
    }

    @Override
    public void appendHoverText(@NotNull ItemStack stack, Item.@NotNull TooltipContext context, @NotNull List<Component> tooltipComponents, @NotNull TooltipFlag tooltipFlag) {
        super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
        tooltipComponents.add(Component.literal("⇲" + getItemSize().description + " ⚖" + getItemWeight().description).withStyle(ChatFormatting.GRAY));
    }

}
