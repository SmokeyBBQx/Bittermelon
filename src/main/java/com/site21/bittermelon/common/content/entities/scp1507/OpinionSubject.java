package com.site21.bittermelon.common.content.entities.scp1507;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.UUIDUtil;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

import java.util.UUID;

public interface OpinionSubject {
    static OpinionSubject of(Block block) {
        return new OfBlock(block);
    }

    static OpinionSubject of(UUID character) {
        return new OfCharacter(character);
    }

    static OpinionSubject of(Item item) {
        return new OfItem(item);
    }

    static OpinionSubject of(EntityType<?> type) {
        return new OfEntityType(type);
    }

    Codec<OpinionSubject> CODEC = Codec.STRING.dispatch(
            "type",
            OpinionSubject::subjectType,
            OpinionSubject::codecFor
    );

    String subjectType();

    private static MapCodec<? extends OpinionSubject> codecFor(String type) {
        return switch (type) {
            case "block" -> OfBlock.CODEC;
            case "character" -> OfCharacter.CODEC;
            case "item" -> OfItem.CODEC;
            case "entity_type" -> OfEntityType.CODEC;
            default -> throw new IllegalArgumentException("Unknown opinion subject type: " + type);
        };
    }

    record OfBlock(Block block) implements OpinionSubject {
        static final MapCodec<OfBlock> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
                BuiltInRegistries.BLOCK.byNameCodec().fieldOf("block").forGetter(OfBlock::block)
        ).apply(instance, OfBlock::new));

        @Override
        public String subjectType() { return "block"; }
    }

    record OfCharacter(UUID id) implements OpinionSubject {
        static final MapCodec<OfCharacter> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
                UUIDUtil.CODEC.fieldOf("id").forGetter(OfCharacter::id)
        ).apply(instance, OfCharacter::new));

        @Override
        public String subjectType() { return "character"; }
    }

    record OfItem(Item item) implements OpinionSubject {
        static final MapCodec<OfItem> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
                BuiltInRegistries.ITEM.byNameCodec().fieldOf("item").forGetter(OfItem::item)
        ).apply(instance, OfItem::new));

        @Override
        public String subjectType() { return "item"; }
    }

    record OfEntityType(EntityType<?> entityType) implements OpinionSubject {
        static final MapCodec<OfEntityType> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
                BuiltInRegistries.ENTITY_TYPE.byNameCodec().fieldOf("entity_type").forGetter(OfEntityType::entityType)
        ).apply(instance, OfEntityType::new));

        @Override
        public String subjectType() { return "entity_type"; }
    }
}