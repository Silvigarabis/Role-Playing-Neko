package io.github.silvigarabis.rplayneko;

import java.util.*;
import org.jetbrains.annotations.*;
import io.github.silvigarabis.rplayneko.power.RPlayNekoPowerType;

public class RPlayNekoConfig {
    public static enum ChatNyaLocation {
        END, HEAD, CUSTOM /* why this exists? */
    }

    private final int configVersion = 1;
    public int getConfigVersion(){
        return configVersion;
    }

    private String language = "zh_cn";
    public void setLanguage(String lang){
        language = lang;
    }
    public String getLanguage(){
        return language;
    }
    private Set<String> enabledPowers = new HashSet<>();
    public void removeEnabledPower(String type){
        enabledPowers.remove(type);
    }
    public void addEnabledPower(String type){
        enabledPowers.add(type);
    }
    public void setEnabledPowers(Collection<String> typeSet){
        enabledPowers.clear();
        enabledPowers.addAll(typeSet);
    }
    public Set<String> getEnabledPowers(){
        return enabledPowers;
    }

    private Set<String> enabledFeatures = new HashSet<>();
    public void setEnabledFeatures(Collection<String> featureSet){
        enabledFeatures.clear();
        enabledFeatures.addAll(featureSet);
    }
    public Set<String> getEnabledFeatures(){
        return enabledFeatures;
    }
    public void removeEnabledFeature(String type){
        enabledFeatures.remove(type);
    }
    public void addEnabledFeature(String type){
        enabledFeatures.add(type);
    }
}
