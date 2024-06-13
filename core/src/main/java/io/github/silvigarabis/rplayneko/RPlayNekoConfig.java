package io.github.silvigarabis.rplayneko;

import java.util.Locale;
import org.jetbrains.annotations.*;

public class RPlayNekoConfig {
    public static class InvalidConfigException extends IllegalStateException {
        public InvalidConfigException(String configKey, Object invalidValue){
            super(buildMessage(configKey, invalidValue));
            this.configKey = configKey;
            this.invalidValue = invalidValue;
        }
        public InvalidConfigException(String configKey){
            super("Invalid config for " + configKey);
            this.configKey = configKey;
            this.invalidValue = null;
        }
        private static String buildMessage(String configKey, Object invalidValue){
            String errorMessage = "Invalid config for " + configKey + ": ";

            if (invalidValue == null){
                errorMessage += "null";
            } else {
                try {
                    errorMessage += invalidValue.getClass().getMethod("toString").invoke(invalidValue);
                } catch (Throwable ignored){
                }
            }
            return errorMessage;
        }
        public final String configKey;
        public final @Nullable Object invalidValue;
    }

    public static enum RPlayNekoLanguage {
        zh_CN("zh_cn"), zh_cn("zh_cn"), zh("zh_cn"),
        en("en_us"), CUSTOM(true /* I wonder why I need it */);

        private RPlayNekoLanguage(boolean isCustom){
            this.isCustom = true;
            this.langName = null;
        }
        private RPlayNekoLanguage(@NotNull String langName){
            this.isCustom = false;
            this.langName = langName;
        }
        private final boolean isCustom;
        private final @Nullable String langName;
        public boolean isCustom(){
            return isCustom;
        }
        public @Nullable String langName(){
            return langName;
        }
    }

    public static enum ChatNyaLocation {
        END, HEAD, CUSTOM /* why this exists? */
    }

    public int configVersion = 1;
    public RPlayNekoLanguage language = RPlayNekoLanguage.zh;
    public boolean feature_Neko_AddSpeakReplace = true; // 猫娘可以修改自己的发言替换配置
    public boolean feature_Neko_RequestForOwnerWhenOwnered = true; // 猫娘可以在有主人时换主人
    public boolean feature_Neko_RequestForOwner = true; // 猫娘可以自己找主人
    public boolean feature_Neko_MultiOwner = false; // 多主人支持
    public boolean feature_Neko_CloseToOwner = true; // 猫娘会一直跟着主人（通过传送）

    public boolean feature_NekoTransform_SelfTransform = true; // 玩家可以自己变猫娘
    public boolean feature_NekoTransform_OtherTransform = true; // 其他玩家可以请求别人变猫娘
    public boolean feature_NekoTransform_OtherTransformSelfBack = true; // 被其他玩家变的猫娘可以自己变普通玩家

    public boolean feature_NekoOwner_ForcedMute = true; // 主人可以强行禁言猫娘
    public boolean feature_NekoOwner_AddSpeakReplace = true; // 主人可以修改猫娘的发言替换配置

    public boolean feature_ChatNya = true; // 启用口癖
    public ChatNyaLocation feature_ChatNya_Location = ChatNyaLocation.END;

    public boolean feature_SpeakReplace = true; // 启用发言替换
    public boolean feature_SpeakReplaceAlways = true; // 对所有玩家启用发言替换
    public boolean feature_SpeakReplace_InSign = true; // 启用对告示牌的发言替换
    public boolean feature_SpeakReplace_InAnvil = true; // 启用对铁砧的发言替换
    public boolean feature_SpeakReplace_Regex = true; // 启用正则表达式
    public int feature_SpeakReplace_MaxLength = 5; // 文本最大长度
    public int feature_SpeakReplace_MaxRegexLength = 20; // 正则表达式文本最大长度

    public boolean feature_NSFW = false; // 启用NSFW的内容（你懂的）
    public boolean feature_NSFW_Interact = true; // 找不到合适的英文了，就这样吧
    public boolean feature_NSFW_InteractWithBlock = true;
    public final int[] feature_NSFW_InteractXpRange = new int[]{-2, 3};

    public boolean feature_NekoAction = true;
    public boolean feature_NekoAction_Hiss = true;
    public boolean feature_NekoAction_Purr = true;
    public boolean feature_NekoAction_Lovebite = true;
    public boolean feature_NekoAction_Scratch = true;
    public int feature_NekoAction_Scratch_Damage = 1;

    public boolean feature_NekoPower_NightVision = true;
    public boolean feature_NekoPower_JumpBoost = true;

    public RPlayNekoConfig(int configVersion){
        if (configVersion != 1){
            throw new InvalidConfigException("configVersion", configVersion);
        }
    }
}
