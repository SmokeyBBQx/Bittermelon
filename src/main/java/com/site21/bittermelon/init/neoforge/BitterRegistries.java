package com.site21.bittermelon.init.neoforge;

import com.site21.bittermelon.Bittermelon;
import com.site21.bittermelon.content.blocks.devices.connection.Signal;
import com.site21.bittermelon.content.chat.VerbSet;
import com.site21.bittermelon.content.substance.Substance;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.registries.NewRegistryEvent;
import net.neoforged.neoforge.registries.RegistryBuilder;
import org.jetbrains.annotations.NotNull;

import java.util.function.Function;

public class BitterRegistries {
    public static final ResourceKey<Registry<Substance>> SUBSTANCE_REGISTRY_KEY = ResourceKey.createRegistryKey(ResourceLocation.fromNamespaceAndPath(Bittermelon.MOD_ID, "substances"));
    public static final Registry<Substance> SUBSTANCE_REGISTRY = new RegistryBuilder<>(SUBSTANCE_REGISTRY_KEY)
            .sync(true)
            .create();

    public static final ResourceKey<Registry<VerbSet>> VERB_SET_REGISTRY_KEY = ResourceKey.createRegistryKey(ResourceLocation.fromNamespaceAndPath(Bittermelon.MOD_ID, "verb_sets"));
    public static final Registry<VerbSet> VERB_SET_REGISTRY = new RegistryBuilder<>(VERB_SET_REGISTRY_KEY)
            .sync(true)
            .create();

    public static final ResourceKey<Registry<Function<Signal, Signal>>> CONDITIONS_REGISTRY_KEY = ResourceKey.createRegistryKey(ResourceLocation.fromNamespaceAndPath(Bittermelon.MOD_ID, "conditions"));
    public static final Registry<Function<Signal, Signal>> CONDITION_REGISTRY = new RegistryBuilder<>(CONDITIONS_REGISTRY_KEY)
            .sync(true)
            .create();


    @SubscribeEvent
    public static void registerRegistries(@NotNull NewRegistryEvent event) {
       event.register(SUBSTANCE_REGISTRY);
       event.register(VERB_SET_REGISTRY);
       event.register(CONDITION_REGISTRY);
    }
}
