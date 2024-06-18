package io.github.silvigarabis.rplayneko;

import io.github.silvigarabis.rplayneko.event.*;
import io.github.silvigarabis.rplayneko.data.*;
import io.github.silvigarabis.rplayneko.power.*;
import io.github.silvigarabis.rplayneko.util.*;

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
        synchronized(nekoPlayerOriginMap){
            var player = nekoPlayerOriginMap.get(origin);
            if (player != null){
                return player;
            }
        }

        RPlayNekoData playerData = getDataSource().fetchData(uuid);
        RPlayNekoPlayer<Player> player = new RPlayNekoPlayer<>(this, origin, playerData);
        synchronized(nekoPlayerOriginMap){
            nekoPlayerOriginMap.put(origin, player);
        }
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
        if (dataSource == null){
            throw new IllegalStateException("dataSource not initiated");
        }
        return dataSource;
    }
    public boolean initDataSource(){
        if (dataSource == null){
            return false;
        }
        this.dataSource = new RPlayNekoDataSource(this.platform.getDataTarget());
        return true;
    }
    public void reloadData(){
        if (this.dataSource != null){
            this.dataSource = null;
            this.dataSource.close();
        }
        this.initDataSource();
    }

    private RPlayNekoConfig config;
    public RPlayNekoConfig getConfig(){
        return config;
    }
    private void reloadConfig(){
        this.config = platform.getCoreConfig();
        var messageConfig = platform.getMessageConfig();
        Messages.cleanMessageConfig();
        Messages.loadMessageConfig(messageConfig);
    }

    public RPlayNekoCore(Platform<Sender, Player> platform){
        this.platform = platform;
        this.messages = new Messages<>(this);
        this.reloadConfig();
        INSTANCE = this;
    }

    private Platform<Sender, Player> platform;
    public Platform<Sender, Player> getPlatform(){
        return platform;
    }

    public Logger getLogger(){
        return platform.getLogger();
    }
    private Messages<Sender, Player> messages;
    public Messages<Sender, Player> getMessages(){
        return messages;
    }

    public void reload(){
        this.dataSource.close();
        this.dataSource = null;
        this.reloadConfig();
        this.initDataSource();
    }

    public void stop(){
        this.dataSource.unloadAllData();
        this.dataSource.close();
        this.dataSource = null;
    }

    public RPlayNekoPower<Player> newPowerInstance(RPlayNekoPowerType type, RPlayNekoPlayer<Player> player){
        return platform.getPowerFactory(type).newPowerInstance(player);
    }

    public boolean checkPermission(Sender sender, String permission){
        return this.platform.checkPermission(sender, "rplayneko." + permission);
    }
}
