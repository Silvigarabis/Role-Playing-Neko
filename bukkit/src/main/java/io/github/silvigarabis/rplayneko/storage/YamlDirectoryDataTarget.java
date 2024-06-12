/*
package io.github.silvigarabis.rplayneko.storage;

import java.util.UUID;
import java.util.HashMap;
import java.util.WeakHashMap;
import java.util.Map;
import java.util.Collections;
import java.util.concurrent.ConcurrentHashMap;
import org.jetbrains.annotations.*;
import java.util.List;
import java.io.File;
import org.yaml.snakeyaml.Yaml;

import io.github.silvigarabis.rplayneko.data.RPlayNekoData;

public class YamlDirectoryDataTarget implements IDataTarget {
    private File dataDir;
    private Yaml Yaml = new Yaml();
    public YamlDirectoryDataTarget(File dataDir){
        if (!dataDir.isDirectory()){
            throw new IllegalArgumentException("Not a directory: " + dataDir.toString());
        }
        this.dataDir = dataDir;
    }
    public RPlayNekoData loadFromDisk(UUID uuid, RPlayNekoData data){
        var dataFile = new File(dataDir, uuid.toString() + ".yml");
        if (dataFile.exists()){
            Yaml.load(new FileInputStream(dataFile));
        }
        return data;
    }
    public boolean saveToDisk(UUID uuid, RPlayNekoData data){
    }
    public boolean deleteInDisk(UUID uuid){
    }
}
*/