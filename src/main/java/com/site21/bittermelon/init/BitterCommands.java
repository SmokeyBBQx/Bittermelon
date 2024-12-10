package com.site21.bittermelon.init;

import com.site21.bittermelon.Bittermelon;
import com.site21.bittermelon.commands.CharacterCommand;
import com.site21.bittermelon.commands.StumbleCommand;
import com.site21.bittermelon.commands.SubstanceCommand;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.RegisterCommandsEvent;

@EventBusSubscriber(modid = Bittermelon.MOD_ID)
public class BitterCommands {

    @SubscribeEvent
    public static void onRegisterCommands(RegisterCommandsEvent event) {
        CharacterCommand.register(event.getDispatcher());
        SubstanceCommand.register(event.getDispatcher());
        StumbleCommand.register(event.getDispatcher());
    }
}
