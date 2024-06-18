package io.github.silvigarabis.rplayneko.util;

import java.util.concurrent.*;
import java.util.logging.Logger;

public class StringUtil {
    private static final Logger logger = Logger.getLogger("StringUtil");

    private static ExecutorService regexExecutor = Executors.newSingleThreadExecutor();
    public static String timeLimitedReplaceAll(String text, String regex, String replacement){
        Future<String> future = regexExecutor.submit(() -> {
            return text.replaceAll(regex, replacement);
        });
        try {
            return future.get(50, TimeUnit.MILLISECONDS);
        } catch (TimeoutException ex0){
            var ex = new RuntimeException("String replace taken too long time to execute", ex0);
            logger.log(java.util.logging.Level.WARNING, "The regex has take too long to execute! Executing: /" + regex + "/" + replacement +"/g, ", ex);
            future.cancel(true);
        } catch (InterruptedException | ExecutionException ignored){
            // nothing to do
        }
        return "";
    }
    public static String timeLimitedReplace(String text, String regex, String replacement){
        Future<String> future = regexExecutor.submit(() -> {
            return text.replace(regex, replacement);
        });
        try {
            return future.get(50, TimeUnit.MILLISECONDS);
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

