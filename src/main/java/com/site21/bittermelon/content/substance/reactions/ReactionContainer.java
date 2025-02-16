package com.site21.bittermelon.content.substance.reactions;

import com.site21.bittermelon.content.substance.SubstanceStack;

public interface ReactionContainer {
    float getTemperature();
    float getHeatCapacity();
    void modifyTemperature(float temperature);
    void updateSubstance(SubstanceStack stack);
}
