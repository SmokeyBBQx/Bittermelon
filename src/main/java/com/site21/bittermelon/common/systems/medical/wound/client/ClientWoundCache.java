package com.site21.bittermelon.common.systems.medical.wound.client;

import com.site21.bittermelon.common.systems.medical.bodypart.BodyPart;
import com.site21.bittermelon.common.systems.medical.wound.Wound;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class ClientWoundCache {
    public static final ClientWoundCache INSTANCE = new ClientWoundCache();
    private final Map<UUID, ClientWound> cache = new ConcurrentHashMap<>();

    private ClientWoundCache() {}

    public ClientWound get(Wound wound, UUID ownerId, BodyPart part) {
        // TODO: Create path once in cached wound, then slowly reveal path on each tick
        return ClientWoundGenerator.test(wound, ownerId, part);
//        return cache.computeIfAbsent(wound.id(), _ -> ClientWoundGenerator.test(wound, ownerId, part));
    }

    public void remove(UUID woundId) {
        ClientWound removed = cache.remove(woundId);
        if (removed != null) {
            removed.texture().close();
        }
    }

    public void clear() {
        cache.values().forEach(cw -> cw.texture().close());
        cache.clear();
    }
}
