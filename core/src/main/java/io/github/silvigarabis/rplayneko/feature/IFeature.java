package io.github.silvigarabis.rplayneko.feature;

import io.github.silvigarabis.rplayneko.data.*;
import io.github.silvigarabis.rplayneko.event.*;

public interface IFeature<Player> {
    default void onChatEvent(ChatEvent<Player> event){
    }
    default void onEvent(Event<Player> event){
    }
    default void tick(RPlayNekoPlayer<Player> player){
    }
    default void onCoreReload(){
    }
    default void onCoreStop(){
    }
}
