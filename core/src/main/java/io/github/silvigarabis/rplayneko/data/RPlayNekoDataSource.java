package io.github.silvigarabis.rplayneko.data;

import java.util.UUID;
import java.util.HashMap;
import java.util.WeakHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Collections;
import java.util.concurrent.ConcurrentHashMap;
import org.jetbrains.annotations.*;

import io.github.silvigarabis.rplayneko.storage.IDataTarget;

public class RPlayNekoDataSource<T> {

    private final Map<UUID, RPlayNekoData> dataMap = new ConcurrentHashMap<>();
    private final Map<UUID, RPlayNekoData> dataMapUnmodifiableCopy = Collections.unmodifiableMap(dataMap);
    private final IDataTarget dataTarget;

    public @UnmodifiableView @NotNull Map<UUID, RPlayNekoData> getDataMap(){
        return dataMapUnmodifiableCopy;
    }

    public RPlayNekoDataSource(IDataTarget dataTarget){
        this.dataTarget = dataTarget;
    }

    public boolean addNeko(@NotNull UUID uuid, @Nullable UUID owner){
        if (this.isNeko(uuid)){
            return false;
        }
        setNeko(uuid, true);
        setOwner(uuid, owner);
        return true;
    }
    public boolean removeNeko(@NotNull UUID uuid){
        if (!this.isNeko(uuid)){
            return false;
        }
        setNeko(uuid, false);

        // clean data
        setOwner(uuid, null);
        setMuted(uuid, false);
        setNya(uuid, null);
        cleanSpeakReplace(uuid);
        return true;
    }

    public void setNeko(@NotNull UUID uuid, boolean enabled){
        getData(uuid).setNeko(enabled);
    }
    public boolean isNeko(@NotNull UUID uuid){
        return getData(uuid).isNeko();
    }

    public void setSelfTransform(@NotNull UUID uuid, boolean mode){
        getData(uuid).setSelfTransform(mode);
    }
    public boolean isSelfTransform(@NotNull UUID uuid){
        return getData(uuid).isSelfTransform();
    }

    public void setOwner(@NotNull UUID uuid, @Nullable UUID ownerUUID){
        getData(uuid).setOwnerUUID(ownerUUID);
    }
    public @Nullable UUID getOwner(@NotNull UUID uuid){
        return getData(uuid).getOwnerUUID();
    }
    public Set<UUID> getMultiOwners(@NotNull UUID uuid){
        return getData(uuid).getMultiOwners();
    }

    public void setMuted(@NotNull UUID uuid, boolean muted){
        getData(uuid).setMuted(muted);
    }
    public boolean isMuted(@NotNull UUID uuid){
        return getData(uuid).isMuted();
    }

    public void setNya(@NotNull UUID uuid, @Nullable String nyaText){
        getData(uuid).setNyaText(nyaText);
    }
    public @Nullable String getNya(@NotNull UUID uuid){
        return getData(uuid).getNyaText();
    }

    public boolean addSpeakReplace(@NotNull UUID uuid, @NotNull String pattern, @NotNull String replacement){
        Map<String, String> map = getSpeakReplaces(uuid);
        if (map.containsKey(pattern)){
            return false;
        }
        map.put(pattern, replacement);
        markDirty(uuid);
        return true;
    }
    public boolean removeSpeakReplace(@NotNull UUID uuid, @NotNull String pattern){
        boolean result = getSpeakReplaces(uuid).containsKey(pattern);
        if (result){
            getSpeakReplaces(uuid).remove(pattern);
            markDirty(uuid);
        }
        return result;
    }
    public void cleanSpeakReplace(@NotNull UUID uuid){
        getSpeakReplaces(uuid).clear();
    }
    public @NotNull Map<String, String> getSpeakReplaces(@NotNull UUID uuid){
        return getData(uuid).getSpeakReplaces();
    }

    public Set<String> getEnabledPowers(UUID uuid){
        return getData(uuid).getEnabledPowers();
    }

    public void markDirty(UUID uuid){
        getData(uuid)._dirty(true);
    }

    public RPlayNekoData createData(UUID uuid){
        if (dataMap.containsKey(uuid)){
            throw new IllegalStateException("data of " + uuid.toString() + "already loaded");
        }
        var data = new RPlayNekoData(uuid);
        dataMap.put(uuid, data);
        return data;
    }
    public boolean removeData(@NotNull UUID uuid, boolean inDisk){
        if (inDisk){
            dataTarget.deleteInDisk(uuid);
        }
        boolean result = dataMap.containsKey(uuid);
        if (result){
            dataMap.remove(uuid);
        }
        return result;
    }
    public boolean removeData(@NotNull UUID uuid){
        return removeData(uuid, false);
    }
    public @NotNull RPlayNekoData getData(@NotNull UUID uuid){
        if (!dataMap.containsKey(uuid)){
            throw new IllegalStateException("data of " + uuid.toString() + "have not been load");
        }
        return dataMap.get(uuid);
    }
    public boolean hasData(UUID uuid){
        return dataMap.containsKey(uuid);
    }
    public boolean saveData(UUID uuid){
        if (!hasData(uuid)){
            return false;
        }
        var data = getData(uuid);
        return dataTarget.saveToDisk(uuid, data);
    }
    public boolean unloadData(UUID uuid){
        if (!dataMap.containsKey(uuid)){
            return false;
        }
        var data = getData(uuid);
        return dataTarget.saveToDisk(uuid, data);
    }
    public boolean loadData(UUID uuid){
        if (dataMap.containsKey(uuid)){
            return false;
        }
        var data = createData(uuid);
        boolean result = dataTarget.loadFromDisk(uuid, data);
        if (!result){
            removeData(uuid);
        }
        return result;
    }
    public RPlayNekoData fetchData(UUID uuid){
        if (hasData(uuid)){
            return getData(uuid);
        }
        if (loadData(uuid)){
            return getData(uuid);
        }
        return createData(uuid);
    }
}