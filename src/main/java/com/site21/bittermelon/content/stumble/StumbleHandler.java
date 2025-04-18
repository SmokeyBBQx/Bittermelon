package com.site21.bittermelon.content.stumble;

import com.site21.bittermelon.Bittermelon;
import com.site21.bittermelon.client.visualeffects.screenshake.StartScreenshake;
import com.site21.bittermelon.content.character.Character;
import com.site21.bittermelon.content.character.CharacterManager;
import com.site21.bittermelon.content.stumble.networking.ClearStumbleTimer;
import com.site21.bittermelon.content.stumble.networking.UpdateStumbleTimer;
import com.site21.bittermelon.networking.client.ClearForcedPose;
import com.site21.bittermelon.networking.client.SetForcedPose;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.tick.EntityTickEvent;
import net.neoforged.neoforge.network.PacketDistributor;
import org.jetbrains.annotations.NotNull;

import java.util.*;

import static com.site21.bittermelon.init.neoforge.BitterAttachmentTypes.STUMBLE_TICKS;
import static com.site21.bittermelon.init.neoforge.BitterSounds.FALL;
import static com.site21.bittermelon.util.LocalMessageHelper.sendLocalMessage;
import static net.minecraft.world.effect.MobEffects.MOVEMENT_SLOWDOWN;


@EventBusSubscriber(modid = Bittermelon.MOD_ID)
public class StumbleHandler {
    private static final Map<UUID, Integer> effectDelays = new HashMap<>();
    private static final ResourceLocation JUMP_STUN_ID = ResourceLocation.fromNamespaceAndPath(Bittermelon.MOD_ID, "jump_stun");
    private static final ResourceLocation MOVEMENT_STUN_ID = ResourceLocation.fromNamespaceAndPath(Bittermelon.MOD_ID, "movement_stun");
    private static final Random RANDOM = new Random();

    public static void stumble(@NotNull LivingEntity entity, int length, Vec3 pushDirection) {
        Pose pose = entity.getPose();
        if (pose == Pose.SLEEPING || pose == Pose.SWIMMING) return;

        if (entity.level().isClientSide) return;

        Character character = CharacterManager.get(entity.level()).getActiveCharacter(entity);
        if (character != null) {
            int movement = (int) character.getMedicalStats().getMovement();
            if (movement > 0) {
                length /= movement;
            } else {
                length = 1000;
            }
        }

        entity.setData(STUMBLE_TICKS.get(), length);
        effectDelays.put(entity.getUUID(), 5);
        motion(entity, length, pushDirection);
        addStun(entity);
        announceFall(entity);

        // Client Handling
        if (entity instanceof ServerPlayer serverPlayer) {
            PacketDistributor.sendToPlayer(serverPlayer, new UpdateStumbleTimer(length));
        }
    }

    public static void stumble(LivingEntity entity) {
        stumble(entity, entity instanceof Player ? 40 : 100, entity.getLookAngle());
    }

    public static void stumble(LivingEntity entity, Vec3 pushDirection) {
        stumble(entity, entity instanceof Player ? 40 : 100, pushDirection);
    }

    private static void motion(@NotNull LivingEntity entity, int length, @NotNull Vec3 pushDirection) {
        Vec3 normalizedPush = pushDirection.normalize();
        Vec3 lookVector = entity.getLookAngle();

        double dotProduct = normalizedPush.dot(lookVector);
        entity.addDeltaMovement(pushDirection.scale(1.2d * entity.getEyeHeight()));
        entity.hurtMarked = true;

        // TODO: Speed based distance

        if (entity instanceof ServerPlayer player) {
            Pose fallPose = dotProduct > 0 ? Pose.SWIMMING : Pose.SLEEPING;

            player.setForcedPose(fallPose);
            PacketDistributor.sendToAllPlayers(new SetForcedPose(player.getUUID(), fallPose));

            if (fallPose == Pose.SLEEPING) {
                dropItem(player, 0.7);
            } else {
                dropItem(player, 0.3);
            }
        } else {
            entity.addEffect(new MobEffectInstance(MOVEMENT_SLOWDOWN, length, 255, false, false));
//            entity.setPose(Pose.SLEEPING);
        }
    }

