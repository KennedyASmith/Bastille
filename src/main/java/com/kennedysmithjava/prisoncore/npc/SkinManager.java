package com.kennedysmithjava.prisoncore.npc;

import com.kennedysmithjava.prisoncore.PrisonCore;
import com.kennedysmithjava.prisoncore.entity.npcs.SkinsConf;
import net.citizensnpcs.api.npc.NPC;
import net.citizensnpcs.trait.SkinTrait;
import net.skinsrestorer.api.SkinsRestorerAPI;
import net.skinsrestorer.api.property.IProperty;
import org.bukkit.scheduler.BukkitRunnable;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class SkinManager {

    private static SkinsRestorerAPI skinAPI = SkinsRestorerAPI.getApi();
    private static Map<String, String[][]> skinHeads = new HashMap<>();

    public static void registerAll(){
        for (Map.Entry<String, Skin> stringSkinEntry : SkinsConf.get().getCustomSkins().entrySet()) {
            Skin skin = stringSkinEntry.getValue();
            String skinName = stringSkinEntry.getKey();
            registerAsync(skinName, skin);
        }
    }

    public static void registerAsync(String skinName, Skin skin){
        new BukkitRunnable() {
            @Override
            public void run() {
                registerSync(skinName, skin);
            }
        }.runTaskAsynchronously(PrisonCore.get());
    }

    public static void registerSync(String skinName, Skin skin){
        String[][] headImage = getHeadData(skinName, skin);
        skinHeads.put(skinName, headImage);
    }


    public static void skin(NPC npc, String name, Skin skin, int delay){
        skin(npc, name, skin.getTexture(), skin.getSignature(), delay);
    }

    public static void skin(NPC npc, String name, int delay){
        SkinTrait trait = npc.getTrait(SkinTrait.class);
        new BukkitRunnable() {
            @Override
            public void run() {
                trait.setSkinName(name,  true);
            }
        }.runTaskLater(PrisonCore.get(), delay);
    }

    public static void skin(NPC npc, String name, String texture, String signature, int delay){
        SkinTrait trait = npc.getTrait(SkinTrait.class);
        new BukkitRunnable() {
            @Override
            public void run() {
                trait.setShouldUpdateSkins(true);
                trait.setSkinPersistent(name, signature, texture);

            }
        }.runTaskLater(PrisonCore.get(), delay);
    }



    /**
     * Returns a 2D array of RGB squares for the head of this skin.
     * Should not be used synchronously.
     * @param name
     * @param skin
     * @return
     */
    private static String[][] getHeadData(String name, Skin skin){

        String textureValue = skin.getTexture();
        String textureSignature = skin.getSignature();
        IProperty property = skinAPI.getSkinData(name);
        if(property == null){
            property = skinAPI.createPlatformProperty(name, textureValue, textureSignature);
        }

        String url = skinAPI.getSkinTextureUrl(property);
        BufferedImage image = getImage(url);
        String[][] faceColorCodes = new String[8][8];

        for (int y = 8; y < 16; y++) {
            for (int x = 8; x < 16; x++) {
                java.awt.Color color = new java.awt.Color(image.getRGB(x, y));
                String hexCode = String.format("#%02X%02X%02X", color.getRed(), color.getGreen(), color.getBlue());
                faceColorCodes[y - 8][x - 8] = net.md_5.bungee.api.ChatColor.of(hexCode) + "⬛";
            }
        }

        return faceColorCodes;
    }

    /**
     * Get head image from cache if it exists, otherwise caches and forces a sync URL pull
     * Forces a sync
     * @param name
     * @param skin
     * @return
     */
    public static String[][] getHead(String name, Skin skin){
        if(skinHeads.containsKey(name)){
            return skinHeads.get(name);
        }
        registerAsync(name, skin);
        return getHeadData(name, skin);
    }

    private static BufferedImage getImage(String url) {
        // Schedule a task to retrieve the skin texture asynchronously
        try {
            // Load the skin texture image
            return ImageIO.read(new URL(url));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
