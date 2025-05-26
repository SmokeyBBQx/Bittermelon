package com.site21.bittermelon.content.entities.implementations.scp650.behavior;

import com.mojang.datafixers.util.Pair;
import com.site21.bittermelon.content.entities.implementations.scp650.SCP650;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.player.Player;
import net.tslat.smartbrainlib.api.core.behaviour.ExtendedBehaviour;
import net.tslat.smartbrainlib.util.BrainUtils;
import org.jetbrains.annotations.NotNull;

import java.util.List;

import static com.site21.bittermelon.init.neoforge.BitterMemoryTypes.SCARE_TARGET;
import static com.site21.bittermelon.init.neoforge.BitterMemoryTypes.OBSERVERS;
import static com.site21.bittermelon.init.neoforge.BitterSounds.*;

public class InvalidateFoundTarget<E extends SCP650> extends ExtendedBehaviour<E> {
    public static final SoundEvent[] SCARE_SOUNDS = {SCARE_1.get(), SCARE_2.get(), SCARE_3.get(), SCARE_4.get()};

    private static final List<Pair<MemoryModuleType<?>, MemoryStatus>> MEMORY_REQUIREMENTS = ObjectArrayList.of(
            Pair.of(SCARE_TARGET.get(), MemoryStatus.VALUE_PRESENT),
            Pair.of(OBSERVERS.get(), MemoryStatus.VALUE_PRESENT)
    );

    @Override
    protected List<Pair<MemoryModuleType<?>, MemoryStatus>> getMemoryRequirements() {
        return MEMORY_REQUIREMENTS;
    }

    @Override
    protected void start(@NotNull E entity) {
        if (entity.level().isClientSide) return;
        Player player = BrainUtils.getMemory(entity, SCARE_TARGET.get());
        List<LivingEntity> observers = BrainUtils.getMemory(entity, OBSERVERS.get());

        // Checks if one of the observes is the target themselves. If so, it'll play a scare sound.
        if (observers != null && observers.contains(player)) {
            if (player == null) return;
            RandomSource random = player.getRandom();

            // TODO: Playsound won't work for the individual player
            entity.level().playSound(
                    null,
                    player.getOnPos(),
                    SCARE_SOUNDS[random.nextInt(SCARE_SOUNDS.length)],
                    SoundSource.HOSTILE,
                    0.8f,
                    Mth.randomBetween(random, 0.95f, 1f)
            );
        }

        // If the entity has been observed after it has teleported to its target, clear the target to let it find a new one.
        BrainUtils.clearMemory(entity, SCARE_TARGET.get());
    }
}
