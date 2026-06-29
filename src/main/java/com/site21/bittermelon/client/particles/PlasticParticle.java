package com.site21.bittermelon.client.particles;

import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SingleQuadParticle;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.renderer.state.level.QuadParticleRenderState;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.particles.ColorParticleOption;
import net.minecraft.util.ARGB;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.levelgen.LegacyRandomSource;
import net.minecraft.world.level.levelgen.synth.PerlinSimplexNoise;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Quaternionf;

import java.util.List;

public class PlasticParticle extends SingleQuadParticle {
    private static final float SIZE = 0.001f;
    private static final float GRAVITY = 0.7f;
    private static final float FRICTION = 0.94f;
    private static final int LIFETIME_MIN = 400;
    private static final int LIFETIME_MAX = 700;
    private static final float QUAD_SIZE_MULTIPLIER = 1.25f;

    private static final float POSITION_NOISE_SCALE = 0.01f;
    private static final float ROTATION_NOISE_SCALE = 0.1f;
    private static final float ROTATION_MOMENTUM = 0.98f;
    private static final int LANDING_FADE_DURATION = 20;

    protected static final PerlinSimplexNoise X_NOISE = noise(58637214);
    protected static final PerlinSimplexNoise Z_NOISE = noise(823917);
    protected static final PerlinSimplexNoise YAW_NOISE = noise(28943157);
    protected static final PerlinSimplexNoise ROLL_NOISE = noise(80085);
    protected static final PerlinSimplexNoise PITCH_NOISE = noise(49715286);

    @Contract("_ -> new")
    private static @NotNull PerlinSimplexNoise noise(int seed) {
        return new PerlinSimplexNoise(new LegacyRandomSource(seed),
                List.of(-4, -3, -2, -1, 0, 1, 2));
    }

    private final int particleRandom;
    private float pitch;
    private float oPitch;
    private float yaw;
    private float oYaw;

    private float dPitch;
    private float dYaw;
    private float dRoll;

    private PlasticParticle(ColorParticleOption options, ClientLevel level, double x, double y, double z,
                            double motionX, double motionY, double motionZ, TextureAtlasSprite sprite) {
        super(level, x, y, z, sprite);

        particleRandom = random.nextInt();
        xd = motionX;
        yd = motionY;
        zd = motionZ;
        roll = random.nextFloat() * 360f;
        yaw = random.nextFloat() * 360f;
        pitch = random.nextFloat() * 360f;

        setSize(SIZE, SIZE);
        gravity = GRAVITY;
        friction = FRICTION;
        lifetime = random.nextInt(LIFETIME_MIN, LIFETIME_MAX);
        quadSize *= QUAD_SIZE_MULTIPLIER;

        rCol = options.getRed();
        gCol = options.getGreen();
        bCol = options.getBlue();
        alpha = options.getAlpha();
    }

    @Override
    protected void extractRotatedQuad(QuadParticleRenderState particleTypeRenderState, Camera camera, Quaternionf rotation, float partialTickTime) {
        Quaternionf fullRotation = new Quaternionf(rotation);
        fullRotation.rotateZ(Mth.lerp(partialTickTime, oRoll, roll));
        fullRotation.rotateY(Mth.lerp(partialTickTime, oYaw, yaw));
        fullRotation.rotateX(Mth.lerp(partialTickTime, oPitch, pitch));

        var pos = camera.position();
        float relX = (float) (Mth.lerp(partialTickTime, xo, x) - pos.x());
        float relY = (float) (Mth.lerp(partialTickTime, yo, y) - pos.y());
        float relZ = (float) (Mth.lerp(partialTickTime, zo, z) - pos.z());

        float size = getQuadSize(partialTickTime);
        int color = ARGB.colorFromFloat(alpha, rCol, gCol, bCol);
        int light = getLightCoords(partialTickTime);
        SingleQuadParticle.Layer layer = getLayer();

        particleTypeRenderState.add(
                layer,
                relX, relY, relZ,
                fullRotation.x, fullRotation.y, fullRotation.z, fullRotation.w,
                size,
                getU0(), getU1(), getV0(), getV1(),
                color, light
        );

        Quaternionf backRotation = new Quaternionf(
                -fullRotation.x, -fullRotation.y, -fullRotation.z, fullRotation.w
        );

        particleTypeRenderState.add(
                layer,
                relX, relY, relZ,
                backRotation.x, backRotation.y, backRotation.z, backRotation.w,
                size,
                getU0(), getU1(), getV0(), getV1(),
                color, light
        );
    }

    @Override
    public void tick() {
        boolean isStationary = x == xo && z == zo && y == yo && age != 0;
        boolean hasLanded = onGround || isStationary;

        // Apply procedural noise to position
        xd += POSITION_NOISE_SCALE * X_NOISE.getValue(particleRandom, age, false);
        zd += POSITION_NOISE_SCALE * Z_NOISE.getValue(particleRandom, age, false);

        // Store previous rotation
        oYaw = yaw;
        oPitch = pitch;
        oRoll = roll;

        if (!hasLanded) {
            // Apply procedural noise to rotation
            dYaw += (float) (ROTATION_NOISE_SCALE * YAW_NOISE.getValue(particleRandom, age, false));
            dRoll += (float) (ROTATION_NOISE_SCALE * ROLL_NOISE.getValue(particleRandom, age, false));
            dPitch += (float) (ROTATION_NOISE_SCALE * PITCH_NOISE.getValue(particleRandom, age, false));

            // Update rotation
            yaw += dYaw;
            pitch += dPitch;
            roll += dRoll;
        } else {
            // Extend lifetime to allow for fade out
            age = Math.max(age, lifetime - LANDING_FADE_DURATION);
        }

        // Apply rotation momentum damping
        dYaw *= ROTATION_MOMENTUM;
        dRoll *= ROTATION_MOMENTUM;
        dPitch *= ROTATION_MOMENTUM;

        super.tick();
    }

    @Override
    protected Layer getLayer() {
        return Layer.OPAQUE;
    }

    public static class Provider implements ParticleProvider<ColorParticleOption> {
        private final SpriteSet sprites;

        public Provider(SpriteSet spriteSet) {
            sprites = spriteSet;
        }

        @Override
        public @Nullable Particle createParticle(
                ColorParticleOption options, ClientLevel level, double x, double y, double z, double xAux, double yAux,
                double zAux, RandomSource random
        ) {
            return new PlasticParticle(options, level, x, y, z, xAux, yAux, zAux, sprites.get(random));
        }
    }
}
