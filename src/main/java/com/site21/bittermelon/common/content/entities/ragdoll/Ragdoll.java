package com.site21.bittermelon.common.content.entities.ragdoll;

import com.github.stephengold.joltjni.*;
import com.github.stephengold.joltjni.enumerate.EActivation;
import com.github.stephengold.joltjni.enumerate.EConstraintSpace;
import com.github.stephengold.joltjni.enumerate.EMotionType;
import com.github.stephengold.joltjni.enumerate.ESwingType;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.ArrayList;
import java.util.List;

import static com.site21.bittermelon.common.events.PhysicsManager.MOVING_LAYER;
import static com.site21.bittermelon.common.events.PhysicsManager.NON_MOVING_LAYER;

public class Ragdoll {
    private final PhysicsSystem physicsSystem;
    private final List<Body> parts = new ArrayList<>();
    private final List<Constraint> joints = new ArrayList<>();
    private final List<Body> localStaticCollision = new ArrayList<>();
    private static final int COLLISION_RADIUS = 3;

    public Ragdoll(PhysicsSystem physicsSystem, Vec3 pos) {
        this.physicsSystem = physicsSystem;
        BodyCreationSettings bcs = new BodyCreationSettings();
        bcs.setMotionType(EMotionType.Dynamic);
        bcs.setObjectLayer(MOVING_LAYER);
        bcs.setFriction(0.5f);
        bcs.setRestitution(0.3f);

        makeBody(bcs, 0.25f, 0.25f, 0.25f, 10.0f, pos, new Vec3(0.0f, 0.64f, 0.0f));
        makeBody(bcs, 0.24f, 0.375f, 0.129f, 30.0f, pos, new Vec3(0.0f, 0.125f, 0.0f));
        makeBody(bcs, 0.15f, 0.45f, 0.15f, 1.0f, pos, new Vec3(-0.369f, -0.375f, 0.0f));
        makeBody(bcs, 0.15f, 0.45f, 0.15f, 1.0f, pos, new Vec3(0.369f, -0.375f, 0.0f));
        makeBody(bcs, 0.15f, 0.45f, 0.15f, 1.0f, pos, new Vec3(-0.13f, -0.77f, 0.0f));
        makeBody(bcs, 0.15f, 0.45f, 0.15f, 1.0f, pos, new Vec3(0.13f, -0.77f, 0.0f));

        connectJoints();
    }

    private void makeBody(BodyCreationSettings bcs, float xHalfExtent, float yHalfExtent, float zHalfExtent, float mass, Vec3 origin, Vec3 offset) {
        bcs.setShape(new BoxShape(xHalfExtent, yHalfExtent, zHalfExtent));

        bcs.setPosition(origin.getX() + offset.getX(), origin.getY() + offset.getY(), origin.getZ() + offset.getZ());

        BodyInterface bi = physicsSystem.getBodyInterface();
        Body body = bi.createBody(bcs);
        bi.addBody(body, EActivation.Activate);
        parts.add(body);
    }

    private void connectJoints() {
        SwingTwistConstraintSettings settings = new SwingTwistConstraintSettings();

        // neck
        addJoint(settings, 0, new Vec3(0, 0.41f, 0), new Vec3(0.0f, -0.23f, 0.0f), 80f, 120f, 80f);

        // shoulders
        addJoint(settings, 2, new Vec3(-0.369f, 0.375f, 0.0f), new Vec3(0.0f, 0.35f, 0.0f), 120f, 175f, 175f);
        addJoint(settings, 3, new Vec3(0.369f, 0.375f, 0.0f), new Vec3(0.0f, 0.35f, 0.0f), 120f, 175f, 175f);

        // hips
        addJoint(settings, 4, new Vec3(-0.13f, -0.42f, 0.0f), new Vec3(0.0f, 0.35f, 0.0f), 80f, 140f, 100f);
        addJoint(settings, 5, new Vec3(0.13f, -0.42f, 0.0f), new Vec3(0.0f, 0.35f, 0.0f), 80f, 140f, 100f);
    }

