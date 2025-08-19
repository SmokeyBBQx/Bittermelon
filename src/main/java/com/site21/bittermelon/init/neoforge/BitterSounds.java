package com.site21.bittermelon.init.neoforge;

import com.site21.bittermelon.Bittermelon;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.jetbrains.annotations.NotNull;

public class BitterSounds {
    public static final DeferredRegister<SoundEvent> SOUND_EVENTS = DeferredRegister.create(BuiltInRegistries.SOUND_EVENT, Bittermelon.MOD_ID);

    public static final DeferredHolder<SoundEvent, SoundEvent> TOOLBOX_OPEN = register("container.toolbox_open");
    public static final DeferredHolder<SoundEvent, SoundEvent> TOOLBOX_CLOSE = register("container.toolbox_close");
    public static final DeferredHolder<SoundEvent, SoundEvent> METAL_INVENTORY = register("container.metal_inventory");
    public static final DeferredHolder<SoundEvent, SoundEvent> GHOSTLY_EXHALE = register("entity.scp939.ghostly_exhale");
    public static final DeferredHolder<SoundEvent, SoundEvent> SCALPEL = register("medical.scalpel");
    public static final DeferredHolder<SoundEvent, SoundEvent> RETRACT = register("medical.retract");
    public static final DeferredHolder<SoundEvent, SoundEvent> CAUTERY = register("medical.cautery");
    public static final DeferredHolder<SoundEvent, SoundEvent> FALL = register("entity.fall");
    public static final DeferredHolder<SoundEvent, SoundEvent> WRESTLE = register("entity.wrestle");
    public static final DeferredHolder<SoundEvent, SoundEvent> STAB = register("entity.stab");
    public static final DeferredHolder<SoundEvent, SoundEvent> SMASH = register("entity.smash");
    public static final DeferredHolder<SoundEvent, SoundEvent> SLASH = register("entity.slash");
    public static final DeferredHolder<SoundEvent, SoundEvent> SCREAM = register("entity.scp939.939_scream");
    public static final DeferredHolder<SoundEvent, SoundEvent> BITE = register("entity.bite");
    public static final DeferredHolder<SoundEvent, SoundEvent> DRAG = register("entity.drag");
    public static final DeferredHolder<SoundEvent, SoundEvent> LOW_IMPACT = register("misc.low_impact");
    public static final DeferredHolder<SoundEvent, SoundEvent> SCANNER_BEEP = register("misc.scanner_beep");
    public static final DeferredHolder<SoundEvent, SoundEvent> BOOT_UP_TUNE = register("machine.boot_up_tune");
    public static final DeferredHolder<SoundEvent, SoundEvent> TERMINAL_HUM = register("machine.terminal_hum");
    public static final DeferredHolder<SoundEvent, SoundEvent> CONTAINMENT_ALERT = register("machine.containment_alert");
    public static final DeferredHolder<SoundEvent, SoundEvent> BAT_IMPACT = register("misc.bat_impact");
    public static final DeferredHolder<SoundEvent, SoundEvent> LARGE_SLIDING_DOOR_CLOSE = register("machine.large_sliding_door_close");
    public static final DeferredHolder<SoundEvent, SoundEvent> LARGE_SLIDING_DOOR_OPEN = register("machine.large_sliding_door_open");
    public static final DeferredHolder<SoundEvent, SoundEvent> LARGE_SLIDING_DOOR_STUCK = register("machine.large_sliding_door_stuck");
    public static final DeferredHolder<SoundEvent, SoundEvent> SCARE_1 = register("horror.scare_1");
    public static final DeferredHolder<SoundEvent, SoundEvent> SCARE_2 = register("horror.scare_2");
    public static final DeferredHolder<SoundEvent, SoundEvent> SCARE_3 = register("horror.scare_3");
    public static final DeferredHolder<SoundEvent, SoundEvent> SCARE_4 = register("horror.scare_4");
    public static final DeferredHolder<SoundEvent, SoundEvent> SPARKS = register("misc.sparks");
    public static final DeferredHolder<SoundEvent, SoundEvent> MALE_COUGH = register("entity.cough.male_cough");
    public static final DeferredHolder<SoundEvent, SoundEvent> FEMALE_COUGH = register("entity.cough.female_cough");
    public static final DeferredHolder<SoundEvent, SoundEvent> SPLAT = register("misc.splat");
    public static final DeferredHolder<SoundEvent, SoundEvent> HEART_BEAT = register("misc.heart_beat");
    public static final DeferredHolder<SoundEvent, SoundEvent> BELL_SCARE = register("horror.bell_scare");
    public static final DeferredHolder<SoundEvent, SoundEvent> SPLATTER = register("misc.splatter");
    public static final DeferredHolder<SoundEvent, SoundEvent> TASER = register("item.taser");
    public static final DeferredHolder<SoundEvent, SoundEvent> TASER_RELOAD = register("item.taser_reload");
    public static final DeferredHolder<SoundEvent, SoundEvent> TASER_SHOOT = register("item.taser_shoot");
    public static final DeferredHolder<SoundEvent, SoundEvent> SLIP = register("misc.slip");
    public static final DeferredHolder<SoundEvent, SoundEvent> SOGGY = register("misc.soggy");
    public static final DeferredHolder<SoundEvent, SoundEvent> SNORT = register("misc.snort");
    public static final DeferredHolder<SoundEvent, SoundEvent> COMPUTER_END = register("machine.computer_end");
    public static final DeferredHolder<SoundEvent, SoundEvent> COMPUTER_MID1 = register("machine.computer_mid1");
    public static final DeferredHolder<SoundEvent, SoundEvent> COMPUTER_MID2 = register("machine.computer_mid2");
    public static final DeferredHolder<SoundEvent, SoundEvent> COMPUTER_START = register("machine.computer_start");
    public static final DeferredHolder<SoundEvent, SoundEvent> MOUSE_CLICK = register("machine.mouse_click");
    public static final DeferredHolder<SoundEvent, SoundEvent> AMBIENT_NOISES = register("ambient.ambient_noises");
    public static final DeferredHolder<SoundEvent, SoundEvent> FACILITY_AMBIENCE = register("ambient.facility_ambience");
    public static final DeferredHolder<SoundEvent, SoundEvent> PULSING = register("ambient.pulsing");
    public static final DeferredHolder<SoundEvent, SoundEvent> BANJO = register("music.banjo");


    private static @NotNull DeferredHolder<SoundEvent, SoundEvent> register(String name) {
        return SOUND_EVENTS.register(name, () ->
                SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath(Bittermelon.MOD_ID, name)));
    }
}
