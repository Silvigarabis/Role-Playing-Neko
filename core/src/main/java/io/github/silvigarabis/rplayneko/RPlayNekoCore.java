package io.github.silvigarabis.rplayneko;

import io.github.silvigarabis.rplayneko.event.*;
import io.github.silvigarabis.rplayneko.data.*;
import io.github.silvigarabis.rplayneko.power.*;
import io.github.silvigarabis.rplayneko.command.*;

import org.jetbrains.annotations.*;
import java.util.function.Consumer;
import java.util.logging.Logger;
import java.util.Map;
import java.util.UUID;
import java.util.WeakHashMap;

public class RPlayNekoCore<Instance, Sender, Player> {
    private final Map<Player, RPlayNekoPlayer<Player>> nekoPlayerOriginMap = new WeakHashMap<>();
    public synchronized RPlayNekoPlayer<Player> getNekoPlayer(@NotNull UUID uuid, @NotNull Player origin){
        if (nekoPlayerOriginMap.containsKey(origin)){
            return nekoPlayerOriginMap.get(origin);
        }
        var player = new RPlayNekoPlayer<Player>(uuid, this.dataSource, origin, this);
        nekoPlayerOriginMap.put(origin, player);
        dataSource.markDirty(uuid);
        return player;
    }

    private RPlayNekoDataSource<Player> dataSource;
    public RPlayNekoDataSource<Player> getDataSource(){
        return dataSource;
    }
    private static RPlayNekoCore<?, ?, ?> instance;
    public static RPlayNekoCore<?, ?, ?> getInstance(){
        return instance;
    }
    private RPlayNekoConfig config;
    public RPlayNekoConfig getConfig(){
        return config;
    }
    private Platform<Instance, Sender, Player> platform;
    private Class<Player> PlayerClass;
    public Platform getPlayform(){
        return platform;
    }
    public Logger getLogger(){
        return platform.getLogger();
    }
    public void reloadConfig(){
        if (this.config != null){
            platform.saveCoreConfig(this.config);
        }
        this.config = platform.getCoreConfig();
    }
    private Messages<Instance, Sender, Player> messages;
    public Messages<Instance, Sender, Player> getMessages(){
        return messages;
    }
    public RPlayNekoCore(Platform platform, Class<Player> PlayerClass){
        this.platform = platform;
        this.PlayerClass = PlayerClass;
        this.messages = new Messages(platform);
        reloadConfig();
        this.dataSource = new RPlayNekoDataSource<>(platform.getCoreDataTarget());
        instance = this;
    }
    public void addTickImpl(Consumer<RPlayNekoPlayer<?>> tickImpl){
        RPlayNekoPlayer.addTickImpl(PlayerClass, tickImpl);
    }
    public void addPowerTypeTickImpl(RPlayNekoPowers type, Consumer<RPlayNekoPlayer<?>> tickImpl){
        RPlayNekoPowers.addPowerTypeTickImpl(type, PlayerClass, tickImpl);
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