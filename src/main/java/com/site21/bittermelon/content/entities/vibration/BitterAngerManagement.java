package com.site21.bittermelon.content.entities.vibration;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.collect.Streams;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectIterator;
import it.unimi.dsi.fastutil.objects.Object2IntMap.Entry;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import javax.annotation.Nullable;
import net.minecraft.core.UUIDUtil;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.warden.AngerLevel;
import net.minecraft.world.entity.player.Player;

public class BitterAngerManagement {
    @VisibleForTesting
    protected static final int CONVERSION_DELAY = 2;
    @VisibleForTesting
    protected static final int MAX_ANGER = 100;
    private static final int DEFAULT_ANGER_DECREASE = 1;
    private int conversionDelay = Mth.randomBetweenInclusive(RandomSource.create(), 0, 2);
    int highestAnger;
    private static final Codec<Pair<UUID, Integer>> SUSPECT_ANGER_PAIR = RecordCodecBuilder.create(
            p_253580_ -> p_253580_.group(
                            UUIDUtil.CODEC.fieldOf("uuid").forGetter(Pair::getFirst), ExtraCodecs.NON_NEGATIVE_INT.fieldOf("anger").forGetter(Pair::getSecond)
                    )
                    .apply(p_253580_, Pair::of)
    );
    private final Predicate<Entity> filter;
    @VisibleForTesting
    protected final ArrayList<Entity> suspects;
    private final BitterAngerManagement.Sorter suspectSorter;
    @VisibleForTesting
    protected final Object2IntMap<Entity> angerBySuspect;
    @VisibleForTesting
    protected final Object2IntMap<UUID> angerByUuid;

    public static Codec<BitterAngerManagement> codec(Predicate<Entity> filter) {
        return RecordCodecBuilder.create(
                p_219281_ -> p_219281_.group(
                                SUSPECT_ANGER_PAIR.listOf().fieldOf("suspects").orElse(Collections.emptyList()).forGetter(BitterAngerManagement::createUuidAngerPairs)
                        )
                        .apply(p_219281_, p_219284_ -> new BitterAngerManagement(filter, p_219284_))
        );
    }

    public BitterAngerManagement(Predicate<Entity> filter, List<Pair<UUID, Integer>> angerByUuid) {
        this.filter = filter;
        this.suspects = new ArrayList<>();
        this.suspectSorter = new BitterAngerManagement.Sorter(this);
        this.angerBySuspect = new Object2IntOpenHashMap<>();
        this.angerByUuid = new Object2IntOpenHashMap<>(angerByUuid.size());
        angerByUuid.forEach(p_219272_ -> this.angerByUuid.put(p_219272_.getFirst(), p_219272_.getSecond()));
    }

    private List<Pair<UUID, Integer>> createUuidAngerPairs() {
        return Streams.<Pair<UUID, Integer>>concat(
                        this.suspects.stream().map(p_219295_ -> Pair.of(p_219295_.getUUID(), this.angerBySuspect.getInt(p_219295_))),
                        this.angerByUuid.object2IntEntrySet().stream().map(p_219276_ -> Pair.of(p_219276_.getKey(), p_219276_.getIntValue()))
                )
                .collect(Collectors.toList());
    }

