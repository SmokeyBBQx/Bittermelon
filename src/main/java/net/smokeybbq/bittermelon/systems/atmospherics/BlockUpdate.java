package net.smokeybbq.bittermelon.systems.atmospherics;

import net.minecraft.core.BlockPos;
import org.jetbrains.annotations.NotNull;

public class BlockUpdate implements Comparable<BlockUpdate> {
    final BlockPos pos;
    final long updateTime;
    final float priority;

    public BlockUpdate(BlockPos pos, long updateTime, float priority) {
        this.pos = pos;
        this.updateTime = updateTime;
        this.priority = priority;
    }

    @Override
    public int compareTo(@NotNull BlockUpdate other) {
        int timeCompare = Long.compare(this.updateTime, other.updateTime);
        if (timeCompare != 0) return timeCompare;
        return Float.compare(other.priority, this.priority);
    }
}
