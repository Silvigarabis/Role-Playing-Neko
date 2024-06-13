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
        if (dataSource == null || dataSource.isClosed()){
            this.dataSource = core.getDataSource();
        }
        return dataSource;
    }

    public RPlayNekoPlayer(@NotNull UUID uuid, @NotNull RPlayNekoCore<?, T> core, @NotNull T origin){
        this.uuid = uuid;
        this.core = core;
        this.origin = origin;
    }

    public boolean isNeko(){
        return getDataSource().isNeko(uuid);
    }
    public boolean nekoAddWithOwner(@NotNull UUID owner){
        return getDataSource().addNeko(uuid, owner);
    }
    public boolean nekoAdd(){
        return getDataSource().addNeko(uuid, null);
    }
    public boolean nekoRemove(){
        return getDataSource().removeNeko(uuid);
    }

    public void setSelfTransform(boolean mode){
        getDataSource().setSelfTransform(uuid, mode);
    }
    public boolean isSelfTransform(){
        return getDataSource().isSelfTransform(uuid);
    }

    public void addOwner(@NotNull UUID ownerUUID){
        if (getOwner() == null){
            setOwner(ownerUUID);
        }
        if (core.getConfig().feature_Neko_MultiOwner){
            getDataSource().getMultiOwners(uuid).add(ownerUUID);
            getDataSource().markDirty(uuid);
        }
    }
    public void setOwner(@Nullable UUID ownerUUID){
        if (!core.getConfig().feature_Neko_MultiOwner){
            getDataSource().getMultiOwners(uuid).clear();
            getDataSource().markDirty(uuid);
        }
        getDataSource().getMultiOwners(uuid).add(ownerUUID);
        getDataSource().setOwner(uuid, ownerUUID);
    }
    public @Nullable UUID getOwner(){
        return getDataSource().getOwner(uuid);
    }
    public boolean isOwner(@NotNull UUID personUUID){
        if (core.getConfig().feature_Neko_MultiOwner){
            return getDataSource().getMultiOwners(uuid).contains(personUUID);
        }
        return personUUID != null && personUUID.equals(getDataSource().getOwner(uuid));
    }

    public void setNyaText(@Nullable String text){
        getDataSource().setNya(uuid, text);
    }
    public @Nullable String getNyaText(){
        return getDataSource().getNya(uuid);
    }
    public boolean hasNyaText(){
        return getNyaText() != null;
    }

    public void setMuted(boolean enabled){
        getDataSource().setMuted(uuid, enabled);
    }
    public boolean isMuted(){
        return getDataSource().isMuted(uuid);
    }

    public boolean addMasterAlias(@NotNull String masterAlias){
        return addSpeakReplace(masterAlias, core.getMessages().getMessage(this.origin, Messages.MessageKey.NEKO_CALL_MASTER_NAME));
    }
    public boolean addSpeakReplace(@NotNull String pattern, @NotNull String replacement){
        return getDataSource().addSpeakReplace(uuid, pattern, replacement);
    }
    public boolean removeSpeakReplace(@NotNull String pattern){
        return getDataSource().removeSpeakReplace(uuid, pattern);
    }
    /**
     * 对该Map的修改后，应该使用 {RPlayerNekoDataSource#markDirty} 标记
     */
    public @NotNull @Unmodifiable Map<String, String> getSpeakReplaces(){
        return Collections.unmodifiableMap(getDataSource().getSpeakReplaces(uuid));
    }

    private final Set<RPlayNekoPower<T>> EnabledPowers = ConcurrentHashMap.newKeySet();
    private void saveEnabledPowers(){
        Set<String> powerData = getDataSource().getEnabledPowers(uuid);
        powerData.clear();
        for (var power : EnabledPowers){
            powerData.add(power.type.name);
        }
        getDataSource().markDirty(uuid);
    }
    private void loadEnabledPowers(){
        getDataSource().getEnabledPowers(uuid).stream().forEach(powerName -> {
            EnabledPowers.add(core.newPowerInstance(RPlayNekoPowerType.Powers.get(powerName), this));
        });
    }

    public boolean addPower(@NotNull RPlayNekoPowerType powerType){
        boolean result = EnabledPowers.add(core.newPowerInstance(powerType, this));
        getDataSource().getEnabledPowers(uuid).add(powerType.name);
        getDataSource().markDirty(uuid);
        return result;
    }
    public boolean removePower(@NotNull RPlayNekoPowerType powerType){
        boolean result = EnabledPowers.removeIf(power -> power.type == powerType);
        getDataSource().getEnabledPowers(uuid).remove(powerType.name);
        getDataSource().markDirty(uuid);
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
        return this.uuid.equals(oplayer.uuid) && this.getDataSource().equals(oplayer.getDataSource());
    }
    @Override
    public int hashCode(){
        final int prime = 31;
        int result = 1;

        result = prime * result + this.uuid.hashCode();
        result = prime * result + this.getDataSource().hashCode();

        return result;
    }
}
