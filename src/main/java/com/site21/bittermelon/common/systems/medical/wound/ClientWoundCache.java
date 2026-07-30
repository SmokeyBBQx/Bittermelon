package com.site21.bittermelon.common.systems.medical.wound;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class ClientWoundCache {
    public static final ClientWoundCache INSTANCE = new ClientWoundCache();
    private final Map<UUID, ClientWound> cache = new ConcurrentHashMap<>();

    private ClientWoundCache() {}

    public ClientWound get(Wound wound, UUID ownerId, BodyPart part) {
        return cache.computeIfAbsent(wound.id(), _ -> ClientWoundFactory.test(wound, ownerId, part));
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
