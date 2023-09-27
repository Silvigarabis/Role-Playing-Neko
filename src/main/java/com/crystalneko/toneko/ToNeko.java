package com.crystalneko.toneko;

import com.crystalneko.toneko.command.ToNekoCommand;
import com.crystalneko.toneko.event.PlayerDeath;
import com.crystalneko.toneko.files.create;
import com.crystalneko.toneko.chat.nekoed;
import com.crystalneko.toneko.items.getStick;
import org.bukkit.plugin.java.JavaPlugin;


public final class ToNeko extends JavaPlugin {
    private create createFile;
    private nekoed catedChat;
    private getStick getstick;
    private PlayerDeath playerDeath;

    @Override
    public void onEnable() {
        int pluginId = 19899; // <-- Replace with the id of your plugin!
        Metrics metrics = new Metrics(this, pluginId);
        //初始化createFile
        createFile = new create();
        /*/ 判断插件是否启用
        if(ctLib == null) {

            //下载ctLib前置插件
            try {
                download.downloadFile("https://w.csk.asia/res/ctLib.jar","plugins/ctLib.jar");
            } catch (IOException e) {
                System.out.println(e);
            }

            // 获取插件管理器
            PluginManager pluginManager = Bukkit.getPluginManager();
            // 加载插件
            try {
                Plugin ctLib = pluginManager.loadPlugin(new File("plugins/ctLib.jar"));
                // 启用插件
                pluginManager.enablePlugin(ctLib);
            } catch (InvalidPluginException e) {
                System.out.println(e);
            } catch (InvalidDescriptionException e) {
                System.out.println(e);
            }
        }*/


        //创建数据保存文件
        createFile.createNewFile("plugins/toNeko/nekos.yml");
        //初始化聊天监听器
        this.catedChat = new nekoed(this);
        //初始化厥猫棍获取器
        this.getstick = new getStick(this);
        //初始化死亡监听器
        this.playerDeath = new PlayerDeath(this);
        // 注册命令执行器
        getCommand("toneko").setExecutor(new ToNekoCommand(this,createFile,getstick));

    }




    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
