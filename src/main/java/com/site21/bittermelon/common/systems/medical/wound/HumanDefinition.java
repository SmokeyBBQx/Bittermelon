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
        torso.attachPart(TORSO.get().attachmentPoints().get(0), head);
        torso.attachPart(TORSO.get().attachmentPoints().get(1), rightArm);
        torso.attachPart(TORSO.get().attachmentPoints().get(2), leftArm);
        torso.attachPart(TORSO.get().attachmentPoints().get(3), rightLeg);
        torso.attachPart(TORSO.get().attachmentPoints().get(4), leftLeg);
        head.setParent(torso);
        rightArm.setParent(torso);
        leftArm.setParent(torso);
        rightLeg.setParent(torso);
        leftLeg.setParent(torso);
        healthContainer.addPart(head);
        healthContainer.addPart(torso);
        healthContainer.addPart(rightArm);
        healthContainer.addPart(leftArm);
        healthContainer.addPart(rightLeg);
        healthContainer.addPart(leftLeg);
        return healthContainer;
    }
}
