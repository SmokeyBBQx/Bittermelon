package com.site21.bittermelon.common.content.items;

import com.site21.bittermelon.common.content.blocks.electronics.intercom.IntercomBlockEntity;
import com.site21.bittermelon.common.content.items.base.BitterItem;
import com.site21.bittermelon.common.content.items.base.ItemWeight;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import static com.site21.bittermelon.init.neoforge.BitterDataComponents.CORD_CONNECTION;

public class IntercomPhoneItem extends BitterItem {
    public IntercomPhoneItem(Properties properties) {
        super(properties);
    }

    @Override
    public void inventoryTick(@NotNull ItemStack stack, @NotNull Level level, @NotNull Entity entity, int slotId, boolean isSelected) {
        super.inventoryTick(stack, level, entity, slotId, isSelected);

        if (level.isClientSide) return;
        BlockPos intercomPos = stack.get(CORD_CONNECTION.get());

        if (intercomPos == null) return;

        if (level.getBlockEntity(intercomPos) instanceof IntercomBlockEntity intercom) {
            if (!isSelected) {
                stack.setCount(0);
                intercom.setPhonePickedUp(false);
                intercom.setPhoneUser(null);
                level.playSound(null, intercomPos, SoundEvents.HEAVY_CORE_HIT, SoundSource.PLAYERS);
                entity.sendSystemMessage(Component.literal("You must hold the phone.").withStyle(ChatFormatting.RED));
            }

            if (entity.distanceToSqr(intercomPos.getX(), intercomPos.getY(), intercomPos.getZ()) > 2 * 2) {
                stack.setCount(0);
                intercom.setPhonePickedUp(false);
                intercom.setPhoneUser(null);
                level.playSound(null, intercomPos, SoundEvents.HEAVY_CORE_HIT, SoundSource.PLAYERS);
                entity.sendSystemMessage(Component.literal("You must stay within range.").withStyle(ChatFormatting.RED));
            }
        }
    }
}
