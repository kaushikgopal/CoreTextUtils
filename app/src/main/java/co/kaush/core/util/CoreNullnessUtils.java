package co.kaush.core.util;

import java.util.List;
import org.jetbrains.annotations.Contract;

public class CoreNullnessUtils {

    public static <T> T firstNonNull(T... objs) {
        for (T obj : objs) {
            if (obj != null) {
                return obj;
            }
        }
        throw new NullPointerException();
    }

    @Contract("null -> false")
    public static <T> boolean isNotNull(T obj) {
        return obj != null;
    }

    @Contract("null -> true")
    public static <T> boolean isNull(T obj) {
        return !isNotNull(obj);
    }

    @Contract("null -> false")
    public static boolean isNotNullOrEmpty(List list) {
        return !isNullOrEmpty(list);
    }

    @Contract("null -> true")
    public static boolean isNullOrEmpty(List list) {
        return list == null || list.isEmpty();
    }

    @Contract("null -> true")
    public static boolean isNullOrEmpty(String string) {
        return string == null || string.length() == 0; // string.isEmpty() in Java 6
    }

    @Contract("null -> false")
    public static boolean isNotNullOrEmpty(String string) {
        return !isNullOrEmpty(string);
    }

    public static String firstNonNull(String... objs) {
        for (String obj : objs) {
            if (obj != null) {
                return obj;
            }
        }
        throw new NullPointerException();
    }
}