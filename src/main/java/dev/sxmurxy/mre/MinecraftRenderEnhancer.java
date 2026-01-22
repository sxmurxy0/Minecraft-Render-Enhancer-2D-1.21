package dev.sxmurxy.mre;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.RenderTickCounter;

public final class MinecraftRenderEnhancer implements ModInitializer {

	public static final String MOD_ID = "mre";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		HudRenderCallback.EVENT.register(this::render);
	}

	private void render(DrawContext context, RenderTickCounter tickCounter) {
		
	}

}