package com.site21.bittermelon.init.neoforge;

import com.site21.bittermelon.Bittermelon;
import com.site21.bittermelon.common.content.blocks.electronics.television.Media;
import com.site21.bittermelon.common.systems.chat.VerbSet;
import com.site21.bittermelon.common.systems.electronics.wiring.Signal;
import com.site21.bittermelon.common.systems.medical.anatomy.Anatomy;
import com.site21.bittermelon.common.systems.medical.compartment.Compartment;
import com.site21.bittermelon.common.systems.medical.drug.Drug;
import com.site21.bittermelon.common.systems.roles.Role;
import com.site21.bittermelon.common.systems.substance.Substance;
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
            .create();

    public static final ResourceKey<Registry<Function<Float, Function<Signal, Signal>>>> LOGICAL_OPERATORS_REGISTRY_KEY = ResourceKey.createRegistryKey(ResourceLocation.fromNamespaceAndPath(Bittermelon.MOD_ID, "conditions"));
    public static final Registry<Function<Float, Function<Signal, Signal>>> LOGICAL_OPERATORS_REGISTRY = new RegistryBuilder<>(LOGICAL_OPERATORS_REGISTRY_KEY)
            .sync(true)
            .create();

    public static final ResourceKey<Registry<Compartment>> COMPARTMENT_REGISTRY_KEY = ResourceKey.createRegistryKey(ResourceLocation.fromNamespaceAndPath(Bittermelon.MOD_ID, "compartments"));
    public static final Registry<Compartment> COMPARTMENT_REGISTRY = new RegistryBuilder<>(COMPARTMENT_REGISTRY_KEY)
            .sync(true)
            .create();

    public static final ResourceKey<Registry<Drug>> DRUG_REGISTRY_KEY = ResourceKey.createRegistryKey(ResourceLocation.fromNamespaceAndPath(Bittermelon.MOD_ID, "drugs"));
    public static final Registry<Drug> DRUG_REGISTRY = new RegistryBuilder<>(DRUG_REGISTRY_KEY)
            .create();

    public static final ResourceKey<Registry<Role>> ROLE_REGISTRY_KEY = ResourceKey.createRegistryKey(ResourceLocation.fromNamespaceAndPath(Bittermelon.MOD_ID, "roles"));
    public static final Registry<Role> ROLE_REGISTRY = new RegistryBuilder<>(ROLE_REGISTRY_KEY)
            .sync(true)
            .create();

    public static final ResourceKey<Registry<Media>> MEDIA_REGISTRY_KEY = ResourceKey.createRegistryKey(ResourceLocation.fromNamespaceAndPath(Bittermelon.MOD_ID, "media"));
    public static final Registry<Media> MEDIA_REGISTRY = new RegistryBuilder<>(MEDIA_REGISTRY_KEY)
            .sync(true)
            .create();

    public static final ResourceKey<Registry<Anatomy>> ANATOMY_REGISTRY_KEY = ResourceKey.createRegistryKey(Bittermelon.resource("anatomies"));
    public static final Registry<Anatomy> ANATOMY_REGISTRY = new RegistryBuilder<>(ANATOMY_REGISTRY_KEY)
            .sync(true)
            .create();

    @SubscribeEvent
    public static void registerRegistries(@NotNull NewRegistryEvent event) {
       event.register(SUBSTANCE_REGISTRY);
       event.register(VERB_SET_REGISTRY);
       event.register(LOGICAL_OPERATORS_REGISTRY);
       event.register(COMPARTMENT_REGISTRY);
       event.register(DRUG_REGISTRY);
       event.register(ROLE_REGISTRY);
       event.register(MEDIA_REGISTRY);
       event.register(ANATOMY_REGISTRY);
    }
}
