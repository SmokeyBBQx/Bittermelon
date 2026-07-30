package com.site21.bittermelon.common.content.blocks.scp.scp151;

import com.site21.bittermelon.common.systems.character.Character;
import com.site21.bittermelon.common.systems.character.CharacterManager;
import com.site21.bittermelon.common.systems.stumble.StumbleHandler;
import com.site21.bittermelon.init.neoforge.BitterSounds;
import com.site21.bittermelon.util.LocalMessageUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundSoundPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

import static com.site21.bittermelon.init.neoforge.BitterMobEffects.DROWNING;

public class DrowningEffect extends MobEffect {

    private static final int MAX_STAGE = 9;
    private static final int EARLY_STAGE_INTERVAL = 5000;
    private static final int LATE_STAGE_INTERVAL = 400;
    private static final int LATE_STAGE_START = 4;

    private static final float FILLER_CHANCE = 0.03f;
    private static final float COUGH_CHANCE = 0.35f;
    private static final int COUGH_COOLDOWN_TICKS = 30;
    private static final int COUGH_STAGE_MIN = 4;
    private static final int COUGH_STAGE_MAX = 5;

    private static final String[][] PROGRESSION_MESSAGES = {
            {},
            {"Your chest tightens and each breath comes out as a wheeze."},
            {},
            {"Your heart races and the world starts to spin."},
            {"Saltwater burns the back of your throat. You cough and gag violently."},
            {"You hack desperately but the water stays trapped in your lungs."},
            {"More water rushes in. Your thoughts become foggy and slow."},
            {"A deep chill spreads through your body."},
            {"Your vision fades to black as consciousness slips away."},
            {"The struggle leaves your body. You feel strangely peaceful."}
    };

    private static final String[][] FILLER_MESSAGES = {
            {},
            {
                    "Your breathing feels... off.",
                    "You can't seem to take a satisfying breath.",
                    "A strange tightness settles in your chest.",
                    "You clear your throat, but the feeling remains."
            },
            {
                    "You cough into your hand.",
                    "Your throat feels oddly damp.",
                    "A dry cough interrupts your breathing."
            },
            {
                    "You can't catch your breath.",
                    "Each breath feels smaller than the last.",
                    "You begin breathing faster without realizing it."
            },
            {
                    "You cough up salty fluid.",
                    "A sharp taste of salt fills your mouth.",
                    "Something wet rattles deep inside your lungs."
            },
            {
                    "No matter how hard you cough, your lungs remain heavy.",
                    "Your breathing turns wet and ragged.",
                    "Every breath crackles."
            },
            {
                    "Your fingertips begin to tingle.",
                    "The room won't stop spinning.",
                    "Your thoughts come slower than they should."
            },
            {
                    "Every breath sounds like water.",
                    "You hear waves that aren't there.",
                    "Your chest feels completely full."
            },
            {
                    "Your legs refuse to cooperate.",
                    "Your vision narrows to a tunnel.",
                    "Your lungs burn."
            },
            {
                    "Your body finally stops fighting.",
                    "The weight in your lungs becomes strangely comforting.",
                    "You stop reaching for air."
            }
    };

    private static final String[] COUGH_MESSAGES = {
            "%s coughs!",
            "%s hacks violently!",
            "%s doubles over, coughing!",
            "%s sputters and coughs!",
            "%s coughs up water!"
    };

    private final Map<UUID, Set<Integer>> fillerStagesShown = new HashMap<>();
    private final Map<UUID, Integer> lastCoughIndex = new HashMap<>();
    private final Map<UUID, Integer> lastCoughTick = new HashMap<>();

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

        if (amplifier == 0) {
            fillerStagesShown.remove(player.getUUID());
        }

        int ticksRemaining = Objects.requireNonNull(entity.getEffect(DROWNING)).getDuration();
        int progressionInterval = amplifier < LATE_STAGE_START ? EARLY_STAGE_INTERVAL : LATE_STAGE_INTERVAL;

