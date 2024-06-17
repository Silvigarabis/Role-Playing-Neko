package io.github.silvigarabis.rplayneko.feature;

import io.github.silvigarabis.rplayneko.event.*;

public abstract class SpeakReplaceFeature<Player> implements IFeature<Player> {
    public void onChatEvent(ChatEvent<Player> event){
        event.getPlayer().getData().getSpeakReplaces();
        event.getPlayer().getData().getRegexpSpeakReplaces();
    }
}
