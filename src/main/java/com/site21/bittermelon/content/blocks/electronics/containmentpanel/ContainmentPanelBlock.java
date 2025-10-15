package com.site21.bittermelon.content.blocks.electronics.containmentpanel;

import com.site21.bittermelon.content.blocks.base.IndentedSmallBlock;
import com.site21.bittermelon.content.blocks.electronics.containmentpanel.networking.OpenContainmentPanelScreen;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.phys.BlockHitResult;
import net.neoforged.neoforge.network.PacketDistributor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static com.site21.bittermelon.init.neoforge.BitterDataComponents.POSITION_1;
import static com.site21.bittermelon.init.neoforge.BitterDataComponents.POSITION_2;
import static com.site21.bittermelon.init.neoforge.BitterSounds.SCANNER_BEEP;

public class ContainmentPanelBlock extends IndentedSmallBlock implements EntityBlock {
    public static final BooleanProperty ON = BooleanProperty.create("on");

    public ContainmentPanelBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.defaultBlockState()
                .setValue(ON, false)
                .setValue(FACING, Direction.NORTH));
    }

    @Override
    protected @NotNull InteractionResult useWithoutItem(@NotNull BlockState state, @NotNull Level level, @NotNull BlockPos pos, @NotNull Player player, @NotNull BlockHitResult hitResult) {
        BlockState newState = state.setValue(ON, true);
        level.setBlock(pos, newState, 3);
        if (!level.isClientSide) {
            if (player instanceof ServerPlayer serverPlayer)
                PacketDistributor.sendToPlayer(serverPlayer, new OpenContainmentPanelScreen(pos));
        }

        return InteractionResult.PASS;
    }

    protected @NotNull InteractionResult useItemOn(@NotNull ItemStack stack, @NotNull BlockState state, @NotNull Level level, @NotNull BlockPos pos, @NotNull Player player, @NotNull InteractionHand hand, @NotNull BlockHitResult hitResult) {
        if (level.getBlockEntity(pos) instanceof ContainmentPanelBlockEntity blockEntity) {
            BlockPos pos1 = stack.get(POSITION_1.get());
            BlockPos pos2 = stack.get(POSITION_2.get());

            if (pos1 == null || pos2 == null) return InteractionResult.TRY_WITH_EMPTY_HAND;

            BoundingBox boundingBox = BoundingBox.fromCorners(new Vec3i(pos1.getX(), pos1.getY(), pos1.getZ()), new Vec3i(pos2.getX(), pos2.getY(), pos2.getZ()));
            blockEntity.setBoundingBox(boundingBox);

            player.level().playSound(null, player.getOnPos(), SCANNER_BEEP.get(), SoundSource.PLAYERS, 0.5f, 0.8f);
        }

        return InteractionResult.SUCCESS;
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(@NotNull BlockPos blockPos, @NotNull BlockState blockState) {
        return new ContainmentPanelBlockEntity(blockPos, blockState);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(@NotNull Level level, @NotNull BlockState state, @NotNull BlockEntityType<T> blockEntityType) {
        return level.isClientSide() ? null : (level0, state0, blockEntityType0, blockEntity) -> ((ContainmentPanelBlockEntity) blockEntity).tick();
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.@NotNull Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(ON);
    }
}
