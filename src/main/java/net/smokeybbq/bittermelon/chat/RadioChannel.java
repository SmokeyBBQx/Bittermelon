package net.smokeybbq.bittermelon.chat;

public class RadioChannel extends Channel {

    public RadioChannel(String name, String chatColor, String channelNameColor) {
        super(name, 0, chatColor, channelNameColor);
        setProperty(ChannelProperty.DOUBLE_SPEAK, true);
    }

}