    private void addJoint(SwingTwistConstraintSettings settings, int i, Vec3 attachmentPoint, Vec3 pivot, float twist, float swingY, float swingZ) {
        settings.setSpace(EConstraintSpace.LocalToBodyCom);
        settings.setSwingType(ESwingType.Pyramid);
        settings.setPosition1(attachmentPoint.toRVec3());
        settings.setPosition2(pivot.toRVec3());

        Vec3 primaryAxis = new com.github.stephengold.joltjni.Vec3(0, 1, 0);
        Vec3 secondaryAxis = new com.github.stephengold.joltjni.Vec3(1, 0, 0);
        settings.setTwistAxis1(primaryAxis);
        settings.setPlaneAxis1(secondaryAxis);
        settings.setTwistAxis2(primaryAxis);
        settings.setPlaneAxis2(secondaryAxis);

        settings.setTwistMinAngle((float) Math.toRadians(-twist));
        settings.setTwistMaxAngle((float) Math.toRadians(twist));
        settings.setPlaneHalfConeAngle((float) Math.toRadians(swingY));
        settings.setNormalHalfConeAngle((float) Math.toRadians(swingZ));

        Body torso = parts.get(1);
        Body body = parts.get(i);
        TwoBodyConstraint constraint = settings.create(torso, body);
        joints.add(constraint);
        physicsSystem.addConstraint(constraint);
    }

    public void updateLocalWorldCollision(Level level, BlockPos center) {
        BodyInterface bi = physicsSystem.getBodyInterface();
        // TODO: only rebuild when relevant
        for (Body body : localStaticCollision) {
            bi.removeBody(body.getId());
            bi.destroyBody(body.getId());
        }
        localStaticCollision.clear();

        BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos();

        BodyCreationSettings bcs = new BodyCreationSettings();
        bcs.setMotionType(EMotionType.Static);
        bcs.setObjectLayer(NON_MOVING_LAYER);
        bcs.setFriction(0.9f);
        bcs.setRestitution(0.0f);

        for (int dx = -COLLISION_RADIUS; dx <= COLLISION_RADIUS; dx++) {
            for (int dy = -COLLISION_RADIUS; dy <= COLLISION_RADIUS; dy++) {
                for (int dz = -COLLISION_RADIUS; dz <= COLLISION_RADIUS; dz++) {
                    pos.set(center.getX() + dx, center.getY() + dy, center.getZ() + dz);
                    BlockState state = level.getBlockState(pos);
                    if (state.isAir() || state.getFluidState().isSource()) continue;
                    if (isSurrounded(level, pos)) continue;

                    VoxelShape shape = state.getCollisionShape(level, pos);
                    if (shape.isEmpty()) continue;

                    for (AABB box : shape.toAabbs()) {
                        Vec3 halfExtents = new Vec3(
                                box.getXsize() / 2,
                                box.getYsize() / 2,
                                box.getZsize() / 2
                        );

                        if (halfExtents.getX() <= 0 || halfExtents.getY() <= 0 || halfExtents.getZ() <= 0) continue;

                        createBlockBody(bcs, box, halfExtents, pos);
                    }
                }
            }
        }
    }

    private void createBlockBody(BodyCreationSettings bcs, AABB box, Vec3 halfExtents, BlockPos.MutableBlockPos pos) {
        bcs.setShape(new BoxShape(halfExtents));
        bcs.setPosition(
                (pos.getX() + (box.minX + box.getXsize()) / 2f),
                (pos.getY() + (box.minY + box.getYsize()) / 2f),
                (pos.getZ() + (box.minZ + box.getZsize()) / 2f)
        );

        BodyInterface bi = physicsSystem.getBodyInterface();
        Body body = bi.createBody(bcs);
        bi.addBody(body, EActivation.DontActivate);
        localStaticCollision.add(body);
    }

    private boolean isSurrounded(Level level, BlockPos pos) {
        for (Direction dir : Direction.values()) {
            BlockPos neighbor = pos.relative(dir);
            BlockState neighborState = level.getBlockState(neighbor);
            if (neighborState.canBeReplaced() || neighborState.getCollisionShape(level, neighbor).isEmpty())
                return false;
        }
        return true;
    }

    public Body getPart(int i) {
        return parts.get(i);
    }

    public void addUniformVelocity(Vec3 delta) {
        for (Body body : parts) {
            body.addForce(delta);
        }
    }

    public void destroy() {
        BodyInterface bi = physicsSystem.getBodyInterface();
        for (Body body : localStaticCollision) {
            bi.removeBody(body.getId());
            bi.destroyBody(body.getId());
        }
        for (Body body : parts) {
            bi.removeBody(body.getId());
            bi.destroyBody(body.getId());
        }
        for (Constraint joint : joints) {
            physicsSystem.removeConstraint(joint);
        }
    }
}
