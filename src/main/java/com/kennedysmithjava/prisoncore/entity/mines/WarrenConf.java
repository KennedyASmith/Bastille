package com.kennedysmithjava.prisoncore.entity.mines;

import com.kennedysmithjava.prisoncore.npc.Skin;
import com.massivecraft.massivecore.command.editor.annotation.EditorName;
import com.massivecraft.massivecore.store.Entity;
import com.massivecraft.massivecore.util.MUtil;

import java.util.List;

@EditorName("config")
public class WarrenConf extends Entity<WarrenConf>
{
    // -------------------------------------------- //
    // META
    // -------------------------------------------- //

    protected static transient WarrenConf i;

    public static WarrenConf get() { return i; }

    @Override
    public WarrenConf load(WarrenConf that)
    {
        super.load(that);
        return this;
    }

    public String researcherName = "Warren";
    public String researcherLineNoPermission = "&7[&bRon&7] I don't think you have permission to order from me, pal!";
    public String researcherLineWelcome = "&7[&bRon&7] What can I do for ya today, chief?";
    public List<String> warrenHologram = MUtil.list("&7&m-------------", "Warren the Warden", "&e&lRIGHT CLICK", "&7&m-------------");
    public Skin warrenSkin = new Skin("ewogICJ0aW1lc3RhbXAiIDogMTYyNzgwMDc0MTI4NSwKICAicHJvZmlsZUlkIiA6ICJmNThkZWJkNTlmNTA0MjIyOGY2MDIyMjExZDRjMTQwYyIsCiAgInByb2ZpbGVOYW1lIiA6ICJ1bnZlbnRpdmV0YWxlbnQiLAogICJzaWduYXR1cmVSZXF1aXJlZCIgOiB0cnVlLAogICJ0ZXh0dXJlcyIgOiB7CiAgICAiU0tJTiIgOiB7CiAgICAgICJ1cmwiIDogImh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZWVlNmFjZjAyY2Y0Y2NmMGRiMTcwNDQwMGI3NWYwZjFlYWM5YmQ5Y2M0MTY5N2E2OTk2OWYwMzMzMzA5OWNjMSIKICAgIH0KICB9Cn0=", "dmqa8B1S3P48i38CnCZjD0rPBsoPlCTXmzDlsgoN7IqH1yXAAqQiB1xRZOnhWXpaLnag6izQfFFOte/ldo7bnEhSauFcHWF8Y4VoBoIFlpx6aSLgSGhfni7QNyfUEJ0f6UFlYUcP1/LVwTQK/N/AQR3wvE+owilpc0AIQV2VDcY9eaJGzKf9Hfift4T+9+5VlY/LGGIG9+vqvsUiZH9fCAc8Ixg/XqzXKqqOkfw4ETAUUq5to1zMGjYwmBelpPUlSgfjbPNU23+mFbRepIXTZa0UcvYa8ASscv8GZyZdOw4hZ9TPzbuc9M3FEEOXLgY4BCgocgFEamhzAwSaYOHwL/8Zzz/I96+Wr/3riTgHwozZXbiedz8Dghyh3TfCvmLXpB6kWa0YhoYsgI9LO5Bx+Dw1zHjwS8PCYDtEuGqmDltvjU1ZxJ982VQz2fBPrlZQYGCykQRlnXX4ElB++a1bfXGRHqL0S/YryPED6t3bZiIEwpnXuSPqBoeCEDXpvg4g73Y4JFsJjhQmyShkRPuKsbuhqoA93acwoggV62h2VRzDW6J86kV0kUaISIscLxlpI0zfVV/llbueu6CLgTK/yp0zAzasArfVVxAieisWA+o2z7D77GAJzCIlatirQyEwcBrxM3nKSd0072bqoXE4PNPKjv5geM3mjUWdix2KFm4=");

}

