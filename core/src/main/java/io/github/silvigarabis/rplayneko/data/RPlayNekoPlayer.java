package io.github.silvigarabis.rplayneko.data;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import org.jetbrains.annotations.*;

import io.github.silvigarabis.rplayneko.power.*;
import io.github.silvigarabis.rplayneko.Messages;
import io.github.silvigarabis.rplayneko.RPlayNekoCore;

public abstract class RPlayNekoPlayer<T> {
    private final UUID uuid;
    public UUID getUUID(){
        return uuid;
    }

    public final T origin;
    public T getOrigin(){
        return origin;
    }

    private RPlayNekoCore<?, T> core;

    private final @NotNull RPlayNekoDataSource dataSource;
    public RPlayNekoDataSource getDataSource(){
        return dataSource;
    }

    public RPlayNekoPlayer(@NotNull UUID uuid, @NotNull RPlayNekoCore<?, T> core, @NotNull T origin){
        this.uuid = uuid;
        this.core = core;
        this.origin = origin;
        this.dataSource = core.getDataSource();
    }

    public boolean isNeko(){
        return dataSource.isNeko(uuid);
    }
    public boolean nekoAddWithOwner(@NotNull UUID owner){
        return dataSource.addNeko(uuid, owner);
    }
    public boolean nekoAdd(){
        return dataSource.addNeko(uuid, null);
    }
    public boolean nekoRemove(){
        return dataSource.removeNeko(uuid);
    }

    public void setSelfTransform(boolean mode){
        dataSource.setSelfTransform(uuid, mode);
    }
    public boolean isSelfTransform(){
        return dataSource.isSelfTransform(uuid);
    }

    public void addOwner(@NotNull UUID ownerUUID){
        if (getOwner() == null){
            setOwner(ownerUUID);
        }
        if (core.getConfig().feature_Neko_MultiOwner){
            dataSource.getMultiOwners(uuid).add(ownerUUID);
            dataSource.markDirty(uuid);
        }
    }
    public void setOwner(@Nullable UUID ownerUUID){
        if (!core.getConfig().feature_Neko_MultiOwner){
            dataSource.getMultiOwners(uuid).clear();
            dataSource.markDirty(uuid);
        }
        dataSource.getMultiOwners(uuid).add(ownerUUID);
        dataSource.setOwner(uuid, ownerUUID);
    }
    public @Nullable UUID getOwner(){
        return dataSource.getOwner(uuid);
    }
    public boolean isOwner(@NotNull UUID personUUID){
        if (core.getConfig().feature_Neko_MultiOwner){
            return dataSource.getMultiOwners(uuid).contains(personUUID);
        }
        return personUUID != null && personUUID.equals(dataSource.getOwner(uuid));
    }

    public void setNyaText(@Nullable String text){
        dataSource.setNya(uuid, text);
    }
    public @Nullable String getNyaText(){
        return dataSource.getNya(uuid);
    }
    public boolean hasNyaText(){
        return getNyaText() != null;
    }

    public void setMuted(boolean enabled){
        dataSource.setMuted(uuid, enabled);
    }
    public boolean isMuted(){
        return dataSource.isMuted(uuid);
    }

    public boolean addMasterAlias(@NotNull String masterAlias){
        return addSpeakReplace(masterAlias, core.getMessages().getMessage(this.origin, Messages.MessageKey.NEKO_CALL_MASTER_NAME));
    }
    public boolean addSpeakReplace(@NotNull String pattern, @NotNull String replacement){
        return dataSource.addSpeakReplace(uuid, pattern, replacement);
    }
    public boolean removeSpeakReplace(@NotNull String pattern){
        return dataSource.removeSpeakReplace(uuid, pattern);
    }
    /**
     * 对该Map的修改后，应该使用 {RPlayerNekoDataSource#markDirty} 标记
     */
    public @NotNull @Unmodifiable Map<String, String> getSpeakReplaces(){
        return Collections.unmodifiableMap(dataSource.getSpeakReplaces(uuid));
    }

    private final Set<RPlayNekoPower<T>> EnabledPowers = ConcurrentHashMap.newKeySet();
    private void saveEnabledPowers(){
        Set<String> powerData = dataSource.getEnabledPowers(uuid);
        powerData.clear();
        for (var power : EnabledPowers){
            powerData.add(power.type.name);
        }
        dataSource.markDirty(uuid);
    }
    private void loadEnabledPowers(){
        dataSource.getEnabledPowers(uuid).stream().forEach(powerName -> {
            EnabledPowers.add(core.newPowerInstance(RPlayNekoPowerType.Powers.get(powerName), this));
        });
    }

    public boolean addPower(@NotNull RPlayNekoPowerType powerType){
        boolean result = EnabledPowers.add(core.newPowerInstance(powerType, this));
        dataSource.getEnabledPowers(uuid).add(powerType.name);
        dataSource.markDirty(uuid);
        return result;
    }
    public boolean removePower(@NotNull RPlayNekoPowerType powerType){
        boolean result = EnabledPowers.removeIf(power -> power.type == powerType);
        dataSource.getEnabledPowers(uuid).remove(powerType.name);
        dataSource.markDirty(uuid);
        return result;
    }
    public @NotNull Set<RPlayNekoPowerType> getEnabledPowers(){
        Set<RPlayNekoPowerType> result = new HashSet<>();
        for (var p : EnabledPowers){
            result.add(p.type);
        }
        return result;
    }

    public void tick(){
        this.tickPlayer();
        this.EnabledPowers.stream().forEach(power -> {
            power.tick();
        });
    }
    public abstract void tickPlayer();

    @Override
    public boolean equals(Object o){
        if (o == this){
            return true;
        }
        if (!(o instanceof RPlayNekoPlayer)){
            return false;
        }
        RPlayNekoPlayer oplayer = (RPlayNekoPlayer)o;
        return this.uuid.equals(oplayer.uuid) && this.dataSource.equals(oplayer.dataSource);
    }
    @Override
    public int hashCode(){
        final int prime = 31;
        int result = 1;

        result = prime * result + this.uuid.hashCode();
        result = prime * result + this.dataSource.hashCode();

        return result;
    }
}
