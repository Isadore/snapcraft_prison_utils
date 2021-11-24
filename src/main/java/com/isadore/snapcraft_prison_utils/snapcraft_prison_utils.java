package com.isadore.snapcraft_prison_utils;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.UUID;

@Mod(snapcraft_prison_utils.MODID)
public class snapcraft_prison_utils
{
    public static final String MODID = "snapcraft_prison_utils";
    public static final Logger LOGGER = LogManager.getLogger();
    public static final Minecraft mc = Minecraft.getInstance();

    public snapcraft_prison_utils() {
        FMLJavaModLoadingContext.get().getModEventBus().register(new snapcraft_prison_utils());
        MinecraftForge.EVENT_BUS.register(new EventHandler());
    }

    public static String playerID() {
        return fromTrimmed(mc.session.getPlayerID()).toString();
    }

    public static UUID fromTrimmed(String trimmedUUID) throws IllegalArgumentException{
        if(trimmedUUID == null) throw new IllegalArgumentException();
        StringBuilder builder = new StringBuilder(trimmedUUID.trim());
        /* Backwards adding to avoid index adjustments */
        try {
            builder.insert(20, "-");
            builder.insert(16, "-");
            builder.insert(12, "-");
            builder.insert(8, "-");
        } catch (StringIndexOutOfBoundsException e){
            throw new IllegalArgumentException();
        }

        return UUID.fromString(builder.toString());
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
