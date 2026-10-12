package dev.glitchvisual;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.screen.v1.ScreenEvents;
import net.minecraft.client.gui.screen.GameMenuScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;
import org.lwjgl.glfw.GLFW;

public final class GlitchVisual implements ClientModInitializer {

    private boolean rightShiftWasDown;

    @Override
    public void onInitializeClient() {

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            boolean down = GLFW.glfwGetKey(
                    client.getWindow().getHandle(),
                    GLFW.GLFW_KEY_RIGHT_SHIFT
            ) == GLFW.GLFW_PRESS;

            if (down && !rightShiftWasDown
                    && client.currentScreen == null) {
                client.setScreen(new GlitchVisualScreen());
            }

            rightShiftWasDown = down;
        });

        ScreenEvents.AFTER_INIT.register(
                (client, screen, width, height) -> {
                    if (screen instanceof GameMenuScreen) {
                        screen.addDrawableChild(
                                ButtonWidget.builder(
                                        Text.literal("GlitchVisual"),
                                        button -> client.setScreen(
                                                new GlitchVisualScreen()
                                        )
                                ).dimensions(8, 8, 110, 20).build()
                        );
                    }
                }
        );
    }
}
