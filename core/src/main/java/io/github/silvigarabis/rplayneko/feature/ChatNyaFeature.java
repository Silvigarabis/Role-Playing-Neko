package io.github.silvigarabis.rplayneko.feature;

import io.github.silvigarabis.rplayneko.event.*;
import static io.github.silvigarabis.rplayneko.Messages.MessageKey.NEKO_NYA_TEXT;

public abstract class ChatNyaFeature<Player> implements IFeature<Player> {
    public void onChatEvent(ChatEvent<Player> event){
        if (!event.getPlayer().getData().isNeko()){
            return;
        }
        String nyaText = event.getPlayer().getData().getNyaText();
        if (nyaText == null){
            event.getPlayer().getCore().getMessages().getMessage(event.getPlayer().getOrigin(), NEKO_NYA_TEXT);
        }
        if (nyaText != null && nyaText.length() > 0){
            event.setChatSuffix(nyaText);
        }
    }
}
