package com.site21.bittermelon.init;

import com.site21.bittermelon.Bittermelon;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class BitterSounds {
    public static final DeferredRegister<SoundEvent> SOUND_EVENTS = DeferredRegister.create(BuiltInRegistries.SOUND_EVENT, Bittermelon.MOD_ID);

    public static final DeferredHolder<SoundEvent, SoundEvent> TOOLBOX_OPEN = SOUND_EVENTS.register("container.toolbox_open",
            () -> SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath(Bittermelon.MOD_ID, "container.toolbox_open")));
    public static final DeferredHolder<SoundEvent, SoundEvent> TOOLBOX_CLOSE = SOUND_EVENTS.register("container.toolbox_close",
            () -> SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath(Bittermelon.MOD_ID, "container.toolbox_close")));
    public static final DeferredHolder<SoundEvent, SoundEvent> METAL_INVENTORY = SOUND_EVENTS.register("container.metal_inventory",
            () -> SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath(Bittermelon.MOD_ID, "container.metal_inventory")));
    public static final DeferredHolder<SoundEvent, SoundEvent> GHOSTLY_EXHALE = SOUND_EVENTS.register("entity.scp939.ghostly_exhale",
            () -> SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath(Bittermelon.MOD_ID, "entity.scp939.ghostly_exhale")));
    public static final DeferredHolder<SoundEvent, SoundEvent> SCALPEL = SOUND_EVENTS.register("medical.scalpel",
            () -> SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath(Bittermelon.MOD_ID, "medical.scalpel")));
    public static final DeferredHolder<SoundEvent, SoundEvent> RETRACT = SOUND_EVENTS.register("medical.retract",
            () -> SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath(Bittermelon.MOD_ID, "medical.retract")));
    public static final DeferredHolder<SoundEvent, SoundEvent> CAUTERY = SOUND_EVENTS.register("medical.cautery",
            () -> SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath(Bittermelon.MOD_ID, "medical.cautery")));
    public static final DeferredHolder<SoundEvent, SoundEvent> FALL = SOUND_EVENTS.register("entity.fall",
            () -> SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath(Bittermelon.MOD_ID, "entity.fall")));
    public static final DeferredHolder<SoundEvent, SoundEvent> WRESTLE = SOUND_EVENTS.register("entity.wrestle",
            () -> SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath(Bittermelon.MOD_ID, "entity.wrestle")));
    public static final DeferredHolder<SoundEvent, SoundEvent> STAB = SOUND_EVENTS.register("entity.stab",
            () -> SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath(Bittermelon.MOD_ID, "entity.stab")));
    public static final DeferredHolder<SoundEvent, SoundEvent> SMASH = SOUND_EVENTS.register("entity.smash",
            () -> SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath(Bittermelon.MOD_ID, "entity.smash")));
    public static final DeferredHolder<SoundEvent, SoundEvent> SLASH = SOUND_EVENTS.register("entity.slash",
            () -> SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath(Bittermelon.MOD_ID, "entity.slash")));
    public static final DeferredHolder<SoundEvent, SoundEvent> SCREAM = SOUND_EVENTS.register("entity.scp939.939_scream",
            () -> SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath(Bittermelon.MOD_ID, "entity.scp939.939_scream")));
    public static final DeferredHolder<SoundEvent, SoundEvent> BITE = SOUND_EVENTS.register("entity.bite",
            () -> SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath(Bittermelon.MOD_ID, "entity.bite")));
    public static final DeferredHolder<SoundEvent, SoundEvent> DRAG = SOUND_EVENTS.register("entity.drag",
            () -> SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath(Bittermelon.MOD_ID, "entity.drag")));
}
