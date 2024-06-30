package net.smokeybbq.bittermelon.medical.symptoms;

import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.*;
import net.minecraft.network.protocol.game.ClientboundClearTitlesPacket;
import net.minecraft.network.protocol.game.ClientboundSetActionBarTextPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.smokeybbq.bittermelon.character.Character;

import java.util.concurrent.ThreadLocalRandom;

import static net.smokeybbq.bittermelon.util.LocalMessageHandler.sendLocalMessage;

@Mod.EventBusSubscriber(modid = "bittermelon")
public class ItchyRash extends Rash {
    private int ticksSinceLastReminder = 0;
    private int TICKS_BETWEEN_REMINDERS = 0;
    private int REMINDER_MINIMUM_TICKS = 400;
    private int REMINDER_MAXIMUM_TICKS = 600;
    boolean scratched = false;
    private static final int TICKS_REQUIRED = 100;
    private int ticksHeld = 0;

    public ItchyRash(Character character, String affectedArea, float amplifier) {
        super(character, affectedArea, amplifier);
        this.REMINDER_MINIMUM_TICKS -= (int) (amplifier * 5);
        this.REMINDER_MAXIMUM_TICKS -= (int) (amplifier * 10);
        MinecraftForge.EVENT_BUS.register(this);
    }

    @Override
    public void update() {
        effects();
    }

    @Override
    public void initializeDescriptions() {

    }

    @Override
    public void effects() {
        ticksSinceLastReminder++;
        TICKS_BETWEEN_REMINDERS = ThreadLocalRandom.current().nextInt(REMINDER_MINIMUM_TICKS, REMINDER_MAXIMUM_TICKS);

        if (ticksSinceLastReminder >= TICKS_BETWEEN_REMINDERS) {
            itch();
            ticksSinceLastReminder = 0;
        }

        if (!(entity instanceof ServerPlayer)) {
            scratch();
        }

        if (scratched) {
            entity.removeEffect(MobEffects.MOVEMENT_SLOWDOWN);

            if (entity instanceof ServerPlayer player) {
                player.connection.send(new ClientboundClearTitlesPacket(true));
            }
        }
    }

    @SubscribeEvent
    @OnlyIn(Dist.CLIENT)
    public void onClientTick(TickEvent.ClientTickEvent event) {
        if (Minecraft.getInstance().options.keyUse.isDown() && !scratched) {
            ticksHeld++;
            if (ticksHeld >= TICKS_REQUIRED) {
                ticksHeld = 0;
                scratch();
            }
        } else {
            ticksHeld = 0;
        }
    }

    private void scratch() {
        scratched = true;
        Component messageComponent = Component.literal(character.getName() + " scratches their " + affectedArea + ".").setStyle(Style.EMPTY.withColor(TextColor.parseColor(character.getEmoteColor())));
        sendLocalMessage(entity, 5, messageComponent);
    }

    private void itch() {
        scratched = false;
        entity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, TICKS_BETWEEN_REMINDERS, 1, false, true));

        if (entity instanceof ServerPlayer player) {
            player.connection.send(new ClientboundSetActionBarTextPacket(Component.literal("Your rash itches! Hold right-click to scratch it.").setStyle(Style.EMPTY.withColor(TextColor.parseColor("#eb6b34")))));
        }
    }

    public void setAmplifier(int amplifier) {
        REMINDER_MAXIMUM_TICKS -= amplifier * 10;
    }

    public void decreaseItchRate(int decreaseTicks) {
        REMINDER_MINIMUM_TICKS += decreaseTicks;
        REMINDER_MAXIMUM_TICKS += decreaseTicks;
    }
}
