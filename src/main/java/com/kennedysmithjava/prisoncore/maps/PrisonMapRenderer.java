package com.kennedysmithjava.prisoncore.maps;

import com.kennedysmithjava.prisoncore.entity.player.MPlayer;
import com.kennedysmithjava.prisoncore.quest.Quest;
import com.kennedysmithjava.prisoncore.regions.Region;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.MapMeta;
import org.bukkit.map.*;

import java.util.*;

@SuppressWarnings({"deprecation", "NullableProblems", "DataFlowIssue"})
public class PrisonMapRenderer extends MapRenderer {

    public static String MAP_KEY = "§" + MapPalette.matchColor(190, 190, 190) + ";Key";
    public static byte GRAY_COLOR = MapPalette.matchColor(79, 79, 79);
    public static byte BLACK_COLOR = MapPalette.matchColor(50, 50, 50);
    public static Set<UUID> shouldRenderPlayers = new HashSet<>();
    public static Map<UUID, PrisonMapRenderer> mapRenderers = new HashMap<>();
    public static Set<UUID> reRenderQuest = new HashSet<>();

    private String areaName;
    private int textWidth;

    private boolean clearTextArea = false;

    //** For quests
    //private List<Integer, Color> lastQuestRegionPixels <-Turn all transparent each iteration
    //private Region questRegion
    //


    private Region region;

    private static final int textX = 4;
    public PrisonMapRenderer(boolean contextual, String areaName){
        super(contextual);
        setAreaName(areaName);
    }

