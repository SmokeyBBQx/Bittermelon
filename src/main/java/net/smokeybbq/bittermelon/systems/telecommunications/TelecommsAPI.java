package net.smokeybbq.bittermelon.systems.telecommunications;


import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;

import java.util.Objects;

public class TelecommsAPI {
    EncodedMessage message;

    TelecommsAPI(EncodedMessage message) {
        this.message = message;
    }
    public void sendMessageToReceiver(TelecommsManager.Receiver receiver) {


    }

    public void broadcastReceivers(BlockPos pos, Level level, float frequency, int transmitRange, EncodedMessage message) {
      TelecommsManager.broadcastReceivers(pos, level, frequency, transmitRange, message);
    }

//    public String getKey() {
//        return message.key();
//    }

    public String getCode(String key) {
        if (Objects.equals(key, message.key())) {
            return message.code();
        }
        return "";
    }

    public String getMessage(String key) {
        if (Objects.equals(key, message.key())) {
            return message.message();
        }
        return "<BZZZTTT>";
    }
}
