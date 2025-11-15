package com.site21.bittermelon.init.custom;

import com.site21.bittermelon.Bittermelon;
import com.site21.bittermelon.common.content.blocks.electronics.television.Media;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.Optional;

import static com.site21.bittermelon.init.neoforge.BitterRegistries.MEDIA_REGISTRY_KEY;


public class Medias {
    public static final DeferredRegister<Media> MEDIA = DeferredRegister.create(MEDIA_REGISTRY_KEY, Bittermelon.MOD_ID);

    public static final DeferredHolder<Media, Media> COLOR_TEST = MEDIA.register("color_test",
            () -> new Media(Bittermelon.resource("media/color_test"), Optional.empty()));

    public static final DeferredHolder<Media, Media> BLOCK_WAVE = MEDIA.register("block_wave",
            () -> new Media(Bittermelon.resource("media/block_wave"), Optional.empty()));

    public static final DeferredHolder<Media, Media> VILLAGER_NEWS = MEDIA.register("villager_news",
            () -> new Media(Bittermelon.resource("media/villager_news"), Optional.empty()));

    public static final DeferredHolder<Media, Media> MANAPHY = MEDIA.register("manaphy",
            () -> new Media(Bittermelon.resource("media/manaphy"), Optional.empty()));
}
