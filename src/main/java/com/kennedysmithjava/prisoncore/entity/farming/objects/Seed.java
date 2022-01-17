package com.kennedysmithjava.prisoncore.entity.farming.objects;

import com.massivecraft.massivecore.ps.PS;
import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class Seed {

    private PS ps;
    private long timeToTurnIntoWheat;

}
