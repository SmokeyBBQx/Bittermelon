package net.smokeybbq.bittermelon.init;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.registries.NewRegistryEvent;
import net.minecraftforge.registries.RegistryBuilder;
import net.smokeybbq.bittermelon.Bittermelon;
import net.smokeybbq.bittermelon.systems.substances.Substance;

public class ModRegistries {
    public static final ResourceKey<Registry<Substance>> SUBSTANCE_REGISTRY = ResourceKey.createRegistryKey(
            new ResourceLocation(Bittermelon.MODID, "substances"));

    public static void registerRegistries(NewRegistryEvent event) {
        event.create(new RegistryBuilder<Substance>().setName(SUBSTANCE_REGISTRY.location()));
    }
}
