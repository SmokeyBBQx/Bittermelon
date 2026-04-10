package com.site21.bittermelon.common.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.site21.bittermelon.common.content.entities.mimicplayer.Mimic;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import org.jetbrains.annotations.NotNull;

import static com.site21.bittermelon.init.neoforge.BitterAttachmentTypes.ENRAGED;

public class MimicPlayerTestCommand {
//    public static void register(@NotNull CommandDispatcher<CommandSourceStack> dispatcher) {
//        dispatcher.register(Commands.literal("mimicplayer")
//                .requires(source -> source.hasPermission(2))
//                .executes(context -> {
//                    ServerLevel level = context.getSource().getLevel();
//                    MinecraftServer server = context.getSource().getServer();
//                    ServerPlayer player = context.getSource().getPlayer();
//                    GameProfile gameProfile = new GameProfile(UUID.randomUUID(), "MimicPlayer");
//                    MimicPlayer mimic = new MimicPlayer(server, level, gameProfile, ClientInformation.createDefault());
//                    mimic.setPos(player.getX(), player.getY(), player.getZ());
//                    level.addFreshEntity(mimic);
//                    server.getConnection().getConnections().add(mimic.connection.getConnection());
//                    player.connection.send(new ClientboundPlayerInfoUpdatePacket(ClientboundPlayerInfoUpdatePacket.Action.ADD_PLAYER, mimic));
//                    player.connection.send(new ClientboundAddEntityPacket(
//                            mimic,
//                            0,
//                            mimic.blockPosition()
//                    ));
//                    context.getSource().sendSuccess(() -> Component.nullToEmpty("MimicPlayer test command executed"), false);
//                    return 1;
//                })
//        );
//    }

    public static void register(@NotNull CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("mimicplayer")
                .requires(source -> source.hasPermission(2))
                .executes(context -> {
                    ServerLevel level = context.getSource().getLevel();
                    ServerPlayer player = context.getSource().getPlayer();
                    Mimic mimic = new Mimic(level, player);
                    mimic.setPos(player.getX(), player.getY(), player.getZ());
                    level.addFreshEntity(mimic);
                    context.getSource().sendSuccess(() -> Component.nullToEmpty("MimicPlayer test command executed"), false);
                    player.setCamera(mimic);
                    player.setData(ENRAGED, true);
                    return 1;
                })
        );
    }
}
