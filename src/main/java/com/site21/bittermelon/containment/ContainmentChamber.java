package com.site21.bittermelon.containment;

import com.site21.bittermelon.blocks.DirtyFloorBlock;
import com.site21.bittermelon.blocks.blockentities.StructuralBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.BoundingBox;

import static com.site21.bittermelon.blocks.DirtyFloorBlock.DIRTINESS;

public class ContainmentChamber {
    private float containmentGrade;
    private float securityGrade;
    private float researchGrade;
    private float maintenanceGrade;
    private float caretakingGrade;

    private static final int EVALUATE_THRESHOLD = 1;
    private final Level level;
    private BoundingBox boundingBox;

    public ContainmentChamber(Level level) {
        this.level = level;
    }

    public void update() {

    }

    public BoundingBox getBoundingBox() {
        return boundingBox;
    }

    public void setBoundingBox(BoundingBox boundingBox) {
        this.boundingBox = boundingBox;
    }

    private float checkContainmentIntegrity() {
        int count = 0;
        float damage = 0;

        for (BlockPos pos : BlockPos.betweenClosed(boundingBox.minX(), boundingBox.minY(), boundingBox.minZ(), boundingBox.maxX(), boundingBox.maxY(), boundingBox.maxZ())) {
            if (level.getBlockEntity(pos) instanceof StructuralBlockEntity structuralBlockEntity) {
                damage += structuralBlockEntity.getBreakProgress();
                count++;
            }
        }

        return count == 0 ? 0 : damage / count;
    }

    private int checkSanitation() {
        int count = 0;

        for (BlockPos pos : BlockPos.betweenClosed(boundingBox.minX(), boundingBox.minY(), boundingBox.minZ(), boundingBox.maxX(), boundingBox.maxY(), boundingBox.maxZ())) {
            BlockState state = level.getBlockState(pos);

            if (state.getBlock() instanceof DirtyFloorBlock) {
                count += 1 + state.getValue(DIRTINESS);
            }
        }

        return count;
    }

    private int checkForSecurity() {
        int count = 0;

        for (Player player : level.players()) {
            if (boundingBoxContains(player.getX(), player.getY(), player.getZ())) {
                // TODO: Add security check
                count++;
            }
        }

        return count;
    }

    private boolean boundingBoxContains(double x, double y, double z) {
        return x >= boundingBox.minX() && x < boundingBox.maxX() && y >= boundingBox.minY() && y < boundingBox.maxY()
                && z >= boundingBox.minZ() && z < boundingBox.maxZ();
    }
}
