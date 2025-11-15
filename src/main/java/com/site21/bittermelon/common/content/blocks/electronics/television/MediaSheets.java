package com.site21.bittermelon.common.content.blocks.electronics.television;

import com.site21.bittermelon.Bittermelon;
import net.minecraft.client.resources.model.Material;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public class MediaSheets {
    public static final ResourceLocation ATLAS_LOCATION = Bittermelon.resource("textures/atlas/media.png");
    public static final ResourceLocation ATLAS_INFO_LOCATION = Bittermelon.resource("media");
    private static final Map<ResourceLocation, Material> MEDIA_MATERIALS = new HashMap<>();
    private static final Material DEFAULT_MATERIAL = new Material(ATLAS_LOCATION, Bittermelon.resource("default"));

    public static Material getMaterial(@NotNull Holder<Media> mediaHolder) {
        return MEDIA_MATERIALS.computeIfAbsent(mediaHolder.value().resource(), res -> new Material(ATLAS_LOCATION, res));
    }
}
