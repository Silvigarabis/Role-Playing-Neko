package io.github.silvigarabis.rplayneko.power;

import io.github.silvigarabis.rplayneko.data.*;

public abstract class NightVisionPower<Player> extends AbstractStatusEffectPower<Player> {
    protected NightVisionPower(Player player){
        super(RPlayNekoPowerType.NIGHT_VISION, player, "night_vision", 0);
    }
}
