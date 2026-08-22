package com.site21.bittermelon.common.content.entities.scp939.lure;

import net.minecraft.sounds.SoundEvent;

public record LurePool(String[][] dialogue, SoundEvent[] sounds, int interval, int additionalRandomInterval) {
}
