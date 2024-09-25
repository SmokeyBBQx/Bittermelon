package com.site21.bittermelon.entities.scps.SCP939;

import com.site21.bittermelon.init.MemoryModuleTypeInit;
import net.minecraft.util.Unit;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.ai.behavior.BehaviorControl;
import net.minecraft.world.entity.ai.behavior.declarative.BehaviorBuilder;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;

import static com.site21.bittermelon.init.MemoryModuleTypeInit.IS_LISTENING;

public class TryToListen {
    private static final IntProvider LISTEN_COOLDOWN = UniformInt.of(100, 200);

    public static BehaviorControl<LivingEntity> create() {
        return BehaviorBuilder.create(
                p_259979_ -> p_259979_.group(
                                p_259979_.registered(IS_LISTENING.get()),
                                p_259979_.registered(MemoryModuleType.WALK_TARGET),
                                p_259979_.absent(MemoryModuleTypeInit.LISTEN_COOLDOWN.get()),
                                p_259979_.present(MemoryModuleType.NEAREST_ATTACKABLE),
                                p_259979_.absent(MemoryModuleType.DISTURBANCE_LOCATION)
                        )
                        .apply(p_259979_, (p_260219_, p_260252_, p_260090_, p_259577_, p_260020_) -> (p_325756_, p_325757_, p_325758_) -> {
                            p_260219_.set(Unit.INSTANCE);
                            p_260090_.setWithExpiry(Unit.INSTANCE, LISTEN_COOLDOWN.sample(p_325756_.getRandom()));
                            p_260252_.erase();
                            return true;
                        })
        );
    }
}
