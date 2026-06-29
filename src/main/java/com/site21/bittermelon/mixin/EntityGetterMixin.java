package com.site21.bittermelon.mixin;

import com.google.common.collect.ImmutableList;
import com.site21.bittermelon.common.content.entities.cage.Cage;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.EntityGetter;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import javax.annotation.Nullable;
import java.util.List;
import java.util.function.Predicate;

@Mixin(EntityGetter.class)
public interface EntityGetterMixin {
    @Inject(method = "getEntityCollisions", at = @At("HEAD"), cancellable = true)
    default void overrideGetEntityCollisions(@Nullable Entity source, AABB testArea, CallbackInfoReturnable<List<VoxelShape>> cir) {
        if (testArea.getSize() < 1.0E-7) {
            cir.setReturnValue(List.of());
            return;
        }

        EntityGetter self = (EntityGetter) this;
        Predicate<Entity> predicate = source == null
                ? net.minecraft.world.entity.EntitySelector.CAN_BE_COLLIDED_WITH
                : net.minecraft.world.entity.EntitySelector.NO_SPECTATORS.and(source::canCollideWith);

        List<Entity> list = self.getEntities(source, testArea.inflate(1.0E-7), predicate);
        if (list.isEmpty()) {
            cir.setReturnValue(List.of());
            return;
        }

        ImmutableList.Builder<VoxelShape> builder = ImmutableList.builder();
        for (Entity e : list) {
            if (e instanceof Cage cage) {
                builder.addAll(cage.getCollisionShapes(source));
            } else {
                builder.add(Shapes.create(e.getBoundingBox()));
            }
        }

        cir.setReturnValue(builder.build());
    }
}
