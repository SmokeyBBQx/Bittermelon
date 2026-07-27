package com.site21.bittermelon.common.systems.medical.wound;

import net.minecraft.world.entity.Entity;

import static com.site21.bittermelon.init.custom.BodyParts.*;

public class HumanDefinition implements AnatomyDefinition {
    @Override
    public HealthContainer createHealthContainer(Entity entity) {
        PartInstance torso = TORSO.get().toInstance();
        HealthContainer healthContainer = new HealthContainer(torso);
        PartInstance leftArm = LEFT_ARM.get().toInstance();
        PartInstance rightArm = RIGHT_ARM.get().toInstance();
        PartInstance leftLeg = LEFT_LEG.get().toInstance();
        PartInstance rightLeg = RIGHT_LEG.get().toInstance();
        PartInstance head = HEAD.get().toInstance();
        healthContainer.addPart(torso);
        return healthContainer;
    }
}
