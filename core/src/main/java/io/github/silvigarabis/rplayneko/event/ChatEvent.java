package io.github.silvigarabis.rplayneko.event;

import io.github.silvigarabis.rplayneko.data.*;

public class ChatEvent<Player> {
    public String namePrefix = null;
    public String chatSuffix = null;
    public RPlayNekoPlayer<Player> player;
    public String message;
    public EventResult result;
    public ChatEvent(RPlayNekoPlayer<Player> player, String message){
        this.player = player;
        this.message = message;
        this.result = EventResult.PASS;
    }
}