    @Override
    public void render(MapView mapView, MapCanvas mapCanvas, Player player) {
        boolean shouldRender = shouldRenderPlayers.contains(player.getUniqueId());
        if(!shouldRender) return;

        Bukkit.broadcastMessage("Rendering map: " + areaName);

        if(reRenderQuest.contains(player.getUniqueId())){
            MPlayer mPlayer = MPlayer.get(player);
            Quest activeQuest = mPlayer.getQuestProfile().getActiveQuest();
            if(activeQuest != null && activeQuest.hasRegion(activeQuest.getProgress())) {
                region = activeQuest.getRegion(activeQuest.getProgress());
            }else {
                region = null;
            }
            reRenderQuest.remove(player.getUniqueId());
        }

        Location loc = player.getLocation();
        int pX = loc.getBlockX();
        int pZ = loc.getBlockZ();

        mapView.setCenterX(pX);
        mapView.setCenterZ(pZ);

        MapCursorCollection cursors = mapCanvas.getCursors();
        while(cursors.size() > 0){
            cursors.removeCursor(cursors.getCursor(0));
        }

        if(region != null){
            boolean canFullyRenderQuestRegion = region.displayOnMap(pX, pZ, mapCanvas);
            Location qCenter = region.getCenterPosition();
            int mapX = -2 * (pX - qCenter.getBlockX());
            int mapZ = -2 * (pZ - qCenter.getBlockZ());
            if(canFullyRenderQuestRegion){
                cursors.addCursor(mapX, mapZ, (byte) 8, MapCursor.Type.RED_MARKER.getValue(), true);
            }else{
                int bestX = (mapX > 105) ? 105 : (Math.max(mapX, -105));
                int bestY = (mapZ > 94) ? 94 : (Math.max(mapZ, -85));
                cursors.addCursor(bestX, bestY, MapUtil.getDirectionFrom(loc, qCenter), MapCursor.Type.RED_MARKER.getValue(), true, "Quest");
            }
        }
        if(clearTextArea){
            for (int x = 0; x < 128; x++) {
                for (int y = 12; y < 128; y++) {
                    mapCanvas.setPixel(x, y, MapPalette.TRANSPARENT);
                }
            }
            clearTextArea = false;
        }

        int barTop = 111;

        // Large gray bar at bottom
        for (int x = 0; x < 128; x++) {
            for (int y = barTop; y < 128; y++) {
                mapCanvas.setPixel(x, y, GRAY_COLOR);
            }
        }

        //Black outline at bottom
        for (int x = 3; x < 126; x++) {
            mapCanvas.setPixel(x, barTop, BLACK_COLOR);
        }

        //Gray bar on left
        for (int x = 0; x < 4; x++) {
            for (int y = 0; y < 128; y++) {
                mapCanvas.setPixel(x, y, GRAY_COLOR);
            }
        }

        //Black bar on left
        for (int y = 2; y < 128; y++) {
            mapCanvas.setPixel(3, y, BLACK_COLOR);
        }

        //Gray bar on right
        for (int x = 125; x < 128; x++) {
            for (int y = 0; y < 128; y++) {
                mapCanvas.setPixel(x, y, GRAY_COLOR);
            }
        }

        //Black bar on right
        for (int y = 2; y < barTop; y++) {
            mapCanvas.setPixel(124, y, BLACK_COLOR);
        }

        int textPadding = 3;
        //Gray bar at top for text
        for (int x = 0; x < textX + textWidth + textPadding; x++) {
            for (int y = 0; y < 13; y++) {
                mapCanvas.setPixel(x, y, GRAY_COLOR);
            }
        }

        //Gray bar at top after text
        for (int x = textX + textWidth + textPadding; x < 128; x++) {
            for (int y = 0; y < 3; y++) {
                mapCanvas.setPixel(x, y, GRAY_COLOR);
            }
        }

        //Black bar at top for text
        for (int x = 3; x < textX + textWidth + textPadding; x++) {
            mapCanvas.setPixel(x, 12, BLACK_COLOR);
        }

        //Black bar at top after text
        for (int x = textX + textWidth + textPadding; x < 125; x++) {
            mapCanvas.setPixel(x, 2, BLACK_COLOR);
        }

        //Black bar after mapkey text
        for (int y = barTop; y < 128; y++) {
            mapCanvas.setPixel(27, y, BLACK_COLOR);
        }

        cursors.addCursor(-51, 105, (byte) 8, MapCursor.Type.MANSION.getValue(), true, "SHOP");
        cursors.addCursor(-17, 105, (byte) 0, MapCursor.Type.SMALL_WHITE_CIRCLE.getValue(), true, "NPC");

        //cursors.addCursor(22, 103, (byte) 0, MapCursor.Type.WHITE_CROSS.getValue(), true, "X : " + pX + " Z:" + pZ);

        mapCanvas.setCursors(cursors);
        mapCanvas.drawText(textX, 2, MinecraftFont.Font, areaName);
        mapCanvas.drawText(7, 116, MinecraftFont.Font, MAP_KEY);
        shouldRenderPlayers.remove(player.getUniqueId());
    }

    public void setQuestRegion(Region region) {
        this.region = region;
    }

    public void setAreaName(String areaName) {
        this.areaName = "§" + MapPalette.matchColor(254, 254, 254) + ";" + areaName;
        this.textWidth = MinecraftFont.Font.getWidth(areaName);
    }

    public void setClearTextArea(boolean clearTextArea) {
        this.clearTextArea = clearTextArea;
    }

    public static ItemStack mapPlayer(Player player, Location loc){
        ItemStack mapItem = new ItemStack(Material.FILLED_MAP);
        MapView mapView = Bukkit.getServer().createMap(loc.getWorld());
        MapMeta mapMeta = (MapMeta) mapItem.getItemMeta();
        mapView.setScale(MapView.Scale.CLOSEST);
        mapView.setCenterX(loc.getBlockX());
        mapView.setCenterZ(loc.getBlockZ());
        mapView.setUnlimitedTracking(true);
        mapView.setTrackingPosition(true);

        PrisonMapRenderer renderer = new PrisonMapRenderer(true, loc.getWorld().getName());
        mapView.addRenderer(renderer);

        // Set the map ID in the map item's metadata
        mapMeta.setMapView(mapView);
        mapItem.setItemMeta(mapMeta);
        shouldRenderPlayers.add(player.getUniqueId());
        reRenderQuest.add(player.getUniqueId());
        mapRenderers.put(player.getUniqueId(), renderer);
        return mapItem;
    }
}
