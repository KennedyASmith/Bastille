package com.kennedysmithjava.prisoncore.cmd;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.EnumWrappers;
import com.comphenix.protocol.wrappers.Pair;
import com.kennedysmithjava.prisoncore.Perm;
import com.kennedysmithjava.prisoncore.PrisonCore;

import com.comphenix.protocol.PacketType;
import com.kennedysmithjava.prisoncore.cmd.type.TypeMPlayer;
import com.kennedysmithjava.prisoncore.crates.CratePrize;
import com.kennedysmithjava.prisoncore.crates.CrateRoulette;
import com.kennedysmithjava.prisoncore.entity.tools.PickaxeType;
import com.kennedysmithjava.prisoncore.entity.tools.PickaxeTypeColl;
import com.kennedysmithjava.prisoncore.util.MapRendererUtil;
import com.massivecraft.massivecore.MassiveException;
import com.massivecraft.massivecore.command.requirement.RequirementHasPerm;
import com.massivecraft.massivecore.command.requirement.RequirementIsPlayer;
import com.massivecraft.massivecore.command.type.primitive.TypeDouble;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.MapMeta;
import org.bukkit.map.MapView;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

public class CmdMapCreate extends CoreCommand {
    // -------------------------------------------- //
    // CONSTRUCT
    // -------------------------------------------- //

    public CmdMapCreate() {

        //Requirement
        this.addRequirements(RequirementIsPlayer.get(), RequirementHasPerm.get(Perm.ADMIN));

        // Parameters
        this.addParameter(TypeDouble.get(), "luck");

        //Description
        this.setDesc("Get a map");
    }

    // -------------------------------------------- //
    // OVERRIDE
    // -------------------------------------------- //

    @Override
    public void perform() throws MassiveException {
        if (!(sender instanceof Player)) return;

        double luck = readArg();


        //Build the list of prizes
        List<CratePrize> prizes = new ArrayList<>();

        for (PickaxeType pickaxeType : PickaxeTypeColl.get().getAll()) {
            if(pickaxeType.getRarity() > 0){
                pickaxeType.setStartDurability(1000);
                ItemStack prize = pickaxeType.getItemStack();
                ItemStack icon = prize.clone();
                prize.setType(Material.DIAMOND_PICKAXE);
                CratePrize cratePrize = new CratePrize(
                        prize,
                        icon,
                        pickaxeType.getRarityName().getGlassMaterial(),
                        pickaxeType.getRarity()
                );
                prizes.add(cratePrize);
            }
        }

        //Show the crate animation to the player
        CrateRoulette.animate((Player) sender, prizes, luck);



        //setItemInHand(me);
    }

    public void setItemInHand(Player player) {

        ItemStack map = new ItemStack(Material.FILLED_MAP);
        MapView mapView = Bukkit.createMap(player.getWorld());
        mapView.getRenderers().clear();

        try {
            BufferedImage image = ImageIO.read(new File(PrisonCore.get().getDataFolder().getPath() + File.separator + "map.png"));
            mapView.addRenderer(new MapRendererUtil(image));
            mapView.setScale(MapView.Scale.CLOSEST);
            // Set map item to the created map
            MapMeta mapMeta = (MapMeta) map.getItemMeta();
            mapMeta.setMapView(mapView);
            map.setItemMeta(mapMeta);
            // Give map item to player
        } catch (IOException e) {
            Bukkit.getLogger().log(Level.INFO, "Can't find: " + PrisonCore.get().getDataFolder().getPath() + File.pathSeparatorChar + "map.png");
        }

        ProtocolManager protocolManager = ProtocolLibrary.getProtocolManager();
        PacketContainer packet = protocolManager.createPacket(PacketType.Play.Server.ENTITY_EQUIPMENT);
        packet.getIntegers().write(0, player.getEntityId());
        List<Pair<EnumWrappers.ItemSlot, ItemStack>> list = new ArrayList<>();
        list.add(new Pair<>(EnumWrappers.ItemSlot.MAINHAND, map));
        packet.getSlotStackPairLists().write(0, list);
        try {
            protocolManager.sendServerPacket(player, packet);
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }

        player.getInventory().addItem(map);
    }
}
