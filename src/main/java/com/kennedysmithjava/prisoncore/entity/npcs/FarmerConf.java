package com.kennedysmithjava.prisoncore.entity.npcs;

import com.kennedysmithjava.prisoncore.npc.Skin;
import com.massivecraft.massivecore.command.editor.annotation.EditorName;
import com.massivecraft.massivecore.store.Entity;
import com.massivecraft.massivecore.util.MUtil;

import java.util.List;

@EditorName("config")
public class FarmerConf extends Entity<FarmerConf>
{
    // -------------------------------------------- //
    // META
    // -------------------------------------------- //

    protected static transient FarmerConf i;

    public static FarmerConf get() { return i; }

    @Override
    public FarmerConf load(FarmerConf that)
    {
        super.load(that);
        return this;
    }

    public String farmerName = "Frank";
    public String farmerLineNoPermission = "&7[&bFrank&7] I don't think you have permission to order from me, pal!";
    public String farmerLineWelcome = "&7[&bArchie&7] What can I  for ya today, chief?";
    public List<String> farmerHologram = MUtil.list("&7&m-------------", "Frank the farmer", "&e&lRIGHT CLICK", "&7&m-------------");
    public Skin farmerSkin = new Skin(
            "ewogICJ0aW1lc3RhbXAiIDogMTYyMjk3ODUxNzIxNywKICAicHJvZmlsZUlkIiA6ICIzYTNmNzhkZmExZjQ0OTllYjE5NjlmYzlkOTEwZGYwYyIsCiAgInByb2ZpbGVOYW1lIiA6ICJOb19jcmVyYXIiLAogICJzaWduYXR1cmVSZXF1aXJlZCIgOiB0cnVlLAogICJ0ZXh0dXJlcyIgOiB7CiAgICAiU0tJTiIgOiB7CiAgICAgICJ1cmwiIDogImh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYzA0NjVhNzhlNTZhZjcyY2M2ZGNlM2IyMjRhNGFlOWM5YWU5MjI2ZjAzZDJlOGE1MzhlZTI0ZmM2NmY1MmQxYiIKICAgIH0KICB9Cn0=",
            "Nv4NwxTlDCgbzqXxS7kfGG8suIB7Oo7X9WIm4dJdiJlqz62twnnykAoFUxZSeqF9tPwFnnV4o7xgBPy7OdzTkSVzTDkFZSCSYjPZgV8tZdGUqGxpeFvdSwXXC2fCILyAmebjzLytASEP15haxbmmBxosQ2cWERdRrtM9JJfINbTh/7/TNSiAb5N520cwbWKOhTcg3fU5ZigMkpIREFEhEhELBSLpoU+6PdOGs5FWDR8/hGvyblTyXU8DEZ+R68krP/OSk81Omz8yuZu54W5tbsC2+iWqlGr4hdGKz691xUdWmQ4Z1kz+nlAPUIvXsLa7yzdExHtZ6LjPBibk8WrBkJgBadyWx/zDqKn5rM2TsGqnw+YjHIMyc6HR+LSeuDKPRZ89NMQrG+cJnY5547pe8TeKE00ZrXJMBk9MCzx9IQU4TLCcTrvpL2tLXtWjxQlGrtFE8CF/eN+4d53IwbWgCsS+U6hxdhmJq0fWLV/RG3NYQsvcMhTXkzIOOZmTfzFLU8adw16g9Y44dgKBiebCZrHsJbNjrDrcdK/UALUJwxEcZDix6N5rRSiwb1/uFsE8hmnNWTXQgzLz9q/Nd6tA2iPPht6KKxlNdevve1rQ4K5u7xG52OrltFk0Jw9PJUExrcAvZpfyOtOITfEfFiwGn1ns2ndsKifX81uFrBKg7kc=");
}

