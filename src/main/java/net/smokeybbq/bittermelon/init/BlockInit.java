package net.smokeybbq.bittermelon.init;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.smokeybbq.bittermelon.Bittermelon;
import net.smokeybbq.bittermelon.blocks.PuddleBlock;

public class BlockInit {
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, Bittermelon.MODID);

    public static final RegistryObject<Block> PUDDLE = BLOCKS.register("puddle",
            () -> new PuddleBlock(BlockBehaviour.Properties.of()
            .mapColor(MapColor.WATER)
            .noOcclusion()
            .strength(0.1F)
            .noCollission()
            .pushReaction(PushReaction.DESTROY)
            .sound(SoundType.SLIME_BLOCK)
            ));
}
