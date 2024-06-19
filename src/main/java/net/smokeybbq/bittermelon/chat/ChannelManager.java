package net.smokeybbq.bittermelon.chat;

import com.google.gson.Gson;
import net.minecraftforge.fml.loading.FMLPaths;
import net.smokeybbq.bittermelon.character.Character;
import net.smokeybbq.bittermelon.util.DataManager;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;


public class ChannelManager extends DataManager<String, Channel> {
    private static ChannelManager instance = null;
    private static final Map<String, Channel> channels = new ConcurrentHashMap<>();
    private static final String CHANNEL_FOLDER = FMLPaths.GAMEDIR.get().resolve("channels/").toString();
    private static final Gson gson = new Gson();
    private static final HashMap<Character, Channel> playerUUIDToChannel = new HashMap<>();

    private ChannelManager() {
        super(CHANNEL_FOLDER, Channel.class);
    }

    public static synchronized ChannelManager getInstance() {
        if (instance == null) {
            instance = new ChannelManager();
        }
        return instance;
    }

    public static void setCharacterActiveChannel(Character character, Channel channel) {
        playerUUIDToChannel.put(character, channel);
    }
    public static Channel getCharacterActiveChannel(Character character) {
        return playerUUIDToChannel.get(character);
    }

    public Map<String, Channel> getChannels() {
        return getDataMap();
    }

    public Channel getChannel(String channelName) {
        return getData(channelName);
    }

    public void addChannel(String channelName, Channel channel) {
        saveData(channel);
        addData(channelName, channel);
    }

    public void removeChannel(@NotNull Channel channel) {
        deleteData(channel);
        // while loop used to remove all characters tied to the channel
        while (playerUUIDToChannel.containsValue(channel)) {
            playerUUIDToChannel.values().remove(channel);
        }
    }

    private List<Channel> getDefaultChannels() {
        List<Channel> result = new ArrayList<>();
        for (Channel c : getDataMap().values()) {
            if (c.getProperty("isDefault")) {
                result.add(c);
            }
        }
        return result;
    }

    public void setDefaultChannels(Character character) {
        for (Channel c : getDefaultChannels()) {
            c.addMember(character);
        }
    }

    // TODO: add boolean to channels that disables whitelist
    public List<Channel> getPermittedChannels(Character character) {
        List<Channel> list = new ArrayList<>();
        for (Channel channel : getDataMap().values()) {
            if (channel.getWhitelist().contains(character) || channel.getProperty("ignoreWhitelist")) {
                list.add(channel);
            }
        }
        return list;
    }

    public boolean isPermittedChannel(Character character, Channel channel) {
        return getPermittedChannels(character).contains(channel);
    }

    public void updateData(Channel channel) {
        saveData(channel);
    }

    @Override
    protected String getFileName(Channel data) {
        return data.getName();
    }

    @Override
    protected String getKey(Channel data) {
        return getFileName(data);
    }
}
