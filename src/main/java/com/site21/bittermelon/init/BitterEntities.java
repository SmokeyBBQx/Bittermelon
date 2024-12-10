package com.site21.bittermelon.init;

import com.site21.bittermelon.Bittermelon;
import com.site21.bittermelon.entities.miscellaneous.ThrownItemProjectile;
import com.site21.bittermelon.entities.scps.SCP939.SCP939;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class BitterEntities {
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister.create(BuiltInRegistries.ENTITY_TYPE, Bittermelon.MOD_ID);

    public static final DeferredHolder<EntityType<?>, EntityType<ThrownItemProjectile>> THROWN_ITEM_PROJECTILE = ENTITY_TYPES.register("thrown_item_projectile",
            () -> EntityType.Builder.<ThrownItemProjectile>of(ThrownItemProjectile::new, MobCategory.MISC)
                    .sized(0.5f, 0.5f)
                    .build("thrown_item_projectile"));

    public static final DeferredHolder<EntityType<?>, EntityType<SCP939>> SCP_939 = ENTITY_TYPES.register("scp939",
            () -> EntityType.Builder.of(SCP939::new, MobCategory.MONSTER)
                    .sized(1, 2)
                    .build("scp939"));

    public static void register(IEventBus eventBus) {
        ENTITY_TYPES.register(eventBus);
        eventBus.addListener(BitterEntities::registerAttributes);
    }

    private static void registerAttributes(EntityAttributeCreationEvent event) {
        event.put(SCP_939.get(), SCP939.createAttributes().build());
    }
}
