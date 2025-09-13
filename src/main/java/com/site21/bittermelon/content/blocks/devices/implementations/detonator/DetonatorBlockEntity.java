package com.site21.bittermelon.content.blocks.devices.implementations.detonator;

import com.site21.bittermelon.content.blocks.devices.ElectronicDevice;
import com.site21.bittermelon.content.blocks.devices.ElectronicBlockEntity;
import com.site21.bittermelon.content.blocks.devices.wiring.InputPort;
import com.site21.bittermelon.content.blocks.devices.wiring.Signal;
import com.site21.bittermelon.content.items.payload.Payload;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

import static com.site21.bittermelon.init.neoforge.BitterBlockEntities.DETONATOR_BLOCK_ENTITY;

public class DetonatorBlockEntity extends ElectronicBlockEntity implements ElectronicDevice {
    private ItemStack payload = new ItemStack(Items.FEATHER);
    private final Map<String, InputPort> inputPorts;

    public DetonatorBlockEntity(BlockPos pos, BlockState blockState) {
        super(DETONATOR_BLOCK_ENTITY.get(), pos, blockState);
        inputPorts = Map.of(
                "DETONATE", new InputPort("DETONATE", this::detonate, worldPosition)
        );
    }

    public ItemStack getPayload() {
        return payload;
    }

    public void setPayload(ItemStack payload) {
        this.payload = payload;
        setChanged();
    }

    public void detonate(@NotNull Signal signal) {
        if (signal.asBoolean()) {
            detonate();
        }
    }

    public void detonate() {
        if (this.isRemoved() || this.level == null) {
            return;
        }

        if (payload.getItem() instanceof Payload payloadItem) {
            payloadItem.detonate(level, worldPosition);
        }

        if (level == null || level.isClientSide) return;
        level.explode(null, worldPosition.getX(), worldPosition.getY(), worldPosition.getZ(),
                2.0f,
                Level.ExplosionInteraction.TNT);
        level.setBlockAndUpdate(worldPosition, Blocks.AIR.defaultBlockState());
        setRemoved();
    }

    @Override
    protected void saveAdditional(@NotNull CompoundTag tag, HolderLookup.@NotNull Provider registries) {
        super.saveAdditional(tag, registries);
        tag.put("payload", payload.save(registries));
        saveInputPorts(tag);
    }

    @Override
    public void loadAdditional(@NotNull CompoundTag tag, HolderLookup.@NotNull Provider registries) {
        super.loadAdditional(tag, registries);
        ItemStack.parse(registries, tag.get("payload")).ifPresent(stack -> payload = stack);
        loadInputPorts(tag);
    }

    @Override
    public Map<String, InputPort> getInputPorts() {
        return inputPorts;
    }

    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }
}
