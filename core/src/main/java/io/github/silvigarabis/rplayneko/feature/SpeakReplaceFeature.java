package io.github.silvigarabis.rplayneko.feature;

import io.github.silvigarabis.rplayneko.event.*;
import java.util.Map;

public class SpeakReplaceFeature<Player> implements IFeature<Player> {
    public void onChatEvent(ChatEvent<Player> event){
        if (event.isCancelled()){
            return;
        }
        String message = event.getMessage();
        for (Map.Entry<String, String> replace : event.getPlayer().getData().getSpeakReplaces().entrySet()){
            message = message.replace(replace.getKey(), replace.getValue());
        }
        if (message != event.getMessage()){
            event.setMessage(message);
        }
    }
}
