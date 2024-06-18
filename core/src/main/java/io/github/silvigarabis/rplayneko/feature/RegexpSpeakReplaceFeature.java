package io.github.silvigarabis.rplayneko.feature;

import io.github.silvigarabis.rplayneko.event.*;
import io.github.silvigarabis.rplayneko.util.*;
import java.util.Map;

public class RegexpSpeakReplaceFeature<Player> implements IFeature<Player> {
    public void onChatEvent(ChatEvent<Player> event){
        if (event.isCancelled()){
            return;
        }
        String message = event.getMessage();
        for (Map.Entry<String, String> replace : event.getPlayer().getData().getRegexpSpeakReplaces().entrySet()){
            message = StringUtil.timeLimitedReplaceAll(message, replace.getKey(), replace.getValue());
        }
        if (message != event.getMessage()){
            event.setMessage(message);
        }
    }
}