        if (ticksRemaining % progressionInterval == 0) {
            int nextStage = amplifier + 1;
            advanceStage(player, nextStage);
            entity.getEffect(DROWNING).update(new MobEffectInstance(DROWNING, ticksRemaining, nextStage, true, false, false));
        } else {
            tryShowFillerMessage(player, amplifier);
        }

        tryCough(player, amplifier);
        applyAirLoss(player, amplifier);
        return true;
    }

    private void advanceStage(Player player, int stage) {
        stringAt(PROGRESSION_MESSAGES, stage).ifPresent(message -> sendMessage(player, message, ChatFormatting.RED));

        switch (stage) {
            case 4, 7 -> playHeartBeatSound(player);
            case 6 -> StumbleHandler.stumble(player);
            case 8 -> StumbleHandler.stumble(player, -1, player.getLookAngle());
        }
    }

    private void tryShowFillerMessage(Player player, int stage) {
        if (stage <= 0 || stage > MAX_STAGE) return;

        String[] pool = FILLER_MESSAGES[stage];
        if (pool.length == 0) return;

        Set<Integer> shownStages = fillerStagesShown.computeIfAbsent(player.getUUID(), k -> new HashSet<>());
        if (shownStages.contains(stage)) return;

        RandomSource random = player.level().getRandom();
        if (random.nextFloat() > FILLER_CHANCE) return;

        sendMessage(player, pickRandom(random, pool), ChatFormatting.RED);
        shownStages.add(stage);
    }

    private void tryCough(Player player, int stage) {
        if (stage < COUGH_STAGE_MIN || stage > COUGH_STAGE_MAX) return;

        UUID id = player.getUUID();
        int currentTick = player.tickCount;
        boolean offCooldown = currentTick - lastCoughTick.getOrDefault(id, -COUGH_COOLDOWN_TICKS) >= COUGH_COOLDOWN_TICKS;
        if (!offCooldown) return;

        RandomSource random = player.level().getRandom();
        if (random.nextFloat() > COUGH_CHANCE) return;

        lastCoughTick.put(id, currentTick);
        player.level().playSound(null, player.getOnPos(), BitterSounds.MALE_COUGH.value(), SoundSource.PLAYERS);

        Character character = CharacterManager.get(player.level()).getActiveCharacter(player);
        if (character == null) return;

        int index = pickNonRepeatingIndex(random, COUGH_MESSAGES.length, lastCoughIndex.get(id));
        lastCoughIndex.put(id, index);

        String message = String.format(COUGH_MESSAGES[index], character.getName());
        LocalMessageUtil.sendLocalMessage(player, 10, Component.literal(message).withColor(character.getEmoteColor()));
    }

    private void applyAirLoss(Player player, int stage) {
        if (stage < LATE_STAGE_START) return;
        player.setAirSupply(player.getAirSupply() - 2);
        player.hurtMarked = true;
    }

    private void playHeartBeatSound(Player player) {
        if (!(player instanceof ServerPlayer serverPlayer)) return;

        serverPlayer.connection.send(new ClientboundSoundPacket(
                BitterSounds.HEART_BEAT,
                SoundSource.AMBIENT,
                player.getX(), player.getY(), player.getZ(),
                0.3f, 1, player.level().getRandom().nextLong()));
    }

    private void sendMessage(Player player, String message, ChatFormatting color) {
        player.sendSystemMessage(Component.literal(message).withStyle(color).withStyle(ChatFormatting.ITALIC));
    }

    private java.util.Optional<String> stringAt(String[][] pools, int index) {
        if (index < 0 || index >= pools.length || pools[index].length == 0) return java.util.Optional.empty();
        return java.util.Optional.of(pools[index][0]);
    }

    private String pickRandom(RandomSource random, String[] pool) {
        return pool[random.nextInt(pool.length)];
    }

    private int pickNonRepeatingIndex(RandomSource random, int poolSize, Integer previousIndex) {
        if (poolSize <= 1) return 0;

        int index = random.nextInt(poolSize);
        while (previousIndex != null && index == previousIndex) {
            index = random.nextInt(poolSize);
        }
        return index;
    }
}