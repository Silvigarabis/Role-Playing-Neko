package io.github.silvigarabis.rplayneko.feature;

import io.github.silvigarabis.rplayneko.event.*;

public abstract class ChatNyaFeature<Player> implements IFeature<Player> {
    public void onChatEvent(ChatEvent<Player> event){
        if (!event.getPlayer().getData().isNeko()){
            return;
        }
        event.getPlayer().getData().getNyaText();
    }
}

/*
        if (getConfig().feature_ChatNya && event.player.isNeko()){
            switch (getConfig().feature_ChatNya_Location){
                case END:
                    event.chatSuffix = event.player.getNyaText();
                    break;
                case HEAD:
                    event.chatPrefix = event.player.getNyaText() + " ";
                    break;
                case CUSTOM:
                default:
                    break;
            }
            event.result = EventResult.MODIFIED;
        }
*/