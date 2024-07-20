package net.smokeybbq.bittermelon.character.medical;

import net.smokeybbq.bittermelon.character.medical.species.mammal.MammalMedicalStats;
import net.smokeybbq.bittermelon.medical.compartments.Compartment;

public class BodilyInteractions {

    public static void vomit(MammalMedicalStats medicalStats) {
        Compartment stomach = medicalStats.getCompartments().get("abdomen").getCompartment("stomach");
        if (stomach != null) {
            stomach.getConcentrations().replaceAll((substance, concentration) -> concentration / 2);
        }
    }

    public static void sneeze(MammalMedicalStats medicalStats) {
        Compartment nose = medicalStats.getCompartments().get("head").getCompartment("nose");
        if (nose != null) {
            nose.getConcentrations().replaceAll((substance, concentration) -> concentration - concentration / 4);
        }
    }

    public static void cough(MammalMedicalStats medicalStats) {

    }

    public static void modifyInflammation() {

    }
}
