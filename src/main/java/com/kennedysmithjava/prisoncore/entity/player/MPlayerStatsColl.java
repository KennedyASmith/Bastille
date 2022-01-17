package com.kennedysmithjava.prisoncore.entity.player;

import com.massivecraft.massivecore.MassiveCore;
import com.massivecraft.massivecore.store.Coll;

public class MPlayerStatsColl extends Coll<MPlayerStats>
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static MPlayerStatsColl i = new MPlayerStatsColl();
	
	public static MPlayerStatsColl get()
	{
		return i;
	}
	
	// -------------------------------------------- //
	// STACK TRACEABILITY
	// -------------------------------------------- //
	
	@Override
	public void onTick()
	{
		super.onTick();
	}
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public void setActive(boolean active)
	{
		super.setActive(active);
		if (!active) return;
		MPlayerStats.i = this.get(MassiveCore.INSTANCE, true);
	}
	
}