package io.github.silvigarabis.rplayneko;

import io.github.silvigarabis.rplayneko.data.IDataTarget;
import io.github.silvigarabis.rplayneko.feature.IFeature;
import io.github.silvigarabis.rplayneko.spigot.data.YamlDirectoryDataTarget;
import io.github.silvigarabis.rplayneko.power.RPlayNekoPower;
import io.github.silvigarabis.rplayneko.power.RPlayNekoPowerFactory;
import io.github.silvigarabis.rplayneko.power.RPlayNekoPowerType;

import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.*;
import java.util.logging.Logger;
import java.io.File;

public class RPlayNekoSpigot extends JavaPlugin implements Platform<CommandSender, Player> {
    private RPlayNekoCore<CommandSender, Player> core;

    @Override
    public void onLoad(){
        core = new RPlayNekoCore<>(this);
        dataTarget = new YamlDirectoryDataTarget(new File(getDataFolder(), "playerdata"));
    }

    @Override
    public void onEnable(){
        core.reload();
        getServer().getScheduler().runTaskTimer(this, core::tick, 20, 20);
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
        throw new RuntimeException("not implemented");
    }

    @Override
    public RPlayNekoConfig getCoreConfig(){
        throw new RuntimeException("not implemented");
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
    public RPlayNekoPowerFactory<Player> getPowerFactory(RPlayNekoPowerType type){
        throw new IllegalArgumentException("the specified power not implemented");
    }

    @Override
    public IFeature<Player> getFeature(String featureType){
        throw new RuntimeException("not implemented");
    }
}
