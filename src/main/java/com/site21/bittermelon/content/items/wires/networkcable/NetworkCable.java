package com.site21.bittermelon.content.items.wires.networkcable;

import com.site21.bittermelon.content.blocks.devices.IElectronic;
import com.site21.bittermelon.content.blocks.devices.implementations.containmentalarm.ContainmentAlarmBlockEntity;
import com.site21.bittermelon.content.items.base.BaseItem;
import com.site21.bittermelon.content.items.base.ItemWeight;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import static com.site21.bittermelon.init.neoforge.BitterDataComponents.*;

public class NetworkCable extends BaseItem {
    public NetworkCable(Properties properties, int width, int height, ItemWeight itemWeight) {
        super(properties, width, height, itemWeight);
    }

    @Override
    public @NotNull InteractionResult useOn(@NotNull UseOnContext context) {
        Level level = context.getLevel();
        BlockPos pos = context.getClickedPos();
        ItemStack stack = context.getItemInHand();
        Player player = context.getPlayer();

        if (player == null) return InteractionResult.FAIL;

        if (level.getBlockEntity(pos) instanceof IElectronic) {
            if (stack.get(CORD_CONNECTION) != null) {
                makeConnection(pos, level, stack);
            } else {
                stack.set(CORD_CONNECTION, pos);
            }
            return InteractionResult.SUCCESS;
        }

        return InteractionResult.FAIL;
    }

    private void makeConnection(BlockPos pos, @NotNull Level level, ItemStack stack) {
        if (level.getBlockEntity(pos) instanceof ContainmentAlarmBlockEntity device) {
            device.addLinkedDevice(stack.get(CORD_CONNECTION));
            stack.remove(CORD_CONNECTION);
        }
    }

    @Override
    public void inventoryTick(@NotNull ItemStack stack, @NotNull Level level, @NotNull Entity entity, int slotId, boolean isSelected) {
        super.inventoryTick(stack, level, entity, slotId, isSelected);

        if (level.isClientSide) return;

        if (!isSelected) {
            stack.remove(CORD_CONNECTION);
        }
    }
}
