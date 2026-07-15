package com.site21.bittermelon.common.content.entities.scp1507;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.site21.bittermelon.common.systems.character.Character;
import com.site21.bittermelon.common.systems.character.CharacterUtil;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;

import java.util.HashMap;
import java.util.Map;

public class Opinions {
    public static final Codec<Opinions> CODEC;
    private final Map<OpinionSubject, OpinionValue> opinions;

    public Opinions(Map<OpinionSubject, OpinionValue> opinions) {
        this.opinions = new HashMap<>(opinions);
    }

    public Opinions() {
        this(new HashMap<>());
    }

    public OpinionValue get(OpinionSubject subject) {
        return opinions.getOrDefault(subject, OpinionValue.NEUTRAL);
    }

    public void adjust(OpinionSubject subject, float trustAmount, float respectAmount) {
        opinions.compute(subject, (_, existing) -> {
            float currentTrust = existing != null ? existing.trust() : 0.5f;
            float currentRespect = existing != null ? existing.respect() : 0.5f;

            float newTrust = getTrust(currentTrust, trustAmount);
            float newRespect = getRespect(currentRespect, respectAmount);

            return new OpinionValue(newTrust, newRespect);
        });
    }

    public void reactToDamage(DamageSource source, float amount) {
        if (source.getEntity() != null) {
            if (!source.isDirect()) {
                Entity causing = source.getEntity();
                adjust(OpinionSubject.of(causing.getType()), -amount / 2, -amount / 2);
                if (CharacterUtil.getCharacter(causing) instanceof Character character) {
                    adjust(OpinionSubject.of(character.getId()), -amount, -amount);
                }
            }

            ItemStack weapon = source.getWeaponItem();
            if (weapon != null && !weapon.isEmpty()) {
                adjust(OpinionSubject.of(weapon.getItem()), -amount / 2, -amount / 2);
            }

            Entity direct = source.getDirectEntity();
            adjust(OpinionSubject.of(direct.getType()), -amount / 2, -amount / 2);
            if (CharacterUtil.getCharacter(direct) instanceof Character character) {
                adjust(OpinionSubject.of(character.getId()), -amount, -amount);
            }
        }
    }

    private float getTrust(float currentTrust, float amount) {
        float target = amount > 0 ? 1f : -1f;
        return Mth.lerp(0.03f, currentTrust, target);
    }

    private float getRespect(float currentRespect, float amount) {
        float delta = Mth.clamp(amount * 0.15f, -0.6f, 0.6f);
        float headroom = delta > 0 ? (1f - currentRespect) : (1f + currentRespect);
        return currentRespect + delta * headroom;
    }

    static {
        CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.unboundedMap(OpinionSubject.CODEC, OpinionValue.CODEC).fieldOf("opinions").forGetter(opinions -> opinions.opinions)
        ).apply(instance, Opinions::new));
    }
}
