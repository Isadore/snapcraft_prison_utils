package com.isadore.snapcraft_prison_utils;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.util.ColorHelper;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import org.lwjgl.glfw.GLFW;

public class GuiOverlay {

    private static final Minecraft mc = Minecraft.getInstance();

    public static int screenWidth() { return mc.getMainWindow().getScaledWidth(); }
    public static int screenHeight() { return mc.getMainWindow().getScaledHeight(); }

    public static final int sliceTimeMS = 7 * 60 * 1000;
    public static long sliceTimerEnd = 0; //System.currentTimeMillis() + sliceTimeMS;
    public static final double oreSeekTimeMS = 60 * 1000;
    public static double oreSeekTimerEnd = 0;

    private static void renderBackgroundBar(MatrixStack matrix, int y) {
        int lightGrey = ColorHelper.PackedColor.packColor(255, 153,153,153);
        int darkGrey = ColorHelper.PackedColor.packColor(255, 56, 56, 56);
        AbstractGui.fill(matrix, screenWidth() - 75, y + 10, screenWidth(), y, lightGrey);
        AbstractGui.fill(matrix, screenWidth() - 49, y + 9, screenWidth() - 1, y + 1, darkGrey);
    }

    private static void renderPercentBar(MatrixStack matrix, int y, double percent, int color, int outlineColor) {
        if(Double.isNaN(percent) || percent == 0) return;
        int widthToSubtract = (int) (48 * (1 - percent));
        int maxX = (screenWidth() - widthToSubtract);
        AbstractGui.fill(matrix, screenWidth() - 49, y + 8, maxX - 1, y, outlineColor);
        if(widthToSubtract >= 47) return;
        AbstractGui.fill(matrix, screenWidth() - 48, y + 7, maxX - 2, y + 1, color);
    }

    public static void renderSliceTimer(MatrixStack matrix) {
        double sliceTimeRemaining = sliceTimerEnd - System.currentTimeMillis();
        int sliceMinutesRemaining = (int) (sliceTimeRemaining / 60000);
        int sliceSecondsRemaining = (int) (sliceTimeRemaining - (sliceMinutesRemaining * 60000)) / 1000;
        GuiOverlay.renderBackgroundBar(matrix, 0);
        if(sliceTimeRemaining >= 0) {
            int brightRed = ColorHelper.PackedColor.packColor(255, 158, 26, 38);
            int darkRed = ColorHelper.PackedColor.packColor(255, 215, 0, 39);
            GuiOverlay.renderPercentBar(matrix, 1, sliceTimeRemaining/sliceTimeMS, darkRed, brightRed);
            mc.fontRenderer.drawString(matrix, String.format("%d:%02d", sliceMinutesRemaining, sliceSecondsRemaining), screenWidth() - 71, 1, 0);
            if(sliceTimeRemaining/1000 <= 5 && mc.ingameGUI.displayedTitle == null) {
                long window = mc.getMainWindow().getHandle();
                GLFW.glfwRequestWindowAttention(window);
                ITextComponent announcementText = new StringTextComponent(String.format("Slice Cooldown Ending In %d Seconds", (int) sliceTimeRemaining/1000))
                        .mergeStyle(TextFormatting.DARK_RED);
                mc.ingameGUI.renderTitles(announcementText, null, 1, 6, 1);
            }
        } else {
            mc.fontRenderer.drawString(matrix, "0:00", screenWidth() - 71, 1, 0);
        }
    }

    public static void renderOreSeekTimer(MatrixStack matrix) {
        double oreSeekTimeRemaining = oreSeekTimerEnd - System.currentTimeMillis();
        int secondsRemaining = (int) oreSeekTimeRemaining / 1000;
        GuiOverlay.renderBackgroundBar(matrix, 11);
        if(oreSeekTimeRemaining > 0) {
            int darkPink = ColorHelper.PackedColor.packColor(255, 130, 47, 100);
            int lightPink = ColorHelper.PackedColor.packColor(255, 235, 89, 181);
            GuiOverlay.renderPercentBar(matrix, 12, oreSeekTimeRemaining / oreSeekTimeMS,  darkPink, lightPink);
            mc.fontRenderer.drawString(matrix, String.format("0:%02d", secondsRemaining), screenWidth() - 71, 12, 0);
        } else {
            mc.fontRenderer.drawString(matrix, "0:00", screenWidth() - 71, 12, 0);
        }
    }

}
