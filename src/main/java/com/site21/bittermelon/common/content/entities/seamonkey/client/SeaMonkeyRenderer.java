package com.site21.bittermelon.common.content.entities.seamonkey.client;

import com.site21.bittermelon.Bittermelon;
import com.site21.bittermelon.common.content.entities.seamonkey.SeaMonkey;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.resources.ResourceLocation;

import java.util.List;

import static com.site21.bittermelon.client.event.LayerDefinitions.SEA_MONKEY_LAYER;

public class SeaMonkeyRenderer extends MobRenderer<SeaMonkey, LivingEntityRenderState, SeaMonkeyModel> {
    private static final ResourceLocation TEXTURE = Bittermelon.resource("textures/entity/sea_monkey/sea_monkey.png");
    private static final ResourceLocation APPENDAGES_1 = Bittermelon.resource("textures/entity/sea_monkey/sea_monkey_appendages_1.png");
    private static final ResourceLocation APPENDAGES_2 = Bittermelon.resource("textures/entity/sea_monkey/sea_monkey_appendages_2.png");
    private static final ResourceLocation APPENDAGES_3 = Bittermelon.resource("textures/entity/sea_monkey/sea_monkey_appendages_3.png");
    private static final ResourceLocation APPENDAGES_4 = Bittermelon.resource("textures/entity/sea_monkey/sea_monkey_appendages_4.png");

    public SeaMonkeyRenderer(EntityRendererProvider.Context context) {
        super(context, new SeaMonkeyModel(context.bakeLayer(SEA_MONKEY_LAYER)), 0.1f);
        addLayer(
                new AppendagesLayer<>(
                        this,
                        List.of(APPENDAGES_1, APPENDAGES_2, APPENDAGES_3, APPENDAGES_4),
                        SeaMonkeyModel::getAppendages,
                        RenderType::entityCutout,
                        true
                )
        );
    }

    @Override
    public ResourceLocation getTextureLocation(LivingEntityRenderState renderState) {
        return TEXTURE;
    }

    @Override
    public LivingEntityRenderState createRenderState() {
        return new LivingEntityRenderState();
    }
}
