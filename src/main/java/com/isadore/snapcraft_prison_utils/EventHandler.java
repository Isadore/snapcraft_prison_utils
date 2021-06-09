package com.isadore.snapcraft_prison_utils;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.network.NetworkManager;
import net.minecraftforge.client.event.*;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class EventHandler {

    private static final Minecraft mc = Minecraft.getInstance();

    @SubscribeEvent
    public void onPress(InputEvent event) {
        if(!snapcraft_prison_utils.serverIsSnapcraft()) return;
        if(KeyBinds.isKeyDown(KeyBinds.resetSliceTimer))
            KeyBinds.resetTimer();
    }

    @SubscribeEvent
    public void renderOverlay(RenderGameOverlayEvent.Post event) {
        if(!snapcraft_prison_utils.serverIsSnapcraft()) return;
        if(event.getType() == RenderGameOverlayEvent.ElementType.ALL && !mc.gameSettings.showDebugInfo) {
            MatrixStack matrix = event.getMatrixStack();
            GuiOverlay.renderSliceTimer(matrix);
            GuiOverlay.renderOreSeekTimer(matrix);
        }
    }

    private boolean loggedIn = false;

    @SubscribeEvent
    public void onLogin(ClientPlayerNetworkEvent.LoggedInEvent event) {
        NetworkManager manager = event.getNetworkManager();
        if(manager == null || loggedIn) return;
        manager.channel().pipeline().addBefore("packet_handler", "listener", new PacketHandler());
        loggedIn = true;
    }

    @SubscribeEvent
    public void onLogout(ClientPlayerNetworkEvent.LoggedOutEvent event) {
        loggedIn = false;
    }

    @SubscribeEvent
    public void onMessageSent(ClientChatEvent event) {
//        String msg = event.getMessage();
//        if(msg.startsWith("/cf") || msg.startsWith("/coinflip")) event.setMessage("/");
    }

}
