package net.smokeybbq.bittermelon.items.radio;

import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.PacketDistributor;
import net.smokeybbq.bittermelon.character.Character;
import net.smokeybbq.bittermelon.character.CharacterManager;
import net.smokeybbq.bittermelon.chat.Channel;
import net.smokeybbq.bittermelon.chat.ChannelManager;
import net.smokeybbq.bittermelon.chat.RadioChannel;
import net.smokeybbq.bittermelon.items.base.BaseItem;
import net.smokeybbq.bittermelon.items.base.ItemSize;
import net.smokeybbq.bittermelon.items.base.ItemWeight;
import net.smokeybbq.bittermelon.networking.OpenChatS2CPacket;
import net.smokeybbq.bittermelon.networking.PacketHandler;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class RadioItem extends BaseItem {
    private static final String ACTIVE_FREQUENCY_KEY = "ActiveFrequency";
    private static final String PRESETS_KEY = "presets";
    private final float minFrequency;
    private final float maxFrequency;
    private final int transmitRange;
    private final boolean twoWay;

    // TODO: Digital radio boolean

    public RadioItem(Properties pProperties, ItemSize itemSize, ItemWeight itemWeight, float minFrequency, float maxFrequency, int transmitRange, boolean twoWay) {
        super(pProperties, itemSize, itemWeight);
        this.minFrequency = minFrequency;
        this.maxFrequency = maxFrequency;
        this.transmitRange = transmitRange;
        this.twoWay = twoWay;
    }

    /**
     * -- Radio Guide --
     * Radio channels are identified by frequencies (stored as floats, converted to strings when used)
     * Presets are predefined frequency settings accessible through the Radio GUI
     * Presets can be toggled on/off, allowing players to join/leave channels
     * Multiple presets can be active (for listening), but only one frequency is set for transmission
     * The active frequency is the channel the player speaks into when using the radio
     */

    public static boolean talkIntoRadio(ServerPlayer player) {
        Optional<ItemStack> radioStackOpt = findRadioStackInInventory(player);
        if (radioStackOpt.isPresent()) {
            ItemStack radioStack = radioStackOpt.get();
            RadioItem radioItem = (RadioItem) radioStack.getItem();

            if (!radioItem.canTransmit()) {
                player.sendSystemMessage(Component.literal("Radio can only receive!").withStyle(ChatFormatting.RED));
                return false;
            }

            Character activeCharacter = CharacterManager.getActiveCharacter(player.getUUID());
            String activeChannel = getActiveChannel(radioStack);
            ChannelManager.setCharacterActiveChannel(activeCharacter, activeChannel);

            PacketHandler.INSTANCE.send(PacketDistributor.PLAYER.with(() -> player), new OpenChatS2CPacket());
            return true;

            // TODO: Add check if player exits chat window without sending a message
        }
        return false;
    }

    // TODO: Eventually replace this with custom radio slot
    public static Optional<ItemStack> findRadioStackInInventory(ServerPlayer player) {
        for (int i = 0; i < player.getInventory().getContainerSize(); i++) {
            ItemStack itemStack = player.getInventory().getItem(i);
            if (itemStack.getItem() instanceof RadioItem) {
                return Optional.of(itemStack);
            }
        }
        return Optional.empty();
    }

    public static String getActiveChannel(ItemStack stack) {
        CompoundTag nbt = stack.getTag();
        return nbt != null ? nbt.getString(ACTIVE_FREQUENCY_KEY) : "";
    }

    public static void setActiveChannel(ItemStack stack, float frequency) {
        CompoundTag nbt = stack.getOrCreateTag();
        nbt.putString(ACTIVE_FREQUENCY_KEY, String.valueOf(frequency));
    }

    public static void togglePreset(ItemStack stack, float frequency, ServerPlayer player) {
        Preset preset = getPreset(stack, frequency);
        if (preset == null) {
            return;
        }
        Character character = CharacterManager.getActiveCharacter(player.getUUID());
        Channel presetChannel = ChannelManager.getInstance().getChannel(String.valueOf(frequency));

        if (character == null || presetChannel == null) {
            return;
        }

        preset.active = !preset.active;
        if (preset.active) {
            presetChannel.addMember(character);
        } else {
            presetChannel.removeMember(character);
        }
        savePreset(stack, preset);
    }

    public static void addPreset(ItemStack stack, String name, float frequency) {
        CompoundTag nbt = stack.getOrCreateTag();
        CompoundTag presetsTag = nbt.getCompound(PRESETS_KEY);
        Preset preset = new Preset(name, frequency, false);
        presetsTag.put(String.valueOf(frequency), preset.toNBT());
        nbt.put(PRESETS_KEY, presetsTag);

        if (ChannelManager.getInstance().getChannel(String.valueOf(frequency)) == null) {
            ChannelManager.getInstance().addChannel(String.valueOf(frequency), new RadioChannel(String.valueOf(frequency), "00FF00", "00FF00"));
        }
    }

    public static void removePreset(ItemStack stack, float frequency) {
        CompoundTag nbt = stack.getOrCreateTag();
        CompoundTag presetsTag = nbt.getCompound(PRESETS_KEY);
        presetsTag.remove(String.valueOf(frequency));
        nbt.put(PRESETS_KEY, presetsTag);
    }

    public static List<Preset> getAllPresets(ItemStack stack) {
        CompoundTag nbt = stack.getOrCreateTag();
        CompoundTag presetsTag = nbt.getCompound(PRESETS_KEY);
        List<Preset> presets = new ArrayList<>();

        for (String key : presetsTag.getAllKeys()) {
            CompoundTag presetTag = presetsTag.getCompound(key);
            presets.add(Preset.fromNBT(presetTag));
        }

        return presets;
    }

    public static Preset getPreset(ItemStack stack, float frequency) {
        CompoundTag nbt = stack.getOrCreateTag();
        CompoundTag presetsTag = nbt.getCompound(PRESETS_KEY);
        CompoundTag presetTag = presetsTag.getCompound(String.valueOf(frequency));

        return presetTag.isEmpty() ? null : Preset.fromNBT(presetTag);
    }

    private static void savePreset(ItemStack stack, Preset preset) {
        CompoundTag nbt = stack.getOrCreateTag();
        CompoundTag presetsTag = nbt.getCompound(PRESETS_KEY);
        presetsTag.put(String.valueOf(preset.frequency), preset.toNBT());
        nbt.put(PRESETS_KEY, presetsTag);
    }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(Level level, @NotNull Player player, @NotNull InteractionHand hand) {
        if (level.isClientSide()) {
            Minecraft.getInstance().setScreen(new RadioScreen(player.getItemInHand(hand)));
        }
        return InteractionResultHolder.success(player.getItemInHand(hand));
    }

    public static void editPreset(ItemStack radioItem, float oldFrequency, String presetName, float frequency) {
        removePreset(radioItem, oldFrequency);
        Preset preset = new Preset(presetName, frequency, false);

        preset.name = presetName;
        preset.frequency = frequency;
        savePreset(radioItem, preset);
    }

    public boolean canTransmit() {
        return twoWay;
    }

    public float getMinFrequency() {
        return minFrequency;
    }

    public float getMaxFrequency() {
        return maxFrequency;
    }

    public int getTransmitRange() {
        return transmitRange;
    }

    public static class Preset {
        public String name;
        public float frequency;
        public boolean active;

        public Preset(String name, float frequency, boolean active) {
            this.name = name;
            this.frequency = frequency;
            this.active = active;
        }

        public CompoundTag toNBT() {
            CompoundTag tag = new CompoundTag();
            tag.putString("name", name);
            tag.putFloat("frequency", frequency);
            tag.putBoolean("active", active);
            return tag;
        }

        public static Preset fromNBT(CompoundTag tag) {
            return new Preset(tag.getString("name"), tag.getFloat("frequency"), tag.getBoolean("active"));
        }
    }

}
