package com.site21.bittermelon.entities.behavior.needs;

import com.mojang.datafixers.util.Pair;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.tslat.smartbrainlib.api.core.behaviour.ExtendedBehaviour;
import net.tslat.smartbrainlib.api.core.behaviour.GroupBehaviour;
import net.tslat.smartbrainlib.object.SBLShufflingList;
import org.jetbrains.annotations.Nullable;

public class PrioritizedNeedsBehavior<E extends LivingEntity> extends GroupBehaviour<E> {
    @SafeVarargs
    public PrioritizedNeedsBehavior(Pair<ExtendedBehaviour<? super E>, Integer>... behaviours) {
        super(behaviours);
    }

    @Override
    protected @Nullable ExtendedBehaviour<? super E> pickBehaviour(ServerLevel level, E entity, long gameTime, SBLShufflingList<ExtendedBehaviour<? super E>> extendedBehaviours) {
        return null;
    }
}
