package com.site21.bittermelon.init.custom;

import com.google.common.collect.ImmutableList;
import com.site21.bittermelon.Bittermelon;
import com.site21.bittermelon.common.systems.medical.wound.BodyPart;
import com.site21.bittermelon.common.systems.medical.wound.TissueType;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import static com.site21.bittermelon.init.neoforge.BitterRegistries.BODY_PART_REGISTRY;

public class BodyParts {
    public static final DeferredRegister<BodyPart> BODY_PARTS = DeferredRegister.create(BODY_PART_REGISTRY, Bittermelon.MOD_ID);

    public static final DeferredHolder<BodyPart, BodyPart> EMPTY = BODY_PARTS.register("empty",
            () -> new BodyPart(
                    0,
                    0,
                    new int[][]{{8, 8, 8, 8}, {24, 8, 8, 8}, {0, 8, 8, 8}, {16, 8, 8, 8}},
                    TissueType.NONE,
                    0,
                    ImmutableList.of(),
                    new Vec3(0, 0, 0),
                    new AABB(0, 0, 0, 0, 0, 0)
            )
    );

    public static final DeferredHolder<BodyPart, BodyPart> TORSO = BODY_PARTS.register("torso",
            () -> humanPart(
                    new int[][]{{20, 20, 8, 12}, {32, 20, 8, 12}, {16, 20, 4, 12}, {28, 20, 4, 12}},
                    ImmutableList.of(new Vec3(0, 0, 0), new Vec3(-5, 2, 0), new Vec3(5, 2, 0), new Vec3(-2, 12, 0), new Vec3(2, 12, 0)),
                    new Vec3(0, 0, 0),
                    new AABB(-0.5, -0.5, -0.5, 0.5, 0.5, 0.5)
            )
    );

    public static final DeferredHolder<BodyPart, BodyPart> HEAD = BODY_PARTS.register("head",
            () -> humanPart(
                    new int[][]{{8, 8, 8, 8}, {24, 8, 8, 8}, {0, 8, 8, 8}, {16, 8, 8, 8}},
                    ImmutableList.of(),
                    new Vec3(0, 0, 0),
                    new AABB(-0.5, -0.5, -0.5, 0.5, 0.5, 0.5)
            )
    );

    public static final DeferredHolder<BodyPart, BodyPart> RIGHT_ARM = BODY_PARTS.register("right_arm",
            () -> humanPart(
                    new int[][]{{44, 20, 4, 12}, {52, 20, 4, 12}, {40, 20, 4, 12}, {48, 20, 4, 12}},
                    ImmutableList.of(),
                    new Vec3(0, 0, 0),
                    new AABB(-0.5, -0.5, -0.5, 0.5, 0.5, 0.5)
            )
    );

    public static final DeferredHolder<BodyPart, BodyPart> LEFT_ARM = BODY_PARTS.register("left_arm",
            () -> humanPart(
                    new int[][]{{44, 20, 4, 12}, {52, 20, 4, 12}, {40, 20, 4, 12}, {48, 20, 4, 12}},
                    ImmutableList.of(),
                    new Vec3(0, 0, 0),
                    new AABB(-0.5, -0.5, -0.5, 0.5, 0.5, 0.5)
            )
    );

    public static final DeferredHolder<BodyPart, BodyPart> RIGHT_LEG = BODY_PARTS.register("right_leg",
            () -> humanPart(
                    new int[][]{{4, 20, 4, 12}, {12, 20, 4, 12}, {0, 20, 4, 12}, {8, 20, 4, 12}},
                    ImmutableList.of(),
                    new Vec3(0, 0, 0),
                    new AABB(-0.5, -0.5, -0.5, 0.5, 0.5, 0.5)
            )
    );

    public static final DeferredHolder<BodyPart, BodyPart> LEFT_LEG = BODY_PARTS.register("left_leg",
            () -> humanPart(
                    new int[][]{{4, 20, 4, 12}, {12, 20, 4, 12}, {0, 20, 4, 12}, {8, 20, 4, 12}},
                    ImmutableList.of(),
                    new Vec3(0, 0, 0),
                    new AABB(-0.5, -0.5, -0.5, 0.5, 0.5, 0.5)
            )
    );

    private static BodyPart humanPart(int[][] uvs, ImmutableList<Vec3> attachmentPoints, Vec3 pivot, AABB boundingBox) {
        return new BodyPart(64, 64, uvs, TissueType.FLESH, 0xFF0000, attachmentPoints, pivot, boundingBox);
    }
}
