package com.kennedysmithjava.prisoncore.entity.player;

import com.drtshock.playervaults.PlayerVaults;
import com.drtshock.playervaults.vaultmanagement.VaultViewInfo;
import com.kennedysmithjava.prisoncore.PrisonParticipator;
import com.kennedysmithjava.prisoncore.eco.CurrencyType;
import com.kennedysmithjava.prisoncore.engine.EngineCooldown;
import com.kennedysmithjava.prisoncore.entity.mines.Mine;
import com.kennedysmithjava.prisoncore.entity.mines.MineColl;
import com.kennedysmithjava.prisoncore.entity.player.objects.StorageWrapper;
import com.kennedysmithjava.prisoncore.quest.QuestPath;
import com.kennedysmithjava.prisoncore.quest.quests.enums.CollectibleKey;
import com.kennedysmithjava.prisoncore.storage.StorageManager;
import com.kennedysmithjava.prisoncore.storage.StorageType;
import com.kennedysmithjava.prisoncore.util.CooldownReason;
import com.massivecraft.massivecore.store.SenderEntity;
import com.massivecraft.massivecore.util.MUtil;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.Map;

@SuppressWarnings("unused")
public class MPlayer extends SenderEntity<MPlayer> implements PrisonParticipator {

    // -------------------------------------------- //
    // CONSTANTS
    // -------------------------------------------- //

    private Map<CurrencyType, Double> economy = MUtil.map(CurrencyType.CASH, 0.25, CurrencyType.RESEARCH, 15.0, CurrencyType.GEMS, 50.0);
    private Map<CurrencyType, Double> economyMultiplier = MUtil.map(CurrencyType.CASH, 1.0, CurrencyType.RESEARCH, 1.0, CurrencyType.GEMS, 1.0);

    private String gangID = null;
    private String mineID = "none";
    private String clanName = "";

    private Map<StorageType, StorageWrapper> storage = MUtil.map(
            StorageType.STORAGE_1, new StorageWrapper(false, Material.CHEST, "&aStorage 1"),
            StorageType.STORAGE_2, new StorageWrapper(false, Material.CHEST, "&aStorage 2"),
            StorageType.STORAGE_3, new StorageWrapper(false, Material.CHEST, "&aStorage 3"),
            StorageType.STORAGE_4, new StorageWrapper(false, Material.CHEST, "&aStorage 4"),
            StorageType.STORAGE_5, new StorageWrapper(false, Material.CHEST, "&aStorage 5"),
            StorageType.STORAGE_6, new StorageWrapper(false, Material.CHEST, "&aStorage 6"),
            StorageType.STORAGE_7, new StorageWrapper(false, Material.CHEST, "&aStorage 7"),
            StorageType.COLLECTIBLES, new StorageWrapper(false, Material.ENDER_CHEST, "&aCollectibles"),
            StorageType.PRESTIGE, new StorageWrapper(false, Material.ENDER_CHEST, "&aRebirth Vault")
    );

    public static MPlayer get(Object oid) {
        return MPlayerColl.get().get(oid);
    }

    @Override
    public MPlayer load(MPlayer that) {
        this.setEconomy(that.economy);
        this.setMineID(that.mineID);
        this.setStorage(that.storage);
        return this;
    }

    // -------------------------------------------- //
    // ECONOMY
    // -------------------------------------------- //

    /**
     * This method is used to display how
     * a player has of each currency.
     *
     * @return map of currencies.
     */
    public Map<CurrencyType, Double> getEconomy() {
        return economy;
    }

    /**
     * Get how much a player has of a specific Currency
     *
     * @param eco CurrencyType
     * @return Balance for a specific currency
     */
    public Double getBalance(CurrencyType eco) {
        return economy.get(eco);
    }


    /**
     * Set the player's balance to anything you want.
     *
     * @param eco    CurrencyType
     * @param amount Double; amount of money you want the player to have.
     */
    public void setBalance(CurrencyType eco, Double amount) {
        economy.put(eco, Math.max(0, amount));
        this.changed();
    }

