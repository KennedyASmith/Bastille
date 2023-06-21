package com.kennedysmithjava.prisoncore.quest;

import com.kennedysmithjava.prisoncore.PrisonCore;
import com.kennedysmithjava.prisoncore.npc.Skin;
import com.kennedysmithjava.prisoncore.npc.SkinManager;
import com.kennedysmithjava.prisoncore.util.Color;
import com.massivecraft.massivecore.util.MUtil;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;

public class QuestMessage {

    private String name;

    private List<String> lines;
    private Skin skin;

    public QuestMessage(String npcName, Skin skin, List<String> lines) {
        this.name = npcName;
        this.lines = lines;
        this.skin = skin;
    }


    public void sendMessage(Player player){
        new BukkitRunnable() {
            @Override
            public void run() {

                String[][] faceColorCodes = SkinManager.getHead(name, skin);

                List<String> messageLines = MUtil.list(" &r");
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
                    player.sendMessage(Color.get(messageLine));
                }
            }
        }.runTaskAsynchronously(PrisonCore.get());
    }

}
