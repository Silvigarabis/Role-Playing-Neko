package io.github.silvigarabis.rplayneko.power;

import io.github.silvigarabis.rplayneko.data.*;

public abstract class RPlayNekoPower<Player> {
    public final Player player;
    public final RPlayNekoPowerType type;

    protected RPlayNekoPower(RPlayNekoPowerType type, Player player){
        this.type = type;
        this.player = player;
    }
    public abstract void tick();
}