    /**
     * Add money to the player's balance.
     *
     * @param eco    CurrencyType
     * @param amount amount of money you want to add to the player's balance.
     */
    public void addBalance(CurrencyType eco, Double amount, boolean applyMultiplier) {
        double multiplier = economyMultiplier.get(eco);
        setBalance(eco, getBalance(eco) + (amount * multiplier));
    }

    /**
     * Remove money from the player's balance.
     * ! DON'T USE FOR TRANSACTIONS !
     * @param eco    CurrencyType
     * @param amount amount of money you want to remove from the player's balance.
     */
    public void removeBalance(CurrencyType eco, Double amount) {
        setBalance(eco, getBalance(eco) - amount);
    }

    public void setEconomy(Map<CurrencyType, Double> economy) {
        this.economy = economy;
        this.changed();
    }

    public void setMineID(String mineID) {

        this.mineID = mineID;
        this.changed();
    }

    public String getMineID() {
        return mineID;
    }

    public Mine getMine(){
        return MineColl.get().get(getMineID());
    }

    public boolean hasMine(){
        return !getMineID().equals("none");
    }

    // -------------------------------------------- //
    // Gang
    // -------------------------------------------- //

    public Guild getGang() {
        return GuildColl.get().getByID(gangID);
    }

    public void setGang(Guild guild) {
        gangID = guild.getId();
        this.changed();
    }

    public boolean inCooldown(CooldownReason reason){
        return EngineCooldown.inCooldown(this.getUuid(), reason);
    }

    public String getClanName() {
        return clanName;
    }

    public void setClanName(String clanName) {
        this.clanName = clanName;
    }

    public QuestProfile getQuestProfile(){
        return QuestProfileColl.get().getByUUID(this.getPlayer().getUniqueId());
    }

    public SkillProfile getSkillProfile(){
        return SkillProfileColl.get().getByUUID(this.getPlayer().getUniqueId());
    }

    public void giveCollectible(String itemID, ItemStack itemStack){

    }

    public void giveCollectible(CollectibleKey key, ItemStack itemStack){
        this.giveCollectible(key.getKey(), itemStack);
    }

    public void interruptAnyActiveQuest(){
        QuestPath activeQuestPath = getQuestProfile().getActiveQuestPath();
        if(activeQuestPath == null) return;
        activeQuestPath.deactivate(this);
    }

    public boolean hasCurrency(CurrencyType type, double neededAmount){
        if(!economy.containsKey(type)) return false;
        double hasAmt = economy.get(type);
        return hasAmt >= neededAmount;
    }

    /**
     * Used for transactions.
     * @return true if the transaction was successful
     */
    public boolean takeCurrency(CurrencyType type, double amount){
        if(!hasCurrency(type, amount)) return false;
        removeBalance(type, amount);
        return true;
    }

    public void setStorage(Map<StorageType, StorageWrapper> storage) {
        this.storage = storage;
        this.changed();
    }

    public void unlockStorage(StorageType type, boolean unlocked){
        storage.get(type).setUnlocked(unlocked);
        this.changed();
    }

    public void setStorageIcon(StorageType type, Material icon){
        storage.get(type).setIcon(icon);
        this.changed();
    }

    public void setStorageName(StorageType type, String name){
        storage.get(type).setName(name);
        this.changed();
    }

    public StorageWrapper getStorageWrapper(StorageType type){
        return storage.get(type);
    }

    public Inventory getStorage(StorageType type){
        return StorageManager.get().loadChest(this, type);
    }

    public void openStorage(StorageType type, Player player){
        Inventory inv = getStorage(type);
        player.openInventory(inv);
        PlayerVaults.getInstance().getInVault().put(player.getUniqueId().toString(), new VaultViewInfo(this.getName(), type.getId()));
    }

    public void addEconomyMultiplier(CurrencyType type, double multiplier){
        economyMultiplier.put(type, multiplier);
        this.changed();
    }

    public void setEconomyMultiplier(Map<CurrencyType, Double> multiplier){
        this.economyMultiplier = multiplier;
        this.changed();
    }


}

