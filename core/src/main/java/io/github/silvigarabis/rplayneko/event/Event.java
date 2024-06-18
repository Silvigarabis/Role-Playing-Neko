package io.github.silvigarabis.rplayneko.event;

import io.github.silvigarabis.rplayneko.data.*;
import org.jetbrains.annotations.*;

public abstract class Event<Player> {
    protected @NotNull EventResult result = EventResult.NORMAL;
    public void setResult(@NotNull EventResult result){
        this.result = result;
    }
    public @NotNull EventResult getResult(){
        return result;
    }
    protected final RPlayNekoPlayer<Player> player;
    public RPlayNekoPlayer<Player> getPlayer(){
        return player;
    }
    public Event(@NotNull RPlayNekoPlayer<Player> player){
        this.player = player;
    }
    public boolean isCancelled(){
        return result == EventResult.CANCELLED;
    }
    public void setCancelled(boolean cancelled){
        if (!isCancelable()){
            throw new IllegalStateException("event cannot be cancelled");
        }
        if (result != EventResult.CANCELLED && cancelled){
            result = EventResult.CANCELLED;
        } else if (!cancelled && result == EventResult.CANCELLED){
            result = EventResult.MODIFIED;
        }
    }
    public void markModified(){
        if (!iModifiable()){
            throw new IllegalStateException("event cannot be modified");
        }
        if (result == EventResult.NORMAL){
            result = EventResult.MODIFIED;
        }
    }

    public boolean iModifiable(){
        return false;
    }
    public boolean isCancelable(){
        return false;
    }
}
