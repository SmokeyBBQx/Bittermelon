package com.site21.bittermelon.systems.ai.vibration;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import it.unimi.dsi.fastutil.objects.Reference2IntOpenHashMap;
import net.minecraft.Util;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.GameEventTags;
import net.minecraft.tags.TagKey;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.ClipBlockStateContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.gameevent.GameEventListener;
import net.minecraft.world.level.gameevent.PositionSource;
import net.minecraft.world.level.gameevent.vibrations.VibrationInfo;
import net.minecraft.world.level.gameevent.vibrations.VibrationSelector;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Optional;
import java.util.function.ToIntFunction;

public interface BitterVibrationSystem {
    List<ResourceKey<GameEvent>> RESONANCE_EVENTS = List.of(
            GameEvent.RESONATE_1.key(),
            GameEvent.RESONATE_2.key(),
            GameEvent.RESONATE_3.key(),
            GameEvent.RESONATE_4.key(),
            GameEvent.RESONATE_5.key(),
            GameEvent.RESONATE_6.key(),
            GameEvent.RESONATE_7.key(),
            GameEvent.RESONATE_8.key(),
            GameEvent.RESONATE_9.key(),
            GameEvent.RESONATE_10.key(),
            GameEvent.RESONATE_11.key(),
            GameEvent.RESONATE_12.key(),
            GameEvent.RESONATE_13.key(),
            GameEvent.RESONATE_14.key(),
            GameEvent.RESONATE_15.key()
    );
    int DEFAULT_VIBRATION_FREQUENCY = 0;
    /** @deprecated Neo: use the {@link net.neoforged.neoforge.registries.datamaps.builtin.NeoForgeDataMaps#VIBRATION_FREQUENCIES data map} instead. */
    @Deprecated
    ToIntFunction<ResourceKey<GameEvent>> VIBRATION_FREQUENCY_FOR_EVENT = Util.make(new Reference2IntOpenHashMap<>(), p_316653_ -> {
        p_316653_.defaultReturnValue(0);
        p_316653_.put(GameEvent.STEP.key(), 1);
        p_316653_.put(GameEvent.SWIM.key(), 1);
        p_316653_.put(GameEvent.FLAP.key(), 1);
        p_316653_.put(GameEvent.PROJECTILE_LAND.key(), 2);
        p_316653_.put(GameEvent.HIT_GROUND.key(), 2);
        p_316653_.put(GameEvent.SPLASH.key(), 2);
        p_316653_.put(GameEvent.ITEM_INTERACT_FINISH.key(), 3);
        p_316653_.put(GameEvent.PROJECTILE_SHOOT.key(), 3);
        p_316653_.put(GameEvent.INSTRUMENT_PLAY.key(), 3);
        p_316653_.put(GameEvent.ENTITY_ACTION.key(), 4);
        p_316653_.put(GameEvent.ELYTRA_GLIDE.key(), 4);
        p_316653_.put(GameEvent.UNEQUIP.key(), 4);
        p_316653_.put(GameEvent.ENTITY_DISMOUNT.key(), 5);
        p_316653_.put(GameEvent.EQUIP.key(), 5);
        p_316653_.put(GameEvent.ENTITY_INTERACT.key(), 6);
        p_316653_.put(GameEvent.SHEAR.key(), 6);
        p_316653_.put(GameEvent.ENTITY_MOUNT.key(), 6);
        p_316653_.put(GameEvent.ENTITY_DAMAGE.key(), 7);
        p_316653_.put(GameEvent.DRINK.key(), 8);
        p_316653_.put(GameEvent.EAT.key(), 8);
        p_316653_.put(GameEvent.CONTAINER_CLOSE.key(), 9);
        p_316653_.put(GameEvent.BLOCK_CLOSE.key(), 9);
        p_316653_.put(GameEvent.BLOCK_DEACTIVATE.key(), 9);
        p_316653_.put(GameEvent.BLOCK_DETACH.key(), 9);
        p_316653_.put(GameEvent.CONTAINER_OPEN.key(), 10);
        p_316653_.put(GameEvent.BLOCK_OPEN.key(), 10);
        p_316653_.put(GameEvent.BLOCK_ACTIVATE.key(), 10);
        p_316653_.put(GameEvent.BLOCK_ATTACH.key(), 10);
        p_316653_.put(GameEvent.PRIME_FUSE.key(), 10);
        p_316653_.put(GameEvent.NOTE_BLOCK_PLAY.key(), 10);
        p_316653_.put(GameEvent.BLOCK_CHANGE.key(), 11);
        p_316653_.put(GameEvent.BLOCK_DESTROY.key(), 12);
        p_316653_.put(GameEvent.FLUID_PICKUP.key(), 12);
        p_316653_.put(GameEvent.BLOCK_PLACE.key(), 13);
        p_316653_.put(GameEvent.FLUID_PLACE.key(), 13);
        p_316653_.put(GameEvent.ENTITY_PLACE.key(), 14);
        p_316653_.put(GameEvent.LIGHTNING_STRIKE.key(), 14);
        p_316653_.put(GameEvent.TELEPORT.key(), 14);
        p_316653_.put(GameEvent.ENTITY_DIE.key(), 15);
        p_316653_.put(GameEvent.EXPLODE.key(), 15);

        for (int i = 1; i <= 15; i++) {
            p_316653_.put(getResonanceEventByFrequency(i), i);
        }
    });

