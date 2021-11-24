package com.isadore.snapcraft_prison_utils;

import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.network.IPacket;
import net.minecraft.network.play.server.SWindowItemsPacket;

import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

public class PacketHandler extends ChannelDuplexHandler {

    private static final Minecraft mc = Minecraft.getInstance();

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object obj) throws Exception {
        super.channelRead(ctx, obj);
        if(obj instanceof IPacket) {
            this.onPacket((IPacket<?>) obj, true);
        }
    }

    @Override
    public void write(ChannelHandlerContext ctx, Object obj, ChannelPromise promise) throws Exception {
        super.write(ctx, obj, promise);
        if(obj instanceof IPacket) {
            this.onPacket((IPacket<?>) obj, false);
        }
    }

    private void onPacket(IPacket<?> packet, boolean incoming) {
        Method[] methods = this.getClass().getDeclaredMethods();
        String packetName = packet.getClass().getTypeName();
        for (Method m : methods) {
            Type[] params = m.getGenericParameterTypes();
            if(params.length > 0 && packetName.equals(params[0].getTypeName())) {
                try {
                    if(params.length > 1 && params[1].getTypeName().equals("boolean")) {
                        m.invoke(this, packet, incoming);
                    } else {
                        m.invoke(this, packet);
                    }
                } catch (Exception e) {
                    snapcraft_prison_utils.LOGGER.error("Failed to call method: {} for packet: {}", m.getName(), packetName);
                }
            }
        }
    }

    Map<String, Integer> lastItemCounts = new HashMap<>();

    public void onItemsUpdate(SWindowItemsPacket packet) {
        Map<String, Integer> itemCounts = new HashMap<>();
        for(ItemStack i : packet.getItemStacks()) {
            String itemID = InventoryUtils.getItemID(i);
            if(itemID == null) continue;
            itemCounts.computeIfAbsent(itemID, k -> i.getCount());
            itemCounts.computeIfPresent(itemID, (k, v) -> v + i.getCount());
        }
        int maxChange = 0;
        int increasedItemTypes = 0;
        int decreasedItemTypes = 0;
        for (Map.Entry<String, Integer> i : lastItemCounts.entrySet()) {
            Integer currentVal = itemCounts.get(i.getKey());
            Integer lastVal = i.getValue();
            if(currentVal == null) {
                decreasedItemTypes++;
            } else if(!currentVal.equals(lastVal)) {
                if(currentVal > lastVal)
                    increasedItemTypes++;
                else
                    decreasedItemTypes++;
                int change = currentVal - lastVal;
                if(change > maxChange)
                    maxChange = change;
            }
        }
        if(maxChange > 25 && packet.getWindowId() == 0 && mc.player != null) {
            Map<String, Integer> itemEnchants = InventoryUtils.getItemEnchants(mc.player.getHeldItemMainhand());
            if(itemEnchants != null) {
                if(itemEnchants.get("OreSeeker") != null && increasedItemTypes == 1 && decreasedItemTypes == 0)
                    UserData.profile.oreSeekTimerEnd = System.currentTimeMillis() + GuiOverlay.oreSeekTimeMS;
                if(itemEnchants.get("Slicing") != null)
                    UserData.profile.sliceTimerEnd = System.currentTimeMillis() + GuiOverlay.sliceTimeMS;
            }
        }
        lastItemCounts = itemCounts;
    }

}



