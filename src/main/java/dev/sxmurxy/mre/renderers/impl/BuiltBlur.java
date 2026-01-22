package dev.sxmurxy.mre.renderers.impl;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.Framebuffer;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.BuiltBuffer;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexFormats;

import org.joml.Matrix4f;

import com.mojang.blaze3d.systems.RenderPass;
import com.mojang.blaze3d.vertex.VertexFormat.DrawMode;

import dev.sxmurxy.mre.builders.states.QuadColorState;
import dev.sxmurxy.mre.builders.states.QuadRadiusState;
import dev.sxmurxy.mre.builders.states.SizeState;
import dev.sxmurxy.mre.renderers.IRenderer;
import dev.sxmurxy.mre.utils.BufferRenderer;
import dev.sxmurxy.mre.utils.CRenderPipelines;

public record BuiltBlur(
        SizeState size,
        QuadRadiusState radius,
        QuadColorState color,
        float smoothness,
        float blurRadius
    ) implements IRenderer {

    @Override
    public void render(Matrix4f matrix, float x, float y, float z) {
        Framebuffer fbo = MinecraftClient.getInstance().getFramebuffer();

        float width = this.size.width(), height = this.size.height();
		
		BufferBuilder builder = Tessellator.getInstance().begin(DrawMode.QUADS, VertexFormats.POSITION_COLOR);
        builder.vertex(matrix, x, y, z).color(this.color.color1());
        builder.vertex(matrix, x, y + height, z).color(this.color.color2());
        builder.vertex(matrix, x + width, y + height, z).color(this.color.color3());
        builder.vertex(matrix, x + width, y, z).color(this.color.color4());

        BuiltBuffer buffer = builder.end();
        RenderPass renderPass = BufferRenderer.uploadBuffer(buffer);

        renderPass.setPipeline(CRenderPipelines.BLUR_PIPLINE);

        renderPass.setUniform("Size", width, height);
        renderPass.setUniform("Radius", this.radius.radius1(), this.radius.radius2(), 
            this.radius.radius3(), this.radius.radius4());
        renderPass.setUniform("Smoothness", this.smoothness);
        renderPass.setUniform("BlurRadius", this.blurRadius);
        renderPass.bindSampler("Sampler0", fbo.getColorAttachment()); // ??

        BufferRenderer.renderBuffer(buffer, renderPass);
    }

}