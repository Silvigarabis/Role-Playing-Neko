package io.github.silvigarabis.rplayneko;

import java.util.Collections;
import java.util.EnumMap;
import java.util.Map;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import java.util.logging.Level;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

/**
 * 和插件聊天消息发送有关的类。
 */
public class Messages<Sender, Player> {
    public enum MessageKey {
        COMMAND_NO_PERMISSION("command-no-permission"),
        COMMAND_UNKNOWN("command-unknown"),
        COMMAND_RESOLVING("command-resolving"),

        COMMAND_HELP_NO_PERMISSION("command-help.no-permission"),
        COMMAND_HELP_GENERAL("command-help.general"),
        COMMAND_HELP_ESGUI("command-help.esgui"),

        INVALID_PLUGIN_CONFIG("console-message.invalid-plugin-config", "配置文件错误！尝试在修正后使用/esplitter reload重新加载"),
        PLUGIN_LOADING("console-message.plugin-loading", "插件正在加载"),
        PLUGIN_ENABLED("console-message.plugin-enabled", "插件已加载完毕"),
        PLUGIN_DISABLED("console-message.plugin-disabled", "插件已退出"),
        PLUGIN_ERROR_DOUBLE_LOAD("console-message.plugin-load-twice-error", "错误！插件被启用了两次！"),
        PLUGIN_RELOADING_CONFIG("console-message.plugin-config-reloading", "正在加载配置"),
        PLUGIN_CONFIG_LOADED("console-message.plugin-config-loaded", "消息文本已加载"),
        PLUGIN_CONFIG_LOAD_ERROR("console-message.plugin-config-load-fail", "配置加载出现错误"),
        PLUGIN_RELOADING_MESSAGE_CONFIG("console-message.plugin-message-config-reloading", "正在加载消息文本"),
        PLUGIN_MESSAGE_CONFIG_LOADED("console-message.plugin-message-config-loaded", "消息文本已加载"),
        PLUGIN_MESSAGE_CONFIG_LOAD_MISSING("console-message.plugin-message-config-missing", "缺失 {} 条消息文本： {}"),

        NEKO_CALL_MASTER_NAME("neko-call-master-name"),
        NEKO_NOTICE_MUTED("neko-notice-muted"),
        NEKO_NOTICE_UNMUTED("neko-notice-unmuted"),
        NEKO_CHAT_MUTED("neko-chat-muted"),
        NEKO_CHAT_PREFIX("neko-chat-prefix"),

        CHAT_PREFIX("chat-prefix", "[RPlayNeko]"),
        LOGGER_NAME("logger-name", "RPlayNeko");

        private final String key;
        public String getKey(){
            return key;
        }
        private final String defaultText;
        public String getDefaultText(){
            return defaultText;
        }
        private MessageKey(String messageKey){
            this.messageKey = messageKey;
            this.defaultString = null;
        }
        private MessageKey(String messageKey, String defaultText){
            this.messageKey = messageKey;
            this.defaultText = defaultText;
        }

        public static final Map<MessageKey, String> Keys;
        static {
            Map<MessageKey, String> m = new EnumMap<>(MessageKey.class);
            for (MessageKey messageKey : MessageKey.values()){
                m.put(messageKey, messageKey.getKey());
            }
            Keys = Collections.unmodifiableMap(m);
        }
    }

    private static Map<MessageKey, String> messages = new EnumMap<>(MessageKey.class);

    public static List<MessageKey> getMissingMessageKeys(){
        List<MessageKey> missingMessageKeys = new ArrayList<>();
        for (MessageKey messageKey : MessageKey.Keys.keySet()){
            if (!messages.containsKey(messageKey)){
                missingMessageKeys.add(messageKey);
            }
        }
        return missingMessageKeys;
    }
    public static void cleanMessageConfig(){
        messages.clear();
    }

    public static void loadMessageConfig(Map<String, String> messageConfig){
        for (MessageKey messageKey : MessageKey.Keys.keySet()){
            String key = messageKey.getKey();
            String messageText = messageConfig.get(key);
            if (messageString != null){
                messages.put(messageKey, messageText);
            }
        }
    }

    public static String getMessageText(MessageKey messageKey, boolean translateColorCode){
        String text = messages.get(messageKey);
        if (text == null){
            text = messageKey.getDefaultString();
        }

        if (text == null){
            text = messageKey.getKey();
        } else if (translateColorCode){
            text = text.replaceAll("&([0-9a-fmnol])", "§$1")
            text = text.replaceAll("&&", "&");
        }
        return string;
    }
    public static String getMessageText(MessageKey messageKey){
        return getMessageText(messageKey, true);
    }

