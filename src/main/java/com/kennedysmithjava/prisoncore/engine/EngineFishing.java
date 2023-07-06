package com.kennedysmithjava.prisoncore.engine;

import com.kennedysmithjava.prisoncore.PrisonCore;
import com.kennedysmithjava.prisoncore.crafting.objects.PrisonFish;
import com.kennedysmithjava.prisoncore.entity.farming.FishingConf;
import com.kennedysmithjava.prisoncore.entity.farming.objects.FishingArea;
import com.kennedysmithjava.prisoncore.entity.player.MPlayer;
import com.kennedysmithjava.prisoncore.entity.player.Skill;
import com.kennedysmithjava.prisoncore.skill.SkillType;
import com.kennedysmithjava.prisoncore.tools.FishingPole;
import com.kennedysmithjava.prisoncore.util.Color;
import com.kennedysmithjava.prisoncore.util.IntRange;
import com.kennedysmithjava.prisoncore.util.MiscUtil;
import com.kennedysmithjava.prisoncore.util.Pair;
import com.massivecraft.massivecore.Engine;
import org.bukkit.Bukkit;
import org.bukkit.entity.FishHook;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

public class EngineFishing extends Engine {

    //Players that have cast a bobber
    HashMap<UUID, PlayerFishEvent> fishingPlayers = new HashMap<>();
    //Players that are actually legally fishing
    HashMap<UUID, PlayerFishEvent> trulyFishingPlayers = new HashMap<>();

    public EngineFishing(){
        new BukkitRunnable() {

            final Collection<FishingArea> fishingAreas = FishingConf.get().getFishingAreaList().values();

            @Override
            public void run() {
                List<UUID> removeList = new ArrayList<>();
                for (UUID uuid : fishingPlayers.keySet()) {
                    Player player = Bukkit.getPlayer(uuid);
                    PlayerFishEvent event = fishingPlayers.get(uuid);
                    if(player == null || event == null || event.isCancelled()) {
                        removeList.add(uuid);
                        continue;
                    }
                    FishHook hook = event.getHook();
                    if(hook.isInOpenWater()){
                        FishingArea fishArea = null;
                        for (FishingArea fishingArea : fishingAreas) {
                            if(!fishingArea.in(hook.getLocation())) continue;
                            fishArea = fishingArea;
                            break;
                        }
                        if(fishArea == null){
                            player.sendMessage(Color.get("&7[&fFishing&7] &7You probably won't find any fish here..."));
                            event.setCancelled(true);
                        }else{
                            MPlayer mPlayer = MPlayer.get(player);
                            Skill skill = mPlayer.getSkillProfile().getSkill(SkillType.FISHING);
                            if(skill.getCurrentLevel() >= fishArea.getLevelRequired()){
                                if(trulyFishingPlayers.containsKey(uuid)) continue;

                                Inventory inv = player.getInventory();
                                ItemStack item = inv.getItem(player.getInventory().getHeldItemSlot());
                                if(!FishingPole.isPole(item)) continue;
                                FishingPole pole = FishingPole.get(item);
                                int newDurability = pole.getDurability() - 1;
                                if(newDurability <= 0) continue;
                                pole.setDurability(newDurability);
                                FishingPole.addToLoreUpdateQueue(pole, pole.getItem());

                                sendFishingAnimation(player, event, fishArea);
                            }else{
                                event.setCancelled(true);
                                player.sendMessage(Color.get("&7[&fFishing&7] &7Fishing level &e" + fishArea.getLevelRequired() + " &7required for this area."));
                            }
                        }
                    }
                }
                removeList.forEach(uuid -> fishingPlayers.remove(uuid));
            }
        }.runTaskTimerAsynchronously(PrisonCore.get(), 20L, 40L);
    }

    @EventHandler
    public void onPlayerFish(PlayerFishEvent event) {
        if(event.getState() == PlayerFishEvent.State.BITE || event.getState() == PlayerFishEvent.State.CAUGHT_FISH){
            event.setCancelled(true);
            return;
        }
        Player player = event.getPlayer();
        UUID uuid = player.getUniqueId();
        if(event.getState() == PlayerFishEvent.State.REEL_IN){
            fishingPlayers.remove(uuid);
            trulyFishingPlayers.remove(uuid);
            player.sendMessage(Color.get("&7[&fFishing&7] &7Canceled fishing!"));
            return;
        }
        if(event.getState() == PlayerFishEvent.State.FISHING){
            player.sendMessage(Color.get("&7[&fFishing&7] &7&lHow to fish: &7When you get a bite, press &eShift &7while your bobber is over the green bar&7 to reel!"));
            fishingPlayers.put(player.getUniqueId(), event);
        }
    }

