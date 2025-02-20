package com.site21.bittermelon.content.atmosphere;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.site21.bittermelon.content.atmosphere.networking.UpdateAtmosBlocks;
import com.site21.bittermelon.content.atmosphere.networking.UpdateAtmosGas;
import com.site21.bittermelon.content.atmosphere.networking.UpdateAtmosTemperature;
import com.site21.bittermelon.content.substance.SubstanceStack;
import com.site21.bittermelon.util.SubstanceUtils;
import io.netty.buffer.ByteBuf;
import it.unimi.dsi.fastutil.longs.LongOpenHashSet;
import it.unimi.dsi.fastutil.longs.LongSet;
import net.minecraft.core.UUIDUtil;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.network.PacketDistributor;
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

    public static final StreamCodec<RegistryFriendlyByteBuf, AtmosInstance> STREAM_CODEC =
            StreamCodec.composite(
                    ByteBufCodecs.FLOAT,
                    AtmosInstance::getTemperature,
                    SubstanceStack.STREAM_CODEC.apply(ByteBufCodecs.list()),
                    AtmosInstance::getGases,
                    UUIDUtil.STREAM_CODEC,
                    AtmosInstance::getUuid,
                    ByteBufCodecs.collection(
                            LongOpenHashSet::new,
                            ByteBufCodecs.VAR_LONG
                    ),
                    AtmosInstance::getBlocks,
                    AtmosInstance::new
            );

    private final List<SubstanceStack> gases;
    private float temperature; // Kelvin
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


    public void addBlock(Long packedPos, Level level) {
        blocks.add(packedPos);
        if (!level.isClientSide) {
            PacketDistributor.sendToAllPlayers(new UpdateAtmosBlocks(uuid, true, packedPos));
        }
    }

    public void removeBlock(Long packedPos, Level level) {
        blocks.remove(packedPos);
        if (!level.isClientSide) {
            PacketDistributor.sendToAllPlayers(new UpdateAtmosBlocks(uuid, false, packedPos));
        }
    }

    public LongSet getBlocks() {
        return blocks;
    }

    public void setTemperature(float temperature, Level level) {
        this.temperature = temperature;
        if (!level.isClientSide) {
            PacketDistributor.sendToAllPlayers(new UpdateAtmosTemperature(uuid, temperature));
        }
    }


    public void merge(@NotNull AtmosInstance other, Level level) {
        setTemperature((temperature + other.getTemperature()) / 2, level);
        for (SubstanceStack gas : other.gases) {
            updateGas(gas, level);
        }
    }

    public void updateGas(SubstanceStack gas, Level level) {
        for (SubstanceStack stack : gases) {
            if (stack.canMergeWith(gas)) {
                stack.modifyAmount(gas.getAmount());
                if (!level.isClientSide) {
                    PacketDistributor.sendToAllPlayers(new UpdateAtmosGas(uuid, stack));
                }
                return;
            }
        }

        gases.add(gas);
        if (!level.isClientSide) {
            PacketDistributor.sendToAllPlayers(new UpdateAtmosGas(uuid, gas));
        }
    }
}
