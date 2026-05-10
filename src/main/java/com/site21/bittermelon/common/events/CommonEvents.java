package com.site21.bittermelon.common.events;

import com.site21.bittermelon.Bittermelon;
import com.site21.bittermelon.common.content.items.scps.scp377.FortuneHandler;
import com.site21.bittermelon.common.systems.blockdamage.BlockDamageUtil;
import com.site21.bittermelon.common.systems.carry.CarryHandler;
import com.site21.bittermelon.common.systems.character.CharacterManager;
import com.site21.bittermelon.common.systems.fluid.substance.SubstanceFluid;
import com.site21.bittermelon.common.systems.fluid.substance.SubstanceFluidBlockEntity;
import com.site21.bittermelon.common.systems.rage.RageHandler;
import com.site21.bittermelon.common.systems.stress.StressHandler;
import com.site21.bittermelon.common.systems.substance.SubstanceMixture;
import com.site21.bittermelon.common.systems.substance.SubstanceStack;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.phys.Vec3;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.damagesource.DamageContainer;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;
import net.neoforged.neoforge.event.entity.living.LivingDrownEvent;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import net.neoforged.neoforge.event.level.BlockEvent;
import net.neoforged.neoforge.event.tick.EntityTickEvent;
import org.jetbrains.annotations.NotNull;

import java.util.List;

import static com.site21.bittermelon.init.custom.Anatomies.HUMAN;
import static com.site21.bittermelon.init.neoforge.BitterAttachmentTypes.MEDICAL_STATS;
import static com.site21.bittermelon.init.neoforge.BitterAttachmentTypes.STAINS;
import static com.site21.bittermelon.init.neoforge.BitterFluids.SUBSTANCE_FLUID;

@EventBusSubscriber(modid = Bittermelon.MOD_ID)
public class CommonEvents {
    private static final int STAIN_TICK_INTERVAL = 20;

    @SubscribeEvent
    public static void onEntityTick(EntityTickEvent.@NotNull Post event) {
        Entity entity = event.getEntity();
        Level level = entity.level();
        FortuneHandler.onEntityTick(entity);

        if (entity instanceof LivingEntity livingEntity) {
            CarryHandler.tickCarrying(livingEntity);
            tickStains(level, livingEntity);
        }

        if (entity instanceof Player player) {
            StressHandler.tickStress(level, player);
            if (!player.hasData(MEDICAL_STATS)) {
                player.setData(MEDICAL_STATS, HUMAN.get().toInstance(player));
            } else {
                player.getData(MEDICAL_STATS).tick(player);
            }

            RageHandler.tick(level, player);
        }
    }

    public static void tickStains(Level level, LivingEntity entity) {
        BlockPos pos = entity.blockPosition();

        if (level.isClientSide) return;

        if (entity.hasData(STAINS) && level.getGameTime() % STAIN_TICK_INTERVAL == 0) {
            SubstanceMixture stains = entity.getData(STAINS);
            stains.tickReactions(level, pos);

            for (SubstanceStack stack : stains.getSubstances()) {
                stack.getSubstance().onContact(stack, entity);
            }

            if (stains.getVolume() > 10 && level.random.nextFloat() > 0.1f) {
                List<SubstanceStack> drippedSubstances = stains.spreadSubstancesByVolume(10);
                if (drip(level, pos, drippedSubstances)) {
                    stains.removeSubstances(drippedSubstances);
                    level.playSound(null, pos, SoundEvents.POINTED_DRIPSTONE_DRIP_WATER,
                            SoundSource.AMBIENT, 1.0f, 0.8f + level.random.nextFloat() * 0.4f);
                }
            }
        }
    }

    public static boolean drip(@NotNull Level level, BlockPos pos, List<SubstanceStack> substances) {
        SubstanceFluid fluid = SUBSTANCE_FLUID.get();

        if (level.getBlockState(pos).canBeReplaced() && level.getFluidState(pos).isEmpty()) {
            level.setBlock(pos, fluid.defaultFluidState().createLegacyBlock(), Block.UPDATE_ALL);
        }

        if (level.getBlockEntity(pos) instanceof SubstanceFluidBlockEntity spreadBE) {
            spreadBE.transferSubstances(substances);
            return true;
        }

        return false;
    }

    @SubscribeEvent
    public static void onBreakBlock(BlockEvent.@NotNull BreakEvent event) {
        BlockDamageUtil.clearDamage(event.getLevel(), event.getPos());

        // TODO: Reset damage if the new block state is a different block
    }

    @SubscribeEvent
    public static void onEntityInteract(PlayerInteractEvent.@NotNull EntityInteract event) {
        if (CarryHandler.playerPickUpEntity(event.getEntity(), event.getTarget())) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public static void onRightClickBlock(PlayerInteractEvent.@NotNull RightClickBlock event) {
        if (CarryHandler.placeEntity(event.getEntity(), event.getPos(), event.getHitVec())) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public static void onLivingDrown(@NotNull LivingDrownEvent event) {
        LivingEntity entity = event.getEntity();

        if (CharacterManager.get(entity.level()).getActiveCharacter(entity) != null) {
            event.setCanceled(true);
        }
    }

    public static void OnEntityHurt(LivingDamageEvent.Pre event) {
        DamageContainer container = event.getContainer();
        DamageSource source = container.getSource();
        Vec3 position = source.getSourcePosition();

    }
}
