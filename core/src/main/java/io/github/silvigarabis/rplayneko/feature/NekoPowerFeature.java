package io.github.silvigarabis.rplayneko.feature;

import io.github.silvigarabis.rplayneko.event.*;
import io.github.silvigarabis.rplayneko.data.*;

public abstract class NekoPowerFeature<Player> implements IFeature<Player> {
    public void tick(RPlayNekoPlayer<Player> player){
        if (!player.getData().isNeko()){
            return;
        }
        for (var power : player.getPowers().values()){
            power.tick();
        }
    }
}
