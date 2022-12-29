package com.kennedysmithjava.prisoncore.crafting.objects;

import com.kennedysmithjava.prisoncore.PrisonCore;
import com.kennedysmithjava.prisoncore.crafting.PrisonObject;
import com.kennedysmithjava.prisoncore.crafting.objects.type.LogType;
import com.kennedysmithjava.prisoncore.util.ItemBuilder;
import com.massivecraft.massivecore.util.MUtil;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;

import java.util.Map;

public class PrisonLog extends PrisonObject<PrisonLog> {

    private static final NamespacedKey logKey = new NamespacedKey(PrisonCore.get(), "logKey");

    private final LogType type;

    public PrisonLog(LogType type){
        this.type = type;
    }

    @Override
    public ItemStack giveRawItem() {
        return new ItemBuilder(type.getMaterial()).lore(type.getLore()).name(type.getName()).build();
    }

    @Override
    public String getKey() {
        return "log";
    }

    @Override
    public String getName() {
        return type.getName();
    }

    @Override
    public Map<PersistentDataType<?, ?>, Map<NamespacedKey, ?>> getStoredData() {
        return MUtil.map(PersistentDataType.STRING, MUtil.map(logKey, type.name()));
    }

    public static ItemStack giveFromMaterial(Material material) {
        return new PrisonLog(LogType.getFromMaterial(material)).give();
    }

    @Override
    public boolean is(ItemStack itemStack) {
        if(!super.is(itemStack)) return false;

        LogType thisType = LogType.getFromMaterial(itemStack.getType());

        if(type.equals(LogType.ANY)){
            return true;
        }else{
            return thisType.equals(type);
        }
    }
}

