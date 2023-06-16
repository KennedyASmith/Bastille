package com.kennedysmithjava.prisoncore.quest;

import com.kennedysmithjava.prisoncore.PrisonCore;
import com.kennedysmithjava.prisoncore.util.Color;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class QuestMessage {

    private UUID npcUUID;
    private String name;

    private List<String> lines;

    public QuestMessage(UUID npcUUID, String npcName, List<String> lines) {
        this.npcUUID = npcUUID;
        this.name = npcName;
        this.lines = lines;
    }


    public void sendMessage(Player player){
        new BukkitRunnable() {
            @Override
            public void run() {
                BufferedImage skinImage = getPlayerSkin(player.getUniqueId());
                String[][] faceColorCodes = getFaceColorCodes(skinImage);

                List<String> messageLines = new ArrayList<>();
                int largestLine = 0;

                //1 row for name  & lines size & 1 row for empty space at bottom
                int messageHeight = Math.max(lines.size() + 2, faceColorCodes.length);
                for (int row = 0; row < messageHeight; row++) {
                    StringBuilder rowBuilder = new StringBuilder();
                    if(row < faceColorCodes.length){
                        for (String s : faceColorCodes[row]) rowBuilder.append(s);
                    }
                    if(row - 1 < lines.size() && row > 0 && row < 7){
                        rowBuilder
                                .append("&r &7 &r")
                                .append(Color.get(lines.get(row - 1)));
                    }else if(row > 0 && row < 7){
                        rowBuilder.append("&r &7 &r");
                    }
                    if(row == 0) rowBuilder.append(" &r&7▣&7&m")
                            .append("╌".repeat(60))
                            .append("&r&7▣");
                    if(row == 7) rowBuilder.append(" &r&7▣&7&m")
                            .append("╌".repeat(60))
                            .append("&r&7▣");
                    messageLines.add(rowBuilder.toString());
                    if(largestLine < rowBuilder.length()) largestLine = rowBuilder.length();
                }

                for (String messageLine : messageLines) {
                    Bukkit.broadcastMessage(Color.get(messageLine));
                }
            }
        }.runTaskAsynchronously(PrisonCore.get());
    }

    public static String[][] getFaceColorCodes(BufferedImage image) {
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

    public BufferedImage getPlayerSkin(UUID playerUUID) {
        // Schedule a task to retrieve the skin texture asynchronously
        try {

            // Load the skin texture image
            return ImageIO.read(new URL("https://crafatar.com/skins/" + playerUUID.toString()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
