package dev.sxmurxy.mre.renderers.impl;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.Framebuffer;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.BuiltBuffer;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexFormats;

import java.util.OptionalInt;
import org.joml.Matrix4f;

import com.mojang.blaze3d.buffers.GpuBuffer;
import com.mojang.blaze3d.systems.RenderPass;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.textures.GpuTexture;
import com.mojang.blaze3d.textures.TextureFormat;
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

    private static GpuTexture TEMP_TEXTURE = null;

    private static void prepareTempTexture() {
        Framebuffer fbo = MinecraftClient.getInstance().getFramebuffer();
        if (TEMP_TEXTURE == null
                || TEMP_TEXTURE.getWidth(0) != fbo.textureWidth || TEMP_TEXTURE.getHeight(0) != fbo.textureHeight) {
            if (TEMP_TEXTURE != null) {
                TEMP_TEXTURE.close();
            }
            
            TEMP_TEXTURE = RenderSystem.getDevice().createTexture((String) null, 
                TextureFormat.RGBA8, fbo.textureWidth, fbo.textureHeight, 1);
        }

        RenderSystem.ShapeIndexBuffer shapeIndexBuffer = RenderSystem.getSequentialBuffer(DrawMode.QUADS);
        GpuBuffer indexBuffer = shapeIndexBuffer.getIndexBuffer(6);
        GpuBuffer vertexBuffer = RenderSystem.getQuadVertexBuffer();
        RenderPass renderPass = RenderSystem.getDevice().createCommandEncoder().createRenderPass(TEMP_TEXTURE, OptionalInt.empty());
        
        renderPass.setPipeline(CRenderPipelines.BLIT_PIPLINE);
        renderPass.setVertexBuffer(0, vertexBuffer);
        renderPass.setIndexBuffer(indexBuffer, shapeIndexBuffer.getIndexType());
        renderPass.bindSampler("InSampler", fbo.getColorAttachment());
        renderPass.drawIndexed(0, 6);
        renderPass.close();
    }

    @Override
    public void render(Matrix4f matrix, float x, float y, float z) {
        prepareTempTexture();

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
        renderPass.bindSampler("Sampler0", TEMP_TEXTURE);

        BufferRenderer.renderBuffer(buffer, renderPass);
    }

}