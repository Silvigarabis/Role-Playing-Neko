package io.github.silvigarabis.rplayneko.event;

import io.github.silvigarabis.rplayneko.data.*;

public abstract class AbstractInteractEvent<Player> extends Event<Player> {

    public AbstractInteractEvent(RPlayNekoPlayer<Player> player){
        super(player);
    }
}
