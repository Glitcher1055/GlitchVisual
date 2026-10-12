package dev.glitchvisual;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

public class GlitchVisualScreen extends Screen {

    public GlitchVisualScreen() {
        super(Text.literal("GlitchVisual"));
    }

    @Override
    protected void init() {
        // Инициализация меню
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        context.fill(0, 0, this.width, this.height, 0xFF080808);

        context.fill(20, 20, this.width - 20, this.height - 20, 0xFF171717);

        context.fill(20, 20, this.width - 20, 55, 0xFF8B0000);

        context.drawCenteredTextWithShadow(
                this.textRenderer,
                Text.literal("GLITCH VISUAL"),
                this.width / 2,
                32,
                0xFFFFFFFF
        );

        context.drawTextWithShadow(
                this.textRenderer,
                Text.literal("Visual Client"),
                35,
                75,
                0xFFFF3333
        );

        context.drawTextWithShadow(
                this.textRenderer,
                Text.literal("Minecraft 1.21.11"),
                35,
                95,
                0xFFAAAAAA
        );

        context.drawTextWithShadow(
                this.textRenderer,
                Text.literal("Red and Black Interface"),
                35,
                115,
                0xFFFFFFFF
        );

        super.render(context, mouseX, mouseY, delta);
    }

    @Override
    public boolean shouldPause() {
        return false;
    }
}
