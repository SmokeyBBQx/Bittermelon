package com.site21.bittermelon.init;

import com.site21.bittermelon.Bittermelon;
import com.site21.bittermelon.content.substance.Substance;
import com.site21.bittermelon.content.substance.reactions.Reaction;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.registries.NewRegistryEvent;
import net.neoforged.neoforge.registries.RegistryBuilder;

public class BitterRegistries {
    public static final ResourceKey<Registry<Substance>> SUBSTANCE_REGISTRY_KEY = ResourceKey.createRegistryKey(ResourceLocation.fromNamespaceAndPath(Bittermelon.MOD_ID, "substances"));
    public static final Registry<Substance> SUBSTANCE_REGISTRY = new RegistryBuilder<>(SUBSTANCE_REGISTRY_KEY)
            .sync(true)
            .create();

    public static final ResourceKey<Registry<Reaction>> REACTION_REGISTRY_KEY = ResourceKey.createRegistryKey(ResourceLocation.fromNamespaceAndPath(Bittermelon.MOD_ID, "reactions"));
    public static final Registry<Reaction> REACTION_REGISTRY = new RegistryBuilder<>(REACTION_REGISTRY_KEY)
            .create();

    @SubscribeEvent
    public static void registerRegistries(NewRegistryEvent event) {
       event.register(SUBSTANCE_REGISTRY);
       event.register(REACTION_REGISTRY);
    }
}
