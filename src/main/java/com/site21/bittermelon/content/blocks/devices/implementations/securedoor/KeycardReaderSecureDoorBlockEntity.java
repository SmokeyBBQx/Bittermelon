package com.site21.bittermelon.content.blocks.devices.implementations.securedoor;

import com.site21.bittermelon.content.personnel.privilege.PrivilegeOwner;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

import static com.site21.bittermelon.init.neoforge.BitterBlockEntities.KEYCARD_READER_SECURE_DOOR_BLOCK_ENTITY;

public class KeycardReaderSecureDoorBlockEntity extends SecureDoorBlockEntity implements PrivilegeOwner {
    private final Map<String, Boolean> privileges;

    public KeycardReaderSecureDoorBlockEntity(BlockPos pos, BlockState blockState) {
        super(KEYCARD_READER_SECURE_DOOR_BLOCK_ENTITY.get(), pos, blockState);
        privileges = new HashMap<>();
    }

    public Map<String, Boolean> getPrivileges() {
        return privileges;
    }

    @Override
    public String getName() {
        return getAddress();
    }

    @Override
    public boolean canAccess() {
        return getBlockState().getValue(SecureDoorBlock.HALF).equals(DoubleBlockHalf.UPPER);
    }

    @Override
    protected void saveAdditional(@NotNull ValueOutput output) {
        super.saveAdditional(output);

        serializePrivileges(output);
    }

    @Override
    protected void loadAdditional(@NotNull ValueInput input) {
        super.loadAdditional(input);

        deserializePrivileges(input);
    }
}
