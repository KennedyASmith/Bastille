package com.kennedysmithjava.prisoncore.skill;

import com.kennedysmithjava.prisoncore.entity.player.MPlayer;
import com.kennedysmithjava.prisoncore.entity.player.Skill;
import com.kennedysmithjava.prisoncore.entity.player.SkillProfile;
import com.kennedysmithjava.prisoncore.entity.player.SkillsConf;
import com.kennedysmithjava.prisoncore.quest.QuestPath;
import com.kennedysmithjava.prisoncore.util.Color;
import com.kennedysmithjava.prisoncore.util.ItemBuilder;
import com.massivecraft.massivecore.chestgui.ChestGui;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.List;

/**
    This class creates a GUI for displaying a list of skills to a player.
    @author kennedyASmith
    @see QuestPath
 */

public class SkillListGUI {

    private final MPlayer player;


    public SkillListGUI(MPlayer player) {
        this.player = player;
    }

    /**
     * Opens the quest list GUI for the player.
     */
    public void open(){
        if(player.getPlayer() == null) return;
        player.getPlayer().openInventory(getQuestInventory(player).getInventory());
    }

    private ChestGui getQuestInventory(MPlayer player){
        Inventory inventory = Bukkit.createInventory(null, 4*9, Color.get("&c&l" + player.getName() + "&8's Skills"));
        ChestGui gui = ChestGui.getCreative(inventory);
        gui.setAutoclosing(false);
        gui.setAutoremoving(true);
        gui.setSoundOpen(null);
        gui.setSoundClose(null);

        ItemStack glass = new ItemBuilder(Material.LIGHT_BLUE_STAINED_GLASS_PANE).lore(" &r").build();

        //Top row glass
        for (int i = 0; i < 9; i++) {
            inventory.setItem(i, glass.clone());
        }

        //Bottom row glass
        for (int i = 27; i < 36; i++) {
            inventory.setItem(i, glass.clone());
        }

        SkillProfile skillProfile = player.getSkillProfile();
        for (Skill skill : skillProfile.getSkills().values()) {
            SkillType type = skill.getType();
            int currentLevel = skill.getCurrentLevel();
            int currentXP = skill.getCurrentXP();
            int maxLevel = SkillsConf.get().getMaxLevels().get(type);
            int neededXP = SkillsConf.getXpRequired(type, currentLevel);
            String name = type.getDisplayName();
            Material iconMat = type.getIcon();
            List<String> lore = type.getLore();
            lore.add(0, "&7Level: &e" + currentLevel + " &7/ " + maxLevel);
            lore.add(1, skill.getXPBar(neededXP) + " &7(&e" + currentXP + "&7/&e" + neededXP + "&7) XP");
            lore.add(2, "&r");

            lore.add("&r");
            lore.add("&e&oRight-Click &7&oto display this skill on your sidebar.");
            ItemBuilder skillIconBuilder =
                    new ItemBuilder(iconMat, skill.getCurrentLevel())
                            .lore(Color.get(lore))
                            .name(name);
            if(skillProfile.getFeaturedSkill() == type){
                skillIconBuilder.addGlow();
            }else {
                gui.setAction(type.getGuiSlot(), inventoryClickEvent -> {
                    player.getSkillProfile().setFeaturedSkill(type);
                    player.getPlayer().closeInventory();
                    open();
                    return false;
                });
            }
            ItemStack skillIcon = skillIconBuilder.build();
            inventory.setItem(type.getGuiSlot(), skillIcon);
        }

        gui.setBottomInventoryAllow(false);
        return gui;
    }

}
