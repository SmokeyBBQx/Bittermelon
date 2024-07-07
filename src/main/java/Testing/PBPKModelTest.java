package Testing;

import net.smokeybbq.bittermelon.medical.simulation.IVAdministration;
import net.smokeybbq.bittermelon.medical.simulation.OralAdministration;
import net.smokeybbq.bittermelon.medical.simulation.PBPKModel;
import net.smokeybbq.bittermelon.medical.simulation.compartments.Compartment;
import net.smokeybbq.bittermelon.medical.substance.Substance;
import net.smokeybbq.bittermelon.character.Character;
import net.smokeybbq.bittermelon.medical.substance.toxins.Bacteria;
import net.smokeybbq.bittermelon.medical.substance.toxins.Toxin;

public class PBPKModelTest {
    public static void main(String[] args) {

        //Initializes a default character
        Character testCharacter = CharacterTestFactory.createDummyCharacter();

        Substance substance = new Bacteria("Salmonella", 0.9F, 0F, 0F, 0.01F);
        Compartment compartment = new Compartment("Tumor", 0.9F);
        compartment.modifyImmunePrivilege(-99);
        testCharacter.getMedicalStats().addCompartment(compartment);

        PBPKModel model = new IVAdministration(100, testCharacter, substance);

        testCharacter.getMedicalStats().getSimulationHandler().addSimulation(model);

//        testCharacter.getMedicalStats().getSimulationHandler().initialize();

        // Adjust the modifiers so that it takes different times to reach a total concentration of 1

        // Run the simulation 20 times a second (equal to minecraft ticks)
        int runsPerSecond = 20;
        long delay = 1000 / runsPerSecond; // Delay in milliseconds


        // use for (int i = 0; i < runsPerSecond; i++) for 20 runs
        // use while(model.getTotalConcentration() > 1) for same exit condition as actual model

        for (int i = 0; i < 400; i++) {
            // Runs all simulations for the character
            testCharacter.update();
            System.out.println("Tumor Health " + compartment.getHealth());

            try {
                Thread.sleep(delay); // Introduce delay to achieve 20 runs per second
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}