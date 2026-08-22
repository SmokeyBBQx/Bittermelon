package com.site21.bittermelon.common.content.entities.chicken.behavior;

import com.site21.bittermelon.common.content.entities.chicken.Chicken;
import com.site21.bittermelon.common.systems.ai.base.Need;
import com.site21.bittermelon.common.systems.character.Character;
import com.site21.bittermelon.common.systems.character.CharacterManager;
import com.site21.bittermelon.util.LocalMessageUtil;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.ai.behavior.declarative.MemoryCondition;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.WalkTarget;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.tslat.smartbrainlib.api.core.behaviour.base.ExtendedBehaviour;
import net.tslat.smartbrainlib.library.object.MemoryTest;
import net.tslat.smartbrainlib.registry.SBLMemoryTypes;
import net.tslat.smartbrainlib.util.BrainUtil;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Set;

public class PluckAtRandomItem<E extends Chicken> extends ExtendedBehaviour<E> {
    private static final MemoryTest MEMORY_REQUIREMENTS = MemoryTest.builder()
            .hasMemory(SBLMemoryTypes.NEARBY_ITEMS.get());

    @Override
    public Set<MemoryCondition<?, ?>> getMemoryRequirements() {
        return MEMORY_REQUIREMENTS;
    }

    @Override
    protected void start(@NotNull E entity) {
        List<ItemEntity> nearbyItems = entity.getBrain().getMemory(SBLMemoryTypes.NEARBY_ITEMS.get()).orElse(null);
        if (nearbyItems == null) return;

        ItemEntity itemEntity = nearbyItems.getFirst();
        ItemStack stack = itemEntity.getItem();

        if (entity.distanceToSqr(itemEntity) < 1) {
            entity.getNavigation().stop();
            CharacterManager characterManager = CharacterManager.get(entity.level());
            Character entityCharacter = characterManager.getActiveCharacter(entity);

            if (entityCharacter != null) {
                LocalMessageUtil.sendLocalMessage(entity, 10,
                        Component.literal(entityCharacter.getName() + " plucks at " + stack.getHoverName().getString().toLowerCase() + ".")
                                .withColor(entityCharacter.getEmoteColor())
                );
            }

            entity.modifyNeed(Need.RECREATION, -20);
        } else {
            BrainUtil.setMemory(entity, MemoryModuleType.WALK_TARGET, new WalkTarget(itemEntity.blockPosition(), 1, 0));
        }
    }
}
