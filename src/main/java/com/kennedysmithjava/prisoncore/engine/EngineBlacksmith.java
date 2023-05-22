package com.kennedysmithjava.prisoncore.engine;

import com.kennedysmithjava.prisoncore.crafting.CraftingRequest;
import com.kennedysmithjava.prisoncore.crafting.objects.PrisonMetal;
import com.kennedysmithjava.prisoncore.crafting.objects.PrisonStick;
import com.kennedysmithjava.prisoncore.crafting.objects.type.MetalType;
import com.kennedysmithjava.prisoncore.entity.tools.PickaxeType;
import com.kennedysmithjava.prisoncore.entity.tools.PickaxeTypeColl;
import com.kennedysmithjava.prisoncore.crates.CrateRoulette;
import com.kennedysmithjava.prisoncore.crates.CratePrize;
import com.massivecraft.massivecore.Engine;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.*;

public class EngineBlacksmith extends Engine {

    public static EngineBlacksmith i = new EngineBlacksmith();

    public static EngineBlacksmith get() {
        return i;
    }

    @SuppressWarnings("DataFlowIssue")
    public static void beginCrafting(CraftingRequest craftingRequest){
        PrisonMetal anyMetal = new PrisonMetal(MetalType.ANY);
        Map<Integer, ItemStack> ingredientsMap = craftingRequest.getIngredients();
        int durability = 0;
        int hardness = 0;
        int luck = 0;

        Collection<ItemStack> ingredients = ingredientsMap.values();
        for (ItemStack itemStack : ingredients) {
            PersistentDataContainer pdc = itemStack.getItemMeta().getPersistentDataContainer();
            if(anyMetal.is(itemStack)){ //We know it's a metal
                luck += pdc.get(PrisonMetal.metalRarityKey, PersistentDataType.INTEGER);
                hardness += pdc.get(PrisonMetal.metalHardnessKey, PersistentDataType.INTEGER);
            } else { //We can know it's a stick
                durability += pdc.get(PrisonStick.durabilityKey, PersistentDataType.INTEGER);
            }
        }

        //Build the list of prizes
        List<CratePrize> prizes = new ArrayList<>();

        for (PickaxeType pickaxeType : PickaxeTypeColl.get().getAll()) {
            if(pickaxeType.getRarity() > 0){
                pickaxeType.setStartDurability(durability);
                ItemStack prize = pickaxeType.getItemStack();
                ItemStack icon = prize.clone();
                prize.setType(getMaterialFromHardness(hardness));
                CratePrize cratePrize = new CratePrize(
                                prize,
                                icon,
                                pickaxeType.getRarityName().getGlassMaterial(),
                                pickaxeType.getRarity()
                );
                prizes.add(cratePrize);
            }
        }

        //Show the crate animation to the player
        CrateRoulette.animate(craftingRequest.getPlayer(), prizes, 1.0);
    }

    //Max "hardness" is 6, 6 * 5 = 30 possible hardness values
    public static Material getMaterialFromHardness(int h){
        if(h > 28){
            return Material.NETHERITE_PICKAXE;
        }else if (h > 22) {
            return Material.DIAMOND_PICKAXE;
        }else if (h > 16) {
            return Material.GOLDEN_PICKAXE;
        }else if (h > 12) {
            return Material.IRON_PICKAXE;
        }else if (h > 8){
            return Material.STONE_PICKAXE;
        }else {
            return Material.WOODEN_PICKAXE;
        }
    }

}
