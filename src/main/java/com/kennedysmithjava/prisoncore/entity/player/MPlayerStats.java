package com.kennedysmithjava.prisoncore.entity.player;

import com.massivecraft.massivecore.collections.MassiveMap;
import com.massivecraft.massivecore.command.editor.annotation.EditorName;
import com.massivecraft.massivecore.store.Entity;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

@EditorName("config")
public class MPlayerStats extends Entity<MPlayerStats>
{
	// -------------------------------------------- //
	// META
	// -------------------------------------------- //

	protected static transient MPlayerStats i;
	public MassiveMap<String, PlayerStats> playerStats = new MassiveMap<>();
	public long lastUpdatedTime = 0L;

	// -------------------------------------------- //
	// PLAYER STATS COLL
	// -------------------------------------------- //

	public static MPlayerStats get()
	{
		return i;
	}

	// -------------------------------------------- //
	// OVERRIDE: ENTITY
	// -------------------------------------------- //

	@Override
	public MPlayerStats load(MPlayerStats that)
	{
		super.load((MPlayerStats) that);
		return this;
	}

	public void setPlayerStats(MassiveMap<String, PlayerStats> playerStats)
	{
		this.playerStats = playerStats;
		lastUpdatedTime = System.currentTimeMillis();
		this.changed();
	}

	private void createPlayerStats(MPlayer mPlayer)
	{
		playerStats.put(mPlayer.getUuid().toString(), new PlayerStats(mPlayer.getUuid().toString()));
		lastUpdatedTime = System.currentTimeMillis();
		this.changed();
	}

	public PlayerStats getPlayerStats(MPlayer mPlayer)
	{
		if (mPlayer == null || mPlayer.getUuid() == null) return null;
		if (!playerStats.containsKey(mPlayer.getUuid().toString())) createPlayerStats(mPlayer);
		return playerStats.get(mPlayer.getUuid().toString());
	}

	public PlayerStats getPlayerStats(Player player)
	{
		if (player == null) {
			return null;
		}

		return getPlayerStats(MPlayer.get(player));
	}

	public PlayerStats getPlayerStats(OfflinePlayer player)
	{
		if (player == null || !player.hasPlayedBefore() || player.getUniqueId() == null) {
			return null;
		}

		if (!playerStats.containsKey(player.getUniqueId().toString()))
			playerStats.put(player.getUniqueId().toString(), new PlayerStats(player.getUniqueId().toString()));

		return playerStats.get(player.getUniqueId().toString());
	}

	public boolean containsPlayer(Player player) {
		return playerStats.containsKey(player.getUniqueId().toString());
	}

	public void update() {
		lastUpdatedTime = System.currentTimeMillis();
		this.changed();
	}

}