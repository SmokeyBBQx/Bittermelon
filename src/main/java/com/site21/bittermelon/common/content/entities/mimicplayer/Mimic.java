package com.site21.bittermelon.common.content.entities.mimicplayer;

import com.site21.bittermelon.common.systems.ai.base.BitterMob;
import com.site21.bittermelon.common.systems.ai.base.Need;
import com.site21.bittermelon.common.systems.ai.base.NeedInstance;
import com.site21.bittermelon.common.systems.character.Character;
import com.site21.bittermelon.common.systems.character.CharacterManager;
import com.site21.bittermelon.init.neoforge.BitterDataSerializers;
import net.minecraft.core.UUIDUtil;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.behavior.BehaviorControl;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.PlayerModelPart;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.tslat.smartbrainlib.api.core.behaviour.FirstApplicableBehaviour;
import net.tslat.smartbrainlib.api.core.behaviour.OneRandomBehaviour;
import net.tslat.smartbrainlib.api.core.behaviour.custom.attack.AnimatableMeleeAttack;
import net.tslat.smartbrainlib.api.core.behaviour.custom.look.LookAtTarget;
import net.tslat.smartbrainlib.api.core.behaviour.custom.misc.Idle;
import net.tslat.smartbrainlib.api.core.behaviour.custom.move.WalkOrRunToWalkTarget;
import net.tslat.smartbrainlib.api.core.behaviour.custom.path.SetRandomWalkTarget;
import net.tslat.smartbrainlib.api.core.behaviour.custom.path.SetWalkTargetToAttackTarget;
import net.tslat.smartbrainlib.api.core.behaviour.custom.target.InvalidateAttackTarget;
import net.tslat.smartbrainlib.api.core.behaviour.custom.target.SetPlayerLookTarget;
import net.tslat.smartbrainlib.api.core.behaviour.custom.target.SetRandomLookTarget;
import net.tslat.smartbrainlib.api.core.behaviour.custom.target.TargetOrRetaliate;
import net.tslat.smartbrainlib.api.core.sensor.ExtendedSensor;
import net.tslat.smartbrainlib.api.core.sensor.vanilla.HurtBySensor;
import net.tslat.smartbrainlib.api.core.sensor.vanilla.NearbyLivingEntitySensor;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import static com.site21.bittermelon.init.neoforge.BitterAttachmentTypes.ENRAGED;
import static com.site21.bittermelon.init.neoforge.BitterAttachmentTypes.RAGE;
import static com.site21.bittermelon.init.neoforge.BitterEntities.MIMIC;

public class Mimic extends BitterMob<Mimic> {
    private Player player;
    private static final EntityDataAccessor<UUID> PLAYER_UUID =
            SynchedEntityData.defineId(Mimic.class, BitterDataSerializers.UUID.get());

    public Mimic(EntityType<? extends PathfinderMob> entityType, Level level) {
        super(entityType, level);
    }

    @Override
    protected Character initializeCharacter() {
        Player player = getPlayer();
        if (player == null) {
            return new Character(uuid, "Mimic");
        }
        return CharacterManager.get(level()).getActiveCharacter(player);
    }

    @Override
    protected Map<Need, NeedInstance> initializeNeeds() {
        return Map.of();
    }

    public Mimic(Level level, Player player) {
        this(MIMIC.get(), level);
        this.player = player;
        getEntityData().set(PLAYER_UUID, player.getUUID());
        setXRot(player.getXRot());
        setYRot(player.getYRot());
        setYHeadRot(player.getYHeadRot());
        setYBodyRot(player.yBodyRot);
    }

    public static AttributeSupplier.@NotNull Builder createAttributes() {
        // Copied from Player
        return Mob.createMobAttributes()
                .add(Attributes.ATTACK_DAMAGE, 1.0)
                .add(Attributes.MOVEMENT_SPEED, 0.3f)
                .add(Attributes.ATTACK_SPEED)
                .add(Attributes.LUCK)
                .add(Attributes.BLOCK_INTERACTION_RANGE, 4.5)
                .add(Attributes.ENTITY_INTERACTION_RANGE, 3.0)
                .add(Attributes.BLOCK_BREAK_SPEED)
                .add(Attributes.SUBMERGED_MINING_SPEED)
                .add(Attributes.SNEAKING_SPEED)
                .add(Attributes.MINING_EFFICIENCY)
                .add(Attributes.SWEEPING_DAMAGE_RATIO)
                .add(Attributes.WAYPOINT_TRANSMIT_RANGE, 6.0E7)
                .add(Attributes.WAYPOINT_RECEIVE_RANGE, 6.0E7);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(PLAYER_UUID, new UUID(0, 0));
    }

    @Override
    public List<? extends ExtendedSensor<?>> getSensors(Mimic owner) {
        return List.of(
                new NearbyLivingEntitySensor<>(),
                new HurtBySensor<>()
        );
    }

    @Override
    public List<? extends BehaviorControl<?>> getAlwaysRunningBehaviours(Mimic owner) {
        return List.of(
                new LookAtTarget<>(),
                new WalkOrRunToWalkTarget<>()
        );
    }

    @Override
    public List<? extends BehaviorControl<?>> getIdleBehaviours(Mimic owner) {
        return List.of(
                new FirstApplicableBehaviour<Mimic>(
                        new TargetOrRetaliate<>(),
                        new SetPlayerLookTarget<>(),
                        new SetRandomLookTarget<>()),
                new OneRandomBehaviour<>(
                        new SetRandomWalkTarget<>(),
                        new Idle<>().runFor(entity -> entity.getRandom().nextInt(30, 60)))
        );
    }

    @Override
    public List<? extends BehaviorControl<?>> getFightingBehaviours(Mimic owner) {
        return List.of(
                new InvalidateAttackTarget<>(),
                new SetWalkTargetToAttackTarget<>(),
                new AnimatableMeleeAttack<>(0)
        );
    }

    @Override
    protected void customServerAiStep(@NotNull ServerLevel level) {
        super.customServerAiStep(level);
        updateSwingTime();

        Player player = getPlayer();
        if (player != null) {
            if (player.getData(RAGE) < 50 || (player instanceof ServerPlayer sPlayer && sPlayer.getCamera() != this)) {
                discard();
            }
        }
    }

    @Override
    public void onRemoval(RemovalReason reason) {
        super.onRemoval(reason);

        Player player = getPlayer();
        if (player == null) return;

        player.setXRot(getXRot());
        player.setYRot(getYRot());
        player.setYHeadRot(getYHeadRot());
        player.setYBodyRot(yBodyRot);
        player.setData(ENRAGED, false);

        if (player instanceof ServerPlayer serverPlayer) {
            serverPlayer.setCamera(null);
            serverPlayer.setGameMode(GameType.DEFAULT_MODE);
        }
    }

    public Player getPlayer() {
        if (player == null) {
            player = level().getPlayerByUUID(entityData.get(PLAYER_UUID));
        }

        return player;
    }

    public boolean isModelPartShown(PlayerModelPart part) {
        Player player = getPlayer();
        return player == null || player.isModelPartShown(part);
    }

    @Override
    protected void addAdditionalSaveData(@NotNull ValueOutput output) {
        super.addAdditionalSaveData(output);
        output.store("PlayerUUID", UUIDUtil.CODEC, entityData.get(PLAYER_UUID));
    }

    @Override
    protected void readAdditionalSaveData(@NotNull ValueInput input) {
        super.readAdditionalSaveData(input);
        entityData.set(PLAYER_UUID, input.read("PlayerUUID", UUIDUtil.CODEC).orElse(new UUID(0, 0)));
    }
}
