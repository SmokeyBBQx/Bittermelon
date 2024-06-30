package net.smokeybbq.bittermelon.medical.symptoms;

import net.smokeybbq.bittermelon.character.Character;

public class Rash extends Symptom {
    String name = "Rash";
    public Rash(Character character, String affectedArea, float amplifier) {
        super(character, affectedArea, amplifier);
    }

    @Override
    public void update() {

    }

    @Override
    public void initializeDescriptions() {
        MILD_DESCRIPTION = "The skin appears slightly red, with small, slightly raised bumps. The affected area is small and seems contained, causing minimal discomfort.";
        MODERATE_DESCRIPTION = "The skin is moderately red with noticeable swelling. Numerous small bumps cover a medium-sized area, giving the skin a warm sensation to the touch.";
        SEVERE_DESCRIPTION = "The skin displays a deep red color, covered with large blisters and welts. Some blisters are leaking fluid, and the area feels hot and swollen, causing significant discomfort.";
        CRITICAL_DESCRIPTION = "The affected area has a deep, almost purple-red color. Large blisters and welts are visible, with some leaking fluid. The skin is highly inflamed and swollen, indicating severe irritation.";
        TERMINAL_DESCRIPTION = "The area shows extensive damage, with the skin appearing dark, almost black in some places. Blisters have turned into open, weeping sores. The swelling is severe and the skin is highly inflamed.";
    }

    @Override
    public void effects() {

    }
}
