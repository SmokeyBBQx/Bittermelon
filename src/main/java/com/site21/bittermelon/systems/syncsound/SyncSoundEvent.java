package com.site21.bittermelon.systems.syncsound;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.level.Level;
import net.neoforged.bus.api.Event;
import org.jetbrains.annotations.Nullable;

public class SyncSoundEvent extends Event {
    private final Level level;
    private final BlockPos source;
    private final SyncSoundType soundType;
    private final Component soundDescription;
    private final int radius;
    private final @Nullable SoundEvent soundEvent;

    public SyncSoundEvent(Level level, BlockPos source, SyncSoundType soundType, Component soundDescription, int radius, @Nullable SoundEvent soundEvent) {
        this.level = level;
        this.source = source;
        this.soundType = soundType;
        this.soundDescription = soundDescription;
        this.radius = radius;
        this.soundEvent = soundEvent;
    }

    public SyncSoundEvent(Level level, BlockPos source, SyncSoundType soundType, Component soundDescription, int radius) {
        this(level, source, soundType, soundDescription, radius, null);
    }

    public Level getLevel() { return level; }
    public BlockPos getSource() { return source; }
    public SyncSoundType getSoundType() { return soundType; }
    public Component getSoundDescription() { return soundDescription; }
    public double getRadius() { return radius; }
    public @Nullable SoundEvent getSoundEvent() {
        return soundEvent;
    }

    public boolean isInRange(BlockPos pos) {
        return source.distSqr(pos) <= radius * radius;
    }
}
