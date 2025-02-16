package com.site21.bittermelon.content.medical.compartments;

public interface HasReminder {
    Severity getSeverity();
    String getMildReminder();
    String getModerateReminder();
    String getSevereReminder();
    String getCriticalReminder();
    String getTerminalReminder();

    default String getReminder() {
        switch(getSeverity()) {
            case MILD -> {
                return getMildReminder();
            }
            case MODERATE -> {
                return getModerateReminder();
            }
            case SEVERE -> {
                return getSevereReminder();
            }
            case CRITICAL -> {
                return getCriticalReminder();
            }
            case TERMINAL -> {
                return getTerminalReminder();
            }
            default -> {
                return null;
            }
        }
    }
}
