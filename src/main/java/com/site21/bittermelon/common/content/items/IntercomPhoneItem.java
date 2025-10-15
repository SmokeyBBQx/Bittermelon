package com.site21.bittermelon.common.content.items;

import com.site21.bittermelon.common.content.blocks.electronics.intercom.IntercomBlockEntity;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static com.site21.bittermelon.init.neoforge.BitterDataComponents.CORD_CONNECTION;

public class IntercomPhoneItem extends Item {
    public IntercomPhoneItem(Properties properties) {
        super(properties);
    }

    @Override
    public void inventoryTick(@NotNull ItemStack stack, @NotNull ServerLevel level, @NotNull Entity entity, @Nullable EquipmentSlot slot) {
        BlockPos intercomPos = stack.get(CORD_CONNECTION.get());

        if (intercomPos == null) return;
        if (!(entity instanceof ServerPlayer player)) return;

        if (level.getBlockEntity(intercomPos) instanceof IntercomBlockEntity intercom) {
            if (slot != EquipmentSlot.MAINHAND && slot != EquipmentSlot.OFFHAND) {
                stack.setCount(0);
                intercom.setPhonePickedUp(false);
                intercom.setPhoneUser(null);
                level.playSound(null, intercomPos, SoundEvents.HEAVY_CORE_HIT, SoundSource.PLAYERS);
                player.sendSystemMessage(Component.literal("You must hold the phone.").withStyle(ChatFormatting.RED));
            }

            if (entity.distanceToSqr(intercomPos.getX(), intercomPos.getY(), intercomPos.getZ()) > 2 * 2) {
                stack.setCount(0);
                intercom.setPhonePickedUp(false);
                intercom.setPhoneUser(null);
                level.playSound(null, intercomPos, SoundEvents.HEAVY_CORE_HIT, SoundSource.PLAYERS);
                player.sendSystemMessage(Component.literal("You must stay within range.").withStyle(ChatFormatting.RED));
            }
        }
    }
}
