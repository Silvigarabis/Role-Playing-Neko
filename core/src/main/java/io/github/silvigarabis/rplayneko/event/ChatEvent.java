package io.github.silvigarabis.rplayneko.event;

import io.github.silvigarabis.rplayneko.data.*;

public class ChatEvent<Player> extends Event<Player> {
    private @Nullable String namePrefix = null;
    public @Nullable String getNamePrefix(){
        return namePrefix;
    }
    public void setNameSuffix(@Nullable String namePrefix){
        this.namePrefix = namePrefix;
        markModified();
    }

    private @Nullable String nameSuffix = null;
    public @Nullable String getNameSuffix(){
        return nameSuffix;
    }
    public void setNameSuffix(@Nullable String nameSuffix){
        this.nameSuffix = nameSuffix;
        markModified();
    }

    private @Nullable String chatPrefix = null;
    public @Nullable String getChatPrefix(){
        return chatPrefix;
    }
    public void setChatPrefix(@Nullable String chatPrefix){
        this.chatPrefix = chatPrefix;
        markModified();
    }

    private @Nullable String chatSuffix = null;
    public @Nullable String getChatSuffix(){
        return chatSuffix;
    }
    public void setChatSuffix(@Nullable String chatSuffix){
        this.chatSuffix = chatSuffix;
        markModified();
    }

    private @NotNull String message;
    public String getMessage(){
        return message;
    }
    public void setMessage(@NotNull String message){
        this.message = message;
        markModified();
    }

    public ChatEvent(RPlayNekoPlayer<Player> player, @NotNull String message){
        super(player);
        this.message = message;
    }

    @Override
    public boolean isCancelable(){
        return true;
    }
}
