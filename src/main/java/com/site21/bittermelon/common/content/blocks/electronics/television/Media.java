package com.site21.bittermelon.common.content.blocks.electronics.television;

import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;

import java.util.Optional;
import java.util.function.Supplier;

public record Media(ResourceLocation resource, Optional<Holder<SoundEvent>> soundEvent) {
}
