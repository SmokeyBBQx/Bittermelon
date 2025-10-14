package com.site21.bittermelon.datagen;

import com.site21.bittermelon.Bittermelon;
import com.site21.bittermelon.init.neoforge.BitterItems;
import com.site21.bittermelon.init.neoforge.BitterMobEffects;
import net.minecraft.data.PackOutput;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.common.data.LanguageProvider;
import net.neoforged.neoforge.registries.DeferredHolder;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

import static com.site21.bittermelon.init.neoforge.BitterItems.*;

public class BitterLanguageProvider extends LanguageProvider {
    List<Item> customItemTranslations = new ArrayList<>();

    public BitterLanguageProvider(PackOutput output) {
        super(output, Bittermelon.MOD_ID, "en_us");
    }

    @Override
    protected void addTranslations() {
        addCustomItem(SCP_109, "SCP-109");
        addCustomItem(SCP_2398, "SCP-2398");
        addCustomItem(SCP_018, "SCP-018");
        addCustomItem(SCP_377_1, "Fortune");
        addCustomItem(SCP_377, "SCP-377");
        addCustomItem(SCP_151, "SCP-151");
        customItemTranslations.add(CHALK.get());
        customItemTranslations.add(HIGHLIGHTER.get());
        BitterItems.ITEMS.getEntries().forEach(this::addItemTranslation);
        BitterMobEffects.MOB_EFFECTS.getEntries().forEach(effect ->
                addEffect(effect, formatName(effect.getKey().location().getPath())));
    }

    private void addCustomItem(@NotNull Supplier<? extends Item> key, String name) {
        customItemTranslations.add(key.get());
        addItem(key, name);
    }

    private void addItemTranslation(@NotNull DeferredHolder<Item, ? extends Item> itemHolder) {
        if (customItemTranslations.contains(itemHolder.get())) {
            return;
        }

        String key = itemHolder.getKey().location().getPath();
        String displayName = formatName(key);
        addItem(itemHolder, displayName);
    }

    private @NotNull String formatName(@NotNull String registryName) {
        registryName = registryName.replace("_", " ");

        StringBuilder result = new StringBuilder();
        boolean capitalizeNext = true;

        for (char c : registryName.toCharArray()) {
            if (Character.isWhitespace(c)) {
                capitalizeNext = true;
                result.append(c);
            } else if (capitalizeNext) {
                result.append(Character.toUpperCase(c));
                capitalizeNext = false;
            } else {
                result.append(c);
            }
        }

        return result.toString();
    }
}
