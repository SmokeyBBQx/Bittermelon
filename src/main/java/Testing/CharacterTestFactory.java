package Testing;
import java.util.UUID;
import net.smokeybbq.bittermelon.character.Character;

public class CharacterTestFactory {

    //This class will generate a dummy character to be used for testing
    public static Character createDummyCharacter() {
        UUID playerUuid = UUID.randomUUID();
        String name = "TestCharacter";
        String gender = "Male";
        String description = "This is a test character.";
        String skinUrl = "http://wow.com/skin.png";
        int age = 25;
        float height = 1.75F;
        float weight = 70.0F;
        String emoteColor = "#FFFFFF";

        return new Character(playerUuid, name, gender, description, skinUrl, age, height, weight, emoteColor);
    }
}