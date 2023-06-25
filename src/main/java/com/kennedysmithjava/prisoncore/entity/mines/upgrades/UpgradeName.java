package com.kennedysmithjava.prisoncore.entity.mines.upgrades;

import com.kennedysmithjava.prisoncore.eco.Cost;
import com.kennedysmithjava.prisoncore.eco.CostCurrency;
import com.kennedysmithjava.prisoncore.eco.CostSkillLevel;
import com.kennedysmithjava.prisoncore.eco.CurrencyType;
import com.kennedysmithjava.prisoncore.skill.SkillType;
import com.massivecraft.massivecore.util.MUtil;

import java.util.ArrayList;
import java.util.List;

public enum UpgradeName {

    /* Upgrade Categories */
    CATEGORY_BUILDINGS,
    CATEGORY_ARCHITECTURE,
    CATEGORY_REGENERATION,
    CATEGORY_FAST_TRAVEL,
    CATEGORY_MOBILITY,
    CATEGORY_ROBOTICS,
    CATEGORY_COLLECTION,
    CATEGORY_ENVIRONMENT,

    /* BUILDINGS */
    BUILDING_PORTAL,
    BUILDING_ENCHANT_TABLE,
    BUILDING_ANVIL,
    BUILDING_BEACON,
    BUILDING_STORAGE,
    BUILDING_FURNACE,

    /* ARCHITECTURE */
    ARCHITECTURE_HEIGHT_INCREASE,
    ARCHITECTURE_HEIGHT_DECREASE,
    ARCHITECTURE_WIDTH_INCREASE,
    ARCHITECTURE_WIDTH_DECREASE,
    ARCHITECTURE_PATHS,
    ARCHITECTURE_WALLS,

    /* REGENERATION */
    REGENERATION_MANUAL,
    REGENERATION_MANUAL_ANIMATION_SPEED_1,
    REGENERATION_MANUAL_ANIMATION_SPEED_2,
    REGENERATION_MANUAL_ANIMATION_SPEED_3,
    REGENERATION_MANUAL_ANIMATION_SPEED_4,
    REGENERATION_MANUAL_ANIMATION_SPEED_5,
    REGENERATION_MANUAL_ANIMATION_SPEED_6,
    REGENERATION_MANUAL_ANIMATION_SPEED_7,
    REGENERATION_MANUAL_ANIMATION_DELAY_1,
    REGENERATION_MANUAL_ANIMATION_DELAY_2,
    REGENERATION_MANUAL_ANIMATION_DELAY_3,
    REGENERATION_MANUAL_ANIMATION_DELAY_4,
    REGENERATION_MANUAL_ANIMATION_DELAY_5,
    REGENERATION_MANUAL_ANIMATION_DELAY_6,
    REGENERATION_MANUAL_ANIMATION_DELAY_7,
    REGENERATION_AUTO,
    REGENERATION_AUTO_SPEED_1,
    REGENERATION_AUTO_SPEED_2,
    REGENERATION_AUTO_SPEED_3,
    REGENERATION_AUTO_SPEED_4,
    REGENERATION_AUTO_SPEED_5,
    REGENERATION_AUTO_SPEED_6,
    REGENERATION_AUTO_SPEED_7,

    /* Mobility */
    MOBILITY_LADDER_1("&6Ladders 1", MUtil.list(new CostCurrency(CurrencyType.CASH, 10), new CostSkillLevel(SkillType.PLAYER, 10))),
    MOBILITY_LADDER_2("&6Ladders 2", MUtil.list(new CostCurrency(CurrencyType.CASH, 10), new CostSkillLevel(SkillType.PLAYER, 10))),
    MOBILITY_LADDER_3("&6Ladders 3", MUtil.list(new CostCurrency(CurrencyType.CASH, 10), new CostSkillLevel(SkillType.PLAYER, 10))),
    MOBILITY_JUMP_PAD("&6Jump Pad", MUtil.list(new CostCurrency(CurrencyType.CASH, 10), new CostSkillLevel(SkillType.PLAYER, 10))),
    MOBILITY_FLIGHT("&6Flight", MUtil.list(new CostCurrency(CurrencyType.CASH, 10), new CostSkillLevel(SkillType.PLAYER, 10))),

    /* COLLECTION */
    COLLECTION_DEFAULT,
    COLLECTION_SELECT_ALL,
    COLLECTION_SELL_ALL,
    COLLECTION_AUTO_1,
    COLLECTION_AUTO_2,
    COLLECTION_AUTO_3,
    COLLECTION_AUTO_4,

    /* Storage */
    STORAGE_1,
    STORAGE_2,
    STORAGE_3,
    STORAGE_4,
    STORAGE_5,
    STORAGE_6,
    STORAGE_7,
    STORAGE_8,
    STORAGE_9,

    STORAGE_PRESTIGE;

    private final List<Cost> costs;
    private final String displayName;
    UpgradeName(){
        this.costs = new ArrayList<>();
        this.displayName = get();
    }

    UpgradeName(String displayName, List<Cost> costs){
        this.costs = costs;
        this.displayName = displayName;
    }

    public String get(){
        return this.name();
    }

    public List<Cost> getCosts() {
        return costs;
    }

    public String getDisplayName() {
        return displayName;
    }
}
