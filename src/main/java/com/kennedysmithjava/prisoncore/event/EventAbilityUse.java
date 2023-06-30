package com.kennedysmithjava.prisoncore.event;

import com.kennedysmithjava.prisoncore.blockhandler.Reward;
import com.kennedysmithjava.prisoncore.entity.mines.Distribution;
import com.kennedysmithjava.prisoncore.entity.mines.Mine;
import com.kennedysmithjava.prisoncore.entity.mines.MineColl;
import com.kennedysmithjava.prisoncore.entity.mines.objects.PrisonBlock;
import com.kennedysmithjava.prisoncore.regions.LazyRegion;
import com.kennedysmithjava.prisoncore.tools.Pickaxe;
import com.kennedysmithjava.prisoncore.ability.LeveledAbility;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.ArrayList;
import java.util.List;

public class EventAbilityUse extends Event implements Cancellable {

    private final Distribution distribution;

    private final LazyRegion mineRegion;

    private final Block block;
    private Pickaxe pickaxe;
    private LeveledAbility leveledAbility;
    private final Player player;
    private List<Reward> rewards;
    private boolean cancelled;
    private double blockMultiplier;
    private double awardMultiplier;
    private double oreChance;
    private int toolDurabilityLoss;
    private final List<Location> affectedBlocks;
    private static final HandlerList handlers = new HandlerList();

    public EventAbilityUse(PlayerInteractEvent playerInteractEvent, LazyRegion region, Distribution distribution) {
        this(playerInteractEvent.getClickedBlock(), playerInteractEvent.getPlayer(), region, distribution);
    }

    public EventAbilityUse(Block block, Player player, LazyRegion region, Distribution distribution) {
        this.cancelled = false;
        this.blockMultiplier = 1.0D;
        this.awardMultiplier = 1.0D;
        this.toolDurabilityLoss = 5;
        this.oreChance = 0.05;
        this.block = block;
        this.player = player;
        this.distribution = distribution;
        this.mineRegion = region;
        this.rewards = new ArrayList<>();
        this.rewards.add(distribution.generatePrisonBlock(block.getType(), block.getBlockData()));
        this.affectedBlocks = new ArrayList<>();
    }

    public Mine tryGetMine() {
        return MineColl.get().getByLocation(this.getBlock());
    }

    public LazyRegion getMineRegion() {
        return this.mineRegion;
    }

    public void clearRewards() {
        this.rewards.clear();
    }

    public void addReward(PrisonBlock reward) {
        this.rewards.add(reward);
    }

    public List<Reward> getRewards() {
        return this.rewards;
    }

    public void setRewards(List<Reward> rewards) {
        this.rewards = rewards;
    }

    public Distribution getDistribution() {
        return this.distribution;
    }

    public Block getBlock() {
        return this.block;
    }

    public Player getPlayer() {
        return this.player;
    }

    public boolean isCancelled() {
        return this.cancelled;
    }

    public void setOreChance(double oreChance) {
        this.oreChance = oreChance;
    }

    public double getOreChance() {
        return oreChance;
    }

    public void setCancelled(boolean cancel) {
        this.cancelled = cancel;
    }

    public void setAwardMultiplier(double awardMultiplier) {
        this.awardMultiplier = awardMultiplier;
    }

    public void setBlockMultiplier(double blockMultiplier) {
        this.blockMultiplier = blockMultiplier;
    }

    public double getAwardMultiplier() {
        return this.awardMultiplier;
    }

    public double getBlockMultiplier() {
        return this.blockMultiplier;
    }

    public int getToolDurabilityLoss() {
        return this.toolDurabilityLoss;
    }

    public void addAffectedBlock(Location location){
        affectedBlocks.add(location);
    }

    public List<Location> getAffectedBlocks() {
        return affectedBlocks;
    }

    public void setToolDurabilityLoss(int toolDurabilityLoss) {
        this.toolDurabilityLoss = toolDurabilityLoss;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    public HandlerList getHandlers() {
        return handlers;
    }

    public void fulfill(){
        if(!this.isCancelled()){



            this.setCancelled(true);
        }
    }
}
