package net.smokeybbq.bittermelon.systems.telecommunications;

public interface TelecommsEntity {
    void processTransmission(float frequency, EncodedMessage message);
}
