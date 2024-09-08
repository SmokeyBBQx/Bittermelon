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
import net.smokeybbq.bittermelon.blocks.*;

public class BlockInit {
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, Bittermelon.MODID);

    public static final RegistryObject<Block> PUDDLE = BLOCKS.register("puddle",
            () -> new PuddleBlock(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.WATER)
                    .noOcclusion()
                    .noCollission()
                    .destroyTime(-1)
                    .sound(SoundType.SLIME_BLOCK)
                    .pushReaction(PushReaction.NORMAL)
            ));

    public static final RegistryObject<Block> DIRTY = BLOCKS.register("dirty",
            () -> new DirtyDecalBlock(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.DIRT)
                    .noOcclusion()
                    .noCollission()
                    .destroyTime(-1)
                    .pushReaction(PushReaction.IGNORE)
            ));

    public static final RegistryObject<Block> WINE_BOTTLE_BLOCK = BLOCKS.register("wine_bottle_block",
            () -> new BottleBlock(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.COLOR_PURPLE)
                    .destroyTime(1)
                    .sound(SoundType.AMETHYST)));

    public static final RegistryObject<Block> BEER_BOTTLE_BLOCK = BLOCKS.register("beer_bottle_block",
            () -> new BottleBlock(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.COLOR_GREEN)
                    .destroyTime(1)
                    .sound(SoundType.AMETHYST)));

    public static final RegistryObject<Block> KETCHUP_BOTTLE_BLOCK = BLOCKS.register("ketchup_bottle_block",
            () -> new BottleBlock(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.COLOR_RED)
                    .destroyTime(1)
                    .sound(SoundType.GLASS)));

    public static final RegistryObject<Block> DRINKING_GLASS_BLOCK = BLOCKS.register("drinking_glass_block",
            () -> new CupBlock(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.COLOR_LIGHT_BLUE)
                    .destroyTime(1)
                    .sound(SoundType.GLASS)));

    public static final RegistryObject<Block> LARGE_BEAKER_BLOCK = BLOCKS.register("large_beaker_block",
            () -> new LargeBeakerBlock(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.COLOR_LIGHT_BLUE)
                    .destroyTime(1)
                    .sound(SoundType.GLASS)));
}
