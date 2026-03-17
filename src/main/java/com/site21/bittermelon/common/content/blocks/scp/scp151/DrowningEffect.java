package com.site21.bittermelon.common.content.blocks.scp.scp151;

import com.site21.bittermelon.common.content.blocks.substance.fluid.FluidBlock;
import com.site21.bittermelon.common.systems.character.Character;
import com.site21.bittermelon.common.systems.character.CharacterManager;
import com.site21.bittermelon.common.systems.stumble.StumbleHandler;
import com.site21.bittermelon.common.systems.substance.SubstanceStack;
import com.site21.bittermelon.init.custom.Substances;
import com.site21.bittermelon.init.neoforge.BitterSounds;
import com.site21.bittermelon.util.LocalMessageHelper;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundSoundPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

import static com.site21.bittermelon.init.neoforge.BitterBlocks.FLUID;
import static com.site21.bittermelon.init.neoforge.BitterMobEffects.DROWNING;
import static net.minecraft.world.level.block.Block.UPDATE_ALL_IMMEDIATE;

public class DrowningEffect extends MobEffect {
    public DrowningEffect() {
        super(MobEffectCategory.HARMFUL, 0x2E2E2E);
    }

    @Override
    public boolean shouldApplyEffectTickThisTick(int duration, int amplifier) {
        return duration % 20 == 0;
    }

    @Override
    public boolean applyEffectTick(@NotNull ServerLevel level, @NotNull LivingEntity entity, int amplifier) {
        if (!(entity instanceof Player player)) return true;

        int ticksRemaining = Objects.requireNonNull(entity.getEffect(DROWNING)).getDuration();
        int progressionInterval = amplifier < 4 ? 5000 : 400;

        if (ticksRemaining % progressionInterval == 0) {
            int newAmplifier = amplifier + 1;
            handleDrownProgression(player, newAmplifier);

            Objects.requireNonNull(entity.getEffect(DROWNING)).update(
                    new MobEffectInstance(DROWNING, ticksRemaining, newAmplifier, true, false, false));
//                System.out.println("Progressing amplifier to: " + newAmplifier + " with " + ticksRemaining + " ticks remaining");
        }

        handlePhysicalEffects(player, amplifier);
        return true;
    }

    private void handleDrownProgression(Player player, int level) {
        String message = "";
        if (level == 1) {
            message = "Your chest tightens and each breath comes out as a wheeze.";
        } else if (level == 3) {
            message = "Your heart races and the world starts to spin.";
        } else if (level == 4) {
            message = "Saltwater burns down your throat. You cough and gag violently.";
            playHeartBeatSound(player);
        } else if (level == 5) {
            message = "You hack desperately but the water stays trapped in your lungs.";
        } else if (level == 6) {
            message = "More water rushes in. Your thoughts become foggy and slow.";
            StumbleHandler.stumble(player);
        } else if (level == 7) {
            message = "A deep chill spreads through your body.";
           playHeartBeatSound(player);
        } else if (level == 8) {
            message = "Your vision fades to black as consciousness slips away.";
            StumbleHandler.stumble(player, -1, player.getLookAngle());
        } else if (level == 9) {
            message = "The struggle leaves your body. You feel strangely peaceful.";
        }

        if (!message.isBlank()) {
            Component component = Component.literal(message).withStyle(ChatFormatting.RED).withStyle(ChatFormatting.ITALIC);
            player.displayClientMessage(component, false);
        }
    }

    private void playHeartBeatSound(Player player) {
        if (player instanceof ServerPlayer serverPlayer) {
            serverPlayer.connection.send(new ClientboundSoundPacket(
                    BitterSounds.HEART_BEAT,
                    SoundSource.AMBIENT,
                    player.getX(),
                    player.getY(),
                    player.getZ(),
                    0.3f,
                    1,
                    player.level().getRandom().nextLong()));
        }
    }

    private void handlePhysicalEffects(Player player, int amplifier) {
        if (amplifier >= 4) {
            player.setAirSupply(player.getAirSupply() - 2);
            player.hurtMarked = true;
        }

        Character character = CharacterManager.get(player.level()).getActiveCharacter(player);
        String message = "";

        if (amplifier < 9 && amplifier > 0) {
            if (player.level().getRandom().nextFloat() > 0.8f) {
                player.level().playSound(null, player.getOnPos(), BitterSounds.MALE_COUGH.value(), SoundSource.PLAYERS);
                if (character != null) {
                    message = character.getName() + " coughs!";
                }
            } else if (player.level().getRandom().nextFloat() > 0.98f && amplifier > 4) {
                handleVomit(player);
                if (character != null) {
                    message = character.getName() + " vomits!";
                }
            }

            if (!message.isEmpty()) {
                Component component = Component.literal(message).withColor(character.getEmoteColor());
                LocalMessageHelper.sendLocalMessage(player, 10, component);
            }
        }
    }

    private void handleVomit(@NotNull Player player) {
        if (player.level().isClientSide) return;

        sendVomitParticles(player);

        SubstanceStack vomit = new SubstanceStack(Substances.VOMIT.get(), 1);
        vomit.setVolume(3);
        SubstanceStack water = new SubstanceStack(Substances.WATER.get(), 1);
        water.setVolume(1);

        BlockPos pos = player.getOnPos().above();
        BlockState existingState = player.level().getBlockState(pos);

        if (!(existingState.getBlock() instanceof FluidBlock) && existingState.canBeReplaced()) {
            player.level().setBlock(pos, FLUID.get().defaultBlockState(), UPDATE_ALL_IMMEDIATE);
        }

        // TODO: Reimplement substance transfer to blocks
//        if (player.level().getBlockEntity(pos) instanceof FluidBlockEntity fluid) {
//            fluid.updateSubstance(vomit);
//            fluid.updateSubstance(water);
//        }

        player.level().playSound(null, player.getOnPos(), BitterSounds.SPLAT.value(), SoundSource.PLAYERS);
    }

    private void sendVomitParticles(@NotNull Player player) {
        if (player.level() instanceof ServerLevel serverLevel) {
            double x = player.getX();
            double y = player.getY() + 0.1;
            double z = player.getZ();

            for (int i = 0; i < 10; i++) {
                double offsetX = (player.level().getRandom().nextDouble() - 0.5) * 0.8;
                double offsetY = player.level().getRandom().nextDouble() * 0.3;
                double offsetZ = (player.level().getRandom().nextDouble() - 0.5) * 0.8;

                serverLevel.sendParticles(
                        net.minecraft.core.particles.ParticleTypes.ITEM_SLIME,
                        x + offsetX,
                        y + offsetY,
                        z + offsetZ,
                        1,
                        0, 0, 0,
                        0.01
                );
            }
        }
    }
}
