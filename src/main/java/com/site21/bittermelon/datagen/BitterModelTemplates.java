package com.site21.bittermelon.datagen;

import com.site21.bittermelon.Bittermelon;
import net.minecraft.client.data.models.model.ModelTemplate;
import net.minecraft.client.data.models.model.TextureSlot;
import net.minecraft.resources.ResourceLocation;

import java.util.Optional;

public class BitterModelTemplates {
    public static final ModelTemplate SLIDING_DOOR_BOTTOM_LEFT = new ModelTemplate(
            Optional.of(ResourceLocation.fromNamespaceAndPath(Bittermelon.MOD_ID, "block/sliding_door_bottom_left")),
            Optional.of("_bottom_left"),
            TextureSlot.TOP, TextureSlot.BOTTOM
    );

    public static final ModelTemplate SLIDING_DOOR_BOTTOM_RIGHT = new ModelTemplate(
            Optional.of(ResourceLocation.fromNamespaceAndPath(Bittermelon.MOD_ID, "block/sliding_door_bottom_right")),
            Optional.of("_bottom_right"),
            TextureSlot.TOP, TextureSlot.BOTTOM
    );

    public static final ModelTemplate SLIDING_DOOR_TOP_LEFT = new ModelTemplate(
            Optional.of(ResourceLocation.fromNamespaceAndPath(Bittermelon.MOD_ID, "block/sliding_door_top_left")),
            Optional.of("_top_left"),
            TextureSlot.TOP, TextureSlot.BOTTOM
    );

    public static final ModelTemplate SLIDING_DOOR_TOP_RIGHT = new ModelTemplate(
            Optional.of(ResourceLocation.fromNamespaceAndPath(Bittermelon.MOD_ID, "block/sliding_door_top_right")),
            Optional.of("_top_right"),
            TextureSlot.TOP, TextureSlot.BOTTOM
    );

    public static final ModelTemplate BUTTON_LIKE = new ModelTemplate(
            Optional.of(ResourceLocation.fromNamespaceAndPath(Bittermelon.MOD_ID, "block/button")),
            Optional.of(""),
            TextureSlot.TEXTURE
    );

    public static final ModelTemplate SMALL_POSTER_LEFT = new ModelTemplate(
            Optional.of(ResourceLocation.fromNamespaceAndPath(Bittermelon.MOD_ID, "block/small_poster_left")),
            Optional.of("_left"),
            TextureSlot.TEXTURE
    );

    public static final ModelTemplate SMALL_POSTER_RIGHT = new ModelTemplate(
            Optional.of(ResourceLocation.fromNamespaceAndPath(Bittermelon.MOD_ID, "block/small_poster_right")),
            Optional.of("_right"),
            TextureSlot.TEXTURE
    );

    public static final ModelTemplate PAINTING_SIDE = new ModelTemplate(
            Optional.of(ResourceLocation.fromNamespaceAndPath(Bittermelon.MOD_ID, "block/painting_side")),
            Optional.of("_side"),
            TextureSlot.TEXTURE
    );

    public static final ModelTemplate PAINTING_TOP = new ModelTemplate(
            Optional.of(ResourceLocation.fromNamespaceAndPath(Bittermelon.MOD_ID, "block/painting_top")),
            Optional.of("_top"),
            TextureSlot.TEXTURE
    );

    public static final ModelTemplate PAINTING_BOTTOM = new ModelTemplate(
            Optional.of(ResourceLocation.fromNamespaceAndPath(Bittermelon.MOD_ID, "block/painting_bottom")),
            Optional.of("_bottom"),
            TextureSlot.TEXTURE
    );

    public static final ModelTemplate TRAPDOOR_SHAPED_SIDE = new ModelTemplate(
            Optional.of(ResourceLocation.withDefaultNamespace("block/template_orientable_trapdoor_open")),
            Optional.of("_side"),
            TextureSlot.TEXTURE
    );

    public static final ModelTemplate TRAPDOOR_SHAPED_TOP = new ModelTemplate(
            Optional.of(ResourceLocation.withDefaultNamespace("block/template_orientable_trapdoor_top")),
            Optional.of("_top"),
            TextureSlot.TEXTURE
    );

    public static final ModelTemplate TRAPDOOR_SHAPED_BOTTOM = new ModelTemplate(
            Optional.of(ResourceLocation.withDefaultNamespace("block/template_orientable_trapdoor_bottom")),
            Optional.of("_bottom"),
            TextureSlot.TEXTURE
    );

    public static final ModelTemplate SMOKABLE = new ModelTemplate(
            Optional.of(ResourceLocation.fromNamespaceAndPath(Bittermelon.MOD_ID, "item/smokable")),
            Optional.empty(),
            TextureSlot.LAYER0
    );

    public static final ModelTemplate CAGE_LAMP_SIDE_ON = new ModelTemplate(
            Optional.of(ResourceLocation.fromNamespaceAndPath(Bittermelon.MOD_ID, "block/cage_lamp_side")),
            Optional.of("_side_on"),
            TextureSlot.TEXTURE
    );

    public static final ModelTemplate CAGE_LAMP_SIDE_OFF = new ModelTemplate(
            Optional.of(ResourceLocation.fromNamespaceAndPath(Bittermelon.MOD_ID, "block/cage_lamp_side")),
            Optional.of("_side_off"),
            TextureSlot.TEXTURE
    );

    public static final ModelTemplate CAGE_LAMP_TOP_ON = new ModelTemplate(
            Optional.of(ResourceLocation.fromNamespaceAndPath(Bittermelon.MOD_ID, "block/cage_lamp_top")),
            Optional.of("_top_on"),
            TextureSlot.TEXTURE
    );

    public static final ModelTemplate CAGE_LAMP_TOP_OFF = new ModelTemplate(
            Optional.of(ResourceLocation.fromNamespaceAndPath(Bittermelon.MOD_ID, "block/cage_lamp_top")),
            Optional.of("_top_off"),
            TextureSlot.TEXTURE
    );

    public static final ModelTemplate CAGE_LAMP_BOTTOM_ON = new ModelTemplate(
            Optional.of(ResourceLocation.fromNamespaceAndPath(Bittermelon.MOD_ID, "block/cage_lamp_bottom")),
            Optional.of("_bottom_on"),
            TextureSlot.TEXTURE
    );

    public static final ModelTemplate CAGE_LAMP_BOTTOM_OFF = new ModelTemplate(
            Optional.of(ResourceLocation.fromNamespaceAndPath(Bittermelon.MOD_ID, "block/cage_lamp_bottom")),
            Optional.of("_bottom_off"),
            TextureSlot.TEXTURE
    );
}
