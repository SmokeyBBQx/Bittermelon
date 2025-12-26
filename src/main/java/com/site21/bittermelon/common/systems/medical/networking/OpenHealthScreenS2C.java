package com.site21.bittermelon.common.systems.medical.networking;

import com.site21.bittermelon.Bittermelon;
import com.site21.bittermelon.client.gui.ScreenHandler;
import com.site21.bittermelon.common.systems.character.Character;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.jetbrains.annotations.NotNull;

public record OpenHealthScreenS2C(Character character, ItemStack stack) implements CustomPacketPayload {
    public static final Type<OpenHealthScreenS2C> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(Bittermelon.MOD_ID, "open_health_screen_s2c"));

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static final StreamCodec<RegistryFriendlyByteBuf, OpenHealthScreenS2C> STREAM_CODEC = StreamCodec.composite(
            Character.STREAM_CODEC,
            OpenHealthScreenS2C::character,
            ItemStack.OPTIONAL_STREAM_CODEC,
            OpenHealthScreenS2C::stack,
            OpenHealthScreenS2C::new
    );

    public void handle(@NotNull IPayloadContext ctx) {
        Player player = ctx.player();
        if (character != null) {
            ScreenHandler.displayHealthScreen(character, player, stack);
        }
    }
}
