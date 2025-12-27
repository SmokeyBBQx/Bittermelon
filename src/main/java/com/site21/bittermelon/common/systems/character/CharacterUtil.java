package com.site21.bittermelon.common.systems.character;

import net.minecraft.world.entity.Entity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class CharacterUtil {
    public static @Nullable Character getCharacter(@NotNull Entity entity) {
        return CharacterManager.get(entity.level()).getCharacter(entity.getUUID());
    }

    public static CharacterManager getCharacterManager(@NotNull Entity entity) {
        return CharacterManager.get(entity.level());
    }
}
