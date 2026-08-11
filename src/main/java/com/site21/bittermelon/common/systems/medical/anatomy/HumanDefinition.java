package com.site21.bittermelon.common.systems.medical.anatomy;

import com.site21.bittermelon.common.systems.medical.bodypart.HealthContainer;
import com.site21.bittermelon.common.systems.medical.bodypart.LimbSlot;
import com.site21.bittermelon.common.systems.medical.bodypart.PartInstance;
import net.minecraft.world.entity.Entity;

import static com.site21.bittermelon.init.custom.BodyParts.*;

public class HumanDefinition implements AnatomyDefinition {
    @Override
    public HealthContainer createHealthContainer(Entity entity) {
        PartInstance torso = TORSO.get().toInstance(LimbSlot.BODY);
        HealthContainer healthContainer = new HealthContainer(torso);
        PartInstance leftArm = LEFT_ARM.get().toInstance(LimbSlot.LEFT_ARM);
        PartInstance rightArm = ZOMBIE_RIGHT_ARM.get().toInstance(LimbSlot.RIGHT_ARM);
        PartInstance leftLeg = LEFT_LEG.get().toInstance(LimbSlot.LEFT_LEG);
        PartInstance rightLeg = RIGHT_LEG.get().toInstance(LimbSlot.RIGHT_LEG);
        PartInstance head = HEAD.get().toInstance(LimbSlot.HEAD);
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
