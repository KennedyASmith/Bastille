package com.kennedysmithjava.prisoncore.entity.player.objects;

import com.kennedysmithjava.prisoncore.entity.player.MPlayer;
import com.massivecraft.massivecore.store.EntityInternal;
import org.bukkit.Material;

public class StorageWrapper extends EntityInternal<MPlayer> {

    private boolean unlocked;
    private Material icon;

    private String name;


    public StorageWrapper(boolean unlocked, Material icon, String name) {
        this.unlocked = unlocked;
        this.icon = icon;
        this.name = name;
    }

    public void setUnlocked(boolean unlocked) {
        this.unlocked = unlocked;
        this.changed();
    }


    public void setIcon(Material icon) {
        this.icon = icon;
        this.changed();
    }

    public void setName(String name) {
        this.name = name;
        this.changed();
    }


    public boolean isUnlocked() {
        return unlocked;
    }

    public String getName() {
        return name;
    }

    public Material getIcon() {
        return icon;
    }
}
