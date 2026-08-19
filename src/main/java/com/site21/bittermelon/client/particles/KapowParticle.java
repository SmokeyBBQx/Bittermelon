package com.site21.bittermelon.client.particles;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SingleQuadParticle;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.RandomSource;

import org.jetbrains.annotations.Nullable;

public class KapowParticle extends SingleQuadParticle {

    private KapowParticle(ClientLevel level, double x, double y, double z, TextureAtlasSprite sprite) {
        super(level, x, y, z, sprite);
        this.setSprite(sprite);
        gravity = 0f;
        hasPhysics = false;
        lifetime = 15;
        xd = yd = zd = 0;
        setAlpha(1.0f);
        quadSize = 0.1f;
    }

    @Override
    public void tick() {
        xo = x; yo = y; zo = z;

        if (age++ >= lifetime) {
            remove();
            return;
        }

        float lifeRatio = (float) age / (float) lifetime;

        float growPhase = 0.2f;
        if (lifeRatio < growPhase) {
            float t = lifeRatio / growPhase;
            quadSize = 1.4f * easeOutBack(t);
        } else {
            float t = (lifeRatio - growPhase) / (1.0f - growPhase);
            quadSize = 1.4f * (1.0f - 0.3f * t);
        }

        setAlpha(1.0f - (float) Math.pow(lifeRatio, 3));
    }

    private static float easeOutBack(float t) {
        float c1 = 1.70158f;
        float c3 = c1 + 1f;
        return 1 + c3 * (float) Math.pow(t - 1, 3) + c1 * (float) Math.pow(t - 1, 2);
    }

    @Override
    protected Layer getLayer() {
        return Layer.TRANSLUCENT;
    }

    public static class Provider implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet sprites;
        public Provider(SpriteSet spriteSet) { sprites = spriteSet; }

        @Override
        public @Nullable Particle createParticle(
                SimpleParticleType type, ClientLevel level, double x, double y, double z,
                double xAux, double yAux, double zAux, RandomSource random
        ) {
            return new KapowParticle(level, x, y, z, sprites.get(random));
        }
    }
}
