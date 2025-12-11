package com.site21.bittermelon.common.systems.medical.compartment;

import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.util.List;
import java.util.UUID;

public class LayerData {
    private final String name;
    private final ResourceLocation slotTexture;
    private final int width, height;
    private final LayerSlot[][] grid;

    public LayerData(String name, ResourceLocation slotTexture, int width, int height) {
        this.name = name;
        this.slotTexture = slotTexture;
        this.width = width;
        this.height = height;
        this.grid = new LayerSlot[height][width];
    }

    public LayerData(String name, ResourceLocation slotTexture, int width, int height, @NotNull List<Point> shape) {
        this(name, slotTexture, width, height);
        for (Point p : shape) {
            grid[p.y][p.x] = new LayerSlot();
        }
    }

    public boolean canFit(int x, int y, @NotNull List<Point> shape) {
        for (Point p : shape) {
            int targetX = x + p.x;
            int targetY = y + p.y;
            if (targetX < 0 || targetX >= width || targetY < 0 || targetY >= height) return false;
            if (grid[targetY][targetX] == null) return false;
            if (grid[targetY][targetX].isOccupied()) return false;
        }
        return true;
    }

    public boolean attemptToPlace(int x, int y, @NotNull UUID instanceId, @NotNull List<Point> shape) {
        if (!canFit(x, y, shape)) return false;

        for (Point p : shape) {
            int targetX = x + p.x;
            int targetY = y + p.y;
            grid[targetY][targetX].setInstanceId(instanceId);
        }

        return true;
    }

    public void removeInstance(@NotNull UUID instanceId) {
        for (int y = 0; y < height; ++y) {
            for (int x = 0; x < width; ++x) {
                if (grid[y][x] != null && instanceId.equals(grid[y][x].getInstanceId())) {
                    grid[y][x].setInstanceId(null);
                }
            }
        }
    }

    public void revealSlots(int x, int y, @NotNull List<Point> shape, float visibility) {
        for (Point p : shape) {
            int targetX = x + p.x;
            int targetY = y + p.y;
            if (targetX < 0 || targetX >= width || targetY < 0 || targetY >= height) continue;
            if (grid[targetY][targetX] != null) {
                grid[targetY][targetX].setVisibility(visibility);
            }
        }
    }
}
