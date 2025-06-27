package com.site21.bittermelon.content.blocks.devices.implementations.securedoor;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

import static com.site21.bittermelon.init.neoforge.BitterBlockEntities.KEYCARD_READER_SECURE_DOOR_BLOCK_ENTITY;

public class KeycardReaderSecureDoorBlockEntity extends SecureDoorBlockEntity {
    public KeycardReaderSecureDoorBlockEntity(BlockPos pos, BlockState blockState) {
        super(KEYCARD_READER_SECURE_DOOR_BLOCK_ENTITY.get(), pos, blockState);
    }
}
