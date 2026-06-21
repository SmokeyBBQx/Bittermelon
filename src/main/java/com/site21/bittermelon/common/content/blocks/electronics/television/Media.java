package com.site21.bittermelon.common.content.blocks.electronics.television;

import net.minecraft.core.Holder;
import net.minecraft.resources.Identifier;
import net.minecraft.sounds.SoundEvent;

import java.util.Optional;

public record Media(Identifier resource, Optional<Holder<SoundEvent>> soundEvent) {
}
