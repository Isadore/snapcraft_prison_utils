package com.isadore.snapcraft_prison_utils;

import com.google.gson.Gson;
import net.minecraft.client.Minecraft;

import javax.annotation.Nullable;
import java.io.*;
import java.nio.file.Files;

public class UserData {

    private static final Minecraft mc = Minecraft.getInstance();
    private static final File baseDir = new File(mc.gameDir, "IsadoreMod");
    @Nullable
    private static File playerDir = null;
    @Nullable
    private static File profilePath = null;

    public static PlayerProfile profile = new PlayerProfile();

    public static class PlayerProfile {
        public boolean hideChestShopMessages = false;
        public double sliceTimerEnd = 0;
        public double oreSeekTimerEnd = 0;
    }

    public static void init() {
        try {
            baseDir.mkdir();
            playerDir = new File(baseDir, snapcraft_prison_utils.playerID());
            playerDir.mkdir();
            profilePath = new File(playerDir, "profile.json");
            if(profilePath.exists()) {
                try {
                    BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(profilePath)));
                    profile = new Gson().fromJson(reader, PlayerProfile.class);
                } catch (Exception e) {
                    snapcraft_prison_utils.LOGGER.error("Failed reading player profile");
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            snapcraft_prison_utils.LOGGER.error("Failed initializing directories");
            e.printStackTrace();
        }
    }

    public static void writeProfile() {
        if(profilePath != null) {
            try {
                Files.write(profilePath.toPath(), new Gson().toJson(profile).getBytes());
            } catch (Exception e) {
                snapcraft_prison_utils.LOGGER.error("Failed writing player profile");
                e.printStackTrace();
            }
        }
    }

}
