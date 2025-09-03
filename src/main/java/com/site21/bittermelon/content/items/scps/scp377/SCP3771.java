package com.site21.bittermelon.content.items.scps.scp377;

import com.site21.bittermelon.content.items.base.BaseItem;
import com.site21.bittermelon.content.items.base.ItemWeight;
import com.site21.bittermelon.content.items.scps.scp377.networking.OpenSCP3771Screen;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.network.PacketDistributor;
import org.jetbrains.annotations.NotNull;

import static com.site21.bittermelon.init.neoforge.BitterDataComponents.FORTUNE;
import static com.site21.bittermelon.init.neoforge.BitterDataComponents.FORTUNE_READ_TIME;

public class SCP3771 extends Item {
    private static final long ACTIVATION_DELAY = 100;

    public SCP3771(Properties properties) {
        super(properties);
    }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(@NotNull Level level, @NotNull Player player, @NotNull InteractionHand usedHand) {
        ItemStack stack = player.getItemInHand(usedHand);

        if (level.isClientSide) return InteractionResultHolder.pass(stack);


        if (stack.getOrDefault(FORTUNE_READ_TIME, -1L) == -1) {
            stack.set(FORTUNE_READ_TIME, level.getGameTime());
        }

        if (player instanceof ServerPlayer serverPlayer) {
            PacketDistributor.sendToPlayer(serverPlayer, new OpenSCP3771Screen(stack.getOrDefault(FORTUNE, Fortune.values()[0])));
        }

        return InteractionResultHolder.success(stack);
    }

    @Override
    public void inventoryTick(@NotNull ItemStack stack, @NotNull Level level, Entity entity, int slotId, boolean isSelected) {
        // TODO: Fortune will only be applied if item is held (and also only to the player holding it)

        if (level.isClientSide) return;
        long readTime = stack.getOrDefault(FORTUNE_READ_TIME, -1L);

        if (readTime != -1L && readTime != -2L &&
                level.getGameTime() - readTime >= ACTIVATION_DELAY) {

            Fortune fortune = stack.getOrDefault(FORTUNE, Fortune.values()[0]);
            if (entity instanceof Player player) {
                fortune.applyTo(player);
                stack.set(FORTUNE_READ_TIME, -2L);
            }
        }
    }
}
