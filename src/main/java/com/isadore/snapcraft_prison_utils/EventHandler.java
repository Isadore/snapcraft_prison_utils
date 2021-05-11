package com.isadore.snapcraft_prison_utils;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraftforge.client.event.*;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
        }
    }

    @SubscribeEvent
    public void onMessage(ClientChatReceivedEvent event) {
        String messageContent = event.getMessage().getString();
        Pattern eTokenPattern = Pattern.compile("^\\+5 tokens for mining 250 blocks! \\(Use tokens to /enchant\\)$");
        Matcher eTokenMatcher = eTokenPattern.matcher(messageContent);
        if(eTokenMatcher.find()) {
            if(GuiOverlay.eTokenMessageCounter.secondsElapsed() > 5) GuiOverlay.eTokenMessageCounter.reset();
            GuiOverlay.eTokenMessageCounter.increment();
            if(GuiOverlay.eTokenMessageCounter.secondsElapsed() < 5 && GuiOverlay.eTokenMessageCounter.messageCount >= 6)
                GuiOverlay.sliceTimerEnd = System.currentTimeMillis() + GuiOverlay.sliceTimeMS;
        }
    }

    @SubscribeEvent
    public void onMessageSent(ClientChatEvent event) {
//        String msg = event.getMessage();
//        if(msg.startsWith("/cf") || msg.startsWith("/coinflip")) event.setMessage("/");
    }

}
