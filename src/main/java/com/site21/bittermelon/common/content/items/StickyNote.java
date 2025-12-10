package com.site21.bittermelon.common.content.items;

import com.site21.bittermelon.common.content.blocks.stickynote.StickyNoteBlock;
import com.site21.bittermelon.common.content.blocks.stickynote.StickyNoteBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

import static com.site21.bittermelon.init.neoforge.BitterDataComponents.MESSAGE;

public class StickyNote extends BlockItem {
    public StickyNote(Block block, Properties properties) {
        super(block, properties);
    }

    @Override
    protected boolean placeBlock(BlockPlaceContext context, BlockState state) {
        boolean result = context.getLevel().setBlock(context.getClickedPos(), state, 11);

        ItemStack stack = context.getItemInHand();
        Level level = context.getLevel();
        BlockPos pos = context.getClickedPos();
        StickyNoteBlock.Position position = StickyNoteBlock.getPosition(state, context.getClickLocation(), pos);
        if (level.getBlockEntity(pos) instanceof StickyNoteBlockEntity stickyNote) {
            stickyNote.setNote(position.ordinal(), stack.get(MESSAGE));
        }

        return result;
    }

}
