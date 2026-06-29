package com.site21.bittermelon.common.systems.component;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.site21.bittermelon.util.LocalMessageUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

import static com.site21.bittermelon.common.content.items.substance.SubstanceItemUtil.consumeSubstances;
import static com.site21.bittermelon.init.neoforge.BitterDataComponents.*;
import static com.site21.bittermelon.init.neoforge.BitterItemTags.LIGHTER;
import static com.site21.bittermelon.init.neoforge.BitterItems.CIGARETTE_BUTT;
import static net.minecraft.sounds.SoundEvents.FIRE_AMBIENT;

public record Smokable(Holder<Item> buttItem, int smokeDuration, Holder<SoundEvent> smokeSound) {
    private static final int SMOKE_TICK_INTERVAL = 200;

    public static final Codec<Smokable> CODEC;
    public static final Smokable DEFAULT = new Smokable(CIGARETTE_BUTT, 30, Holder.direct(FIRE_AMBIENT));

    public @NotNull InteractionResult use(@NotNull Level level, @NotNull Player player, @NotNull InteractionHand hand) {
        if (level.isClientSide()) return InteractionResult.PASS;

        // Determine the other hand
        InteractionHand otherHand = player.getUsedItemHand() == InteractionHand.MAIN_HAND
                ? InteractionHand.OFF_HAND
                : InteractionHand.MAIN_HAND;

        ItemStack smokableItem = player.getItemInHand(hand);
        ItemStack otherItem = player.getItemInHand(otherHand);

        if (!smokableItem.getOrDefault(LIT, false)) {
            return handleLightingOrSwallowing(level, player, smokableItem, otherItem);
        }

        return ItemUtils.startUsingInstantly(level, player, hand);
    }

    private @NotNull InteractionResult handleLightingOrSwallowing(Level level, Player player, ItemStack stack, @NotNull ItemStack otherStack) {
        // Try to light the smokable item
        if (otherStack.is(LIGHTER) || otherStack.is(Items.FLINT_AND_STEEL)) {
            playLightingSound(level, player.getOnPos());
            stack.set(LIT, true);

            if (otherStack.isStackable()) {
                otherStack.consume(1, player);
            } else {
                otherStack.hurtAndBreak(1, player, player.getEquipmentSlotForItem(otherStack));
            }
        } else {
            // Swallow the smokable item
            sendSwallowMessage(player, stack);
            playSwallowSound(level, player.getOnPos());
            stack.consume(1, player);
        }

        return InteractionResult.SUCCESS;
    }

    public @NotNull InteractionResult useOn(@NotNull UseOnContext context) {
        Level level = context.getLevel();
        if (level.isClientSide()) return InteractionResult.PASS;

        Player player = context.getPlayer();
        ItemStack stack = context.getItemInHand();
        BlockPos pos = context.getClickedPos().relative(context.getClickedFace());

        if (player == null || !player.isShiftKeyDown()) return InteractionResult.PASS;

        stack.consume(1, player);
        ItemEntity smokableButt = new ItemEntity(level, pos.getX(), pos.getY(), pos.getZ(), getButtItem(stack));
        playExtinguishSound(level, player.getOnPos());
        addExtinguishParticles(level, pos);
        level.addFreshEntity(smokableButt);

        return InteractionResult.SUCCESS;
    }

    public @NotNull ItemStack finishUsingItem(@NotNull ItemStack stack, @NotNull Level level, @NotNull LivingEntity entity) {
        if (level.isClientSide() || !stack.getOrDefault(LIT, false)) return stack;

        playSmokeSound(level, entity.getOnPos(), stack);
        addSmokeParticles(level, entity);
        consumeSubstances(stack, 1, entity);

        if (getTotalAmount(stack) < 0.1f) {
            return getButtItem(stack);
        }

        return stack;
    }

    public boolean releaseUsing(@NotNull ItemStack stack, @NotNull Level level, @NotNull LivingEntity entity, int timeLeft) {
        if (timeLeft > getUseDuration(stack, entity) / 4) return false;
        if (level.isClientSide() || !stack.getOrDefault(LIT, false)) return false;

        playSmokeSound(level, entity.getOnPos(), stack);
        addSmokeParticles(level, entity);
        consumeSubstances(stack, ((getUseDuration(stack, entity) - timeLeft) / getUseDuration(stack, entity)), entity);

        if (getTotalAmount(stack) < 0.1f && entity instanceof ServerPlayer player) {
            int slot = player.getInventory().findSlotMatchingItem(stack);
            stack.consume(1, entity);
            player.getInventory().add(slot, getButtItem(stack));
        }

        return true;
    }

    public void inventoryTick(@NotNull ItemStack stack, @NotNull ServerLevel level, @NotNull Entity entity, @Nullable EquipmentSlot slot) {
        if (level.isClientSide()) return;
        if (slot != EquipmentSlot.HEAD || !stack.getOrDefault(LIT, false)) return;

        if (level.getGameTime() % SMOKE_TICK_INTERVAL == 0) {
            playSmokeSound(level, entity.getOnPos(), stack);
            addSmokeParticles(level, entity);
            consumeSubstances(stack, 1, (LivingEntity) entity);

            if (getTotalAmount(stack) < 0.1f) {
                ((LivingEntity) entity).setItemSlot(EquipmentSlot.HEAD, ItemStack.EMPTY);

                if (entity instanceof Player player) {
                    player.addItem(getButtItem(stack));
                }
            }
        }
    }

