package com.kennedysmithjava.prisoncore.cmd;

public class CmdMineUpgrade extends CoreCommand {


    /**
     * Requires UpgradeConf to be hardcoded or altered
     */


    /*// -------------------------------------------- //
    // CONSTRUCT
    // -------------------------------------------- //

    public CmdMineUpgrade() {
        //Requirement
        this.addRequirements(RequirementIsPlayer.get(), RequirementHasPerm.get(Perm.ADMIN));

        // Parameters
        this.addParameter(TypeUpgrade.get(), "upgrade");
        this.addParameter(true, TypeBooleanTrue.get(), "purchased");
        this.addParameter(true, TypeBooleanTrue.get(), "active");
        this.addParameter(false, TypeBooleanTrue.get(), "remove");
        this.addParameter(MPlayer.get(sender), TypeMPlayer.get(), "player");

        //Description
        this.setDesc("Upgrade a personal mine");
    }

    // -------------------------------------------- //
    // OVERRIDE
    // -------------------------------------------- //

    @Override
    public void perform() throws MassiveException {
        if (!(sender instanceof Player)) return;

        String upgrade = readArg();


        MPlayer player = MPlayer.get(sender);
        if(!player.hasMine()){
            msg("That player doesn't have a mine.");
            return;
        }



        msg("");
    }
*/
}
