package com.site21.bittermelon.content.items.wirecutter.networking;

import com.site21.bittermelon.Bittermelon;
import com.site21.bittermelon.content.blocks.devices.ElectronicDevice;
import com.site21.bittermelon.content.blocks.devices.wiring.Port;
import com.site21.bittermelon.content.character.Character;
import com.site21.bittermelon.content.character.CharacterManager;
import com.site21.bittermelon.content.character.skills.Skill;
import com.site21.bittermelon.content.stumble.StumbleHandler;
import com.site21.bittermelon.init.neoforge.BitterSounds;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.BlockPos;
import net.minecraft.core.UUIDUtil;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

import static com.site21.bittermelon.init.neoforge.BitterMobEffects.ELECTROCUTED;
import static com.site21.bittermelon.init.neoforge.BitterMobEffects.STUN;

public record CutWire(BlockPos pos, String portId, UUID playerId, boolean inputPort) implements CustomPacketPayload {
    public static final Type<CutWire> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(Bittermelon.MOD_ID, "cut_wire"));

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static final StreamCodec<ByteBuf, CutWire> STREAM_CODEC = StreamCodec.composite(
            BlockPos.STREAM_CODEC,
            CutWire::pos,
            ByteBufCodecs.STRING_UTF8,
            CutWire::portId,
            UUIDUtil.STREAM_CODEC,
            CutWire::playerId,
            ByteBufCodecs.BOOL,
            CutWire::inputPort,
            CutWire::new
    );

    public void handle(@NotNull IPayloadContext ctx) {
        Level level = ctx.player().level();
        Player player = level.getPlayerByUUID(playerId);
        if (player == null) return;

        level.playSound(null, pos, BitterSounds.WIRE_CUTTERS.get(), SoundSource.PLAYERS, 1.0f, 1.0f);

        float chance = 0.5f;

        Character character = CharacterManager.get(level).getActiveCharacter(player);
        if (character != null) {
            chance += character.getSkill(Skill.ELECTRICAL) - 1;
        }

        RandomSource random = player.getRandom();
        if (random.nextFloat() > chance) {
            StumbleHandler.stumble(player, 200, player.getLookAngle().scale(-1));
            level.playSound(null, pos, BitterSounds.ZAP.get(), SoundSource.PLAYERS, 1.0f, 1.0f);
            player.addEffect(new MobEffectInstance(ELECTROCUTED, 20, 1, false, false));
        } else {
            if (level.getBlockEntity(pos) instanceof ElectronicDevice electronic) {
                Port<?> port = inputPort ? electronic.findInputPort(portId) : electronic.findOutputPort(portId);
                port.disconnect(level);
                if (level.getBlockEntity(pos) instanceof BlockEntity blockEntity) {
                    blockEntity.setChanged();
                }
            }

            if (character != null) {
                character.modifySkill(Skill.ELECTRICAL, 0.002f);
            }
        }
    }
}
