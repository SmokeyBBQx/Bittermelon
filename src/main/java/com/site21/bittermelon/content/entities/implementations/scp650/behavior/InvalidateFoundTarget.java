package com.site21.bittermelon.content.entities.implementations.scp650.behavior;

import com.mojang.datafixers.util.Pair;
import com.site21.bittermelon.content.entities.implementations.scp650.SCP650;
import com.site21.bittermelon.init.neoforge.BitterSounds;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.core.Holder;
import net.minecraft.network.protocol.game.ClientboundSoundPacket;
import net.minecraft.server.level.ServerPlayer;
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
    private static final List<Holder<SoundEvent>> SCARE_SOUNDS = List.of(SCARE_1, SCARE_2, SCARE_3, SCARE_4);

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

            if (player instanceof ServerPlayer serverPlayer) {
                serverPlayer.connection.send(new ClientboundSoundPacket(
                        SCARE_SOUNDS.get(random.nextInt(SCARE_SOUNDS.size())),
                        SoundSource.HOSTILE,
                        player.getX(),
                        player.getY(),
                        player.getZ(),
                        0.3f,
                        1,
                        player.level().getRandom().nextLong()));
            }
        }

        // If the entity has been observed after it has teleported to its target, clear the target to let it find a new one.
        BrainUtils.clearMemory(entity, SCARE_TARGET.get());
    }
}
