package com.site21.bittermelon.content.blocks.container.smallbox;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.core.Vec3i;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BarrelBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.ContainerOpenersCounter;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

import static com.site21.bittermelon.init.neoforge.BitterBlockEntities.BOX_BLOCK_ENTITY;

public class BoxBlockEntity extends BlockEntity {
    private NonNullList<ItemStack> items = NonNullList.withSize(27, ItemStack.EMPTY);
    private final ContainerOpenersCounter openersCounter = new ContainerOpenersCounter() {

        @Override
        protected void onOpen(@NotNull Level level, @NotNull BlockPos pos, @NotNull BlockState state) {
            BoxBlockEntity.this.playSound(state, SoundEvents.CHEST_OPEN);
        }

        @Override
        protected void onClose(Level level, BlockPos pos, BlockState state) {
            BoxBlockEntity.this.playSound(state, SoundEvents.CHEST_CLOSE);
        }

        @Override
        protected void openerCountChanged(Level level, BlockPos pos, BlockState state, int count, int openCount) {
        }

        @Override
        protected boolean isOwnContainer(Player player) {
            return false;
        }
    };

    public BoxBlockEntity(BlockPos pos, BlockState blockState) {
        super(BOX_BLOCK_ENTITY.get(), pos, blockState);
    }



    void playSound(BlockState state, SoundEvent sound) {
        Vec3i vec3i = state.getValue(BarrelBlock.FACING).getNormal();
        double d0 = (double)this.worldPosition.getX() + 0.5 + (double)vec3i.getX() / 2.0;
        double d1 = (double)this.worldPosition.getY() + 0.5 + (double)vec3i.getY() / 2.0;
        double d2 = (double)this.worldPosition.getZ() + 0.5 + (double)vec3i.getZ() / 2.0;
        this.level.playSound(null, d0, d1, d2, sound, SoundSource.BLOCKS, 0.5F, this.level.random.nextFloat() * 0.1F + 0.9F);
    }

    @Override
    protected void saveAdditional(@NotNull CompoundTag tag, HolderLookup.@NotNull Provider registries) {

    }

    @Override
    protected void loadAdditional(@NotNull CompoundTag tag, HolderLookup.@NotNull Provider registries) {

    }
}
