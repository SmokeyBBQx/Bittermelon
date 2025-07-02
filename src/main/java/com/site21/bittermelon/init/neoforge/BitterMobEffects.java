package com.site21.bittermelon.init.neoforge;

import com.site21.bittermelon.Bittermelon;
import com.site21.bittermelon.content.blocks.scp.scp151.DrowningEffect;
import com.site21.bittermelon.content.medical.mobeffects.Asphyxiation;
import com.site21.bittermelon.content.medical.mobeffects.BadMobility;
import com.site21.bittermelon.content.medical.mobeffects.Pain;
import com.site21.bittermelon.content.medical.mobeffects.Tremor;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.effect.MobEffect;
import net.neoforged.neoforge.registries.DeferredRegister;

public class BitterMobEffects {
    public static final DeferredRegister<MobEffect> MOB_EFFECTS = DeferredRegister.create(BuiltInRegistries.MOB_EFFECT, Bittermelon.MOD_ID);

    public static final Holder<MobEffect> DROWNING = MOB_EFFECTS.register("drowning", DrowningEffect::new);
    public static final Holder<MobEffect> TREMOR = MOB_EFFECTS.register("tremor", Tremor::new);
    public static final Holder<MobEffect> BAD_MOBILITY = MOB_EFFECTS.register("bad_mobility", BadMobility::new);
    public static final Holder<MobEffect> ASPHYXIATION = MOB_EFFECTS.register("asphyxiation", Asphyxiation::new);
    public static final Holder<MobEffect> PAIN = MOB_EFFECTS.register("pain", Pain::new);
}
