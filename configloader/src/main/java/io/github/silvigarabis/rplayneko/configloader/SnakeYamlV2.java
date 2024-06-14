package io.github.silvigarabis.rplayneko.configloader;

import io.github.silvigarabis.rplayneko.RPlayNekoConfig;
import java.util.Map;
import java.io.InputStream;
import org.yaml.snakeyaml.Yaml;

// 保留一份代码便于复制

public final class SnakeYamlV2 {
    public static boolean isAvailable(){
        try {
            Class.forName("org.yaml.snakeyaml.Yaml");
        } catch (ClassNotFoundException ex){
           return false;
        }
        return true;
    }
    public static RPlayNekoConfig loadCoreConfig(InputStream in){
        var data = new Yaml().load(in);
        throw new RuntimeException();
    }
    public static Map<String, String> loadMessageConfig(InputStream in){
        throw new RuntimeException();
    }
    private SnakeYamlV2(){ }
}
