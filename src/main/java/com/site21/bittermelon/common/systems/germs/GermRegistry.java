package com.site21.bittermelon.common.systems.germs;

import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.site21.bittermelon.Bittermelon;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraft.world.level.saveddata.SavedDataType;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class GermRegistry extends SavedData {
    public static final SavedDataType<GermRegistry> TYPE;
    private final Map<UUID, Germ> germs = new HashMap<>();

    public GermRegistry() {}

    public static @NotNull GermRegistry get(@NotNull ServerLevel level) {
        return level.getDataStorage().computeIfAbsent(TYPE);
    }

    public UUID registerGerm(Germ germ) {
        UUID id = UUID.randomUUID();
        germs.put(id, germ);
        setDirty();
        return id;
    }

    public Germ getGerm(UUID id) {
        return germs.get(id);
    }

    public void removeGerm(UUID id) {
        germs.remove(id);
        setDirty();
    }

    static {
        TYPE = new SavedDataType<>(
                Bittermelon.identifier("germs"),
                GermRegistry::new,
                RecordCodecBuilder.create(instance -> instance.group(
                        Germ.CODEC.listOf().fieldOf("germs")
                                .forGetter(gr -> new ArrayList<>(gr.germs.values()))
                ).apply(instance, (java.util.List<Germ> germList) -> {
                    GermRegistry gr = new GermRegistry();
                    for (Germ germ : germList) {
                        gr.germs.put(UUID.randomUUID(), germ); // TODO: Update to store UUIDs properly
                    }
                    return gr;
                }))
        );
    }
}
