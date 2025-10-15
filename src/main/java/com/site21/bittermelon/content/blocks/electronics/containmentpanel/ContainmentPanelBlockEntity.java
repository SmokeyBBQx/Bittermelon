package com.site21.bittermelon.content.blocks.electronics.containmentpanel;

import com.site21.bittermelon.content.blocks.base.structuralblock.StructuralBlockEntity;
import com.site21.bittermelon.systems.electronics.ContainmentDevice;
import com.site21.bittermelon.systems.electronics.ElectronicBlockEntity;
import com.site21.bittermelon.content.blocks.dirtyfloor.DirtyFloorBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import static com.site21.bittermelon.content.blocks.dirtyfloor.DirtyFloorBlock.DIRTINESS;
import static com.site21.bittermelon.init.neoforge.BitterBlockEntities.CONTAINMENT_PANEL_BLOCK_ENTITY;

public class ContainmentPanelBlockEntity extends ElectronicBlockEntity {
    private String name = "";
    private float containmentScore;
    private float securityScore;
    private float researchScore;
    private float maintenanceScore;
    private float caretakingScore;

    private long securityLastVisited = 0;
    private long timeSinceLastSecurity = 0;

    private int updateTimer = 0;
    private static final int UPDATE_TIME = 40;

    private int researchQuota = 0;
    private float researchPoints = 0;

    private int caretakingQuota = 0;
    private float caretakingPoints = 0;

    private int securityPriority = 1000;

    private BoundingBox boundingBox;
    private final List<ContainmentDevice> containmentDevices;

    public ContainmentPanelBlockEntity(BlockPos pos, BlockState blockState) {
        super(CONTAINMENT_PANEL_BLOCK_ENTITY.get(), pos, blockState);
         boundingBox = new BoundingBox(pos);
         containmentDevices = new ArrayList<>();
    }

    public void tick() {
        updateTimer++;
        if (updateTimer >= UPDATE_TIME) {
            updateScores();
            updateTimer = 0;
            setChanged();
        }
    }

    private void updateScores() {
        updateContainmentStat();
        checkForSecurity();

        researchPoints -= 0.001f;

        if (researchQuota > 0) {
            setResearchScore((researchPoints / researchQuota) * 100);
        } else {
            setResearchScore(100);
        }



        if (caretakingQuota > 0) {
            setCaretakingScore((caretakingPoints / caretakingQuota) * 100);
        } else {
            setCaretakingScore(100);
        }

        setSecurityScore(100 - (float) timeSinceLastSecurity / securityPriority);
        setMaintenanceScore(100 - (checkContainmentIntegrity() + checkSanitation()));
    }

    public String getName() {
        return name;
    }

    public float getContainmentScore() {
        return containmentScore;
    }

    public float getSecurityScore() {
        return securityScore;
    }

    public float getResearchScore() {
        return researchScore;
    }

    public float getMaintenanceScore() {
        return maintenanceScore;
    }

    public float getCaretakingScore() {
        return caretakingScore;
    }

    public void setContainmentScore(float value) {
        containmentScore = Math.max(0, Math.min(100, value));
    }

    public void setMaintenanceScore(float value) {
        maintenanceScore = Math.max(0, Math.min(100, value));
    }

    public void setSecurityScore(float value) {
        securityScore = Math.max(0, Math.min(100, value));
    }

    public void setSecurityPriority(int securityPriority) {
        this.securityPriority = securityPriority;
    }

    public void setResearchScore(float value) {
        this.researchScore = Math.max(0, Math.min(100, value));;
    }

    public void setResearchPoints(float value) {
        this.researchPoints = Math.max(0, value);
    }

    public void setCaretakingScore(float caretakingScore) {
        this.caretakingScore = caretakingScore;
    }

    public void setCaretakingQuota(int quota) {
        this.caretakingQuota = Math.max(0, quota);;
    }

