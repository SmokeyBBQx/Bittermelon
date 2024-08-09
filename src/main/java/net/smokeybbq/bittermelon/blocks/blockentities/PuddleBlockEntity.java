package net.smokeybbq.bittermelon.blocks.blockentities;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.Connection;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.smokeybbq.bittermelon.init.BlockEntityInit;
import net.smokeybbq.bittermelon.substances.Substance;
import net.smokeybbq.bittermelon.util.ColorUtil;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

import static net.smokeybbq.bittermelon.util.ColorUtil.mixColors;

public class PuddleBlockEntity extends BlockEntity {
    private final Map<Substance, Float> substances = new HashMap<>();
    private int cachedColor = -1;

    public PuddleBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(BlockEntityInit.PUDDLE_BLOCK_ENTITY.get(), pPos, pBlockState);
    }

//    public void tick() {
//    }

    @Override
    public void load(@NotNull CompoundTag nbt) {
        super.load(nbt);
        substances.clear();
        ListTag substancesList = nbt.getList("Substances", 10);
        for (int i = 0; i < substancesList.size(); i++) {
            CompoundTag substanceTag = substancesList.getCompound(i);
            Substance substance = Substance.fromNBT(substanceTag);
            float amount = substanceTag.getFloat("Amount");
            substances.put(substance, amount);
        }
        System.out.println("Loaded substances: " + substances);
        cachedColor = -1;
    }

    @Override
    protected void saveAdditional(@NotNull CompoundTag nbt) {
        super.saveAdditional(nbt);
        ListTag substancesList = new ListTag();
        for (Map.Entry<Substance, Float> entry : substances.entrySet()) {
            CompoundTag substanceTag = entry.getKey().serializeNBT();
            substanceTag.putFloat("Amount", entry.getValue());
            substancesList.add(substanceTag);
        }
        nbt.put("Substances", substancesList);
        System.out.println("Saved substances: " + substances);
    }

    @OnlyIn(Dist.CLIENT)
    public int getColor() {
        if (cachedColor == -1) {
            if (substances.isEmpty()) {
                cachedColor = 0xFFAAD5DB; // Default color if no substances
            } else {
                Map<Integer, Float> colors = new HashMap<>();
                for (Map.Entry<Substance, Float> entry : substances.entrySet()) {
                    colors.put(entry.getKey().getColor(), entry.getValue());
                }
                cachedColor = ColorUtil.mixColors(colors);
            }
        }
        return cachedColor;
    }

    public void addSubstance(Substance substance, float amount) {
        substances.put(substance, substances.getOrDefault(substance, 0f) + amount);
        invalidateColor();
        setChanged();
        syncToClient();
    }

    private void invalidateColor() {
        cachedColor = -1;
        if (level != null && level.isClientSide) {
            requestModelDataUpdate();
        }
    }

    @Override
    public CompoundTag getUpdateTag() {
        CompoundTag tag = new CompoundTag();
        saveAdditional(tag);
        return tag;
    }

    @Override
    public void handleUpdateTag(CompoundTag tag) {
        load(tag);
    }

    @Override
    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket pkt) {
        super.onDataPacket(net, pkt);
        CompoundTag tag = pkt.getTag();
        handleUpdateTag(tag);
        requestModelDataUpdate();
        if (level != null) {
            level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), 3);
        }
    }

    private void syncToClient() {
        if (level != null && !level.isClientSide) {
            BlockState state = level.getBlockState(worldPosition);
            level.sendBlockUpdated(worldPosition, state, state, 3);
            setChanged();
        }
    }

    @Override
    public void setChanged() {
        super.setChanged();
        if (level != null && !level.isClientSide) {
            syncToClient();
        }
    }

    private float getTotalAmount() {
        return substances.values().stream().reduce(0f, Float::sum);
    }

    public Component getContentsDescription() {
        StringBuilder description = new StringBuilder("Puddle contains: ");
        for (Map.Entry<Substance, Float> entry : substances.entrySet()) {
            description.append(String.format("%.1f%% %s, ", entry.getValue(), entry.getKey().getName()));
        }
        return Component.literal(description.substring(0, description.length() - 2));
    }
}
