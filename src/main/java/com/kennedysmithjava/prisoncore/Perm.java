package com.kennedysmithjava.prisoncore;

import com.massivecraft.massivecore.Identified;
import com.massivecraft.massivecore.util.PermissionUtil;
import org.bukkit.permissions.Permissible;

public enum Perm implements Identified
{
    // -------------------------------------------- //
    // ENUM
    // -------------------------------------------- //

    ACCESS,
    CREATE,
    BASECOMMAND,
    DELETE,
    REGEN,
    OFFSET,
    POSONE,
    POSTWO,
    WAND,
    SETSPAWN,
    TELEPORT,
    SIZE,
    ADMIN,
    ADD,
    BALANCE,
    REMOVE,
    TREE,
     PITEM,
    GIVE,
    MAP,
    HELP,
    FISH_POS_ONE,
    FISH_POS_TWO,
    FISH_CREATE,
    FISHING


    // END OF LIST
    ;

    // -------------------------------------------- //
    // FIELDS
    // -------------------------------------------- //

    private final String id;
    @Override public String getId() { return this.id; }

    // -------------------------------------------- //
    // CONSTRUCT
    // -------------------------------------------- //

    Perm()
    {
        this.id = PermissionUtil.createPermissionId(PrisonCore.get(), this);
    }

    // -------------------------------------------- //
    // HAS
    // -------------------------------------------- //

    public boolean has(Permissible permissible, boolean verboose)
    {
        return PermissionUtil.hasPermission(permissible, this, verboose);
    }

    public boolean has(Permissible permissible)
    {
        return PermissionUtil.hasPermission(permissible, this);
    }

}
