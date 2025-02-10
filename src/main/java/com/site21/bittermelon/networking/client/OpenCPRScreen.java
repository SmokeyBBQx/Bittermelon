package com.site21.bittermelon.networking.client;

import com.site21.bittermelon.Bittermelon;
import com.site21.bittermelon.character.Character;
import com.site21.bittermelon.character.CharacterManager;
import com.site21.bittermelon.medical.client.gui.minigame.CPRScreen;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.core.UUIDUtil;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public record OpenCPRScreen(UUID targetCharacterUUID) implements CustomPacketPayload {
    public static final Type<OpenCPRScreen> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(Bittermelon.MOD_ID, "open_cpr_screen"));

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static final StreamCodec<ByteBuf, OpenCPRScreen> STREAM_CODEC = StreamCodec.composite(
            UUIDUtil.STREAM_CODEC,
            OpenCPRScreen::targetCharacterUUID,
            OpenCPRScreen::new
    );

    public void handle(IPayloadContext ctx) {
//        Character character = CharacterManager.get(Minecraft.getInstance().level).getActiveCharacter(targetCharacterUUID);
//        if (character != null) {
//            Minecraft.getInstance().setScreen(new CPRScreen(character));
//        }
    }
}
