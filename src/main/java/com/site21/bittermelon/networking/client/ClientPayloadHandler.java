package com.site21.bittermelon.networking.client;

import com.site21.bittermelon.networking.server.SetLastTypingTime;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.jetbrains.annotations.NotNull;

import static com.site21.bittermelon.init.neoforge.BitterAttachmentTypes.LAST_TYPING_TIME;

public class ClientPayloadHandler {
    public static void setLastTypingTime(@NotNull SetLastTypingTime data, @NotNull IPayloadContext ctx) {
        Player player = ctx.player().level().getPlayerByUUID(data.playerUUID());
        if (player == null) return;

        if (data.lastTypingTime() == -1) {
            player.removeData(LAST_TYPING_TIME);
            return;
        }

        player.setData(LAST_TYPING_TIME, data.lastTypingTime());
    }
}
