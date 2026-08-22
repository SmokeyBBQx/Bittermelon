package com.site21.bittermelon.common.systems.ai.behavior.mentalbreak;

import com.site21.bittermelon.common.systems.character.Character;
import com.site21.bittermelon.common.systems.character.CharacterManager;
import com.site21.bittermelon.util.LocalMessageUtil;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.behavior.declarative.MemoryCondition;
import net.tslat.smartbrainlib.api.core.behaviour.base.ExtendedBehaviour;

import java.util.List;
import java.util.Set;

public class WarnHighStress<E extends LivingEntity> extends ExtendedBehaviour<E> {
    protected final List<String> messages;


    public WarnHighStress(List<String> messages) {
        this.messages = messages;
    }

    @Override
    public Set<MemoryCondition<?, ?>> getMemoryRequirements() {
        return Set.of();
    }

    @Override
    protected void start(E entity) {
        sendRandomMessage(entity);
    }

    protected void sendRandomMessage(E entity) {
        if (!messages.isEmpty()) {
            CharacterManager characterManager = CharacterManager.get(entity.level());
            Character character = characterManager.getActiveCharacter(entity);
            if (character == null) return;

            String message = this.messages.get(entity.getRandom().nextInt(messages.size()));
            LocalMessageUtil.sendLocalMessage(entity, 10, Component.literal(character.getName() + message)
                    .withColor(character.getEmoteColor()));
        }
    }
}
