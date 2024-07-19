package net.smokeybbq.bittermelon.medical.symptoms;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextColor;
import net.minecraft.server.level.ServerPlayer;
import net.smokeybbq.bittermelon.character.Character;

import java.util.concurrent.ThreadLocalRandom;

public class Ache extends Symptom {
    private int ticksSinceLastReminder = 0;
    private int REMINDER_MINIMUM_TICKS = 400;
    private int REMINDER_MAXIMUM_TICKS = 600;

    public Ache(Character character, String affectedArea, float amplifier) {
        super(character, affectedArea, amplifier);
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
            ache();
            ticksSinceLastReminder = 0;
        }
    }

    private void ache() {
        if (entity instanceof ServerPlayer player) {
            String message = "";

            switch (severity) {
                case MILD -> {
                    message = "You feel a slight, nagging ache in your " + affectedArea + ", like a persistent dull throb.";
                    break;
                }
                case MODERATE -> {
                    message = "You feel a deep, aching pain in your " + affectedArea + ", making it uncomfortable to move.";
                    break;
                }
                case SEVERE -> {
                    message = "You feel an intense, sharp pain in your " + affectedArea + ", making every movement a struggle.";
                    break;
                }
                case CRITICAL -> {
                    message = "You feel a relentless, excruciating pain in your " + affectedArea + ", making it hard to focus on anything else.";
                    break;
                }
                case TERMINAL -> {
                    message = "You feel an unbearable, all-consuming pain throughout your " + affectedArea + ", leaving you incapacitated and in agony.";
                    break;
                }
                default -> {
                    message = "";
                    break;
                }
            }
            player.sendSystemMessage(Component.literal(message).setStyle(Style.EMPTY.withColor(TextColor.parseColor(character.getEmoteColor()))));
        }
    }
}
