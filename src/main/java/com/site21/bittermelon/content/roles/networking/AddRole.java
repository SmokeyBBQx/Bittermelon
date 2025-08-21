package com.site21.bittermelon.content.roles.networking;

import com.site21.bittermelon.Bittermelon;
import com.site21.bittermelon.content.character.Character;
import com.site21.bittermelon.content.character.CharacterManager;
import com.site21.bittermelon.content.roles.Role;
import com.site21.bittermelon.init.neoforge.BitterRegistries;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.Holder;
import net.minecraft.core.UUIDUtil;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public record AddRole(UUID characterUUID, Holder<Role> role) implements CustomPacketPayload {
    public static final Type<AddRole> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(Bittermelon.MOD_ID, "add_role"));

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static final StreamCodec<RegistryFriendlyByteBuf, AddRole> STREAM_CODEC = StreamCodec.composite(
            UUIDUtil.STREAM_CODEC,
            AddRole::characterUUID,
            ByteBufCodecs.holderRegistry(BitterRegistries.ROLE_REGISTRY_KEY),
            AddRole::role,
            AddRole::new
    );

    public void handle(@NotNull IPayloadContext ctx) {
        Character character = CharacterManager.get(ctx.player().level()).getCharacter(characterUUID);
        if (character == null) return;

        Player player = ctx.player().level().getPlayerByUUID(character.getEntityUUID());
        if (player == null) return;

        role.value().onRoleAdded(player, character);
    }
}
