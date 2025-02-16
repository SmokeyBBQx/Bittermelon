package com.site21.bittermelon.content.entities.ai.behavior.mood.mentalbreak;

import com.mojang.datafixers.util.Pair;
import com.site21.bittermelon.content.character.Character;
import com.site21.bittermelon.content.character.CharacterManager;
import com.site21.bittermelon.util.LocalMessageHelper;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.tslat.smartbrainlib.api.core.behaviour.ExtendedBehaviour;

import java.util.List;

public class WarnHighStress<E extends LivingEntity> extends ExtendedBehaviour<E> {
    protected final List<String> messages;

    @Override
    protected List<Pair<MemoryModuleType<?>, MemoryStatus>> getMemoryRequirements() {
        return List.of();
    }

    public WarnHighStress(List<String> messages) {
        this.messages = messages;
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
            LocalMessageHelper.sendLocalMessage(entity, 10, Component.literal(character.getName() + message)
                    .withColor(character.getEmoteColor()));
        }
    }
}
