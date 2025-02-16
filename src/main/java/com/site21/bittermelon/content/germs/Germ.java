package com.site21.bittermelon.content.germs;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.ExtraCodecs;
import org.jetbrains.annotations.NotNull;

import java.util.Random;

public class Germ {
    // TODO: Figure out something better instead of doing it like this?

    public static final Codec<Germ> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            ExtraCodecs.POSITIVE_INT.fieldOf("incubationTime").forGetter(Germ::getIncubationTime),
            ExtraCodecs.POSITIVE_INT.fieldOf("infectionTime").forGetter(Germ::getInfectionTime),
            ExtraCodecs.POSITIVE_INT.fieldOf("contagiousTime").forGetter(Germ::getContagiousTime),
            ExtraCodecs.POSITIVE_INT.fieldOf("survivalOutsideHostTime").forGetter(Germ::getSurvivalOutsideHostTime),
            Codec.FLOAT.fieldOf("toxicity").forGetter(Germ::getToxicity),
            Codec.FLOAT.fieldOf("heatSensitivity").forGetter(Germ::getHeatSensitivity),
            Codec.FLOAT.fieldOf("medicineResistance").forGetter(Germ::getMedicineResistance),
            Codec.FLOAT.fieldOf("mutationRate").forGetter(Germ::getMutationRate),
            Codec.FLOAT.fieldOf("transmissionRate").forGetter(Germ::getTransmissionRate),
            Codec.FLOAT.fieldOf("growthRate").forGetter(Germ::getGrowthRate)
    ).apply(instance, Germ::new));

    public static final StreamCodec<FriendlyByteBuf, Germ> STREAM_CODEC = new StreamCodec<>() {

        @Override
        public void encode(@NotNull FriendlyByteBuf buf, @NotNull Germ germ) {
            buf.writeInt(germ.getIncubationTime());
            buf.writeInt(germ.getInfectionTime());
            buf.writeInt(germ.getContagiousTime());
            buf.writeInt(germ.getSurvivalOutsideHostTime());
            buf.writeFloat(germ.getToxicity());
            buf.writeFloat(germ.getHeatSensitivity());
            buf.writeFloat(germ.getMedicineResistance());
            buf.writeFloat(germ.getMutationRate());
            buf.writeFloat(germ.getTransmissionRate());
            buf.writeFloat(germ.getGrowthRate());
        }

        @Override
        public @NotNull Germ decode(@NotNull FriendlyByteBuf buf) {
            int incubationTime = buf.readInt();
            int infectionTime = buf.readInt();
            int contagiousTime = buf.readInt();
            int survivalTime = buf.readInt();
            float toxicity = buf.readFloat();
            float heatSensitivity = buf.readFloat();
            float medicineResistance = buf.readFloat();
            float mutationRate = buf.readFloat();
            float transmissionRate = buf.readFloat();
            float growthRate = buf.readFloat();
            return new Germ(incubationTime, infectionTime, contagiousTime,
                    survivalTime, toxicity, heatSensitivity, medicineResistance,
                    mutationRate, transmissionRate, growthRate);
        }
    };

    private final int incubationTime;
    private final int infectionTime;
    private final int contagiousTime;
    private final int survivalOutsideHostTime;

    private final float toxicity;
    private final float heatSensitivity;
    private final float medicine_resistance;
    private final float mutationRate;
    private final float transmissionRate;
    private final float growthRate;

    // TODO: Description, special symptoms


    public Germ(int incubationTime, int infectionTime, int contagiousTime,
                int survivalOutsideHostTime, float toxicity, float heatSensitivity,
                float medicineResistance, float mutationRate, float transmissionRate,
                float growthRate) {
        this.incubationTime = incubationTime;
        this.infectionTime = infectionTime;
        this.contagiousTime = contagiousTime;
        this.survivalOutsideHostTime = survivalOutsideHostTime;
        this.toxicity = toxicity;
        this.heatSensitivity = heatSensitivity;
        this.medicine_resistance = medicineResistance;
        this.mutationRate = mutationRate;
        this.transmissionRate = transmissionRate;
        this.growthRate = growthRate;
    }

    public static @NotNull Germ generateRandomGerm(long seed) {
        Random random = new Random(seed);

        int incubationTime = random.nextInt(20 * 30, 20 * 300);
        int infectionTime = random.nextInt(20 * 60, 20 * 600);
        int contagiousTime = random.nextInt(20 * 30, 20 * 180);
        int survivalOutsideHostTime = random.nextInt(20 * 10, 20 * 60);

        float toxicity = random.nextFloat();
        float heatSensitivity = random.nextFloat();
        float medicineResistance = random.nextFloat();
        float mutationRate = random.nextFloat() * 0.2f;
        float transmissionRate = random.nextFloat();
        float growthRate = random.nextFloat();

        return new Germ(
                incubationTime,
                infectionTime,
                contagiousTime,
                survivalOutsideHostTime,
                toxicity,
                heatSensitivity,
                medicineResistance,
                mutationRate,
                transmissionRate,
                growthRate
        );
    }

    public int getIncubationTime() {
        return incubationTime;
    }

    public int getInfectionTime() {
        return infectionTime;
    }

    public int getContagiousTime() {
        return contagiousTime;
    }

    public int getSurvivalOutsideHostTime() {
        return survivalOutsideHostTime;
    }

    public float getToxicity() {
        return toxicity;
    }

    public float getHeatSensitivity() {
        return heatSensitivity;
    }

    public float getMedicineResistance() {
        return medicine_resistance;
    }

    public float getMutationRate() {
        return mutationRate;
    }

    public float getTransmissionRate() {
        return transmissionRate;
    }

    public float getGrowthRate() {
        return growthRate;
    }
}
