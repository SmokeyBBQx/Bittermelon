package com.site21.bittermelon.common.content.blocks.electronics.television;

import com.site21.bittermelon.Bittermelon;
import net.minecraft.client.resources.model.sprite.SpriteId;
import net.minecraft.core.Holder;
import net.minecraft.resources.Identifier;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public class MediaSheets {
    public static final Identifier ATLAS_LOCATION = Bittermelon.identifier("textures/atlas/media.png");
    public static final Identifier ATLAS_INFO_LOCATION = Bittermelon.identifier("media");
    private static final Map<Identifier, SpriteId> MEDIA_MATERIALS = new HashMap<>();
    private static final SpriteId DEFAULT_MATERIAL = new SpriteId(ATLAS_LOCATION, Bittermelon.identifier("default"));

    public static SpriteId getMaterial(@NotNull Holder<Media> mediaHolder) {
        return MEDIA_MATERIALS.computeIfAbsent(mediaHolder.value().resource(), res -> new SpriteId(ATLAS_LOCATION, res));
    }
}
