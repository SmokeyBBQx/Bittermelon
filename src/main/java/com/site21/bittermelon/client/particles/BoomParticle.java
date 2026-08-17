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

public class BoomParticle extends SingleQuadParticle {

    private BoomParticle(ClientLevel level, double x, double y, double z, TextureAtlasSprite sprite) {
        super(level, x, y, z, sprite);
        this.setSprite(sprite);
        gravity = 0f;
        hasPhysics = false;
        lifetime = 20;
        xd = yd = zd = 0;
        setAlpha(1.0f);
        quadSize = 0.1f;
        roll = 0f;
        oRoll = 0f;
        rCol = 1.0f;
        gCol = 1.0f;
        bCol = 0.8f;
    }

    @Override
    public void tick() {
        xo = x; yo = y; zo = z;
        oRoll = roll;

        if (age++ >= lifetime) {
            remove();
            return;
        }

        float lifeRatio = (float) age / (float) lifetime;

        float growPhase = 0.15f;
        if (lifeRatio < growPhase) {
            float t = lifeRatio / growPhase;
            quadSize = 2.2f * easeOutBack(t);
        } else {
            float t = (lifeRatio - growPhase) / (1.0f - growPhase);
            quadSize = 2.2f * (1.0f + 0.4f * t);
        }

        float jerkAmplitude = 0.25f;
        float jerkSpeed = 2.2f;
        roll = (float) Math.sin(age * jerkSpeed) * jerkAmplitude;

        if (lifeRatio < 0.3f) {
            float t = lifeRatio / 0.3f;
            rCol = 1.0f;
            gCol = 1.0f - t * 0.4f;
            bCol = 0.8f - t * 0.8f;
        } else {
            float t = (lifeRatio - 0.3f) / 0.7f;
            rCol = 1.0f - t * 0.3f;
            gCol = 0.6f - t * 0.4f;
            bCol = 0.0f;
        }

        setAlpha(1.0f - (float) Math.pow(lifeRatio, 2));
    }

    private static float easeOutBack(float t) {
        float c1 = 1.70158f;
        float c3 = c1 + 1f;
        return 1 + c3 * (float) Math.pow(t - 1, 3) + c1 * (float) Math.pow(t - 1, 2);
    }

    @Override
    protected SingleQuadParticle.Layer getLayer() {
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
            return new BoomParticle(level, x, y, z, sprites.get(random));
        }
    }
}