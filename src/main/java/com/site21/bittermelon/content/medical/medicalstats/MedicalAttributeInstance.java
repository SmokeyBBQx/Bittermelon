package com.site21.bittermelon.content.medical.medicalstats;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.UUIDUtil;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

import java.util.*;

public class MedicalAttributeInstance {
    public static final Codec<MedicalAttributeInstance> CODEC;
    public static final StreamCodec<ByteBuf, MedicalAttributeInstance> STREAM_CODEC;

    private final HashMap<UUID, MedicalAttributeModifier> modifiers;
    private Float value;
    private final Set<UUID> modifiersToRemove;

    public MedicalAttributeInstance() {
        modifiers = new HashMap<>();
        modifiersToRemove = new HashSet<>();
    }

    public MedicalAttributeInstance(Map<UUID, MedicalAttributeModifier> modifiers, float value) {
        this.modifiers = new HashMap<>(modifiers);
        this.value = value;
        modifiersToRemove = new HashSet<>();
    }


    public MedicalAttributeModifier getModifier(UUID uuid) {
        return modifiers.computeIfAbsent(uuid, ( k) ->
                new MedicalAttributeModifier(MedicalAttributeModifier.Operation.AVERAGE, 0));
    }

    public HashMap<UUID, MedicalAttributeModifier> getModifiers() {
        return modifiers;
    }

    public void updateModifier(UUID uuid, float value) {
        getModifier(uuid).setModifier(value);
        updateValue();
    }

    public void addModifier(UUID uuid, MedicalAttributeModifier modifier) {
        if (getModifiers().get(uuid).equals(modifier)) return;
        modifiers.put(uuid, modifier);
        updateValue();
    }

    public void addModifierNoUpdate(UUID uuid, MedicalAttributeModifier modifier) {
        modifiers.put(uuid, modifier);
    }

    public void removeModifier(UUID uuid) {
        if (getModifiers().containsKey(uuid)) {
            modifiersToRemove.add(uuid);
            updateValue();
        }
    }

    public float getValue() {
        if (value.isNaN()) return updateValue();
        return value;
    }

    public float updateValue() {
        int averageOperationCount = 0;
        float averageOperationTotal = 0;
        List<MedicalAttributeModifier> multipliers = new ArrayList<>();

        for (Map.Entry<UUID, MedicalAttributeModifier> entry : modifiers.entrySet()) {
            if (modifiersToRemove.contains(entry.getKey())) continue;
            MedicalAttributeModifier modifier = entry.getValue();

            if (modifier.getOperation() == MedicalAttributeModifier.Operation.AVERAGE) {
                averageOperationCount++;
                averageOperationTotal += modifier.getModifier();
            } else {
                multipliers.add(modifier);
            }
        }

        for (UUID uuid : modifiersToRemove) {
            modifiers.remove(uuid);
        }
        modifiersToRemove.clear();

        if (averageOperationCount == 0) return 0;

        float averageValue = averageOperationTotal / averageOperationCount;
        for (MedicalAttributeModifier multiplier : multipliers) {
            averageValue *= multiplier.getModifier();
        }

        value = averageValue;
        return averageValue;
    }

    static {
        CODEC = RecordCodecBuilder.create(instance -> instance.group(
                Codec.unboundedMap(UUIDUtil.CODEC, MedicalAttributeModifier.CODEC).fieldOf("modifiers").forGetter(MedicalAttributeInstance::getModifiers),
                Codec.FLOAT.fieldOf("value").forGetter(MedicalAttributeInstance::getValue)
                ).apply(instance, MedicalAttributeInstance::new)
        );

        STREAM_CODEC = StreamCodec.composite(
                ByteBufCodecs.map(
                        HashMap::new,
                        UUIDUtil.STREAM_CODEC,
                        MedicalAttributeModifier.STREAM_CODEC),
                MedicalAttributeInstance::getModifiers,
                ByteBufCodecs.FLOAT,
                MedicalAttributeInstance::getValue,
                MedicalAttributeInstance::new
        );
    }
}
