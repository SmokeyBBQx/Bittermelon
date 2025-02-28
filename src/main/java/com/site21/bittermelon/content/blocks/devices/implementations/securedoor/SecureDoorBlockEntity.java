package com.site21.bittermelon.content.blocks.devices.implementations.securedoor;

import com.site21.bittermelon.content.blocks.devices.IElectronic;
import com.site21.bittermelon.content.blocks.devices.wiring.InputPort;
import com.site21.bittermelon.content.blocks.devices.wiring.OutputPort;
import com.site21.bittermelon.content.blocks.devices.wiring.Signal;
import com.site21.bittermelon.content.personnel.PersonnelRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

public class SecureDoorBlockEntity extends BlockEntity implements IElectronic {
    private final Map<String, OutputPort> outputPorts;
    private final Map<String, InputPort> inputPorts;
    private boolean isLocked = true;
    private List<String> requiredPrivileges = new ArrayList<>();
    private int lockTickCounter = 0;
    private int lockTickThreshold = 80;

    public SecureDoorBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState blockState) {
        super(type, pos, blockState);

        outputPorts = Map.of(
                "IS_LOCKED", new OutputPort("IS_LOCKED", this::isLocked, worldPosition)
        );

        inputPorts = Map.of(
                "TOGGLE_LOCK", new InputPort("TOGGLE_LOCK", this::toggleLocked, worldPosition),
                "SET_LOCK", new InputPort("SET_LOCK", this::setLocked, worldPosition)
        );
    }

    public void tick() {
        if (!isLocked) {
            lockTickCounter++;
            if (lockTickCounter >= lockTickThreshold) {
                setLocked(true);
            }
        }
    }

    public void scan(int id) {
        if (level == null || level.isClientSide) return;
        List<String> privileges = PersonnelRegistry.get(level).getEntry(id).getPrivileges();
        if (privileges.stream().anyMatch(privilege -> requiredPrivileges.contains(privilege))) {
            setLocked(false);
        }
    }

    private void toggleLocked(@NotNull Signal signal) {
        if (signal.asBoolean()) {
            setLocked(!isLocked);
        }
    }

    private void setLocked(@NotNull Signal signal) {
        setLocked(signal.asBoolean());
    }

    public boolean isLocked() {
        return isLocked;
    }

    public void setLocked(boolean locked) {
        if (locked != isLocked) {
            isLocked = locked;
            setChanged();
        }
    }

    public List<String> getRequiredPrivileges() {
        return requiredPrivileges;
    }

    public void addPrivilege(String privilege) {
        requiredPrivileges.add(privilege);
    }

    @Override
    public Map<String, OutputPort> getOutputPorts() {
        return outputPorts;
    }

    @Override
    public Map<String, InputPort> getInputPorts() {
        return inputPorts;
    }

    @Override
    public String getAddress() {
        return "";
    }

    @Override
    protected void saveAdditional(@NotNull CompoundTag tag, HolderLookup.@NotNull Provider registries) {
        super.saveAdditional(tag, registries);
        tag.putBoolean("isLocked", isLocked);
        saveInputPorts(tag);
        saveOutputPorts(tag);
        ListTag privilegesList = new ListTag();
        for (String privilege : requiredPrivileges) {
            CompoundTag privilegeTag = new CompoundTag();
            privilegeTag.putString("privilege", privilege);
            privilegesList.add(privilegeTag);
        }
        tag.put("privileges", privilegesList);
    }

    @Override
    public void loadAdditional(@NotNull CompoundTag tag, HolderLookup.@NotNull Provider registries) {
        super.loadAdditional(tag, registries);
        isLocked = tag.getBoolean("isLocked");
        loadInputPorts(tag, level);
        loadOutputPorts(tag, level);

        requiredPrivileges.clear();
        ListTag privilegesList = tag.getList("privileges", Tag.TAG_COMPOUND);

        for (int i = 0; i < privilegesList.size(); i++) {
            CompoundTag privilegeTag = privilegesList.getCompound(i);
            requiredPrivileges.add(privilegeTag.getString("privilege"));
        }
    }

    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    public @NotNull CompoundTag getUpdateTag(HolderLookup.@NotNull Provider registries) {
        return this.saveCustomOnly(registries);
    }

    @Override
    public void setChanged() {
        super.setChanged();
        syncToClient();
    }

    public void syncToClient() {
        if (level != null && !level.isClientSide) {
            level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), 3);
        }
    }
}
