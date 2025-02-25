package com.site21.bittermelon.content.items.screwdriver;

import com.site21.bittermelon.content.blocks.devices.IElectronic;
import com.site21.bittermelon.content.items.base.BaseItem;
import com.site21.bittermelon.content.items.base.ItemWeight;
import com.site21.bittermelon.content.items.wires.wire.client.WiringScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public class ScrewdriverItem extends BaseItem {
    public ScrewdriverItem(Properties properties, int width, int height, ItemWeight itemWeight) {
        super(properties, width, height, itemWeight);
    }

    @Override
    public @NotNull InteractionResult useOn(@NotNull UseOnContext context) {
        Level level = context.getLevel();
        BlockPos pos = context.getClickedPos();
        ItemStack stack = context.getItemInHand();
        Player player = context.getPlayer();

        if (player == null) return InteractionResult.FAIL;

        if (level.getBlockEntity(pos) instanceof IElectronic electronic) {
            if (level.isClientSide) {
                Minecraft.getInstance().setScreen(new WiringScreen(electronic, stack));
            }
            return InteractionResult.SUCCESS;
        }

        return InteractionResult.FAIL;
    }
}
