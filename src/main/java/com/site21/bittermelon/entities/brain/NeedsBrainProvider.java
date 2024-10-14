package com.site21.bittermelon.entities.brain;

import com.mojang.serialization.Dynamic;
import net.minecraft.world.entity.LivingEntity;
import net.tslat.smartbrainlib.api.SmartBrainOwner;
import net.tslat.smartbrainlib.api.core.SmartBrain;
import net.tslat.smartbrainlib.api.core.SmartBrainProvider;

public class NeedsBrainProvider<E extends LivingEntity & SmartBrainOwner<E>> extends SmartBrainProvider<E> {
    public NeedsBrainProvider(E owner) {
        super(owner);
    }
//
//    @Override
//    public final NeedsBrain<E> makeBrain(Dynamic<?> codecLoader) {
//
//    }
}
