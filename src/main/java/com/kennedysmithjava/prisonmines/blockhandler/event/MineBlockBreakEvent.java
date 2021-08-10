package com.kennedysmithjava.prisonmines.blockhandler.event;

import com.avaje.ebean.validation.NotNull;
import com.kennedysmithjava.prisonmines.blockhandler.animation.BreakAnimation;
import com.kennedysmithjava.prisonmines.entity.blocks.Distribution;
import com.kennedysmithjava.prisonmines.entity.blocks.PrisonBlock;
import com.kennedysmithjava.prisonmines.entity.mine.Mine;
import com.kennedysmithjava.prisonmines.entity.mine.MineColl;
import com.kennedysmithjava.prisonmines.util.LazyRegion;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.event.block.BlockBreakEvent;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MineBlockBreakEvent extends Event implements Cancellable {

    @NotNull
    private final Distribution distribution;
    @NotNull
    private final LazyRegion mineRegion;
    @NotNull
    private final BlockBreakEvent parentEvent;
    private final List<PrisonBlock> rewards;
    private final List<BreakAnimation> breakAnimations;
    private boolean cancelled = false;
    private boolean shouldDeleteBlock = true;

    public MineBlockBreakEvent(BlockBreakEvent blockBreakEvent, LazyRegion region, Distribution distribution) {
        this.parentEvent = blockBreakEvent;
        this.distribution = distribution;
        this.mineRegion = region;

        this.rewards = new ArrayList<>(Collections.singletonList(distribution.generatePrisonBlock(blockBreakEvent.getBlock().getType(), blockBreakEvent.getBlock().getData())));
        this.breakAnimations = new ArrayList<>();
    }

    // ----------------------- //
    // BOILER PLATE
    // ----------------------- //

    public Mine tryGetMine() {
        return MineColl.get().getByLocation(this.parentEvent.getBlock());
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

    public List<PrisonBlock> getRewards() {
        return this.rewards;
    }

    public Distribution getDistribution() {
        return distribution;
    }

    public BlockBreakEvent getParentEvent() {
        return parentEvent;
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
