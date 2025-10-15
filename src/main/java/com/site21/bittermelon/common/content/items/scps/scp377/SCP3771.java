package com.site21.bittermelon.common.content.items.scps.scp377;

import com.site21.bittermelon.common.content.items.scps.scp377.networking.OpenSCP3771Screen;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.network.PacketDistributor;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import static com.site21.bittermelon.init.neoforge.BitterAttachmentTypes.FORTUNE_INSTANCES;
import static com.site21.bittermelon.init.neoforge.BitterDataComponents.FORTUNE;
import static com.site21.bittermelon.init.neoforge.BitterDataComponents.FORTUNE_READ;

public class SCP3771 extends Item {
    public SCP3771(Properties properties) {
        super(properties);
    }

    @Override
    public @NotNull InteractionResult use(@NotNull Level level, @NotNull Player player, @NotNull InteractionHand usedHand) {
        ItemStack stack = player.getItemInHand(usedHand);

        if (level.isClientSide) return InteractionResult.PASS;

        if (!stack.getOrDefault(FORTUNE_READ, false)) {
            stack.set(FORTUNE_READ, true);
            List<FortuneInstance> fortuneInstances = new ArrayList<>(player.getExistingData(FORTUNE_INSTANCES).orElse(List.of()));
            fortuneInstances.add(new FortuneInstance(stack.getOrDefault(FORTUNE, Fortune.values()[0]), level.getGameTime()));
            player.setData(FORTUNE_INSTANCES, fortuneInstances);
        }

        if (player instanceof ServerPlayer serverPlayer) {
            PacketDistributor.sendToPlayer(serverPlayer, new OpenSCP3771Screen(stack.getOrDefault(FORTUNE, Fortune.values()[0])));
        }

        return InteractionResult.SUCCESS;
    }
}
