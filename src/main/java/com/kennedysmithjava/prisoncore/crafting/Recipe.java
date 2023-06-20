package com.kennedysmithjava.prisoncore.crafting;

import com.kennedysmithjava.prisoncore.crafting.objects.*;
import com.kennedysmithjava.prisoncore.crafting.objects.type.EssenceType;
import com.kennedysmithjava.prisoncore.crafting.objects.type.LogType;
import com.kennedysmithjava.prisoncore.crafting.objects.type.MetalType;
import com.kennedysmithjava.prisoncore.crafting.objects.type.StickType;
import com.kennedysmithjava.prisoncore.eco.Cost;
import com.kennedysmithjava.prisoncore.engine.EngineBlacksmith;
import com.kennedysmithjava.prisoncore.tools.enchantment.PickaxeEfficiencyEnchant;
import com.massivecraft.massivecore.util.MUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

public enum Recipe {

    PICKAXE(MUtil.map(
            12, new PrisonMetal(MetalType.ANY),
            13, new PrisonMetal(MetalType.ANY),
            14, new PrisonMetal(MetalType.ANY),
            20, new PrisonMetal(MetalType.ANY),
            24, new PrisonMetal(MetalType.ANY),
            22, new PrisonStick(StickType.WOOD),
            31, new PrisonStick(StickType.WOOD),
            40, new PrisonStick(StickType.WOOD)), () -> new ProductItem(EngineBlacksmith::beginCrafting)),

    STICK(MUtil.map(22, new PrisonWoodenPlank()), () -> giveItem(new PrisonStick(StickType.WOOD), 1)),
    WOODEN_PLANK_4X(MUtil.map(22, new PrisonLog(LogType.ANY)), () -> giveItem(new PrisonWoodenPlank(), 4)),
    WOODEN_AXE(MUtil.map(
            12, new PrisonWoodenPlank(),
            13, new PrisonWoodenPlank(),
            21, new PrisonWoodenPlank(),
            22, new PrisonStick(StickType.WOOD),
            31, new PrisonStick(StickType.WOOD),
            40, new PrisonStick(StickType.WOOD)
    ), () -> new ProductItem(craftingRequest -> craftingRequest.give(new PrisonWoodenAxe().give(1)))),
    WOODEN_SCYTHE(MUtil.map(
            12, new PrisonWoodenPlank(),
            13, new PrisonWoodenPlank(),
            21, new PrisonWoodenPlank(),
            22, new PrisonStick(StickType.WOOD),
            31, new PrisonStick(StickType.WOOD),
            40, new PrisonStick(StickType.WOOD)
    ), () -> new ProductItem(craftingRequest -> craftingRequest.give(new PrisonWoodenScythe().give(1)))),
    FISHING_ROD(MUtil.map(
            30, new PrisonStick(StickType.WOOD),
            22, new PrisonStick(StickType.WOOD),
            14, new PrisonStick(StickType.WOOD),
            38, new PrisonWoodenPlank()
    ), () -> new ProductItem(craftingRequest -> craftingRequest.give(new PrisonWoodenScythe().give(1)))),
    SAWDUST(MUtil.map(22, new PrisonLog(LogType.ANY)), () -> giveItem(new PrisonSawdust(), 3)),
    BOWL(MUtil.map(
            21, new PrisonWoodenPlank(),
            23, new PrisonWoodenPlank(),
            31, new PrisonWoodenPlank()
    ), () -> giveItem(new PrisonBowl(), 1)),
    ENCHANT_BOOK_EFFICIENCY(MUtil.map(
            12, new PrisonFeather(),
            14, new PrisonFeather(),
            30, new PrisonFeather(),
            32, new PrisonFeather(),
            31, new PrisonEssence(EssenceType.EARTH),
            13, new PrisonEssence(EssenceType.EARTH),
            21, new PrisonEssence(EssenceType.EARTH),
            23, new PrisonEssence(EssenceType.EARTH),
            22, new PrisonBook()
    ), () -> new ProductItem(cr ->
            PrisonEnchantBook.beginCrafting(PickaxeEfficiencyEnchant.get(), cr)),
            PickaxeEfficiencyEnchant.get().getCraftCosts())
    ;

    //Key: GUI Slot ||| Value: Ingredient for that slot
    private final Map<Integer, PrisonObject> ingredients;

    private final Supplier<ProductItem> product;

    private final List<Cost> additionalCosts;

    Recipe(Map<Integer, PrisonObject> ingredients, Supplier<ProductItem> product) {
        this.ingredients = ingredients;
        this.product = product;
        this.additionalCosts = new ArrayList<>();
    }

    Recipe(Map<Integer, PrisonObject> ingredients, Supplier<ProductItem> product, List<Cost> additionalCosts) {
        this.ingredients = ingredients;
        this.product = product;
        this.additionalCosts = additionalCosts;
    }

    private static ProductItem giveItem(PrisonObject obj, int amount){
        return new ProductItem(craftingRequest -> craftingRequest.give(obj.give(amount)));
    }
    public Map<Integer, PrisonObject> getIngredients() {
        return ingredients;
    }

    public ProductItem getProduct() {
        return product.get();
    }

    public List<Cost> getAdditionalCosts() {
        return additionalCosts;
    }
}


