package io.github.silvigarabis.rplayneko;

import java.io.File;
import java.util.Map;
import java.util.EnumMap;

import java.util.logging.Logger;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;

/**
 * 和插件聊天消息发送有关的类。
 *
 * TODO: 统一命名
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
        NEKO_CHAT_PREFIX("neko-chat-prefix"),

        CHAT_PREFIX("chat-prefix", "[RPlayNeko]"),
        LOGGER_NAME("logger-name", "RPlayNeko");

        private String messageKey = null;
        public String getMessageKey(){
            return messageKey;
        }
        private String defaultString;
        public String getDefaultString(){
            return defaultString;
        }
        private MessageKey(String messageKey){
            this.messageKey = messageKey;
        }
        private MessageKey(String messageKey, String defaultString){
            this.messageKey = messageKey;
            this.defaultString = defaultString;
        }
        public String getMessageString(){
            return Messages.getMessageString(this);
        }
    }

    private static String DEFAULT_LOGGER_NAME = "RPlayNeko";
    private static Map<MessageKey, String> messages = new EnumMap<MessageKey, String>(MessageKey.class);
    public static List<MessageKey> getMissingMessageKeys(){
        List<MessageKey> missingKeys = new ArrayList<>();
        for (MessageKey key : MessageKey.values()){
            if (!messages.containsKey(key)){
                missingKeys.add(key);
            }
        }
        return missingKeys;
    }
    public static void cleanMessageConfig(){
        messages.clear();
    }
    public static void loadMessageConfig(Map<String, String> messageConfig){
        for (Object messageKeyObject : MessageKey.values()){
            MessageKey messageKey = (MessageKey) messageKeyObject;

            String key = messageKey.getMessageKey();

            String messageString = messageConfig.get(key);
            if (messageString == null){
                continue;
            }
            messages.put(messageKey, messageString);
        }
    }

    public static String getMessageString(MessageKey messageKey, boolean translateColorCode){
        var string = messages.get(messageKey);
        if (string == null){
            string = messageKey.getDefaultString();
        }

        if (string == null){
            string = messageKey.getMessageKey();
        } else if (translateColorCode){
            string = string.replaceAll("&([0-9a-fmnol])", "§$1");
            string = string.replaceAll("&&", "&");
        }
        return string;
    }
    public static String getMessageString(MessageKey messageKey){
        return getMessageString(messageKey, true);
    }
    
    public static String getMessage(MessageKey key, String[] replacements){
        var messageString = getMessageString(key);
        for (var replacement : replacements){
            messageString = messageString.replaceFirst("\\{\\}", Matcher.quoteReplacement(replacement));
        }
        return messageString;
    }
    public static String getMessage(MessageKey key){
        return getMessageString(key);
    }
    public static String getMessage(MessageKey key, String replacement, String... replacements){
       String[] fullReplacements = new String[replacements.length + 1];
       fullReplacements[0] = replacement;
       for (int idx = 1; idx <= replacements.length; idx++){
          fullReplacements[idx] = replacements[idx - 1];
       }
       return getMessage(key, fullReplacements);
    }

    public Messages(Platform<Sender, Player> platform){
        this.platform = platform;
    }
    private Platform<Sender, Player> platform;

    public String getMessage(Player player, MessageKey key, String[] replacements){
        return getMessage(key, replacements);
    }
    public String getMessage(Player player, MessageKey key){
        return getMessage(key);
    }
    public String getMessage(Player player, MessageKey key, String replacement, String... replacements){
       String[] fullReplacements = new String[replacements.length + 1];
       fullReplacements[0] = replacement;
       for (int idx = 1; idx <= replacements.length; idx++){
          fullReplacements[idx] = replacements[idx - 1];
       }
       return getMessage(player, key, fullReplacements);
    }

    public void send(Sender sender, MessageKey key, String replacement, String... replacements){
        String[] fullReplacements = new String[replacements.length + 1];
        fullReplacements[0] = replacement;
        for (int idx = 1; idx <= replacements.length; idx++){
           fullReplacements[idx] = replacements[idx - 1];
        }
        send(sender, key, fullReplacements);
    }
    public void send(Sender sender, MessageKey key, String[] replacements){
        send(sender, getMessage(key, replacements));
    }
    public void send(Sender sender, MessageKey key){
        send(sender, getMessageString(key));
    }

    public void send(Sender sender, String message){
        var prefix = getMessageString(MessageKey.CHAT_PREFIX);
        if (prefix.length() > 0){
            prefix += " ";
        }

        for (var line : message.split("\n")){
            line = prefix + line;
            this.platform.sendMessage(sender, line);
        }
    }
    
    public void consoleLog(java.util.logging.Level level, MessageKey key, String replacement, String... replacements){
        String[] fullReplacements = new String[replacements.length + 1];
        fullReplacements[0] = replacement;
        for (int idx = 1; idx <= replacements.length; idx++){
           fullReplacements[idx] = replacements[idx - 1];
        }
        consoleLog(level, key, fullReplacements);
    }
    public void consoleLog(java.util.logging.Level level, MessageKey key, String[] replacements){
        consoleLog(level, getMessage(key, replacements));
    }
    public void consoleLog(java.util.logging.Level level, MessageKey key){
        consoleLog(level, getMessageString(key));
    }
    public void consoleLog(java.util.logging.Level level, String message){
        String loggerName = getMessageString(MessageKey.LOGGER_NAME);
        if (loggerName == null
          || loggerName.length() == 0
          || loggerName.equals(MessageKey.LOGGER_NAME.getMessageKey())){
            loggerName = DEFAULT_LOGGER_NAME;
        }
        message = message.replaceAll("[§&]([0-9a-fmnol])", "");
        var logger = this.platform.getLogger();
        for (var line : message.split("\n")){
            logger.log(level, line);
        }
    }

    public void consoleInfo(MessageKey key, String replacement, String... replacements){
        String[] fullReplacements = new String[replacements.length + 1];
        fullReplacements[0] = replacement;
        for (int idx = 1; idx <= replacements.length; idx++){
           fullReplacements[idx] = replacements[idx - 1];
        }
        consoleInfo(key, fullReplacements);
    }
    public void consoleInfo(MessageKey key, String[] replacements){
        consoleInfo(getMessage(key, replacements));
    }
    public void consoleInfo(MessageKey key){
        consoleInfo(getMessageString(key));
    }
    public void consoleInfo(String message){
        consoleLog(java.util.logging.Level.INFO, message);
    }

    public void consoleWarn(MessageKey key, String replacement, String... replacements){
        String[] fullReplacements = new String[replacements.length + 1];
        fullReplacements[0] = replacement;
        for (int idx = 1; idx <= replacements.length; idx++){
           fullReplacements[idx] = replacements[idx - 1];
        }
        consoleWarn(key, fullReplacements);
    }
    public void consoleWarn(MessageKey key, String[] replacements){
        consoleWarn(getMessage(key, replacements));
    }
    public void consoleWarn(MessageKey key){
        consoleWarn(getMessageString(key));
    }
    public void consoleWarn(String message){
        consoleLog(java.util.logging.Level.WARNING, message);
    }

    public void consoleError(MessageKey key, String replacement, String... replacements){
        String[] fullReplacements = new String[replacements.length + 1];
        fullReplacements[0] = replacement;
        for (int idx = 1; idx <= replacements.length; idx++){
           fullReplacements[idx] = replacements[idx - 1];
        }
        consoleError(key, fullReplacements);
    }
    public void consoleError(MessageKey key, String[] replacements){
        consoleError(getMessage(key, replacements));
    }
    public void consoleError(MessageKey key){
        consoleError(getMessageString(key));
    }
    public void consoleError(String message){
        consoleLog(java.util.logging.Level.SEVERE, message);
    }

}