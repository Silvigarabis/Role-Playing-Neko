package io.github.silvigarabis.rplayneko;

import io.github.silvigarabis.rplayneko.event.*;
import io.github.silvigarabis.rplayneko.data.*;
import io.github.silvigarabis.rplayneko.util.*;
import io.github.silvigarabis.rplayneko.feature.IFeature;
import io.github.silvigarabis.rplayneko.power.RPlayNekoPowerType;
import io.github.silvigarabis.rplayneko.power.RPlayNekoPower;

import org.jetbrains.annotations.*;
import java.util.logging.Logger;
import java.util.*;
import java.util.function.Consumer;

/**
 * 使用要求：
 * 创建并加载配置后，请调用 initDataSource()，务必保证 platform.getDataTarget() 已经可以正常返回结果
 * 每1秒调用一次 tick()（除了Folia平台）（这会为每一个玩家调用 tick()）
 * 需要触发事件请使用 emitEvent(Event)
 */
public class RPlayNekoCore<Sender, Player> {
    private static RPlayNekoCore<?, ?> INSTANCE = null;
    public static RPlayNekoCore<?, ?> getInstance(){
        if (INSTANCE == null){
            throw new IllegalStateException("core not init");
        }
        return INSTANCE;
    }

    private final Map<Player, RPlayNekoPlayer<Player>> nekoPlayerOriginMap = new WeakHashMap<>();
    private final Map<Player, RPlayNekoPlayer<Player>> nekoPlayerOriginMapView = Collections.unmodifiableMap(nekoPlayerOriginMap);
    public @UnmodifiableView Map<Player, RPlayNekoPlayer<Player>> getNekoPlayerOriginMapView(){
        return nekoPlayerOriginMapView;
    }
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

    private Set<IFeature<Player>> enabledFeatures = new HashSet<>();
    private Set<IFeature<Player>> enabledFeaturesView = Collections.unmodifiableSet(enabledFeatures);
    public void addFeature(IFeature<Player> feature){
        enabledFeatures.add(feature);
    }
    public void removeFeature(IFeature<Player> feature){
        enabledFeatures.remove(feature);
    }
    public @UnmodifiableView Set<IFeature<Player>> getEnabledFeatures(){
        return enabledFeaturesView;
    }

    public Logger getLogger(){
        return platform.getLogger();
    }
    private Messages<Sender, Player> messages;
    public Messages<Sender, Player> getMessages(){
        return messages;
    }

    public void reload(){
        forEachRun(this.enabledFeatures, "execute callback onCoreReload", f -> f.onCoreReload());
        this.dataSource.close();
        this.dataSource = null;
        this.reloadConfig();
        this.initDataSource();
    }

    public void stop(){
        forEachRun(this.enabledFeatures, "execute callback onCoreStop", f -> f.onCoreStop());
        enabledFeatures.clear();
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

    public void tick(){
        for (Player origin : platform.getPlayers()){
            var player = getNekoPlayer(platform.getPlayerUuid(origin), origin);
            player.tick();
        }
    }

    public void emitEvent(Event<Player> event){
        forEachRun(this.enabledFeatures, "passing event " + event.getClass().getName(), f -> f.onEvent(event));
        if (event instanceof ChatEvent<Player> tEvent){
            forEachRun(this.enabledFeatures, "passing ChatEvent", f -> f.onChatEvent(tEvent));
        } else if (event instanceof NekoTransformEvent<Player> tEvent){
            forEachRun(this.enabledFeatures, "passing NekoTransformEvent", f -> f.onNekoTransformEvent(tEvent));
        }
    }

    public <T> void forEachRun(Iterable<T> it, String message, Consumer<T> a){
        for (T value : it){
            try {
                a.accept(value);
            } catch (Throwable ex){
                this.getLogger().log(java.util.logging.Level.SEVERE, "error while " + message, ex);
            }
        }
    }
}
