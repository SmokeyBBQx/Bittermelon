package com.site21.bittermelon.common.content.blocks.electronics.securedoor;

import com.site21.bittermelon.common.systems.personnel.privilege.PrivilegeOwner;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
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
    protected void saveAdditional(@NotNull CompoundTag tag, HolderLookup.@NotNull Provider registries) {
        super.saveAdditional(tag, registries);
        if (getBlockState().getValue(SecureDoorBlock.HALF).equals(DoubleBlockHalf.LOWER)) return;

        serializePrivileges(tag);
    }

    @Override
    public void loadAdditional(@NotNull CompoundTag tag, HolderLookup.@NotNull Provider registries) {
        super.loadAdditional(tag, registries);
        if (getBlockState().getValue(SecureDoorBlock.HALF).equals(DoubleBlockHalf.LOWER)) return;

        deserializePrivileges(tag);
    }
}
