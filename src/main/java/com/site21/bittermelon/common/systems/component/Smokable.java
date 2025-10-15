package com.site21.bittermelon.common.systems.component;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

import static com.site21.bittermelon.init.neoforge.BitterDataComponents.LIT;
import static com.site21.bittermelon.init.neoforge.BitterItems.CIGARETTE_BUTT;
import static net.minecraft.sounds.SoundEvents.FIRE_AMBIENT;

public record Smokable(Holder<Item> buttItem, int smokeDuration, Holder<SoundEvent> smokeSound) {
    public static final Codec<Smokable> CODEC;
    public static final Smokable DEFAULT = new Smokable(CIGARETTE_BUTT, 30, Holder.direct(FIRE_AMBIENT));

    public boolean use(@NotNull ItemStack stack, InteractionHand hand, Player player) {
        if (stack.getOrDefault(LIT, false)) {

        }

        return false;
    }

    static {
        CODEC = RecordCodecBuilder.create(instance -> instance.group(
                Item.CODEC.fieldOf("buttItem").forGetter(Smokable::buttItem),
                Codec.INT.fieldOf("smokeDuration").forGetter(Smokable::smokeDuration),
                SoundEvent.CODEC.fieldOf("smokeSound").forGetter(Smokable::smokeSound)
        ).apply(instance, Smokable::new));
    }
}