    BitterVibrationSystem.Data getVibrationData();

    BitterVibrationSystem.User getVibrationUser();

    static int getGameEventFrequency(Holder<GameEvent> gameEvent) {
        var data = gameEvent.getData(net.neoforged.neoforge.registries.datamaps.builtin.NeoForgeDataMaps.VIBRATION_FREQUENCIES);
        return data != null ? data.frequency() : 0;
    }

    static ResourceKey<GameEvent> getResonanceEventByFrequency(int frequency) {
        return RESONANCE_EVENTS.get(frequency - 1);
    }

    static int getRedstoneStrengthForDistance(float distance, int maxDistance) {
        double d0 = 15.0 / (double)maxDistance;
        return Math.max(1, 15 - Mth.floor(d0 * (double)distance));
    }

    public static final class Data {
        public static Codec<BitterVibrationSystem.Data> CODEC = RecordCodecBuilder.create(
                p_338090_ -> p_338090_.group(
                                VibrationInfo.CODEC.lenientOptionalFieldOf("event").forGetter(p_281665_ -> Optional.ofNullable(p_281665_.currentVibration)),
                                VibrationSelector.CODEC.fieldOf("selector").forGetter(BitterVibrationSystem.Data::getSelectionStrategy),
                                ExtraCodecs.NON_NEGATIVE_INT.fieldOf("event_delay").orElse(0).forGetter(BitterVibrationSystem.Data::getTravelTimeInTicks)
                        )
                        .apply(p_338090_, (p_281934_, p_282381_, p_282931_) -> new BitterVibrationSystem.Data(p_281934_.orElse(null), p_282381_, p_282931_))
        );
        public static final String NBT_TAG_KEY = "listener";
        @Nullable
        VibrationInfo currentVibration;
        private int travelTimeInTicks;
        final VibrationSelector selectionStrategy;

        private Data(@Nullable VibrationInfo currentVibration, VibrationSelector selectionStrategy, int travelTimeInTicks) {
            this.currentVibration = currentVibration;
            this.travelTimeInTicks = travelTimeInTicks;
            this.selectionStrategy = selectionStrategy;
        }

        public Data() {
            this(null, new VibrationSelector(), 0);
        }

        public VibrationSelector getSelectionStrategy() {
            return this.selectionStrategy;
        }

        @Nullable
        public VibrationInfo getCurrentVibration() {
            return this.currentVibration;
        }

        public void setCurrentVibration(@Nullable VibrationInfo currentVibration) {
            this.currentVibration = currentVibration;
        }

        public int getTravelTimeInTicks() {
            return this.travelTimeInTicks;
        }

        public void setTravelTimeInTicks(int travelTimeInTicks) {
            this.travelTimeInTicks = travelTimeInTicks;
        }

