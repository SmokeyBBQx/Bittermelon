package com.site21.bittermelon.common.events;

import com.jme3.bullet.PhysicsSpace;
import com.jme3.math.Vector3f;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;

import java.util.HashMap;
import java.util.Map;

public class PhysicsManager {
    private static final Map<ResourceKey<Level>, PhysicsSpace> physicsSpaces = new HashMap<>();

    public static PhysicsSpace getPhysicsSpace(ResourceKey<Level> level) {
        return physicsSpaces.get(level);
    }

    public static void updatePhysicsSpace(ResourceKey<Level> level) {
        PhysicsSpace space = physicsSpaces.computeIfAbsent(level, _ -> createSpace());
        space.update(0.05f);
    }

    private static PhysicsSpace createSpace() {
        PhysicsSpace space = new PhysicsSpace(PhysicsSpace.BroadphaseType.DBVT);
        space.setGravity(new Vector3f(0, -9.81f, 0));
        return space;
    }
}
