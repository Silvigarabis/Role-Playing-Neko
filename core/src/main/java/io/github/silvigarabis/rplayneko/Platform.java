package io.github.silvigarabis.rplayneko;

import io.github.silvigarabis.rplayneko.storage.IDataTarget;
import io.github.silvigarabis.rplayneko.command.Command;
import io.github.silvigarabis.rplayneko.data.*;
import io.github.silvigarabis.rplayneko.power.RPlayNekoPowerFactory;
import java.util.logging.Logger;
import java.util.*;
import org.jetbrains.annotations.*;

/**
 * Platform 代表了无论在任何平台上运行都需要的东西。
 * 
 * # 创建实例
 * - 一个PlatformInstance应该先被创建
 * - 然后应该由PlatformInstance来初始化Platform，当然也可以直接让PlatformInstance实现Platform
 * - 接着，PlatformInstance读取配置文件，创建Core
 * - new Core()会调用Core#reloadConfig()来加载配置
 * - PlatformInstance调用Core#reloadData()来加载数据
 * - 加载完成，可以开始使用
 * 
 * # 重载实例
 * - PlatformInstance运行自定义逻辑
 * - PlatformInstance调用Core#reload()
 * - 重载完成
 *
 * # 重载实例数据
 * - PlatformInstance运行自定义逻辑
 * - PlatformInstance调用Core#reloadData()
 * - 重载完成
 *
 * # 重载实例配置
 * - PlatformInstance运行自定义逻辑
 * - PlatformInstance调用Core#reloadConfig()
 * - 重载完成
 *
 * # 关闭实例
 * - PlatformInstance运行自定义逻辑
 * - PlatformInstance调用Core#stop()
 * - 实例关闭
 *
 * # 结束实例
 * - PlatformInstance运行自定义逻辑
 * - PlatformInstance调用Core#saveData()
 * - PlatformInstance调用Core#saveConfig()
 * - PlatformInstance调用Core#stop()
 * - 实例关闭
 */
public interface Platform<Sender, Player> {
    // 这些方法随时可能调用

    Logger getLogger();

    PlatformType getPlatformType();

    default String getPlatformName(){
        return getPlatformInstance().getClass().toString();
    }

    default Object getPlatformInstance(){
        return this;
    }

    void saveCoreConfig(RPlayNekoConfig coreConfig);

    // 这些方法可能在重载时调用，得到的结果会被缓存或者持续使用，直到下一次重载

    IDataTarget getDataTarget();

    Map<String, String> getMessageConfig();

    RPlayNekoConfig getCoreConfig();

    // 这些方法在初始化时调用，可能会调用多次

    boolean registerCommand(Command<Sender, Player> command);

    // 这些方法只在数据准备完毕后调用，但是不限时机

    void sendMessage(Sender sender, String message);

    void sendPlayerMessage(Player player, String message);

    Player getPlayerByUuid(UUID uuid);

    boolean checkPermission(Sender sender, String permission);

    RPlayNekoPowerFactory<Player> getPowerFactory();

    @NotNull RPlayNekoPlayer<Player> newRPlayNekoPlayer(UUID uuid, RPlayNekoCore<Sender, Player> core, Player player);
}
