package io.github.silvigarabis.rplayneko.data;

import java.util.*;
import org.jetbrains.annotations.*;
import java.util.concurrent.ConcurrentHashMap;

public class RPlayNekoData {
    private boolean _dirty = true;
    public boolean _dirty(){
        return _dirty;
    }
    public void _dirty(boolean dirty){
        _dirty = dirty;
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
    private final Set<String> enabledPowers = ConcurrentHashMap.newKeySet();
    private final Set<UUID> owners = ConcurrentHashMap.newKeySet();

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
        _dirty(true);
    }

    public boolean isNeko(){
        return isNeko;
    }

    public void setNeko(boolean neko){
        isNeko = neko;
        _dirty(true);
    }

    public boolean isMuted(){
        return isMuted;
    }

    public void setMuted(boolean muted){
        isMuted = muted;
        _dirty(true);
    } 

    public @Nullable String getNyaText(){
        return nyaText;
    }

    public void setNyaText(@Nullable String nyaText){
        this.nyaText = nyaText;
        _dirty(true);
    }

    public @UnmodifiableView Map<UUID, Integer> getExperiences(){
        return Collections.unmodifiableMap(experiences);
    }
    public int getExperience(@NotNull UUID player, int xp){
        if (experiences.containsKey(player)){
            return experiences.get(player);
        }
        throw IllegalArgumentException("no xp record found");
    }
    public boolean setExperience(@NotNull UUID player, int xp){
        experiences.put(player, xp);
        _dirty(true);
    }
    public boolean deleteExperience(@NotNull UUID player){
        experiences.remove(player);
        _dirty(true);
    }

    public @UnmodifiableView Map<String, String> getRegexpSpeakReplaces(){
        return Collections.unmodifiableMap(regexpSpeakReplaces);
    }

    public void addRegexpSpeakReplace(@NotNull String pattern, @NotNull String replacement){
        regexpSpeakReplaces.put(pattern, replacement);
        _dirty(true);
    }

    public boolean removeRegexpSpeakReplace(@NotNull String pattern){
        boolean result = regexpSpeakReplaces.remove(pattern);
        if (result){
            _dirty(true);
        }
    }

    public @UnmodifiableView Map<String, String> getSpeakReplaces(){
        return Collections.unmodifiableMap(speakReplaces);
    }

    public void addSpeakReplace(@NotNull String pattern, @NotNull String replacement){
        speakReplaces.put(pattern, replacement);
        _dirty(true);
    }

    public boolean removeSpeakReplace(@NotNull String pattern){
        boolean result = speakReplaces.remove(pattern);
        if (result){
            _dirty(true);
        }
    }

    public @UnmodifiableView Set<String> getOwnerCalls(){
        return Collections.unmodifiableSet(ownerCalls);
    }

    public boolean addOwnerCall(@NotNull String call){
        boolean result = ownerCalls.add(call);
        if (result){
            _dirty(true);
        }
        return result;
    }

    public boolean removeOwnerCall(@NotNull String call){
        boolean result = ownerCalls.remove(call);
        if (result){
            _dirty(true);
        }
        return result;
    }

    public @UnmodifiableView Set<RPlayNekoPowerType> getEnabledPowers(){
        return Collections.unmodifiableSet(enabledPowers);
    }

    public boolean addEnabledPower(@NotNull RPlayNekoPowerType power){
        boolean result = enabledPowers.add(power);
        if (result){
            _dirty(true);
        }
        return result;
    }

    public boolean removeEnabledPower(@NotNull RPlayNekoPowerType power){
        boolean result = enabledPowers.remove(power);
        if (result){
            _dirty(true);
        }
        return result;
    }

    public List<UUID> getOwners(){
        synchronized(owners){
            return new ArrayList<>(owners);
        }
    }

    public @Nullable getOwner(){
        synchronized(owners){
            if (owners.size() > 0){
                return owners.get(0);
            }
        }
        return null;
    }

    public boolean setOwner(@NotNull UUID owner){
        boolean result;
        synchronized(owners){
            if (owners.contains(owner)){
                owners.remove(owner);
            }
            result = owners.add(0, owner);
        }
        if (result){
            _dirty(true);
        }
        return result;
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
            _dirty(true);
        }
        return result;
    }

    public boolean removeOwner(@NotNull UUID owner){
        boolean result;
        synchronized(owners){
            result = owners.remove(owner);
        }
        if (result){
            _dirty(true);
        }
        return result;
    }

}
