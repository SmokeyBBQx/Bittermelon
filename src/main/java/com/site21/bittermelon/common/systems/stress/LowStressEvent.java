package com.site21.bittermelon.common.systems.stress;

import com.site21.bittermelon.common.systems.character.Character;
import com.site21.bittermelon.common.systems.character.skills.Skill;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import static com.site21.bittermelon.common.systems.character.CharacterUtil.getCharacter;

public enum LowStressEvent {
    AMNESIA((player) -> {
        Character character = getCharacter(player);
        if (character == null) return;
        List<Skill> availableSkills = new ArrayList<>(character.getSkills().keySet());

        if (!availableSkills.isEmpty()) {
            Skill randomSkill = availableSkills.get(player.getRandom().nextInt(availableSkills.size()));
            float depletionAmount = -(0.1f + player.getRandom().nextFloat() * 0.3f);
            character.modifySkill(randomSkill, depletionAmount);

            player.displayClientMessage(
                    Component.literal("Your mind is foggy and you have trouble remembering simple things.")
                            .withStyle(ChatFormatting.ITALIC)
                            .withStyle(ChatFormatting.RED)
                            .append(Component.literal(randomSkill.getSerializedName() +
                                            " skill decreased by " + String.format("%.2f", -depletionAmount))
                                    .withStyle(ChatFormatting.GRAY)),
                    false);
        }
    }),

    BRAIN_FOG((entity) -> {

    }),

    LETHARGY((entity) -> {

    }),

    RESTLESSNESS((entity) -> {

    }),

    POOR_FOCUS((entity) -> {

    });

    public final Consumer<Player> effect;

    LowStressEvent(Consumer<Player> effect) {
        this.effect = effect;
    }
}
