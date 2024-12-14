package com.site21.bittermelon.init;

import com.mojang.serialization.Codec;
import com.site21.bittermelon.Bittermelon;
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
}
