package com.site21.bittermelon.content.items.handheldprogrammer;

import com.site21.bittermelon.content.blocks.devices.wiring.PLCUser;
import com.site21.bittermelon.content.items.base.BaseItem;
import com.site21.bittermelon.content.items.base.ItemWeight;
import com.site21.bittermelon.content.items.handheldprogrammer.client.ProgrammingScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public class HandheldProgrammerItem extends BaseItem {
    public HandheldProgrammerItem(Properties properties, int width, int height, ItemWeight itemWeight) {
        super(properties, width, height, itemWeight);
    }

    @Override
    public @NotNull InteractionResult useOn(@NotNull UseOnContext context) {
        Level level = context.getLevel();
        BlockPos pos = context.getClickedPos();
        Player player = context.getPlayer();

        if (player == null) return InteractionResult.FAIL;

        if (level.getBlockEntity(pos) instanceof PLCUser plcUser) {
            if (level.isClientSide) {
                Minecraft.getInstance().setScreen(new ProgrammingScreen(plcUser.getPLC()));
            }
            return InteractionResult.SUCCESS;
        }

        return InteractionResult.FAIL;
    }
}
