package net.smokeybbq.bittermelon.items.base;

import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.damagesource.DamageSources;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.smokeybbq.bittermelon.entities.ThrownItemProjectile;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public abstract class BaseItem extends Item {
    protected ItemSize itemSize;
    protected ItemWeight itemWeight;

    public BaseItem(Properties pProperties) {
        super(pProperties);
    }

    public ItemSize getItemSize() {
        return itemSize;
    }

    public ItemWeight getItemWeight() {
        return itemWeight;
    }

    // Override and add custom behavior
    public void projectileHitBlock(ItemStack itemStack, Level level, BlockPos pos) {
        ItemEntity itemEntity = new ItemEntity(level, pos.getX(), pos.getY(), pos.getZ(), itemStack);
        level.addFreshEntity(itemEntity);
    }

    public void projectileHitEntity(ItemStack item, Entity entity, DamageSources damageSources, ThrownItemProjectile thrownItemProjectile, Entity owner) {
        float dmg = 0;
        dmg += (float) item.getAttributeModifiers(EquipmentSlot.MAINHAND).get(Attributes.ATTACK_DAMAGE).stream().mapToDouble(AttributeModifier::getAmount).sum();
        dmg += itemWeight.value - 1;
//        dmg *= item.getCount();
//        dmg /= item.getMaxStackSize() / 4f;
        entity.hurt(damageSources.thrown(thrownItemProjectile, owner), dmg);
    }

    @Override
    public void appendHoverText(@NotNull ItemStack stack, Level level, @NotNull List<Component> toolTipComponents, @NotNull TooltipFlag isAdvanced) {
        super.appendHoverText(stack, level, toolTipComponents, isAdvanced);
        toolTipComponents.add(Component.literal("⇲" + getItemSize().description + " ⚖" + getItemWeight().description).withStyle(ChatFormatting.GRAY));
    }

    public static void saveTag(ItemStack itemStack, String key, CompoundTag tagToSave) {
        CompoundTag tag = itemStack.getOrCreateTag();
        tag.put(key, tagToSave);
    }

    public static CompoundTag getTag(ItemStack itemStack, String key) {
        CompoundTag tag = itemStack.getTag();
        if (tag != null && tag.contains(key, 10)) {
            return tag.getCompound(key);
        }
        return new CompoundTag();
    }
}
