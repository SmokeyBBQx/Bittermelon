package net.smokeybbq.bittermelon.blocks.blockentities;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
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

    public int getColor() {
        System.out.println("getColor called. Substances: " + substances);
        if (cachedColor == -1) {
            if (substances.isEmpty()) {
                cachedColor = 0xFFAAD5DB; // Default color if no substances
                System.out.println("No substances, using default color: #FFAAD5DB");
            } else {
                Map<Integer, Float> colors = new HashMap<>();
                for (Map.Entry<Substance, Float> entry : substances.entrySet()) {
                    colors.put(entry.getKey().getColor(), entry.getValue());
                    System.out.println("Substance: " + entry.getKey().getName() + ", Color: " + String.format("#%06X", entry.getKey().getColor()) + ", Amount: " + entry.getValue());
                }
                cachedColor = ColorUtil.mixColors(colors);
                System.out.println("Calculated new color from substances: " + String.format("#%08X", cachedColor));
            }
        } else {
            System.out.println("Using cached color: " + String.format("#%08X", cachedColor));
        }
        return cachedColor;
    }

    public void addSubstance(Substance substance, float amount) {
        substances.put(substance, substances.getOrDefault(substance, 0f) + amount);

        cachedColor = -1;
        setChanged();

        if (level != null && !level.isClientSide) {
            level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), 3);
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
