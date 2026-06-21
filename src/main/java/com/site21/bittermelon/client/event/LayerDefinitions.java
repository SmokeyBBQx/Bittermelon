package com.site21.bittermelon.client.event;

import com.site21.bittermelon.Bittermelon;
import com.site21.bittermelon.common.content.blocks.electronics.largeslidingdoor.client.LargeSlidingDoorModel;
import com.site21.bittermelon.common.content.entities.chicken.client.ChickenModel;
import com.site21.bittermelon.common.content.entities.scp025fr.client.SCP025FRBodyModel;
import com.site21.bittermelon.common.content.entities.scp025fr.client.SCP025FRModel;
import com.site21.bittermelon.common.content.entities.scp025fr.client.SCP025FRTailModel;
import com.site21.bittermelon.common.content.entities.scp131.client.SCP131Model;
import com.site21.bittermelon.common.content.entities.scp1507.client.SCP1507Model;
import com.site21.bittermelon.common.content.entities.scp548.client.SCP548Model;
import com.site21.bittermelon.common.content.entities.scp650.client.SCP650Model;
import com.site21.bittermelon.common.content.entities.scp718.client.SCP718Model;
import com.site21.bittermelon.common.content.entities.scp718.client.SCP718SmallModel;
import com.site21.bittermelon.common.content.entities.scp815snake.client.SCP815SnakeModel;
import com.site21.bittermelon.common.content.entities.scp939.client.SCP939Model;
import com.site21.bittermelon.common.content.entities.seamonkey.client.SeaMonkeyModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.resources.Identifier;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import org.jetbrains.annotations.NotNull;

@EventBusSubscriber(modid = Bittermelon.MOD_ID, value = Dist.CLIENT)
public class LayerDefinitions {
    public static final ModelLayerLocation SCP_131_LAYER = new ModelLayerLocation(
            Identifier.fromNamespaceAndPath(Bittermelon.MOD_ID, "scp_131"),
            "main"
    );

    public static final ModelLayerLocation SCP_650_LAYER = new ModelLayerLocation(
            Identifier.fromNamespaceAndPath(Bittermelon.MOD_ID, "scp_650"),
            "main"
    );

    public static final ModelLayerLocation SCP_1507_LAYER = new ModelLayerLocation(
            Identifier.fromNamespaceAndPath(Bittermelon.MOD_ID, "scp_1507"),
            "main"
    );

    public static final ModelLayerLocation SCP_939_LAYER = new ModelLayerLocation(
            Identifier.fromNamespaceAndPath(Bittermelon.MOD_ID, "scp_939"),
            "main"
    );

    public static final ModelLayerLocation CHICKEN_LAYER = new ModelLayerLocation(
            Identifier.fromNamespaceAndPath(Bittermelon.MOD_ID, "chicken"),
            "main"
    );

    public static final ModelLayerLocation LARGE_SLIDING_DOOR_LAYER = new ModelLayerLocation(
            Identifier.fromNamespaceAndPath(Bittermelon.MOD_ID, "large_sliding_door"),
            "main"
    );

    public static final ModelLayerLocation SCP_548_LAYER = new ModelLayerLocation(
            Identifier.fromNamespaceAndPath(Bittermelon.MOD_ID, "scp_548"),
            "main"
    );

    public static final ModelLayerLocation SEA_MONKEY_LAYER = new ModelLayerLocation(
            Identifier.fromNamespaceAndPath(Bittermelon.MOD_ID, "sea_monkey"),
            "main"
    );

    public static final ModelLayerLocation SCP_718_LAYER = new ModelLayerLocation(
            Identifier.fromNamespaceAndPath(Bittermelon.MOD_ID, "scp_718"),
            "main"
    );

    public static final ModelLayerLocation SCP_718_SMALL_LAYER = new ModelLayerLocation(
            Identifier.fromNamespaceAndPath(Bittermelon.MOD_ID, "scp_718_small"),
            "main"
    );

    public static final ModelLayerLocation SCP_025_FR_LAYER = new ModelLayerLocation(
            Identifier.fromNamespaceAndPath(Bittermelon.MOD_ID, "scp_025_fr"),
            "main"
    );

    public static final ModelLayerLocation SCP_025_FR_TAIL_LAYER = new ModelLayerLocation(
            Identifier.fromNamespaceAndPath(Bittermelon.MOD_ID, "scp_025_fr_tail"),
            "main"
    );

    public static final ModelLayerLocation SCP_025_FR_BODY_LAYER = new ModelLayerLocation(
            Identifier.fromNamespaceAndPath(Bittermelon.MOD_ID, "scp_025_fr_body"),
            "main"
    );

    public static final ModelLayerLocation SCP_815_SNAKE_LAYER = new ModelLayerLocation(
            Identifier.fromNamespaceAndPath(Bittermelon.MOD_ID, "scp_815_snake"),
            "main"
    );

    @SubscribeEvent
    public static void registerLayerDefinitions(EntityRenderersEvent.@NotNull RegisterLayerDefinitions event) {
        event.registerLayerDefinition(SCP_131_LAYER, SCP131Model::createBodyLayer);
        event.registerLayerDefinition(SCP_650_LAYER, SCP650Model::createBodyLayer);
        event.registerLayerDefinition(SCP_1507_LAYER, SCP1507Model::createBodyLayer);
        event.registerLayerDefinition(SCP_939_LAYER, SCP939Model::createBodyLayer);
        event.registerLayerDefinition(CHICKEN_LAYER, ChickenModel::createBodyLayer);
        event.registerLayerDefinition(LARGE_SLIDING_DOOR_LAYER, LargeSlidingDoorModel::createBodyLayer);
        event.registerLayerDefinition(SCP_548_LAYER, SCP548Model::createBodyLayer);
        event.registerLayerDefinition(SEA_MONKEY_LAYER, SeaMonkeyModel::createBodyLayer);
        event.registerLayerDefinition(SCP_718_LAYER, SCP718Model::createBodyLayer);
        event.registerLayerDefinition(SCP_718_SMALL_LAYER, SCP718SmallModel::createBodyLayer);
        event.registerLayerDefinition(SCP_025_FR_LAYER, SCP025FRModel::createBodyLayer);
        event.registerLayerDefinition(SCP_025_FR_BODY_LAYER, SCP025FRBodyModel::createBodyLayer);
        event.registerLayerDefinition(SCP_025_FR_TAIL_LAYER, SCP025FRTailModel::createBodyLayer);
        event.registerLayerDefinition(SCP_815_SNAKE_LAYER, SCP815SnakeModel::createBodyLayer);
    }
}