        public void decrementTravelTime() {
            this.travelTimeInTicks = Math.max(0, this.travelTimeInTicks - 1);
        }
    }

    public static class Listener implements GameEventListener {
        private final BitterVibrationSystem system;

        public Listener(BitterVibrationSystem system) {
            this.system = system;
        }

        @Override
        public PositionSource getListenerSource() {
            return this.system.getVibrationUser().getPositionSource();
        }

        @Override
        public int getListenerRadius() {
            return this.system.getVibrationUser().getListenerRadius();
        }

        @Override
        public boolean handleGameEvent(ServerLevel level, Holder<GameEvent> gameEvent, GameEvent.Context context, Vec3 pos) {
            BitterVibrationSystem.Data vibrationsystem$data = this.system.getVibrationData();
            BitterVibrationSystem.User vibrationsystem$user = this.system.getVibrationUser();
            if (vibrationsystem$data.getCurrentVibration() != null) {
                return false;
            } else if (!vibrationsystem$user.isValidVibration(gameEvent, context)) {
                return false;
            } else {
                Optional<Vec3> optional = vibrationsystem$user.getPositionSource().getPosition(level);
                if (optional.isEmpty()) {
                    return false;
                } else {
                    Vec3 vec3 = optional.get();
                    if (!vibrationsystem$user.canReceiveVibration(level, BlockPos.containing(pos), gameEvent, context)) {
                        return false;
                    } else if (isOccluded(level, pos, vec3)) {
                        return false;
                    } else {
                        this.scheduleVibration(level, vibrationsystem$data, gameEvent, context, pos, vec3);
                        return true;
                    }
                }
            }
        }

        public void forceScheduleVibration(ServerLevel level, Holder<GameEvent> gameEvent, GameEvent.Context context, Vec3 pos) {
            this.system
                    .getVibrationUser()
                    .getPositionSource()
                    .getPosition(level)
                    .ifPresent(p_316103_ -> this.scheduleVibration(level, this.system.getVibrationData(), gameEvent, context, pos, p_316103_));
        }

        private void scheduleVibration(
                ServerLevel level, BitterVibrationSystem.Data data, Holder<GameEvent> gameEvent, GameEvent.Context context, Vec3 pos, Vec3 sensorPos
        ) {
            data.selectionStrategy
                    .addCandidate(
                            new VibrationInfo(gameEvent, (float)pos.distanceTo(sensorPos), pos, context.sourceEntity()), level.getGameTime()
                    );
        }

        public static float distanceBetweenInBlocks(BlockPos pos1, BlockPos pos2) {
            return (float)Math.sqrt(pos1.distSqr(pos2));
        }

        private static boolean isOccluded(Level level, Vec3 eventPos, Vec3 vibrationUserPos) {
            Vec3 vec3 = new Vec3((double)Mth.floor(eventPos.x) + 0.5, (double)Mth.floor(eventPos.y) + 0.5, (double)Mth.floor(eventPos.z) + 0.5);
            Vec3 vec31 = new Vec3((double)Mth.floor(vibrationUserPos.x) + 0.5, (double)Mth.floor(vibrationUserPos.y) + 0.5, (double)Mth.floor(vibrationUserPos.z) + 0.5);

            for (Direction direction : Direction.values()) {
                Vec3 vec32 = vec3.relative(direction, 1.0E-5F);
                if (level.isBlockInLine(new ClipBlockStateContext(vec32, vec31, p_283608_ -> p_283608_.is(BlockTags.OCCLUDES_VIBRATION_SIGNALS))).getType()
                        != HitResult.Type.BLOCK) {
                    return false;
                }
            }

            return true;
        }
    }

    public interface Ticker {
        static void tick(Level level, BitterVibrationSystem.Data data, BitterVibrationSystem.User user) {
            if (level instanceof ServerLevel serverlevel) {
                if (data.currentVibration == null) {
                    trySelectAndScheduleVibration(serverlevel, data, user);
                }

                if (data.currentVibration != null) {
                    boolean flag = data.getTravelTimeInTicks() > 0;
                    data.decrementTravelTime();
                    if (data.getTravelTimeInTicks() <= 0) {
                        flag = receiveVibration(serverlevel, data, user, data.currentVibration);
                    }

                    if (flag) {
                        user.onDataChanged();
                    }
                }
            }
        }

