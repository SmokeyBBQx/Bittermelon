package com.site21.bittermelon.content.chat;

import org.jetbrains.annotations.NotNull;

public class VerbSet {
    public final String normalVerb;
    public final String exclamationVerb;
    public final String shoutingVerb;
    public final String questionVerb;
    public final String whisperVerb;

    public VerbSet(String normalVerb, String exclamationVerb, String shoutingVerb, String questionVerb, String whisperVerb) {
        this.normalVerb = normalVerb;
        this.exclamationVerb = exclamationVerb;
        this.shoutingVerb = shoutingVerb;
        this.questionVerb = questionVerb;
        this.whisperVerb = whisperVerb;
    }

    public String getVerb(@NotNull String message) {
        if (message.contains("?") && message.contains("!")) {
            return exclamationVerb;
        }

        else if (message.contains("?")) {
            return questionVerb;
        }

        else if (message.equals(message.toUpperCase()) && message.contains("!")) {
            return shoutingVerb;
        }

        else if (message.contains("!!") || message.contains("!")) {
            return exclamationVerb;
        }

        return normalVerb;
    }
}
