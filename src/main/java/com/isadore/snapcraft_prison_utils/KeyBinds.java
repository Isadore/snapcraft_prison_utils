package com.isadore.snapcraft_prison_utils;

import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.client.util.InputMappings;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.glfw.GLFW;

public class KeyBinds {

    public static KeyBinding resetSliceTimer = new KeyBinding("Reset slice timer", -1, snapcraft_prison_utils.MODID);
    public static KeyBinding[] allKeyBinds = { resetSliceTimer };

    private static final Logger LOGGER = LogManager.getLogger();
    private static final Minecraft mc = Minecraft.getInstance();

    public static void register() {
        for(KeyBinding k : allKeyBinds)
            ClientRegistry.registerKeyBinding(k);
    }

    public static void resetTimer() {
        UserData.profile.sliceTimerEnd = System.currentTimeMillis() + GuiOverlay.sliceTimeMS;
    }

    public static boolean isKeyDown(KeyBinding keyBind, boolean checkScreen)
    {
        if (keyBind.isInvalid())
            return false;

        if(mc.currentScreen != null && checkScreen)
            return false;

        boolean isDown = false;
        switch (keyBind.getKey().getType())
        {
            case KEYSYM:
                isDown = InputMappings.isKeyDown(mc.getMainWindow().getHandle(), keyBind.getKey().getKeyCode());
                break;
            case MOUSE:
                isDown = GLFW.glfwGetMouseButton(mc.getMainWindow().getHandle(), keyBind.getKey().getKeyCode()) == GLFW.GLFW_PRESS;
                break;
        }
        return isDown && keyBind.getKeyConflictContext().isActive() && keyBind.getKeyModifier().isActive(keyBind.getKeyConflictContext());
    }

    public static boolean isKeyDown(KeyBinding keyBind) {
        return isKeyDown(keyBind, true);
    }


}
