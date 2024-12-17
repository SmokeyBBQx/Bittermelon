package com.site21.bittermelon.entities.scps.SCP939.behavior;

import com.site21.bittermelon.medical.compartments.CompartmentType;
import com.site21.bittermelon.medical.damage.DamageGenerator;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.EnumSet;
import java.util.List;
import java.util.Random;
import java.util.function.Supplier;

public record AttackTemplate(List<String> messageTemplates, EnumSet<CompartmentType> targets, Supplier<DamageGenerator> damageSequenceSupplier) {
    private static final Random random = new Random();

    public @NotNull String getFormattedMessage(String attacker, String victim, String targetBodyPart) {
        String template = messageTemplates.get(random.nextInt(messageTemplates.size()));
        return String.format(template, attacker, victim, targetBodyPart);
    }

    @Contract("_, _, _ -> new")
    public static @NotNull AttackTemplate of(
            EnumSet<CompartmentType> targets,
            Supplier<DamageGenerator> damageSequence,
            String... messages
    ) {
        return new AttackTemplate(Arrays.asList(messages), targets, damageSequence);
    }
}
