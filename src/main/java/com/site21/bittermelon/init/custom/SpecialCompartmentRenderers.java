package com.site21.bittermelon.init.custom;

import com.site21.bittermelon.Bittermelon;
import com.site21.bittermelon.common.systems.medical.client.compartmentrenderers.FaceRenderer;
import com.site21.bittermelon.common.systems.medical.client.compartmentrenderers.SpecialCompartmentRenderer;
import com.site21.bittermelon.init.neoforge.BitterRegistries;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class SpecialCompartmentRenderers {
    public static final DeferredRegister<SpecialCompartmentRenderer> SPECIAL_COMPARTMENT_RENDERERS =
            DeferredRegister.create(BitterRegistries.COMPARTMENT_RENDERER, Bittermelon.MOD_ID);

    public static final DeferredHolder<SpecialCompartmentRenderer, SpecialCompartmentRenderer> FACE_RENDERER =
            SPECIAL_COMPARTMENT_RENDERERS.register("face_renderer", FaceRenderer::new);
}
