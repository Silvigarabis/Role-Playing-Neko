package io.github.silvigarabis.rplayneko.feature;

import io.github.silvigarabis.rplayneko.event.*;

public abstract class NekoChatMuteFeature<Player> implements IFeature<Player> {
    public void onChatEvent(ChatEvent<Player> event){
        if (!event.getPlayer().getData().isNeko()){
            return;
        }
        event.getPlayer().getData().isMuted();
    }
}

/**
        // mute
        if (event.player.isNeko() && event.player.isMuted()){
            event.result = EventResult.CANCELLED;
            getMessages().sendPlayer(event.player.getOrigin(), Messages.MessageKey.NEKO_CHAT_MUTED);
        }
        
        */