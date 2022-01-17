package com.kennedysmithjava.prisoncore.entity.player;

import com.massivecraft.massivecore.store.EntityInternal;

public class PlayerStats extends EntityInternal<MPlayerStats>
{
	
	private String playerId;
	private Long blocksBroken;
	private Long blocksPlaced;
	private Long playersKilled;
	private Long mobsKilled;
	private Long deaths;

	public PlayerStats(String playerId)
	{
		this.playerId = playerId;
		this.blocksBroken = 0L;
		this.blocksPlaced = 0L;
		this.playersKilled = 0L;
		this.mobsKilled = 0L;
		this.deaths = 0L;
	}

	public Long getBlocksBroken()
	{
		return blocksBroken;
	}
	
	public void incrementBlocksBroken()
	{
		this.blocksBroken = this.blocksBroken + 1;
	}
	
	public Long getBlocksPlaced()
	{
		return blocksPlaced;
	}
	
	public void incrementBlocksPlaced()
	{
		this.blocksPlaced = this.blocksPlaced + 1;
	}
	
	public Long getPlayersKilled()
	{
		return playersKilled;
	}
	
	public void incrementPlayersKilled()
	{
		this.playersKilled = this.playersKilled + 1;
	}
	
	public Long getMobsKilled()
	{
		return mobsKilled;
	}
	
	public void incrementMobsKilled()
	{
		this.mobsKilled = this.mobsKilled + 1;
	}
	
	public Long getDeaths()
	{
		return deaths;
	}
	
	public void incrementDeaths()
	{
		this.deaths = this.deaths + 1;
	}
	
	public String getPlayerId()
	{
		return playerId;
	}

}