    public void setCaretakingPoints(float points) {
        this.caretakingPoints = Math.max(0, points);;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setResearchQuota(int quota) {
        this.researchQuota = Math.max(0, quota);
    }

    public void modifyResearchPoints(int points) {
       setResearchPoints(researchPoints + points);
    }

    public void modifySecurityScore(float value) {
        setSecurityScore(securityScore + value);
    }

    public void modifyCaretakingPoints(float points) {
        setCaretakingPoints(caretakingPoints + points);
    }


    public void setCorner1(@NotNull BlockPos pos) {
        setBoundingBox(new BoundingBox(pos.getX(), pos.getY(), pos.getZ(), boundingBox.maxX(), boundingBox.maxY(), boundingBox.maxZ()));
    }

    public void setCorner2(@NotNull BlockPos pos) {
        setBoundingBox(new BoundingBox(boundingBox.minX(), boundingBox.minY(), boundingBox.minZ(), pos.getX(), pos.getY(), pos.getZ()));
    }

    public BoundingBox getBoundingBox() {
        return boundingBox;
    }

    public void setBoundingBox(BoundingBox boundingBox) {
        this.boundingBox = boundingBox;
    }

    private void updateContainmentStat() {
        setContainmentScore((securityScore + researchScore + maintenanceScore + caretakingScore) / 4);
    }

    private float checkContainmentIntegrity() {
        if (level == null) return 0;

        float damage = 0;

        for (BlockPos pos : BlockPos.betweenClosed(boundingBox.minX(), boundingBox.minY(), boundingBox.minZ(), boundingBox.maxX(), boundingBox.maxY(), boundingBox.maxZ())) {
            if (level.getBlockEntity(pos) instanceof StructuralBlockEntity structuralBlockEntity) {
                damage += structuralBlockEntity.getBreakProgress() * 10;
            }
        }

        return damage;
    }

    private int checkSanitation() {
        if (level == null) return 0;

        int count = 0;

        for (BlockPos pos : BlockPos.betweenClosed(boundingBox.minX(), boundingBox.minY(), boundingBox.minZ(), boundingBox.maxX(), boundingBox.maxY(), boundingBox.maxZ())) {
            BlockState state = level.getBlockState(pos);

            if (state.getBlock() instanceof DirtyFloorBlock) {
                count += 1 + state.getValue(DIRTINESS);
            }
        }

        return count;
    }

    private void checkForSecurity() {
        if (level == null) return;

        // TODO: Replace this with scannables checker

        for (Player player : level.players()) {
            if (boundingBoxContains(player.getX(), player.getY(), player.getZ())) {
                // TODO: Add security condition
                securityLastVisited = level.getGameTime();
                break;
            }
        }

        timeSinceLastSecurity = level.getGameTime() - securityLastVisited;
    }

    private boolean boundingBoxContains(double x, double y, double z) {
        return x >= boundingBox.minX() && x < boundingBox.maxX() && y >= boundingBox.minY() && y < boundingBox.maxY()
                && z >= boundingBox.minZ() && z < boundingBox.maxZ();
    }

    @Override
    protected void saveAdditional(@NotNull ValueOutput output) {
        super.saveAdditional(output);

        output.putString("name", name);
        output.putFloat("containmentScore", containmentScore);
        output.putFloat("securityScore", securityScore);
        output.putFloat("researchScore", researchScore);
        output.putFloat("maintenanceScore", maintenanceScore);
        output.putFloat("caretakingScore", caretakingScore);
        output.putInt("boundingBoxMinX", boundingBox.minX());
        output.putInt("boundingBoxMinY", boundingBox.minY());
        output.putInt("boundingBoxMinZ", boundingBox.minZ());
        output.putInt("boundingBoxMaxX", boundingBox.maxX());
        output.putInt("boundingBoxMaxY", boundingBox.maxY());
        output.putInt("boundingBoxMaxZ", boundingBox.maxZ());
    }

    @Override
    protected void loadAdditional(@NotNull ValueInput input) {
        super.loadAdditional(input);

        name = input.getStringOr("name", "");
        containmentScore = input.getFloatOr("containmentScore", 0.0f);
        securityScore = input.getFloatOr("securityScore", 0.0f);
        researchScore = input.getFloatOr("researchScore", 0.0f);
        maintenanceScore = input.getFloatOr("maintenanceScore", 0.0f);
        caretakingScore = input.getFloatOr("caretakingScore", 0.0f);

        int minX = input.getIntOr("boundingBoxMinX", 0);
        int minY = input.getIntOr("boundingBoxMinY", 0);
        int minZ = input.getIntOr("boundingBoxMinZ", 0);
        int maxX = input.getIntOr("boundingBoxMaxX", 0);
        int maxY = input.getIntOr("boundingBoxMaxY", 0);
        int maxZ = input.getIntOr("boundingBoxMaxZ", 0);
        boundingBox = new BoundingBox(minX, minY, minZ, maxX, maxY, maxZ);
    }

}
