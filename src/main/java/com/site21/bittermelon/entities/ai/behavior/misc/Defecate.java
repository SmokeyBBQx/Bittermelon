package com.site21.bittermelon.entities.ai.behavior.misc;

import com.mojang.datafixers.util.Pair;
import com.site21.bittermelon.character.Character;
import com.site21.bittermelon.character.CharacterManager;
import com.site21.bittermelon.entities.ai.behavior.basicneeds.HasBasicNeeds;
import com.site21.bittermelon.util.LocalMessageHelper;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.tslat.smartbrainlib.api.core.behaviour.ExtendedBehaviour;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class Defecate<E extends Mob & HasBasicNeeds> extends ExtendedBehaviour<E> {
    @Override
    protected List<Pair<MemoryModuleType<?>, MemoryStatus>> getMemoryRequirements() {
        return List.of();
    }

    protected void start(@NotNull E entity) {
        CharacterManager characterManager = CharacterManager.get(entity.level());
        Character entityCharacter = characterManager.getActiveCharacter(entity);

        if (entityCharacter != null) {
            int textColor = entityCharacter.getEmoteColor();

            LocalMessageHelper.sendLocalMessage(entity, 10, Component.literal(
                    entityCharacter.getName() + " poops.").withColor(textColor));
        }

        entity.modifyDefecation(-100);
    }
}
