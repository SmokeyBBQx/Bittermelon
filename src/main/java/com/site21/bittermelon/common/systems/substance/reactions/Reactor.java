package com.site21.bittermelon.common.systems.substance.reactions;

import com.site21.bittermelon.common.systems.substance.Nature;
import com.site21.bittermelon.common.systems.substance.SubstanceStack;

import java.util.Collection;
import java.util.EnumSet;

public interface Reactor {
    void updateSubstance(SubstanceStack stack);

    void removeSubstance(SubstanceStack stack, int amount);

    int getAmount();

    Collection<SubstanceStack> getSubstances();

    default EnumSet<Nature> getNatures() {
        EnumSet<Nature> natures = EnumSet.noneOf(Nature.class);
        for (SubstanceStack stack : getSubstances()) {
            natures.addAll(stack.getSubstance().getNature().keySet());
        }
        return natures;
    }
}
