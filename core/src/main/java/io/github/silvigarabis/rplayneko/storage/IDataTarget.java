package io.github.silvigarabis.rplayneko.storage;

import java.util.UUID;
import java.util.HashMap;
import java.util.WeakHashMap;
import java.util.Map;
import java.util.Collections;
import java.util.concurrent.ConcurrentHashMap;
import org.jetbrains.annotations.*;
import java.util.List;

import io.github.silvigarabis.rplayneko.data.RPlayNekoData;

public interface IDataTarget {
    boolean loadFromDisk(UUID uuid, RPlayNekoData data);
    boolean saveToDisk(UUID uuid, RPlayNekoData data);
    boolean deleteInDisk(UUID uuid);
}
