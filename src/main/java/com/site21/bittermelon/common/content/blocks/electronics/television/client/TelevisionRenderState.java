package com.site21.bittermelon.common.content.blocks.electronics.television.client;

import com.site21.bittermelon.common.content.blocks.electronics.television.Media;
import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.core.Holder;

public class TelevisionRenderState extends BlockEntityRenderState {
    boolean powered;
    int rotation;
    boolean standing;
    Holder<Media> media;
}
