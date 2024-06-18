package io.github.silvigarabis.rplayneko.data;

import io.github.silvigarabis.rplayneko.power.RPlayNekoPowerType;

import java.util.*;
import org.jetbrains.annotations.*;
import java.util.concurrent.ConcurrentHashMap;

public class RPlayNekoData {
    private boolean _dirty = true;
    private boolean _delete = false;
    @ApiStatus.Internal
    public boolean _delete(){
        return _delete;
    }
    @ApiStatus.Internal
    public void _delete(boolean delete){
        _delete = delete;
        if (delete){
            _dirty = false;
        }
    }
    @ApiStatus.Internal
    public boolean _dirty(){
        return _dirty;
    }
    @ApiStatus.Internal
    public void _dirty(boolean dirty){
        _dirty = dirty;
        if (dirty){
            _delete = false;
        }
    }

    public void markDirty(){
        _dirty(true);
    }
    public void markDelete(){
        _delete(true);
    }

    private final  @NotNull UUID uuid;
    private @Nullable UUID castor = null;

    private boolean isNeko = false;
    private boolean isMuted = false;
    private @Nullable String nyaText = null;

    private final Map<UUID, Integer> experiences = new ConcurrentHashMap<>();
    private final Map<String, String> speakReplaces = new ConcurrentHashMap<>();
    private final Map<String, String> regexpSpeakReplaces = new ConcurrentHashMap<>();
    private final Set<String> ownerCalls = ConcurrentHashMap.newKeySet();
    private final Set<RPlayNekoPowerType> enabledPowers = ConcurrentHashMap.newKeySet();
    private final List<UUID> owners = new ArrayList<>();

    public RPlayNekoData(UUID uuid){
        this.uuid = uuid;
    }

    public @NotNull UUID getUuid(){
        return uuid;
    }

    public @Nullable UUID getCastor(){
        return castor;
    }

    public void setCastor(@Nullable UUID castor){
        this.castor = castor;
        markDirty();
    }

    public boolean isNeko(){
        return isNeko;
    }

    public void setNeko(boolean neko){
        isNeko = neko;
        markDirty();
    }

    public boolean isMuted(){
        return isMuted;
    }

    public void setMuted(boolean muted){
        isMuted = muted;
        markDirty();
    } 

    public @Nullable String getNyaText(){
        return nyaText;
    }

    public void setNyaText(@Nullable String nyaText){
        this.nyaText = nyaText;
        markDirty();
    }

    public Map<UUID, Integer> getExperiences(){
        return experiences;
    }
    public int getExperience(@NotNull UUID player, int xp){
        if (experiences.containsKey(player)){
            return experiences.get(player);
        }
        throw new IllegalArgumentException("no xp record found");
    }
    public void setExperience(@NotNull UUID player, int xp){
        experiences.put(player, xp);
        markDirty();
    }
    public void deleteExperience(@NotNull UUID player){
        experiences.remove(player);
        markDirty();
    }

    public Map<String, String> getRegexpSpeakReplaces(){
        return regexpSpeakReplaces;
    }

    public void addRegexpSpeakReplace(@NotNull String pattern, @NotNull String replacement){
        regexpSpeakReplaces.put(pattern, replacement);
        markDirty();
    }

    public boolean removeRegexpSpeakReplace(@NotNull String pattern){
        boolean result = null != regexpSpeakReplaces.remove(pattern);
        if (result){
            markDirty();
        }
        return result;
    }

    public Map<String, String> getSpeakReplaces(){
        return speakReplaces;
    }

    public void addSpeakReplace(@NotNull String pattern, @NotNull String replacement){
        speakReplaces.put(pattern, replacement);
        markDirty();
    }

    public boolean removeSpeakReplace(@NotNull String pattern){
        boolean result = null != speakReplaces.remove(pattern);
        if (result){
            markDirty();
        }
        return result;
    }

    public Set<String> getOwnerCalls(){
        return ownerCalls;
    }

    public boolean addOwnerCall(@NotNull String call){
        boolean result = ownerCalls.add(call);
        if (result){
            markDirty();
        }
        return result;
    }

    public boolean removeOwnerCall(@NotNull String call){
        boolean result = ownerCalls.remove(call);
        if (result){
            markDirty();
        }
        return result;
    }

    public Set<RPlayNekoPowerType> getEnabledPowers(){
        return enabledPowers;
    }

    public boolean addEnabledPower(@NotNull RPlayNekoPowerType power){
        boolean result = enabledPowers.add(power);
        if (result){
            markDirty();
        }
        return result;
    }

    public boolean removeEnabledPower(@NotNull RPlayNekoPowerType power){
        boolean result = enabledPowers.remove(power);
        if (result){
            markDirty();
        }
        return result;
    }

    public List<UUID> getOwners(){
        synchronized(this.owners){
            return new ArrayList<>(this.owners);
        }
    }

    public void setOwners(@Nullable List<UUID> owners){
        synchronized(this.owners){
            this.owners.clear();

            if (owners != null)
            for (var p : owners){
                this.owners.add(p);
            }
        }
    }

    public @Nullable UUID getOwner(){
        synchronized(owners){
            if (owners.size() > 0){
                return owners.get(0);
            }
        }
        return null;
    }

    public void setOwner(@NotNull UUID owner){
        synchronized(owners){
            if (owners.contains(owner)){
                owners.remove(owner);
            }
            owners.add(0, owner);
            markDirty();
        }
    }

    public boolean addOwner(@NotNull UUID owner){
        boolean result;
        synchronized(owners){
            if (owners.contains(owner)){
                result = false;
            } else {
                result = owners.add(owner);
            }
        }
        if (result){
            markDirty();
        }
        return result;
    }

    public boolean removeOwner(@NotNull UUID owner){
        boolean result;
        synchronized(owners){
            result = owners.remove(owner);
        }
        if (result){
            markDirty();
        }
        return result;
    }

}
