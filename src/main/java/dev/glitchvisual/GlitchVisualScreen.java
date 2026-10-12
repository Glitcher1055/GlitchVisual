package dev.glitchvisual;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;
import org.lwjgl.glfw.GLFW;
import java.util.ArrayList;
import java.util.List;

public final class GlitchVisualScreen extends Screen {
    private static final int RED = 0xFFE32636, BG = 0xF0101015, PANEL = 0xFF19191F;
    private static final int PANEL_HOVER = 0xFF29232A, MUTED = 0xFFAAAAB4;
    private final List<Feature> features = new ArrayList<>();
    private String category = "VISUALS";

    public GlitchVisualScreen() {
        super(Text.literal("GlitchVisual"));
        features.add(new Feature("Target ESP", "Highlight entities", "VISUALS"));
        features.add(new Feature("China Hat", "Cosmetic overlay", "VISUALS"));
        features.add(new Feature("Particles", "Red hit particles", "VISUALS"));
        features.add(new Feature("World Color", "Red world tint", "WORLD"));
        features.add(new Feature("Fog", "Custom fog effect", "WORLD"));
        features.add(new Feature("Target HUD", "Target information", "HUD"));
        features.add(new Feature("Watermark", "GlitchVisual label", "HUD"));
        features.add(new Feature("Hit Effects", "Combat visual effects", "COMBAT"));
    }

    @Override public boolean shouldPause() { return false; }

    @Override
    public void render(DrawContext ctx, int mouseX, int mouseY, float delta) {
        ctx.fill(0, 0, width, height, 0x99000000);
        int panelW = Math.min(470, width - 24), panelH = Math.min(300, height - 24);
        int x = (width - panelW) / 2, y = (height - panelH) / 2;
        ctx.fill(x, y, x + panelW, y + panelH, BG);
        ctx.fill(x, y, x + panelW, y + 3, RED);
        ctx.fill(x, y, x + 112, y + panelH, 0xFF141419);
        ctx.drawTextWithShadow(textRenderer, Text.literal("GLITCH"), x + 14, y + 14, RED);
        ctx.drawTextWithShadow(textRenderer, Text.literal("VISUAL"), x + 61, y + 14, 0xFFFFFFFF);
        ctx.drawTextWithShadow(textRenderer, Text.literal("FABRIC / 1.21.11"), x + 14, y + 31, MUTED);

        String[] cats = {"VISUALS", "COMBAT", "WORLD", "HUD"};
        for (int i = 0; i < cats.length; i++) {
            int cy = y + 61 + i * 29; boolean active = category.equals(cats[i]);
            if (active) { ctx.fill(x + 8, cy - 4, x + 104, cy + 18, 0xFF3A171D); ctx.fill(x + 8, cy - 4, x + 11, cy + 18, RED); }
            ctx.drawTextWithShadow(textRenderer, Text.literal(cats[i]), x + 19, cy + 3, active ? 0xFFFFFFFF : MUTED);
        }
        int contentX = x + 128;
        ctx.drawTextWithShadow(textRenderer, Text.literal(category), contentX, y + 17, 0xFFFFFFFF);
        ctx.drawTextWithShadow(textRenderer, Text.literal("Click a feature to toggle"), contentX, y + 32, MUTED);
        int rowY = y + 57, shown = 0;
        for (Feature f : features) {
            if (!f.category.equals(category)) continue;
            int ry = rowY + shown * 39;
            boolean hover = mouseX >= contentX && mouseX <= x + panelW - 13 && mouseY >= ry && mouseY <= ry + 31;
            ctx.fill(contentX, ry, x + panelW - 13, ry + 31, hover ? PANEL_HOVER : PANEL);
            ctx.drawTextWithShadow(textRenderer, Text.literal(f.name), contentX + 9, ry + 5, 0xFFFFFFFF);
            ctx.drawTextWithShadow(textRenderer, Text.literal(f.description), contentX + 9, ry + 17, MUTED);
            int sx = x + panelW - 47, sy = ry + 9;
            ctx.fill(sx, sy, sx + 25, sy + 13, f.enabled ? RED : 0xFF45454D);
            int knobX = f.enabled ? sx + 14 : sx + 2;
            ctx.fill(knobX, sy + 2, knobX + 9, sy + 11, 0xFFFFFFFF);
            shown++;
        }
        ctx.drawTextWithShadow(textRenderer, Text.literal("RIGHT SHIFT  •  CLOSE"), x + 14, y + panelH - 19, MUTED);
        super.render(ctx, mouseX, mouseY, delta);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (button != 0) return super.mouseClicked(mouseX, mouseY, button);
        int panelW = Math.min(470, width - 24), panelH = Math.min(300, height - 24);
        int x = (width - panelW) / 2, y = (height - panelH) / 2;
        String[] cats = {"VISUALS", "COMBAT", "WORLD", "HUD"};
        for (int i = 0; i < cats.length; i++) {
            int cy = y + 61 + i * 29;
            if (mouseX >= x + 8 && mouseX <= x + 108 && mouseY >= cy - 4 && mouseY <= cy + 18) { category = cats[i]; return true; }
        }
        int contentX = x + 128, rowY = y + 57, shown = 0;
        for (Feature f : features) {
            if (!f.category.equals(category)) continue;
            int ry = rowY + shown * 39;
            if (mouseX >= contentX && mouseX <= x + panelW - 13 && mouseY >= ry && mouseY <= ry + 31) { f.enabled = !f.enabled; return true; }
            shown++;
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (keyCode == GLFW.GLFW_KEY_ESCAPE || keyCode == GLFW.GLFW_KEY_RIGHT_SHIFT) { close(); return true; }
        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    private static final class Feature {
        final String name, description, category; boolean enabled;
        Feature(String name, String description, String category) { this.name = name; this.description = description; this.category = category; }
    }
}
