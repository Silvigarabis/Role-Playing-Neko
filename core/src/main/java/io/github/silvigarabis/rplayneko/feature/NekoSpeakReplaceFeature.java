package io.github.silvigarabis.rplayneko.feature;

import io.github.silvigarabis.rplayneko.event.*;

public abstract class NekoSpeakReplaceFeature<Player> implements IFeature<Player> {
    public void onChatEvent(ChatEvent<Player> event){
        event.getPlayer().getData().getSpeakReplaces();
        event.getPlayer().getData().getRegexpSpeakReplaces();
    }
}

/*
        // replace
        if (getConfig().feature_SpeakReplaceAlways || (getConfig().feature_SpeakReplace && event.player.isNeko())){

            boolean isEnabledRegex = getConfig().feature_SpeakReplace_Regex;
            for (Map.Entry<String, String> entry : event.player.getSpeakReplaces().entrySet()){
                String pattern = entry.getKey();
                String replacement = entry.getValue();

                boolean isRegex = false;
                if (isEnabledRegex && pattern.length() > 2 && pattern.startsWith("/") && pattern.endsWith("/")){
                    isRegex = true;
                }

                if (isRegex){
                    pattern = pattern.substring(1, pattern.length() - 1);
                    event.message = StringUtil.timeLimitedReplaceAll(event.message, pattern, replacement);
                } else {
                    event.message = event.message.replace(pattern, replacement);
                }
            }

            event.result = EventResult.MODIFIED;
        }
*/