//package com.site21.bittermelon.items.base;
//
//import net.minecraft.core.BlockPos;
//import net.minecraft.nbt.CompoundTag;
//import net.minecraft.sounds.SoundEvents;
//import net.minecraft.sounds.SoundSource;
//import net.minecraft.world.entity.Entity;
//import net.minecraft.world.entity.item.ItemEntity;
//import net.minecraft.world.item.Item;
//import net.minecraft.world.item.ItemStack;
//import net.minecraft.world.level.Level;
//import org.jetbrains.annotations.NotNull;
//
//public interface FragileItem {
//    default float getFragility() {
//        return 0.1f; // Default value, can be overridden
//    }
//
//    default Item getBreakItem() {
//        return GLASS_SHARD.get(); // Default value, can be overridden
//    }
//
//    default boolean checkForBreak(ItemStack stack, ItemEntity entity) {
//        Level level = entity.level();
//        if (!level.isClientSide && !entity.isNoGravity() && entity.onGround()) {
//            CompoundTag tag = stack.getTags();
//            if (!tag.getBoolean("hasLanded")) {
//                tag.putBoolean("hasLanded", true);
//                breakItem(entity, level);
//            }
//        }
//        return false;
//    }
//
//    default void breakItem(ItemEntity entity, Level level) {
//        float chance = level.getRandom().nextFloat();
//        if (chance > getFragility()) {
//            entity.remove(Entity.RemovalReason.DISCARDED);
//            int amount = level.getRandom().nextIntBetweenInclusive(1, 5);
//            ItemStack pieces = new ItemStack(getBreakItem(), amount);
//            level.addFreshEntity(new ItemEntity(level, entity.getX(), entity.getY(), entity.getZ(), pieces));
//            level.playSound(null, entity.getX(), entity.getY(), entity.getZ(), SoundEvents.GLASS_BREAK, SoundSource.AMBIENT, 1F, 1F);
//        }
//    }
//
//    default void hasLanded(@NotNull ItemStack stack) {
//        CompoundTag tag = stack.getOrCreateTag();
//        if (tag.getBoolean("hasLanded")) {
//            tag.putBoolean("hasLanded", false);
//        }
//    }
//
//    default boolean checkForBreakOnHit(ItemStack itemStack, Level level, BlockPos pos) {
//        float chance = level.getRandom().nextFloat();
//        if (chance > getFragility()) {
//            int amount = level.getRandom().nextIntBetweenInclusive(0, 5);
//            ItemStack pieces = new ItemStack(getBreakItem(), amount);
//            level.addFreshEntity(new ItemEntity(level, pos.getX(), pos.getY() + 1, pos.getZ(), pieces));
//            level.playSound(null, pos.getX(), pos.getY(), pos.getZ(), SoundEvents.GLASS_BREAK, SoundSource.AMBIENT, 1F, 1F);
//            return true;
//        } else {
//            return false;
//        }
//    }
//
//}
