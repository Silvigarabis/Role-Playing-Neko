package io.github.silvigarabis.rplayneko;

import io.github.silvigarabis.rplayneko.storage.IDataTarget;
import java.util.logging.Logger;
import java.util.UUID;

import org.cneko.toneko.common.util.scheduled.ISchedulerPool;

public interface Platform<Instance, Sender, Player> {
    Logger getLogger();
    void sendMessage(Sender sender, String message);
    boolean registerCommand(io.github.silvigarabis.rplayneko.command.Command command);
    Instance getPlatformInstance();
    Player getPlayerById(UUID uuid);
    boolean checkPermission(Sender sender, String permission);
    RPlayNekoConfig getCoreConfig();
    void saveCoreConfig(RPlayNekoConfig coreConfig);
    IDataTarget getCoreDataTarget();
    ISchedulerPool getSchedulePool();
}
