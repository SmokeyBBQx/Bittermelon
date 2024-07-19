package net.smokeybbq.bittermelon.character.medical;

import net.smokeybbq.bittermelon.medical.simulation.compartments.Compartment;

public class BodilyInteractions {

    public static void vomit(AnimalMedicalStats medicalStats) {
        Compartment stomach = medicalStats.getCompartments().get("abdomen").getCompartment("stomach");
        if (stomach != null) {
            stomach.getConcentrations().replaceAll((substance, concentration) -> concentration / 2);
        }
    }

    public static void sneeze(AnimalMedicalStats medicalStats) {
        Compartment nose = medicalStats.getCompartments().get("head").getCompartment("nose");
        if (nose != null) {
            nose.getConcentrations().replaceAll((substance, concentration) -> concentration - concentration / 4);
        }
    }

    public static void cough(AnimalMedicalStats medicalStats) {

    }

    public static void modifyInflammation() {

    }
}
