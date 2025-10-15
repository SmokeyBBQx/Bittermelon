package com.site21.bittermelon.init.neoforge;

import com.site21.bittermelon.Bittermelon;
import com.site21.bittermelon.common.content.entities.scp131.SCP131;
import com.site21.bittermelon.common.content.entities.scp1507.SCP1507;
import com.site21.bittermelon.common.content.entities.scp650.SCP650;
import com.site21.bittermelon.common.content.entities.scp939.SCP939;
import com.site21.bittermelon.common.content.entities.ThrownItemProjectile;
import com.site21.bittermelon.common.content.entities.chicken.Chicken;
import com.site21.bittermelon.common.content.items.scps.scp2398.SCP2398Projectile;
import com.site21.bittermelon.common.content.items.taser.TaserProjectile;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.jetbrains.annotations.NotNull;

public class BitterEntities {
    public static final DeferredRegister.Entities ENTITY_TYPES = DeferredRegister.createEntities(Bittermelon.MOD_ID);

    public static final DeferredHolder<EntityType<?>, EntityType<ThrownItemProjectile>> THROWN_ITEM_PROJECTILE = ENTITY_TYPES.register("thrown_item_projectile",
            () -> EntityType.Builder.<ThrownItemProjectile>of(ThrownItemProjectile::new, MobCategory.MISC)
                    .sized(0.5f, 0.5f)
                    .build(ResourceKey.create(Registries.ENTITY_TYPE, ResourceLocation.fromNamespaceAndPath(Bittermelon.MOD_ID, "thrown_item_projectile"))));

    public static final DeferredHolder<EntityType<?>, EntityType<SCP2398Projectile>> SCP_2398_PROJECTILE = ENTITY_TYPES.register("scp_2398_projectile",
            () -> EntityType.Builder.<SCP2398Projectile>of(SCP2398Projectile::new, MobCategory.MISC)
                    .sized(0.5f, 0.5f)
                    .build(ResourceKey.create(Registries.ENTITY_TYPE, ResourceLocation.fromNamespaceAndPath(Bittermelon.MOD_ID, "scp_2398_projectile"))));

    public static final DeferredHolder<EntityType<?>, EntityType<SCP939>> SCP_939 = ENTITY_TYPES.register("scp939",
            () -> EntityType.Builder.of(SCP939::new, MobCategory.MONSTER)
                    .sized(0.9f, 0.9f)
                    .build(ResourceKey.create(Registries.ENTITY_TYPE, ResourceLocation.fromNamespaceAndPath(Bittermelon.MOD_ID, "scp939"))));

    public static final DeferredHolder<EntityType<?>, EntityType<Chicken>> CHICKEN = ENTITY_TYPES.register("chicken",
            () -> EntityType.Builder.of(Chicken::new, MobCategory.AMBIENT)
                    .sized(0.3f, 0.3f)
                    .build(ResourceKey.create(Registries.ENTITY_TYPE, ResourceLocation.fromNamespaceAndPath(Bittermelon.MOD_ID, "chicken"))));

    public static final DeferredHolder<EntityType<?>, EntityType<SCP650>> SCP_650 = ENTITY_TYPES.register("scp650",
            () -> EntityType.Builder.of(SCP650::new, MobCategory.MONSTER)
                    .sized(0.9f, 1.6f)
                    .build(ResourceKey.create(Registries.ENTITY_TYPE, ResourceLocation.fromNamespaceAndPath(Bittermelon.MOD_ID, "scp650"))));

    public static final DeferredHolder<EntityType<?>, EntityType<SCP131>> SCP_131 = ENTITY_TYPES.register("scp131",
            () -> EntityType.Builder.of(SCP131::new, MobCategory.AMBIENT)
                    .sized(0.3f, 0.3f)
                    .build(ResourceKey.create(Registries.ENTITY_TYPE, ResourceLocation.fromNamespaceAndPath(Bittermelon.MOD_ID, "scp131"))));

    public static final DeferredHolder<EntityType<?>, EntityType<TaserProjectile>> TASER_PROJECTILE = ENTITY_TYPES.register("taser_projectile",
            () -> EntityType.Builder.<TaserProjectile>of(TaserProjectile::new, MobCategory.MISC)
                    .sized(0.5f, 0.5f)
                    .build(ResourceKey.create(Registries.ENTITY_TYPE, ResourceLocation.fromNamespaceAndPath(Bittermelon.MOD_ID, "taser_projectile"))));

    public static final DeferredHolder<EntityType<?>, EntityType<SCP1507>> SCP_1507 = ENTITY_TYPES.register("scp1507",
            () -> EntityType.Builder.of(SCP1507::new, MobCategory.MONSTER)
                    .sized(0.5f, 0.5f)
                    .build(ResourceKey.create(Registries.ENTITY_TYPE, ResourceLocation.fromNamespaceAndPath(Bittermelon.MOD_ID, "scp1507"))));

    public static void register(IEventBus eventBus) {
        ENTITY_TYPES.register(eventBus);
        eventBus.addListener(BitterEntities::registerAttributes);
    }

    private static void registerAttributes(@NotNull EntityAttributeCreationEvent event) {
        event.put(SCP_939.get(), SCP939.createAttributes().build());
        event.put(CHICKEN.get(), Chicken.createAttributes().build());
        event.put(SCP_650.get(), SCP650.createAttributes().build());
        event.put(SCP_131.get(), SCP131.createAttributes().build());
        event.put(SCP_1507.get(), SCP1507.createAttributes().build());
    }
}