package dev.sxmurxy.mre.renderers.impl;

import org.joml.Matrix4f;

import com.mojang.blaze3d.systems.RenderPass;
import com.mojang.blaze3d.vertex.VertexFormat.DrawMode;
import dev.sxmurxy.mre.msdf.MsdfFont;
import dev.sxmurxy.mre.providers.ColorProvider;
import dev.sxmurxy.mre.renderers.IRenderer;
import dev.sxmurxy.mre.utils.BufferRenderer;
import dev.sxmurxy.mre.utils.CRenderPipelines;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.BuiltBuffer;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexFormats;

public record BuiltText(
        MsdfFont font,
        String text,
    	float size,
        float thickness,
        int color,
		float smoothness,
        float spacing,
		int outlineColor,
		float outlineThickness
    ) implements IRenderer {
	
	@Override
    public void render(Matrix4f matrix, float x, float y, float z) {
		BufferBuilder builder = Tessellator.getInstance().begin(DrawMode.QUADS, VertexFormats.POSITION_TEXTURE_COLOR);
		this.font.applyGlyphs(matrix, builder, this.text, this.size,
			(this.thickness + this.outlineThickness * 0.5f) * 0.5f * this.size, this.spacing,
				x, y + this.font.getMetrics().baselineHeight() * this.size, z, this.color);
		
		BuiltBuffer buffer = builder.end();
        RenderPass renderPass = BufferRenderer.uploadBuffer(buffer);

		renderPass.setPipeline(CRenderPipelines.MSDF_FONT_PIPLINE);

		boolean outlineEnabled = (this.outlineThickness > 0.0f);
		renderPass.setUniform("Range", this.font.getAtlas().range());
		renderPass.setUniform("Thickness", this.thickness);
		renderPass.setUniform("Smoothness", this.smoothness);
		renderPass.setUniform("Outline", outlineEnabled ? 1 : 0);

		if (outlineEnabled) {
			renderPass.setUniform("OutlineThickness", this.outlineThickness);
			float[] outlineComponents = ColorProvider.normalize(this.outlineColor);
			renderPass.setUniform("OutlineColor", outlineComponents[0], outlineComponents[1], 
				outlineComponents[2], outlineComponents[3]);
		}

		renderPass.bindSampler("Sampler0", this.font.getGlTexture());

		BufferRenderer.renderBuffer(buffer, renderPass);
	}

}