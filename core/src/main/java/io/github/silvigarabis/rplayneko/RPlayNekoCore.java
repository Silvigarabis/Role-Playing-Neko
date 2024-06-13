package io.github.silvigarabis.rplayneko;

import io.github.silvigarabis.rplayneko.event.*;
import io.github.silvigarabis.rplayneko.data.*;
import io.github.silvigarabis.rplayneko.power.*;
import io.github.silvigarabis.rplayneko.command.*;
import io.github.silvigarabis.rplayneko.storage.IDataTarget;

import com.crystalneko.tonekocommon.util.StringUtil;

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

    public RPlayNekoCore(Platform platform){
        this.platform = platform;
        this.messages = new Messages(platform);
        this.reloadConfig();
        INSTANCE = this;
    }

    private Platform<Sender, Player> platform;
    public Platform getPlayform(){
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
        this.dataSource = null:
        this.reloadConfig();
        this.initDataSource();
    }

    public void stop(){
        this.dataSource.unloadAllData();
        this.dataSource.close();
        this.dataSource = null;
    }

    public RPlayNekoPower<Player> newPowerInstance(RPlayNekoPowerType type, RPlayNekoPlayer<Player> player){
        return platform.getPowerFactory().newPowerInstance(type, player);
    }

    private void registerCommands(){
        this.platform.registerCommand(new CommandRPlayNeko(this));
    }

    public boolean checkPermission(Sender sender, String permission){
        return this.platform.checkPermission(sender, "rplayneko." + permission);
    }

    public ChatEvent<Player> onChatEvent(ChatEvent<Player> event){
        // replace
        if (getConfig().feature_SpeakReplaceAlways || (getConfig().feature_SpeakReplace && event.player.isNeko())){

            boolean isEnabledRegex = getConfig().feature_SpeakReplace_Regex;
            for (Map.Entry<String, String> entry : event.player.getSpeakReplaces().entrySet()){
                String pattern = entry.getKey();
                String replacement = entry.getValue();

                boolean isRegex = false;
                if (isEnabledRegex && pattern.length() > 2 && pattern.startsWith("/") && pattern.endsWith("/")){
                    isRegex = true;
                }

                if (isRegex){
                    pattern = pattern.substring(1, pattern.length() - 1);
                    event.message = StringUtil.timeLimitedReplaceAll(event.message, pattern, replacement);
                } else {
                    event.message = event.message.replace(pattern, replacement);
                }
            }

            event.result = EventResult.MODIFIED;
        }

        // append nya
        if (getConfig().feature_ChatNya && event.player.isNeko()){
            switch (getConfig().feature_ChatNya_Location){
                case END:
                    event.chatSuffix = event.player.getNyaText();
                    break;
                case HEAD:
                    event.chatPrefix = event.player.getNyaText() + " ";
                    break;
                case CUSTOM:
                default:
                    break;
            }
            event.result = EventResult.MODIFIED;
        }

        // mute
        if (event.player.isNeko() && event.player.isMuted()){
            event.result = EventResult.CANCELLED;
            getMessages().sendPlayer(event.player.getOrigin(), Messages.MessageKey.NEKO_CHAT_MUTED);
        }

        return event;
    }
}
