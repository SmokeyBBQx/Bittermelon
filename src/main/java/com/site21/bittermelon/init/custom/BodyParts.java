package com.site21.bittermelon.init.custom;

import com.google.common.collect.ImmutableList;
import com.site21.bittermelon.Bittermelon;
import com.site21.bittermelon.common.systems.medical.bodypart.BodyPart;
import com.site21.bittermelon.common.systems.medical.bodypart.TissueType;
import com.site21.bittermelon.common.systems.medical.bodypart.client.FaceUV;
import net.minecraft.core.Direction;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.Map;

import static com.site21.bittermelon.init.neoforge.BitterRegistries.BODY_PART_REGISTRY;

public class BodyParts {
    public static final DeferredRegister<BodyPart> BODY_PARTS = DeferredRegister.create(BODY_PART_REGISTRY, Bittermelon.MOD_ID);

    private static final AABB HEAD_BOX  = box(8, 8, 8, new Vec3(0, 8, 0));
    private static final AABB TORSO_BOX = box(8, 12, 4, Vec3.ZERO);
    private static final AABB ARM_BOX = box(4, 9, 4, new Vec3(0, 2, 0));
    private static final AABB LEG_BOX = box(4, 12, 4, Vec3.ZERO);

    public static final DeferredHolder<BodyPart, BodyPart> EMPTY = BODY_PARTS.register("empty",
            () -> new BodyPart(
                    0,
                    0,
                    texOffs(0, 0, 0, 0, 0),
                    TissueType.NONE,
                    0,
                    ImmutableList.of(),
                    new Vec3(0, 0, 0),
                    new AABB(0, 0, 0, 0, 0, 0)
            )
    );

    public static final DeferredHolder<BodyPart, BodyPart> TORSO = BODY_PARTS.register("torso",
            () -> humanPart(
                    texOffs(16, 16, 8, 12, 4),
                    ImmutableList.of(new Vec3(0, -4, 0), new Vec3(-4, -2, 0), new Vec3(4, -2, 0), new Vec3(-2, 6, 0), new Vec3(2, 6, 0)),
                    new Vec3(0, 0, 0),
                    TORSO_BOX
            )
    );

    public static final DeferredHolder<BodyPart, BodyPart> HEAD = BODY_PARTS.register("head",
            () -> humanPart(
                    texOffs(0, 0, 8, 8, 8),
                    ImmutableList.of(),
                    new Vec3(0, 4, 0),
                    HEAD_BOX
            )
    );

    public static final DeferredHolder<BodyPart, BodyPart> RIGHT_ARM = BODY_PARTS.register("right_arm",
            () -> humanPart(
                    texOffs(40, 16, 4, 9, 4),
                    ImmutableList.of(),
                    new Vec3(-1, 4, 0),
                    ARM_BOX
            )
    );

    public static final DeferredHolder<BodyPart, BodyPart> LEFT_ARM = BODY_PARTS.register("left_arm",
            () -> humanPart(
                    texOffs(32, 48, 4, 9, 4),
                    ImmutableList.of(),
                    new Vec3(1, 4, 0),
                    ARM_BOX
            )
    );

    public static final DeferredHolder<BodyPart, BodyPart> RIGHT_LEG = BODY_PARTS.register("right_leg",
            () -> humanPart(
                    texOffs(0, 16, 4, 12, 4),
                    ImmutableList.of(),
                    new Vec3(0, 6, 0),
                    LEG_BOX
            )
    );

    public static final DeferredHolder<BodyPart, BodyPart> LEFT_LEG = BODY_PARTS.register("left_leg",
            () -> humanPart(
                    texOffs(16, 48, 4, 12, 4),
                    ImmutableList.of(),
                    new Vec3(0, 6, 0),
                    LEG_BOX
            )
    );

    public static final DeferredHolder<BodyPart, BodyPart> SKELETON_LEFT_ARM = BODY_PARTS.register("skeleton_left_arm",
            () -> new BodyPart(
                    64,
                    64,
                    texOffs(44, 20, 4, 12, 4),
                    TissueType.FLESH,
                    0xFFFFFF,
                    ImmutableList.of(),
                    new Vec3(0, 0, 0),
                    new AABB(-0.5, -0.5, -0.5, 0.5, 0.5, 0.5)
            )
    );

    public static final DeferredHolder<BodyPart, BodyPart> ZOMBIE_RIGHT_ARM = BODY_PARTS.register("zombie_right_arm",
            () -> new BodyPart(
                    64,
                    64,
                    texOffs(40, 16, 4, 9, 4),
                    TissueType.FLESH,
                    0xAAFF00,
                    ImmutableList.of(),
                    new Vec3(-1, 4, 0),
                    ARM_BOX
            )
    );

    private static BodyPart humanPart(Map<Direction, FaceUV> uvs, ImmutableList<Vec3> attachmentPoints, Vec3 pivot, AABB boundingBox) {
        return new BodyPart(64, 64, uvs, TissueType.FLESH, 0x8B0000, attachmentPoints, pivot, boundingBox);
    }

    private static AABB box(double width, double height, double depth, Vec3 offset) {
        double hw = (width / 16.0) / 2.0;
        double hh = (height / 16.0) / 2.0;
        double hd = (depth / 16.0) / 2.0;
        return new AABB(-hw, -hh, -hd, hw, hh, hd).move(offset.scale(1 / 16.0));
    }

    private static Map<Direction, FaceUV> texOffs(int u, int v, int width, int height, int depth) {
        return Map.of(
                Direction.UP, new FaceUV(u + depth, v, width, depth),
                Direction.DOWN, new FaceUV(u + depth + width, v, width, depth),
                Direction.WEST, new FaceUV(u, v + depth, depth, height),
                Direction.NORTH, new FaceUV(u + depth, v + depth, width, height),
                Direction.EAST, new FaceUV(u + depth + width, v + depth, depth, height),
                Direction.SOUTH, new FaceUV(u + depth + width + depth, v + depth, width, height)
        );
    }
}