    public static String getMessageText(MessageKey messageKey, String[] replacements){
        String text = getMessageText(messageKey);
        StringBuffer result = new StringBuffer();

        Pattern pattern = Pattern.compile("%(?:(\\d)\\$)?s");
        Matcher matcher = pattern.matcher(text);

        int location = 1;
        while (matcher.find()){
            String match = matcher.group(0);
            String p1 = matcher.group(1);
            String replacement;
            int index;
            if (p1 != null){
                index = Integer.parseInt(p1) - 1;
            } else {
                index = location - 1;
                location++;
            }
            if (index < replacements.length){
                replacement = replacements[index];
            } else {
                replacement = match;
            }
            if (replacement == null){
                replacement = "";
            } else {
                replacement = Matcher.quoteReplacement(replacement);
            }
            matcher.appendReplacement(result, replacement);
        }
        matcher.appendTail(result);

        return result.toString();
    }

    public static String getMessageText(MessageKey messageKey, List<String> replacements){
        String text = getMessageText(messageKey);
        StringBuffer result = new StringBuffer();

        Pattern pattern = Pattern.compile("%(?:(\\d)\\$)?s");
        Matcher matcher = pattern.matcher(text);

        int location = 1;
        while (matcher.find()){
            String match = matcher.group(0);
            String p1 = matcher.group(1);
            String replacement;
            int index;
            if (p1 != null){
                index = Integer.parseInt(p1) - 1;
            } else {
                index = location - 1;
                location++;
            }
            if (index < replacements.size()){
                replacement = replacements.get(index);
            } else {
                replacement = match;
            }
            if (replacement == null){
                replacement = "";
            } else {
                replacement = Matcher.quoteReplacement(replacement);
            }
            matcher.appendReplacement(result, replacement);
        }
        matcher.appendTail(result);

        return result.toString();
    }

    public Messages(RPlayNekoCore<Sender, Player> core){
        this.core = core;
    }
    private final RPlayNekoCore<Sender, Player> platform;

    public String getMessage(Player player, MessageKey messageKey, String... replacements){
        return getMessageText(messageKey, replacements);
    }
    public String getMessage(Player player, MessageKey messageKey){
        return getMessageText(messageKey);
    }
    public String getMessage(Player player, MessageKey messageKey, List<String> replacement){
        return getMessageText(messageKey, replacements);
    }

    public String getMessage(MessageKey messageKey, String... replacements){
        return getMessageText(messageKey, replacements);
    }
    public String getMessage(MessageKey messageKey){
        return getMessageText(messageKey);
    }
    public String getMessage(MessageKey messageKey, List<String> replacement){
        return getMessageText(messageKey, replacements);
    }

    //TODO: 多语言支持
    private String defaultLocale = null;
    private String getMessage(String locale, MessageKey messageKey, String[] replacements){
        return getMessageText(messageKey, replacements);
    }
    private String getMessage(String locale, MessageKey messageKey){
        return getMessageText(messageKey);
    }
    private String getMessage(String locale, MessageKey messageKey, List<String> replacement){
        return getMessageText(messageKey, replacements);
    }

    public void send(Sender sender, String message){
        String prefix = getMessageText(MessageKey.CHAT_PREFIX);
        if (prefix.length() > 0){
            prefix += " ";
        }

        for (var line : message.split("\n")){
            line = prefix + line;
            this.core.getPlatform().sendMessage(sender, line);
        }
    }
    public void send(Sender sender, MessageKey messageKey, List<String> replacements){
        send(sender, getMessage(messageKey, replacements));
    }
    public void send(Sender sender, MessageKey messageKey, String[] replacements){
        send(sender, getMessage(messageKey, replacements));
    }
    public void send(Sender sender, MessageKey messageKey){
        send(sender, getMessage(messageKey));
    }

    public void sendPlayer(Player player, String message){
        String prefix = getMessageText(MessageKey.CHAT_PREFIX);
        if (prefix.length() > 0){
            prefix += " ";
        }

        for (var line : message.split("\n")){
            line = prefix + line;
            this.core.getPlatform().sendMessageToPlayer(player, line);
        }
    }
    public void sendPlayer(Player player, MessageKey messageKey, List<String> replacements){
        sendPlayer(player, getMessage(player, messageKey, replacements));
    }
    public void sendPlayer(Player player, MessageKey messageKey, String[] replacements){
        sendPlayer(player, getMessage(player, messageKey, replacements));
    }
    public void sendPlayer(Player player, MessageKey messageKey){
        sendPlayer(player, getMessage(player, messageKey));
    }

    public void consoleLog(Level level, MessageKey messageKey, String[] replacements){
        consoleLog(level, getMessage(messageKey, replacements));
    }
    public void consoleLog(Level level, String message){
        Logger logger = Logger.getLogger(MessageKey.LOGGER_NAME);

        // remove format code
        message = message.replaceAll("[§&]([0-9a-fmnol])", "");

        logger.log(level, line);
    }

    public void consoleInfo(MessageKey messageKey, String... replacements){
        consoleLog(Level.INFO, messageKey, replacements);
    }
    public void consoleWarn(MessageKey messageKey, String... replacements){
        consoleLog(Level.WARNING, messageKey, replacements);
    }
    public void consoleError(MessageKey messageKey, String... replacements){
        consoleLog(Level.SEVERE, messageKey, replacements);
    }
}