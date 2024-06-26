package net.smokeybbq.bittermelon.medical.infections;

import net.smokeybbq.bittermelon.medical.symptoms.Symptom;

import java.util.List;

public interface InfectionStage {

    //nextStage advances the current stage of the infection
    void nextStage(Infection infection);

    //Each stage of the infection could have its own symptoms
    List<Symptom> getSymptoms();

    String getStageName();
}




