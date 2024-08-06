package io.github.silvigarabis.rplayneko.data;

import io.github.silvigarabis.rplayneko.power.RPlayNekoPowerType;
import java.io.*;
import java.util.UUID;
import org.jetbrains.annotations.*;
import org.yaml.snakeyaml.Yaml;

public class YamlDirectoryDataTarget implements IDataTarget {
    public boolean loadFromDisk(UUID uuid, RPlayNekoData data){
        File dataFile = getFileIfExists(uuid);
        if (dataFile == null){
            return false;
        }
        YamlConfiguration fileData = YamlConfiguration.loadConfiguration(dataFile);

        //TODO: 异常处理？那是什么？能吃吗？
        data.setCastor(UUID.fromString(fileData.getString("castor")));
        data.setNeko(fileData.getBoolean("is-neko", false));
        data.setMuted(fileData.getBoolean("is-muted", false));
        data.setNyaText(fileData.getString("nya-text", null));
        var relatedPlayersData = fileData.getConfigurationSection("related-players");
        for (String playerUuidString : relatedPlayersData.getKeys(false)){
            UUID relatedPlayerUuid = UUID.fromString(playerUuidString);
            var relatedData = relatedPlayersData.getConfigurationSection(playerUuidString);
            
            data.setExperience(relatedPlayerUuid, relatedData.getInt("xp", 0));
            if (relatedData.getBoolean("is-owner", false)){
                data.addOwner(relatedPlayerUuid);
            }
        }
        data.getEnabledPowers().addAll(fileData.getStringList("enabled-powers"));
        data.getMasterCalls().addAll(fileData.getStringList("master-calls"));
        for (Map<String, String> entryMap : speakReplaceData.getList("speak-replaces")){
            String pattern = entryMap.get("pattern");
            String replacement = entryMap.get("replacement");
            data.getSpeakReplaces().put(pattern, replacement);
        }
        for (Map<String, String> entryMap : speakReplaceData.getList("regex-speak-replaces")){
            String regex = entryMap.get("regex");
            String replacement = entryMap.get("replacement");
            data.getRegexpSpeakReplaces().put(regex, replacement);
        }
        return true;
    }
    public boolean saveToDisk(UUID uuid, RPlayNekoData data){
        File file = getFile(uuid);
        YamlConfiguration fileData;
        try {
            dataFile.createNewFile();
            fileData = YamlConfiguration.loadConfiguration(dataFile);
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
