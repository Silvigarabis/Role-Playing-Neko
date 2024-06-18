package io.github.silvigarabis.rplayneko.feature;

import io.github.silvigarabis.rplayneko.event.*;

public class NekoRegexpSpeakReplaceFeature<Player> extends RegexpSpeakReplaceFeature<Player> {
    public void onChatEvent(ChatEvent<Player> event){
        if (!event.getPlayer().getData().isNeko()){
            return;
        }
        super.onChatEvent(event);
    }
}
