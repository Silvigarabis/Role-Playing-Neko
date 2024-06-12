package io.github.silvigarabis.rplayneko.data;

import java.util.UUID;
import java.util.HashMap;
import java.util.WeakHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Collection;
import java.util.Collections;
import java.util.concurrent.ConcurrentHashMap;
import org.jetbrains.annotations.*;

public class RPlayNekoData {
    private boolean _dirty = true;
    public boolean _dirty(){
        return _dirty;
    }
    public void _dirty(boolean dirty){
        _dirty = dirty;
    }

    private @NotNull UUID uuid;
    private boolean isNeko = false;
    private boolean isMuted = false;
    private boolean isSelfTransform = false;
    private @Nullable String nyaText = null;
    private final Map<String, String> speakReplaces = new ConcurrentHashMap<>();
    private final Set<String> enabledPowers = ConcurrentHashMap.newKeySet();
    private @Nullable UUID ownerUUID = null;
    private @NotNull Set<UUID> multiOwners = ConcurrentHashMap.newKeySet();

    public RPlayNekoData(UUID uuid){
        this.uuid = uuid;
    }

    public @NotNull UUID getUuid() {
        return uuid;
    }

    public boolean isNeko() {
        return isNeko;
    }

    public void setNeko(boolean neko) {
        _dirty(true);
        isNeko = neko;
    }

    public boolean isMuted() {
        return isMuted;
    }

    public void setMuted(boolean muted) {
        _dirty(true);
        isMuted = muted;
    }

    public boolean isSelfTransform() {
        return isSelfTransform;
    }

    public void setSelfTransform(boolean selfTransform) {
        _dirty(true);
        isSelfTransform = selfTransform;
    }

    public @Nullable String getNyaText() {
        return nyaText;
    }

    public void setNyaText(@Nullable String nyaText) {
        _dirty(true);
        this.nyaText = nyaText;
    }

    public @NotNull Map<String, String> getSpeakReplaces() {
        return speakReplaces;
    }

    public void setSpeakReplaces(@NotNull Map<String, String> speakReplaces) {
        _dirty(true);
        this.speakReplaces.clear();
        for (Map.Entry<String, String> entry : speakReplaces.entrySet()){
            this.speakReplaces.put(entry.getKey(), entry.getValue());
        }
    }

    public @NotNull Set<String> getEnabledPowers() {
        return enabledPowers;
    }

    public void setEnabledPowers(@NotNull Collection<String> enabledPowers) {
        _dirty(true);
        this.enabledPowers.clear();
        for (String power : enabledPowers){
            this.enabledPowers.add(power);
        }
    }

    public @Nullable UUID getOwnerUUID() {
        return ownerUUID;
    }

    public void setOwnerUUID(@Nullable UUID ownerUUID) {
        _dirty(true);
        this.ownerUUID = ownerUUID;
    }

    public @NotNull Set<UUID> getMultiOwners(){
        return multiOwners;
    }
    public void setMultiOwners(@NotNull Collection<UUID> multiOwners){
        _dirty(true);
        this.multiOwners.clear();
        for (UUID power : multiOwners){
            this.multiOwners.add(power);
        }
    }
}
