package io.github.silvigarabis.rplayneko;

import io.github.silvigarabis.rplayneko.storage.IDataTarget;
import io.github.silvigarabis.rplayneko.command.Command;
import io.github.silvigarabis.rplayneko.data.*;
import io.github.silvigarabis.rplayneko.power.RPlayNekoPowerFactory;
import java.util.logging.Logger;
import java.util.UUID;
import org.jetbrains.annotations.*;

import org.cneko.toneko.common.util.scheduled.ISchedulerPool;

public interface Platform<Sender, Player> {
    PlatformType getPlatformType();

    default String getPlatformName(){
        return getPlatformInstance().getClass().toString();
    }

    default Object getPlatformInstance(){
        return this;
    }

    boolean registerCommand(Command command);

    Logger getLogger();

    void sendMessage(Sender sender, String message);

    void sendPlayerMessage(Player player, String message);

    Player getPlayerByUuid(UUID uuid);

    boolean checkPermission(Sender sender, String permission);

    RPlayNekoConfig getCoreConfig();

    void saveCoreConfig(RPlayNekoConfig coreConfig);

    RPlayNekoPowerFactory<Player> getPowerFactory();

    @NotNull RPlayNekoPlayer<Player> newRPlayNekoPlayer(UUID uuid, RPlayNekoCore<Sender, Player> core, Player player);
}
