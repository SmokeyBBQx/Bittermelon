package com.site21.bittermelon.content.items.taser;

import com.site21.bittermelon.Bittermelon;
import com.site21.bittermelon.content.items.substance.GasContainerItem;
import com.site21.bittermelon.content.items.substance.networking.ReleasePressureUpdate;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.jetbrains.annotations.NotNull;

import static com.site21.bittermelon.init.neoforge.BitterAttachmentTypes.SHAKE_TICKS;

public record SetShakeTicks(int entityID, int shakeTicks) implements CustomPacketPayload {
    public static final Type<SetShakeTicks> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(Bittermelon.MOD_ID, "set_shake_ticks"));

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static final StreamCodec<ByteBuf, SetShakeTicks> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.INT,
            SetShakeTicks::entityID,
            ByteBufCodecs.INT,
            SetShakeTicks::shakeTicks,
            SetShakeTicks::new
    );

    public void handle(@NotNull IPayloadContext ctx) {
        if (ctx.player().level().getEntity(entityID) instanceof LivingEntity entity) {
            entity.setData(SHAKE_TICKS, shakeTicks);
        }
     }
}
