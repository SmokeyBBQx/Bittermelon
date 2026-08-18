package com.site21.bittermelon.common.events;

import com.github.stephengold.joltjni.*;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;

import java.util.HashMap;
import java.util.Map;

public class PhysicsManager {
    private static final Map<ResourceKey<Level>, PhysicsSystem> systems = new HashMap<>();
    private static final TempAllocator tempAllocator = new TempAllocatorMalloc();
    private static final JobSystem jobSystem = new JobSystemThreadPool(
            Jolt.cMaxPhysicsJobs, Jolt.cMaxPhysicsBarriers, Runtime.getRuntime().availableProcessors());
    public static final int MOVING_LAYER = 0;
    public static final int NON_MOVING_LAYER = 1;

    public static PhysicsSystem getPhysicsSystem(ResourceKey<Level> level) {
        return systems.get(level);
    }

    public static void updatePhysicsSystem(ResourceKey<Level> level) {
        PhysicsSystem system = systems.computeIfAbsent(level, _ -> createSystem());
        system.update(0.05f, 1, tempAllocator, jobSystem);
    }

    private static PhysicsSystem createSystem() {
        final int numBpLayers = 1;
        final int numObjLayers = 2;

        ObjectLayerPairFilterTable ovoFilter = new ObjectLayerPairFilterTable(numObjLayers);
        // Enable collisions between 2 moving bodies:
        ovoFilter.enableCollision(MOVING_LAYER, MOVING_LAYER);
        // Enable collisions between a moving body and a non-moving one:
        ovoFilter.enableCollision(MOVING_LAYER, NON_MOVING_LAYER);
        // Disable collisions between 2 non-moving bodies:
        ovoFilter.disableCollision(NON_MOVING_LAYER, NON_MOVING_LAYER);

        // Map both object layers to broadphase layer 0:
        BroadPhaseLayerInterfaceTable layerMap = new BroadPhaseLayerInterfaceTable(numObjLayers, numBpLayers);
        layerMap.mapObjectToBroadPhaseLayer(MOVING_LAYER, 0);
        layerMap.mapObjectToBroadPhaseLayer(NON_MOVING_LAYER, 0);
        /*
         * Pre-compute the rules for colliding object layers
         * with broadphase layers:
         */
        ObjectVsBroadPhaseLayerFilterTable ovbFilter = new ObjectVsBroadPhaseLayerFilterTable(layerMap, numBpLayers, ovoFilter, numObjLayers);

        PhysicsSystem system = new PhysicsSystem();
        int maxBodies = 500;
        int numBodyMutexes = 0;
        int maxBodyPairs = 65536;
        int maxContacts = 20480;
        system.setGravity(0, -9.81f, 0);
        system.init(maxBodies, numBodyMutexes, maxBodyPairs, maxContacts, layerMap, ovbFilter, ovoFilter);
        return system;
    }
}
