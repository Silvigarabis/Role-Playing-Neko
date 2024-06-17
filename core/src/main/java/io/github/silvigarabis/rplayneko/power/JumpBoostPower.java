package io.github.silvigarabis.rplayneko.power;

import io.github.silvigarabis.rplayneko.data.*;

public abstract class JumpBoostPower<Player> extends AbstractStatusEffectPower<Player> {
    protected JumpBoostPower(Player player){
        super(RPlayNekoPowerTypes.JUMP_BOOST, player, "jump_boost", 2);
    }
}
