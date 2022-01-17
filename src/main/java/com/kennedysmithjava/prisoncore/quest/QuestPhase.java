package com.kennedysmithjava.prisoncore.quest;

import com.kennedysmithjava.prisoncore.PrisonCore;
import com.kennedysmithjava.prisoncore.entity.player.MPlayer;
import com.kennedysmithjava.prisoncore.util.TimeUnit;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;


public abstract class QuestPhase {


    QuestProfile profile;
    QuestPhaseGroup questGroup;
    boolean cancelled;
    boolean completed;
    boolean counterPaused;
    BukkitTask task;
    MPlayer player;
    int counter;

    public QuestPhase(QuestPhaseGroup group) {
        this.questGroup = group;
    }

    public void startThisPhase(QuestProfile profile) {
        this.cancelled = false;
        this.completed = false;
        this.profile = profile;
        this.player = profile.getPlayer();
        int delay = 1;
        initialize();

        this.task = new BukkitRunnable() {
            @Override
            public void run() {
                if (!counterPaused) {
                    counter++;
                }
                if(player.getPlayer() == null){this.cancel(); interrupted();}
                execute();
            }
        }.runTaskTimer(PrisonCore.get(), delay, 1);
    }

    /**
     * This will cancel the current event.
     */
    void cancel() {
        this.cancelled = true;
        task.cancel();
        interrupted();
    }

    /**
     * This will cancel the task. #execute() will no longer be run.
     */
    void cancelTask() {
        task.cancel();
    }

    /**
     * This method is called the first time that this event starts.
     */
    public abstract void initialize();

    /**
     * This method is called repeatedly until this event either finishes or is canceled.
     * Will be marked as interrupted and cancel this task if the player is null.
     */
    public void execute() {

    }

    /**
     * Called when the event ends because cancel() was called.
     */
    public void interrupted() {

    }

    /**
     * Returns whether or not this event has been canceled.
     */
    boolean isCancelled() {
        return cancelled;
    }

    /**
     * Calculates time passed since this event began
     *
     * @param unit
     * @return Time passed since this event began, in the div-floored equivalent of the {@link TimeUnit}
     * <p>
     * Example- 5000 ticks on the counter:
     * TimeUnit.SECONDS would return 250
     * TimeUnit.MINUTES would return 4
     * TimeUnit.HOURS would return 0
     */
    public int getCounter(TimeUnit unit) {

        switch (unit) {
            case TICKS:
                return counter;
            case SECONDS:
                return Math.floorDiv(counter, 20);
            case MINUTES:
                return Math.floorDiv(counter, 1200);
            case HOURS:
                return Math.floorDiv(counter, 72000);
        }

        return counter;
    }

    public void pauseCounter(boolean paused) {
        counterPaused = paused;
    }

    public boolean isCounterPaused() {
        return counterPaused;
    }

    public void setCounter(int value, TimeUnit unit) {
        switch (unit) {
            case TICKS:
                counter = value;
                return;
            case SECONDS:
                counter = value * 20;
                return;
            case MINUTES:
                counter = value * 1200;
                return;
            case HOURS:
                counter = value * 72000;
        }
    }

    public MPlayer getPlayer() {
        return player;
    }

    public BukkitTask getTask() {
        return task;
    }

    public QuestPhaseGroup getQuestGroup() {
        return questGroup;
    }

    public boolean isCompleted() {
        return completed;
    }

    public QuestProfile getProfile() {
        return profile;
    }

    public void completed(boolean completed) {
        if (completed) {
            this.cancelled = true;
            task.cancel();
            profile.nextPhase(questGroup, this);
        }
    }

}
