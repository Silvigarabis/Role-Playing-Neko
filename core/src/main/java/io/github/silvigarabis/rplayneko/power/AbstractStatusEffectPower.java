package io.github.silvigarabis.rplayneko.power;

import io.github.silvigarabis.rplayneko.data.*;

public abstract class AbstractStatusEffectPower<Player> extends RPlayNekoPower<Player> {
    public String statusEffect;
    public int amplifier;

    protected AbstractStatusEffectPower(RPlayNekoPowerType type, Player player, String statusEffect, int amplifier){
        super(type, player);
        this.statusEffect = statusEffect;
        this.amplifier = amplifier;
    }
}
