package com.site21.bittermelon.common.systems.character;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public class CharacterUtil {
    public static @Nullable Character getCharacter(@NotNull Entity entity) {
        return CharacterManager.get(entity.level()).getCharacter(entity.getUUID());
    }

    public static @Nullable Character getCharacter(@NotNull Level level, UUID characterUUID) {
        return CharacterManager.get(level).getCharacter(characterUUID);
    }

    public static CharacterManager getCharacterManager(@NotNull Entity entity) {
        return CharacterManager.get(entity.level());
    }
}
