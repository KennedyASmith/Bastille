package com.kennedysmithjava.prisoncore.entity.mines.objects;

public class UpgradeStatus {

    private boolean purchased;
    private boolean active;

    public UpgradeStatus(boolean purchased, boolean active) {
        this.purchased = purchased;
        this.active = active;
    }

    public boolean isActive() {
        return active;
    }

    public boolean isPurchased() {
        return purchased;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public void setPurchased(boolean purchased) {
        this.purchased = purchased;
    }
}
