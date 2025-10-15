package com.site21.bittermelon.common.systems.ai.behavior.misc;

import net.minecraft.sounds.SoundEvent;

public interface FeelsPain {
    String getPainMessage(float pain);
    SoundEvent getPainSound(float pain);
}
