package net.smokeybbq.bittermelon.systems.telecommunications;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;

import java.util.List;

public class TelecommsManager {
    private static List<Receiver> receivers;

    private TelecommsManager() {

    }

    public static void addReceiver(BlockPos pos, float frequency) {
        receivers.add(new Receiver(pos, frequency));
    }

    public static void removeReceiver(BlockPos pos, float frequency) {
        receivers.removeIf(receiver -> receiver.pos == pos && receiver.frequency == frequency);
    }

    public static void broadcastReceivers(BlockPos pos, Level level, float frequency, int transmitRange, EncodedMessage message) {
        if (level == null) {
            return;
        }

        int transmitRangeSquared = transmitRange * transmitRange;

        receivers.stream()
                .filter(receiver -> receiver.frequency == frequency)
                .filter(receiver -> pos.distSqr(receiver.pos) < transmitRangeSquared)
                .forEach(receiver -> {
                    BlockEntity blockEntity = level.getBlockEntity(receiver.pos);
                    if (blockEntity instanceof TelecommsEntity telecomms) {
                        telecomms.processTransmission(frequency, message);
                    }
                });
    }

    public record Receiver(BlockPos pos, float frequency) {}

}
