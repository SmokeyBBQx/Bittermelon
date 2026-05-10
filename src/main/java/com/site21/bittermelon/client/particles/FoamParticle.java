package com.site21.bittermelon.client.particles;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.TextureSheetParticle;
import net.minecraft.core.particles.ColorParticleOption;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class FoamParticle extends TextureSheetParticle {
    public FoamParticle(ColorParticleOption options, ClientLevel level, double x, double y, double z) {
        super(level, x, y, z);

    }

    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
    }

    public static class Provider implements ParticleProvider<ColorParticleOption> {
        public Provider() {}

        @Override
        public @Nullable Particle createParticle(@NotNull ColorParticleOption option, @NotNull ClientLevel level,
                                                 double x, double y, double z, double xSpeed, double ySpeed,
                                                 double zSpeed) {
            return new FoamParticle(option, level, x, y, z);
        }
    }
}
