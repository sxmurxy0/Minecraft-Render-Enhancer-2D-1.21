# Minecraft-Render-Enhancer-2D-1.21
Simple system for rendering 2D objects in Minecraft 1.21. System supports basic shapes, blur, msdf fonts and objects transformations with `MatrixStack`.
## Usage example
``` java
public final class MinecraftRenderEnhancer implements ModInitializer {

	public static final String MOD_ID = "mre";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
    private static final Supplier<MsdfFont> BIKO_FONT = Suppliers.memoize(() -> MsdfFont.builder().atlas("biko").data("biko").build());

	@Override
	public void onInitialize() {
		HudRenderCallback.EVENT.register(this::render);
	}

	private void render(DrawContext context, RenderTickCounter tickCounter) {
		Matrix4f matrix = context.getMatrices().peek().getPositionMatrix();
      
        BuiltRectangle rectangle = Builder.rectangle()
            .size(new SizeState(70, 70))
            .color(new QuadColorState(Color.BLACK))
            .radius(new QuadRadiusState(6f, 0f, 20f, 35f))
            .smoothness(1.0f)
            .build();
        rectangle.render(matrix, 40, 40);
      
        BuiltBorder border = Builder.border()
            .size(new SizeState(70, 70))
            .color(new QuadColorState(Color.WHITE, Color.BLUE, Color.WHITE, Color.BLUE))
            .radius(new QuadRadiusState(6f, 0f, 20f, 35f))
            .thickness(2f)
            .smoothness(2f, 2f)
            .build();
        border.render(matrix, 40, 130);

        AbstractTexture abstractTexture = MinecraftClient.getInstance().getTextureManager()
            .getTexture(Identifier.ofVanilla("textures/entity/creeper/creeper.png"));
        BuiltTexture texture = Builder.texture()
            .size(new SizeState(70, 70))
            .radius(new QuadRadiusState(6f, 10f, 20f, 0f))
            .texture(0.125f, 0.25f, 0.125f, 0.25f, abstractTexture)
            .color(new QuadColorState(Color.WHITE, Color.GREEN, Color.GREEN, Color.WHITE))
            .build();
        texture.render(matrix, 40, 220);

        BuiltText text = Builder.text()
            .font(BIKO_FONT.get())
            .text("This is text render.")
            .color(Color.BLACK)
            .size(20f)
            .thickness(0.05f)
            .build();
        text.render(matrix, 140, 170);

        BuiltBlur blur = Builder.blur()
            .size(new SizeState(150, 100))
            .radius(new QuadRadiusState(12f))
            .blurRadius(12f)
            .smoothness(6f)
            .color(new QuadColorState(new Color(100, 100, 100)))
            .build();
        blur.render(matrix, 140, 50);
	}

}
```
<img width="1920" height="974" alt="usage" src="https://github.com/user-attachments/assets/6bdd692d-5e3a-4ea5-8160-a5bdd44d8ef9" />

## How to generate MSDF font
- Get `.ttf` file of the font you need
- Download last release of [generator](https://github.com/Chlumsky/msdf-atlas-gen) and [example](https://drive.google.com/file/d/1A-oywYiLUd2d72N-hJysZZfBMhrTBTVY/view?usp=drive_link)
- Move all files in one folder
- Edit font input file in `run.bat` and run it
- Other generator options you can find in original repository
## Useful links
- [About msdf fonts in Minecraft](https://yougame.biz/threads/301776/)
- [Msdf-atlas-gen](https://github.com/Chlumsky/msdf-atlas-gen)
- [GLSL-150](https://registry.khronos.org/OpenGL/specs/gl/GLSLangSpec.1.50.pdf)
