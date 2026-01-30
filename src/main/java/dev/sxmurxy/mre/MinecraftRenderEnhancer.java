package dev.sxmurxy.mre;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.HudLayerRegistrationCallback;
import net.fabricmc.fabric.api.client.rendering.v1.IdentifiedLayer;
import net.fabricmc.fabric.impl.client.rendering.WrappedLayer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.util.Identifier;

public final class MinecraftRenderEnhancer implements ModInitializer {

	public static final String MOD_ID = "mre";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
        HudLayerRegistrationCallback.EVENT.register((layeredDrawer) -> {
            layeredDrawer.attachLayerAfter(IdentifiedLayer.MISC_OVERLAYS, new WrappedLayer(
                Identifier.of(MOD_ID, "test-hud"),
                this::render
            ));
        });
	}
    

	private void render(DrawContext context, RenderTickCounter tickCounter) {
		
	}

}