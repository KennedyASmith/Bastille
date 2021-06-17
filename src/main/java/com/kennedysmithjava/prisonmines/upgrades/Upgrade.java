package com.kennedysmithjava.prisonmines.upgrades;

import com.kennedysmithjava.prisonmines.entity.Distribution;
import com.kennedysmithjava.prisonmines.entity.DistributionConf;
import com.massivecraft.massivecore.util.MUtil;
import com.mcrivals.prisoncore.entity.MPlayer;
import org.bukkit.Material;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public enum Upgrade {

    COBWEB_CLEAR_1(
            "&c&lClear Cobwebs",
            Material.WEB,
            29,
            listU(
                    new CommandUpgrade(listS("give %player% stone 1")),
                    new ResourceUpgrade(DistributionConf.get().distribution.get(2)),
                    new MessageUpgrade("%trader% My boys cleared out the mine of all of the cobwebs.")
            ),
            listS()
    ),
    RESOURCE_1(
            "&c&lResource Upgrade",
            Material.STONE,
            31,
            listU(
                    new ResourceUpgrade(DistributionConf.get().distribution.get(3)),
                    new MessageUpgrade("%trader% That'll get your money flowing quick!")
            ),
            listS("COBWEB_CLEAR_1")
    ),
    HEIGHT_1(
            "&c&lHeight Upgrade",
            Material.FEATHER,
            33,
            listU(
                    new MineSizeUpgrade(3, 5)
            ),
            listS("COBWEB_CLEAR_1")
    ),
    MINE_2(
            "%name%",
            Material.MINECART,
            13,
            listU(),
            listS("COBWEB_CLEAR_1", "HEIGHT_1", "RESOURCE_1")
    )
    ;

    String name;
    Material icon;
    List<AbstractUpgrade> upgrades;
    List<String> requiredUpgrades;
    int slot;

    /**
     * An upgrade that will appear in the personal mine upgrade GUI menu.
     * @param displayName Display displayName of the upgrade.
     * @param icon The icon that will be displayed on the upgrade menu GUI.
     * @param upgrades The unlock/upgrades that come with this upgrade
     * @param requiredUpgrades The unlock/upgrades necessary to acquire this upgrade.
     */
    Upgrade(String displayName, Material icon, int slot, List<AbstractUpgrade> upgrades, List<String> requiredUpgrades){
        this.name = displayName;
        this.icon = icon;
        this.upgrades = upgrades;
        this.requiredUpgrades = requiredUpgrades;
        this.slot = slot;
    }

    Upgrade(String displayName, Material icon, int slot, AbstractUpgrade upgrade, List<String> requiredUpgrades){
        this.name = displayName;
        this.icon = icon;
        this.upgrades = listU(upgrade);
        this.requiredUpgrades = requiredUpgrades;
        this.slot = slot;
    }

    private static List<AbstractUpgrade> listU(AbstractUpgrade... upgrades){
        return new ArrayList<>(Arrays.asList(upgrades));
    }

    private static List<String> listS(String... strings){
        return new ArrayList<>(Arrays.asList(strings));
    }

    public void apply(MPlayer player){
        upgrades.forEach(upgrade -> {
            upgrade.apply(player);
        });
    }

    public boolean canGet(MPlayer player){
        return true;
    }

    public String getDisplayName() {
        return name;
    }

    public Material getIcon() {
        return icon;
    }

    public List<String> getRequiredUpgrades() {
        return requiredUpgrades;
    }

    public List<AbstractUpgrade> getUpgrades() {
        return upgrades;
    }

    public int getSlot() {
        return slot;
    }

    public String getName(){
        return this.name();
    }
}
