package net.smokeybbq.bittermelon.entities;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.smokeybbq.bittermelon.items.base.BaseItem;
import org.jetbrains.annotations.NotNull;

import static net.smokeybbq.bittermelon.init.EntityInit.THROWN_ITEM_PROJECTILE;

public class ThrownItemProjectile extends ThrowableItemProjectile {
    private final float BASE_GRAVITY = 0.03F;
    public ThrownItemProjectile(EntityType<? extends ThrownItemProjectile> pEntityType, Level level) {
        super(pEntityType, level);
    }

    public ThrownItemProjectile(Level level, LivingEntity player, ItemStack itemStack) {
        super(THROWN_ITEM_PROJECTILE.get(), level);
        this.setItem(itemStack);
    }

    public ThrownItemProjectile(Level pLevel, double pX, double pY, double pZ) {
        super(THROWN_ITEM_PROJECTILE.get(), pX, pY, pZ, pLevel);
    }

    @Override
    protected @NotNull Item getDefaultItem() {
        Item item = this.getItemRaw().getItem();
        return item;
    }

    @Override
    protected void onHitBlock(BlockHitResult result) {
        super.onHitBlock(result);

        if (!this.level().isClientSide) {
            if (this.getItem().getItem() instanceof BaseItem item) {
                item.projectileHitBlock(this.getItem(), this.level(), result.getBlockPos());
            } else {
                spawnAtLocation(this.getItem());
            }
            this.level().broadcastEntityEvent(this, (byte) 3);
            this.discard();
        }
    }


        @Override
        protected void onHitEntity (@NotNull EntityHitResult result){
            super.onHitEntity(result);
            Entity entity = result.getEntity();

            if (!this.level().isClientSide) {
                if (this.getItem().getItem() instanceof BaseItem item) {
                    item.projectileHitEntity(this.getItem(), entity, this.damageSources(), this, this.getOwner());
                } else {
                    float dmg = 1;
                    dmg += (float) this.getItem().getAttributeModifiers(EquipmentSlot.MAINHAND).get(Attributes.ATTACK_DAMAGE).stream().mapToDouble(AttributeModifier::getAmount).sum();
                    dmg *= this.getItem().getCount();
                    dmg /= this.getItem().getMaxStackSize() / 4f;
                    entity.hurt(this.damageSources().thrown(this, this.getOwner()), dmg);
                }
            }
        }

        @Override
        protected float getGravity() {
            if (this.getItem().getItem() instanceof BaseItem item) {
                return BASE_GRAVITY + (float) item.getItemWeight().value / 100;
            }
            return BASE_GRAVITY;
        }
    }