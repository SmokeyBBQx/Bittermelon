package net.smokeybbq.bittermelon.chat;

import net.smokeybbq.bittermelon.character.Character;
import net.smokeybbq.bittermelon.character.CharacterManager;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class Channel {
    private final String name;
    private final int range;
    private final String chatColor;
    private final String channelNameColor;
    private final Set<UUID> members = new HashSet<>();


    public Channel (String name, int range, String chatColor, String channelNameColor) {
        this.name = name;
        this.range = range;
        this.chatColor = chatColor;
        this.channelNameColor = channelNameColor;
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

    public void addMember(Character character) {
        UUID characterUUID = character.getUUID();
        members.add(characterUUID);
        save();
    }

    public Set<Character> getMembers() {
        Set<Character> channelMembers = new HashSet<>();

        for (UUID memberUUID : members) {
            Character character = CharacterManager.getInstance().getData(memberUUID);
            channelMembers.add(character);
        }

        return channelMembers;
    }

    public void save() {
        ChannelManager.getInstance().updateData(this);
    }
}

