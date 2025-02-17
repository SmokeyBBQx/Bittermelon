package com.site21.bittermelon.content.blocks.devices.containmentalarm;

import com.site21.bittermelon.content.blocks.devices.IDeviceEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import java.util.HashSet;
import java.util.Set;

public class ContainmentAlarmBlockEntity extends BlockEntity implements IDeviceEntity {
    private final Set<String> sensors = new HashSet<>();
    private boolean isActive = false;
    private AlarmLevel currentAlarmLevel = AlarmLevel.NONE;

    public enum AlarmLevel {
        NONE,
        ALERT,
        EMERGENCY
    }

    public ContainmentAlarmBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState blockState) {
        super(type, pos, blockState);
    }


    @Override
    public String getAddress() {
        return "";
    }
}
