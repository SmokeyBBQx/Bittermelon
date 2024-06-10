package net.smokeybbq.bittermelon.commands.character;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.players.PlayerList;
import net.smokeybbq.bittermelon.character.Character;
import net.smokeybbq.bittermelon.character.CharacterManager;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Optional;

public class CommandSwitchCharacter {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("switchcharacter")
                .then(Commands.argument("characterName", StringArgumentType.string())
                        .executes(context -> switchCharacter(context)))
        );
    }

    private static int switchCharacter(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        String characterName = StringArgumentType.getString(context, "characterName");
        ServerPlayer player = context.getSource().getPlayerOrException();

        List<Character> characters = CharacterManager.getInstance().getCharacters(player.getUUID());

        Optional<Character> selectedCharacter = characters.stream()
                .filter(c -> c.getName().equalsIgnoreCase(characterName))
                .findFirst();

        if (!selectedCharacter.isPresent()) {
            context.getSource().sendFailure(Component.literal("Character not found: " + characterName));
            return 0;
        }

        if (CharacterManager.getInstance().getActiveCharacter(player.getUUID()) != null) {
            Character currentCharacter = CharacterManager.getInstance().getActiveCharacter(player.getUUID());
            CompoundTag playerData = player.saveWithoutId(new CompoundTag());
            currentCharacter.savePlayerData(playerData);
        }

        if (selectedCharacter.get().getPlayerData() != null) {
            player.load(selectedCharacter.get().getPlayerData());
            player.connection.teleport(player.getX(), player.getY(), player.getZ(), player.getYRot(), player.getXRot());
            player.refreshDimensions();
            player.getInventory().setChanged();
            player.resetSentInfo();
        }

        CharacterManager.getInstance().setActiveCharacter(player, selectedCharacter.get());
        context.getSource().sendSystemMessage(Component.literal("Character switched: " + selectedCharacter.get().getName()));
        return 1;

    }

}
