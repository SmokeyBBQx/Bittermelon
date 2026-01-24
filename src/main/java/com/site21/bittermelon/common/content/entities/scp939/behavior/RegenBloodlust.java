package com.site21.bittermelon.common.content.entities.scp939.behavior;

import com.mojang.datafixers.util.Pair;
import com.site21.bittermelon.common.content.entities.scp939.SCP939;
import com.site21.bittermelon.common.systems.ai.base.Need;
import com.site21.bittermelon.common.systems.character.Character;
import com.site21.bittermelon.common.systems.character.CharacterManager;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.player.Player;
import net.tslat.smartbrainlib.api.core.behaviour.ExtendedBehaviour;
import net.tslat.smartbrainlib.util.BrainUtil;

import java.util.List;

import static com.site21.bittermelon.init.neoforge.BitterAttachmentTypes.MEDICAL_STATS;

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
        LivingEntity target = BrainUtil.getTargetOfEntity(entity);

        if (target == null)
            return;

        Character targetCharacter = CharacterManager.get(entity.level()).getActiveCharacter(target);
        if (targetCharacter == null) return;

        if (target.getData(MEDICAL_STATS).getConsciousness() < 0.1f) {
            entity.modifyNeed(Need.BLOODLUST, -30);
        }

        if (target instanceof Player) {
            entity.addVictim(targetCharacter);
        }
    }
}
