package net.smokeybbq.bittermelon.medical.symptoms;

import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.*;
import net.minecraft.network.protocol.game.ClientboundClearTitlesPacket;
import net.minecraft.network.protocol.game.ClientboundSetActionBarTextPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.smokeybbq.bittermelon.character.Character;
import net.smokeybbq.bittermelon.character.CharacterManager;

import java.util.concurrent.ThreadLocalRandom;

@Mod.EventBusSubscriber(modid = "bittermelon")
public class Rash extends Symptom {
    String name = "Rash";
    int ticksSinceLastReminder = 0;
    int TICKS_BETWEEN_REMINDERS = 0;
    private int REMINDER_MINIMUM_TICKS = 400;
    private int REMINDER_MAXIMUM_TICKS = 600;
    boolean scratched = false;
    private static final int TICKS_REQUIRED = 100;
    private int ticksHeld = 0;
    private String bodyPart;

    public Rash(double amplifier, Character character, String bodyPart) {
        super(amplifier, character);
        this.REMINDER_MINIMUM_TICKS -= amplifier * 5;
        this.REMINDER_MAXIMUM_TICKS -= amplifier * 10;
        this.bodyPart = bodyPart;
        MinecraftForge.EVENT_BUS.register(this);
    }

    @Override
    public void update() {
        effects();
    }

    @Override
    public void effects() {
        ticksSinceLastReminder++;
        TICKS_BETWEEN_REMINDERS = ThreadLocalRandom.current().nextInt(REMINDER_MINIMUM_TICKS, REMINDER_MAXIMUM_TICKS);

        if (ticksSinceLastReminder >= TICKS_BETWEEN_REMINDERS) {
            itch();
            ticksSinceLastReminder = 0;
        }

        if (scratched) {
            player.removeEffect(MobEffects.MOVEMENT_SLOWDOWN);
            player.connection.send(new ClientboundClearTitlesPacket(true));
        }
    }

    @SubscribeEvent
    @OnlyIn(Dist.CLIENT)
    public void onClientTick(TickEvent.ClientTickEvent event) {
        if (Minecraft.getInstance().options.keyUse.isDown() && !scratched) {
            ticksHeld++;
            if (ticksHeld >= TICKS_REQUIRED) {
                scratched = true;
                ticksHeld = 0;
                Character character = CharacterManager.getInstance().getActiveCharacter(player.getUUID());
                player.sendSystemMessage(Component.literal(character.getName() + " scratches their " + bodyPart + ".").setStyle(Style.EMPTY.withColor(TextColor.parseColor(character.getEmoteColor()))));
            }
        } else {
            ticksHeld = 0;
        }
    }

    private void itch() {
        scratched = false;
        player.connection.send(new ClientboundSetActionBarTextPacket(Component.literal("Your rash itches! Hold right-click to scratch it.").setStyle(Style.EMPTY.withColor(TextColor.parseColor("#eb6b34")))));
        player.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, TICKS_BETWEEN_REMINDERS, 1, false, true));
    }

    public void setAmplifier(int amplifier) {
        REMINDER_MAXIMUM_TICKS -= amplifier * 10;
    }

    public void decreaseItchRate(int decreaseTicks) {
        REMINDER_MINIMUM_TICKS += decreaseTicks;
        REMINDER_MAXIMUM_TICKS += decreaseTicks;
    }
}
