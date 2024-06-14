package io.github.silvigarabis.rplayneko.configloader;

import io.github.silvigarabis.rplayneko.RPlayNekoConfig;
import java.io.InputStream;
import java.util.Map;

public final class ConfigLoader {
    public RPlayNekoConfig coreConfig_loadYaml(InputStream in){
        if (SnakeYamlV2.isAvailable()){
            return SnakeYamlV2.loadCoreConfig(in);
        }
        return SnakeYamlV1.loadCoreConfig(in);
    }
    public RPlayNekoConfig coreConfig_loadJson(InputStream in){
        return Gson.loadCoreConfig(in);
    }
    public RPlayNekoConfig coreConfig_loadProperties(InputStream in){
        return Properties.loadCoreConfig(in);
    }

    public Map<String, String> messageConfig_loadYaml(InputStream in){
        if (SnakeYamlV2.isAvailable()){
            return SnakeYamlV2.loadMessageConfig(in);
        }
        return SnakeYamlV1.loadMessageConfig(in);
    }
    public Map<String, String> messageConfig_loadJson(InputStream in){
        return Gson.loadMessageConfig(in);
    }
    public Map<String, String> messageConfig_loadProperties(InputStream in){
        return Properties.loadMessageConfig(in);
    }

    private ConfigLoader(){ }
}
