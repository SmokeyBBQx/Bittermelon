package com.site21.bittermelon.content.blocks.devices.implementations.securedoor;

import com.site21.bittermelon.content.personnel.privilege.PrivilegeOwner;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

import static com.site21.bittermelon.init.neoforge.BitterBlockEntities.KEYCARD_READER_SECURE_DOOR_BLOCK_ENTITY;

public class KeycardReaderSecureDoorBlockEntity extends SecureDoorBlockEntity implements PrivilegeOwner {
    private final Map<String, Boolean> privileges = new HashMap<>();

    public KeycardReaderSecureDoorBlockEntity(BlockPos pos, BlockState blockState) {
        super(KEYCARD_READER_SECURE_DOOR_BLOCK_ENTITY.get(), pos, blockState);
    }

    public Map<String, Boolean> getPrivileges() {
        return privileges;
    }

    @Override
    public String getName() {
        return getAddress();
    }

    @Override
    protected void saveAdditional(@NotNull CompoundTag tag, HolderLookup.@NotNull Provider registries) {
        super.saveAdditional(tag, registries);
        CompoundTag privilegesTag = new CompoundTag();
        for (Map.Entry<String, Boolean> entry : privileges.entrySet()) {
            privilegesTag.putBoolean(entry.getKey(), entry.getValue());
        }
        tag.put("privileges", privilegesTag);
    }

    @Override
    public void loadAdditional(@NotNull CompoundTag tag, HolderLookup.@NotNull Provider registries) {
        super.loadAdditional(tag, registries);

        privileges.clear();
        CompoundTag privilegesTag = tag.getCompound("privileges");

        for (String key : privilegesTag.getAllKeys()) {
            privileges.put(key, privilegesTag.getBoolean(key));
        }
    }
}