    @SuppressWarnings("ConstantValue")
    public void sendFishingAnimation(Player player, PlayerFishEvent event, FishingArea area) {
        trulyFishingPlayers.put(player.getUniqueId(), event);
        UUID uuid = player.getUniqueId();

        new BukkitRunnable() {

            final Skill fishingSkill = MPlayer.get(player).getSkillProfile().getSkill(SkillType.FISHING);

            final static int size = 44;
            final int maxWins = 3;
            final int maxLosses = 3;

            static Pair<Set<Integer>, Set<Integer>> winMarkPair = getWinMarks(size, 4, 8);
            static Set<Integer> winMarks = winMarkPair.getLeft();
            static Set<Integer> semiMarks = winMarkPair.getRight();

            boolean onBar = false;
            boolean playerKnownSneaking = player.isSneaking();

            int wins = 0;

            int losses = 0;
            boolean forwards = true;
            int currentIndex = 0;
            String subtitle;

            @Override
            public void run() {

                if(player == null || !trulyFishingPlayers.containsKey(uuid)){
                    this.cancel();
                    return;
                }

                onBar = winMarks.contains(currentIndex);


                EXIT:
                if(playerKnownSneaking){
                    if(player.isSneaking()) break EXIT;
                    playerKnownSneaking = false;
                } else{
                    if(!player.isSneaking()) break EXIT;
                    playerKnownSneaking = true;
                    if(onBar){
                        if(wins == maxWins - 1){
                            PrisonFish prizeFish = PrisonFish.pickRandom(area);
                            ItemStack prizeItem = prizeFish.give(1);
                            player.sendMessage(Color.get("&7[&b\uD83C\uDFA3&7] &aYou've successfully caught a fish!"));
                            player.sendTitle(
                                    Color.get("&a&lCaught!"),
                                    Color.get("&71x " + prizeFish.getName() + " &7(" + prizeFish.getType().getRarity().getDisplayName() + "&7)"),
                                    0, 20, 5);
                            MiscUtil.givePlayerItem(player, prizeItem, 1);
                            fishingSkill.addXP(25);
                            trulyFishingPlayers.remove(player.getUniqueId());
                            sendFishingAnimation(player, fishingPlayers.get(player.getUniqueId()), area);

                            this.cancel();
                            return;
                        }else {
                            wins++;
                            player.sendMessage(
                                    Color.get("&7[&b\uD83C\uDFA3&7] Keep going! &aYou've made " + wins + "/" + maxWins + " successful reel attempts."));
                            fishingSkill.addXP(5);
                            winMarkPair = getWinMarks(size, 4, 10);
                            winMarks = winMarkPair.getLeft();
                            semiMarks = winMarkPair.getRight();
                        }
                    }else if(semiMarks.contains(currentIndex)){
                        player.sendMessage(Color.get("&7[&b\uD83C\uDFA3&7] &eOops! Try again!"));
                    } else {
                        losses++;
                        player.sendMessage(Color.get("&7[&b\uD83C\uDFA3&7] &cOops! You've tried to reel in too early " + losses + "/" + maxLosses + " times!"));
                    }
                }

                subtitle = getSubtitle(size, winMarks, semiMarks, currentIndex, losses, maxLosses, wins, maxWins);
                player.sendTitle(Color.get("&bYou got a bite!"), Color.get(subtitle), 0, -1, 0);

                if(currentIndex == size - 1 && forwards){
                    forwards = false;
                } else if(currentIndex == 0 && !forwards){
                    forwards = true;
                } else if (forwards){
                    currentIndex ++;
                }else {
                    currentIndex --;
                }
            }
        }.runTaskTimer(PrisonCore.get(), new IntRange(20, 15*20).getRandom(), 1);
    }


    public String getSubtitle(int size, Set<Integer> winMarks, Set<Integer> semiMarks, int currentIndex, int losses, int maxLosses, int wins, int maxWins){

        StringBuilder subtitle = new StringBuilder("&c" + losses + "/" + maxLosses + " &7< &r");
        for (int i = 0; i < size; i++) {
            String character;
            if (currentIndex == i){
                character = "&f⧮";
            } else if(winMarks.contains(i)){
                character = "&a▦";
            } else if(semiMarks.contains(i)){
                character = "&6▥";
            } else{
                character = "&f▢";
            }
            subtitle.append(character);
        }
        subtitle.append("&r &7> &a").append(wins).append("/").append(maxWins);
        return subtitle.toString();
    }


    public static Pair<Set<Integer>, Set<Integer>> getWinMarks(int size, int minBarSize, int maxBarSize){
        Set<Integer> winMarks = new HashSet<>();
        Set<Integer> orangeMarks = new HashSet<>();

        int barSize = new IntRange(minBarSize, maxBarSize).getRandom();
        int orangeBarSize = (int) Math.floor(barSize * 0.5);

        IntRange range = new IntRange(0, size - ((orangeBarSize * 2) + barSize));
        int startIndex = range.getRandom();

        for (int i = startIndex; i <= (startIndex + barSize + (orangeBarSize * 2)); i++) {
            if(i < startIndex + orangeBarSize || i > startIndex + barSize + orangeBarSize){
                orangeMarks.add(i);
            }else{
                winMarks.add(i);
            }
        }

        return new Pair<>(winMarks, orangeMarks);
    }

}

