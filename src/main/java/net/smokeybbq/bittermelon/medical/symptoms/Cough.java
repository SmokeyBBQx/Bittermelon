package net.smokeybbq.bittermelon.medical.symptoms;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextColor;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.smokeybbq.bittermelon.character.Character;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

import static net.smokeybbq.bittermelon.util.LocalMessageHandler.sendLocalMessage;

@Mod.EventBusSubscriber(modid = "bittermelon")
public class Cough extends Symptom {
    private int ticksSinceLastReminder = 0;
    private int REMINDER_MINIMUM_TICKS = 400;
    private int REMINDER_MAXIMUM_TICKS = 600;

    private static final Random RANDOM = new Random();

    private static final String[] MILD_MESSAGES = {
            " coughs softly, a persistent dryness echoing in the sound.",
            " coughs lightly, each sound carrying a subtle, dry rasp.",
            " coughs gently, the sound marked by a consistent dryness.",
            " coughs intermittently, with a persistent dry tone.",
            " coughs quietly, each fit carrying a hint of dryness."
    };

    private static final String[] MODERATE_MESSAGES = {
            " coughs harshly, each fit sounding intense and strained.",
            " coughs vigorously, the effort audible in each harsh burst.",
            " coughs with force, each sound harsh and labored.",
            " coughs intensely, the harshness of each fit evident.",
            " coughs roughly, each bout sounding forceful and demanding effort."
    };

    private static final String[] SEVERE_MESSAGES = {
            " coughs violently, each fit requiring significant effort and causing breathlessness.",
            " coughs fiercely, each bout leaving moments of gasping for air.",
            " coughs intensely, every fit taking considerable effort and leaving breathless pauses.",
            " coughs forcefully, each fit demanding effort and causing moments of breathlessness.",
            " coughs with intensity, each bout of coughing leaving them struggling for breath."
    };

    private static final String[] CRITICAL_MESSAGES = {
            " coughs violently and persistently, each fit causing severe distress and making it hard to breathe.",
            " coughs continuously and with force, each bout causing visible distress and breathlessness.",
            " coughs relentlessly, each fit bringing severe distress and difficulty drawing breath.",
            " coughs with alarming intensity, each fit causing extreme distress and struggling to breathe.",
            " coughs incessantly, each violent fit causing severe difficulty in catching breath."
    };

    private static final String[] TERMINAL_MESSAGES = {
            " coughs desperately, producing blood-tinged sputum, each fit causing exhaustion and gasping for air.",
            " coughs weakly, each fit desperate and producing blood-streaked mucus, leaving them gasping.",
            " coughs with desperation, each fit bringing up blood-tinged sputum and leaving them exhausted.",
            " coughs in distress, producing blood-tinged mucus, each fit leaving them breathless and fatigued.",
            " coughs with extreme effort, blood-tinged sputum evident, each fit causing severe exhaustion and gasping for breath."
    };

    public Cough(Character character, String affectedArea, float amplifier) {
        super(character, affectedArea, amplifier);
        this.REMINDER_MINIMUM_TICKS -= (int) (amplifier * 5);
        this.REMINDER_MAXIMUM_TICKS -= (int) (amplifier * 10);
        MinecraftForge.EVENT_BUS.register(this);
    }

    @Override
    public void update() {

    }

    @Override
    public void initializeDescriptions() {

    }

    @Override
    public void effects() {
        ticksSinceLastReminder++;
        int TICKS_BETWEEN_REMINDERS = ThreadLocalRandom.current().nextInt(REMINDER_MINIMUM_TICKS, REMINDER_MAXIMUM_TICKS);

        if (ticksSinceLastReminder >= TICKS_BETWEEN_REMINDERS) {
            cough();
            ticksSinceLastReminder = 0;
        }

    }

    private void cough() {
        String message = "";

        switch (severity) {
            case MILD -> {
                message = MILD_MESSAGES[RANDOM.nextInt(MILD_MESSAGES.length)];
                break;
            }
            case MODERATE -> {
                message = MODERATE_MESSAGES[RANDOM.nextInt(MODERATE_MESSAGES.length)];
                break;
            }
            case SEVERE -> {
                message = SEVERE_MESSAGES[RANDOM.nextInt(SEVERE_MESSAGES.length)];
                break;
            }
            case CRITICAL -> {
                message = CRITICAL_MESSAGES[RANDOM.nextInt(CRITICAL_MESSAGES.length)];
                break;
            }
            case TERMINAL -> {
                message = TERMINAL_MESSAGES[RANDOM.nextInt(TERMINAL_MESSAGES.length)];
                break;
            }
            default -> {
                message = "";
                break;
            }
        }
        Component messageComponent = Component.literal(character.getName() + message).setStyle(Style.EMPTY.withColor(TextColor.parseColor(character.getEmoteColor())));
        sendLocalMessage(entity, 5 + (int) amplifier, messageComponent);
    }
}
