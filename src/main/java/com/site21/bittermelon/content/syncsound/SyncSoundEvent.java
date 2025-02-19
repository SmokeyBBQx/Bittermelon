package com.site21.bittermelon.content.syncsound;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.Level;
import net.neoforged.bus.api.Event;

import java.awt.*;

public class SyncSoundEvent extends Event {
    private final Level level;
    private final BlockPos source;
    private final SyncSoundType soundType;
    private final Component soundDescription;
    private final int radius;

    public SyncSoundEvent(Level level, BlockPos source, SyncSoundType soundType, Component soundDescription, int radius) {
        this.level = level;
        this.source = source;
        this.soundType = soundType;
        this.soundDescription = soundDescription;
        this.radius = radius;
    }

    public Level getLevel() { return level; }
    public BlockPos getSource() { return source; }
    public SyncSoundType getSoundType() { return soundType; }
    public Component getSoundDescription() { return soundDescription; }
    public double getRadius() { return radius; }

    public boolean isInRange(BlockPos pos) {
        return source.distSqr(pos) <= radius * radius;
    }
}
