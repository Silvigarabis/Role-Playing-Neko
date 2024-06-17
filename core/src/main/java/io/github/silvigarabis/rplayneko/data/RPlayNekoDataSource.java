package io.github.silvigarabis.rplayneko.data;

import java.util.*;
import java.util.function.Predicate;
import java.util.concurrent.ConcurrentHashMap;
import org.jetbrains.annotations.*;

import io.github.silvigarabis.rplayneko.storage.IDataTarget;

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

    public RPlayNekoData createData(UUID uuid){
        checkIsClosed();
        if (dataMap.containsKey(uuid)){
            throw new IllegalStateException("data of " + uuid.toString() + "already loaded");
        }
        var data = new RPlayNekoData(uuid);
        dataMap.put(uuid, data);
        return data;
    }

    public void removeAllData(){
        checkIsClosed();
        dataMap.clear();
    }

    public boolean removeData(@NotNull UUID uuid, boolean inDisk){
        checkIsClosed();
        if (inDisk){
            dataTarget.deleteInDisk(uuid);
        }
        boolean result = dataMap.containsKey(uuid);
        if (result){
            dataMap.remove(uuid);
        }
        return result;
    }

    public @NotNull RPlayNekoData getData(@NotNull UUID uuid){
        checkIsClosed();
        if (!dataMap.containsKey(uuid)){
            throw new IllegalStateException("data of " + uuid.toString() + "have not been load");
        }
        return dataMap.get(uuid);
    }

    public boolean saveData(UUID uuid){
        checkIsClosed();
        if (!hasData(uuid)){
            return false;
        }
        var data = getData(uuid);
        return dataTarget.saveToDisk(uuid, data);
    }
    public int saveDataIf(Predicate<RPlayNekoData> p){
        checkIsClosed();
        List<RPlayNekoData> listDataToSave = new LinkedList<>();

        for (var data : data.valueSet()){
            if (p.test(data)){
                listDataToSave.add(data);
            }
        }

        for (var data : listDataToSave){
            dataTarget.saveToDisk(data.getUuid(), data);
        }

        return listDataToUnload.size();
    }

    public boolean removeData(@NotNull UUID uuid){
        checkIsClosed();
        return removeData(uuid, false);
    }

    public boolean hasData(UUID uuid){
        checkIsClosed();
        return dataMap.containsKey(uuid);
    }

    public boolean unloadData(UUID uuid){
        checkIsClosed();
        if (!dataMap.containsKey(uuid)){
            return false;
        }
        var data = getData(uuid);
        return dataTarget.saveToDisk(uuid, data);
    }

    public int unloadDataIf(Predicate<RPlayNekoData> p){
        checkIsClosed();
        List<RPlayNekoData> listDataToUnload = new LinkedList<>();

        for (var data : data.valueSet()){
            if (p.test(data)){
                listDataToUnload.add(data);
            }
        }

        for (var data : listDataToUnload){
            dataTarget.saveToDisk(data.getUuid(), data);
            removeData(data.getUuid());
        }

        return listDataToUnload.size();
    }

    public boolean loadData(UUID uuid){
        checkIsClosed();
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

    /**
     * 尝试从内存加载，若不存在则从dataTarget加载，仍然不存在时创建新的数据。
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
    public boolean unloadAllData(){
        checkIsClosed();
        for (Map.Entry<UUID, RPlayNekoData> entry : dataMap.entrySet()){
            dataTarget.saveToDisk(entry.getKey(), entry.getValue());
            dataMap.remove(entry.getKey());
        }
        return true;
    }

    /**
     * 保存所有数据到dataTarget
     */
    public boolean saveAllData(){
        checkIsClosed();
        for (Map.Entry<UUID, RPlayNekoData> entry : dataMap.entrySet()){
            UUID uuid = entry.getKey();
            RPlayNeKoData data = entry.getValue();
            if (data._dirty()){
                dataTarget.saveToDisk(uuid, data);
            }
        }
        return true;
    }
}
