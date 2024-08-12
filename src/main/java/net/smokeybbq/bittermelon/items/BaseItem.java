package net.smokeybbq.bittermelon.items;

import net.minecraft.core.BlockPos;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageSources;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.smokeybbq.bittermelon.entities.ThrownItemProjectile;

public class BaseItem extends Item {
    public BaseItem(Properties pProperties) {
        super(pProperties);
    }

    // Override and add custom behavior
    public void projectileHitBlock(ItemStack itemStack, Level level, BlockPos pos) {
        ItemEntity itemEntity = new ItemEntity(level, pos.getX(), pos.getY(), pos.getZ(), itemStack);
        level.addFreshEntity(itemEntity);
    }


    public void projectileHitEntity(ItemStack item, Entity entity, DamageSources damageSources, ThrownItemProjectile thrownItemProjectile, Entity owner) {
        float dmg = 1;
        dmg += (float) item.getAttributeModifiers(EquipmentSlot.MAINHAND).get(Attributes.ATTACK_DAMAGE).stream().mapToDouble(AttributeModifier::getAmount).sum();
        dmg *= item.getCount();
        dmg /= item.getMaxStackSize() / 4f;
        entity.hurt(damageSources.thrown(thrownItemProjectile, owner), dmg);
    }
}
