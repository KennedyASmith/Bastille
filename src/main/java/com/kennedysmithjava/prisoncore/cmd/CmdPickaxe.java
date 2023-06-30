package com.kennedysmithjava.prisoncore.cmd;

import com.kennedysmithjava.prisoncore.cmd.type.TypePickaxeType;
import com.kennedysmithjava.prisoncore.entity.MConf;
import com.kennedysmithjava.prisoncore.entity.tools.PickaxeType;
import com.massivecraft.massivecore.MassiveException;

import java.util.List;

public class CmdPickaxe extends ToolsCommand {
    // -------------------------------------------- //
    // INSTANCE
    // -------------------------------------------- //

    private static final CmdPickaxe i = new CmdPickaxe();


    // -------------------------------------------- //
    // FIELDS
    // -------------------------------------------- //

    //public MassiveCommandVersion cmdFactionsVersion = new MassiveCommandVersion(PrisonTools.get()).setAliases("v", "version");

    public CmdPickaxe() {

        //addParameter(TypeEnchantType.get(), "enchant");
        setDesc("Get a pickaxe");
        addParameter(new TypePickaxeType(), "name");
    }

    @Override
    public void perform() throws MassiveException {
        PickaxeType pickaxeType = this.readArg();
        me.getInventory().addItem(pickaxeType.getItemStack());
    }

    // -------------------------------------------- //
    // CONSTRUCT
    // -------------------------------------------- //

    public static CmdPickaxe get() {
        return i;
    }

    // -------------------------------------------- //
    // OVERRIDE
    // -------------------------------------------- //

    @Override
    public List<String> getAliases() {
        return MConf.get().aliasesP;
    }
}

/*    public static void testCode(){
        String ability = this.readArg();
        int power = this.readArg();
        int speed = this.readArg();
        int efficiency = this.readArg();
        PickaxeType pickaxeType = PickaxeTypeColl.get().create(MStore.createId());
        pickaxeType.setDisplayName("&6&lSolar Pickaxe");
        pickaxeType.setDescription("This pickaxe was smelted in flames!");
        pickaxeType.setMaterial(Material.DIAMOND_PICKAXE);
        pickaxeType.setStartDurability(1000);
        pickaxeType.setMaxDurability(1000);
        pickaxeType.setRarity(5);
        pickaxeType.setLore(MUtil.list(
                "&7This pickaxe was smelted",
                "&7in the flames of our Sun!",
                " ",
                "&b&m----------&r&l Ability &b&m----------",
                "%ability%",
                "%buffers%",
                "&7Buffers: (&f%buffer_count%&7/%buffer_max%)",
                " ",
                "&b&m----------&r&l Enchants &b&m----------",
                "%enchants%",
                " ",
                "&b&m----------&r&l Other &b&m----------",
                "%durHeader%",
                "&7[%durBar%&7]"));

        pickaxeType.addEnchant(PickaxeVeinEnchant.get(), 2);
        pickaxeType.addEnchant(PickaxeExplosiveEnchant.get(), 1);
        pickaxeType.addEnchant(PickaxeNightVisionEnchant.get(), 1);

        pickaxeType.setBuffers(MUtil.map(
                BufferConf.SPEED.getName(), speed,
                BufferConf.POWER.getName(), power,
                BufferConf.EFFICIENCY.getName(), efficiency));
        pickaxeType.setAbility(ability);

        Player player = (Player) sender;
        ItemStack item = pickaxeType.getItemStack();
        player.getInventory().addItem(item);
    }*/
