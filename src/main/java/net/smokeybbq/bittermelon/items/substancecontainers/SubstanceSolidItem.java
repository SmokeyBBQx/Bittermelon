package net.smokeybbq.bittermelon.items.substancecontainers;

import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUtils;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.smokeybbq.bittermelon.init.ModCapabilities;
import net.smokeybbq.bittermelon.substances.Substance;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.Map;

public class SubstanceSolidItem extends SubstanceItem {
    private final int capacity;
    protected final int CONSUME_RATE = 10;
    public SubstanceSolidItem(Properties pProperties, int capacity) {
        super(pProperties, capacity);
        this.capacity = capacity;
    }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(@NotNull Level level, @NotNull Player player, @NotNull InteractionHand hand) {
        level.playSound(null, player.getX(), player.getY(), player.getZ(),
                SoundEvents.GENERIC_EAT, SoundSource.PLAYERS, 0.5F,
                level.getRandom().nextFloat() * 0.1F + 0.9F);

        return ItemUtils.startUsingInstantly(level, player, hand);
    }

    @Override
    public int getUseDuration(@NotNull ItemStack stack) {
        return CONSUME_RATE * 2;
    }

    @Override
    public @NotNull UseAnim getUseAnimation(@NotNull ItemStack stack) {
        return UseAnim.EAT;
    }

    @Override
    public @NotNull ItemStack finishUsingItem(@NotNull ItemStack stack, @NotNull Level level, @NotNull LivingEntity entity) {
        if (entity instanceof Player player) {

            if (!level.isClientSide()) {
                player.sendSystemMessage(getFlavorMessageComponent(stack));
            }

            getSubstanceContainer(stack).ifPresent(cap -> {
                Map<Substance, Integer> substances = cap.getSubstances();
                int totalSubstances = substances.size();
                if (totalSubstances == 0) return;

                int totalAmount = cap.getTotalAmount();

                substances.forEach((substance, amount) -> {
                    float proportion = (float) amount / totalAmount;
                    int consumeAmount = (int) Math.ceil(CONSUME_RATE * proportion);
                    int actualAmount = Math.min(amount, consumeAmount);

                    updateSubstance(stack, substance, -actualAmount);
                    substance.getEffects(entity, actualAmount);
                });
            });

            level.playSound(null, entity.getX(), entity.getY(), entity.getZ(),
                    SoundEvents.PLAYER_BURP, SoundSource.PLAYERS, 0.5F,
                    level.getRandom().nextFloat() * 0.1F + 0.9F);

            checkIfConsumed(stack);
        }
        return stack;
    }

    public void checkIfConsumed(ItemStack stack) {
        getSubstanceContainer(stack).ifPresent(cap -> {
            if (cap.getTotalAmount() <= 0) {
                stack.shrink(1);
            }
        });
    }
}
