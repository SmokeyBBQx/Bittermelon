package net.smokeybbq.bittermelon.init;

import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.smokeybbq.bittermelon.Bittermelon;
import net.smokeybbq.bittermelon.blocks.blockentities.PuddleBlockEntity;

public class BlockEntityInit {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, Bittermelon.MODID);

    public static final RegistryObject<BlockEntityType<PuddleBlockEntity>> PUDDLE_BLOCK_ENTITY = BLOCK_ENTITIES.register("puddle_block_entity",
            () -> BlockEntityType.Builder.of(PuddleBlockEntity::new, BlockInit.PUDDLE.get())
                    .build(null)
    );
}
