package com.kennedysmithjava.prisonmines.buildings;

import com.kennedysmithjava.prisonmines.entity.Mine;

public abstract class Building {

    public abstract void destroy(Mine mine);
    public abstract void place(Mine mine);

}
