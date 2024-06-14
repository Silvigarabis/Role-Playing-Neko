package io.github.silvigarabis.rplayneko.configloader;

import io.github.silvigarabis.rplayneko.RPlayNekoConfig;
import java.util.Map;
import java.io.InputStream;

// 保留一份代码便于复制

public final class Gson {
    public static boolean isAvailable(){
        return false;
    }
    public static RPlayNekoConfig loadCoreConfig(InputStream in){
        throw new RuntimeException();
    }
    public static Map<String, String> loadMessageConfig(InputStream in){
        throw new RuntimeException();
    }
    private Gson(){ }
}
