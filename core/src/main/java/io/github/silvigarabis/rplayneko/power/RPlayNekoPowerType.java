package io.github.silvigarabis.rplayneko.power;

import java.util.*;
import io.github.silvigarabis.rplayneko.data.*;

public enum RPlayNekoPowerType {
    NIGHT_VISION("NIGHT_VISION"), JUMP_BOOST("JUMP_BOOST");

    private RPlayNekoPowerType(String name){
        this.name = name;
    }
    public final String name;
    
    public static final Map<String, RPlayNekoPowerType> Powers;
    static {
        Map<String, RPlayNekoPowerType> map = new HashMap<>();
        for (var power : RPlayNekoPowerType.values()){
            map.put(power.name, power);
        }
        Powers = Collections.unmodifiableMap(map);
    }
}
