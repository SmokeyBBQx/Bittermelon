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
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.site21.bittermelon.init.neoforge.BitterBlockEntities.SECURE_DOOR_BLOCK_ENTITY;

public class SecureDoorBlockEntity extends BlockEntity implements IElectronic {
    private final Map<String, OutputPort> outputPorts;
    private final Map<String, InputPort> inputPorts;
    private boolean isLocked = true;
    private final List<String> requiredPrivileges = new ArrayList<>();
    private int lockTickCounter = 0;
    private static final int LOCK_TICK_THRESHOLD = 80;

    public SecureDoorBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState blockState) {
        super(type, pos, blockState);

        outputPorts = Map.of(
                "IS_LOCKED", new OutputPort("IS_LOCKED", this::isLocked, worldPosition),
                "MOTORS_ACTIVE", new OutputPort("MOTORS_ACTIVE", null, worldPosition)
        );

        inputPorts = Map.of(
                "TOGGLE_LOCK", new InputPort("TOGGLE_LOCK", this::toggleLocked, worldPosition),
                "SET_LOCK", new InputPort("SET_LOCK", this::setLocked, worldPosition),
                "TOGGLE_MOTORS", new InputPort("TOGGLE_MOTORS", this::toggleMotors, worldPosition),
                "SET_MOTORS", new InputPort("SET_MOTORS", this::setMotors, worldPosition)
        );
    }

    public SecureDoorBlockEntity(BlockPos pos, BlockState state) {
        this(SECURE_DOOR_BLOCK_ENTITY.get(), pos, state);
    }

    public void tick() {
        if (!isLocked) {
            lockTickCounter++;
            if (lockTickCounter >= LOCK_TICK_THRESHOLD) {
                lockTickCounter = 0;
                setLocked(true);
                setMotors(false);
                if (level == null) return;
                level.playSound(null, worldPosition, SoundEvents.NOTE_BLOCK_IRON_XYLOPHONE.value(), SoundSource.BLOCKS);
            }
        }
    }

    private void toggleMotors(@NotNull Signal signal) {
        if (signal.asBoolean()) {
            if (level == null) return;
            BlockState blockState = level.getBlockState(worldPosition);
            if (blockState.getBlock() instanceof SecureDoorBlock secureDoorBlock) {
                setMotors(!secureDoorBlock.isOpen(blockState));
            }
        }
    }

    private void setMotors(@NotNull Signal signal) {
        setMotors(signal.asBoolean());
    }

    public void setMotors(boolean open) {
        if (level == null) return;
        BlockState blockState = level.getBlockState(worldPosition);
        if (blockState.getBlock() instanceof SecureDoorBlock secureDoorBlock) {
            if (isLocked && !secureDoorBlock.isOpen(blockState)) return;
            secureDoorBlock.setOpen(null, level, blockState, worldPosition, open);

            InputPort connectedPort = findOutputPort("MOTORS_ACTIVE").connectedPort;
            if (connectedPort != null) {
                connectedPort.receive(new Signal(true));
            }
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
            lockTickCounter = 0;
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
