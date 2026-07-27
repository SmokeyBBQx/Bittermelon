package com.site21.bittermelon.common.systems.medical.wound;

import com.mojang.blaze3d.platform.NativeImage;
import com.site21.bittermelon.Bittermelon;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.resources.Identifier;

import java.util.List;
import java.util.UUID;

public class ClientWoundFactory {

    public static ClientWound test(Wound wound, UUID uuid, BodyPart part) {
        NativeImage image = new NativeImage(part.texW(), part.texH(), false);
        image.fillRect(0, 0, part.texW(), part.texH(), 0);
        List<Pixel> pixels = testPixels(part);
        for (Pixel pixel : pixels) {
            image.setPixel(pixel.x() + wound.u(), pixel.y() + wound.v(), pixel.color());
        }

        Identifier identifier = Bittermelon.identifier("wound/" + uuid.toString() + "/" + wound.id().toString());
        DynamicTexture texture = new DynamicTexture(() -> "Wound", image);
        Minecraft.getInstance().getTextureManager().register(identifier, texture);
        return new ClientWound(wound, texture, identifier);
    }

    public static List<Pixel> testPixels(BodyPart part) {
        return List.of(
                new Pixel(0, 0, 0xFF00000),
                new Pixel(1, 0, 0xFF00000),
                new Pixel(2, 0, 0xFF00000),
                new Pixel(3, 0, 0xFF00000),
                new Pixel(4, 0, 0xFF00000),
                new Pixel(5, 0, 0xFF00000),
                new Pixel(6, 0, 0xFF00000),
                new Pixel(7, 0, 0xFF00000),
                new Pixel(8, 0, 0xFF00000),
                new Pixel(9, 0, 0xFF00000)
        );
    }
}
