package com.kennedysmithjava.prisoncore.entity.npcs;

import com.massivecraft.massivecore.command.editor.annotation.EditorName;
import com.massivecraft.massivecore.store.Entity;

@EditorName("config")
public class NPCDialogueConf extends Entity<NPCDialogueConf>{
    protected static transient NPCDialogueConf i;

    public static NPCDialogueConf get() { return i; }

    @Override
    public NPCDialogueConf load(NPCDialogueConf that)
    {
        super.load(that);
        return this;
    }


}
