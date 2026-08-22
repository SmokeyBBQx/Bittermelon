package com.site21.bittermelon.common.content.entities.scp939.lure;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.site21.bittermelon.common.systems.character.Character;
import com.site21.bittermelon.common.systems.character.CharacterUtil;
import com.site21.bittermelon.util.LocalMessageUtil;
import net.minecraft.core.UUIDUtil;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class LureSystem {
    public static final Codec<LureSystem> CODEC = RecordCodecBuilder.create(
            instance -> instance.group(
                    Codec.list(UUIDUtil.CODEC)
                            .fieldOf("characters")
                            .forGetter(LureSystem::getCharacters)
            ).apply(instance, LureSystem::new)
    );

    private static final Map<LureType, List<LurePool>> pools;
    private List<UUID> characters;
    private @Nullable LureScene activeScene;
    private long lastLure = 0;
    private int interval = 0;

    public LureSystem(List<UUID> characters) {
        this.characters = characters;
    }

    public LureSystem() {}

    public LureScene createScene(Entity entity, LureType type) {
        RandomSource random = entity.getRandom();
        List<LurePool> poolList = pools.get(type);
        LurePool pool = poolList.get(random.nextInt(poolList.size()));
        UUID[] characterIds = new UUID[pool.dialogue().length];
        for (int i = 0; i < pool.dialogue().length; i++) {
            characterIds[i] = characters.get(random.nextInt(characters.size()));
        }
        return new LureScene(characterIds, pool);
    }

    public void attemptLure(Entity entity) {
        if (activeScene == null) return;
        Level level = entity.level();
        LurePool pool = activeScene.pool();
        if (level.getGameTime() - lastLure < interval) return;

        RandomSource random = entity.getRandom();
        if (random.nextFloat() < 0.1 && pool.sounds().length > 0) {
            SoundEvent[] sounds = pool.sounds();
            SoundEvent sound = sounds[random.nextInt(sounds.length)];
            entity.playSound(sound);
        } else {
            int i = random.nextInt(activeScene.characters().length);
            Character character = CharacterUtil.getCharacter(level, activeScene.characters()[i]);
            if (character == null) {
                activeScene = null;
                return;
            }

            String[] dialogue = pool.dialogue()[i];
            Component message = Component.literal(character.getName() + " " + dialogue[random.nextInt(dialogue.length)]);
            LocalMessageUtil.sendLocalMessage(entity, 16, message);
        }

        lastLure = level.getGameTime();
        interval = pool.interval() + random.nextInt(pool.additionalRandomInterval());
    }

    public List<UUID> getCharacters() {
        return characters;
    }

    static {
        pools = new HashMap<>();
        pools.put(LureType.GENERIC, List.of(
                new LurePool(
                        new String[][] {
                                {"Hello there!", "How are you doing?", "Nice to meet you!"},
                                {"I hope you're having a good day.", "Stay safe out there!", "Take care!"}
                        },
                        new SoundEvent[] {
                        },
                        100,
                        50
                )
        ));
    }
}
