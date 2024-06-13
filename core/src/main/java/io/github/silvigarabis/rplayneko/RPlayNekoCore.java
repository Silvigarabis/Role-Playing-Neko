package io.github.silvigarabis.rplayneko;

import io.github.silvigarabis.rplayneko.event.*;
import io.github.silvigarabis.rplayneko.data.*;
import io.github.silvigarabis.rplayneko.power.*;
import io.github.silvigarabis.rplayneko.command.*;
import io.github.silvigarabis.rplayneko.storage.IDataTarget;

import org.jetbrains.annotations.*;
import java.util.logging.Logger;
import java.util.*;

public class RPlayNekoCore<Sender, Player> {
    private static RPlayNekoCore<?, ?> INSTANCE = null;
    public static RPlayNekoCore<?, ?> getInstance(){
        if (INSTANCE == null){
            throw new IllegalStateException("core not init");
        }
        return INSTANCE;
    }
    public static @Nullable RPlayNekoPlayer<?> Instance_getNekoPlayer(@NotNull UUID uuid){
        return getInstance().getNekoPlayer(uuid);
    }

    private final Map<Player, RPlayNekoPlayer<Player>> nekoPlayerOriginMap = new WeakHashMap<>();
    public @NotNull RPlayNekoPlayer<Player> getNekoPlayer(@NotNull UUID uuid, @NotNull Player origin){
        RPlayNekoPlayer<Player> player;
        synchronized(nekoPlayerOriginMap){
            if (nekoPlayerOriginMap.containsKey(origin)){
                return nekoPlayerOriginMap.get(origin);
            }
        
            player = platform.newRPlayNekoPlayer(uuid, this, origin);
            nekoPlayerOriginMap.put(origin, player);
        }
        dataSource.markDirty(uuid);
        return player;
    }
    public @Nullable RPlayNekoPlayer<Player> getNekoPlayer(@NotNull UUID uuid){
        Player player = platform.getPlayerByUuid(uuid);
        synchronized(nekoPlayerOriginMap){
            return nekoPlayerOriginMap.get(player);
        }
    }

    private RPlayNekoDataSource dataSource;
    public RPlayNekoDataSource getDataSource(){
        return dataSource;
    }

    private RPlayNekoConfig config;
    public RPlayNekoConfig getConfig(){
        return config;
    }

    private Class<Player> PlayerClass;

    private Platform<Sender, Player> platform;
    public Platform getPlayform(){
        return platform;
    }

    public Logger getLogger(){
        return platform.getLogger();
    }

    public void reloadConfig(){
        this.config = platform.getCoreConfig();
    }

    private Messages<Sender, Player> messages;
    public Messages<Sender, Player> getMessages(){
        return messages;
    }

    public RPlayNekoCore(Platform platform, Class<Player> PlayerClass, IDataTarget dataTarget){
        this.platform = platform;
        this.PlayerClass = PlayerClass;
        this.messages = new Messages(platform);
        this.config = platform.getCoreConfig();
        this.dataSource = new RPlayNekoDataSource(dataTarget);
        INSTANCE = this;
    }

    public void stop(){
        this.dataSource.unloadAllData();
    }

    public RPlayNekoPower<Player> newPowerInstance(RPlayNekoPowerType type, RPlayNekoPlayer<Player> player){
        return platform.getPowerFactory().newPowerInstance(type, player);
    }

    public void registerCommands(){
        this.platform.registerCommand(new CommandRPlayNeko(this));
    }

    public boolean checkPermission(Sender sender, String permission){
        return this.platform.checkPermission(sender, "rplayneko." + permission);
    }

    public ChatEvent<Player> onChatEvent(ChatEvent<Player> event){
        return event;
    }
}