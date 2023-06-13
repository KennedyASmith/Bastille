package com.kennedysmithjava.prisoncore.crafting.objects.type;

import com.kennedysmithjava.prisoncore.crafting.Rarity;
import com.massivecraft.massivecore.util.MUtil;
import org.bukkit.Material;

import java.util.List;

public enum FishType {
    //USED FOR CRAFTING
    COD(Material.COD, "&f&lCod", MUtil.list(Rarity.COMMON.getDisplayName(), "&7Fish", " ", "&7A small, silvery fish found in chilly waters."), Rarity.COMMON),
    SALMON(Material.SALMON, "&f&lSalmon", MUtil.list(Rarity.COMMON.getDisplayName(), "&7Fish",  " ", "&7A jumping fish that swims upstream to lay its eggs."), Rarity.COMMON),
    BASS(Material.COOKED_COD, "&f&lBass", MUtil.list(Rarity.COMMON.getDisplayName(), "&7Fish",  " ", "&7A popular sport fish known for its fighting spirit."), Rarity.COMMON),
    TROUT(Material.COOKED_SALMON, "&f&lTrout", MUtil.list(Rarity.COMMON.getDisplayName(), "&7Fish",  " ", "&7A freshwater fish that thrives in clear, cold streams."), Rarity.COMMON),
    CATFISH(Material.COOKED_SALMON, "&f&lCatfish", MUtil.list(Rarity.COMMON.getDisplayName(), "&7Fish",  " ", "&7Often found in muddy waters, this bottom-dweller is strong."), Rarity.COMMON),
    TROPICAL_FISH(Material.TROPICAL_FISH, "&f&lTropical Fish", MUtil.list(Rarity.COMMON.getDisplayName(), "&7Fish",  " ", "&7Colorful and vibrant, this fish brightens up the waters."), Rarity.COMMON),
    PUFFERFISH(Material.PUFFERFISH, "&f&lPufferfish", MUtil.list(Rarity.UNCOMMON.getDisplayName(), "&7Fish",  " ", "&7Inflates itself when threatened, a delicacy for some."), Rarity.UNCOMMON),
    CLOWNFISH(Material.TROPICAL_FISH, "&f&lClownfish", MUtil.list(Rarity.UNCOMMON.getDisplayName(), "&7Fish",  " ", "&7Known for its vibrant orange color and distinctive stripes."), Rarity.UNCOMMON),
    PIKE(Material.COOKED_COD, "&f&lPike", MUtil.list(Rarity.UNCOMMON.getDisplayName(), "&7Fish",  " ", "&7A carnivorous fish with sharp teeth and an aggressive nature."), Rarity.UNCOMMON),
    SNAPPER(Material.SALMON, "&f&lSnapper", MUtil.list(Rarity.UNCOMMON.getDisplayName(), "&7Fish",  " ", "&7Known for its distinctive red coloration and delicious taste."), Rarity.UNCOMMON),
    HERRING(Material.COD, "&f&lHerring", MUtil.list(Rarity.UNCOMMON.getDisplayName(), "&7Fish",  " ", "&7A schooling fish that migrates in vast numbers."), Rarity.UNCOMMON),
    GOLDFISH(Material.GOLD_NUGGET, "&f&lGoldfish", MUtil.list(Rarity.RARE.getDisplayName(), "&7Fish",  " ", "&7A majestic fish that glimmers with a golden sheen."), Rarity.RARE),
    ANGLERFISH(Material.LANTERN, "&f&lAnglerfish", MUtil.list(Rarity.RARE.getDisplayName(), "&7Fish",  " ", "&7Lures prey with a glowing bulb attached to its head."), Rarity.RARE),
    TUNA(Material.COD, "&f&lTuna", MUtil.list(Rarity.RARE.getDisplayName(), " ", "&7Fish",  "&7A fast and powerful fish, highly sought after by anglers."), Rarity.RARE),
    MAHI_MAHI(Material.TROPICAL_FISH, "&f&lMahi-Mahi", MUtil.list(Rarity.RARE.getDisplayName(), "&7Fish",  " ", "&7With its vibrant colors, it's a prized catch in warm waters."), Rarity.RARE),
    GROUPER(Material.COD, "&f&lGrouper", MUtil.list(Rarity.RARE.getDisplayName(), "&7Fish",  " ", "&7A large, stout fish known for its tasty flesh."), Rarity.RARE),
    SWORDFISH(Material.IRON_SWORD, "&f&lSwordfish", MUtil.list(Rarity.RARE.getDisplayName(), "&7Fish",  " ", "&7A long, swift fish with a distinctive pointed snout."), Rarity.RARE),
    JELLYFISH(Material.SLIME_BALL, "&f&lJellyfish", MUtil.list(Rarity.EPIC.getDisplayName(), "&7Fish",  " ", "&7A graceful creature that pulsates through the depths."), Rarity.EPIC),
    MARLIN(Material.ARROW, "&f&lMarlin", MUtil.list(Rarity.EPIC.getDisplayName(), "&7Fish",  " ", "&7A fierce and fast predator, highly prized for its speed."), Rarity.EPIC),
    MANTA_RAY(Material.BLACK_DYE, "&f&lManta Ray", MUtil.list(Rarity.EPIC.getDisplayName(), "&7Fish",  " ", "&7With its graceful movements, it glides through the water."), Rarity.EPIC),
    BARRACUDA(Material.END_ROD, "&f&lBarracuda", MUtil.list(Rarity.EPIC.getDisplayName(), "&7Fish",  " ", "&7A fearsome predator with sharp teeth and a sleek body."), Rarity.EPIC),
    EEL(Material.STRING, "&f&lWhite Eel", MUtil.list(Rarity.EPIC.getDisplayName(), "&7Fish",  " ", "&7This serpentine fish can produce electric shocks for defense."), Rarity.EPIC),
    SWORDTAIL(Material.STONE_SWORD, "&f&lSwordtail", MUtil.list(Rarity.EPIC.getDisplayName(), "&7Fish",  " ", "&7A colorful fish with a distinctive, sword-like tail fin."), Rarity.EPIC),
    SHARK_FIN(Material.PRISMARINE_SHARD, "&f&lShark Fin", MUtil.list(Rarity.LEGENDARY.getDisplayName(), "&7Fish",  " ", "&7The apex predator of the oceans, feared by many."), Rarity.LEGENDARY),
    LIONFISH(Material.PUFFERFISH, "&f&lLionfish", MUtil.list(Rarity.LEGENDARY.getDisplayName(), "&7Fish",  " ", "&7With its venomous spines, it's both beautiful and dangerous."), Rarity.LEGENDARY);

    final String displayName;
    final List<String> lore;
    final Material material;

    final Rarity rarity;

    FishType(Material material, String displayName, List<String> lore, Rarity rarity) {
        this.displayName = displayName;
        this.lore = lore;
        this.material = material;
        this.rarity = rarity;
    }

    public List<String> getLore() {
        return lore;
    }

    public String getDisplayName() {
        return displayName;
    }

    public Material getMaterial() {
        return material;
    }

    public Rarity getRarity() {
        return rarity;
    }
    }

