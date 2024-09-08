package net.smokeybbq.bittermelon.systems.atmospherics;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.smokeybbq.bittermelon.util.ModLogger;

import java.util.BitSet;

public class AtmosChunk {
    private static final int SIZE = 16;
    private static final int HEIGHT = 384;
    private final AtmosCell[][][] cells = new AtmosCell[SIZE][HEIGHT][SIZE];
    private final BitSet activeCells = new BitSet(SIZE * HEIGHT * SIZE);

    public AtmosChunk() {

    }

    public void setActive(int x, int y, int z, boolean active) {
        if (outOfBounds(x, y, z)) {
            throw new IllegalArgumentException("Coordinates out of bounds: x=" + x + ", y=" + y + ", z=" + z);
        }

        int index = calculateIndex(x, y, z);
        if (active) {
            activeCells.set(index);
            if (cells[x][y][z] == null) {
                cells[x][y][z] = new AtmosCell();
            }
        } else {
            activeCells.clear(index);
        }
    }

    public boolean isActive(int x, int y, int z) {
        if (outOfBounds(x, y, z)) {return false;}

        return activeCells.get(calculateIndex(x, y, z));
    }

    public AtmosCell getCell(int x, int y, int z) {
        if (outOfBounds(x, y, z)) {return null;}

        return cells[x][y][z];
    }

    private boolean outOfBounds(int x, int y, int z) {
//        return (x < 0 || x >= SIZE || y < 0 || y >= HEIGHT || z < 0 || z >= SIZE);
        return false;
    }

    private int calculateIndex(int x, int y, int z) {
        return (y * SIZE * SIZE) + (z * SIZE) + x;
    }

    public boolean containsCell(AtmosCell cell) {
        for (AtmosCell[][] plane : cells) {
            for (AtmosCell[] column : plane) {
                for (AtmosCell currentCell : column) {
                    if (currentCell == cell) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public BlockPos getCellPosition(AtmosCell cell) {
        for (int x = 0; x < SIZE; x++) {
            for (int y = 0; y < HEIGHT; y++) {
                for (int z = 0; z < SIZE; z++) {
                    if (cells[x][y][z] == cell) {
                        return new BlockPos(x, y, z);
                    }
                }
            }
        }
        return null;
    }

    public Tag save(CompoundTag compoundTag) {
        return compoundTag;
    }
}
