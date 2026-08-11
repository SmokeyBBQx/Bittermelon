package com.site21.bittermelon.common.systems.medical.wound.client;

import com.mojang.blaze3d.platform.NativeImage;
import com.site21.bittermelon.Bittermelon;
import com.site21.bittermelon.common.systems.medical.bodypart.BodyPart;
import com.site21.bittermelon.common.systems.medical.wound.Wound;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.Level;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

public class ClientWoundGenerator {
    public static ClientWound test(Wound wound, UUID uuid, BodyPart part) {
        NativeImage image = new NativeImage(part.texW(), part.texH(), false);
        image.fillRect(0, 0, part.texW(), part.texH(), 0);

        List<Pixel> pixels = dripPixels(part, wound, new Random(wound.seed()));
        int bruiseColor = (200 << 24) | part.bloodColor();
        pixels.add(new Pixel(0, 0, bruiseColor));
        pixels.add(new Pixel(0, 1, bruiseColor));
        pixels.add(new Pixel(1, 0, bruiseColor));
        pixels.add(new Pixel(-1, 0, bruiseColor));
        pixels.add(new Pixel(0, -1, bruiseColor));
        for (Pixel pixel : pixels) {
            image.setPixel(pixel.x() + wound.u(), pixel.y() + wound.v(), pixel.color());
        }

        Identifier identifier = Bittermelon.identifier("wound/" + uuid.toString() + "/" + wound.id().toString());
        DynamicTexture texture = new DynamicTexture(() -> "Wound", image);
        Minecraft.getInstance().getTextureManager().register(identifier, texture);
        return new ClientWound(wound, texture, identifier);
    }

    public static List<Pixel> dripPixels(BodyPart part, Wound wound, Random random) {
        Level level = Minecraft.getInstance().level;
        if (level == null) return new ArrayList<>();

        List<Pixel> pixels = new ArrayList<>();
        long time = level.getGameTime() - wound.timeAdded();
        int dripCount = 1 + random.nextInt(3);

        for (int i = 0; i < dripCount; i++) {
            int x = random.nextInt(5) - 2;
            int maxLength = part.texH() - wound.v();
            if (maxLength <= 0) continue;
            int length = 4 + random.nextInt(maxLength);
            int startAlpha = 200 + random.nextInt(56);

            List<Pixel> path = new ArrayList<>(length);
            for (int y = 0; y < length; y++) {
                int absX = wound.u() + x;
                int absY = wound.v() + y;
                if (absX < 0 || absX >= part.texW() || absY < 0 || absY >= part.texH()) break;

                float fade = 1.0f - ((float) y / length) * 0.65f;
                int alpha = Math.max(25, (int) (startAlpha * fade));

                int color = (alpha << 24) | part.bloodColor();
                path.add(new Pixel(x, y, color));

                if (random.nextInt(4) == 0) {
                    x += random.nextBoolean() ? 1 : -1;
                }

                if (y > 3 && random.nextInt(20) == 0) break;
            }

            int visible = Math.clamp(time / 20, 1, path.size());
            pixels.addAll(path.subList(0, visible));
        }

        return pixels;
    }
}
