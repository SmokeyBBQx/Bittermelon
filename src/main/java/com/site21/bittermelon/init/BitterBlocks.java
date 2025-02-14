package com.site21.bittermelon.init;

import com.site21.bittermelon.Bittermelon;
import com.site21.bittermelon.blocks.*;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;

public class BitterBlocks {
    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(Bittermelon.MOD_ID);

    public static final DeferredBlock<FluidBlock> FLUID = BLOCKS.register("fluid", () -> new FluidBlock(BlockBehaviour.Properties.of()
            .mapColor(MapColor.WATER)
            .noOcclusion()
            .noCollission()
            .destroyTime(-1)
            .sound(SoundType.SLIME_BLOCK)
            .pushReaction(PushReaction.NORMAL)
    ));

    public static final DeferredBlock<SmallBox> SMALL_CARDBOARD_BOX = BLOCKS.register("small_cardboard_box", () -> new SmallBox(BlockBehaviour.Properties.of()
            .destroyTime(-1)
            .sound(SoundType.WOOL)
    ));

    public static final DeferredBlock<StructuralBlock> STRUCTURAL_BLOCK = BLOCKS.register("structural_block", () -> new StructuralBlock(BlockBehaviour.Properties.of()
            .destroyTime(1.5f)
    ));

    public static final DeferredBlock<ATMBlock> ATM = BLOCKS.register("atm", () -> new ATMBlock(BlockBehaviour.Properties.of()
            .noOcclusion()
    ));

    public static final DeferredBlock<ContainmentPanelBlock> CONTAINMENT_PANEL = BLOCKS.register("containment_panel",
            () -> new ContainmentPanelBlock(BlockBehaviour.Properties.of().noOcclusion()
    ));

    public static final DeferredBlock<DirtyFloorBlock> DIRTY_FLOOR = BLOCKS.register("dirty_floor",
            () -> new DirtyFloorBlock(BlockBehaviour.Properties.of()
                    .noOcclusion()
                    .noCollission()
                    .destroyTime(-1)
            ));

    public static final DeferredBlock<ThermometerBlock> THERMOMETER = BLOCKS.register("thermometer",
            () -> new ThermometerBlock(BlockBehaviour.Properties.of().noOcclusion()
            ));
}
