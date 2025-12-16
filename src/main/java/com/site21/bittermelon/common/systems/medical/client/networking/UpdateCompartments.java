package com.site21.bittermelon.common.systems.medical.client.networking;

import com.site21.bittermelon.Bittermelon;
import com.site21.bittermelon.common.systems.medical.compartment.CompartmentInstance;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public record UpdateCompartments(List<CompartmentInstance> compartments) implements CustomPacketPayload {
    public static final Type<UpdateCompartments> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(Bittermelon.MOD_ID, "update_compartments"));

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static final StreamCodec<RegistryFriendlyByteBuf, UpdateCompartments> STREAM_CODEC = StreamCodec.composite(
            CompartmentInstance.STREAM_CODEC.apply(ByteBufCodecs.list()),
            UpdateCompartments::compartments,
            UpdateCompartments::new
    );

    public void handle(@NotNull IPayloadContext ctx) {
//        if (Minecraft.getInstance().screen instanceof HealthScreen screen) {
//            for (CompartmentInstance instance : compartments) {
//                screen.getMedicalStats().getCompartments().put(instance.getId(), instance);
//            }
//            screen.refresh();
//        }
    }
}
