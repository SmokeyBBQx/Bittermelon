package com.site21.bittermelon.common.systems.medical.drug;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.site21.bittermelon.common.systems.medical.compartment.MedicalAttribute;
import com.site21.bittermelon.common.systems.medical.medicalstats.MedicalAttributeModifier;
import com.site21.bittermelon.common.systems.medical.medicalstats.MedicalStats;
import net.minecraft.core.Holder;
import net.minecraft.core.UUIDUtil;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.UUID;

import static com.site21.bittermelon.init.neoforge.BitterRegistries.DRUG_REGISTRY;
import static com.site21.bittermelon.init.neoforge.BitterRegistries.DRUG_REGISTRY_KEY;

public class DrugInstance {
    public static final Codec<DrugInstance> CODEC;
    public static final StreamCodec<RegistryFriendlyByteBuf, DrugInstance> STREAM_CODEC;

    private final Holder<Drug> drug;
    private float amount;
    private float absorbedAmount;
    private int duration;
    private final UUID id;

    public DrugInstance(Holder<Drug> drug, float amount, float absorbedAmount, int duration, UUID id) {
        this.drug = drug;
        this.amount = amount;
        this.absorbedAmount = absorbedAmount;
        this.duration = duration;
        this.id = id;
    }

    public DrugInstance(Holder<Drug> drug, float amount) {
        this(drug, amount, 0, 0, UUID.randomUUID());
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

        updateAttributes(medicalStats);
    }

    public void updateAttributes(MedicalStats medicalStats) {
        for (Map.Entry<MedicalAttribute, Float> entry : drug.value().getAttributes().entrySet()) {
            medicalStats.getAttributes().get(entry.getKey()).addModifier(id,
                    new MedicalAttributeModifier(MedicalAttributeModifier.Operation.MULTIPLIER,
                            entry.getValue() * absorbedAmount));
        }
    }

    public void remove(MedicalStats medicalStats) {
        drug.value().onRemoval(medicalStats);
        medicalStats.removeModifiers(id, drug.value().getAttributes().keySet());
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

    public UUID getId() {
        return id;
    }

    static {
        CODEC = RecordCodecBuilder.create(instance -> instance.group(
                DRUG_REGISTRY.holderByNameCodec().fieldOf("drug").forGetter(DrugInstance::getDrug),
                Codec.FLOAT.fieldOf("amount").forGetter(DrugInstance::getAmount),
                Codec.FLOAT.fieldOf("absorbedAmount").forGetter(DrugInstance::getAbsorbedAmount),
                Codec.INT.fieldOf("duration").forGetter(DrugInstance::getDuration),
                UUIDUtil.CODEC.fieldOf("id").forGetter(DrugInstance::getId)
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
                UUIDUtil.STREAM_CODEC,
                DrugInstance::getId,
                DrugInstance::new
        );
    }
}
