package com.kennedysmithjava.prisoncore.task;

import com.kennedysmithjava.prisoncore.entity.farming.FarmingConf;
import com.kennedysmithjava.prisoncore.entity.farming.objects.Seed;
import com.massivecraft.massivecore.ModuloRepeatTask;
import com.massivecraft.massivecore.util.MUtil;

import java.util.Set;

public class TaskReGrowWheat extends ModuloRepeatTask {

    private static TaskReGrowWheat i = new TaskReGrowWheat();

    public static TaskReGrowWheat get()
    {
        return i;
    }

    @Override
    public long getDelayMillis()
    {
        return 1000l;
    }

    @Override
    public void invoke(long l) {

        if(FarmingConf.get().seeds.isEmpty()) return;

        // Set of seed(s) to remove
        Set<Seed> toRemove = MUtil.set();

        // Adds all the seeds back that are past their grow time
        for(Seed n : FarmingConf.get().seeds) {
            if(System.currentTimeMillis() > n.getTimeToTurnIntoWheat()) {
                FarmingConf.get().addWheat(n);
                toRemove.add(n);
            }
        }

        // Removes the seeds
        toRemove.forEach(n -> {
            FarmingConf.get().seeds.remove(n);
        });

        // Updates the config entity
        FarmingConf.get().save();
    }

}
