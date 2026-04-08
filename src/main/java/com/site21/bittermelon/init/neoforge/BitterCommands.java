package com.site21.bittermelon.init.neoforge;

import com.site21.bittermelon.Bittermelon;
import com.site21.bittermelon.common.commands.*;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
import org.jetbrains.annotations.NotNull;

@EventBusSubscriber(modid = Bittermelon.MOD_ID)
public class BitterCommands {

    @SubscribeEvent
    public static void onRegisterCommands(@NotNull RegisterCommandsEvent event) {
        CharacterCommand.register(event.getDispatcher());
        SubstanceCommand.register(event.getDispatcher(), event.getBuildContext());
        StumbleCommand.register(event.getDispatcher());
        CPRCommand.register(event.getDispatcher());
        ChatCommands.register(event.getDispatcher());
        MedicalStatsCommand.register(event.getDispatcher(), event.getBuildContext());
        DrugCommand.register(event.getDispatcher(), event.getBuildContext());
        PrivilegeCommand.register(event.getDispatcher());
        BlockDamageCommand.register(event.getDispatcher());
        StressCommand.register(event.getDispatcher());
        MimicPlayerTestCommand.register(event.getDispatcher());
    }
}
