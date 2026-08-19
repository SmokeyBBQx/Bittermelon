package com.site21.bittermelon.common.physics;

import com.github.stephengold.joltjni.*;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;

import java.util.HashMap;
import java.util.Map;

public class PhysicsManager {
    private static final Map<ResourceKey<Level>, PhysicsLevel> systems = new HashMap<>();
    public static final TempAllocator TEMP_ALLOCATOR = new TempAllocatorMalloc();
    public static final JobSystem JOB_SYSTEM = new JobSystemThreadPool(
            Jolt.cMaxPhysicsJobs, Jolt.cMaxPhysicsBarriers, Runtime.getRuntime().availableProcessors());
    public static final short NON_MOVING = 1;
    public static final short MOVING = 0;

    public static PhysicsLevel getPhysicsLevel(ResourceKey<Level> level) {
        return systems.get(level);
    }

    public static PhysicsLevel getPhysicsLevel(Level level) {
        return getPhysicsLevel(level.dimension());
    }

    public static void updatePhysicsLevel(Level level) {
        PhysicsLevel physicsLevel = systems.computeIfAbsent(level.dimension(), _ -> new PhysicsLevel(createSystem()));
        physicsLevel.update(level);
    }

    private static PhysicsSystem createSystem() {
        final int numBpLayers = 1;
        final int numObjLayers = 2;

        ObjectLayerPairFilterTable ovoFilter = new ObjectLayerPairFilterTable(numObjLayers);
        ovoFilter.enableCollision(MOVING, MOVING);
        ovoFilter.enableCollision(MOVING, NON_MOVING);
        ovoFilter.disableCollision(NON_MOVING, NON_MOVING);

        BroadPhaseLayerInterfaceTable layerMap = new BroadPhaseLayerInterfaceTable(numObjLayers, numBpLayers);
        layerMap.mapObjectToBroadPhaseLayer(MOVING, 0);
        layerMap.mapObjectToBroadPhaseLayer(NON_MOVING, 0);

        ObjectVsBroadPhaseLayerFilterTable ovbFilter = new ObjectVsBroadPhaseLayerFilterTable(layerMap, numBpLayers, ovoFilter, numObjLayers);

        PhysicsSystem system = new PhysicsSystem();
        int maxBodies = 2000;
        int numBodyMutexes = 0;
        int maxBodyPairs = 65536;
        int maxContacts = 20480;
        system.init(maxBodies, numBodyMutexes, maxBodyPairs, maxContacts, layerMap, ovbFilter, ovoFilter);
        return system;
    }
}
