package com.site21.bittermelon.content.medical.client.screen;

import com.site21.bittermelon.Bittermelon;
import com.site21.bittermelon.ClientHandler;
import com.site21.bittermelon.content.character.Character;
import com.site21.bittermelon.content.character.CharacterManager;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.core.UUIDUtil;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public record OpenHealthScreenS2C(UUID characterID, ItemStack stack) implements CustomPacketPayload {
    public static final Type<OpenHealthScreenS2C> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(Bittermelon.MOD_ID, "open_health_screen_s2c"));

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static final StreamCodec<RegistryFriendlyByteBuf, OpenHealthScreenS2C> STREAM_CODEC = StreamCodec.composite(
            UUIDUtil.STREAM_CODEC,
            OpenHealthScreenS2C::characterID,
            ItemStack.OPTIONAL_STREAM_CODEC,
            OpenHealthScreenS2C::stack,
            OpenHealthScreenS2C::new
    );

    public void handle(@NotNull IPayloadContext ctx) {
        Player player = ctx.player();
        Character character = CharacterManager.get(player.level()).getCharacter(characterID);
        if (character != null) {
            ClientHandler.displayHealthScreen(character, player, stack);
        }
    }
}
