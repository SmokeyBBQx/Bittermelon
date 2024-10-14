package com.site21.bittermelon.entities.scps.SCP939;

import net.minecraft.world.entity.Entity;

import java.util.*;
import java.util.function.Function;

public class SCP939Pseudo {
    Map<UUID, Float> relationships = new HashMap<>();
    int bloodlust = 100;
    int procreation = 100;
    int amnestics = 100;

    Map<Integer, Function<SCP939Pseudo, Boolean>> needs = Map.of(
            bloodlust, SCP939Pseudo::handleBloodlustReplenishment,
            procreation, SCP939Pseudo::handleProcreationReplenishment,
            amnestics, SCP939Pseudo::handleAmnesticsReplenishment
    );

    public void updateNeeds() {
        bloodlust -= 4;
        procreation -= 1;

        // TODO: Implement personality trait amplifiers
    }

    public void queueNeeds() {
        List<Integer> needsSorted = new ArrayList<>(needs.keySet());
        Collections.sort(needsSorted);

        for (int i = 0; i < needsSorted.size(); i++) {
            if (needs.get(i).apply(this)) {
                break;
            }
        }
    }

    public boolean getActiveNeed() {
        List<Integer> needsSorted = new ArrayList<>(needs.keySet());
        Collections.sort(needsSorted);

        for (int i = 0; i < needsSorted.size(); i++) {
            if (needs.get(i).apply(this)) {
                break;
            }
        }
        return false;
    }

    public boolean handleBloodlustReplenishment() {
        return true;

        // Just does luring and walking further, but attacking nearby entities and investigating is a core behavior
    }

    public boolean handleProcreationReplenishment() {
        return true;
    }

    public boolean handleAmnesticsReplenishment() {
        return true;
    }

    public void affectRelationship(Entity entity, float amount) {

    }
}
