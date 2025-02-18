package com.site21.bittermelon.content.chat;

import org.jetbrains.annotations.NotNull;

public record VerbSet(String normalVerb, String exclamationVerb, String shoutingVerb, String questionVerb,
                      String whisperVerb) {

    public String getVerb(@NotNull String message) {
        if (message.contains("?") && message.contains("!")) {
            return exclamationVerb;
        } else if (message.contains("?")) {
            return questionVerb;
        }

        if (message.contains("!!")) {
            return shoutingVerb;
        } else if (message.contains("!")) {
            return exclamationVerb;
        }

        return normalVerb;
    }
}
