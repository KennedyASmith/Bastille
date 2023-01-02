package com.kennedysmithjava.prisoncore.crafting.objects;

import com.kennedysmithjava.prisoncore.PrisonCore;
import com.kennedysmithjava.prisoncore.crafting.PrisonObject;
import com.kennedysmithjava.prisoncore.crafting.objects.type.MetalType;
import com.kennedysmithjava.prisoncore.util.ItemBuilder;
import com.massivecraft.massivecore.util.MUtil;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.Map;

public class PrisonMetal extends PrisonObject {

    public static final NamespacedKey metalRarityKey = new NamespacedKey(PrisonCore.get(), "pMetalRarity");
    public static final NamespacedKey metalHardnessKey = new NamespacedKey(PrisonCore.get(), "pMetalHardness");
    public static final NamespacedKey metalTypeKey = new NamespacedKey(PrisonCore.get(), "pMetalType");

    private final MetalType type;
    private final Integer hardness;
    private final Integer rarity;

    public PrisonMetal(MetalType type, Integer hardness, Integer rarity){
        this.type = type;
        this.hardness = hardness;
        this.rarity = rarity;
    }

    public PrisonMetal(MetalType type){
        this.type = type;
        this.hardness = type.getHardness();
        this.rarity = type.getRarity();
    }

    @Override
    public ItemStack giveRawItem() {
        return new ItemBuilder(type.getMaterial()).lore(type.getLore()).name(type.getDisplayName()).build();
    }

    @Override
    public String getKey() {
        return "prisonMetal";
    }

    @Override
    public String getName() {
        return type.getDisplayName();
    }

    @Override
    public Map<PersistentDataType<?, ?>, Map<NamespacedKey, ?>> getStoredData() {
        return MUtil.map(
                PersistentDataType.STRING, MUtil.map(metalTypeKey, type.name()),
                PersistentDataType.INTEGER, MUtil.map(
                        metalHardnessKey, hardness,
                        metalRarityKey, rarity)
        );
    }

    @SuppressWarnings("DataFlowIssue")
    @Override
    public boolean is(ItemStack itemStack) {
        if(!super.is(itemStack)) return false;
        PersistentDataContainer pdc = itemStack.getItemMeta().getPersistentDataContainer();
        MetalType thisType = MetalType.valueOf(pdc.get(metalTypeKey, PersistentDataType.STRING));
        if(type.equals(MetalType.ANY)){
            return true;
        }else{
            return thisType.equals(type);
        }
    }
}

