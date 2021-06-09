package com.isadore.snapcraft_prison_utils;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Set;

public class InventoryUtils {

    @Nullable
    public static String getItemID(ItemStack stack) {
        return getItemID(stack.getItem());
    }

    @Nullable
    public static String getItemID(Item item) {
        ResourceLocation id = ForgeRegistries.ITEMS.getKey(item);
        return id != null ? id.toString() : null;
    }

    @Nullable
    public static HashMap<String, Integer> getItemEnchants(ItemStack i) {
        CompoundNBT tags = i.getTag();
        if(tags != null) {
            CompoundNBT display = tags.getCompound("display");
            Set<String> displayKeys = display.keySet();
            for (String s : displayKeys) {
                if(s.startsWith("ViaVersion") && s.endsWith("Lore")) {
                    ListNBT enchantNBTList = display.getList(s, Constants.NBT.TAG_STRING);
                    HashMap<String, Integer> enchantMap = new HashMap<>();
                    for (INBT e : enchantNBTList) {
                        String enchantStr = e.getString().substring(2);
                        if(enchantStr.endsWith(" ")) continue;
                        String[] enchStrArr = enchantStr.split("\\s");
                        if(enchStrArr.length < 2) continue;
                        String enchantName = enchStrArr[0];
                        try {
                            Integer enchantLevel = Integer.parseInt(enchStrArr[1]);
                            enchantMap.put(enchantName, enchantLevel);
                        } catch (Exception ignored) {}
                    }
                    return enchantMap;
                }
            }
        }
        return null;
    }

}
