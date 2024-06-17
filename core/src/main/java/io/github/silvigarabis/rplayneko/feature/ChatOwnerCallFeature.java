package io.github.silvigarabis.rplayneko.feature;

import io.github.silvigarabis.rplayneko.event.*;

public abstract class ChatOwnerCallFeature<Player> implements IFeature<Player> {
    public void onChatEvent(ChatEvent<Player> event){
        if (!event.getPlayer().getData().isNeko()){
            return;
        }
        event.getPlayer().getData().getOwnerCalls();
    }
}
