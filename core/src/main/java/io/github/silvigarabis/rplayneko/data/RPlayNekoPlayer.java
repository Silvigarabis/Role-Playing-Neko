package io.github.silvigarabis.rplayneko.data;

import io.github.silvigarabis.rplayneko.power.RPlayNekoPowers;
import io.github.silvigarabis.rplayneko.Messages;
import io.github.silvigarabis.rplayneko.RPlayNekoCore;

import java.util.UUID;
import java.util.HashMap;
import java.util.WeakHashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Collections;
import java.util.Set;
import java.util.function.Consumer;
import java.util.concurrent.ConcurrentHashMap;
import org.jetbrains.annotations.*;

public class RPlayNekoPlayer<T> {
    private RPlayNekoCore<?, ?, T> core;
    private static final Map<Class<?>, Consumer<RPlayNekoPlayer<?>>> TickImpl = new HashMap<>(); 
    public static void addTickImpl(Class<?> clazz, Consumer<RPlayNekoPlayer<?>> tickImpl){
        TickImpl.put(clazz, tickImpl);
    }

    public final @NotNull T origin;
    public final @NotNull UUID uuid;
    public final @NotNull RPlayNekoDataSource data;
    public RPlayNekoPlayer(@NotNull UUID uuid, @NotNull RPlayNekoDataSource data, @NotNull T origin, @NotNull RPlayNekoCore<?, ?, T> core){
        this.uuid = uuid;
        this.origin = origin;
        this.data = data;
        this.core = core;
    }
    public boolean isNeko(){
        return data.isNeko(uuid);
    }
    public boolean nekoAddWithOwner(@NotNull UUID owner){
        return data.addNeko(uuid, owner);
    }
    public boolean nekoAdd(){
        return data.addNeko(uuid, null);
    }
    public boolean nekoRemove(){
        return data.removeNeko(uuid);
    }
    public void setSelfTransform(@NotNull UUID uuid, boolean mode){
        data.setSelfTransform(uuid, mode);
    }
    public boolean isSelfTransform(@NotNull UUID uuid){
        return data.isSelfTransform(uuid);
    }

    public void addOwner(@NotNull UUID ownerUUID){
        if (getOwner() == null){
            setOwner(ownerUUID);
        }
        if (RPlayNekoCore.getInstance().getConfig().feature_Neko_MultiOwner){
            data.getMultiOwners(uuid).add(ownerUUID);
            data.markDirty(uuid);
        }
    }
    public void setOwner(@Nullable UUID ownerUUID){
        if (!RPlayNekoCore.getInstance().getConfig().feature_Neko_MultiOwner){
            data.getMultiOwners(uuid).clear();
            data.markDirty(uuid);
        }
        data.getMultiOwners(uuid).add(ownerUUID);
        data.setOwner(uuid, ownerUUID);
    }
    public @Nullable UUID getOwner(){
        return data.getOwner(uuid);
    }
    public boolean isOwner(@NotNull UUID personUUID){
        if (RPlayNekoCore.getInstance().getConfig().feature_Neko_MultiOwner){
            return data.getMultiOwners(uuid).contains(personUUID);
        }
        return personUUID != null && personUUID.equals(data.getOwner(uuid));
    }
    public void setNyaText(@Nullable String text){
        data.setNya(uuid, text);
    }
    public @Nullable String getNyaText(){
        return data.getNya(uuid);
    }
    public boolean hasNyaText(){
        return getNyaText() != null;
    }
    public void setMuted(boolean enabled){
        data.setMuted(uuid, enabled);
    }
    public boolean isMuted(){
        return data.isMuted(uuid);
    }
    public boolean addMasterAlias(@NotNull String masterAlias){
        return addSpeakReplace(masterAlias, core.getMessages().getMessage(this.origin, Messages.MessageKey.NEKO_CALL_MASTER_NAME));
    }
    public boolean addSpeakReplace(@NotNull String pattern, @NotNull String replacement){
        return data.addSpeakReplace(uuid, pattern, replacement);
    }
    public boolean removeSpeakReplace(@NotNull String pattern){
        return data.removeSpeakReplace(uuid, pattern);
    }
    /**
     * 对该Map的修改后，应该使用 {RPlayerNekoDataSource#markDirty} 标记
     */
    public @NotNull Map<String, String> getSpeakReplaces(){
        return data.getSpeakReplaces(uuid);
    }

    public boolean enablePower(@NotNull RPlayNekoPowers power){
        boolean result = !data.getEnabledPowers(uuid).contains(power.name);
        if (result){
            data.getEnabledPowers(uuid).add(power.name);
        }
        return result;
    }
    public boolean disablePower(@NotNull RPlayNekoPowers power){
        boolean result = data.getEnabledPowers(uuid).contains(power.name);
        if (result){
            data.getEnabledPowers(uuid).remove(power.name);
        }
        return result;
    }
    public @NotNull Set<RPlayNekoPowers> getEnabledPowers(){
        return new HashSet<>(data.getEnabledPowers(uuid).stream().map(name -> RPlayNekoPowers.Powers.get(name)).toList());
    }

    public void tick(){
        var tickImpl = TickImpl.get(this.origin.getClass());
        if (tickImpl != null){
            tickImpl.accept(this);
        } else {
            throw new IllegalArgumentException("No neko tick implements of class " + origin.getClass().toString());
        }
        this.getEnabledPowers().stream().forEach((p) -> {
            RPlayNekoPowers.tickPowerForPlayer(p, this);
        });
    }

    @Override
    public boolean equals(Object o){
        if (o == this){
            return true;
        }
        if (!(o instanceof RPlayNekoPlayer)){
            return false;
        }
        RPlayNekoPlayer oplayer = (RPlayNekoPlayer)o;
        return this.uuid.equals(oplayer.uuid) && this.data.equals(oplayer.data);
    }
    @Override
    public int hashCode(){
        final int prime = 31;
        int result = 1;

        result = prime * result + this.uuid.hashCode();
        result = prime * result + this.data.hashCode();

        return result;
    }
}
