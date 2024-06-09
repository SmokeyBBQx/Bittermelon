package Testing;
import net.smokeybbq.bittermelon.medical.conditions.Influenza;
import net.smokeybbq.bittermelon.medical.simulation.PBPKModel;
import net.smokeybbq.bittermelon.medical.simulation.OralAdministration;
import net.smokeybbq.bittermelon.medical.substance.Substance;
import net.smokeybbq.bittermelon.medical.substance.medicine.Acetaminophen;
import net.smokeybbq.bittermelon.medical.substance.medicine.Penicillin;
import net.smokeybbq.bittermelon.character.Character;

public class PBPKModelTest {
    public static void main(String[] args) {

        //Initializes a default character
        Character testCharacter = CharacterTestFactory.createDummyCharacter();

        Influenza influenza = new Influenza(100, false, 1, "Lungs", testCharacter);

        testCharacter.getMedicalStats().addCondition(influenza);

        // Adjust the modifiers so that it takes different times to reach a total concentration of 1
        Substance drug = new Acetaminophen(1,2,2);

        PBPKModel model = new OralAdministration(100, testCharacter, drug);

        testCharacter.getMedicalStats().simulationHandler.addSimulation(model);

        // Run the simulation 20 times a second (equal to minecraft ticks)
        int runsPerSecond = 10000;
        long delay = 1000 / runsPerSecond; // Delay in milliseconds


        // use for (int i = 0; i < runsPerSecond; i++) for 20 runs
        // use while(model.getTotalConcentration() <= 1) for same exit condition as actual model
        for (int i = 0; i < runsPerSecond; i++) {
            // Runs all simulations for the character
            testCharacter.update();

            // Gets the fever symptom
            System.out.println(influenza.getSymptoms().get(0).getAmplifier());
            try {
                Thread.sleep(delay); // Introduce delay to achieve 20 runs per second
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}