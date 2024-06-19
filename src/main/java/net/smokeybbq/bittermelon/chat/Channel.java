package net.smokeybbq.bittermelon.chat;

import net.smokeybbq.bittermelon.character.Character;
import net.smokeybbq.bittermelon.character.CharacterManager;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class Channel {
    private final String name;
    private final int range;
    private final String chatColor;
    private final String channelNameColor;
    private final Set<UUID> members = new HashSet<>();
    private final Set<UUID> whitelist = new HashSet<>();
    private final Set<String> layeredChannels = new HashSet<>();
    private final Map<String, Boolean> properties = new ConcurrentHashMap<>();


    public Channel (String name, int range, String chatColor, String channelNameColor) {
        this.name = name;
        this.range = range;
        this.chatColor = chatColor;
        this.channelNameColor = channelNameColor;
        this.properties.put("ignoreWhitelist", Boolean.FALSE);
        this.properties.put("ignoreDimensions", Boolean.FALSE);
        this.properties.put("ignoreRange", Boolean.FALSE);
        this.properties.put("canLeave", Boolean.TRUE);
        this.properties.put("isDefault", Boolean.FALSE);
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

    @Nullable
    public boolean getProperty(String name) {
        return properties.getOrDefault(name, null);
    }

    public List<Channel> getLayeredChannels() {
        List<Channel> result = new ArrayList<>();
        Channel layer;
        for (String s : layeredChannels) {
            layer = ChannelManager.getInstance().getChannel(s);
            if (layer != null) {
                result.add(layer);
            } else {
                layeredChannels.remove(s);
            }
        }
        return result;
    }

    public void addMember(Character character) {
        UUID characterUUID = character.getUUID();
        members.add(characterUUID);
        save();
    }

    public void removeMember(Character character) {
        UUID characterUUID = character.getUUID();
        members.remove(characterUUID);
        save();
    }

    /**
     * Removes any UUIDs from the set that do not return a character
     * @return Set of characters that are members of the channel
     */
    public Set<Character> getMembers() {
        Set<Character> channelMembers = new HashSet<>();
        for (UUID memberUUID : members) {
            Character character = CharacterManager.getInstance().getData(memberUUID);
            if (character != null) {
                channelMembers.add(character);
            } else {
                members.remove(memberUUID);
            }
        }
        save();
        return channelMembers;
    }

    public void addToWhitelist(Character character) {
        UUID characterUUID = character.getUUID();
        whitelist.add(characterUUID);
        save();
    }

    public void removeFromWhitelist(Character character) {
        UUID characterUUID = character.getUUID();
        whitelist.remove(characterUUID);
        members.remove(characterUUID);
        save();
    }

    public Set<Character> getWhitelist() {
        Set<Character> whitelistCharacters = new HashSet<>();
        for (UUID memberUUID : whitelist) {
            Character character = CharacterManager.getInstance().getData(memberUUID);
            if (character != null) {
                whitelistCharacters.add(character);
            } else {
                whitelist.remove(memberUUID);
            }
        }
        save();
        return whitelistCharacters;
    }

    public void save() {
        ChannelManager.getInstance().updateData(this);
    }
}

