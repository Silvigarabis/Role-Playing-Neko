package io.github.silvigarabis.rplayneko.spigot.data;

import io.github.silvigarabis.rplayneko.power.RPlayNekoPowerType;
import io.github.silvigarabis.rplayneko.data.*;
import org.bukkit.configuration.file.YamlConfiguration;
import java.io.*;
import java.util.UUID;
import org.jetbrains.annotations.*;

public class YamlDirectoryDataTarget implements IDataTarget {
    public boolean loadFromDisk(UUID uuid, RPlayNekoData data){
        File dataFile = getFileIfExists(uuid);
        if (dataFile == null){
            return false;
        }
        YamlConfiguration fileData = YamlConfiguration.loadConfiguration(dataFile);
        throw new RuntimeException("not implemented");
    }
    public boolean saveToDisk(UUID uuid, RPlayNekoData data){
        File dataFile = getFile(uuid);
        try {
            //throw new RuntimeException("not implemented");
            dataFile.createNewFile();
            YamlConfiguration fileData = YamlConfiguration.loadConfiguration(dataFile);
            fileData.save(dataFile);
        } catch (IOException ex){
            //TODO: log error there
            return false;
        }
        return true;
    }
    public boolean deleteInDisk(UUID uuid){
        return getFile(uuid).delete();
    }
    public @NotNull File getFile(UUID uuid){
        return new File(dataDir, uuid.toString() + ".yml");
    }
    public @Nullable File getFileIfExists(UUID uuid){
        File file = new File(dataDir, uuid.toString() + ".yml");
        if (file.exists()){
            return file;
        }
        return null;
    }
    public YamlDirectoryDataTarget(File dataDir){
        if (!dataDir.exists()){
            dataDir.mkdirs();
        }
        if (!dataDir.isDirectory()){
            throw new IllegalArgumentException("provided file is not a directory");
        }
        this.dataDir = dataDir;
    }
    private File dataDir;
}
