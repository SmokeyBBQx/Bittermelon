package com.site21.bittermelon.common.content.entities.implementations.scp650.networking;

import com.site21.bittermelon.Bittermelon;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector3f;

public record SetEntityPos(Vector3f pos, int entityID, float yaw) implements CustomPacketPayload {
    public static final Type<SetEntityPos> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(Bittermelon.MOD_ID, "set_entity_pos"));

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static final StreamCodec<ByteBuf, SetEntityPos> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.VECTOR3F,
            SetEntityPos::pos,
            ByteBufCodecs.INT,
            SetEntityPos::entityID,
            ByteBufCodecs.FLOAT,
            SetEntityPos::yaw,
            SetEntityPos::new
    );

    public void handle(@NotNull IPayloadContext ctx) {
        Level level = ctx.player().level();
        Entity entity = level.getEntity(entityID);
        if (entity == null) return;

        // Set both old and new position to same position to avoid tweening.
        entity.xo = pos.x;
        entity.yo = pos.y;
        entity.zo = pos.z;
        entity.xOld = pos.x;
        entity.yOld = pos.y;
        entity.zOld = pos.z;
        entity.yRotO = yaw;
        entity.setPos(pos.x, pos.y, pos.z);
        entity.setYBodyRot(yaw);
        entity.setYHeadRot(yaw);
        entity.setYRot(yaw);
    }
}
