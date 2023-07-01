package com.kennedysmithjava.prisoncore.engine;

import com.kennedysmithjava.prisoncore.crafting.CraftingRequest;
import com.kennedysmithjava.prisoncore.crafting.objects.PrisonMetal;
import com.kennedysmithjava.prisoncore.crafting.objects.PrisonStick;
import com.kennedysmithjava.prisoncore.crafting.objects.type.MetalType;
import com.kennedysmithjava.prisoncore.crates.CratePrize;
import com.kennedysmithjava.prisoncore.crates.CrateRoulette;
import com.kennedysmithjava.prisoncore.entity.tools.*;
import com.massivecraft.massivecore.Engine;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public class EngineCraftingTools extends Engine {

    public static EngineCraftingTools i = new EngineCraftingTools();

    public static EngineCraftingTools get() {
        return i;
    }

    @SuppressWarnings("DataFlowIssue")
    public static void beginCraftingPickaxe(CraftingRequest craftingRequest){
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
                prize.setType(getPickaxeMaterialFromHardness(hardness));
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

    @SuppressWarnings("DataFlowIssue")
    public static void beginCraftingAxe(CraftingRequest craftingRequest){
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

        for (AxeType axeType : AxeTypeColl.get().getAll()) {
            if(axeType.getRarity() > 0){
                axeType.setStartDurability(durability);
                ItemStack prize = axeType.getItemStack();
                ItemStack icon = prize.clone();
                prize.setType(getAxeMaterialFromHardness(hardness));
                CratePrize cratePrize = new CratePrize(
                        prize,
                        icon,
                        axeType.getRarityName().getGlassMaterial(),
                        axeType.getRarity()
                );
                prizes.add(cratePrize);
            }
        }

        //Show the crate animation to the player
        CrateRoulette.animate(craftingRequest.getPlayer(), prizes, 1.0);
    }

    @SuppressWarnings("DataFlowIssue")
    public static void beginCraftingHoe(CraftingRequest craftingRequest){
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

        for (HoeType hoeType : HoeTypeColl.get().getAll()) {
            if(hoeType.getRarity() > 0){
                hoeType.setStartDurability(durability);
                ItemStack prize = hoeType.getItemStack();
                ItemStack icon = prize.clone();
                prize.setType(getHoeMaterialFromHardness(hardness));
                CratePrize cratePrize = new CratePrize(
                        prize,
                        icon,
                        hoeType.getRarityName().getGlassMaterial(),
                        hoeType.getRarity()
                );
                prizes.add(cratePrize);
            }
        }

        //Show the crate animation to the player
        CrateRoulette.animate(craftingRequest.getPlayer(), prizes, 1.0);
    }

    @SuppressWarnings("DataFlowIssue")
    public static void beginCraftingPole(CraftingRequest craftingRequest){
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
        for (FishingPoleType poleType : FishingPoleTypeColl.get().getAll()) {
            if(poleType.getRarity() > 0){
                poleType.setStartDurability(durability);
                ItemStack prize = poleType.getItemStack();
                ItemStack icon = prize.clone();
                prize.setType(Material.FISHING_ROD);
                CratePrize cratePrize = new CratePrize(
                        prize,
                        icon,
                        poleType.getRarityName().getGlassMaterial(),
                        poleType.getRarity()
                );
                prizes.add(cratePrize);
            }
        }

        //Show the crate animation to the player
        CrateRoulette.animate(craftingRequest.getPlayer(), prizes, 1.0);
    }


    //Max "hardness" is 6, 6 * 5 = 30 possible hardness values
    public static Material getPickaxeMaterialFromHardness(int h){
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

    public static Material getAxeMaterialFromHardness(int h){
        if(h > 28){
            return Material.NETHERITE_AXE;
        }else if (h > 22) {
            return Material.DIAMOND_AXE;
        }else if (h > 16) {
            return Material.GOLDEN_AXE;
        }else if (h > 12) {
            return Material.IRON_AXE;
        }else if (h > 8){
            return Material.STONE_AXE;
        }else {
            return Material.WOODEN_AXE;
        }
    }

    public static Material getHoeMaterialFromHardness(int h){
        if(h > 28){
            return Material.NETHERITE_HOE;
        }else if (h > 22) {
            return Material.DIAMOND_HOE;
        }else if (h > 16) {
            return Material.GOLDEN_HOE;
        }else if (h > 12) {
            return Material.IRON_HOE;
        }else if (h > 8){
            return Material.STONE_HOE;
        }else {
            return Material.WOODEN_HOE;
        }
    }

}
