package com.site21.bittermelon.datagen.property;

import com.mojang.serialization.MapCodec;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.item.properties.conditional.ConditionalItemModelProperty;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import org.jspecify.annotations.Nullable;

public record OnCooldown() implements ConditionalItemModelProperty {
    public static final MapCodec<OnCooldown> MAP_CODEC = MapCodec.unit(new OnCooldown());

    @Override
    public MapCodec<? extends ConditionalItemModelProperty> type() {
        return MAP_CODEC;
    }

    @Override
    public boolean get(ItemStack itemStack, @Nullable ClientLevel level, @Nullable LivingEntity owner, int seed, ItemDisplayContext displayContext) {
        if (!(owner instanceof Player player)) return false;
        return player.getCooldowns().isOnCooldown(itemStack);
    }
}
