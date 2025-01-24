package com.site21.bittermelon.entities.ai.behavior.basicneeds;

import com.mojang.datafixers.util.Pair;
import com.site21.bittermelon.character.Character;
import com.site21.bittermelon.character.CharacterManager;
import com.site21.bittermelon.entities.ai.behavior.social.interactions.GenericInteraction;
import com.site21.bittermelon.util.LocalMessageHelper;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.tslat.smartbrainlib.api.core.behaviour.DelayedBehaviour;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Random;

public class Groom<E extends Mob & HasBasicNeeds> extends DelayedBehaviour<E> {
    protected List<String> messages;

    public Groom(int delayTicks) {
        super(delayTicks);
    }

    @Override
    protected List<Pair<MemoryModuleType<?>, MemoryStatus>> getMemoryRequirements() {
        return List.of();
    }

    public Groom<E> messages(List<String> messages) {
        this.messages = messages;

        return this;
    }

    @Override
    protected void doDelayedAction(@NotNull E entity) {
        entity.modifyHygiene(-30);

        if (messages != null && !messages.isEmpty()) {
            CharacterManager characterManager = CharacterManager.getInstance();
            com.site21.bittermelon.character.Character entityCharacter = characterManager.getActiveCharacter(entity.getUUID());

            if (entityCharacter != null) {
                Random random = new Random();
                String message = messages.get(random.nextInt(messages.size()));
                LocalMessageHelper.sendLocalMessage(entity, 10,
                        Component.literal(entityCharacter.getName() + message)
                                .withColor(entityCharacter.getEmoteColor())
                );
            }
        }
    }
}
