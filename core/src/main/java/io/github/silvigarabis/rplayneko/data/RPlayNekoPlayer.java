package io.github.silvigarabis.rplayneko.data;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import org.jetbrains.annotations.*;

import io.github.silvigarabis.rplayneko.event.NekoTransformEvent;
import io.github.silvigarabis.rplayneko.power.*;
import io.github.silvigarabis.rplayneko.Messages;
import io.github.silvigarabis.rplayneko.RPlayNekoCore;

public class RPlayNekoPlayer<T> {
    protected final RPlayNekoData data;
    public RPlayNekoData getData(){
        return data;
    }
    protected final T origin;
    public T getOrigin(){
        return origin;
    }

    protected final RPlayNekoCore<?, T> core;
    public RPlayNekoCore<?, T> getCore(){
        return core;
    }

    public boolean transformToNeko(@Nullable UUID castor){
        if (this.data.isNeko()){
            return false;
        }
        NekoTransformEvent<T> tEvent = new NekoTransformEvent<>(this, false);
        this.core.emitEvent(tEvent);
        if (tEvent.isCancelled()){
            return false;
        }
        data.setNeko(true);
        data.setCastor(castor);

        return true;
    }
    public boolean transformBack(){
        if (!this.data.isNeko()){
            return false;
        }
        NekoTransformEvent<T> tEvent = new NekoTransformEvent<>(this, true);
        this.core.emitEvent(tEvent);
        if (tEvent.isCancelled()){
            return false;
        }
        data.setNeko(false);
        data.setMuted(false);
        data.setNyaText(null);
        data.setCastor(null);
        data.getSpeakReplaces().clear();
        data.getRegexpSpeakReplaces().clear();
        data.getEnabledPowers().clear();
        data.getMasterCalls().clear();
        data.getExperiences().clear();
        data.setOwners(null);
        data.markDirty();
        // for save-only-neko-data
        //data.markDelete();

        return true;
    }

    public RPlayNekoPlayer(@NotNull RPlayNekoCore<?, T> core, @NotNull T origin, @NotNull RPlayNekoData data){
        this.core = core;
        this.origin = origin;
        this.data = data;
        applyPowerChange();
    }

    private Map<RPlayNekoPowerType, RPlayNekoPower<T>> powers = new HashMap<>();
    private void applyPowerChange(){
        var enabledPowers = data.getEnabledPowers();
        if (powers.keySet().equals(enabledPowers)){
            return;
        }
        for (var powerType : enabledPowers){
            if (!powers.containsKey(powerType)){
                var power = core.newPowerInstance(powerType, this);
                powers.put(powerType, power);
            }
        }
        var removedPowers = new HashSet<RPlayNekoPowerType>();
        for (var powerType : powers.keySet()){
            if (!enabledPowers.contains(powerType)){
                removedPowers.add(powerType);
            }
        }
        for (var powerType : removedPowers){
            powers.remove(powerType);
        }
    }

    public boolean enablePower(RPlayNekoPowerType powerType){
        boolean result = data.addEnabledPower(powerType);
        applyPowerChange();
        return result;
    }

    public boolean disablePower(RPlayNekoPowerType powerType){
        boolean result = data.removeEnabledPower(powerType);
        applyPowerChange();
        return result;
    }

    public void tick(){
        this.powers.values().stream()
            .forEach(p -> p.tick());
    }

    public void sendMessage(String message){
        this.getCore().getMessages().sendPlayer(this.origin, message);
    }
    public void sendMessage(Messages.MessageKey messageKey, List<String> replacements){
        this.getCore().getMessages().sendPlayer(this.origin, messageKey, replacements);
    }
    public void sendMessage(Messages.MessageKey messageKey, String... replacements){
        this.getCore().getMessages().sendPlayer(this.origin, messageKey, replacements);
    }
    public void sendMessage(Messages.MessageKey messageKey){
        this.getCore().getMessages().sendPlayer(this.origin, messageKey);
    }

    @Override
    public boolean equals(Object o){
        if (o == this){
            return true;
        }
        if (!(o instanceof RPlayNekoPlayer)){
            return false;
        }
        RPlayNekoPlayer<?> oplayer = (RPlayNekoPlayer)o;
        return this.data.getUuid().equals(oplayer.data.getUuid()) && this.core.getDataSource().equals(oplayer.core.getDataSource());
    }
    @Override
    public int hashCode(){
        final int prime = 31;
        int result = 1;

        result = prime * result + this.data.getUuid().hashCode();
        result = prime * result + this.core.getDataSource().hashCode();

        return result;
    }
}
