package com.site21.bittermelon.blocks;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.properties.IntegerProperty;


public class DirtyFloorBlock extends Block {
    public static final IntegerProperty DIRTINESS = IntegerProperty.create("dirtiness", 0, 2);

    public DirtyFloorBlock(Properties properties) {
        super(properties);
    }
}
