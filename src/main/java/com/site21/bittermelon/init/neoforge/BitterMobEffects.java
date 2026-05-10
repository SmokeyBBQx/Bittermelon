package com.site21.bittermelon.init.neoforge;

import com.site21.bittermelon.Bittermelon;
import com.site21.bittermelon.common.content.blocks.scp.scp151.DrowningEffect;
import com.site21.bittermelon.common.content.entities.scp718.EyeballGrowthEffect;
import com.site21.bittermelon.common.content.items.taser.TaserEffect;
import com.site21.bittermelon.common.content.mobeffects.*;
import com.site21.bittermelon.common.content.mobeffects.electrocuted.ElectrocutedEffect;
import com.site21.bittermelon.common.systems.stumble.FallenEffect;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.neoforged.neoforge.registries.DeferredRegister;

public class BitterMobEffects {
    public static final DeferredRegister<MobEffect> MOB_EFFECTS = DeferredRegister.create(BuiltInRegistries.MOB_EFFECT, Bittermelon.MOD_ID);

    public static final Holder<MobEffect> DROWNING = MOB_EFFECTS.register("drowning", DrowningEffect::new);

    public static final Holder<MobEffect> TREMOR = MOB_EFFECTS.register("tremor", TremorEffect::new);

    public static final Holder<MobEffect> BAD_MOBILITY = MOB_EFFECTS.register("bad_mobility", BadMobilityEffect::new);

    public static final Holder<MobEffect> ASPHYXIATION = MOB_EFFECTS.register("asphyxiation", AsphyxiationEffect::new);

    public static final Holder<MobEffect> PAIN = MOB_EFFECTS.register("pain", PainEffect::new);

    public static final Holder<MobEffect> STUN = MOB_EFFECTS.register("stun", () ->
            new BitterEffect(MobEffectCategory.HARMFUL, 0)
                    .addAttributeModifier(Attributes.MOVEMENT_SPEED,
                            ResourceLocation.fromNamespaceAndPath(Bittermelon.MOD_ID, "effect.movement_stun"),
                            -1,
                            AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL)
                    .addAttributeModifier(Attributes.JUMP_STRENGTH,
                            ResourceLocation.fromNamespaceAndPath(Bittermelon.MOD_ID, "effect.jump_stun"),
                            -1,
                            AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL));

    public static final Holder<MobEffect> FALLEN = MOB_EFFECTS.register("fallen", FallenEffect::new);

    public static final Holder<MobEffect> ELECTROCUTED = MOB_EFFECTS.register("electrocuted", ElectrocutedEffect::new);

    public static final Holder<MobEffect> TASERED = MOB_EFFECTS.register("tasered", TaserEffect::new);

    public static final Holder<MobEffect> EYE_IRRITATION = MOB_EFFECTS.register("eye_irritation",
            () -> new BitterEffect(MobEffectCategory.HARMFUL, 0));

    public static final Holder<MobEffect> FAINTING = MOB_EFFECTS.register("fainting",
            () -> new BitterEffect(MobEffectCategory.HARMFUL, 0));

    public static final Holder<MobEffect> UNCONSCIOUS = MOB_EFFECTS.register("unconscious",
            () -> new BitterEffect(MobEffectCategory.HARMFUL, 0));

    public static final Holder<MobEffect> HALLUCINATION = MOB_EFFECTS.register("hallucination",
            () -> new BitterEffect(MobEffectCategory.NEUTRAL, 0));

    public static final Holder<MobEffect> BLEEDING = MOB_EFFECTS.register("bleeding", BleedingEffect::new);

    public static final Holder<MobEffect> EUPHORIA = MOB_EFFECTS.register("euphoria", EuphoriaEffect::new);

    public static final Holder<MobEffect> EYEBALL_GROWTH = MOB_EFFECTS.register("eyeball_growth", EyeballGrowthEffect::new);

    public static final Holder<MobEffect> BUBBLE_BATH = MOB_EFFECTS.register("bubble_bath", BubbleBathEffect::new);

}