        private static void trySelectAndScheduleVibration(ServerLevel level, BitterVibrationSystem.Data data, BitterVibrationSystem.User user) {
            data.getSelectionStrategy()
                    .chosenCandidate(level.getGameTime())
                    .ifPresent(
                            p_282059_ -> {
                                data.setCurrentVibration(p_282059_);
                                data.setTravelTimeInTicks(user.calculateTravelTimeInTicks(p_282059_.distance()));
                                user.onDataChanged();
                                data.getSelectionStrategy().startOver();
                            }
                    );
        }

        private static boolean receiveVibration(ServerLevel level, BitterVibrationSystem.Data data, BitterVibrationSystem.User user, VibrationInfo vibrationInfo) {
            BlockPos blockpos = BlockPos.containing(vibrationInfo.pos());
            BlockPos blockpos1 = user.getPositionSource().getPosition(level).map(BlockPos::containing).orElse(blockpos);
            if (user.requiresAdjacentChunksToBeTicking() && !areAdjacentChunksTicking(level, blockpos1)) {
                return false;
            } else {
                user.onReceiveVibration(
                        level,
                        blockpos,
                        vibrationInfo.gameEvent(),
                        vibrationInfo.getEntity(level).orElse(null),
                        vibrationInfo.getProjectileOwner(level).orElse(null),
                        BitterVibrationSystem.Listener.distanceBetweenInBlocks(blockpos, blockpos1)
                );
                data.setCurrentVibration(null);
                return true;
            }
        }

        private static boolean areAdjacentChunksTicking(Level level, BlockPos pos) {
            ChunkPos chunkpos = new ChunkPos(pos);

            for (int i = chunkpos.x - 1; i <= chunkpos.x + 1; i++) {
                for (int j = chunkpos.z - 1; j <= chunkpos.z + 1; j++) {
                    if (!level.shouldTickBlocksAt(ChunkPos.asLong(i, j)) || level.getChunkSource().getChunkNow(i, j) == null) {
                        return false;
                    }
                }
            }

            return true;
        }
    }

    public interface User {
        int getListenerRadius();

        PositionSource getPositionSource();

        boolean canReceiveVibration(ServerLevel level, BlockPos pos, Holder<GameEvent> gameEvent, GameEvent.Context context);

        void onReceiveVibration(
                ServerLevel level, BlockPos pos, Holder<GameEvent> gameEvent, @Nullable Entity entity, @Nullable Entity playerEntity, float distance
        );

        default TagKey<GameEvent> getListenableEvents() {
            return GameEventTags.VIBRATIONS;
        }

        default boolean canTriggerAvoidVibration() {
            return false;
        }

        default boolean requiresAdjacentChunksToBeTicking() {
            return false;
        }

        default int calculateTravelTimeInTicks(float distance) {
            return Mth.floor(distance);
        }

        default boolean isValidVibration(Holder<GameEvent> gameEvent, GameEvent.Context context) {
            if (!gameEvent.is(this.getListenableEvents())) {
                return false;
            } else {
                Entity entity = context.sourceEntity();
                if (entity != null) {
                    if (entity.isSpectator()) {
                        return false;
                    }

                    if (entity.isSteppingCarefully() && gameEvent.is(GameEventTags.IGNORE_VIBRATIONS_SNEAKING)) {
                        if (this.canTriggerAvoidVibration() && entity instanceof ServerPlayer serverplayer) {
                            CriteriaTriggers.AVOID_VIBRATION.trigger(serverplayer);
                        }

                        return false;
                    }

                    if (entity.dampensVibrations()) {
                        return false;
                    }
                }

                return context.affectedState() != null ? !context.affectedState().is(BlockTags.DAMPENS_VIBRATIONS) : true;
            }
        }

        default void onDataChanged() {
        }
    }
}