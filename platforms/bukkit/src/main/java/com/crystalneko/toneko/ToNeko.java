package com.crystalneko.toneko;

import com.crystalneko.toneko.bstats.Metrics;
import com.crystalneko.toneko.chat.NekoChatListener;
import com.crystalneko.toneko.command.AINekoCommand;
import com.crystalneko.toneko.command.NekoCommand;
import com.crystalneko.toneko.command.TabCompleter;
import com.crystalneko.toneko.command.ToNekoCommand;
import com.crystalneko.toneko.event.PlayerEventListenerBase;
import com.crystalneko.toneko.items.StickItemRecipeRegistry;
import com.crystalneko.toneko.utils.ConfigFileUtils;
import com.crystalneko.toneko.utils.LibraryDownloader;
import org.bukkit.Bukkit;
import org.bukkit.configuration.MemoryConfiguration;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.InvalidDescriptionException;
import org.bukkit.plugin.InvalidPluginException;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.logging.Level;
import java.util.logging.Logger;

import java.nio.file.Paths;

import static org.cneko.ctlib.common.util.LocalDataBase.Connections.sqlite;

//温馨提示：代码中所有的判断是否为猫娘都是判断是否有主人，这意味着猫娘必须有主人，否则就不被判断为猫娘

public class ToNeko extends JavaPlugin {

    private LibraryDownloader libraryDownloader;
    private YamlConfiguration languageConfig;
    private YamlConfiguration config;
    private static ToNeko INSTANCE = null;
    public static ToNeko getInstance(){
        if (INSTANCE == null){
            throw new IllegalStateException("Neko Plugin not initiated");
        }
        return INSTANCE;
    }

    @Override
    public void onEnable() {
        INSTANCE = this;

        if (!getDataFolder().exists()){
            getDataFolder().mkdir();
        }

        new Metrics(this, 19899);

        reloadConfig();
        config = getConfig();

        ConfigFileUtils.createNewFile(new File(getDataFolder(), "nekos.yml").getAbsolutePath());
        //创建数据库
        initDataBase();

        //注册物品
        StickItemRecipeRegistry.registerRecipe();

        //注册命令执行器
        TabCompleter tabCompleter = new TabCompleter();
        getCommand("toneko").setExecutor(new ToNekoCommand());
        getCommand("toneko").setTabCompleter(tabCompleter);
        getCommand("neko").setExecutor(new NekoCommand());
        getCommand("neko").setTabCompleter(tabCompleter);
        getCommand("aineko").setExecutor(new AINekoCommand());
        getCommand("aineko").setTabCompleter(tabCompleter);

        registerEvents();
    }

    private void registerEvents(){
        //注册玩家事件监听器
        Bukkit.getPluginManager().registerEvents(new PlayerEventListenerBase(), this);
        //初始化聊天监听器

        var ChatListener = new NekoChatListener(this);
        //注册玩家聊天监听器
        try {
            //使用Paper的聊天监听器（如果存在）
            Class.forName("io.papermc.paper.event.player.AsyncChatEvent");
            plugin.getLogger().info(ToNeko.getMessage("folia.use.chatEvent"));

            getPluginManager()
            .registerEvent(
                io.papermc.paper.event.player.AsyncChatEvent.class,
                ChatListener,
                EventPriority.NORMAL,
                (listener, event) -> ChatListener.onPlayerChatPaper((io.papermc.paper.event.player.AsyncChatEvent) event),
                this
            );
        } catch (ClassNotFoundException ignored) {
            getPluginManager()
            .registerEvent(
                AsyncPlayerChatEvent.class,
                ChatListener,
                EventPriority.NORMAL,
                (listener, event) -> ChatListener.onPlayerChat((AsyncPlayerChatEvent) event),
                this
            );
        }

    }

    @Override
    public void saveDefaultConfig(){
        super.saveDefaultConfig();
        saveResource("assets/rplayneko/lang/en_us.json", false);
        saveResource("assets/rplayneko/lang/zh_cn.json", false);
    }

    @Override
    public void reloadConfig(){
        saveDefaultConfig();
        updateConfig();
        super.reloadConfig();
        loadLanguageFile();
    }

    //配置文件更新
    private void updateConfig() {
    }

    private void initDataBase(){
        if (sqlite.isTableExists("nekoblockword")){
            return;
        }

        sqlite.createTable("nekoblockword");
        sqlite.addColumn("nekoblockword","block");
        sqlite.addColumn("nekoblockword","replace");
        sqlite.addColumn("nekoblockword","method");
        sqlite.addColumn("nekoblockword","neko");
    }

    @Override
    public void onDisable() {
        if (INSTANCE == this){
            INSTANCE = null;
        }
    }

    private void loadLanguageFile() {
        // 获取配置文件中的语言选项
        String language = getConfig().getString("language");
        try {
            loadLanguageFile(language);
        } catch (IllegalArgumentException ignored){
            try {
                loadLanguageFile("zh_cn");
            } catch (IllegalArgumentException ex){
                languageConfig = new MemoryConfiguration();
                getLogger().severe("无法加载语言文件" + ex.getMessage());
            }
        }
    }

    private void loadLanguageFile(@NotNull String langName) {
        File languageFile = Paths.get(getDataFolder().getPath(), "assets/rnekoplay/lang", langName + ".json").toFile();
        if (languageFile.exists()){
            // json可以安全地被yaml加载
            languageConfig = YamlConfiguration.loadConfiguration(languageFile);
            return;
        }
        throw new IllegalArgumentException("no such language: " + langName);
    }

    // 获取翻译内容的方法
    public static String getMessage(@NotNull String key) {
        return getInstance().languageConfig.getString(key, key);
    }

    public static String getMessage(@NotNull String key, String... replacements) {
        String text = getMessage(key);
        String oldText = text;
        for (String replacement: replacements){
            text = text.replace("%s", replacement);
            if (oldText.equals(text)){
                break;
            }
        }
        return text;
    }
}
