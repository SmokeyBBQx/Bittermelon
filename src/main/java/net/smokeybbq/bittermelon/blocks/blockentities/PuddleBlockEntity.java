package net.smokeybbq.bittermelon.blocks.blockentities;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.Connection;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.smokeybbq.bittermelon.init.BlockEntityInit;
import net.smokeybbq.bittermelon.substances.Substance;
import net.smokeybbq.bittermelon.util.ColorUtil;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.stream.Collectors;

public class PuddleBlockEntity extends BlockEntity {
    private final Map<Substance, Integer> substances = new HashMap<>();
    private int cachedColor = -1;

    public PuddleBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(BlockEntityInit.PUDDLE_BLOCK_ENTITY.get(), pPos, pBlockState);
    }

    //    public void tick() {
//    }
    public void updateSubstance(Substance substance, int amount) {
        boolean updated = false;
        for (Map.Entry<Substance, Integer> entry : substances.entrySet()) {
            if (entry.getKey().getName().equals(substance.getName())) {
                substances.merge(entry.getKey(), amount, Integer::sum);
                updated = true;
                break;
            }
        }

        if (!updated) {
            substances.put(substance, amount);
        }
        setChanged();
    }

    public Map<Substance, Integer> transferSubstances(int amount) {
        if (substances.isEmpty()) {
            return new HashMap<>();
        }

        int transferAmount = (amount / substances.size());
        Map<Substance, Integer> transferredSubstances = new HashMap<>();
        List<Substance> substancesToRemove = new ArrayList<>();

        for (Map.Entry<Substance, Integer> entry : substances.entrySet()) {
            Substance substance = entry.getKey();
            int availableAmount = entry.getValue();

            int actualAmount = Math.min(availableAmount, transferAmount);
            updateSubstance(substance, -actualAmount);
            transferredSubstances.put(substance, actualAmount);

            if (availableAmount <= actualAmount) {
                substancesToRemove.add(substance);
            }
        }

        substancesToRemove.forEach(substances::remove);

        if (substances.isEmpty()) {
            removePuddleBlock();
        }

        setChanged();

        return transferredSubstances;
    }

    private void removePuddleBlock() {
        assert level != null;
        if (!level.isClientSide) {
            level.removeBlock(getBlockPos(), false);
        }
    }

    @OnlyIn(Dist.CLIENT)
    public int getColor() {
        if (cachedColor == -1) {
            if (substances.isEmpty()) {
                cachedColor = 0xFFAAD5DB; // Default color if no substances
            } else {
                Map<Integer, Integer> colors = new HashMap<>();
                for (Map.Entry<Substance, Integer> entry : substances.entrySet()) {
                    colors.put(entry.getKey().getColor(), entry.getValue());
                }
                cachedColor = ColorUtil.mixColors(colors);
            }
        }
        return cachedColor;
    }

    private int getTotalAmount() {
        return substances.values().stream().reduce(0, Integer::sum);
    }

    public Component getContentsDescription() {
        return Component.literal("Puddle contains: " +
                substances.entrySet().stream()
                        .map(entry -> String.format("%.1f%% %s", entry.getValue(), entry.getKey().getName()))
                        .collect(Collectors.joining(", "))
        );
    }

    /**
     * NBT/DATA
     */

    private CompoundTag serializeData() {
        CompoundTag nbt = new CompoundTag();
        ListTag substancesList = new ListTag();
        for (Map.Entry<Substance, Integer> entry : substances.entrySet()) {
            CompoundTag substanceTag = entry.getKey().serializeNBT();
            substanceTag.putInt("Amount", entry.getValue());
            substancesList.add(substanceTag);
        }
        nbt.put("Substances", substancesList);
        return nbt;
    }

    private void deserializeData(CompoundTag nbt) {
        substances.clear();
        ListTag substancesList = nbt.getList("Substances", 10);
        for (int i = 0; i < substancesList.size(); i++) {
            CompoundTag substanceTag = substancesList.getCompound(i);
            Substance substance = Substance.fromNBT(substanceTag);
            int amount = substanceTag.getInt("Amount");
            substances.put(substance, amount);
        }
        setChanged();
    }

    @Override
    public void saveAdditional(@NotNull CompoundTag nbt) {
        super.saveAdditional(nbt);
        nbt.put("PuddleData", serializeData());
    }

    @Override
    public void load(@NotNull CompoundTag nbt) {
        super.load(nbt);
        deserializeData(nbt.getCompound("PuddleData"));
    }

    /**
     * NETWORKING
     */
    @Override
    public @NotNull CompoundTag getUpdateTag() {
        CompoundTag tag = new CompoundTag();
        tag.put("PuddleData", serializeData());
        return tag;
    }

    @Override
    public void handleUpdateTag(CompoundTag tag) {
        deserializeData(tag.getCompound("PuddleData"));
        if (level != null && level.isClientSide) {
            level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), 3);
        }
    }

    @Override
    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket pkt) {
        handleUpdateTag(Objects.requireNonNull(pkt.getTag()));
    }

    private void syncToClient() {
        if (level != null && !level.isClientSide) {
            level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), 3);
        }
    }

    @Override
    public void setChanged() {
        super.setChanged();
        invalidateColor();
        syncToClient();
    }

    private void invalidateColor() {
        cachedColor = -1;
        requestModelDataUpdate();
    }

    public void mixWith(CompoundTag nbt) {
        ListTag substancesList = nbt.getList("Substances", 10);
        for (int i = 0; i < substancesList.size(); i++) {
            CompoundTag substanceTag = substancesList.getCompound(i);
            Substance substance = Substance.fromNBT(substanceTag);
            int amount = substanceTag.getInt("Amount");
            updateSubstance(substance, amount);
        }
    }
}
