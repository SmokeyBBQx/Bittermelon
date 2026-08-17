package com.site21.bittermelon.common.systems.medical.wound;

import com.site21.bittermelon.common.systems.medical.bodypart.HealthContainer;
import com.site21.bittermelon.common.systems.medical.bodypart.PartInstance;
import com.site21.bittermelon.common.systems.medical.bodypart.client.FaceUV;
import com.site21.bittermelon.common.systems.medical.wound.networking.WoundPacket;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.common.damagesource.DamageContainer;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;
import net.neoforged.neoforge.network.PacketDistributor;
import org.jspecify.annotations.Nullable;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static com.site21.bittermelon.init.neoforge.BitterAttachmentTypes.HEALTH_CONTAINER;

public class HitCalculator {
    public static void damageEntity(LivingDamageEvent.Pre event) {
        DamageContainer damageContainer = event.getContainer();
        DamageSource source = damageContainer.getSource();
        LivingEntity target = event.getEntity();

        HealthContainer healthContainer = target.getData(HEALTH_CONTAINER);

        if (source.getDirectEntity() instanceof Entity attacker) {
            Vec3 from = attacker.getEyePosition();
            Vec3 to = from.add(attacker.getLookAngle().scale(32));

            PickResult root = new PickResult(healthContainer.getRoot(), Vec3.ZERO, Double.MAX_VALUE, Vec3.ZERO, null);
            PickResult pickResult = pickPart(
                    healthContainer.getRoot(),
                    toLocalSpace(from, target),
                    toLocalSpace(to, target),
                    Vec3.ZERO,
                    Double.MAX_VALUE,
                    root,
                    target
            );

            if (pickResult.face() == null) return;

            Direction hitFace = pickResult.face;
            FaceUV faceUV = pickResult.part().getBodyPart().uvs().get(hitFace);
            AABB partBox = getPartAABB(pickResult.part(), pickResult.offset(), target);
            Vec2 woundUV = getUV(pickResult.point(), partBox, hitFace, faceUV);

            Wound wound = new Wound(
                    UUID.randomUUID(),
                    Math.round(woundUV.x),
                    Math.round(woundUV.y),
                    target.level().getGameTime());

            if (target.level() instanceof ServerLevel level) {
                Vec3 pos = pickResult.point.add(target.position());
                level.sendParticles(
                        new BlockParticleOption(ParticleTypes.BLOCK, Blocks.REDSTONE_BLOCK.defaultBlockState(), null),
                        pos.x, pos.y, pos.z,
                        10,
                        0.1,
                        0.1,
                        0.1,
                        0.0
                );
            }

            PacketDistributor.sendToAllPlayers(
                    new WoundPacket(target.getUUID(), wound, healthContainer.getParts().indexOf(pickResult.part())));
        }
    }

    public static PickResult pickPart(PartInstance part, Vec3 from, Vec3 to, Vec3 offset, double closestDistance, PickResult best, LivingEntity target) {
        AABB aabb = getPartAABB(part, offset, target);
        Optional<HitResult> clipPoint = clip(from, to, aabb);
        if (clipPoint.isPresent()) {
            HitResult hitResult = clipPoint.get();
            double dd = from.distanceTo(hitResult.point());
            if (dd < closestDistance) {
                closestDistance = dd;
                best = new PickResult(part, offset, closestDistance, hitResult.point(), hitResult.face());
            }
        }

        for (Map.Entry<Vec3, PartInstance> entry : part.getAttachedParts().entrySet()) {
            Vec3 childOffset = offset.add(entry.getKey());
            best = pickPart(entry.getValue(), from, to, childOffset, closestDistance, best, target);
            closestDistance = best.distance;
        }

        return new PickResult(best.part, best.offset, closestDistance, best.point(), best.face());
    }

    private static AABB getPartAABB(PartInstance part, Vec3 offset, LivingEntity target) {
        return part.getBodyPart().boundingBox()
                .move(offset.scale(1 / 16.0).multiply(1, -1, 1))
                .move(part.getBodyPart().offset().scale(1 / 16.0).multiply(1, -1, 1))
                .move(0, target.getEyeHeight() - 0.475, 0);
    }

    private static Vec3 toLocalSpace(Vec3 worldPoint, LivingEntity target) {
        Vec3 relative = worldPoint.subtract(target.position());
        double yawRad = Math.toRadians(-target.getYRot());
        double cos = Math.cos(yawRad);
        double sin = Math.sin(yawRad);
        double x = relative.x * cos - relative.z * sin;
        double z = relative.x * sin + relative.z * cos;
        return new Vec3(x, relative.y, z);
    }

    public static Optional<HitResult> clip(Vec3 from, Vec3 to, AABB boundingBox) {
        return clip(boundingBox.minX, boundingBox.minY, boundingBox.minZ, boundingBox.maxX, boundingBox.maxY, boundingBox.maxZ, from, to);
    }

