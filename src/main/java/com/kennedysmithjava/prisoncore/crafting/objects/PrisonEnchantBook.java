package com.kennedysmithjava.prisoncore.crafting.objects;

import com.kennedysmithjava.prisoncore.PrisonCore;
import com.kennedysmithjava.prisoncore.crafting.CraftingRequest;
import com.kennedysmithjava.prisoncore.crafting.PrisonObject;
import com.kennedysmithjava.prisoncore.entity.player.MPlayer;
import com.kennedysmithjava.prisoncore.entity.player.SkillProfile;
import com.kennedysmithjava.prisoncore.skill.SkillType;
import com.kennedysmithjava.prisoncore.enchantment.Enchant;
import com.kennedysmithjava.prisoncore.util.ItemBuilder;
import com.kennedysmithjava.prisoncore.util.MiscUtil;
import com.massivecraft.massivecore.util.MUtil;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class PrisonEnchantBook extends PrisonObject {
    private static final NamespacedKey ENCHANT_NAME_KEY = new NamespacedKey(PrisonCore.get(), "enchantName");
    private static final NamespacedKey ENCHANT_LVL_KEY = new NamespacedKey(PrisonCore.get(), "enchantLevel");
    private static final String ENCHANT_KEY = "prisonEnchantBook";

    private final Enchant<?> enchant;
    private final int level;

    public PrisonEnchantBook(Enchant<?> enchant, int level){
        this.enchant = enchant;
        this.level = level;
    }

    @Override
    public ItemStack giveRawItem() {
        List<String> lore = new ArrayList<>(enchant.getUnformattedGUILore());
        lore.add(0, "&7Enchant Book");
        lore.add(1, " &r");
        lore.add("&7Level &e" + getLevel());
        lore.add(" &r");
        lore.add("&7Apply to a &etool &7at");
        lore.add("&7an &dEnchanting Table!");
        return new ItemBuilder(Material.ENCHANTED_BOOK)
                .name("&d&l" + enchant.getName() + " Book")
                .lore(lore)
                .build();
    }

    @Override
    public String getKey() {
        return ENCHANT_KEY;
    }

    @Override
    public Map<PersistentDataType<?, ?>, Map<NamespacedKey, ?>> getStoredData() {
        return MUtil.map(
                PersistentDataType.STRING, MUtil.map(ENCHANT_NAME_KEY, enchant.getID()),
                PersistentDataType.INTEGER, MUtil.map(ENCHANT_LVL_KEY, level)
        );
    }

    @Override
    public String getName() {
        return enchant.getID();
    }


    @Override
    public boolean isStackable() {
        return false;
    }

    @SuppressWarnings("DataFlowIssue")
    public static PrisonEnchantBook getFrom(ItemStack item){
        ItemMeta meta = item.getItemMeta();
        if(meta == null) throw new NullPointerException();
        PersistentDataContainer pdc = meta.getPersistentDataContainer();
        String enchantID = pdc.get(ENCHANT_NAME_KEY, PersistentDataType.STRING);
        if(!Enchant.exists(enchantID)) throw new NullPointerException();
        Enchant<?> enchant = Enchant.getByName(enchantID);
        int enchantLvl = pdc.get(ENCHANT_LVL_KEY, PersistentDataType.INTEGER);
        return new PrisonEnchantBook(enchant, enchantLvl);
    }

    public int getLevel() {
        return level;
    }

    public Enchant<?> getEnchant() {
        return enchant;
    }

    public static boolean isEnchantBook(ItemStack itemStack){
        ItemMeta meta = itemStack.getItemMeta();
        if(meta == null) return false;
        if(!isPrisonObj(itemStack)) return false;
        PersistentDataContainer pdc = meta.getPersistentDataContainer();
        String key = pdc.get(prisonObjectKey, PersistentDataType.STRING);
        if(key == null) return false;
        return key.equals(ENCHANT_KEY);
    }

    //TODO: Add some RNG on the level of the enchant book based on enchantment level and XP
    public static void beginCrafting(Enchant<?> enchant, CraftingRequest request){
        Player player = request.getPlayer();
        MiscUtil.givePlayerItem(player, new PrisonEnchantBook(enchant, 1).give(1), 1);
        SkillProfile profile = MPlayer.get(player).getSkillProfile();
        profile.getSkill(SkillType.ENCHANTING).addXP(10);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PrisonEnchantBook that = (PrisonEnchantBook) o;
        return level == that.level && enchant.getID().equals(that.enchant.getID());
    }

    @Override
    public int hashCode() {
        return 0;
    }
}
