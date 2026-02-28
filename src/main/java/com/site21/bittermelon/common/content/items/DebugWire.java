package com.site21.bittermelon.common.content.items;

import com.site21.bittermelon.common.systems.electronics.wiring.WireNetwork;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;

import static com.site21.bittermelon.init.neoforge.BitterAttachmentTypes.WIRE_NETWORK;
import static com.site21.bittermelon.init.neoforge.BitterDataComponents.CORD_CONNECTION;

public class DebugWire extends Item {
    public DebugWire(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Level level = context.getLevel();
        ItemStack stack = context.getItemInHand();
        BlockPos connectedPos = stack.get(CORD_CONNECTION);

        if (connectedPos == null) {
            stack.set(CORD_CONNECTION, context.getClickedPos());
        } else {
            WireNetwork network = level.getChunkAt(context.getClickedPos()).getData(WIRE_NETWORK);
            network.connectNodes(connectedPos, context.getClickedPos(), DyeColor.RED);
        }

        return InteractionResult.SUCCESS;
    }
}
