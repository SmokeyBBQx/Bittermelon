package com.site21.bittermelon.common.systems.chemistry.effects;

import com.site21.bittermelon.common.systems.chemistry.ReactionEffect;
import com.site21.bittermelon.common.systems.chemistry.Reactor;
import com.site21.bittermelon.common.systems.substance.Substance;
import com.site21.bittermelon.common.systems.substance.SubstanceStack;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;

import java.util.HashMap;
import java.util.Map;

public class ProductionEffect implements ReactionEffect {
    private final Map<Substance, Integer> products;

    public ProductionEffect(Map<Substance, Integer> products) {
        this.products = products;
    }

    public ProductionEffect() {
        this(new HashMap<>());
    }

    public ProductionEffect addProduct(Substance substance, int proportion) {
        products.put(substance, proportion);
        return this;
    }

    @Override
    public void apply(Reactor reactor, Level level, BlockPos pos, int amount) {
        for (var entry : products.entrySet()) {
            SubstanceStack stack = entry.getKey().toStack();
            stack.setAmount(amount * entry.getValue());
            reactor.updateSubstance(stack);
        }
    }
}
