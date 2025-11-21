package com.site21.bittermelon.client.particles;

import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.particles.ColorParticleOption;
import net.minecraft.util.Mth;
import net.minecraft.world.level.levelgen.LegacyRandomSource;
import net.minecraft.world.level.levelgen.synth.PerlinSimplexNoise;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Quaternionf;
import org.joml.Vector3f;

import java.util.List;

public class PlasticParticle extends TextureSheetParticle {
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

    private PlasticParticle(@NotNull ColorParticleOption options, ClientLevel world, double x, double y, double z, double motionX, double motionY, double motionZ, SpriteSet sprites) {
        super(world, x, y, z);
        pickSprite(sprites);

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
    public void render(@NotNull VertexConsumer buffer, @NotNull Camera renderInfo, float partialTicks) {
        Vec3 cameraPosition = renderInfo.getPosition();

        // Compute interpolated position
        float relX = (float) (Mth.lerp(partialTicks, xo, x) - cameraPosition.x());
        float relY = (float) (Mth.lerp(partialTicks, yo, y) - cameraPosition.y());
        float relZ = (float) (Mth.lerp(partialTicks, zo, z) - cameraPosition.z());

        // Compute rotation
        Quaternionf rotation = new Quaternionf();
        rotation.rotateZ(Mth.lerp(partialTicks, oRoll, roll));
        rotation.rotateY(Mth.lerp(partialTicks, oYaw, yaw));
        rotation.rotateX(Mth.lerp(partialTicks, oPitch, pitch));

        // Define quad vertices
        Vector3f[] vertices = new Vector3f[]{
                new Vector3f(-1.0F, -1.0F, 0.0F),
                new Vector3f(-1.0F, 1.0F, 0.0F),
                new Vector3f(1.0F, 1.0F, 0.0F),
                new Vector3f(1.0F, -1.0F, 0.0F)
        };

        float size = getQuadSize(partialTicks);

        // Transform vertices
        for (int i = 0; i < 4; ++i) {
            Vector3f vertex = vertices[i];
            vertex.rotate(rotation);
            vertex.mul(size);
            vertex.add(relX, relY, relZ);
        }

        // Get texture coordinates and light level
        float minU = getU0();
        float maxU = getU1();
        float minV = getV0();
        float maxV = getV1();
        int lightLevel = getLightColor(partialTicks);

        // Render front face
        renderQuad(buffer, vertices, minU, maxU, minV, maxV, lightLevel);
        // Render back face
        renderQuad(buffer, new Vector3f[]{vertices[3], vertices[2], vertices[1], vertices[0]}, minU, maxU, minV, maxV, lightLevel);
    }

    private void renderQuad(@NotNull VertexConsumer buffer, Vector3f @NotNull [] vertices, float minU, float maxU, float minV, float maxV, int lightLevel) {
        buffer.addVertex(vertices[0].x(), vertices[0].y(), vertices[0].z()).setUv(maxU, maxV).setColor(rCol, gCol, bCol, alpha).setLight(lightLevel);
        buffer.addVertex(vertices[1].x(), vertices[1].y(), vertices[1].z()).setUv(maxU, minV).setColor(rCol, gCol, bCol, alpha).setLight(lightLevel);
        buffer.addVertex(vertices[2].x(), vertices[2].y(), vertices[2].z()).setUv(minU, minV).setColor(rCol, gCol, bCol, alpha).setLight(lightLevel);
        buffer.addVertex(vertices[3].x(), vertices[3].y(), vertices[3].z()).setUv(minU, maxV).setColor(rCol, gCol, bCol, alpha).setLight(lightLevel);
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
    public @NotNull ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
    }

    public static class Provider implements ParticleProvider<ColorParticleOption> {
        private final SpriteSet sprite;

        public Provider(SpriteSet spriteSet) {
            sprite = spriteSet;
        }

        @Override
        public @Nullable Particle createParticle(@NotNull ColorParticleOption option, @NotNull ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            return new PlasticParticle(option, level, x, y, z, xSpeed, ySpeed, zSpeed, sprite);
        }
    }
}
