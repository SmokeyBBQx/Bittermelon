package com.site21.bittermelon.atmosphere;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.site21.bittermelon.substance.SubstanceStack;
import com.site21.bittermelon.util.SubstanceUtils;
import it.unimi.dsi.fastutil.longs.LongOpenHashSet;
import it.unimi.dsi.fastutil.longs.LongSet;
import net.minecraft.core.UUIDUtil;
import org.jetbrains.annotations.NotNull;

import java.util.*;

import static com.site21.bittermelon.util.SubstanceUtils.GAS_CONSTANT;

public class AtmosInstance {
    private static final Codec<LongSet> LONG_SET_CODEC = Codec.LONG.listOf()
            .xmap(
                    LongOpenHashSet::new,
                    ArrayList::new
            );

    public static final Codec<AtmosInstance> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.FLOAT.fieldOf("temperature").forGetter(AtmosInstance::getTemperature),
            SubstanceStack.CODEC.listOf().fieldOf("gases").forGetter(AtmosInstance::getGases),
            UUIDUtil.CODEC.fieldOf("uuid").forGetter(AtmosInstance::getUuid),
            LONG_SET_CODEC.fieldOf("blocks").forGetter(AtmosInstance::getBlocks)
    ).apply(instance, AtmosInstance::new));

    private final List<SubstanceStack> gases;
    private float temperature;
    private final UUID uuid;
    private final LongSet blocks;

    public AtmosInstance(float initialTemp, List<SubstanceStack> initialGasses, UUID uuid, LongSet blocks) {
        this.gases = new ArrayList<>(initialGasses);
        this.temperature = initialTemp;
        this.uuid = uuid;
        this.blocks = new LongOpenHashSet(blocks);
    }

    public AtmosInstance(float initialTemp, List<SubstanceStack> initialGasses, UUID uuid) {
        this(initialTemp, initialGasses, uuid, new LongOpenHashSet());
    }

    public AtmosInstance(float initialTemp, List<SubstanceStack> initialGasses) {
        this(initialTemp, initialGasses, UUID.randomUUID());
    }

    public AtmosInstance(float initialTemp) {
        this(initialTemp, new ArrayList<>(), UUID.randomUUID());
    }

    public float getTemperature() {
        return temperature;
    }

    public List<SubstanceStack> getGases() {
        return gases;
    }

    public float getPressure() {
        if (blocks.isEmpty()) return 0;
        return SubstanceUtils.getTotalAmount(gases) * GAS_CONSTANT * temperature / (blocks.size() * 1000);
    }

    public UUID getUuid() {
        return uuid;
    }


    public void addBlock(Long packedPos) {
        blocks.add(packedPos);
    }

    public void removeBlock(Long packedPos) {
        blocks.remove(packedPos);
    }

    public LongSet getBlocks() {
        return blocks;
    }

    public void setTemperature(float temperature) {
        this.temperature = temperature;
    }

    public void merge(@NotNull AtmosInstance other) {
        setTemperature((temperature + other.getTemperature()) / 2);
        for (SubstanceStack gas : other.gases) {
            updateGas(gas);
        }
    }

    public void updateGas(SubstanceStack gas) {
        for (SubstanceStack stack : gases) {
            if (stack.canMergeWith(gas)) {
                stack.modifyAmount(gas.getAmount());
                return;
            }
        }

        gases.add(gas);
    }
}
