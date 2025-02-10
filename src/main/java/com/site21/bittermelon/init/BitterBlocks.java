package com.site21.bittermelon.init;

import com.site21.bittermelon.Bittermelon;
import com.site21.bittermelon.blocks.ATMBlock;
import com.site21.bittermelon.blocks.FluidBlock;
import com.site21.bittermelon.blocks.SmallBox;
import com.site21.bittermelon.blocks.StructuralBlock;
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
}
