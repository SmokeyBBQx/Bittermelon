package com.site21.bittermelon.common.content.blocks.properties;

import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;

public class BitterStateProperties {
    public static final EnumProperty<Placement> PLACEMENT = EnumProperty.create("placement", Placement.class);
    public static final IntegerProperty LEVEL = IntegerProperty.create("level", 1, 16);
    public static final BooleanProperty OVERFLOWING = BooleanProperty.create("overflowing");
}
