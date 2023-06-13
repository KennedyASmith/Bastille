package com.kennedysmithjava.prisoncore.task;

import com.kennedysmithjava.prisoncore.engine.EngineTrees;
import com.kennedysmithjava.prisoncore.entity.farming.objects.Tree;
import com.massivecraft.massivecore.ModuloRepeatTask;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("unused")
public class TaskRegrowTrees extends ModuloRepeatTask {

    private static final TaskRegrowTrees i = new TaskRegrowTrees();

    public static TaskRegrowTrees get() {
        return i;
    }

    @Override
    public long getDelayMillis() {
        return 1000L;
    }

    @Override
    public void invoke(long l) {
        List<Tree> toRemove = new ArrayList<>();
        EngineTrees.get().getTreeRegenerateTimes().forEach((tree, time) -> {
            if (tree.getRegenerationTime() == 0) return;
            if (System.currentTimeMillis() > tree.getRegenerationTime() + time) {
                EngineTrees.get().pasteTree(tree);
                tree.setNeedsRegeneration(false);
                toRemove.add(tree);
            }
        });
        toRemove.forEach(tree -> EngineTrees.get().getTreeRegenerateTimes().remove(tree));
    }
}
