package com.kennedysmithjava.prisoncore.event;

import com.kennedysmithjava.prisoncore.PrisonCore;
import com.kennedysmithjava.prisoncore.blockhandler.Reward;
import com.kennedysmithjava.prisoncore.crafting.objects.PrisonOre;
import com.kennedysmithjava.prisoncore.crafting.objects.type.OreType;
import com.kennedysmithjava.prisoncore.engine.EngineResearchPoint;
import com.kennedysmithjava.prisoncore.entity.mines.objects.PrisonBlock;
import com.kennedysmithjava.prisoncore.entity.player.MPlayer;
import com.kennedysmithjava.prisoncore.entity.player.Skill;
import com.kennedysmithjava.prisoncore.skill.SkillType;
import com.kennedysmithjava.prisoncore.tools.pouch.Pouch;
import com.kennedysmithjava.prisoncore.tools.pouch.PouchFullException;
import com.kennedysmithjava.prisoncore.tools.pouch.PouchManager;
import com.kennedysmithjava.prisoncore.tools.pouch.Pouchable;
import com.kennedysmithjava.prisoncore.util.Color;
import com.kennedysmithjava.prisoncore.util.IntRange;
import com.kennedysmithjava.prisoncore.util.MiscUtil;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

public class BlockEventFulfiller {

    // -------------------------------------------- //
    // INSTANCE & CONSTRUCT
    // -------------------------------------------- //

    private static final BlockEventFulfiller i = new BlockEventFulfiller();
    private static final EngineResearchPoint rpEngine = EngineResearchPoint.get();

    public static Random random = ThreadLocalRandom.current();

    private BlockEventFulfiller() {
    }

    public static BlockEventFulfiller get() {
        return i;
    }


    // -------------------------------------------- //
    // IMPL
    // -------------------------------------------- //

    /**
     * Handles the things that are defined in the block break event to be executed:
     * - Animations
     * - Deleting Blocks
     * - Item Rewards
     */
    public void handleEventReturn(EventMineBlockBreak finishedEvent) {
        if (finishedEvent.isCancelled()) {
            return;
        }

        Block block = finishedEvent.getBlock();

        finishedEvent.getBreakAnimations().forEach(a -> a.play(block));

        if (finishedEvent.shouldDeleteBlock()) {
            block.setType(Material.AIR);
        }

        this.rewardPlayer(finishedEvent.getPlayer(), finishedEvent.getRewards(), finishedEvent.getBlockMultiplier(), finishedEvent.getAwardMultiplier());


        new BukkitRunnable() {
            @Override
            public void run() {
                double oRandom = random.nextDouble();
                double oreChance = 0.50;
                if(oRandom < oreChance){
                    Player player = finishedEvent.getPlayer();
                    if(player == null) return;
                    MPlayer mPlayer = MPlayer.get(player);
                    Skill skill = mPlayer.getSkillProfile().getSkill(SkillType.MINING);
                    int skillLevel = skill.getCurrentLevel();
                    List<OreType> suitableOres = new ArrayList<>();
                    for (OreType value : OreType.values()) {
                        if(value.getRequiredSkillLevel() <= skillLevel){
                            suitableOres.add(value);
                        }
                    }
                    if(suitableOres.size() > 0){
                        int id = 0;
                        if(suitableOres.size() > 1){
                            id = new IntRange(0, suitableOres.size() - 1).getRandom();
                        }
                        OreType type = suitableOres.get(id);
                        PrisonOre ore = new PrisonOre(type);
                        MiscUtil.givePlayerItem(player, ore.give(1), 1);
                        finishedEvent.getPlayer().sendMessage(Color.get("&7[&b&l⛏&7] &7You found " + ore.getName() + " &7while mining!"));
                    }
                }
            }
        }.runTaskAsynchronously(PrisonCore.get());

        rpEngine.addBlockCount(finishedEvent.getPlayer());
    }

    public void handleEventReturn(EventAbilityUse finishedEvent) {
        if (finishedEvent.isCancelled()) {
            return;
        }

        Block block = finishedEvent.getBlock();
        this.rewardPlayer(finishedEvent.getPlayer(), finishedEvent.getRewards(), finishedEvent.getBlockMultiplier(), finishedEvent.getAwardMultiplier());
    }

    private void rewardPlayer(Player player, List<Reward> r, double blockMultiplier, double awardMultiplier) {

        final List<Reward> rewards = r.stream().filter(Objects::nonNull).collect(Collectors.toList());


        Map<Integer, Pouch> pouches = this.getPouches(player.getInventory());
        pouches.forEach((index, pouch) -> {
            ItemStack item = player.getInventory().getItem(index);

            rewards.removeIf(p -> {
                if (!(p instanceof Pouchable)) {
                    return false;
                }
                try {
                    pouch.pouch((Pouchable) p, item);
                    return true;
                } catch (PouchFullException e) {
                    return false;
                }
            });

        });

        rewards.forEach(e ->{
            if(e instanceof PrisonBlock){
                PrisonBlock pb = (PrisonBlock) e;
                pb.setValue(pb.getValue() * awardMultiplier);
            }
            player.getInventory().addItem(e.getProductItem((int) (blockMultiplier)));
        });
        player.updateInventory();
    }

    private Map<Integer, Pouch> getPouches(PlayerInventory inventory) {
        final PouchManager pouchManager = PouchManager.get();
        final Map<Integer,Pouch> result = new HashMap<>(5);

        for (int i = 0; i < inventory.getSize(); i++) {
            ItemStack item = inventory.getItem(i);
            if (!Pouch.isPouch(item)) {
                continue;
            }

            Pouch pouch = pouchManager.getPouch(item);
            if (pouch == null) {
                continue;
            }

            result.put(i, pouch);
        }

        return result;
    }

    private String combineWithAnd(List<String> strings) {
        if (strings.size() == 0) {
            return "";
        } else if (strings.size() == 1) {
            return strings.get(0);
        } else if (strings.size() == 2) {
            return strings.get(0) + " and " + strings.get(1);
        } else {
            StringBuilder combinedString = new StringBuilder();
            for (int i = 0; i < strings.size() - 1; i++) {
                combinedString.append(strings.get(i)).append(", ");
            }
            combinedString.append("and ").append(strings.get(strings.size() - 1));
            return combinedString.toString();
        }
    }

}
