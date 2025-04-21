package com.site21.bittermelon.init.neoforge;

import com.mojang.serialization.Codec;
import com.site21.bittermelon.Bittermelon;
import com.site21.bittermelon.content.atmosphere.data.AtmosBlockData;
import net.minecraft.core.UUIDUtil;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.function.Supplier;

public class BitterAttachmentTypes {
    public static final DeferredRegister<AttachmentType<?>> ATTACHMENT_TYPES = DeferredRegister.create(NeoForgeRegistries.ATTACHMENT_TYPES, Bittermelon.MOD_ID);

    public static final Supplier<AttachmentType<List<UUID>>> GERMS = ATTACHMENT_TYPES.register(
            "germs", () -> AttachmentType.<List<UUID>>builder(
                    () -> new ArrayList<>()).serialize(Codec.list(UUIDUtil.CODEC)).build()
    );

    public static final Supplier<AttachmentType<UUID>> ACTIVE_CHARACTER = ATTACHMENT_TYPES.register(
            "active_character", () -> AttachmentType.builder(() -> new UUID(0, 0)).serialize(UUIDUtil.CODEC).build()
    );

    public static final Supplier<AttachmentType<Integer>> STUMBLE_TICKS = ATTACHMENT_TYPES.register(
            "stumble_ticks", () -> AttachmentType.builder(() -> 0).serialize(Codec.INT).build()
    );

    public static final Supplier<AttachmentType<AtmosBlockData>> ATMOSPHERE = ATTACHMENT_TYPES.register(
            "atmosphere", () -> AttachmentType.serializable(AtmosBlockData::new).build()
    );

    public static final Supplier<AttachmentType<Integer>> ACTIVE_CHANNEL = ATTACHMENT_TYPES.register(
            "active_channel", () -> AttachmentType.builder(() -> 0).serialize(Codec.INT).build()
    );
}
