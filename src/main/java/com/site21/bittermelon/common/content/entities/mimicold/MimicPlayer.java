package com.site21.bittermelon.common.content.entities.mimicold;

import com.mojang.authlib.GameProfile;
import com.site21.bittermelon.common.content.entities.mimicold.behavior.LookAtTarget;
import com.site21.bittermelon.common.content.entities.mimicold.behavior.MoveToWalkTarget;
import com.site21.bittermelon.common.content.entities.mimicold.behavior.SetRandomWalkTarget;
import com.site21.bittermelon.common.content.entities.mimicold.navigation.NPCGroundPathNavigation;
import com.site21.bittermelon.common.content.entities.mimicold.navigation.NPCLookControl;
import com.site21.bittermelon.common.content.entities.mimicold.navigation.NPCMoveControl;
import com.site21.bittermelon.common.content.entities.mimicold.navigation.NPCNavigation;
import io.netty.channel.ChannelFutureListener;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.PacketFlow;
import net.minecraft.network.protocol.game.ClientboundPlayerPositionPacket;
import net.minecraft.network.protocol.game.ServerboundAcceptTeleportationPacket;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ClientInformation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.CommonListenerCookie;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.pathfinder.PathType;
import net.tslat.smartbrainlib.api.SmartBrainOwner;
import net.tslat.smartbrainlib.api.core.BrainActivityGroup;
import net.tslat.smartbrainlib.api.core.SmartBrainProvider;
import net.tslat.smartbrainlib.api.core.behaviour.custom.target.SetPlayerLookTarget;
import net.tslat.smartbrainlib.api.core.sensor.ExtendedSensor;
import net.tslat.smartbrainlib.api.core.sensor.vanilla.NearbyPlayersSensor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;

public class MimicPlayer extends ServerPlayer implements SmartBrainOwner<MimicPlayer> {
    protected NPCNavigation navigation;
    protected NPCMoveControl moveControl;
    protected NPCLookControl lookControl;

    public MimicPlayer(MinecraftServer server, ServerLevel level, GameProfile gameProfile, ClientInformation clientInformation) {
        super(server, level, gameProfile, clientInformation);
        connection = createConnection(server, new FakeConnection(), CommonListenerCookie.createInitial(gameProfile, false));
        navigation = new NPCGroundPathNavigation(this, level);
        moveControl = new NPCMoveControl(this);
        lookControl = new NPCLookControl(this);
    }

    public FakePlayerNetHandler createConnection(MinecraftServer server, net.minecraft.network.Connection connection,
                                                 CommonListenerCookie cookie) {
        return new FakePlayerNetHandler(server, connection, this, cookie);
    }

    @Override
    public List<? extends ExtendedSensor<? extends MimicPlayer>> getSensors() {
        return List.of(new NearbyPlayersSensor<MimicPlayer>().setRadius(32));
    }

    @Override
    public BrainActivityGroup<? extends MimicPlayer> getCoreTasks() {
        return BrainActivityGroup.coreTasks(
                new LookAtTarget<>(),
                new MoveToWalkTarget<>()
        );
    }

    @Override
    public BrainActivityGroup<? extends MimicPlayer> getIdleTasks() {
        return BrainActivityGroup.idleTasks(
                new SetPlayerLookTarget<>(),
                new SetRandomWalkTarget<>()
        );
    }

    @Override
    public void tick() {
        if (getServer().getTickCount() % 10 == 0) {
            connection.resetPosition();
            level().getChunkSource().move(this);
        }
        doTick();
        super.tick();
    }

    @Override
    protected void serverAiStep() {
        super.serverAiStep();

        navigation.tick();
        moveControl.tick();
        lookControl.tick();

        tickBrain(this);
    }

    public NPCNavigation getNavigation() {
        return this.navigation;
    }

    public NPCMoveControl getMoveControl() {
        return this.moveControl;
    }

    public NPCLookControl getLookControl() {
        return this.lookControl;
    }

    @Override
    public boolean isClientAuthoritative() {
        return false;
    }

    @Override
    protected @NotNull SmartBrainProvider<MimicPlayer> brainProvider() {
        return new SmartBrainProvider<>(this);
    }

    @Override
    public @Nullable GameType gameMode() {
        return GameType.SURVIVAL;
    }

    public float getPathfindingMalus(PathType pathType) {
        return pathType.getMalus();
    }

    @ParametersAreNonnullByDefault
    private static class FakePlayerNetHandler extends ServerGamePacketListenerImpl {

        public FakePlayerNetHandler(MinecraftServer server, net.minecraft.network.Connection connection, ServerPlayer player, CommonListenerCookie cookie) {
            super(server, connection, player, cookie);
        }

        @Override
        public void tick() {
            player.doTick();
        }

        @Override
        public void send(Packet<?> packet, @Nullable ChannelFutureListener sendListener) {
            if (packet instanceof ClientboundPlayerPositionPacket) {
                handleAcceptTeleportPacket(new ServerboundAcceptTeleportationPacket(((ClientboundPlayerPositionPacket) packet).id()));
            }
        }
    }

    private static final class FakeConnection extends net.minecraft.network.Connection {
        public FakeConnection() {
            super(PacketFlow.SERVERBOUND);
        }
    }
}