    public void tick(ServerLevel level, Predicate<Entity> predicate) {
        this.conversionDelay--;
        if (this.conversionDelay <= 0) {
            this.convertFromUuids(level);
            this.conversionDelay = 2;
        }

        ObjectIterator<Entry<UUID>> objectiterator = this.angerByUuid.object2IntEntrySet().iterator();

        while (objectiterator.hasNext()) {
            Entry<UUID> entry = objectiterator.next();
            int i = entry.getIntValue();
            if (i <= 1) {
                objectiterator.remove();
            } else {
                entry.setValue(i - 1);
            }
        }

        ObjectIterator<Entry<Entity>> objectiterator1 = this.angerBySuspect.object2IntEntrySet().iterator();

        while (objectiterator1.hasNext()) {
            Entry<Entity> entry1 = objectiterator1.next();
            int j = entry1.getIntValue();
            Entity entity = entry1.getKey();
            Entity.RemovalReason entity$removalreason = entity.getRemovalReason();
            if (j > 1 && predicate.test(entity) && entity$removalreason == null) {
                entry1.setValue(j - 1);
            } else {
                this.suspects.remove(entity);
                objectiterator1.remove();
                if (j > 1 && entity$removalreason != null) {
                    switch (entity$removalreason) {
                        case CHANGED_DIMENSION:
                        case UNLOADED_TO_CHUNK:
                        case UNLOADED_WITH_PLAYER:
                            this.angerByUuid.put(entity.getUUID(), j - 1);
                    }
                }
            }
        }

        this.sortAndUpdateHighestAnger();
    }

    private void sortAndUpdateHighestAnger() {
        this.highestAnger = 0;
        this.suspects.sort(this.suspectSorter);
        if (this.suspects.size() == 1) {
            this.highestAnger = this.angerBySuspect.getInt(this.suspects.get(0));
        }
    }

    private void convertFromUuids(ServerLevel level) {
        ObjectIterator<Entry<UUID>> objectiterator = this.angerByUuid.object2IntEntrySet().iterator();

        while (objectiterator.hasNext()) {
            Entry<UUID> entry = objectiterator.next();
            int i = entry.getIntValue();
            Entity entity = level.getEntity(entry.getKey());
            if (entity != null) {
                this.angerBySuspect.put(entity, i);
                this.suspects.add(entity);
                objectiterator.remove();
            }
        }
    }

    public int increaseAnger(Entity entity, int offset) {
        boolean flag = !this.angerBySuspect.containsKey(entity);
        int i = this.angerBySuspect.computeInt(entity, (p_219259_, p_219260_) -> Math.min(MAX_ANGER, (p_219260_ == null ? 0 : p_219260_) + offset));
        if (flag) {
            int j = this.angerByUuid.removeInt(entity.getUUID());
            i += j;
            this.angerBySuspect.put(entity, i);
            this.suspects.add(entity);
        }

        this.sortAndUpdateHighestAnger();
        return i;
    }

    public void clearAnger(Entity entity) {
        this.angerBySuspect.removeInt(entity);
        this.suspects.remove(entity);
        this.sortAndUpdateHighestAnger();
    }

    @Nullable
    private Entity getTopSuspect() {
        return this.suspects.stream().filter(this.filter).findFirst().orElse(null);
    }

    public int getActiveAnger(@Nullable Entity entity) {
        return entity == null ? this.highestAnger : this.angerBySuspect.getInt(entity);
    }

    public Optional<LivingEntity> getActiveEntity() {
        return Optional.ofNullable(this.getTopSuspect()).filter(p_219293_ -> p_219293_ instanceof LivingEntity).map(p_219290_ -> (LivingEntity)p_219290_);
    }

    @VisibleForTesting
    protected static record Sorter(BitterAngerManagement angerManagement) implements Comparator<Entity> {
        public int compare(Entity first, Entity second) {
            if (first.equals(second)) {
                return 0;
            } else {
                int i = this.angerManagement.angerBySuspect.getOrDefault(first, 0);
                int j = this.angerManagement.angerBySuspect.getOrDefault(second, 0);
                this.angerManagement.highestAnger = Math.max(this.angerManagement.highestAnger, Math.max(i, j));
                boolean flag = AngerLevel.byAnger(i).isAngry();
                boolean flag1 = AngerLevel.byAnger(j).isAngry();
                if (flag != flag1) {
                    return flag ? -1 : 1;
                } else {
                    boolean flag2 = first instanceof Player;
                    boolean flag3 = second instanceof Player;
                    if (flag2 != flag3) {
                        return flag2 ? -1 : 1;
                    } else {
                        return Integer.compare(j, i);
                    }
                }
            }
        }
    }
}
