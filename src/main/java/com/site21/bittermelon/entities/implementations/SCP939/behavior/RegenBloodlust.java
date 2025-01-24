package com.site21.bittermelon.entities.implementations.SCP939.behavior;

import com.mojang.datafixers.util.Pair;
import com.site21.bittermelon.character.Character;
import com.site21.bittermelon.character.CharacterManager;
import com.site21.bittermelon.entities.implementations.SCP939.SCP939;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.player.Player;
import net.tslat.smartbrainlib.api.core.behaviour.ExtendedBehaviour;
import net.tslat.smartbrainlib.util.BrainUtils;

import java.util.List;

public class RegenBloodlust<E extends SCP939> extends ExtendedBehaviour<E> {
    private static final List<Pair<MemoryModuleType<?>, MemoryStatus>> MEMORY_REQUIREMENTS = ObjectArrayList.of(
            Pair.of(MemoryModuleType.ATTACK_TARGET, MemoryStatus.VALUE_PRESENT)
    );

    @Override
    protected List<Pair<MemoryModuleType<?>, MemoryStatus>> getMemoryRequirements() {
        return MEMORY_REQUIREMENTS;
    }

    @Override
    protected void start(E entity) {
        LivingEntity target = BrainUtils.getTargetOfEntity(entity);

        if (target == null)
            return;

        Character targetCharacter = CharacterManager.getInstance().getActiveCharacter(target.getUUID());
        if (targetCharacter == null) return;

        if (targetCharacter.getMedicalStats().getConsciousness() < 0.1f) {
            entity.modifyBloodlust(-30);
        }

        if (target instanceof Player) {
            entity.addVictim(targetCharacter);
        }
    }
}
