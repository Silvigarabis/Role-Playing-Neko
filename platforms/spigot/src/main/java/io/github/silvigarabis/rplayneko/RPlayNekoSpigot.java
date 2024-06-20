package io.github.silvigarabis.rplayneko;

import io.github.silvigarabis.rplayneko.data.IDataTarget;
import io.github.silvigarabis.rplayneko.data.YamlDirectoryDataTarget;
import io.github.silvigarabis.rplayneko.feature.IFeature;
import io.github.silvigarabis.rplayneko.power.*;

import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import org.bukkit.configuration.file.YamlConfiguration;
import java.io.*;
import java.nio.file.Paths;
import java.nio.file.Path;
import org.jetbrains.annotations.*;

import java.util.*;
import java.util.logging.Logger;

public class RPlayNekoSpigot extends JavaPlugin implements Platform<CommandSender, Player> {
    private RPlayNekoCore<CommandSender, Player> core;

    @Override
    public void onLoad(){
        core = new RPlayNekoCore<>(this);
        dataTarget = new YamlDirectoryDataTarget(new File(getDataFolder(), "playerdata"));
    }

    @Override
    public void onEnable(){
        reloadConfig();
        core.reload();
        getServer().getScheduler().runTaskTimer(this, core::tick, 20, 20);
    }

    private RPlayNekoConfig coreConfig = new RPlayNekoConfig();

    @Override
    public void reloadConfig(){
        saveDefaultConfig();
        super.reloadConfig();
        coreConfig.setLanguage(getConfig().getString("language", "zh_cn"));
        coreConfig.setEnabledFeatures(getConfig().getStringList("enabled-features"));
        coreConfig.setEnabledPowers(getConfig().getStringList("enabled-powers"));
    }

    private IDataTarget dataTarget;
    @Override
    public IDataTarget getDataTarget(){
        return dataTarget;
    }

    @Override
    public void onDisable(){
        core.shutdown();
    }

    @Override
    public Map<String, String> getMessageConfig(){
        Path messageFilePath = Paths.get("assets/rplayneko/lang", coreConfig.getLanguage() + ".yml");
        // I like this
        Path defaultMessageFilePath = Paths.get("assets/rplayneko/lang/zh_cn.yml");

        saveResource(messageFilePath.toString(), false);
        saveResource(defaultMessageFilePath.toString(), false);

        File messageFile = new File(getDataFolder().getPath(), messageFilePath.toString());
        File defaultMessageFile = new File(getDataFolder().getPath(), defaultMessageFilePath.toString());

        YamlConfiguration messageConfig = null;
        YamlConfiguration defaultMessageConfig = null;

        if (messageFile.exists()){
            messageConfig = YamlConfiguration.loadConfiguration(messageFile);
        }
        if (defaultMessageFile.exists() && !defaultMessageFile.equals(messageFile)){
            defaultMessageConfig = YamlConfiguration.loadConfiguration(defaultMessageFile);
        }

        Map<String, String> messages = new HashMap<>();

        for (String key : Messages.MessageKey.Keys.values()){
            String message = messageConfig.getString(key, null);
            if (message == null && defaultMessageConfig != null){
                message = defaultMessageConfig.getString(key, null);
            }
            
            if (message != null){
                messages.put(key, message);
            }
        }

        return messages;
    }

    @Override
    public RPlayNekoConfig getCoreConfig(){
        return coreConfig;
    }

    @Override
    public Logger getLogger(){
        return super.getLogger();
    }

    @Override
    public PlatformType getPlatformType(){
        return PlatformType.Spigot;
    }

    @Override
    public String getPlatformName(){
        return getServer().getName();
    }

    @Override
    public void sendMessage(CommandSender sender, String message){
        sender.sendMessage(message);
    }

    @Override
    public void sendMessageToPlayer(Player player, String message){
        player.sendMessage(message);
    }

    @Override
    public Player getPlayerByUuid(UUID uuid){
        return getServer().getPlayer(uuid);
    }

    @Override
    public UUID getPlayerUuid(Player origin){
        return origin.getUniqueId();
    }

    @Override
    public List<Player> getPlayers(){
        return new ArrayList<>(getServer().getOnlinePlayers());
    }

    @Override
    public boolean checkPermission(CommandSender sender, String permission){
        return sender.hasPermission(permission);
    }

    @Override
    public boolean checkPermissionForPlayer(Player player, String permission){
        return player.hasPermission(permission);
    }

    @Override
    public @Nullable RPlayNekoPowerFactory<Player> getPowerFactory(RPlayNekoPowerType type){
        switch (type){
            case NIGHT_VISION:
                return player -> new NightVisionPower(type, player);
            case JUMP_BOOST:
                return player -> new JumpBoostPower(type, player);
        }
        return null;
    }

    @Override
    public @Nullable IFeature<Player> getFeature(String featureType){
        return null;
    }
}
