package com.kennedysmithjava.prisoncore.util;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.map.*;

import java.awt.image.BufferedImage;

public class MapRendererUtil extends MapRenderer {

    BufferedImage image;

    public MapRendererUtil(BufferedImage image){
        this.image = image;
    }
    public void render(MapView view, MapCanvas canvas, Player player) {
        canvas.drawImage(0, 0, image);
        view.setCenterX(player.getLocation().getBlockX());
        view.setCenterZ(player.getLocation().getBlockZ());
    }
}
