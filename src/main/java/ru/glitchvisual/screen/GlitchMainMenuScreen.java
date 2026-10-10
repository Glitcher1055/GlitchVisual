package ru.glitchvisual.screen;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.MultiplayerScreen;
import net.minecraft.client.gui.screen.OptionsScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.world.SelectWorldScreen;
import net.minecraft.text.Text;
import net.minecraft.util.Util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Random;

public class GlitchMainMenuScreen extends Screen {
    private static final float BOOT_TIME = 2.2f;
    private static final int COUNT = 110;
    private final long openTime;
    private final float[] px = new float[COUNT];
    private final float[] py = new float[COUNT];
    private final float[] sp = new float[COUNT];
    private final int[] sz = new int[COUNT];
    private final boolean[] wh = new boolean[COUNT];

    public GlitchMainMenuScreen() {
        super(Text.literal("GlitchVisual"));
        this.openTime = Util.getMeasurableTimeMs();
        Random rnd = new Random(42);
        for (int i = 0; i < COUNT; i++) {
            px[i] = rnd.nextFloat();
            py[i] = rnd.nextFloat();
            sp[i] = 0.01f + rnd.nextFloat() * 0.03f;
            sz[i] = rnd.nextFloat() < 0.3f ? 2 : 1;
            wh[i] = rnd.nextFloat() < 0.4f;
        }
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        float t = (Util.getMeasurableTimeMs() - openTime) / 1000f;
        int w = this.width, h = this.height;

        context.fillGradient(0, 0, w, h, 0xFF040208, 0xFF150B26);

        if (t < BOOT_TIME) {
            renderBoot(context, t, w, h);
            return;
        }
        float fade = Math.min(1f, (t - BOOT_TIME) / 0.6f);

        renderBeam(context, w, h, t);
        renderParticles(context, w, h, t);

        if ((t % 4.7f) < 0.12f) renderGlitch(context, w, h, t);

        renderContent(context, mouseX, mouseY, w, h);

        if (fade < 1f) context.fill(0, 0, w, h, ((int) ((1 - fade) * 255) << 24));
    }

    private void renderBoot(DrawContext context, float t, int w, int h) {
        context.fill(0, 0, w, h, 0xFF000000);
        int cx = w / 2, cy = h / 2 - 10;
        fillDiamond(context, cx - 7, cy, 9, 0xFF8B5CF6);
        fillDiamond(context, cx + 7, cy, 9, 0xFFA78BFA);
        int progress = (int) (Math.min(1f, t / BOOT_TIME) * 64);
        context.fill(cx - 32, cy + 34, cx - 32 + progress, cy + 36, 0xFFCCCCCC);
    }

    private void renderBeam(DrawContext context, int w, int h, float t) {
        for (int y = 0; y < h; y += 4) {
            float p = y / (float) h;
            int x = (int) (w * 0.12f + p * w * 0.35f + Math.sin(t * 0.5f) * 8);
            int bw = (int) (90 + p * 160);
            int a = (int) (60 * (1 - p * 0.7f));
            if (a > 0) context.fill(x, y, x + bw, y + 4, (a << 24) | 0x8B5CF6);
        }
    }

    private void renderParticles(DrawContext context, int w, int h, float t) {
        for (int i = 0; i < COUNT; i++) {
            float fx = ((px[i] + t * sp[i]) % 1f + 1f) % 1f;
            float fy = ((py[i] - t * sp[i] * 0.35f) % 1f + 1f) % 1f;
            int x = (int) (fx * w), y = (int) (fy * h);
            int a = Math.max(40, Math.min(230, (int) (140 + 90 * Math.sin(t * 1.8 + i * 1.7))));
            context.fill(x, y, x + sz[i], y + sz[i], (a << 24) | (wh[i] ? 0xE8E6F5 : 0x8B5CF6));
        }
    }

    private void renderGlitch(DrawContext context, int w, int h, float t) {
        Random r = new Random((long) (t * 10));
        for (int i = 0; i < 4; i++) {
            int y = r.nextInt(h), hh = 1 + r.nextInt(3);
            context.fill(0, y, w, y + hh, 0x1AFFFFFF);
        }
    }

