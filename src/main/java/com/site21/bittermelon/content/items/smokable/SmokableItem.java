package com.site21.bittermelon.content.items.smokable;

import com.site21.bittermelon.content.character.Character;
import com.site21.bittermelon.content.character.CharacterManager;
import com.site21.bittermelon.content.items.base.ItemWeight;
import com.site21.bittermelon.content.items.substance.SubstanceContainerItem;
import com.site21.bittermelon.content.substance.SubstanceStack;
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
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import static com.site21.bittermelon.init.custom.Substances.LIQUID_BLOOD;
import static com.site21.bittermelon.init.neoforge.BitterDataComponents.LIT;
import static com.site21.bittermelon.init.neoforge.BitterItemTags.LIGHTER;
import static com.site21.bittermelon.util.LocalMessageHelper.sendLocalMessage;

public class SmokableItem extends SubstanceContainerItem {
    private final Item buttItem;

    public SmokableItem(Properties properties, int width, int height, ItemWeight itemWeight, int capacity, Item buttItem) {
        super(properties, width, height, itemWeight, capacity);
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

        if (Boolean.FALSE.equals(smokableItem.get(LIT)) || smokableItem.get(LIT) == null) {
            // TODO: Tag still doesn't work
            if (otherItem.is(LIGHTER) || otherItem.is(Items.FLINT_AND_STEEL)) {
                // TODO: Replace this with actual substance handling
                updateSubstance(smokableItem, new SubstanceStack(LIQUID_BLOOD.get(), 0.8f));
                level.playSound(
                        null,
                        player.getOnPos(),
                        SoundEvents.FLINTANDSTEEL_USE,
                        SoundSource.PLAYERS,
                        0.5F,
                        level.getRandom().nextFloat() * 0.1F + 0.9F
                );
                smokableItem.set(LIT, true);
                if (otherItem.isStackable()) {
                    otherItem.consume(1, player);
                } else {
                    otherItem.hurtAndBreak(1, player, player.getEquipmentSlotForItem(otherItem));
                }
                return InteractionResultHolder.consume(smokableItem);
            } else {
                level.playSound(null, player.getOnPos(),
                        SoundEvents.PLAYER_BURP, SoundSource.PLAYERS, 0.5F,
                        level.getRandom().nextFloat() * 0.1F + 0.9F);

                Character character = CharacterManager.get(level).getActiveCharacter(player);
                if (character != null) {
                    Component component = Component.literal(character.getName() + " swallows " + smokableItem.getHoverName().getString() + ".")
                            .setStyle(Style.EMPTY.withColor(character.getEmoteColor()));
                    sendLocalMessage(player, 5, component);
                }
                smokableItem.consume(1, player);
                return InteractionResultHolder.consume(smokableItem);
            }
        }

        return ItemUtils.startUsingInstantly(level, player, hand);
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
            consumeSubstances(stack, 1);

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
            consumeSubstances(stack, 1 * ((float) (getUseDuration(stack, entity) - timeLeft) / getUseDuration(stack, entity)));

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
    public void inventoryTick(ItemStack stack, @NotNull Level level, @NotNull Entity entity, int slotId, boolean isSelected) {
        // TODO: Simulate with reaction handler
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

    public void addSmokeParticles(@NotNull Level level, @NotNull Entity entity) {
        if (level instanceof ServerLevel serverLevel) {
            if (entity instanceof Player player) {
                float yRot = player.getYRot();

                double offsetX = -Math.sin(Math.toRadians(yRot)) * 0.3;
                double offsetY = 1.6;
                double offsetZ = Math.cos(Math.toRadians(yRot)) * 0.3;

                serverLevel.sendParticles(
                        ParticleTypes.CAMPFIRE_COSY_SMOKE,
                        entity.getX() + offsetX,
                        entity.getY() + offsetY,
                        entity.getZ() + offsetZ,
                        1,
                        0.1, 0.1, 0.1,
                        0.01
                );
            }
        }
    }

    public void addExtinguishParticles(Level level, BlockPos pos) {
        if (level instanceof ServerLevel serverLevel) {
            serverLevel.sendParticles(
                    ParticleTypes.FLAME,
                    pos.getX(),
                    pos.getY(),
                    pos.getZ(),
                    1,
                    0, 0, 0,
                    0.01
            );

            serverLevel.sendParticles(
                    ParticleTypes.SMOKE,
                    pos.getX(),
                    pos.getY(),
                    pos.getZ(),
                    1,
                    0, 0, 0,
                    0.01
            );
        }
    }
}
