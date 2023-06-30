package com.kennedysmithjava.prisoncore.pouch;

import com.massivecraft.massivecore.MassiveException;
import com.massivecraft.massivecore.command.MassiveCommand;

public class PouchCommand extends MassiveCommand {

    private static final PouchCommand i = new PouchCommand();
    public static PouchCommand get() {
        return i;
    }

    public PouchGiveCommand pouchGiveCommand = new PouchGiveCommand();

    public PouchCommand() {
        this.setAliases("pouch");
        this.addChild(pouchGiveCommand);
        this.setSetupEnabled(true);
    }

    @Override
    public void perform() throws MassiveException {

    }


}
