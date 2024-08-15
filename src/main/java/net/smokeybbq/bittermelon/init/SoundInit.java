package net.smokeybbq.bittermelon.init;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.smokeybbq.bittermelon.Bittermelon;

public class SoundInit {
    public static final DeferredRegister<SoundEvent> SOUND_EVENTS = DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, Bittermelon.MODID);

    public static final RegistryObject<SoundEvent> TOOLBOX_OPEN = SOUND_EVENTS.register("toolbox_open",
            () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(Bittermelon.MODID, "toolbox_open")));
    public static final RegistryObject<SoundEvent> TOOLBOX_CLOSE = SOUND_EVENTS.register("toolbox_close",
            () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(Bittermelon.MODID, "toolbox_close")));
    public static final RegistryObject<SoundEvent> METAL_INVENTORY = SOUND_EVENTS.register("metal_inventory",
            () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(Bittermelon.MODID, "metal_inventory")));
}
