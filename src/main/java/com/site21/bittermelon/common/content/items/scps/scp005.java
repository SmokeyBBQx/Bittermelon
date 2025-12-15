package com.site21.bittermelon.common.content.items.scps;

import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.neoforged.neoforge.registries.DeferredItem;

import static com.site21.bittermelon.init.neoforge.BitterItems.ITEMS;

public class scp005 {
    public static final DeferredItem<Item> SCP_005 = ITEMS.register("scp_005", registryName ->            new Item(new Item.Properties()
                    .setId(ResourceKey.create(Registries.ITEM, registryName))));

    // SCP-005 Door Opener Item Class
    public static class SCP005Item extends Item {
        public SCP005Item(Properties properties) {
            super(properties);
        }

        @Override
        public InteractionResult useOn(UseOnContext context) {
            Level level = context.getLevel();
            BlockPos pos = context.getClickedPos();
            BlockState state = level.getBlockState(pos);

            // Check if the clicked block is a door
            if (state.getBlock() instanceof DoorBlock doorBlock) {
                if (!level.isClientSide) {
                    // Get the current open state
                    boolean isOpen = state.getValue(BlockStateProperties.OPEN);

                    // Toggle using the door's own method (handles both halves automatically)
                    doorBlock.setOpen(null, level, state, pos, !isOpen);
                }

                return level.isClientSide ? InteractionResult.SUCCESS : InteractionResult.CONSUME;
            }

            return InteractionResult.PASS;
        }
    }
}