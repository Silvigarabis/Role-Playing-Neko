package io.github.silvigarabis.rplayneko;

import org.bukkit.plugin.JavaPlugin;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public abstract class RPlayNekoBukkit extends JavaPlugin implements Platform<Player, CommandSender> {
    
    private static RPlayNekoBukkit INSTANCE = null;
    public static RPlayNekoBukkit getInstance(){
        if (INSTANCE == null){
            throw new IllegalStateException("Plugin not initiated");
        }
        return INSTANCE;
    }

    private RPlayNekoCore<Player, CommandSender> core;

    public onEnable(){
        INSTANCE = this;
        
    public RPlayNekoCore(Platform platform, Class<Player> PlayerClass, IDataTarget dataTarget){
        this.platform = platform;
        this.PlayerClass = PlayerClass;
        this.messages = new Messages(platform);
        this.config = platform.getCoreConfig();
        this.dataSource = new RPlayNekoDataSource(dataTarget);
        INSTANCE = this;
    }
        core = new RPlayNekoCore<>(this, Player.class, this);
    }
    public onDisable(){
        if (INSTANCE == this){
            INSTANCE = null;
        }
    }
/*
    private LibraryDownloader libraryDownloader;
    private YamlConfiguration languageConfig;
    private YamlConfiguration config;

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
    }*/
}
