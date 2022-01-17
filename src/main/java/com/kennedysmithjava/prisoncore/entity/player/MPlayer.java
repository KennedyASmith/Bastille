package com.kennedysmithjava.prisoncore.entity.player;

import com.kennedysmithjava.prisoncore.PrisonCore;
import com.kennedysmithjava.prisoncore.PrisonParticipator;
import com.kennedysmithjava.prisoncore.eco.CurrencyType;
import com.kennedysmithjava.prisoncore.CooldownReason;
import com.kennedysmithjava.prisoncore.engine.EngineCooldown;
import com.kennedysmithjava.prisoncore.entity.mines.Mine;
import com.kennedysmithjava.prisoncore.entity.mines.MineColl;
import com.kennedysmithjava.prisoncore.quest.QuestPhaseGroup;
import com.kennedysmithjava.prisoncore.quest.QuestProfile;
import com.massivecraft.massivecore.collections.MassiveMap;
import com.massivecraft.massivecore.store.SenderEntity;
import com.massivecraft.massivecore.util.MUtil;
import org.bukkit.ChatColor;

import java.util.Map;

public class MPlayer extends SenderEntity<MPlayer> implements PrisonParticipator {

    // -------------------------------------------- //
    // CONSTANTS
    // -------------------------------------------- //

    private final Map<CurrencyType, Double> economy = MUtil.map(CurrencyType.CASH, 0.25, CurrencyType.RESEARCH, 15.0, CurrencyType.GEMS, 50.0);
    private int level = 1;
    private String gangID = null;
    private String mineID = "none";
    private transient QuestProfile questProfile;
    private Map<String, Integer> activeQuests = new MassiveMap<>("IntroductionTutorial", 0);
    private String clanName = "";

    public static MPlayer get(Object oid) {
        return MPlayerColl.get().get(oid);
    }

    @Override
    public MPlayer load(MPlayer that) {
        this.setLevel(that.level);
        this.setMineID(that.mineID);
        this.setQuestProfile(that.questProfile);
        this.setActiveQuests(that.activeQuests);

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
     * @return list of currencies.
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
     *
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
     * @param times
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

    public QuestProfile getQuestProfile() {
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

    public boolean inCooldown(CooldownReason reason){
        return EngineCooldown.inCooldown(this.getPlayer(), reason);
    }

    public String getClanName() {
        return clanName;
    }

    public void setClanName(String clanName) {
        this.clanName = clanName;
    }
}

