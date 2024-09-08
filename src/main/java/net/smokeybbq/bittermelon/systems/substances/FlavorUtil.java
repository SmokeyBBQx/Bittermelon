package net.smokeybbq.bittermelon.systems.substances;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.HoverEvent;
import net.minecraft.network.chat.MutableComponent;

import java.util.Map;
import java.util.stream.Collectors;

public class FlavorUtil {
    public static Component getFlavorMessageComponent(Map<Substance, Integer> substances, int totalAmount) {
            if (totalAmount == 0 || substances.isEmpty()) {
                return Component.literal("No discernible flavor.");
            }

            if (substances.size() == 1) {
                Map.Entry<Substance, Integer> entry = substances.entrySet().iterator().next();
                Substance substance = entry.getKey();

                String flavorDescription = "Tastes " + substance.getFlavor() + ".";

                return Component.literal(flavorDescription).withStyle(ChatFormatting.GREEN);
            }

            MutableComponent mainComponent = Component.literal("Tastes like...").withStyle(ChatFormatting.GREEN);

            String hoverText = substances.entrySet().stream()
                    .map(entry -> {
                        Substance substance = entry.getKey();
                        int amount = entry.getValue();
                        float percentageAmount = (float) amount / totalAmount * 100;
                        String flavorDescription;

                        if (percentageAmount <= 35) {
                            flavorDescription = "Has a faint hint of " + substance.getFlavor() + " tones.";
                        } else if (percentageAmount <= 65) {
                            flavorDescription = "Tastes like a noticeable blend of " + substance.getFlavor() + " notes.";
                        } else {
                            flavorDescription = "Tastes strongly " + substance.getFlavor() + ".";
                        }

                        return flavorDescription;
                    })
                    .collect(Collectors.joining("\n"));

            return mainComponent.setStyle(mainComponent.getStyle().withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, Component.literal(hoverText))));
    }
}
