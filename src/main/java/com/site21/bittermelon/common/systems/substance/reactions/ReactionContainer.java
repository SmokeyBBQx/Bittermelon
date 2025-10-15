package com.site21.bittermelon.common.systems.substance.reactions;

import com.site21.bittermelon.common.systems.substance.SubstanceStack;

public interface ReactionContainer {
    float getTemperature();
    float getHeatCapacity();
    void modifyTemperature(float temperature);
    void updateSubstance(SubstanceStack stack);
}
