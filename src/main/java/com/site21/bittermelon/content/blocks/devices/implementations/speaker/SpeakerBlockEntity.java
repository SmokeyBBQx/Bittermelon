package com.site21.bittermelon.content.blocks.devices.implementations.speaker;

import com.site21.bittermelon.content.blocks.devices.IDeviceEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class SpeakerBlockEntity extends BlockEntity implements IDeviceEntity {
    private String address = "";

    public SpeakerBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState blockState) {
        super(type, pos, blockState);
        address = generateAddress("SPE");
    }

    @Override
    public String getAddress() {
        return address;
    }
}
