package com.kennedysmithjava.prisoncore.entity.npcs;

import com.kennedysmithjava.prisoncore.npc.Skin;
import com.massivecraft.massivecore.command.editor.annotation.EditorName;
import com.massivecraft.massivecore.store.Entity;
import com.massivecraft.massivecore.util.MUtil;

import java.util.Map;

@EditorName("config")
public class SkinsConf extends Entity<SkinsConf>
{
    // -------------------------------------------- //
    // META
    // -------------------------------------------- //

    protected static transient SkinsConf i;

    public static SkinsConf get() { return i; }

    @Override
    public SkinsConf load(SkinsConf that)
    {
        super.load(that);
        return this;
    }


    public Map<String, Skin> customSkins = MUtil.map(
            "Warren", new Skin(
                    "ewogICJ0aW1lc3RhbXAiIDogMTYyNzgwMDc0MTI4NSwKICAicHJvZmlsZUlkIiA6ICJmNThkZWJkNTlmNTA0MjIyOGY2MDIyMjExZDRjMTQwYyIsCiAgInByb2ZpbGVOYW1lIiA6ICJ1bnZlbnRpdmV0YWxlbnQiLAogICJzaWduYXR1cmVSZXF1aXJlZCIgOiB0cnVlLAogICJ0ZXh0dXJlcyIgOiB7CiAgICAiU0tJTiIgOiB7CiAgICAgICJ1cmwiIDogImh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZWVlNmFjZjAyY2Y0Y2NmMGRiMTcwNDQwMGI3NWYwZjFlYWM5YmQ5Y2M0MTY5N2E2OTk2OWYwMzMzMzA5OWNjMSIKICAgIH0KICB9Cn0=",
                    "dmqa8B1S3P48i38CnCZjD0rPBsoPlCTXmzDlsgoN7IqH1yXAAqQiB1xRZOnhWXpaLnag6izQfFFOte/ldo7bnEhSauFcHWF8Y4VoBoIFlpx6aSLgSGhfni7QNyfUEJ0f6UFlYUcP1/LVwTQK/N/AQR3wvE+owilpc0AIQV2VDcY9eaJGzKf9Hfift4T+9+5VlY/LGGIG9+vqvsUiZH9fCAc8Ixg/XqzXKqqOkfw4ETAUUq5to1zMGjYwmBelpPUlSgfjbPNU23+mFbRepIXTZa0UcvYa8ASscv8GZyZdOw4hZ9TPzbuc9M3FEEOXLgY4BCgocgFEamhzAwSaYOHwL/8Zzz/I96+Wr/3riTgHwozZXbiedz8Dghyh3TfCvmLXpB6kWa0YhoYsgI9LO5Bx+Dw1zHjwS8PCYDtEuGqmDltvjU1ZxJ982VQz2fBPrlZQYGCykQRlnXX4ElB++a1bfXGRHqL0S/YryPED6t3bZiIEwpnXuSPqBoeCEDXpvg4g73Y4JFsJjhQmyShkRPuKsbuhqoA93acwoggV62h2VRzDW6J86kV0kUaISIscLxlpI0zfVV/llbueu6CLgTK/yp0zAzasArfVVxAieisWA+o2z7D77GAJzCIlatirQyEwcBrxM3nKSd0072bqoXE4PNPKjv5geM3mjUWdix2KFm4="),
                "Farmer", new Skin(
                    "ewogICJ0aW1lc3RhbXAiIDogMTYyMjk3ODUxNzIxNywKICAicHJvZmlsZUlkIiA6ICIzYTNmNzhkZmExZjQ0OTllYjE5NjlmYzlkOTEwZGYwYyIsCiAgInByb2ZpbGVOYW1lIiA6ICJOb19jcmVyYXIiLAogICJzaWduYXR1cmVSZXF1aXJlZCIgOiB0cnVlLAogICJ0ZXh0dXJlcyIgOiB7CiAgICAiU0tJTiIgOiB7CiAgICAgICJ1cmwiIDogImh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYzA0NjVhNzhlNTZhZjcyY2M2ZGNlM2IyMjRhNGFlOWM5YWU5MjI2ZjAzZDJlOGE1MzhlZTI0ZmM2NmY1MmQxYiIKICAgIH0KICB9Cn0=",
                    "Nv4NwxTlDCgbzqXxS7kfGG8suIB7Oo7X9WIm4dJdiJlqz62twnnykAoFUxZSeqF9tPwFnnV4o7xgBPy7OdzTkSVzTDkFZSCSYjPZgV8tZdGUqGxpeFvdSwXXC2fCILyAmebjzLytASEP15haxbmmBxosQ2cWERdRrtM9JJfINbTh/7/TNSiAb5N520cwbWKOhTcg3fU5ZigMkpIREFEhEhELBSLpoU+6PdOGs5FWDR8/hGvyblTyXU8DEZ+R68krP/OSk81Omz8yuZu54W5tbsC2+iWqlGr4hdGKz691xUdWmQ4Z1kz+nlAPUIvXsLa7yzdExHtZ6LjPBibk8WrBkJgBadyWx/zDqKn5rM2TsGqnw+YjHIMyc6HR+LSeuDKPRZ89NMQrG+cJnY5547pe8TeKE00ZrXJMBk9MCzx9IQU4TLCcTrvpL2tLXtWjxQlGrtFE8CF/eN+4d53IwbWgCsS+U6hxdhmJq0fWLV/RG3NYQsvcMhTXkzIOOZmTfzFLU8adw16g9Y44dgKBiebCZrHsJbNjrDrcdK/UALUJwxEcZDix6N5rRSiwb1/uFsE8hmnNWTXQgzLz9q/Nd6tA2iPPht6KKxlNdevve1rQ4K5u7xG52OrltFk0Jw9PJUExrcAvZpfyOtOITfEfFiwGn1ns2ndsKifX81uFrBKg7kc=")
    );



    public Map<String, Skin> getCustomSkins() {
        return customSkins;
    }

    public Skin getSkin(String name){
        return customSkins.get(name);
    }
}

