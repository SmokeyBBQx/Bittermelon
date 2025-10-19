package com.site21.bittermelon.common.systems.component.temperature;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

import static com.site21.bittermelon.init.neoforge.BitterDataComponents.BURN_TIME;
import static com.site21.bittermelon.init.neoforge.BitterDataComponents.TEMPERATURE;

public record HeatBehavior(float meltingPoint, float flashPoint, float ignitionPoint, float burnSeconds,
                           Holder<Item> residueItem) {
    public static final Codec<HeatBehavior> CODEC;
    public static final StreamCodec<RegistryFriendlyByteBuf, HeatBehavior> STREAM_CODEC;
    public static final HeatBehavior DEFAULT = new HeatBehavior(Float.MAX_VALUE, 473f, 506f, 5, Items.COAL.builtInRegistryHolder());
    public static final float harmfulTemperature = 317f;

    public void inventoryTick(@NotNull ItemStack stack, @NotNull ServerLevel level, @NotNull Entity entity, @Nullable EquipmentSlot slot) {
        Long burnTime = stack.get(BURN_TIME);
        float temperature = stack.getOrDefault(TEMPERATURE, 273f);
        long gameTime = level.getGameTime();

        if (temperature > 273 && gameTime % 100 == 0) {
            stack.set(TEMPERATURE, temperature - 5);
        }

        if (temperature > harmfulTemperature && gameTime % 20 == 0) {
            if (slot != null && slot.isArmor() && burnTime != null) {
                entity.setRemainingFireTicks(entity.getRemainingFireTicks() + 100);
            } else {
                entity.hurtServer(level, entity.damageSources().onFire(), temperature / 1000);
            }
        }

        if (burnTime != null) {
            handleBurning(stack, level, entity, burnTime);
            if (entity instanceof Player player && slot != null) {
                stack.hurtAndConvertOnBreak(1, residueItem.value(), player, slot);
            }
            return;
        }

        tryIgnite(stack, level, entity, temperature);
    }

    public void onEntityItemUpdate(@NotNull ItemStack stack, @NotNull ItemEntity entity, @NotNull Level level) {
        if (level.isClientSide) return;

        Long burnTime = stack.get(BURN_TIME);
        if (burnTime != null) {
            handleBurning(stack, level, entity, burnTime);
            entity.setRemainingFireTicks(100);

            return;
        }

        float temperature = stack.getOrDefault(TEMPERATURE, 273f);

        long gameTime = level.getGameTime();

        if (temperature > 273 && gameTime % 100 == 0) {
            stack.set(TEMPERATURE, temperature - 5);
        }

        tryIgnite(stack, level, entity, temperature);
    }

    private void handleBurning(@NotNull ItemStack stack, @NotNull Level level, @NotNull Entity entity, long burnTime) {
        if (entity.isInWaterOrRain()) {
            stack.remove(BURN_TIME);
            stack.set(TEMPERATURE, 273f);
            level.playSound(null, entity.getOnPos(), SoundEvents.GENERIC_EXTINGUISH_FIRE, entity.getSoundSource(), 0.1f, 1f);
            return;
        }

        if (burnTime <= level.getGameTime()) {
            stack.shrink(1);
            stack.set(BURN_TIME, (long) (level.getGameTime() + burnSeconds * 20));

            if (residueItem != null) {
                addResidue(level, entity);
                level.playSound(null, entity.getOnPos(), SoundEvents.GENERIC_BURN, entity.getSoundSource(), 0.5f, 1f);
            }
        }
    }

    private void addResidue(@NotNull Level level, @NotNull Entity entity) {
        ItemStack residue = residueItem.value().getDefaultInstance();
        if (entity instanceof Player player) {
            player.addItem(residue);
        } else if (entity instanceof ItemEntity) {
            level.addFreshEntity(new ItemEntity(level, entity.getX(), entity.getY(), entity.getZ(), residue));
        }
    }

    private void tryIgnite(@NotNull ItemStack stack, @NotNull Level level, @NotNull Entity entity, float temperature) {
        if (temperature >= ignitionPoint || (temperature >= flashPoint && canIgnite(entity))) {
            stack.set(BURN_TIME, level.getGameTime() + (long) (burnSeconds * 20));
        } else if (canIgnite(entity) && level.getGameTime() % 20 == 0) {
            stack.set(TEMPERATURE, temperature + 5);
        }
    }

    public boolean canIgnite(@NotNull Entity entity) {
        if (entity.isOnFire()) return true;
        if (entity.getInBlockState().is(Blocks.FIRE)) return true;
        if (entity instanceof Player player) {
            for (ItemStack itemStack : player.getInventory()) {
                if (itemStack.has(BURN_TIME)) return true;
            }
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(meltingPoint, flashPoint, ignitionPoint, burnSeconds, residueItem);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        } else {
            return obj instanceof HeatBehavior(
                    float meltingPoint1, float flashPoint1, float ignitionPoint1, float burnDuration1,
                    Holder<Item> residueItem1
            ) &&
                    this.meltingPoint == meltingPoint1 &&
                    this.flashPoint == flashPoint1 &&
                    this.ignitionPoint == ignitionPoint1 &&
                    this.burnSeconds == burnDuration1 &&
                    Objects.equals(this.residueItem, residueItem1);
        }
    }

    static {
        CODEC = RecordCodecBuilder.create(instance -> instance.group(
                Codec.FLOAT.optionalFieldOf("melting_point", Float.MAX_VALUE).forGetter(HeatBehavior::meltingPoint),
                Codec.FLOAT.optionalFieldOf("flash_point", 473.0f).forGetter(HeatBehavior::flashPoint),
                Codec.FLOAT.optionalFieldOf("ignition_point", 506.0f).forGetter(HeatBehavior::ignitionPoint),
                Codec.FLOAT.optionalFieldOf("burn_duration", 5.0f).forGetter(HeatBehavior::burnSeconds),
                Item.CODEC.optionalFieldOf("residue_item", Items.AIR.builtInRegistryHolder()).forGetter(HeatBehavior::residueItem)
        ).apply(instance, HeatBehavior::new));

        STREAM_CODEC = StreamCodec.composite(
                ByteBufCodecs.FLOAT,
                HeatBehavior::meltingPoint,
                ByteBufCodecs.FLOAT,
                HeatBehavior::flashPoint,
                ByteBufCodecs.FLOAT,
                HeatBehavior::ignitionPoint,
                ByteBufCodecs.FLOAT,
                HeatBehavior::burnSeconds,
                Item.STREAM_CODEC,
                HeatBehavior::residueItem,
                HeatBehavior::new
        );
    }
}
