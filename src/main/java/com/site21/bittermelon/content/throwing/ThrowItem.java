package com.site21.bittermelon.content.throwing;

import com.site21.bittermelon.Bittermelon;
import com.site21.bittermelon.content.character.Character;
import com.site21.bittermelon.content.character.CharacterManager;
import com.site21.bittermelon.content.entities.miscellaneous.ThrownItemProjectile;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.UUIDUtil;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

import static com.site21.bittermelon.init.neoforge.BitterDataComponents.ENERGY_LOSS_ON_BOUNCE;
import static com.site21.bittermelon.init.neoforge.BitterDataComponents.MAX_BOUNCES;
import static com.site21.bittermelon.util.LocalMessageHelper.sendLocalMessage;

public record ThrowItem(UUID playerUUID) implements CustomPacketPayload {
    public static final Type<ThrowItem> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(Bittermelon.MOD_ID, "throw_item"));

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static final StreamCodec<ByteBuf, ThrowItem> STREAM_CODEC = StreamCodec.composite(
            UUIDUtil.STREAM_CODEC,
            ThrowItem::playerUUID,
            ThrowItem::new
    );

    public void handle(@NotNull IPayloadContext ctx) {
        Level level = ctx.player().level();
        Player player = level.getPlayerByUUID(playerUUID);

        if (player == null) return;
        ItemStack heldItem = player.getMainHandItem();
        level.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.SNOWBALL_THROW,
                SoundSource.NEUTRAL, 0.5F, 0.4F / (level.getRandom().nextFloat() * 0.4F + 0.8F));

        if (!heldItem.isEmpty()) {
            ThrownItemProjectile projectile = new ThrownItemProjectile(level, player, heldItem.copy(),
                    heldItem.getOrDefault(ENERGY_LOSS_ON_BOUNCE, 0.7f),
                    heldItem.getOrDefault(MAX_BOUNCES, 50));


            projectile.setPos(player.getX(), player.getEyeY() - 0.1, player.getZ());
            projectile.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0F, 1F, 1.0F);
            player.level().addFreshEntity(projectile);

            Character character = CharacterManager.get(level).getActiveCharacter(player);
            if (character != null) {
                Component component = Component.literal(character.getName() + " throws " + heldItem.getHoverName().getString().toLowerCase() + ".")
                        .setStyle(Style.EMPTY.withColor(character.getEmoteColor()));
                sendLocalMessage(player, 10, component);
            }
            heldItem.shrink(1);
        }
    }
}
