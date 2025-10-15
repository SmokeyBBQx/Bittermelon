package com.site21.bittermelon.content.items.base;

import com.site21.bittermelon.content.entities.ThrownItemProjectile;
import net.minecraft.core.BlockPos;
import net.minecraft.world.damagesource.DamageSources;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

@Deprecated
public class BaseItem extends Item {
    protected final ItemWeight itemWeight;
    protected final int width;
    protected final int height;
    // TODO: Add temperature

    public BaseItem(Properties properties, int width, int height, ItemWeight itemWeight) {
        super(properties);
        this.width = width;
        this.height = height;
        this.itemWeight = itemWeight;
    }


    public void projectileHitBlock(ItemStack stack, Level level, @NotNull BlockPos pos) {
        ItemEntity itemEntity = new ItemEntity(level, pos.getX(), pos.getY(), pos.getZ(), stack);
        level.addFreshEntity(itemEntity);
    }

    public void projectileHitEntity(ItemStack stack, @NotNull Entity entity, @NotNull DamageSources damageSources, ThrownItemProjectile thrownItemProjectile, Entity owner, @NotNull Vec3 velocity) {
        float dmg = 0;
//        dmg += (float) item.getAttributeModifiers().modifiers().get(EquipmentSlot.MAINHAND.getIndex()).attribute(). TODO: Figure out how to get damage of item
        dmg += itemWeight.value;
//        dmg *= item.getCount();
//        dmg /= item.getMaxStackSize() / 4f;
        entity.hurt(damageSources.thrown(thrownItemProjectile, owner), (float) (Math.pow(dmg, velocity.length() * 2)));
    }

//    @Override
//    public void appendHoverText(@NotNull ItemStack stack, Item.@NotNull TooltipContext context, @NotNull List<Component> tooltipComponents, @NotNull TooltipFlag tooltipFlag) {
//        super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
//        tooltipComponents.add(Component.literal("⇲" + getItemSize().description + " ⚖" + getItemWeight().description).withStyle(ChatFormatting.GRAY));
//    }

}
