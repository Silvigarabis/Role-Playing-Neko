package io.github.silvigarabis.rplayneko.data;

import java.util.*;
import java.util.function.Predicate;
import java.util.concurrent.ConcurrentHashMap;
import org.jetbrains.annotations.*;

public class RPlayNekoDataSource {

    private final Map<UUID, RPlayNekoData> dataMap = new ConcurrentHashMap<>();
    private IDataTarget dataTarget;

    private boolean isClosed = false;
    public boolean isClosed(){
        return isClosed;
    }
    public void checkIsClosed(){
        if (this.isClosed){
            throw new IllegalStateException("dataSource already closed!");
        }
    }
    /**
     * 关闭此数据源，不会写出数据到dataTarget
     */
    public boolean close(){
        if (!this.isClosed){
            this.dataMap.clear();
            this.isClosed = true;
            this.dataTarget.close();
            this.dataTarget = null;
            return true;
        } else {
            return false;
        }
    }

    public RPlayNekoDataSource(IDataTarget dataTarget){
        this.dataTarget = dataTarget;
    }

    public void markDirty(UUID uuid){
        getData(uuid)._dirty(true);
    }

    public @NotNull RPlayNekoData getData(@NotNull UUID uuid){
        checkIsClosed();
        var data = dataMap.get(uuid);
        if (data == null){
            throw new IllegalArgumentException("data of " + uuid.toString() + "have not been load");
        }
        return data;
    }

    public boolean hasData(@NotNull UUID uuid){
        checkIsClosed();
        return dataMap.containsKey(uuid);
    }

    public RPlayNekoData createData(UUID uuid){
        checkIsClosed();
        if (hasData(uuid)){
            throw new IllegalStateException("data of " + uuid.toString() + "already loaded");
        }
        var data = new RPlayNekoData(uuid);
        dataMap.put(uuid, data);
        return data;
    }

    public boolean removeData(@NotNull UUID uuid){
        checkIsClosed();
        return null != dataMap.remove(uuid);
    }

    public void removeData(@NotNull UUID uuid, boolean inDisk){
        checkIsClosed();
        dataMap.remove(uuid);
        if (inDisk){
            dataTarget.deleteInDisk(uuid);
        }
    }

    public boolean saveData(UUID uuid){
        checkIsClosed();
        var data = getData(uuid);
        return dataTarget.saveToDisk(uuid, data);
    }

    public boolean loadData(UUID uuid){
        checkIsClosed();
        var data = createData(uuid);
        boolean result = dataTarget.loadFromDisk(uuid, data);
        if (!result){
            data.markDelete();
            dataMap.remove(uuid);
        }
        return result;
    }

    public boolean unloadData(UUID uuid){
        checkIsClosed();
        boolean result;
        var data = getData(uuid);
        if (data._delete()){
            result = dataTarget.deleteInDisk(uuid);
        } else {
            result = dataTarget.saveToDisk(uuid, data);
        }
        if (result){
            dataMap.remove(data);
        }
        return result;
    }

    /**
     * 尝试从内存加载数据，若不存在则从dataTarget加载，仍然不存在时创建新的数据。
     */
    public RPlayNekoData fetchData(UUID uuid){
        checkIsClosed();
        if (hasData(uuid)){
            return getData(uuid);
        }
        if (loadData(uuid)){
            return getData(uuid);
        }
        return createData(uuid);
    }

    /**
     * 保存所有数据到dataTarget，并清空数据源
     */
    public void unloadAllData(){
        checkIsClosed();
        for (UUID uuid : dataMap.keySet()){
            unloadData(uuid);
        }
    }

    /**
     * 保存所有脏数据到dataTarget
     */
    public void saveDirtyData(){
        checkIsClosed();
        for (RPlayNekoData data : dataMap.values()){
            if (data._dirty()){
                dataTarget.saveToDisk(data.getUuid(), data);
            }
        }
    }

    public void removeAllData(){
        checkIsClosed();
        dataMap.clear();
    }

}
