package com.site21.bittermelon.content.items.writingutensils;

import com.site21.bittermelon.content.blocks.wallwriting.WallWritingBlockEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public interface WallWriter {
    boolean tryApplyToWall(Level level, WallWritingBlockEntity wallWriting, Player player, ItemStack stack);
}
