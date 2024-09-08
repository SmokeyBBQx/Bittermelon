package net.smokeybbq.bittermelon.character.medical.species;

import net.smokeybbq.bittermelon.character.Character;
import net.smokeybbq.bittermelon.character.medical.species.animal.gastropod.GastropodMedicalStats;
import net.smokeybbq.bittermelon.character.medical.species.animal.mammal.MammalMedicalStats;
import net.smokeybbq.bittermelon.systems.medical.compartments.Compartment;

import java.util.Map;

public enum Species {
    MAMMAL(MammalMedicalStats.class),
    GASTROPOD(GastropodMedicalStats.class);

    private final Class<? extends MedicalStats> medicalStats;

    <T extends MedicalStats> Species(Class<T> medicalStats) {
        this.medicalStats = medicalStats;
    }

    public MedicalStats createMedicalStats(Character character, Map<String, Compartment> compartments) {
        try {
            return medicalStats.getDeclaredConstructor(Character.class, Map.class).newInstance(character, compartments);
        } catch (Exception e) {
            throw new RuntimeException("Failed to create medical stats", e);
        }
    }
}
