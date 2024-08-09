package net.smokeybbq.bittermelon.init;

import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.*;
import net.smokeybbq.bittermelon.Bittermelon;
import net.smokeybbq.bittermelon.substances.Substance;

import java.util.Optional;

import static net.smokeybbq.bittermelon.init.BlockInit.BLOCKS;

public class SubstanceInit {
    public static final DeferredRegister<Substance> SUBSTANCES = DeferredRegister.create(ModRegistries.SUBSTANCE_REGISTRY, Bittermelon.MODID);
    public static final RegistryObject<Substance> BLOOD = SUBSTANCES.register("blood", () ->
            new Substance("blood", 0xFF990000));

    public static final RegistryObject<Substance> COFFEE = SUBSTANCES.register("coffee", () ->
            new Substance("coffee", 0xFFA67B5B));

    public static final RegistryObject<Substance> WATER = SUBSTANCES.register("water", () ->
            new Substance("water", 0xFFAAD5DB));

    public static final RegistryObject<Substance> VOMIT = SUBSTANCES.register("vomit", () ->
            new Substance("vomit", 0xFFC7C10C));

    public static final RegistryObject<Substance> RANCH = SUBSTANCES.register("ranch", () ->
            new Substance("ranch", 0xFFFEFFF4));

    public static final RegistryObject<Substance> URINE = SUBSTANCES.register("urine", () ->
            new Substance("urine", 0xFFFFDC65));

    public static IForgeRegistry<Substance> getSubstanceRegistry() {
        return RegistryManager.ACTIVE.getRegistry(new ResourceLocation("bittermelon", "substances"));
    }

    public static Substance getSubstance(String name) {
        IForgeRegistry<Substance> registry = getSubstanceRegistry();
        ResourceLocation location = new ResourceLocation("bittermelon", name);
        return registry.getValue(location);
    }
}
