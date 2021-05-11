package com.isadore.snapcraft_prison_utils;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import org.lwjgl.glfw.GLFW;

public class GuiOverlay {

    private static final Minecraft mc = Minecraft.getInstance();

    private static final ResourceLocation empty_bar_texture = new ResourceLocation(snapcraft_prison_utils.MODID, "textures/empty_bar.png");
    private static final ResourceLocation timer_bar_texture = new ResourceLocation(snapcraft_prison_utils.MODID, "textures/timer_bar.png");

    public static int screenWidth() {
        return mc.getMainWindow().getScaledWidth();
    }
    public static int screenHeight() { return mc.getMainWindow().getScaledHeight(); }

    public static final int sliceTimeMS = 7 * 60 * 1000;
    public static long sliceTimerEnd = 0; //System.currentTimeMillis() + sliceTimeMS;
    public static final MessageCounter eTokenMessageCounter = new MessageCounter();

    public static void renderSliceTimer(MatrixStack matrix) {

        long sliceTimeRemaining = sliceTimerEnd - System.currentTimeMillis();
        int sliceMinutesRemaining = (int) (sliceTimeRemaining / 60000);
        int sliceSecondsRemaining = (int) (sliceTimeRemaining - (sliceMinutesRemaining * 60000)) / 1000;

        mc.getTextureManager().bindTexture(empty_bar_texture);
        mc.ingameGUI.blit(matrix, screenWidth() - 76, 1, 0, 0, 75, 10);

        if(sliceTimeRemaining >= 0) {
            mc.getTextureManager().bindTexture(timer_bar_texture);
            mc.ingameGUI.blit(matrix, screenWidth() - 50, 2, 0, 0, (int) (48 * sliceTimeRemaining/sliceTimeMS), 10);
            mc.fontRenderer.drawString(matrix, String.format("%d:%02d", sliceMinutesRemaining, sliceSecondsRemaining), screenWidth() - 72, 3, 0);
            matrix.push();
            matrix.scale(3f, 3f, 0);

                if(sliceTimeRemaining/1000 == 5 && mc.ingameGUI.displayedTitle == null) {
                long window = mc.getMainWindow().getHandle();
                GLFW.glfwRequestWindowAttention(window);
                ITextComponent announcementText = new StringTextComponent(String.format("Slice Cooldown Ending In %d Seconds", sliceTimeRemaining/1000))
                        .mergeStyle(TextFormatting.DARK_RED);
                mc.ingameGUI.renderTitles(announcementText, null, 1, 6, 1);
            }
            matrix.pop();
        } else {
            mc.fontRenderer.drawString(matrix, "0:00", screenWidth() - 72, 2, 0);
        }

    }

}

class MessageCounter {
    public long startTimeStamp = System.currentTimeMillis();
    public int messageCount = 0;

    public long secondsElapsed() {
        return (System.currentTimeMillis() - startTimeStamp) / 1000;
    }

    public void reset() {
        this.startTimeStamp = System.currentTimeMillis();
        this.messageCount = 0;
    }

    public void increment() {
        this.messageCount++;
    }

}
