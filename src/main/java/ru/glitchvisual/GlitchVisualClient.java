package ru.glitchvisual;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.gui.screen.TitleScreen;
import ru.glitchvisual.screen.GlitchMainMenuScreen;

public class GlitchVisualClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (client.currentScreen instanceof TitleScreen) {
                client.setScreen(new GlitchMainMenuScreen());
            }
        });
    }
}
