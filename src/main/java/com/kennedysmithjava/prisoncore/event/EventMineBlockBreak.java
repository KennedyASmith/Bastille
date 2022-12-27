package com.kennedysmithjava.prisoncore.event;

import com.kennedysmithjava.prisoncore.blockhandler.Reward;
import com.kennedysmithjava.prisoncore.blockhandler.animation.BreakAnimation;
import com.kennedysmithjava.prisoncore.entity.mines.Distribution;
import com.kennedysmithjava.prisoncore.entity.mines.Mine;
import com.kennedysmithjava.prisoncore.entity.mines.MineColl;
import com.kennedysmithjava.prisoncore.entity.mines.objects.PrisonBlock;
import com.kennedysmithjava.prisoncore.util.regions.LazyRegion;
import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.event.block.BlockBreakEvent;

import java.util.ArrayList;
import java.util.List;

public class EventMineBlockBreak extends Event implements Cancellable {

    private final Distribution distribution;
    private final LazyRegion mineRegion;
    private final Block block;
    private final Player player;

    private List<Reward> rewards;
    private final List<BreakAnimation> breakAnimations;
    private boolean cancelled = false;
    private boolean shouldDeleteBlock = true;

    private double blockMultiplier = 1;
    private double awardMultiplier = 1;
    private int toolDurabilityLoss = 5;

    public EventMineBlockBreak(BlockBreakEvent blockBreakEvent, LazyRegion region, Distribution distribution) {
        this(blockBreakEvent.getBlock(), blockBreakEvent.getPlayer(), region, distribution);
    }

    public EventMineBlockBreak(Block block, Player player, LazyRegion region, Distribution distribution) {
        this.block = block;
        this.player = player;
        this.distribution = distribution;
        this.mineRegion = region;

        this.rewards = new ArrayList<>();
        this.rewards.add(distribution.generatePrisonBlock(block.getType(), block.getBlockData()));
        this.breakAnimations = new ArrayList<>();

        Bukkit.broadcastMessage("Distribution: " + distribution.getRates().toString());
        Bukkit.broadcastMessage("Rewards: " + rewards.toString());
    }


    // ----------------------- //
    // BOILER PLATE
    // ----------------------- //

    public Mine tryGetMine() {
        return MineColl.get().getByLocation(this.getBlock());
    }

    public LazyRegion getMineRegion() {
        return this.mineRegion;
    }

    public boolean shouldDeleteBlock() {
        return this.shouldDeleteBlock;
    }

    public void setShouldDeleteBlock(boolean shouldDeleteBlock) {
        this.shouldDeleteBlock = shouldDeleteBlock;
    }

    public void addBlockBreakAnimation(BreakAnimation animation) {
        this.breakAnimations.add(animation);
    }

    public void clearAnimations() {
        this.breakAnimations.clear();
    }

    public List<BreakAnimation> getBreakAnimations() {
        return this.breakAnimations;
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
        return distribution;
    }

    public Block getBlock() {
        return block;
    }

    public Player getPlayer() {
        return player;
    }

    @Override
    public boolean isCancelled() {
        return this.cancelled;
    }

    @Override
    public void setCancelled(boolean cancel) {
        this.cancelled = cancel;
    }

    //-----------------------//
    // Multipliers
    //-----------------------//

    public void setAwardMultiplier(double awardMultiplier) {
        this.awardMultiplier = awardMultiplier;
    }

    public void setBlockMultiplier(double blockMultiplier) {
        this.blockMultiplier = blockMultiplier;
    }

    public double getAwardMultiplier() {
        return awardMultiplier;
    }

    public double getBlockMultiplier() {
        return blockMultiplier;
    }

    public int getToolDurabilityLoss() {
        return toolDurabilityLoss;
    }

    public void setToolDurabilityLoss(int toolDurabilityLoss) {
        this.toolDurabilityLoss = toolDurabilityLoss;
    }

    //-----------------------//
    // HANDLERS
    //-----------------------//

    private static final HandlerList handlers = new HandlerList();

    public static HandlerList getHandlerList() {
        return handlers;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    // ----------------------- //
    // END
    // ----------------------- //
}
