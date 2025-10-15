package com.site21.bittermelon.init.neoforge;

import com.mojang.serialization.Codec;
import com.site21.bittermelon.Bittermelon;
import com.site21.bittermelon.systems.atmosphere.data.AtmosBlockData;
import com.site21.bittermelon.content.items.scps.scp377.FortuneInstance;
import net.minecraft.core.UUIDUtil;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import java.util.List;
import java.util.UUID;
import java.util.function.Supplier;

public class BitterAttachmentTypes {
    public static final DeferredRegister<AttachmentType<?>> ATTACHMENT_TYPES = DeferredRegister.create(NeoForgeRegistries.ATTACHMENT_TYPES, Bittermelon.MOD_ID);

    public static final Supplier<AttachmentType<List<UUID>>> GERMS = ATTACHMENT_TYPES.register(
            "germs", () -> AttachmentType.builder(() -> List.<UUID>of()).serialize(Codec.list(UUIDUtil.CODEC).fieldOf("germs")).build()
    );

    public static final Supplier<AttachmentType<UUID>> ACTIVE_CHARACTER = ATTACHMENT_TYPES.register(
            "active_character", () -> AttachmentType.builder(() ->
                    new UUID(0, 0)).serialize(UUIDUtil.CODEC.fieldOf("active_character")).copyOnDeath().build()
    );

    public static final Supplier<AttachmentType<AtmosBlockData>> ATMOSPHERE = ATTACHMENT_TYPES.register(
            "atmosphere", () -> AttachmentType.builder(AtmosBlockData::new).serialize(AtmosBlockData.CODEC.fieldOf("atmosphere")).build()
    );

    public static final Supplier<AttachmentType<Integer>> ACTIVE_CHANNEL = ATTACHMENT_TYPES.register(
            "active_channel", () -> AttachmentType.builder(() -> 0).serialize(Codec.INT.fieldOf("active_channel")).copyOnDeath().build()
    );

    public static final Supplier<AttachmentType<Integer>> STEP_COUNTER = ATTACHMENT_TYPES.register(
            "step_counter", () -> AttachmentType.builder(() -> 0).serialize(Codec.INT.fieldOf("step_counter")).build()
    );

    public static final Supplier<AttachmentType<Integer>> STRESS = ATTACHMENT_TYPES.register(
            "stress", () -> AttachmentType.builder(() -> 0).serialize(Codec.INT.fieldOf("stress")).build()
    );

    public static final Supplier<AttachmentType<Integer>> FEAR = ATTACHMENT_TYPES.register(
            "fear", () -> AttachmentType.builder(() -> 0).serialize(Codec.INT.fieldOf("fear")).build()
    );

    public static final Supplier<AttachmentType<Long>> LAST_TYPING_TIME = ATTACHMENT_TYPES.register(
            "last_typing_time", () -> AttachmentType.builder(() -> 0L).build()
    );

    public static final Supplier<AttachmentType<List<FortuneInstance>>> FORTUNE_INSTANCES = ATTACHMENT_TYPES.register(
            "fortune_instances", () -> AttachmentType.builder(() -> List.<FortuneInstance>of())
                    .serialize(Codec.list(FortuneInstance.CODEC).fieldOf("fortune_instances")).build()
    );

    public static final Supplier<AttachmentType<String>> LORE_OPENING = ATTACHMENT_TYPES.register(
            "lore_opening", () -> AttachmentType.builder(() -> "").serialize(Codec.STRING.fieldOf("lore_opening")).build()
    );
}