    private static void addStun(@NotNull LivingEntity entity) {
        AttributeModifier modifier = new AttributeModifier(
                JUMP_STUN_ID,
                -1,
                AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL
        );

        AttributeInstance jumpStrength = entity.getAttribute(Attributes.JUMP_STRENGTH);
        if (jumpStrength != null) {
            if (!jumpStrength.hasModifier(JUMP_STUN_ID)) {
                jumpStrength.addTransientModifier(modifier);
            }
        }

        if (entity.getPose() == Pose.SLEEPING) {
            AttributeModifier speedModifier = new AttributeModifier(
                    MOVEMENT_STUN_ID,
                    -100,
                    AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL
            );

            AttributeInstance movementSpeed = entity.getAttribute(Attributes.MOVEMENT_SPEED);
            if (movementSpeed != null) {
                if (!movementSpeed.hasModifier(MOVEMENT_STUN_ID)) {
                    movementSpeed.addTransientModifier(speedModifier);
                }
            }
        }
    }

    private static void dropItem(Player player, double chance) {
        if (RANDOM.nextDouble() < chance) {
            ItemStack heldItem = player.getMainHandItem();
            if (!heldItem.isEmpty()) {
                player.drop(heldItem.copy(), true);
                heldItem.setCount(0);
            }
        }
    }

    private static void announceFall(@NotNull LivingEntity entity) {
        Character character = CharacterManager.get(entity.level()).getActiveCharacter(entity);
        if (character != null) {
            Component component = Component.literal(character.getName() + " falls to the ground.").withColor(character.getEmoteColor());
            sendLocalMessage(entity, 10, component);
        }
    }

    @SubscribeEvent
    public static void onEntityTick(EntityTickEvent.@NotNull Post event) {
        if (event.getEntity().level().isClientSide) return;

        UUID uuid = event.getEntity().getUUID();
        Entity entity = event.getEntity();

        if (entity.hasData(STUMBLE_TICKS)) {
            int newValue = entity.getData(STUMBLE_TICKS) - 1;
            entity.setData(STUMBLE_TICKS, newValue);
            if (!(entity instanceof Player)) {
                if (newValue <= 0) {
                    clearEntity(entity);
                    entity.setPose(Pose.STANDING);
                }
            }
        }

        if (effectDelays.containsKey(uuid)) {
            Integer delay = effectDelays.compute(uuid, (k, v) -> (v == null) ? 0 : v - 1);
            if (delay <= 0) {
                if (entity instanceof ServerPlayer player) {
                    PacketDistributor.sendToPlayer(player, new StartScreenshake(70, 10));
                }
                entity.level().playSound(null, entity.getOnPos(), FALL.get(), SoundSource.PLAYERS);
                effectDelays.remove(uuid);
            }
        }
    }

    private static void clearEntity(@NotNull Entity entity) {
        entity.removeData(STUMBLE_TICKS);
        if (entity instanceof LivingEntity livingEntity) {
            Objects.requireNonNull(livingEntity.getAttribute(Attributes.JUMP_STRENGTH)).removeModifier(JUMP_STUN_ID);
            Objects.requireNonNull(livingEntity.getAttribute(Attributes.MOVEMENT_SPEED)).removeModifier(MOVEMENT_STUN_ID);
        }
    }

    public static void attemptToRise(UUID uuid, @NotNull ServerLevel level) {
        Player player = level.getPlayerByUUID(uuid);
        if (player == null) return;
        int value = player.getData(STUMBLE_TICKS.get());
        if (value <= 0) {
            clearEntity(player);

            // Client Handling
            if (player instanceof ServerPlayer serverPlayer) {
                serverPlayer.setForcedPose(null);
                PacketDistributor.sendToAllPlayers(new ClearForcedPose(uuid));
                PacketDistributor.sendToPlayer(serverPlayer, new ClearStumbleTimer());
            }
        }
    }

    public static boolean isStunned(@NotNull Entity entity) {
        return entity.getData(STUMBLE_TICKS) > 0;
    }

    public static boolean isStumbled(@NotNull Entity entity) {
        return entity.hasData(STUMBLE_TICKS);
    }
}
