package net.smokeybbq.bittermelon.entities;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
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
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraftforge.network.NetworkHooks;
import net.smokeybbq.bittermelon.items.BaseItem;
import org.jetbrains.annotations.NotNull;
import net.smokeybbq.bittermelon.util.ModLogger;

import static net.smokeybbq.bittermelon.init.EntityInit.THROWN_ITEM_PROJECTILE;

public class ThrownItemProjectile extends ThrowableItemProjectile {
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
    }