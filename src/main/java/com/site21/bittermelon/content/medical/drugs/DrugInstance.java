package com.site21.bittermelon.content.medical.drugs;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.site21.bittermelon.content.medical.compartments.MedicalAttribute;
import com.site21.bittermelon.content.medical.medicalstats.MedicalStats;
import net.minecraft.core.Holder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import org.jetbrains.annotations.NotNull;

import static com.site21.bittermelon.init.neoforge.BitterRegistries.DRUG_REGISTRY;
import static com.site21.bittermelon.init.neoforge.BitterRegistries.DRUG_REGISTRY_KEY;

public class DrugInstance {
    public static final Codec<DrugInstance> CODEC;
    public static final StreamCodec<RegistryFriendlyByteBuf, DrugInstance> STREAM_CODEC;

    private final Holder<Drug> drug;
    private float amount;
    private float absorbedAmount;
    private int duration;

    public DrugInstance(Holder<Drug> drug, float amount, float absorbedAmount, int duration) {
        this.drug = drug;
        this.amount = amount;
        this.absorbedAmount = absorbedAmount;
        this.duration = duration;
    }

    public DrugInstance(Holder<Drug> drug, float amount) {
        this(drug, amount, 0, 0);
    }

    public void tickInstance(@NotNull MedicalStats medicalStats) {
        Drug drugValue = drug.value();

        if (!drugValue.shouldApplyTick(duration)) return;
        duration++;

        float elimination = medicalStats.getElimination() * drugValue.getEliminationRate();
        float absorption = medicalStats.getAbsorption() * drugValue.getAbsorptionRate();

        absorption = Math.min(amount, absorption);

        absorbedAmount += absorption;
        amount -= absorption;

        drugValue.tickDrug(medicalStats, absorbedAmount);

        absorbedAmount -= elimination;
    }

    public void remove(MedicalStats medicalStats) {
        drug.value().onRemoval(medicalStats);
    }

    public Holder<Drug> getDrug() {
        return drug;
    }

    public float getAmount() {
        return amount;
    }

    public float getAbsorbedAmount() {
        return absorbedAmount;
    }

    public int getDuration() {
        return duration;
    }

    static {
        CODEC = RecordCodecBuilder.create(instance -> instance.group(
                DRUG_REGISTRY.holderByNameCodec().fieldOf("drug").forGetter(DrugInstance::getDrug),
                Codec.FLOAT.fieldOf("amount").forGetter(DrugInstance::getAmount),
                Codec.FLOAT.fieldOf("absorbedAmount").forGetter(DrugInstance::getAbsorbedAmount),
                Codec.INT.fieldOf("duration").forGetter(DrugInstance::getDuration)
                ).apply(instance, DrugInstance::new)
        );

        STREAM_CODEC = StreamCodec.composite(
                ByteBufCodecs.holderRegistry(DRUG_REGISTRY_KEY),
                DrugInstance::getDrug,
                ByteBufCodecs.FLOAT,
                DrugInstance::getAmount,
                ByteBufCodecs.FLOAT,
                DrugInstance::getAbsorbedAmount,
                ByteBufCodecs.INT,
                DrugInstance::getDuration,
                DrugInstance::new
        );
    }
}
