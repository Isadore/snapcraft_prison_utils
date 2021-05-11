package com.isadore.snapcraft_prison_utils;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(snapcraft_prison_utils.MODID)
public class snapcraft_prison_utils
{
    public static final String MODID = "snapcraft_prison_utils";

    public snapcraft_prison_utils() {
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::enqueueIMC);
        MinecraftForge.EVENT_BUS.register(new EventHandler());
    }

    public static boolean serverIsSnapcraft() {
        ServerData server = Minecraft.getInstance().getCurrentServerData();
        if(server != null) return server.serverIP.endsWith("snapcraft.net");
        return false;
    }

    private void enqueueIMC(final InterModEnqueueEvent event)
    {
        KeyBinds.register();
    }

}
