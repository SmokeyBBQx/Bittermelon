package com.site21.bittermelon.client.render;

import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.site21.bittermelon.Bittermelon;
import net.neoforged.neoforge.client.stencil.StencilFunction;
import net.neoforged.neoforge.client.stencil.StencilOperation;
import net.neoforged.neoforge.client.stencil.StencilPerFaceTest;
import net.neoforged.neoforge.client.stencil.StencilTest;

import static net.minecraft.client.renderer.RenderPipelines.GUI_SNIPPET;
import static net.minecraft.client.renderer.RenderPipelines.GUI_TEXTURED_SNIPPET;

public class BitterRenderPipelines {
    public static final RenderPipeline STENCIL_TEST_TEXTURED = RenderPipeline.builder(GUI_TEXTURED_SNIPPET)
            .withLocation(Bittermelon.resource("pipeline/stencil_test_textured"))
            .withStencilTest(
                    new StencilTest(
                            new StencilPerFaceTest(
                                    StencilOperation.KEEP,
                                    StencilOperation.KEEP,
                                    StencilOperation.REPLACE,
                                    StencilFunction.ALWAYS
                            ),
                            StencilTest.DEFAULT_READ_MASK,
                            StencilTest.DEFAULT_WRITE_MASK,
                            1))
            .build();

    public static final RenderPipeline STENCIL_TEST = RenderPipeline.builder(GUI_SNIPPET)
            .withLocation(Bittermelon.resource("pipeline/stencil_test"))
            .withStencilTest(
                    new StencilTest(
                            new StencilPerFaceTest(
                                    StencilOperation.REPLACE,
                                    StencilOperation.REPLACE,
                                    StencilOperation.REPLACE,
                                    StencilFunction.GREATER
                            ),
                            StencilTest.DEFAULT_READ_MASK,
                            StencilTest.DEFAULT_WRITE_MASK,
                            2))
            .build();

    public static final RenderPipeline STENCIL_FOG = RenderPipeline.builder(GUI_SNIPPET)
            .withLocation(Bittermelon.resource("pipeline/stencil_fog"))
            .withStencilTest(
                    new StencilTest(
                            new StencilPerFaceTest(
                                    StencilOperation.REPLACE,
                                    StencilOperation.REPLACE,
                                    StencilOperation.REPLACE,
                                    StencilFunction.EQUAL
                            ),
                            StencilTest.DEFAULT_READ_MASK,
                            StencilTest.DEFAULT_WRITE_MASK,
                            0
                    ))
            .build();
}
