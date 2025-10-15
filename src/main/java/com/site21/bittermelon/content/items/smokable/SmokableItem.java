package com.site21.bittermelon.content.items.smokable;

import com.site21.bittermelon.systems.character.Character;
import com.site21.bittermelon.systems.character.CharacterManager;
import com.site21.bittermelon.content.items.base.ItemWeight;
import com.site21.bittermelon.content.items.substance.SubstanceContainerItem;
import com.site21.bittermelon.systems.substance.SubstanceStack;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import static com.site21.bittermelon.init.custom.Substances.BLOOD;
import static com.site21.bittermelon.init.neoforge.BitterDataComponents.LIT;
import static com.site21.bittermelon.init.neoforge.BitterItemTags.LIGHTER;
import static com.site21.bittermelon.init.neoforge.BitterItems.CIGARETTE_BUTT;
import static com.site21.bittermelon.util.LocalMessageHelper.sendLocalMessage;

public class SmokableItem extends SubstanceContainerItem implements Equipable {
    private final Item buttItem;
    private static final int SMOKE_TICK_INTERVAL = 200;
    private static final double PARTICLE_OFFSET_DISTANCE = 0.3;
    private static final double PARTICLE_OFFSET_HEIGHT = 1.6;

    public SmokableItem(Properties properties, int width, int height, ItemWeight itemWeight, Item buttItem) {
        super(properties, width, height, itemWeight);
        this.buttItem = buttItem;
    }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(@NotNull Level level, @NotNull Player player, @NotNull InteractionHand hand) {
        if (level.isClientSide) return super.use(level, player, hand);

        InteractionHand otherHand = player.getUsedItemHand() == InteractionHand.MAIN_HAND
                ? InteractionHand.OFF_HAND
                : InteractionHand.MAIN_HAND;
        ItemStack smokableItem = player.getItemInHand(hand);
        ItemStack otherItem = player.getItemInHand(otherHand);

        if (!Boolean.TRUE.equals(smokableItem.get(LIT))) {
            return handleLightingOrSwallowing(level, player, smokableItem, otherItem);
        }

        return ItemUtils.startUsingInstantly(level, player, hand);
    }

    private @NotNull InteractionResultHolder<ItemStack> handleLightingOrSwallowing(Level level, Player player,
                                                                                   ItemStack smokableItem, @NotNull ItemStack otherItem) {
        if (otherItem.is(LIGHTER) || otherItem.is(Items.FLINT_AND_STEEL)) {
            SubstanceStack substance = new SubstanceStack(BLOOD.get(), 0);
            substance.setVolume(getCapacity(smokableItem));
            updateSubstance(smokableItem, substance);

            playLightingSound(level, player);
            smokableItem.set(LIT, true);

            if (otherItem.isStackable()) {
                otherItem.consume(1, player);
            } else {
                otherItem.hurtAndBreak(1, player, player.getEquipmentSlotForItem(otherItem));
            }
        } else {
            Character character = CharacterManager.get(level).getActiveCharacter(player);
            if (character != null) {
                Component message = Component.literal(
                                character.getName() + " swallows " + smokableItem.getHoverName().getString() + ".")
                        .setStyle(Style.EMPTY.withColor(character.getEmoteColor()));
                sendLocalMessage(player, 5, message);
            }

            playSwallowSound(level, player);
            smokableItem.consume(1, player);
        }
        return InteractionResultHolder.consume(smokableItem);
    }

    @Override
    public @NotNull InteractionResult useOn(@NotNull UseOnContext context) {
        Player player = context.getPlayer();
        Level level = context.getLevel();
        ItemStack stack = context.getItemInHand();
        BlockPos pos = context.getClickedPos().relative(context.getClickedFace());

        if (player != null && !level.isClientSide) {
            if (player.isShiftKeyDown()) {
                stack.consume(1, player);
                ItemEntity smokableButt = new ItemEntity(level, pos.getX(), pos.getY(), pos.getZ(), buttItem.getDefaultInstance());
                playExtinguishSound(level, player);
                addExtinguishParticles(level, pos);
                level.addFreshEntity(smokableButt);
                return InteractionResult.SUCCESS;
            }
        }
        return InteractionResult.PASS;
    }

    @Override
    public @NotNull ItemStack finishUsingItem(@NotNull ItemStack stack, @NotNull Level level, @NotNull LivingEntity entity) {
        if (!level.isClientSide && Boolean.TRUE.equals(stack.get(LIT))) {
            playSmokeSound(level, entity);
            addSmokeParticles(level, entity);
            consumeSubstances(stack, 1, entity);

            if (getTotalAmount(stack) < 0.1f) {
                return buttItem.getDefaultInstance();
            }
        }
        return stack;
    }

