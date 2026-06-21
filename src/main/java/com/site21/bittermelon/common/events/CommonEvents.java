package com.site21.bittermelon.common.events;

import com.site21.bittermelon.Bittermelon;
import com.site21.bittermelon.common.content.items.scps.scp377.FortuneHandler;
import com.site21.bittermelon.common.systems.atmosphere.data.AtmosLevelData;
import com.site21.bittermelon.common.systems.blockdamage.BlockDamageUtil;
import com.site21.bittermelon.common.systems.carry.CarryHandler;
import com.site21.bittermelon.common.systems.character.Character;
import com.site21.bittermelon.common.systems.character.CharacterManager;
import com.site21.bittermelon.common.systems.character.networking.SyncActiveCharacter;
import com.site21.bittermelon.common.systems.character.networking.SyncCharacters;
import com.site21.bittermelon.common.systems.fluid.substance.SubstanceFluid;
import com.site21.bittermelon.common.systems.fluid.substance.SubstanceFluidBlockEntity;
import com.site21.bittermelon.common.systems.rage.RageHandler;
import com.site21.bittermelon.common.systems.stress.StressHandler;
import com.site21.bittermelon.common.systems.substance.SubstanceMixture;
import com.site21.bittermelon.common.systems.substance.SubstanceStack;
import com.site21.bittermelon.common.systems.telecomms.intercom.IntercomManager;
import com.site21.bittermelon.common.systems.telecomms.intercom.networking.SyncIntercomList;
import com.site21.bittermelon.networking.server.SetLastTypingTime;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import net.neoforged.neoforge.event.level.block.BreakBlockEvent;
import net.neoforged.neoforge.event.tick.EntityTickEvent;
import net.neoforged.neoforge.network.PacketDistributor;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import static com.site21.bittermelon.init.custom.Anatomies.HUMAN;
import static com.site21.bittermelon.init.neoforge.BitterAttachmentTypes.*;
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

        if (entity.getExistingDataOrNull(LAST_TYPING_TIME) != null) {
            long lastTypingTime = entity.getData(LAST_TYPING_TIME);
            long timeSinceTyping = System.currentTimeMillis() - lastTypingTime;

            if (timeSinceTyping > 5000) {
                entity.removeData(LAST_TYPING_TIME);
                PacketDistributor.sendToPlayersTrackingEntityAndSelf(entity, new SetLastTypingTime(entity.getUUID(), -1));
            }
        }
    }

    public static void tickStains(Level level, LivingEntity entity) {
        BlockPos pos = entity.blockPosition();

        if (level.isClientSide()) return;

        if (entity.hasData(STAINS) && level.getGameTime() % STAIN_TICK_INTERVAL == 0) {
            SubstanceMixture stains = entity.getData(STAINS);
            stains.tickReactions(level, pos);

            for (SubstanceStack stack : stains.getSubstances()) {
                stack.getSubstance().onContact(stack, entity);
            }

            if (stains.getVolume() > 10 && level.getRandom().nextFloat() > 0.1f) {
                List<SubstanceStack> drippedSubstances = stains.spreadSubstancesByVolume(10);
                if (drip(level, pos, drippedSubstances)) {
                    stains.removeSubstances(drippedSubstances);
                    level.playSound(null, pos, SoundEvents.POINTED_DRIPSTONE_DRIP_WATER,
                            SoundSource.AMBIENT, 1.0f, 0.8f + level.getRandom().nextFloat() * 0.4f);
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
    public static void onBreakBlock(BreakBlockEvent event) {
        if (event.isCanceled()) return;
        BlockDamageUtil.clearDamage(event.getLevel(), event.getPos());
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
    public static void onEntityDeath(@NotNull LivingDeathEvent event) {
        // TODO: Save dead NPC characters so they can be restored or looked back on
        LivingEntity entity = event.getEntity();

        // Clean up NPCs
        if (!entity.level().isClientSide()) {
            if (!(entity instanceof Player)) {
                CharacterManager characterManager = CharacterManager.get(entity.level());
                List<Character> characters = characterManager.getCharactersByEntityUUID(entity.getUUID());
                List<Character> charactersToRemove = new ArrayList<>(characters);
                for (Character character : charactersToRemove) {
                    characterManager.removeCharacter(character.getId());
                }
            }
        }
    }

    @SubscribeEvent
    public void onPlayerJoin(PlayerEvent.@NotNull PlayerLoggedInEvent event) {
        if (event.getEntity() instanceof ServerPlayer player) {
            Level level = player.level();

            AtmosLevelData.get(level).syncToClient();
            PacketDistributor.sendToPlayer(player, new SyncIntercomList(IntercomManager.get(level).getIntercomIDs()));

            CharacterManager characterManager = CharacterManager.get(level);
            PacketDistributor.sendToPlayer(player, new SyncCharacters(characterManager.getCharacters()));

            Character activeCharacter = characterManager.getActiveCharacter(player);
            if (activeCharacter != null) {
                PacketDistributor.sendToPlayer(player, new SyncActiveCharacter(activeCharacter.getId()));
            }
        }
    }
}
