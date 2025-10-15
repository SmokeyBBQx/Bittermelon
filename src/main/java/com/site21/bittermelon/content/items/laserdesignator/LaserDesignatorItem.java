package com.site21.bittermelon.content.items.laserdesignator;

import com.site21.bittermelon.content.blocks.electronics.containmentpanel.ContainmentPanelBlockEntity;
import com.site21.bittermelon.content.items.base.BaseItem;
import com.site21.bittermelon.content.items.base.ItemWeight;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import static com.site21.bittermelon.init.neoforge.BitterDataComponents.POSITION_1;
import static com.site21.bittermelon.init.neoforge.BitterDataComponents.POSITION_2;
import static com.site21.bittermelon.init.neoforge.BitterSounds.SCANNER_BEEP;

public class LaserDesignatorItem extends BaseItem {
    public LaserDesignatorItem(Properties properties, int width, int height, ItemWeight itemWeight) {
        super(properties, width, height, itemWeight);
    }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(@NotNull Level level, @NotNull Player player, @NotNull InteractionHand usedHand) {
        ItemStack heldItem = player.getItemInHand(usedHand);

        if (level.isClientSide) return InteractionResultHolder.fail(heldItem);

        BlockPos pos1 = heldItem.get(POSITION_1);
        BlockPos pos2 = heldItem.get(POSITION_2);
        if (pos1 == null || pos2 == null) {
            return InteractionResultHolder.pass(heldItem);
        }

        Vec3 lookAngle = player.getLookAngle();
        BlockPos offsetPos = getOffsetFromLookDirection(lookAngle);

        boolean isShrinking = player.isCrouching();

        if (isShrinking) {
            offsetPos = offsetPos.multiply(-1);
        }

        if (shouldUpdateFirstPosition(pos1, pos2, offsetPos) != isShrinking) {
            heldItem.set(POSITION_1.get(), pos1.offset(offsetPos.getX(), offsetPos.getY(), offsetPos.getZ()));
        } else {
            heldItem.set(POSITION_2.get(), pos2.offset(offsetPos.getX(), offsetPos.getY(), offsetPos.getZ()));
        }

        playBeepSound(player);
        return InteractionResultHolder.success(heldItem);
    }

    @Contract("_ -> new")
    private @NotNull BlockPos getOffsetFromLookDirection(@NotNull Vec3 lookAngle) {
        double absX = Math.abs(lookAngle.x);
        double absY = Math.abs(lookAngle.y);
        double absZ = Math.abs(lookAngle.z);

        if (absX > absY && absX > absZ) {
            return new BlockPos(lookAngle.x > 0 ? 1 : -1, 0, 0);
        } else if (absY > absX && absY > absZ) {
            return new BlockPos(0, lookAngle.y > 0 ? 1 : -1, 0);
        } else {
            return new BlockPos(0, 0, lookAngle.z > 0 ? 1 : -1);
        }
    }

    private boolean shouldUpdateFirstPosition(@NotNull BlockPos pos1, @NotNull BlockPos pos2, BlockPos offset) {
        return (pos1.getX() > pos2.getX() && offset.getX() > 0) ||
                (pos1.getX() < pos2.getX() && offset.getX() < 0) ||
                (pos1.getY() > pos2.getY() && offset.getY() > 0) ||
                (pos1.getY() < pos2.getY() && offset.getY() < 0) ||
                (pos1.getZ() > pos2.getZ() && offset.getZ() > 0) ||
                (pos1.getZ() < pos2.getZ() && offset.getZ() < 0);
    }

    @Override
    public @NotNull InteractionResult useOn(@NotNull UseOnContext context) {
        ItemStack usedItem = context.getItemInHand();
        Player player = context.getPlayer();
        if (player == null) return InteractionResult.FAIL;
        if (context.getLevel().isClientSide) return InteractionResult.FAIL;

        if (context.getLevel().getBlockEntity(context.getClickedPos()) instanceof ContainmentPanelBlockEntity blockEntity) {
            if (player.isCrouching()) {
                BoundingBox boundingBox = blockEntity.getBoundingBox();
                usedItem.set(POSITION_1.get(), new BlockPos(boundingBox.minX(), boundingBox.minY(), boundingBox.minZ()));
                usedItem.set(POSITION_2.get(), new BlockPos(boundingBox.maxX(), boundingBox.maxY(), boundingBox.maxZ()));
                player.level().playSound(null, player.getOnPos(), SCANNER_BEEP.get(), SoundSource.PLAYERS, 0.5f, 0.8f);
                player.sendSystemMessage(Component.literal("Bounding box copied from containment panel.").withColor(3066993));
                return InteractionResult.SUCCESS;
            }
        }

        if (player.isCrouching()) {
            context.getItemInHand().set(POSITION_2.get(), context.getClickedPos());
            player.sendSystemMessage(Component.literal("Position 2 set to " + context.getClickedPos().toShortString()).withColor(3066993));
        } else {
            context.getItemInHand().set(POSITION_1.get(), context.getClickedPos());
            player.sendSystemMessage(Component.literal("Position 1 set to " + context.getClickedPos().toShortString()).withColor(3066993));
        }

        playBeepSound(player);
        return InteractionResult.SUCCESS;
    }

    public void playBeepSound(@NotNull Player player) {
        player.level().playSound(null, player.getOnPos(), SCANNER_BEEP.get(), SoundSource.PLAYERS, 0.5f, 0.8f);
    }
}
