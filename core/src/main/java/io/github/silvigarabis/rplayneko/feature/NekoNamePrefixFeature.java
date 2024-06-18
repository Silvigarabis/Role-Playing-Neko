package io.github.silvigarabis.rplayneko.feature;

import io.github.silvigarabis.rplayneko.event.*;
import static io.github.silvigarabis.rplayneko.Messages.MessageKey.NEKO_NAME_PREFIX;

public class NekoNamePrefixFeature<Player> implements IFeature<Player> {
    public void onChatEvent(ChatEvent<Player> event){
        if (!event.getPlayer().getData().isNeko()){
            return;
        }
        String prefix = event.getPlayer().getCore().getMessages().getMessage(event.getPlayer().getOrigin(), NEKO_NAME_PREFIX);
        if (prefix != null && prefix.length() > 0){
            event.setNamePrefix(prefix);
        }
    }
}
