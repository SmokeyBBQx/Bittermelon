package net.smokeybbq.bittermelon.init;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.smokeybbq.bittermelon.Bittermelon;
import net.smokeybbq.bittermelon.blocks.blockentities.PuddleBlockEntity;
import net.smokeybbq.bittermelon.entities.PuddleFallingBlockEntity;
import net.smokeybbq.bittermelon.entities.ThrownItemProjectile;

public class EntityInit {
    public static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, Bittermelon.MODID);

    public static final RegistryObject<EntityType<PuddleFallingBlockEntity>> PUDDLE_FALLING_BLOCK = ENTITIES.register("puddle_falling_block",
            () -> EntityType.Builder.<PuddleFallingBlockEntity>of(PuddleFallingBlockEntity::new, MobCategory.MISC)
                    .sized(0.98F, 0.98F)
                    .build("puddle_falling_block")
    );

    public static final RegistryObject<EntityType<ThrownItemProjectile>> THROWN_ITEM_PROJECTILE = ENTITIES.register("thrown_item_projectile",
            () -> EntityType.Builder.<ThrownItemProjectile>of(ThrownItemProjectile::new, MobCategory.MISC)
                    .sized(0.5F, 0.5F)
                    .build("thrown_item_projectile"));
}
