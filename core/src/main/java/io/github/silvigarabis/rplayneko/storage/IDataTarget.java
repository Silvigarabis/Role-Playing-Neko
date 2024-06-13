package io.github.silvigarabis.rplayneko.storage;

import java.util.UUID;
import org.jetbrains.annotations.*;

import io.github.silvigarabis.rplayneko.data.RPlayNekoData;

public interface IDataTarget {
    boolean loadFromDisk(UUID uuid, RPlayNekoData data);
    boolean saveToDisk(UUID uuid, RPlayNekoData data);
    boolean deleteInDisk(UUID uuid);
    /**
     * 在关闭时调用
     */
    default boolean close(){
        return false;
    }
}
