package com.kennedysmithjava.prisoncore.entity.tools;

import com.kennedysmithjava.prisoncore.tools.Buffer;
import com.massivecraft.massivecore.command.editor.annotation.EditorName;
import com.massivecraft.massivecore.store.Entity;
import com.massivecraft.massivecore.util.MUtil;

import java.util.Map;

@EditorName("config")
public class BufferConf extends Entity<BufferConf> {


    protected static transient BufferConf i;

    public static BufferConf get() { return i; }

    public transient static Buffer SPEED = new Buffer("Speed",
            "&bSpeed",
            /*new BlockMaterial(Material.INK_SACK, 3),*/
            MUtil.list(""),
            "&b▐ Speed&7 - Lvl. %level%"
    );

    public transient static Buffer POWER = new Buffer("Power",
            "&cPower",
            /*new BlockMaterial(Material.INK_SACK, 4),*/
            MUtil.list(""),
            "&c▐ Power&7 - Lvl. %level%"
    );

    public transient static Buffer EFFICIENCY = new Buffer("Efficiency",
            "&eEfficiency",
            /*new BlockMaterial(Material.INK_SACK, 5),*/
            MUtil.list(""),
            "&a▐ Efficiency&7 - Lvl. %level%"
    );

    public static Map<String, Buffer> activeBuffers = MUtil.map(
            "Speed", SPEED,
            "Power", POWER,
            "Efficiency", EFFICIENCY
    );

    @Override
    public BufferConf load(BufferConf that)
    {
        super.load(that);
        return this;
    }

}
