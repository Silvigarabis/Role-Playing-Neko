package io.github.silvigarabis.rplayneko.feature;

import io.github.silvigarabis.rplayneko.event.*;

public abstract class NekoFollowOwnerFeature<Player> implements IFeature<Player> {
    public abstract void tick(RPlayNekoPlayer<Player> player);
}
