package com.site21.bittermelon.common.systems.medical.legacy.client.compartmentrenderers;

import com.site21.bittermelon.common.systems.medical.legacy.compartment.Compartment;
import com.site21.bittermelon.init.custom.Compartments;
import net.minecraft.core.Holder;

import java.util.HashMap;
import java.util.Map;

public class CompartmentRenderers {
    public static final Map<Holder<Compartment>, SpecialCompartmentRenderer> RENDERERS = new HashMap<>();

    public static void register() {
        RENDERERS.put(Compartments.HEAD, new FaceRenderer());
    }
}
