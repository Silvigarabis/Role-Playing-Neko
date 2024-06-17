package io.github.silvigarabis.rplayneko.feature;

import io.github.silvigarabis.rplayneko.event.*;

public abstract class NekoModelFeature<Player> implements IFeature<Player> {
    public abstract void onNekoTransformEvent(NekoTransformEvent<Player> event);
}
