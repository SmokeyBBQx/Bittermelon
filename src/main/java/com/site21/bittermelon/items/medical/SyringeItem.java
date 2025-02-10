package com.site21.bittermelon.items.medical;

import com.site21.bittermelon.character.Character;
import com.site21.bittermelon.character.CharacterManager;
import com.site21.bittermelon.items.base.ItemWeight;
import com.site21.bittermelon.items.containers.substance.FluidContainerItem;
import com.site21.bittermelon.medical.medicalstats.MedicalStats;
import com.site21.bittermelon.medical.simulations.Simulation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import org.jetbrains.annotations.NotNull;

public class SyringeItem extends FluidContainerItem {

    public SyringeItem(Properties properties, int width, int height, ItemWeight itemWeight, int capacity, int maxTransferRate) {
        super(properties, width, height, itemWeight, capacity, maxTransferRate);
    }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(@NotNull Level level, @NotNull Player player, @NotNull InteractionHand usedHand) {
        EntityHitResult hitResult = (EntityHitResult) player.pick(player.getAttributeValue(Attributes.ENTITY_INTERACTION_RANGE), 1.0F, false);

        if (hitResult.getEntity() instanceof LivingEntity targetEntity) {
            Character character = CharacterManager.get(level).getActiveCharacter(targetEntity);

            if (character == null) {
                return InteractionResultHolder.fail(player.getItemInHand(usedHand));
            }
            
            // TODO: Rough injection, use damage generator and get random injection simulation
            return InteractionResultHolder.success(player.getItemInHand(usedHand));
        }

        return super.use(level, player, usedHand);
    }


}
