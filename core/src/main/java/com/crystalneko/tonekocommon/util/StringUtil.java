package com.crystalneko.tonekocommon.util;

import java.util.concurrent.*;
import java.util.logging.Logger;

public class StringUtil {
    private static final Logger logger = Logger.getLogger("StringUtil");

    // 获取字符串中含有某个特定字符串的个数
    public static int getCount(String str, String subStr) {
        if (str == null || subStr == null || str.isEmpty() || subStr.isEmpty()) {
            return 0;
        }
        int count = 0;
        int index = 0;
        while ((index = str.indexOf(subStr, index)) != -1) {
            count++;
            index += subStr.length();
        }
        return count;
    }

    private static ExecutorService regexExecutor = Executors.newSingleThreadExecutor();
    public static String timeLimitedReplaceAll(String text, String regex, String replacement){
        Future<String> future = regexExecutor.submit(() -> {
            return text.replaceAll(regex, replacement);
        });
        try {
            return future.get(150, TimeUnit.MILLISECONDS);
        } catch (TimeoutException ex0){
            var ex = new RuntimeException("String replace taken too long time to execute", ex0);
            logger.log(java.util.logging.Level.WARNING, "The regex has take too long to execute! Executing: /" + regex + "/" + replacement +"/g, ", ex);
            future.cancel(true);
        } catch (InterruptedException | ExecutionException ignored){
            // nothing to do
        }
        return "";
    }
}

