package com.kennedysmithjava.prisoncore.entity.mines;

import com.kennedysmithjava.prisoncore.npc.Skin;
import com.massivecraft.massivecore.command.editor.annotation.EditorName;
import com.massivecraft.massivecore.store.Entity;
import com.massivecraft.massivecore.util.MUtil;

import java.util.List;
import java.util.Map;

@EditorName("config")
public class CoinCollectorConf extends Entity<CoinCollectorConf>
{
    // -------------------------------------------- //
    // META
    // -------------------------------------------- //

    protected static transient CoinCollectorConf i;

    public static CoinCollectorConf get() { return i; }

    @Override
    public CoinCollectorConf load(CoinCollectorConf that)
    {
        super.load(that);
        return this;
    }

    public Map<Integer, Double> autoSellTimes =
            MUtil.map(1, 60D, 2, 30D, 3, 15D);

    public String collectorName = "Collector";
    public String collectorLineNoPermission = "&7[&bCaleb&7] I don't think you have permission to order from me, pal!";
    public String collectorLineWelcome = "&7[&bCaleb&7] What bounty have ye got for me?";
    public Skin collectorSkin = new Skin(
            "ewogICJ0aW1lc3RhbXAiIDogMTYwNTU1NTQ0MDAyOSwKICAicHJvZmlsZUlkIiA6ICJkODY1NjliNzg1ODU0OGU3OTJlYmJjNDM2MGYxNjkwNyIsCiAgInByb2ZpbGVOYW1lIiA6ICJPY2FuYW0iLAogICJzaWduYXR1cmVSZXF1aXJlZCIgOiB0cnVlLAogICJ0ZXh0dXJlcyIgOiB7CiAgICAiU0tJTiIgOiB7CiAgICAgICJ1cmwiIDogImh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZTk4MWI2MzY4Y2NlNzRmMjdmODJmZjJmMTgzOGUxNzlkNGRlMzgyNDFmY2JlZDM1Y2Y3NGNjNzAyZGQ1MzI5MyIKICAgIH0KICB9Cn0=",
            "SZv7ZVIogkUrkMu6zJDaa67I715pvvCPlhAP+EuZJFrR5z7mO9dCiISqMcMUqKDrFcKH708vVDwy+IXxH9Bukp6fpYHA73WhT2f+I7yuimHZUJYy/TbSSOK01g/TskJETvvsZYne8KzFDFQ9uZQz1sSv04bmYyL6BOHQ/wi+WJrUnlrUJsRqN0maMPRqpI2NTlnyD+yqtyxy1DeLAvSk+EP8IXrebImKo+pfrB/sdJ1C6iaA5T9nkjmtj7BvcrjYEjULwdOtYxT8YJ0Vr084YoZWOCCzFfXD8B8aPKeGnfzWWhZJZ2rcAnNVmCjGnMHuQiXWjYL6bN0kQ9WSzeacoqI+A8E05z9MPixtEFx4GCXGjP3uf/aaY0gmv2tQMAcX3pjWqfM1V5N09Ts3D0VvXPUt0nwaEcKn+/qXcpz0K4/F9iygvAM6PfN5onVNu2kmYOzGeJH6Ch8p+wfZCdKX880NnMSZde+X7DiQ3uhbJdskAYJp5P9MlBliQJcMPpIEfJt9blB502UotxJkk6NViQrEfe9Uck8mJvAUeg3bVXusevSbsAFECm7pySptYCeQ/rYku4Xiruq9GPkS4QkkaIO6+SwuN3Vp11w80/gGNBtNam0WYl5pUlXwOtY8L75+CMhPT0fQobDNCrI5kf5fEv0nzrD/Hy99yzqsFo59cL0=");
    public List<String> collectorHologram = MUtil.list("&7&m-------------", "Caleb the Collector", "&e&lRIGHT CLICK", "&7&m-------------");


}

