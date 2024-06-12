package io.github.silvigarabis.rplayneko.power;

import io.github.silvigarabis.rplayneko.data.*;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;

public enum RPlayNekoPowers {
    NIGHT_VISION("NIGHT_VISION"), JUMP_BOOST("JUMP_BOOST");
    private RPlayNekoPowers(String name){
        this.name = name;
    }
    public final String name;
    
    public static final Map<String, RPlayNekoPowers> Powers;
    static {
        Map<String, RPlayNekoPowers> map = new HashMap<>();
        for (var power : RPlayNekoPowers.values()){
            map.put(power.name, power);
        }
        Powers = Collections.unmodifiableMap(map);
    }

    private static final Map<RPlayNekoPowers, Map<Class<?>, Consumer<RPlayNekoPlayer<?>>>> PowerTypeTickImpl = new HashMap<>(); 
    public static void tickPowerForPlayer(RPlayNekoPowers type, RPlayNekoPlayer<?> player){
        var ImplMap = PowerTypeTickImpl.get(type);
        if (ImplMap != null){
            var tickImpl = ImplMap.get(player.origin.getClass());
            if (tickImpl != null){
                tickImpl.accept(player);
                return;
            }
        }
        throw new IllegalArgumentException("No neko power implements for type " + type + " of class " + player.origin.getClass().toString());
    }
    public static void addPowerTypeTickImpl(RPlayNekoPowers type, Class<?> clazz, Consumer<RPlayNekoPlayer<?>> tickImpl){
        PowerTypeTickImpl
            .computeIfAbsent(type, k -> new HashMap<>())
            .put(clazz, tickImpl);
    }
}
