package com.site21.bittermelon.content.blocks.electronics.keycardprinter;

import com.site21.bittermelon.systems.character.Character;
import com.site21.bittermelon.systems.character.CharacterManager;
import com.site21.bittermelon.systems.personnel.registry.PersonnelEntry;
import com.site21.bittermelon.systems.personnel.registry.PersonnelRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.NotNull;

import static com.site21.bittermelon.init.neoforge.BitterDataComponents.ID_NUMBER;
import static com.site21.bittermelon.init.neoforge.BitterItems.KEYCARD;

public class KeycardPrinter extends Block {
    public KeycardPrinter(Properties properties) {
        super(properties);
    }

    @Override
    protected @NotNull InteractionResult useWithoutItem(@NotNull BlockState state, @NotNull Level level,
                                                        @NotNull BlockPos pos, @NotNull Player player,
                                                        @NotNull BlockHitResult hitResult) {
        if (level.isClientSide) return InteractionResult.PASS;

        Character character = CharacterManager.get(level).getActiveCharacter(player);
        if (character == null) return InteractionResult.FAIL;

        PersonnelRegistry registry = PersonnelRegistry.get(level);
        PersonnelEntry entry = registry.getEntry(character);
        ItemStack keycard = new ItemStack(KEYCARD.get());

        if (entry == null) {
            entry = new PersonnelEntry(player.getUUID(), character.getUUID(), character.getName(), "", "");
            registry.addEntry(entry);
        }

        keycard.set(ID_NUMBER, entry.getId());
        player.addItem(keycard);
        return InteractionResult.SUCCESS;
    }
}
