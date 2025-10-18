package com.site21.bittermelon.common.content.blocks.electronics.largeslidingdoor.client;

import net.minecraft.client.model.Model;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

import java.util.function.Function;

public class LargeSlidingDoorModel extends Model {
    private final ModelPart leftDoor;
    private final ModelPart rightDoor;

    public LargeSlidingDoorModel(ModelPart root) {
        super(root, RenderType::entitySolid);
        this.leftDoor = root.getChild("left_door");
        this.rightDoor = root.getChild("right_door");
    }

    public static @NotNull LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition frame = partdefinition.addOrReplaceChild("frame", CubeListBuilder.create().texOffs(0, 0).addBox(-21.0F, -48.0F, -2.0F, 42.0F, 3.0F, 4.0F, new CubeDeformation(0.0F))
                .texOffs(0, 57).addBox(21.0F, -48.0F, -2.0F, 3.0F, 48.0F, 4.0F, new CubeDeformation(0.0F))
                .texOffs(14, 57).addBox(-24.0F, -48.0F, -2.0F, 3.0F, 48.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 24.0F, 0.0F));

        PartDefinition leftDoor = partdefinition.addOrReplaceChild("left_door", CubeListBuilder.create().texOffs(46, 8).addBox(-10.5F, -37.0F, -1.0F, 21.0F, 45.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(-10.5F, 16.0F, 0.0F));

        PartDefinition rightDoor = partdefinition.addOrReplaceChild("right_door", CubeListBuilder.create().texOffs(0, 8).addBox(-10.5F, -21.0F, -1.0F, 21.0F, 45.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(10.5F, 0.0F, 0.0F));

        return LayerDefinition.create(meshdefinition, 128, 128);
    }

    public void setupAnim(float openness) {
        leftDoor.x = -openness * 24 - 10.5f;
        rightDoor.x = openness * 24 + 10.5f;
    }
}
