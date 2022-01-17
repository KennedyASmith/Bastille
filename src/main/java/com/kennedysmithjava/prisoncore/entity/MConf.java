package com.kennedysmithjava.prisoncore.entity;

import com.kennedysmithjava.prisoncore.util.regions.Offset;
import com.massivecraft.massivecore.command.editor.annotation.EditorName;
import com.massivecraft.massivecore.ps.PS;
import com.massivecraft.massivecore.store.Entity;
import com.massivecraft.massivecore.util.MUtil;
import org.bukkit.Bukkit;
import org.bukkit.Location;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@EditorName("config")
public class MConf extends Entity<MConf>
{
    // -------------------------------------------- //
    // META
    // -------------------------------------------- //

    protected static transient MConf i;
    public static MConf get() { return i; }

    @Override
    public MConf load(MConf that)
    {
        super.load(that);
        return this;
    }

    public List<String> limboMessage = MUtil.list(

            "\n \n \n \n \n \n",
            "&r &r &r &r &r &r &r &r &r &r &r &r &r &r &r &r &r &r &r &r &r &r &r &r &r &r &4&lMC&f&lRIVALS &8| &cPrisons",
            "&r",
            "&r &r &r &r &r &r &r &r &r &r &r &r &r &r &r &r &r &r &r &r &r &r &r &7Welcome, &f%player%!",
            "&r &r &r &r &r &r &r &r &r &r &r &r &r &eSpeak to Warren to begin your journey.",
            "&r",
            "&r &r &r &r &r &r &r &r &r &r &r &r &r &r &r &r &r &r &r &r &4&lWEBSITE&7: &fwww.mcrivals.com",
            "&r &r &r &r &r &r &r &r &r &r &r &r &r &r &r &r &r &1&lDISCORD&7: &fdiscord.mcrivals.com",
            "&r"
    );

    public PS limboLocation = PS.valueOf(new Location(Bukkit.getWorld("Limbo"), -4.5, 73.0, 92.5, -179.0F, 0.0F));
    public PS spawnLocation = PS.valueOf(new Location(Bukkit.getWorld("Spawn"), -4.5, 73.0, 92.5, -179.0F, 0.0F));
    public String timeZone = "America/New_York";

    // -------------------------------------------- //
    // COMMAND ALIASES
    // -------------------------------------------- //

    public List<String> aliasesM = MUtil.list("mine", "m");
    public List<String> aliasesMAdmin = MUtil.list("mines");
    public List<String> aliasesOffset = MUtil.list("o", "offset");

    // -------------------------------------------- //
    // MINE SETTINGS
    // -------------------------------------------- //

    public Long defaultResetTimer = 25L;
    public int mineNameLengthMin = 3;
    public int mineNameLengthMax = 9;
    public int mineDefaultHeight = 3;
    public int mineDefaultWidth = 3;
    public int mineDefaultWallID = 1;
    public int mineDefaultPathID = 1;
    public String mineDefaultName = "%player%'s Mine";

    public boolean weatherEnabled = false;
    public Map<String, String> minesWorldDefaultGamerules = MUtil.map("doMobSpawning", "false", "doDaylightCycle", "true", "doMobLoot", "false", "doTileDrops", "false", "keepInventory", "true", "mobGriefing", "false");

    public List<String> aliasesP = MUtil.list("pickaxe", "pick");
    public List<String> aliasesE = MUtil.list("enchant");
    public List<String> aliasesA = MUtil.list("ability");
    public List<String> aliasesEco = MUtil.list("eco", "economy");
    public List<String> aliasesGang = MUtil.list("g", "gang");

    public int gangNameLengthMin = 3;
    public int gangNameLengthMax = 9;

    public List<String> loadingFrames = MUtil.list(
            "&7[ &l&6Loading &7]",
            "&7[  &l&6Loading  &7]",
            "&7[   &l&6Loading   &7]",
            "&7[  &l&6Loading  &7]",
            "&7[ &l&6Loading &7]");

    public String introductionTutorialTourMessage =
            " \n \n \n" +
                    "&7&m-------------------------------------------------- \n" +
                    "&r&7&o[NPC] &r&e&lWarren the Warden:&7" + "\n" +
                    "&r&fWelcome to your new home. You do the crime, you do the time." + "\n" +
                    "&r&fFollow me, I'll show you around.  \n "
                    + "&7&m-------------------------------------------------- \n";

    public Map<Offset, List<String>> introductionTutorialTour = new LinkedHashMap<>(MUtil.map(
            new Offset(-51, 51, -58, 0.0F, 360.0F),
            MUtil.list(
                    "&r&7&o[NPC] &r&e&lWarren the Warden: &fThis area here is your &eMine&f!",
                    "&r&7&o[NPC] &r&e&lWarren: &fYou're expected to mine blocks here.",
                    "&r&7&o[NPC] &r&e&lWarren: &fYou can give me those blocks for &a ⛁Cash&f!"
            ),
            new Offset(-41, 51, -50, 0.0F, 90.0F),
            MUtil.list(
                    "&r&7&o[NPC] &r&e&lWarren the Warden: &fIt's not much yet... but maybe for some &a⛁Cash &fI can fix your &eMine &fup.",
                    "&r&7&o[NPC] &r&e&lWarren: &fTalk to me later, I can improve the &eWidth, Height&f, &fand the &eMaterials &fof your &eMine&f.",
                    "&r&7&o[NPC] &r&e&lWarren: &fWith good behavior, I can even improve your &eMine&f's &eScenery&f. So no sassing!"
            ),
            new Offset(-51, 51, -30),
            MUtil.list(
                    "&r&7&o[NPC] &r&e&lWarren the Warden: &fI'll be watching you from here. No funny business!",
                    "&r&7&o[NPC] &r&e&lWarren: &fDon't bother asking me if you have any questions... ",
                    "&r&7&o[NPC] &r&e&lWarren: &f...but the command &e/help&f might be useful.",
                    "&r&7&o[NPC] &r&e&lWarren: &fYou'll probably need a &ePickaxe&f. I'll give you one.",
                    "&r&7&o[NPC] &r&e&lWarren: &fNow get working! No funny business."
            ))
    );




    // -------------------------------------------- //
    // MISC
    // -------------------------------------------- //

    public Location getLimboLocation() {
        return new Location(Bukkit.getWorld(limboLocation.getWorld()), limboLocation.getLocationX(), limboLocation.getLocationY(), limboLocation.getLocationZ(), limboLocation.getYaw(), 0.0F);
    }

    public Location getSpawnLocation() {
        return new Location(Bukkit.getWorld(spawnLocation.getWorld()), spawnLocation.getLocationX(), spawnLocation.getLocationY(), spawnLocation.getLocationZ(), spawnLocation.getYaw(), 0.0F);
    }

    public String getTimeZone() {
        return timeZone;
    }
}

