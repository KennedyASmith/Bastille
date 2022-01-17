package com.kennedysmithjava.prisoncore.util;

import com.massivecraft.massivecore.util.Txt;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.List;

@AllArgsConstructor
@Data
public class MItem {

        private String name;
        private List<String> lore;
        private Material material;
        private int data;
        private boolean glow;
        private List<String> enchantments;
        private List<Integer> levels;

        public ItemStack build() {
            return glow ? new ItemBuilder(material).name(Txt.parse(name)).lore(Txt.parse(lore)).addGlow().durability(data).unsafeEnchants(enchantments, levels).build() : new ItemBuilder(material).name(Txt.parse(name)).lore(Txt.parse(lore)).durability(data).build();
        }

}

