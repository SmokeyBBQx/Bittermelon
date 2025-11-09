package com.site21.bittermelon.common.systems.ai.behavior.misc;

import com.mojang.datafixers.util.Pair;
import com.site21.bittermelon.common.systems.character.Character;
import com.site21.bittermelon.common.systems.character.CharacterManager;
import com.site21.bittermelon.common.content.entities.base.NeedsUser;
import com.site21.bittermelon.common.content.entities.base.Need;
import com.site21.bittermelon.util.LocalMessageHelper;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.tslat.smartbrainlib.api.core.behaviour.ExtendedBehaviour;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class Defecate<E extends Mob & NeedsUser> extends ExtendedBehaviour<E> {
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

        entity.modifyNeed(Need.DEFECATION, -100);
    }
}
