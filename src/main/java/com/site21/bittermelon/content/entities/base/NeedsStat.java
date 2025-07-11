package com.site21.bittermelon.content.entities.base;

import com.site21.bittermelon.content.entities.implementations.chicken.Chicken;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;

public enum NeedsStat {
    SOCIALIZATION("socialization"),
    PROCREATION("procreation"),
    REST("rest"),
    STRESS("stress"),
    HUNGER("hunger"),
    THIRST("thirst"),
    DEFECATION("defecation"),
    MOVEMENT("movement"),
    HYGIENE("hygiene"),
    RELAXATION("relaxation"),
    RECREATION("recreation"),
    ANGER("anger"),
    BLOODLUST("bloodlust");

    private final String saveKey;

    NeedsStat(String saveKey) {
        this.saveKey = saveKey;
    }

    public String saveKey() {
        return saveKey;
    }
}
