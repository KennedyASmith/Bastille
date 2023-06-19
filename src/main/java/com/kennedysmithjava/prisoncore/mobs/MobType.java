package com.kennedysmithjava.prisoncore.mobs;

import com.kennedysmithjava.prisoncore.crafting.PrisonObject;
import com.kennedysmithjava.prisoncore.crafting.objects.*;
import com.kennedysmithjava.prisoncore.crafting.objects.type.EssenceType;
import com.kennedysmithjava.prisoncore.crafting.objects.type.RawFoodType;
import com.massivecraft.massivecore.util.MUtil;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

public enum MobType {

    PIG(EntityType.PIG,
            MUtil.map(
                    1.0, new PrisonRawFood(RawFoodType.PORKCHOP),
                    0.25, new PrisonEssence(EssenceType.EARTH),
                    0.10, new PrisonFoot(),
                    0.10, new PrisonEye(),
                    0.01, new PrisonHeart()
            )),
    SHEEP(EntityType.SHEEP,
            MUtil.map(
                    1.0, new PrisonRawFood(RawFoodType.MUTTON),
                    0.25, new PrisonEssence(EssenceType.AIR),
                    0.10, new PrisonFoot(),
                    0.10, new PrisonEye(),
                    0.01, new PrisonHeart()
            )),
    RABBIT(EntityType.RABBIT,
            MUtil.map(
                    1.0, new PrisonRawFood(RawFoodType.RABBIT),
                    0.25, new PrisonEssence(EssenceType.AIR),
                    0.10, new PrisonFoot(),
                    0.10, new PrisonEye(),
                    0.01, new PrisonHeart()
            ))
    ;


    private static final Map<EntityType, MobType> typeMapping = new HashMap<>();
    static {
        for (MobType value : MobType.values()) {
            typeMapping.put(value.getType(), value);
        }
    }
    private final EntityType type;
    private final Map<Double, PrisonObject> drops;

    MobType(EntityType type, Map<Double, PrisonObject> drops) {
        this.type = type;
        this.drops = drops;
    }

    public EntityType getType() {
        return type;
    }

    public List<ItemStack> getDrops(){
        List<ItemStack> mDrops = new ArrayList<>();
        for (Map.Entry<Double, PrisonObject> po : drops.entrySet()) {
            double random = ThreadLocalRandom.current().nextDouble();
            if(po.getKey() > random) mDrops.add(po.getValue().give(1));
        }
        return mDrops;
    }

    public static MobType get(EntityType type){
        return typeMapping.get(type);
    }
}
