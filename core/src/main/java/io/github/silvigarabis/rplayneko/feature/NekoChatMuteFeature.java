package io.github.silvigarabis.rplayneko.feature;

import io.github.silvigarabis.rplayneko.Messages;
import io.github.silvigarabis.rplayneko.event.*;

public abstract class NekoChatMuteFeature<Player> implements IFeature<Player> {
    public void onChatEvent(ChatEvent<Player> event){
        if (!event.getPlayer().getData().isNeko()){
            return;
        }
        if (event.getPlayer().getData().isMuted()){
            event.setCancelled(true);
            event.getPlayer().sendMessage(Messages.MessageKey.NOTICE_NEKO_CHAT_MUTED);
        }
    }
}