    public boolean onEntityItemUpdate(@NotNull ItemStack stack, @NotNull ItemEntity entity) {
        if (entity.isInWaterOrRain()) {
            entity.setItem(getButtItem(stack));
            playExtinguishSound(entity.level(), entity.getOnPos());
            return true;
        }
        return false;
    }

    public int getUseDuration(@NotNull ItemStack stack, @NotNull LivingEntity entity) {
        return stack.getOrDefault(SMOKABLE, Smokable.DEFAULT).smokeDuration();
    }

    public @NotNull ItemUseAnimation getUseAnimation(@NotNull ItemStack stack) {
        return ItemUseAnimation.TOOT_HORN;
    }

    private void sendSwallowMessage(@NotNull Player player, @NotNull ItemStack smokableItem) {
        LocalMessageUtil.sendEmoteMessage(player.level(), player, 5, "swallows " + smokableItem.getHoverName().getString() + ".");
    }

    private ItemStack getButtItem(@NotNull ItemStack stack) {
        Smokable smokable = stack.get(SMOKABLE);
        return smokable != null ? smokable.buttItem().value().getDefaultInstance() : ItemStack.EMPTY;
    }

    private void playLightingSound(@NotNull Level level, @NotNull BlockPos pos) {
        level.playSound(null, pos, SoundEvents.FLINTANDSTEEL_USE,
                SoundSource.PLAYERS, 0.5F, level.getRandom().nextFloat() * 0.1F + 0.9F);
    }

    private void playSwallowSound(@NotNull Level level, @NotNull BlockPos pos) {
        level.playSound(null, pos, SoundEvents.PLAYER_BURP,
                SoundSource.PLAYERS, 0.5F, level.getRandom().nextFloat() * 0.1F + 0.9F);
    }

    public void playExtinguishSound(@NotNull Level level, @NotNull BlockPos pos) {
        level.playSound(
                null,
                pos,
                SoundEvents.CANDLE_EXTINGUISH,
                SoundSource.PLAYERS,
                0.5f,
                level.getRandom().nextFloat() * 0.1F + 0.9f
        );
    }

    public void playSmokeSound(@NotNull Level level, @NotNull BlockPos pos, @NotNull ItemStack stack) {
        level.playSound(
                null,
                pos,
                stack.getOrDefault(SMOKABLE, Smokable.DEFAULT).smokeSound().value(),
                SoundSource.PLAYERS,
                0.5f,
                2
        );
    }

    private void addSmokeParticles(@NotNull Level level, @NotNull Entity entity) {
        if (level instanceof ServerLevel serverLevel && entity instanceof Player player) {
            float yRot = player.getYRot();
            double offsetX = -Math.sin(Math.toRadians(yRot)) * 0.3;
            double offsetZ = Math.cos(Math.toRadians(yRot)) * 0.3;

            serverLevel.sendParticles(
                    ParticleTypes.CAMPFIRE_COSY_SMOKE,
                    entity.getX() + offsetX,
                    entity.getY() + 1.6,
                    entity.getZ() + offsetZ,
                    1, 0.1, 0.1, 0.1, 0.01
            );
        }
    }

    private void addExtinguishParticles(Level level, BlockPos pos) {
        if (level instanceof ServerLevel serverLevel) {
            serverLevel.sendParticles(ParticleTypes.FLAME,
                    pos.getX(), pos.getY(), pos.getZ(), 1, 0, 0, 0, 0.01);
            serverLevel.sendParticles(ParticleTypes.SMOKE,
                    pos.getX(), pos.getY(), pos.getZ(), 1, 0, 0, 0, 0.01);
        }
    }

    public float getTotalAmount(@NotNull ItemStack stack) {
        return stack.getOrDefault(SUBSTANCE_CONTENTS, SubstanceContents.EMPTY).getTotalAmount();
    }

    @Override
    public int hashCode() {
        return Objects.hash(buttItem, smokeDuration, smokeSound);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        } else {
            return obj instanceof Smokable(Holder<Item> buttItem1, int smokeDuration1, Holder<SoundEvent> smokeSound1) &&
                    this.buttItem.equals(buttItem1) &&
                    this.smokeDuration == smokeDuration1 &&
                    Objects.equals(this.smokeSound, smokeSound1);
        }
    }

    static {
        CODEC = RecordCodecBuilder.create(instance -> instance.group(
                Item.CODEC.fieldOf("buttItem").forGetter(Smokable::buttItem),
                Codec.INT.fieldOf("smokeDuration").forGetter(Smokable::smokeDuration),
                SoundEvent.CODEC.fieldOf("smokeSound").forGetter(Smokable::smokeSound)
        ).apply(instance, Smokable::new));
    }
}
