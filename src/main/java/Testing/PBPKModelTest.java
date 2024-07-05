package Testing;
import net.smokeybbq.bittermelon.medical.simulation.IVAdministration;
import net.smokeybbq.bittermelon.medical.simulation.OralAdministration;
import net.smokeybbq.bittermelon.medical.simulation.PBPKModel;
import net.smokeybbq.bittermelon.medical.substance.Substance;
import net.smokeybbq.bittermelon.character.Character;
import net.smokeybbq.bittermelon.medical.substance.toxins.Bacteria;

public class PBPKModelTest {
    public static void main(String[] args) {

        //Initializes a default character
        Character testCharacter = CharacterTestFactory.createDummyCharacter();

        PBPKModel model = new IVAdministration(100, testCharacter, new Bacteria("Salmonella", 0.2F, 0.6F, 0.8F, 1));

        testCharacter.getMedicalStats().getSimulationHandler().addSimulation(model);

        testCharacter.getMedicalStats().getSimulationHandler().initialize();

        // Adjust the modifiers so that it takes different times to reach a total concentration of 1

        // Run the simulation 20 times a second (equal to minecraft ticks)
        int runsPerSecond = 100;
        long delay = 1000 / runsPerSecond; // Delay in milliseconds


        // use for (int i = 0; i < runsPerSecond; i++) for 20 runs
        // use while(model.getTotalConcentration() > 1) for same exit condition as actual model
        while(model.getTotalConcentration() > 1) {
            // Runs all simulations for the character
            testCharacter.update();
            System.out.println("Bacteria Concentration: " + model.getTotalConcentration());

            try {
                Thread.sleep(delay); // Introduce delay to achieve 20 runs per second
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}