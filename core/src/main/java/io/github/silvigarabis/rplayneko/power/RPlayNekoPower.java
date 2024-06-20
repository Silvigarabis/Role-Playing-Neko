package io.github.silvigarabis.rplayneko.power;

import io.github.silvigarabis.rplayneko.data.*;

public abstract class RPlayNekoPower<Player> {
    private final RPlayNekoPlayer<Player> player;
    public RPlayNekoPlayer<Player> getPlayer(){
        return player;
    }
    private final RPlayNekoPowerType type;
    public RPlayNekoPowerType getType(){
        return type;
    }

    protected RPlayNekoPower(RPlayNekoPowerType type, RPlayNekoPlayer<Player> player){
        this.type = type;
        this.player = player;
    }
    public void tick(){
    }
}
