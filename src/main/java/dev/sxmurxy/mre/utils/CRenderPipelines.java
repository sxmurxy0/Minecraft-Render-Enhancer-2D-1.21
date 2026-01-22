package dev.sxmurxy.mre.utils;

import com.mojang.blaze3d.pipeline.BlendFunction;
import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.vertex.VertexFormat.DrawMode;

import dev.sxmurxy.mre.providers.ResourceProvider;
import net.minecraft.client.gl.RenderPipelines;
import net.minecraft.client.gl.UniformType;
import net.minecraft.client.render.VertexFormats;

public final class CRenderPipelines {

    public static final RenderPipeline.Snippet ROUNDED_QUAD_SNIPPET = RenderPipeline.builder(
        new RenderPipeline.Snippet[] {RenderPipelines.MATRICES_SNIPPET}
    ).withUniform("Size", UniformType.VEC2).withUniform("Radius", UniformType.VEC4).buildSnippet();
    public static final RenderPipeline.Snippet SMOOTHED_ROUNDED_QUAD_SNIPPET = RenderPipeline.builder(
        new RenderPipeline.Snippet[] {ROUNDED_QUAD_SNIPPET}
    ).withUniform("Smoothness", UniformType.FLOAT).buildSnippet();

    public static final RenderPipeline RECTANGLE_PIPLINE = RenderPipelines.register(
        RenderPipeline.builder(new RenderPipeline.Snippet[] {SMOOTHED_ROUNDED_QUAD_SNIPPET})
            .withLocation("pipeline/rectangle")
            .withVertexShader(ResourceProvider.getShaderIdentifier("rectangle"))
            .withFragmentShader(ResourceProvider.getShaderIdentifier("rectangle"))
            .withBlend(BlendFunction.TRANSLUCENT)
            .withVertexFormat(VertexFormats.POSITION_COLOR, DrawMode.QUADS).build()
    );
    public static final RenderPipeline BORDER_PIPLINE = RenderPipelines.register(
        RenderPipeline.builder(new RenderPipeline.Snippet[] {ROUNDED_QUAD_SNIPPET})
            .withLocation("pipeline/border")
            .withUniform("Smoothness", UniformType.VEC2).withUniform("Thickness", UniformType.FLOAT)
            .withVertexShader(ResourceProvider.getShaderIdentifier("border"))
            .withFragmentShader(ResourceProvider.getShaderIdentifier("border"))
            .withBlend(BlendFunction.TRANSLUCENT)
            .withVertexFormat(VertexFormats.POSITION_COLOR, DrawMode.QUADS).build()
    );
    public static final RenderPipeline TEXTURE_PIPLINE = RenderPipelines.register(
        RenderPipeline.builder(new RenderPipeline.Snippet[] {SMOOTHED_ROUNDED_QUAD_SNIPPET})
            .withLocation("pipeline/texture")
            .withSampler("Sampler0")
            .withVertexShader(ResourceProvider.getShaderIdentifier("texture"))
            .withFragmentShader(ResourceProvider.getShaderIdentifier("texture"))
            .withBlend(BlendFunction.TRANSLUCENT)
            .withVertexFormat(VertexFormats.POSITION_TEXTURE_COLOR, DrawMode.QUADS).build()
    );
    public static final RenderPipeline MSDF_FONT_PIPLINE = RenderPipelines.register(
        RenderPipeline.builder(new RenderPipeline.Snippet[] {RenderPipelines.MATRICES_SNIPPET})
            .withLocation("pipeline/msdf_font")
            .withUniform("Range", UniformType.FLOAT).withUniform("Thickness", UniformType.FLOAT)
            .withUniform("Smoothness", UniformType.FLOAT).withUniform("Outline", UniformType.INT)
            .withUniform("OutlineThickness", UniformType.FLOAT).withSampler("Sampler0")
            .withVertexShader(ResourceProvider.getShaderIdentifier("msdf_font"))
            .withFragmentShader(ResourceProvider.getShaderIdentifier("msdf_font"))
            .withBlend(BlendFunction.TRANSLUCENT)
            .withVertexFormat(VertexFormats.POSITION_TEXTURE_COLOR, DrawMode.QUADS).build()
    );
    public static final RenderPipeline BLUR_PIPLINE = RenderPipelines.register(
        RenderPipeline.builder(new RenderPipeline.Snippet[] {SMOOTHED_ROUNDED_QUAD_SNIPPET})
            .withLocation("pipeline/blur")
            .withUniform("BlurRadius", UniformType.FLOAT).withSampler("Sampler0")
            .withVertexShader(ResourceProvider.getShaderIdentifier("blur"))
            .withFragmentShader(ResourceProvider.getShaderIdentifier("blur"))
            .withBlend(BlendFunction.TRANSLUCENT)
            .withVertexFormat(VertexFormats.POSITION_COLOR, DrawMode.QUADS).build()
    );

}