    @Override
    public void releaseUsing(@NotNull ItemStack stack, @NotNull Level level, @NotNull LivingEntity entity, int timeLeft) {
        if (timeLeft > getUseDuration(stack, entity) / 4) return;

        if (!level.isClientSide && Boolean.TRUE.equals(stack.get(LIT))) {
            playSmokeSound(level, entity);
            addSmokeParticles(level, entity);
            consumeSubstances(stack, 1 * ((float) (getUseDuration(stack, entity) - timeLeft) / getUseDuration(stack, entity)), entity);

            if (getTotalAmount(stack) < 0.1f && entity instanceof ServerPlayer player) {
                int slot = player.getInventory().findSlotMatchingItem(stack);
                stack.consume(1, entity);
                player.getInventory().add(slot, buttItem.getDefaultInstance());
            }
        }
    }

    @Override
    public int getUseDuration(@NotNull ItemStack stack, @NotNull LivingEntity entity) {
        return 30;
    }

    @Override
    public @NotNull UseAnim getUseAnimation(@NotNull ItemStack stack) {
        return UseAnim.TOOT_HORN;
    }

    @Override
    public boolean onEntityItemUpdate(@NotNull ItemStack stack, @NotNull ItemEntity entity) {
        if (entity.isUnderWater() && !entity.level().isClientSide) {
            entity.setItem(buttItem.getDefaultInstance());
            playExtinguishSound(entity.level(), entity);
            return true;
        }
        return false;
    }

    @Override
    public @NotNull EquipmentSlot getEquipmentSlot() {
        return EquipmentSlot.HEAD;
    }

    @Override
    public void inventoryTick(@NotNull ItemStack stack, @NotNull Level level, @NotNull Entity entity, int slotId, boolean isSelected) {
        if (!level.isClientSide && Boolean.TRUE.equals(stack.get(LIT))) {
            if (slotId == 39 && entity instanceof LivingEntity livingEntity) {
                if (level.getGameTime() % SMOKE_TICK_INTERVAL == 0) {
                    playSmokeSound(level, entity);
                    addSmokeParticles(level, entity);
                    consumeSubstances(stack, 1, livingEntity);

                    if (getTotalAmount(stack) < 0.1f) {
                        ((LivingEntity) entity).setItemSlot(EquipmentSlot.HEAD, ItemStack.EMPTY);

                        if (entity instanceof Player player) {
                            player.addItem(CIGARETTE_BUTT.toStack());
                        }
                    }
                }
            }
        }
    }

    private void playLightingSound(@NotNull Level level, @NotNull Player player) {
        level.playSound(null, player.getOnPos(), SoundEvents.FLINTANDSTEEL_USE,
                SoundSource.PLAYERS, 0.5F, level.getRandom().nextFloat() * 0.1F + 0.9F);
    }

    private void playSwallowSound(@NotNull Level level, @NotNull Player player) {
        level.playSound(null, player.getOnPos(), SoundEvents.PLAYER_BURP,
                SoundSource.PLAYERS, 0.5F, level.getRandom().nextFloat() * 0.1F + 0.9F);
    }

    public void playExtinguishSound(@NotNull Level level, @NotNull Entity entity) {
        level.playSound(
                null,
                entity.getOnPos(),
                SoundEvents.CANDLE_EXTINGUISH,
                SoundSource.PLAYERS,
                0.5F,
                level.getRandom().nextFloat() * 0.1F + 0.9F
        );
    }

    public void playSmokeSound(@NotNull Level level, @NotNull Entity entity) {
        level.playSound(
                null,
                entity.getOnPos(),
                SoundEvents.FIRE_AMBIENT,
                SoundSource.PLAYERS,
                0.5F,
                2
        );
    }

    private void addSmokeParticles(@NotNull Level level, @NotNull Entity entity) {
        if (level instanceof ServerLevel serverLevel && entity instanceof Player player) {
            float yRot = player.getYRot();
            double offsetX = -Math.sin(Math.toRadians(yRot)) * PARTICLE_OFFSET_DISTANCE;
            double offsetZ = Math.cos(Math.toRadians(yRot)) * PARTICLE_OFFSET_DISTANCE;

            serverLevel.sendParticles(
                    ParticleTypes.CAMPFIRE_COSY_SMOKE,
                    entity.getX() + offsetX,
                    entity.getY() + PARTICLE_OFFSET_HEIGHT,
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
}
