package Testing;
import net.smokeybbq.bittermelon.medical.simulation.PBPKModel;
import net.smokeybbq.bittermelon.medical.simulation.OralAdministration;
import net.smokeybbq.bittermelon.medical.substance.Substance;
import net.smokeybbq.bittermelon.medical.substance.medicine.Penicillin;
import net.smokeybbq.bittermelon.character.Character;

public class PBPKModelTest {
    public static void main(String[] args) {

        //Initializes a default character
        Character testCharacter = CharacterTestFactory.createDummyCharacter();

        Substance drug = new Penicillin(1,1,1);

        PBPKModel model = new OralAdministration(10, testCharacter, drug);

        // Run the simulation 20 times a second (equal to minecraft ticks)
        int runsPerSecond = 20;
        long delay = 1000 / runsPerSecond; // Delay in milliseconds


        // use for (int i = 0; i < runsPerSecond; i++) for 20 runs
        // use while(model.getTotalConcentration() <= 1) for same exit condition as actual model
        for (int i = 0; i < runsPerSecond; i++) {
            model.runSimulation();
            try {
                Thread.sleep(delay); // Introduce delay to achieve 20 runs per second
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}