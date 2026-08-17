package com.site21.bittermelon.common.content.entities.ragdoll;

import com.jme3.bullet.PhysicsSpace;
import com.jme3.bullet.collision.PhysicsCollisionEvent;
import com.jme3.bullet.collision.PhysicsCollisionListener;
import com.jme3.bullet.collision.shapes.BoxCollisionShape;
import com.jme3.bullet.joints.Constraint;
import com.jme3.bullet.joints.Point2PointJoint;
import com.jme3.bullet.objects.PhysicsRigidBody;
import com.jme3.math.Vector3f;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jspecify.annotations.NonNull;

import java.util.ArrayList;
import java.util.List;

public class Ragdoll implements PhysicsCollisionListener {
    private final PhysicsSpace physicsSpace;
    private final List<PhysicsRigidBody> parts = new ArrayList<>();
    private final List<Constraint> joints = new ArrayList<>();
    private final List<PhysicsRigidBody> localStaticCollision = new ArrayList<>();
    private static final int COLLISION_RADIUS = 3;

    public Ragdoll(PhysicsSpace physicsSpace, Vector3f pos) {
        this.physicsSpace = physicsSpace;
        parts.add(makeBody(new BoxCollisionShape(0.25f, 0.25f, 0.25f), 10.0f));
        parts.add(makeBody(new BoxCollisionShape(0.24f, 0.375f, 0.129f), 30.0f));
        parts.add(makeBody(new BoxCollisionShape(0.15f, 0.45f, 0.15f), 1.0f));
        parts.add(makeBody(new BoxCollisionShape(0.15f, 0.45f, 0.15f), 1.0f));
        parts.add(makeBody(new BoxCollisionShape(0.15f, 0.45f, 0.15f), 1.0f));
        parts.add(makeBody(new BoxCollisionShape(0.15f, 0.45f, 0.15f), 1.0f));

        positionParts(pos);

        for (PhysicsRigidBody body : parts) {
            physicsSpace.addCollisionObject(body);
        }

        connectJoints();
    }

    private PhysicsRigidBody makeBody(BoxCollisionShape shape, float mass) {
        PhysicsRigidBody body = new PhysicsRigidBody(shape, mass);
        body.setCcdMotionThreshold(0.1f);
        body.setCcdSweptSphereRadius(0.1f);
        body.setCollisionGroup(2);
        body.setCollideWithGroups(3);
        return body;
    }

    private void positionParts(Vector3f origin) {
        parts.get(1).setPhysicsLocation(new Vector3f(origin));
        parts.get(0).setPhysicsLocation(new Vector3f(origin).add(0.0f, 0.64f, 0.0f));
        parts.get(2).setPhysicsLocation(new Vector3f(origin).add(-0.369f, 0.025f, 0.0f));
        parts.get(3).setPhysicsLocation(new Vector3f(origin).add(0.369f, 0.025f, 0.0f));
        parts.get(4).setPhysicsLocation(new Vector3f(origin).add(-0.13f, -0.77f, 0.0f));
        parts.get(5).setPhysicsLocation(new Vector3f(origin).add(0.13f, -0.77f, 0.0f));
    }

    private void connectJoints() {
        // neck
        addJoint(0, new Vector3f(0, 0.41f, 0), new Vector3f(0.0f, -0.23f, 0.0f));

        // shoulders
        addJoint(2, new Vector3f(-0.369f, 0.375f, 0.0f), new Vector3f(0.0f, 0.35f, 0.0f));
        addJoint(3, new Vector3f(0.369f, 0.375f, 0.0f), new Vector3f(0.0f, 0.35f, 0.0f));

        // hips
        addJoint(4, new Vector3f(-0.13f, -0.42f, 0.0f), new Vector3f(0.0f, 0.35f, 0.0f));
        addJoint(5, new Vector3f(0.13f, -0.42f, 0.0f), new Vector3f(0.0f, 0.35f, 0.0f));
    }

    private void addJoint(int i, Vector3f torsoPoint, Vector3f partPoint) {
        PhysicsRigidBody torso = parts.get(1);
        PhysicsRigidBody body = parts.get(i);

        Point2PointJoint joint = new Point2PointJoint(torso, body, torsoPoint, partPoint);
        joint.setDamping(1.0f);
        joint.setCollisionBetweenLinkedBodies(false);
        joints.add(joint);
        physicsSpace.addJoint(joint);
    }

    public void updateLocalWorldCollision(Level level, BlockPos center) {
        // TODO: only rebuild when relevant
        for (PhysicsRigidBody body : localStaticCollision) {
            physicsSpace.removeCollisionObject(body);
        }
        localStaticCollision.clear();

        BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos();

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
                        Vector3f halfExtents = new Vector3f(
                                (float) (box.getXsize() / 2),
                                (float) (box.getYsize() / 2),
                                (float) (box.getZsize() / 2)
                        );

                        if (halfExtents.x <= 0f || halfExtents.y <= 0f || halfExtents.z <= 0f) continue;

                        PhysicsRigidBody body = createBlockBody(box, halfExtents, pos);
                        physicsSpace.addCollisionObject(body);
                        localStaticCollision.add(body);
                    }
                }
            }
        }
    }

    private static @NonNull PhysicsRigidBody createBlockBody(AABB box, Vector3f halfExtents, BlockPos.MutableBlockPos pos) {
        PhysicsRigidBody body = new PhysicsRigidBody(new BoxCollisionShape(halfExtents), 0f);
        body.setPhysicsLocation(
                new Vector3f(
                        (float) (pos.getX() + (box.minX + box.getXsize()) / 2f),
                        (float) (pos.getY() + (box.minY + box.getYsize()) / 2f),
                        (float) (pos.getZ() + (box.minZ + box.getZsize()) / 2f)
                )
        );
        body.setFriction(0.9f);
        body.setRestitution(0.0f);
        return body;
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

    public PhysicsRigidBody getPart(int i) {
        return parts.get(i);
    }

    public void setUserObject(Object user) {
        for (PhysicsRigidBody body : parts) {
            body.setUserObject(user);
        }
    }

    public void addUniformVelocity(Vector3f delta) {
        Vector3f v = new Vector3f();

        for (PhysicsRigidBody body : parts) {
            body.activate(true);
            body.getLinearVelocity(v);
            body.setLinearVelocity(v.add(delta));
        }
    }

    public void destroy() {
        for (PhysicsRigidBody body : localStaticCollision) {
            physicsSpace.removeCollisionObject(body);
        }
        for (PhysicsRigidBody body : parts) {
            physicsSpace.removeCollisionObject(body);
        }
        for (Constraint joint : joints) {
            physicsSpace.removeJoint(joint);
        }
    }

    @Override
    public void collision(PhysicsCollisionEvent event) {

    }
}
