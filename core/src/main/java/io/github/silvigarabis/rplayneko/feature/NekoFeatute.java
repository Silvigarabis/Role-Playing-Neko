package io.github.silvigarabis.rplayneko.feature;

import io.github.silvigarabis.rplayneko.event.*;

public abstract class NekoFeature<Player> implements IFeature<Player> {
    public void tick(RPlayNekoPlayer<Player> player){
        if (!player.getData().isNeko()){
            return;
        }
    }
}
