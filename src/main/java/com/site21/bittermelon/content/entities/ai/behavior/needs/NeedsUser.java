package com.site21.bittermelon.content.entities.ai.behavior.needs;

import net.minecraft.world.entity.LivingEntity;

import java.util.List;

public interface NeedsUser<E extends LivingEntity & NeedsUser<E>> {
    List<Need<E>> getNeeds();
    float getMood();
    float getStress();
    void modifyStress(float amount);
    void setStress(float amount);

    default void updateStress() {
        if (getMood() < 40) {
            modifyStress(-0.01f);
        }
    }
}
