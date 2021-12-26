package com.kennedysmithjava.prisonmines.blockhandler.event;

import com.avaje.ebean.validation.NotNull;
import com.kennedysmithjava.prisonmines.blockhandler.Reward;
import com.kennedysmithjava.prisonmines.blockhandler.animation.BreakAnimation;
import com.kennedysmithjava.prisonmines.entity.Distribution;
import com.kennedysmithjava.prisonmines.entity.Mine;
import com.kennedysmithjava.prisonmines.entity.MineColl;
import com.kennedysmithjava.prisonmines.entity.PrisonBlock;
import com.kennedysmithjava.prisonmines.util.LazyRegion;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.event.block.BlockBreakEvent;

import java.util.ArrayList;
import java.util.List;

public class MineBlockBreakEvent extends Event implements Cancellable {

    @NotNull
    private final Distribution distribution;
    @NotNull
    private final LazyRegion mineRegion;
    @NotNull
    private final Block block;
    private final Player player;

    private List<Reward> rewards;
    private final List<BreakAnimation> breakAnimations;
    private boolean cancelled = false;
    private boolean shouldDeleteBlock = true;

    private double blockMultiplier = 1;
    private double awardMultiplier = 1;
    private int toolDurabilityLoss = 5;

    public MineBlockBreakEvent(BlockBreakEvent blockBreakEvent, LazyRegion region, Distribution distribution) {
        this(blockBreakEvent.getBlock(), blockBreakEvent.getPlayer(), region, distribution);
    }

    public MineBlockBreakEvent(Block block, Player player, LazyRegion region, Distribution distribution) {
        this.block = block;
        this.player = player;
        this.distribution = distribution;
        this.mineRegion = region;

        this.rewards = new ArrayList<>();
        this.rewards.add(distribution.generatePrisonBlock(block.getType(), block.getData()));

        this.breakAnimations = new ArrayList<>();
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
