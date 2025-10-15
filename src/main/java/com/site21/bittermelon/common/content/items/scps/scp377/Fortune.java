package com.site21.bittermelon.common.content.items.scps.scp377;

import com.site21.bittermelon.util.LocalMessageHelper;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ThrownTrident;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.function.Consumer;

import static net.minecraft.world.entity.EntityType.*;

public enum Fortune implements StringRepresentable {
    LIGHTNING("The weather is really just not your friend today.", player
            -> summonEntity(LIGHTNING_BOLT, player, player.getOnPos())),
    BROKEN_BONE("What breaks in a moment may take years to mend.", player
            -> LocalMessageHelper.sendLocalMessage(player, 10, Component.literal(player.getName() +
            " breaks several bones in their back. (Placeholder)"))),
    TELEPORT("This isn’t where you’re meant to be.", player
            -> {
        boolean teleported = false;
        int attempts = 0;
        int maxAttempts = 20;
        int range = 20;

        while (!teleported && attempts <= maxAttempts) {
            attempts++;
        teleported = player.randomTeleport(range, range, range, true);
        }
    }),
    FIRE("You'll be surrounded by warmth.", player
            -> player.setRemainingFireTicks(200)),
    CLEAR_INVENTORY("Your burdens will be lifted", player
            -> player.getInventory().clearContent()),
    STRENGTH("You are stronger than you think.", player
            -> player.addEffect(new MobEffectInstance(MobEffects.STRENGTH, 240, 4, true, false))),
    BREAD("Bread today is better than cake tomorrow", player
            -> player.getInventory().add(new ItemStack(Items.BREAD, 640))),
    SPEAR("A well aimed spear is better than three.", player
            -> {
        ThrownTrident trident = new ThrownTrident(EntityType.TRIDENT, player.level());

        Vec3 eyePosition = player.getEyePosition();
        Vec3 lookDirection = player.getLookAngle();

        Vec3 spawnPos = eyePosition.add(lookDirection.scale(1.0));
        trident.setPos(spawnPos.x, spawnPos.y, spawnPos.z);

        Vec3 velocity = lookDirection.scale(-2.5);
        trident.setDeltaMovement(velocity);

        player.level().addFreshEntity(trident);
    }),
    ATTRACT("People are naturally attracted to you.", player -> {
        AABB box = AABB.ofSize(Vec3.atCenterOf(player.getOnPos()), 10, 10, 10);
        List<Player> players = player.level().getEntitiesOfClass(Player.class, box);
        for (Player otherPlayer : players) {
            otherPlayer.teleportTo(player.getX(), player.getY(), player.getZ());
        }
    }),
    EGGS("Don't put all your eggs in one basket.", player -> {
        for (int i = 0; i < 64; i++) {
            summonEntity(EGG, player, player.getOnPos().above(4));
        }
    }),
    EXPLODE("Show everyone what you can do.", player
            -> summonEntity(TNT, player, player.getOnPos())),
    SPEED("Move quickly. Now is the time to make progress", player
            -> player.addEffect(new MobEffectInstance(MobEffects.SPEED, 1000, 255, true, false)));

    private final String message;
    private final Consumer<Player> behavior;

    Fortune(String message, Consumer<Player> behavior) {
        this.message = message;
        this.behavior = behavior;
    }

    public static Fortune getRandom(@NotNull RandomSource random) {
        return Fortune.values()[random.nextInt(Fortune.values().length)];
    }

    public static final StringRepresentable.EnumCodec<Fortune> CODEC = StringRepresentable.fromEnum(Fortune::values);

    public static final StreamCodec<ByteBuf, Fortune> STREAM_CODEC = ByteBufCodecs.idMapper(
            i -> Fortune.values()[i],
            Fortune::ordinal
    );

    public String getMessage() {
        return message;
    }

    public void applyTo(Player player) {
        behavior.accept(player);
    }

    private static void summonEntity(EntityType<?> entityType, @NotNull Player player, BlockPos pos) {
        if (player.level() instanceof ServerLevel level) {
            entityType.spawn(level, pos, EntitySpawnReason.MOB_SUMMONED);
        }
    }

    @Contract(pure = true)
    @Override
    public @NotNull String getSerializedName() {
        return name().toLowerCase();
    }
}