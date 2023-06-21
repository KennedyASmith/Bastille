package com.kennedysmithjava.prisoncore.entity.player;

import com.kennedysmithjava.prisoncore.PrisonParticipator;
import com.kennedysmithjava.prisoncore.eco.CurrencyType;
import com.kennedysmithjava.prisoncore.util.CooldownReason;
import com.kennedysmithjava.prisoncore.engine.EngineCooldown;
import com.kennedysmithjava.prisoncore.entity.mines.Mine;
import com.kennedysmithjava.prisoncore.entity.mines.MineColl;
//import com.kennedysmithjava.prisoncore.quest.QuestPhaseGroup;
//import com.kennedysmithjava.prisoncore.quest.QuestProfile;
import com.kennedysmithjava.prisoncore.quest.QuestPath;
import com.kennedysmithjava.prisoncore.quest.quests.enums.CollectibleKey;
import com.massivecraft.massivecore.store.SenderEntity;
import com.massivecraft.massivecore.util.MUtil;
import org.bukkit.ChatColor;
import org.bukkit.inventory.ItemStack;

import java.util.Map;

@SuppressWarnings("unused")
public class MPlayer extends SenderEntity<MPlayer> implements PrisonParticipator {

    // -------------------------------------------- //
    // CONSTANTS
    // -------------------------------------------- //

    private final Map<CurrencyType, Double> economy = MUtil.map(CurrencyType.CASH, 0.25, CurrencyType.RESEARCH, 15.0, CurrencyType.GEMS, 50.0);
    private String gangID = null;
    private String mineID = "none";
    private String clanName = "";
    private int xp = 0;

    //XP Required to level up your character
    private int xpRequired = 0;

    private int level = 1;

    public static MPlayer get(Object oid) {
        return MPlayerColl.get().get(oid);
    }

    @Override
    public MPlayer load(MPlayer that) {
        this.setMineID(that.mineID);
        this.setXP(that.xp);
        this.setXpRequired(that.xpRequired);
        return this;
    }

    /**
     * Send the player a message.
     *
     * @param message Message to send...
     */
    public void sendMessage(String message) {
        getPlayer().sendMessage(ChatColor.translateAlternateColorCodes('&', message));
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
    public void addBalance(CurrencyType eco, Double amount) {
        setBalance(eco, getBalance(eco) + amount);
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

    // -------------------------------------------- //
    // LEVEL AND PRESTIGE
    // -------------------------------------------- //

    /**
     * set the player's current level
     *
     * @param level The level that you want the player to be
     */
    public void setLevel(int level) {
        this.level = level;
        this.changed();
    }

    /**
     * Will simply level up the player.
     */
    public void levelUp() {
        setLevel(level + 1);
    }

    /**
     * Will level up the player x times.
     *
     * @param times How many times should the player be leveled up.
     */
    public void levelUp(int times) {
        setLevel(level + times);
    }

    /**
     * Will simply downgrade the player a level.
     * <p>
     * Not sure how this will be applicable but...
     */
    public void downgrade() {
        setLevel(level - 1);
    }

    /**
     * Will simply downgrade the player x times.
     * <p>
     * Not sure how this will be applicable but...
     *
     */
    public void downgrade(int times) {
        setLevel(level - times);
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

    /*public QuestProfile getQuestProfile() {
        if(questProfile == null){
            questProfile = new QuestProfile(this);
            getActiveQuests().forEach((s, phase) -> {
                QuestPhaseGroup group = PrisonCore.get().getQuestManager().get(s);
                questProfile.rejoinQuest(group, phase);
            });
        }
        return questProfile;
    }

    public void setQuestProfile(QuestProfile questProfile) {
        this.questProfile = questProfile;
    }

    public void setActiveQuests(Map<String, Integer> activeQuests) {
        this.activeQuests = activeQuests;
    }

    public void setActiveQuest(String questGroup, int progress) {
        activeQuests.putIfAbsent(questGroup, progress);
    }

    public Map<String, Integer> getActiveQuests() {
        return activeQuests;
    }

    public void removeQuest(String name){
        activeQuests.remove(name);
        this.changed();
    }
*/

    public boolean inCooldown(CooldownReason reason){
        return EngineCooldown.inCooldown(this.getPlayer(), reason);
    }

    public String getClanName() {
        return clanName;
    }

    public void setClanName(String clanName) {
        this.clanName = clanName;
    }

    public void setXP(int xp) {
        this.xp = xp;
        this.changed();
    }

    public void addXP(int xpToAdd){
        int currentXP = this.xp;
        int xpRequiredForNextLevel = getXpRequired();
        int sumXP = currentXP + xpToAdd;
        if(sumXP > xpRequiredForNextLevel){
            int remainder = sumXP - xpRequiredForNextLevel;
            setLevel(this.level + 1);
            setXpRequired(calculateXpRequired());
            addXP(remainder);
        }
    }

    public void setXpRequired(int xpRequired) {
        this.xpRequired = xpRequired;
        this.changed();
    }

    public int getXpRequired() {
        return xpRequired;
    }

    private int calculateXP(int level) {
        return (int) Math.floor(10 * Math.pow(level, 0.2) * Math.pow(level, 2.0) + Math.pow(level - 1, 2.0));
    }

    public int calculateXpRequired() {
        int level = this.level;
        return calculateXP(level + 1) - calculateXP(level);
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


}

