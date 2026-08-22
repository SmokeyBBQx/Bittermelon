package com.site21.bittermelon.common.content.entities.scp939;

import com.site21.bittermelon.common.systems.ai.base.BitterMob;
import com.site21.bittermelon.common.systems.ai.base.Need;
import com.site21.bittermelon.common.systems.ai.base.NeedInstance;
import com.site21.bittermelon.common.systems.character.Character;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.behavior.BehaviorControl;
import net.minecraft.world.level.Level;
import net.tslat.smartbrainlib.api.core.sensor.ExtendedSensor;

import java.util.List;
import java.util.Map;

public class SCP939 extends BitterMob {
    public SCP939(EntityType entityType, Level level) {
        super(entityType, level);
    }

    @Override
    protected Character initializeCharacter() {
        return new Character(uuid, "SCP-939-" + getRandom().nextInt(1, 24));
    }

    @Override
    protected Map<Need, NeedInstance> initializeNeeds() {
        return Map.of();
    }

    @Override
    public List<? extends ExtendedSensor<?>> getSensors(LivingEntity owner) {
        return List.of();
    }

    @Override
    public List<? extends BehaviorControl<?>> getAlwaysRunningBehaviours(LivingEntity owner) {
        return super.getAlwaysRunningBehaviours(owner);
    }

    @Override
    public List<? extends BehaviorControl<?>> getIdleBehaviours(LivingEntity owner) {
        return super.getIdleBehaviours(owner);
    }

    @Override
    public List<? extends BehaviorControl<?>> getFightingBehaviours(LivingEntity owner) {
        return super.getFightingBehaviours(owner);
    }
}