    public static Optional<HitResult> clip(double minX, double minY, double minZ, double maxX, double maxY, double maxZ, Vec3 from, Vec3 to) {
        double[] scaleReference = new double[]{1.0};
        double dx = to.x - from.x;
        double dy = to.y - from.y;
        double dz = to.z - from.z;
        Direction direction = getDirection(minX, minY, minZ, maxX, maxY, maxZ, from, scaleReference, null, dx, dy, dz);
        if (direction == null) {
            return Optional.empty();
        } else {
            double scale = scaleReference[0];
            return Optional.of(new HitResult(from.add(scale * dx, scale * dy, scale * dz), direction));
        }
    }

    private static @Nullable Direction getDirection(
            double minX,
            double minY,
            double minZ,
            double maxX,
            double maxY,
            double maxZ,
            Vec3 from,
            double[] scaleReference,
            @Nullable Direction direction,
            double dx,
            double dy,
            double dz
    ) {
        if (dx > 1.0E-7) {
            direction = clipPoint(scaleReference, direction, dx, dy, dz, minX, minY, maxY, minZ, maxZ, Direction.WEST, from.x, from.y, from.z);
        } else if (dx < -1.0E-7) {
            direction = clipPoint(scaleReference, direction, dx, dy, dz, maxX, minY, maxY, minZ, maxZ, Direction.EAST, from.x, from.y, from.z);
        }

        if (dy > 1.0E-7) {
            direction = clipPoint(scaleReference, direction, dy, dz, dx, minY, minZ, maxZ, minX, maxX, Direction.DOWN, from.y, from.z, from.x);
        } else if (dy < -1.0E-7) {
            direction = clipPoint(scaleReference, direction, dy, dz, dx, maxY, minZ, maxZ, minX, maxX, Direction.UP, from.y, from.z, from.x);
        }

        if (dz > 1.0E-7) {
            direction = clipPoint(scaleReference, direction, dz, dx, dy, minZ, minX, maxX, minY, maxY, Direction.SOUTH, from.z, from.x, from.y);
        } else if (dz < -1.0E-7) {
            direction = clipPoint(scaleReference, direction, dz, dx, dy, maxZ, minX, maxX, minY, maxY, Direction.NORTH, from.z, from.x, from.y);
        }

        return direction;
    }

    private static @Nullable Direction clipPoint(
            double[] scaleReference,
            @Nullable Direction direction,
            double da,
            double db,
            double dc,
            double point,
            double minB,
            double maxB,
            double minC,
            double maxC,
            Direction newDirection,
            double fromA,
            double fromB,
            double fromC
    ) {
        double s = (point - fromA) / da;
        double pb = fromB + s * db;
        double pc = fromC + s * dc;
        if (0.0 < s && s < scaleReference[0] && minB - 1.0E-7 < pb && pb < maxB + 1.0E-7 && minC - 1.0E-7 < pc && pc < maxC + 1.0E-7) {
            scaleReference[0] = s;
            return newDirection;
        } else {
            return direction;
        }
    }

    private static Vec2 getUV(Vec3 point, AABB box, Direction face, FaceUV uv) {
        double fracX = 0;
        double fracY = 0;

        switch (face) {
            case NORTH -> {
                fracX = (point.x - box.minX) / (box.maxX - box.minX);
                fracY = 1.0 - (point.y - box.minY) / (box.maxY - box.minY);
            }
            case EAST -> {
                fracX = 1.0 - (point.z - box.minZ) / (box.maxZ - box.minZ);
                fracY = 1.0 - (point.y - box.minY) / (box.maxY - box.minY);
            }
            case SOUTH -> {
                fracX = 1.0 - (point.x - box.minX) / (box.maxX - box.minX);
                fracY = 1.0 - (point.y - box.minY) / (box.maxY - box.minY);
            }
            case WEST -> {
                fracX = (point.z - box.minZ) / (box.maxZ - box.minZ);
                fracY = 1.0 - (point.y - box.minY) / (box.maxY - box.minY);
            }
            case UP -> {
                fracX = (point.x - box.minX) / (box.maxX - box.minX);
                fracY = 1.0 - (point.z - box.minZ) / (box.maxZ - box.minZ);
            }
            case DOWN -> {
                fracX = (point.x - box.minX) / (box.maxX - box.minX);
                fracY = (point.z - box.minZ) / (box.maxZ - box.minZ);
            }
        }

        fracX = Mth.clamp(fracX, 0.0, 1.0);
        fracY = Mth.clamp(fracY, 0.0, 1.0);

        float u = (float) (uv.u() + fracX * uv.width());
        float v = (float) (uv.v() + fracY * uv.height());
        return new Vec2(u, v);
    }

    public record HitResult(Vec3 point, Direction face) {
    }

    public record PickResult(PartInstance part, Vec3 offset, double distance, Vec3 point, Direction face) {
    }
}
