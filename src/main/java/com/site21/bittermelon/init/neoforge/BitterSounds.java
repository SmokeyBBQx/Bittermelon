package com.site21.bittermelon.init.neoforge;

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
    public static final DeferredHolder<SoundEvent, SoundEvent> LOW_IMPACT = SOUND_EVENTS.register("misc.low_impact",
            () -> SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath(Bittermelon.MOD_ID, "misc.low_impact")));
    public static final DeferredHolder<SoundEvent, SoundEvent> SCANNER_BEEP = SOUND_EVENTS.register("misc.scanner_beep",
            () -> SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath(Bittermelon.MOD_ID, "misc.scanner_beep")));
    public static final DeferredHolder<SoundEvent, SoundEvent> BOOT_UP_TUNE = SOUND_EVENTS.register("machine.boot_up_tune",
            () -> SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath(Bittermelon.MOD_ID, "machine.boot_up_tune")));
    public static final DeferredHolder<SoundEvent, SoundEvent> TERMINAL_HUM = SOUND_EVENTS.register("machine.terminal_hum",
            () -> SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath(Bittermelon.MOD_ID, "machine.terminal_hum")));
    public static final DeferredHolder<SoundEvent, SoundEvent> CONTAINMENT_ALERT = SOUND_EVENTS.register("machine.containment_alert",
            () -> SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath(Bittermelon.MOD_ID, "machine.containment_alert")));
    public static final DeferredHolder<SoundEvent, SoundEvent> BAT_IMPACT = SOUND_EVENTS.register("misc.bat_impact",
            () -> SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath(Bittermelon.MOD_ID, "misc.bat_impact")));
    public static final DeferredHolder<SoundEvent, SoundEvent> LARGE_SLIDING_DOOR_CLOSE = SOUND_EVENTS.register("machine.large_sliding_door_close",
            () -> SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath(Bittermelon.MOD_ID, "machine.large_sliding_door_close")));
    public static final DeferredHolder<SoundEvent, SoundEvent> LARGE_SLIDING_DOOR_OPEN = SOUND_EVENTS.register("machine.large_sliding_door_open",
            () -> SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath(Bittermelon.MOD_ID, "machine.large_sliding_door_open")));
    public static final DeferredHolder<SoundEvent, SoundEvent> LARGE_SLIDING_DOOR_STUCK = SOUND_EVENTS.register("machine.large_sliding_door_stuck",
            () -> SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath(Bittermelon.MOD_ID, "machine.large_sliding_door_stuck")));
    public static final DeferredHolder<SoundEvent, SoundEvent> SCARE_1 = SOUND_EVENTS.register("horror.scare_1",
            () -> SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath(Bittermelon.MOD_ID, "horror.scare_1")));
    public static final DeferredHolder<SoundEvent, SoundEvent> SCARE_2 = SOUND_EVENTS.register("horror.scare_2",
            () -> SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath(Bittermelon.MOD_ID, "horror.scare_2")));
    public static final DeferredHolder<SoundEvent, SoundEvent> SCARE_3 = SOUND_EVENTS.register("horror.scare_3",
            () -> SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath(Bittermelon.MOD_ID, "horror.scare_3")));
    public static final DeferredHolder<SoundEvent, SoundEvent> SCARE_4 = SOUND_EVENTS.register("horror.scare_4",
            () -> SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath(Bittermelon.MOD_ID, "horror.scare_4")));
    public static final DeferredHolder<SoundEvent, SoundEvent> SPARKS = SOUND_EVENTS.register("misc.sparks",
            () -> SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath(Bittermelon.MOD_ID, "misc.sparks")));
    public static final DeferredHolder<SoundEvent, SoundEvent> MALE_COUGH = SOUND_EVENTS.register("entity.cough.male_cough",
            () -> SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath(Bittermelon.MOD_ID, "entity.cough.male_cough")));
    public static final DeferredHolder<SoundEvent, SoundEvent> FEMALE_COUGH = SOUND_EVENTS.register("entity.cough.female_cough",
            () -> SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath(Bittermelon.MOD_ID, "entity.cough.female_cough")));
    public static final DeferredHolder<SoundEvent, SoundEvent> SPLAT = SOUND_EVENTS.register("misc.splat",
            () -> SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath(Bittermelon.MOD_ID, "misc.splat")));
    public static final DeferredHolder<SoundEvent, SoundEvent> HEART_BEAT = SOUND_EVENTS.register("misc.heart_beat",
            () -> SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath(Bittermelon.MOD_ID, "misc.heart_beat")));
    public static final DeferredHolder<SoundEvent, SoundEvent> BELL_SCARE = SOUND_EVENTS.register("horror.bell_scare",
            () -> SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath(Bittermelon.MOD_ID, "horror.bell_scare")));
}
