package net.smokeybbq.bittermelon.chat;

import net.smokeybbq.bittermelon.character.Character;
import net.smokeybbq.bittermelon.character.CharacterManager;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class Channel {
    protected final String name;
    protected final int range;
    protected final String chatColor;
    protected final String channelNameColor;
    protected final Set<UUID> members = new HashSet<>();
    protected final Set<String> permissions = new HashSet<>();

    protected final EnumMap<ChannelProperty, Boolean> properties = new EnumMap<>(ChannelProperty.class);


    public Channel (String name, int range, String chatColor, String channelNameColor) {
        this.name = name;
        this.range = range;
        this.chatColor = chatColor;
        this.channelNameColor = channelNameColor;
        initializeProperties();
    }

    private void initializeProperties() {
        for (ChannelProperty property : ChannelProperty.values()) {
            properties.put(property, property.getDefaultValue());
        }
    }

    public String getName() {return name;}
    public String getChatColor() {
        return chatColor;
    }
    public String getChannelNameColor() {
        return channelNameColor;
    }
    public int getRange() {
        return range;
    }

    public boolean getProperty(ChannelProperty property) {
        return properties.get(property);
    }

    public void setProperty(ChannelProperty property, boolean value) {
        properties.put(property, value);
        save();
    }

    public void addMember(Character character) {
        members.add(character.getUUID());
        save();
    }

    public void removeMember(Character character) {
        members.remove(character.getUUID());
        save();
    }

    /**
     * Removes any UUIDs from the set that do not return a character
     * @return Set of characters that are members of the channel
     */
    public Set<Character> getMembers() {
        Set<Character> channelMembers = new HashSet<>();
        members.removeIf(uuid -> {
            Character character = CharacterManager.getInstance().getData(uuid);
            if (character != null) {
                channelMembers.add(character);
                return false;
            }
            return true;
        });
        save();
        return channelMembers;
    }

    public void save() {
        ChannelManager.getInstance().updateData(this);
    }
}

