package com.kennedysmithjava.prisoncore.gui;

import com.kennedysmithjava.prisoncore.crafting.PrisonObject;
import com.kennedysmithjava.prisoncore.crafting.ProductItem;
import com.kennedysmithjava.prisoncore.crafting.objects.PrisonMetal;
import com.kennedysmithjava.prisoncore.crafting.objects.PrisonOre;
import com.kennedysmithjava.prisoncore.crafting.objects.type.MetalType;
import com.kennedysmithjava.prisoncore.crafting.objects.type.OreType;
import com.kennedysmithjava.prisoncore.eco.Cost;
import com.kennedysmithjava.prisoncore.eco.CostSkillLevel;
import com.kennedysmithjava.prisoncore.engine.EngineXP;
import com.kennedysmithjava.prisoncore.entity.player.MPlayer;
import com.kennedysmithjava.prisoncore.skill.SkillType;
import com.kennedysmithjava.prisoncore.util.Color;
import com.kennedysmithjava.prisoncore.util.ItemBuilder;
import com.massivecraft.massivecore.chestgui.ChestGui;
import com.massivecraft.massivecore.util.MUtil;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ForgeCraftGui extends BaseGui{
    public ForgeCraftGui(Player player) {
        super(player, "&4&lSelect Ore to smelt:", 6, false, true);
    }

    @Override
    public void onBuild(Player player, ChestGui gui, Inventory inventory) {
        blockFill(Material.RED_STAINED_GLASS_PANE, 0, 17);
        blockFill(Material.ORANGE_STAINED_GLASS_PANE, 18, 35);
        blockFill(Material.YELLOW_STAINED_GLASS_PANE, 36, 53);
        blockFill(Material.ORANGE_STAINED_GLASS_PANE, 38, 42);
        blockFill(Material.ORANGE_STAINED_GLASS_PANE, 48, 50);
        blockFill(Material.WHITE_STAINED_GLASS_PANE, 45, 46);
        blockFill(Material.WHITE_STAINED_GLASS_PANE, 52, 53);

        for (OreType oreType : OreType.values()) {

            PrisonOre ore = new PrisonOre(oreType);
            MetalType ingotType = oreType.getMetalType();
            PrisonMetal metal = new PrisonMetal(ingotType);
            List<Cost> additionalCosts = MUtil.list(new CostSkillLevel(SkillType.METALWORKING, 2));

            ItemStack oreItem = ore.give(1);
            List<String> lore = oreItem.getItemMeta().getLore();
            lore.add( " &r");
            lore.add( "&eClick to smelt!");
            lore.add(" &r");
            lore.add("&7&lREQUIREMENTS");
            for (Cost additionalCost : additionalCosts) {
                lore.add("&7- " + additionalCost.getPriceline());
            }
            ItemBuilder builder = new ItemBuilder(oreItem).lore(lore);
            setItem(oreType.getForgeGuiSlot(), builder.build());

            BaseGui baseGui = this;

            setAction(oreType.getForgeGuiSlot(), inventoryClickEvent -> {
                Map<Integer, PrisonObject> neededIngredients = MUtil.map(22, ore);
                ProductItem productItem = new ProductItem(craftingRequest -> {
                    craftingRequest.give(metal.give(1));
                    EngineXP.giveXP(SkillType.METALWORKING, MPlayer.get(player), oreType.getSmeltingXP());
                });
                CraftingMenuGui craftingMenuGui = new CraftingMenuGui(
                        player,
                        Color.strip(oreType.getDisplayName()),
                        new HashMap<>(),
                        neededIngredients,
                        productItem,
                        additionalCosts,
                        baseGui);
                close();
                craftingMenuGui.open();
                return false;
            });

        }

        }
    }
