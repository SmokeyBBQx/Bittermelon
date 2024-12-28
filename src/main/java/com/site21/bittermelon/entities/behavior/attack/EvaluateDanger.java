package com.site21.bittermelon.entities.behavior.attack;

import com.mojang.datafixers.util.Pair;
import com.site21.bittermelon.character.Character;
import com.site21.bittermelon.character.CharacterManager;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.tslat.smartbrainlib.api.core.behaviour.ExtendedBehaviour;
import net.tslat.smartbrainlib.api.core.behaviour.GroupBehaviour;
import net.tslat.smartbrainlib.object.SBLShufflingList;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class EvaluateDanger<E extends LivingEntity> extends GroupBehaviour<E> {
    @Nullable
    protected LivingEntity target = null;

    @SafeVarargs
    public EvaluateDanger(ExtendedBehaviour<? super E>... behaviours) {
        super(behaviours);
    }

    @Override
    protected List<Pair<MemoryModuleType<?>, MemoryStatus>> getMemoryRequirements() {
        return List.of();
    }

    @Override
    protected @Nullable ExtendedBehaviour<? super E> pickBehaviour(ServerLevel level, E entity, long gameTime, SBLShufflingList<ExtendedBehaviour<? super E>> extendedBehaviours) {
        if (this.target == null) return null;

        CharacterManager characterManager = CharacterManager.getInstance();
        com.site21.bittermelon.character.Character entityCharacter = characterManager.getActiveCharacter(entity.getUUID());
        Character targetCharacter = characterManager.getActiveCharacter(target.getUUID());

        if (entityCharacter == null || targetCharacter == null) return null;

        float danger = 0;

        double entitySize = entity.getHitbox().getSize();
        double targetSize = target.getHitbox().getSize();

        danger += (float) (targetSize / entitySize);
        danger += entityCharacter.getMedicalStats().getPain();

        return null;
    }


}
