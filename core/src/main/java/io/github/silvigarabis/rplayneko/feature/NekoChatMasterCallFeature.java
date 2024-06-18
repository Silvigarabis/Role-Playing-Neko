package io.github.silvigarabis.rplayneko.feature;

import io.github.silvigarabis.rplayneko.util.*;
import io.github.silvigarabis.rplayneko.event.*;
import static io.github.silvigarabis.rplayneko.Messages.MessageKey.NEKO_CALL_MASTER;

public class NekoChatMasterCallFeature<Player> implements IFeature<Player> {
    public void onChatEvent(ChatEvent<Player> event){
        if (!event.getPlayer().getData().isNeko()){
            return;
        }
        String message = event.getMessage();
        String callName = event.getPlayer().getCore().getMessages().getMessage(event.getPlayer().getOrigin(), NEKO_CALL_MASTER);

        for (String pattern : event.getPlayer().getData().getMasterCalls()){
            message.replace(pattern, callName);
        }

        if (message != event.getMessage()){
            event.setMessage(message);
        }
    }
}
