package net.smokeybbq.bittermelon.character.medical.species;

import net.smokeybbq.bittermelon.character.medical.species.animal.gastropod.GastropodMedicalStats;
import net.smokeybbq.bittermelon.character.medical.species.animal.mammal.MammalMedicalStats;

public enum Species {
    MAMMAL(MammalMedicalStats.class),
    GASTROPOD(GastropodMedicalStats.class);

    private final Class<? extends MedicalStats> medicalStats;

    <T extends MedicalStats> Species(Class<T> medicalStats) {
        this.medicalStats = medicalStats;
    }

    public MedicalStats createMedicalStats() {
        try {
            return medicalStats.getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            throw new RuntimeException("Failed to create medical stats", e);
        }
    }
}
