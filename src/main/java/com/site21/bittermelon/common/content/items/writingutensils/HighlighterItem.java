package com.site21.bittermelon.common.content.items.writingutensils;

import com.site21.bittermelon.common.content.blocks.wallwriting.WallWritingBlockEntity;
import com.site21.bittermelon.init.neoforge.BitterBlocks;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

import static net.minecraft.core.component.DataComponents.BASE_COLOR;

public class HighlighterItem extends WallWriterItem implements WallWriter {
    public HighlighterItem(Properties properties) {
        super(BitterBlocks.WALL_WRITING.get(), properties);
    }

    @Override
    protected void formatText(@NotNull WallWritingBlockEntity wallWriting, ItemStack stack) {
        wallWriting.updateText((text) -> text.setColor(stack.getOrDefault(BASE_COLOR, DyeColor.BLACK)).setHasGlowingText(true));
    }
}
