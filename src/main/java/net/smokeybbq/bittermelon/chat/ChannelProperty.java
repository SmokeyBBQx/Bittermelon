package net.smokeybbq.bittermelon.chat;

public enum ChannelProperty {
    IGNORE_DIMENSIONS(false),
    IGNORE_RANGE(false),
    CAN_LEAVE(false),
    IS_DEFAULT(false),
    DOUBLE_SPEAK(false);

    private final boolean defaultValue;

    ChannelProperty(boolean defaultValue) {
        this.defaultValue = defaultValue;
    }

    public boolean getDefaultValue() {
        return defaultValue;
    }
}
