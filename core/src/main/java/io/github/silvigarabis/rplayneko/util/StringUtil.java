package io.github.silvigarabis.rplayneko.util;

import java.util.concurrent.*;
import java.util.logging.Logger;
import java.util.*;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

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
        return text;
    }

    public static String format(String text, List<String> replacements){
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

    public static String format(String text, String... replacements){
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
}