    private void renderContent(DrawContext context, int mx, int my, int w, int h) {
        LocalDateTime now = LocalDateTime.now();
        String day = now.format(DateTimeFormatter.ofPattern("EEEE", new Locale("ru")));
        day = day.substring(0, 1).toUpperCase() + day.substring(1);
        String clock = now.format(DateTimeFormatter.ofPattern("HH:mm")) + ", " + day;
        context.drawText(textRenderer, clock, w - 8 - textRenderer.getWidth(clock), 10, 0xFFE8E6F5, true);

        int hy = h / 2 - 78;
        Text welcome = Text.literal("Добро пожаловать в ");
        Text logo = Text.literal("GlitchVisual!").styled(s -> s.withItalic(true).withBold(true));
        int w1 = textRenderer.getWidth(welcome), w2 = textRenderer.getWidth(logo);
        int sx = w / 2 - (w1 + 26 + w2) / 2;
        context.drawText(textRenderer, welcome, sx, hy, 0xFFFFFFFF, true);
        fillDiamond(context, sx + w1 + 9, hy + 4, 6, 0xFF8B5CF6);
        fillDiamond(context, sx + w1 + 19, hy + 4, 6, 0xFFA78BFA);
        context.drawText(textRenderer, logo, sx + w1 + 26, hy, 0xFFB9A3FF, true);
        Text sub = Text.literal("Во что сегодня играем?");
        context.drawText(textRenderer, sub, w / 2 - textRenderer.getWidth(sub) / 2, hy + 16, 0xFF9A93B8, false);

        int hovered = buttonAt(mx, my);
        String[] labels = {"Настройки", "Одиночная игра", "Сетевая игра"};
        for (int i = 0; i < 3; i++) {
            int cx = w / 2 + (i - 1) * 74, cy = h / 2 + 24;
            boolean hov = hovered == i;
            fillCircle(context, cx, cy, 24, hov ? 0xFF463867 : 0x55251B3D);
            fillRing(context, cx, cy, 24, 23, hov ? 0x66FFFFFF : 0x22FFFFFF);
            int col = hov ? 0xFFFFFFFF : 0xFFD9D4EA;
            if (i == 0) drawGear(context, cx, cy, col);
            else if (i == 1) drawPerson(context, cx, cy, col);
            else drawDots(context, cx, cy, col);
            if (hov) {
                int lw = textRenderer.getWidth(labels[i]);
                context.drawText(textRenderer, labels[i], cx - lw / 2, cy + 32, 0xFFFFFFFF, true);
            }
        }

        String footer = "GlitchVisual 1.0 • PS5-style UI • Minecraft 1.21.11";
        context.drawText(textRenderer, footer, w / 2 - textRenderer.getWidth(footer) / 2, h - 14, 0x66E8E6F5, false);
    }

    private int buttonAt(int mx, int my) {
        int cy = this.height / 2 + 24;
        for (int i = 0; i < 3; i++) {
            int cx = this.width / 2 + (i - 1) * 74;
            int dx = mx - cx, dy = my - cy;
            if (dx * dx + dy * dy <= 24 * 24) return i;
        }
        return -1;
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        int idx = buttonAt((int) mouseX, (int) mouseY);
        if (idx >= 0 && this.client != null) {
            switch (idx) {
                case 0 -> this.client.setScreen(new OptionsScreen(this, this.client.options));
                case 1 -> this.client.setScreen(new SelectWorldScreen(this));
                case 2 -> this.client.setScreen(new MultiplayerScreen(this));
            }
            return true;
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }

    private void fillCircle(DrawContext c, int cx, int cy, int r, int color) {
        for (int dy = -r; dy <= r; dy++) {
            int hw = (int) Math.floor(Math.sqrt(r * r - dy * dy));
            c.fill(cx - hw, cy + dy, cx + hw + 1, cy + dy + 1, color);
        }
    }

    private void fillRing(DrawContext c, int cx, int cy, int rOut, int rIn, int color) {
        for (int dy = -rOut; dy <= rOut; dy++) {
            int wo = (int) Math.floor(Math.sqrt(Math.max(0, rOut * rOut - dy * dy)));
            int wi = dy * dy < rIn * rIn ? (int) Math.ceil(Math.sqrt(rIn * rIn - dy * dy)) : 0;
            if (wi > wo) continue;
            c.fill(cx - wo, cy + dy, cx - wi, cy + dy + 1, color);
            c.fill(cx + wi, cy + dy, cx + wo + 1, cy + dy + 1, color);
        }
    }

    private void fillDiamond(DrawContext c, int cx, int cy, int r, int color) {
        for (int dy = -r; dy <= r; dy++) {
            int hw = r - Math.abs(dy);
            c.fill(cx - hw, cy + dy, cx + hw + 1, cy + dy + 1, color);
        }
    }

    private void drawGear(DrawContext c, int cx, int cy, int col) {
        fillRing(c, cx, cy, 9, 4, col);
        c.fill(cx - 2, cy - 13, cx + 3, cy - 8, col);
        c.fill(cx - 2, cy + 8, cx + 3, cy + 13, col);
        c.fill(cx - 13, cy - 2, cx - 8, cy + 3, col);
        c.fill(cx + 8, cy - 2, cx + 13, cy + 3, col);
    }

    private void drawPerson(DrawContext c, int cx, int cy, int col) {
        fillCircle(c, cx, cy - 7, 5, col);
        for (int dy = 0; dy <= 8; dy++) {
            int hw = (int) Math.sqrt(Math.max(0, 64 - (8 - dy) * (8 - dy)));
            c.fill(cx - hw, cy + 1 + dy, cx + hw + 1, cy + 2 + dy, col);
        }
    }

    private void drawDots(DrawContext c, int cx, int cy, int col) {
        int[][] off = {{0, -8}, {-7, -3}, {7, -3}, {-4, 4}, {4, 4}, {0, 0}};
        for (int[] o : off) fillCircle(c, cx + o[0], cy + o[1], 3, col);
    }
                            }
