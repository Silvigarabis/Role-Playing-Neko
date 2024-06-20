package io.github.silvigarabis.rplayneko;

import io.github.silvigarabis.rplayneko.event.*;
import io.github.silvigarabis.rplayneko.data.*;
import io.github.silvigarabis.rplayneko.util.*;
import io.github.silvigarabis.rplayneko.feature.*;
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

    public RPlayNekoCore(Platform<Sender, Player> platform){
        this.platform = platform;
        this.messages = new Messages<>(this);
        INSTANCE = this;
    }

    private final Map<Player, RPlayNekoPlayer<Player>> nekoPlayerOriginMap = new WeakHashMap<>();
    private final Map<Player, RPlayNekoPlayer<Player>> nekoPlayerOriginMapView = Collections.unmodifiableMap(nekoPlayerOriginMap);
    public @UnmodifiableView Map<Player, RPlayNekoPlayer<Player>> getNekoPlayerOriginMapView(){
        checkIsRunning();

        return nekoPlayerOriginMapView;
    }
    public @NotNull RPlayNekoPlayer<Player> getNekoPlayer(@NotNull UUID uuid, @NotNull Player origin){
        checkIsRunning();

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
        checkIsRunning();

        Player player = platform.getPlayerByUuid(uuid);
        synchronized(nekoPlayerOriginMap){
            return nekoPlayerOriginMap.get(player);
        }
    }

    private RPlayNekoDataSource dataSource;
    public RPlayNekoDataSource getDataSource(){
        checkIsRunning();

        if (dataSource == null){
            throw new IllegalStateException("dataSource not initiated");
        }
        return dataSource;
    }
    public boolean initDataSource(){
        checkIsRunning();

        if (dataSource == null){
            return false;
        }
        this.dataSource = new RPlayNekoDataSource(this.platform.getDataTarget());
        return true;
    }

    private RPlayNekoConfig config;
    public RPlayNekoConfig getConfig(){
        checkIsRunning();

        return config;
    }

    private Platform<Sender, Player> platform;
    public Platform<Sender, Player> getPlatform(){
        checkIsRunning();

        return platform;
    }

    public Logger getLogger(){
        checkIsRunning();

        return platform.getLogger();
    }

    private Messages<Sender, Player> messages;
    public Messages<Sender, Player> getMessages(){
        checkIsRunning();

        return messages;
    }

    private Set<IFeature<Player>> enabledFeatures = new HashSet<>();
    private Set<IFeature<Player>> enabledFeaturesView = Collections.unmodifiableSet(enabledFeatures);
    public void addFeature(IFeature<Player> feature){
        checkIsRunning();

        enabledFeatures.add(feature);
    }
    public void removeFeature(IFeature<Player> feature){
        checkIsRunning();

        enabledFeatures.remove(feature);
    }
    public @UnmodifiableView Set<IFeature<Player>> getEnabledFeatures(){
        checkIsRunning();

        return enabledFeaturesView;
    }

    public void reloadConfig(){
        checkIsRunning();

        this.config = platform.getCoreConfig();
        var messageConfig = platform.getMessageConfig();
        Messages.cleanMessageConfig();
        Messages.loadMessageConfig(messageConfig);
    }

    private void reloadData(){
        checkIsRunning();

        if (this.dataSource != null){
            this.dataSource.close();
            this.dataSource = null;
        }
        this.initDataSource();
        for (Player player : platform.getPlayers()){
            UUID uuid = platform.getPlayerUuid(player);
            getNekoPlayer(uuid, player); // to load playerdata
        }
    }

    //currently unused
    private String[] IdentifiedFeatures = new String[]{
        "NekoChatMasterCallFeature", "NekoChatMuteFeature",
        "NekoChatNyaFeature", "NekoNamePrefixFeature", "NekoPowerFeature",
        "NekoRegexpSpeakReplaceFeature", "NekoSpeakReplaceFeature",
        "RegexpSpeakReplaceFeature", "SpeakReplaceFeature"
    };

    public void reloadFeatures(){
        checkIsRunning();

        enabledFeatures.clear();
        for (String featureType : this.getConfig().getEnabledFeatures()){
            switch (featureType){
                case "NekoChatMasterCallFeature":
                    enabledFeatures.add(new NekoChatMasterCallFeature<>());
                    break;

                case "NekoChatMuteFeature":
                    enabledFeatures.add(new NekoChatMuteFeature<>());
                    break;

                case "NekoChatNyaFeature":
                    enabledFeatures.add(new NekoChatNyaFeature<>());
                    break;

                case "NekoNamePrefixFeature":
                    enabledFeatures.add(new NekoNamePrefixFeature<>());
                    break;

                case "NekoPowerFeature":
                    enabledFeatures.add(new NekoPowerFeature<>());
                    break;

                case "NekoRegexpSpeakReplaceFeature":
                    enabledFeatures.add(new NekoRegexpSpeakReplaceFeature<>());
                    break;

                case "NekoSpeakReplaceFeature":
                    enabledFeatures.add(new NekoSpeakReplaceFeature<>());
                    break;

                case "RegexpSpeakReplaceFeature":
                    enabledFeatures.add(new RegexpSpeakReplaceFeature<>());
                    break;

                case "SpeakReplaceFeature":
                    enabledFeatures.add(new SpeakReplaceFeature<>());
                    break;

                default:
                    IFeature<Player> platformedFeature = platform.getFeature(featureType);
                    if (platformedFeature != null){
                        enabledFeatures.add(platformedFeature);
                    } else {
                        getLogger().warning("unknown feature: " + featureType);
                    }
            }
        }
    }

    public void reload(){
        checkIsRunning();

        forEachRun(this.enabledFeatures, "execute callback onCoreReload", f -> f.onCoreReload());
        dataSource.unloadAllData();
        reloadConfig();
        reloadFeatures();
        reloadData();
    }

    private boolean isRunning = true;
    private void checkIsRunning(){
        if (!isRunning){
            throw new IllegalStateException("core not running");
        }
    }
    public void shutdown(){
        if (!isRunning) return;
        forEachRun(this.enabledFeatures, "execute callback onCoreStop", f -> f.onCoreStop());
        enabledFeatures.clear();
        if (this.dataSource != null){
            this.dataSource.unloadAllData();
            this.dataSource.close();
            this.dataSource = null;
        }
        isRunning = false;
    }

    public RPlayNekoPower<Player> newPowerInstance(RPlayNekoPowerType type, RPlayNekoPlayer<Player> player){
        checkIsRunning();

        var factory = platform.getPowerFactory(type);
        if (factory == null){
            throw new IllegalArgumentException("the specified power not implemented");
        }
        return factory.newPowerInstance(player);
    }

    public boolean checkPermission(Sender sender, String permission){
        checkIsRunning();

        return this.platform.checkPermission(sender, "rplayneko." + permission);
    }

    public boolean checkPermissionForPlayer(Player player, String permission){
        checkIsRunning();

        return this.platform.checkPermissionForPlayer(player, "rplayneko." + permission);
    }

    public void tick(){
        checkIsRunning();

        for (Player origin : platform.getPlayers()){
            var player = getNekoPlayer(platform.getPlayerUuid(origin), origin);
            player.tick();
        }
    }

    public void emitEvent(Event<Player> event){
        checkIsRunning();

        forEachRun(this.enabledFeatures, "passing event " + event.getClass().getName(), f -> f.onEvent(event));

        if (event instanceof ChatEvent<Player> tEvent){
            forEachRun(this.enabledFeatures, "passing ChatEvent", f -> f.onChatEvent(tEvent));
        } else if (event instanceof NekoTransformEvent<Player> tEvent){
            forEachRun(this.enabledFeatures, "passing NekoTransformEvent", f -> f.onNekoTransformEvent(tEvent));
        }
    }

    public <T> void forEachRun(Iterable<T> it, String message, Consumer<T> a){
        checkIsRunning();

        for (T value : it){
            try {
                a.accept(value);
            } catch (Throwable ex){
                this.getLogger().log(java.util.logging.Level.SEVERE, "error while " + message, ex);
            }
        }
    }
}
