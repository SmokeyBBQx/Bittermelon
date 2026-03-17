package com.site21.bittermelon.common.systems.chemistry.effects;

import com.site21.bittermelon.common.systems.chemistry.ReactionEffect;
import com.site21.bittermelon.common.systems.chemistry.ReactionEffectType;
import com.site21.bittermelon.common.systems.chemistry.Reactor;
import com.site21.bittermelon.common.systems.substance.Substance;
import com.site21.bittermelon.common.systems.substance.SubstanceStack;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.world.level.Level;

import java.util.HashMap;
import java.util.Map;

import static com.site21.bittermelon.init.custom.ReactionEffects.SYNTHESIS;

public class SynthesisEffect implements ReactionEffect {
    private final Map<Holder<Substance>, Integer> products;

    public SynthesisEffect(Map<Holder<Substance>, Integer> products) {
        this.products = products;
    }

    public SynthesisEffect() {
        this(new HashMap<>());
    }

    public SynthesisEffect addProduct(Holder<Substance> substance, int proportion) {
        products.put(substance, proportion);
        return this;
    }

    @Override
    public ReactionEffectType<?> getType() {
        return SYNTHESIS.get();
    }

    @Override
    public void apply(Reactor reactor, Level level, BlockPos pos, int amount) {
        for (var entry : products.entrySet()) {
            SubstanceStack stack = entry.getKey().value().toStack();
            stack.setAmount(amount * entry.getValue());
            reactor.updateSubstanceNoUpdate(stack);
        }
    }

    public Map<Holder<Substance>, Integer> getProducts() {
        return products;
    }
}
