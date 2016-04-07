package co.kaush.core.util;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.text.NumberFormat;
import java.text.ParsePosition;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class CoreTextUtils {

    public static final Charset UTF_8 = Charset.forName("UTF-8");

    // -----------------------------------------------------------------------------------
    // checks

    public static boolean isNumeric(String str) {
        NumberFormat formatter = NumberFormat.getInstance();
        ParsePosition pos = new ParsePosition(0);
        formatter.parse(str, pos);
        return str.length() == pos.getIndex();
    }

    // -----------------------------------------------------------------------------------
    // manipulation

    public static String replaceAllTextInString(String source, String from, String to) {
        return _replaceAllTextInString(new StringBuilder(source), from, to).toString();
    }

    public static String replaceAllTextInString(String sourceText,
          HashMap<String, String> mapOfAttributesToBeReplaced) {
        StringBuilder sourceTextBuilder = new StringBuilder(sourceText);
        for (Map.Entry<String, String> entry : mapOfAttributesToBeReplaced.entrySet()) {
            _replaceAllTextInString(sourceTextBuilder, entry.getKey(), entry.getValue());
        }
        return sourceTextBuilder.toString();
    }

    public static String initCapitalize(String word) {
        if (CoreNullnessUtils.isNullOrEmpty(word)) {
            return "";
        }

        return Character.toUpperCase(word.charAt(0)) + word.substring(1);
    }

    public static String truncate(CharSequence seq, int maxLength, String truncationIndicator) {
        if (CoreNullnessUtils.isNull(seq) || CoreNullnessUtils.isNullOrEmpty(seq.toString())) {
            return "";
        }

        // length to truncate the sequence to, not including the truncation indicator
        int truncationLength = maxLength - truncationIndicator.length();

        // in this worst case, this allows a maxLength equal to the length of the truncationIndicator,
        // meaning that a string will be truncated to just the truncation indicator itself
        if (truncationLength < 0) {
            return "";
        }

        if (seq.length() <= maxLength) {
            String string = seq.toString();
            if (string.length() <= maxLength) {
                return string;
            }
            // if the length of the toString() result was > maxLength for some reason, truncate that
            seq = string;
        }

        return new StringBuilder(maxLength).append(seq, 0, truncationLength).append(truncationIndicator).toString();
    }

    // -----------------------------------------------------------------------------------
    // special

    public static String toJavaMethodName(final String nonJavaMethodName) {
        final StringBuilder nameBuilder = new StringBuilder();
        boolean capitalizeNextChar = false;
        boolean first = true;

        for (int i = 0; i < nonJavaMethodName.length(); i++) {
            final char c = nonJavaMethodName.charAt(i);
            if (!Character.isLetterOrDigit(c)) {
                if (!first) {
                    capitalizeNextChar = true;
                }
            } else {
                nameBuilder.append(capitalizeNextChar ? Character.toUpperCase(c) : Character.toLowerCase(c));
                capitalizeNextChar = false;
                first = false;
            }
        }
        return nameBuilder.toString();
    }

    // -----------------------------------------------------------------------------------
    // Joiners

    public static String joinMap(String entrySeparator, String mapKeyValueSeperator, HashMap<String, String> map) {

        StringBuilder stringBuilder = new StringBuilder();
        Set<String> keySet = map.keySet();

        for (String key : keySet) {

            Object valueObject = map.get(key);
            if (CoreNullnessUtils.isNull(valueObject)) {
                continue;
            }

            String value = valueObject.toString();
            if (stringBuilder.length() > 0) {
                stringBuilder.append(entrySeparator);
            }

            try {
                stringBuilder.append((key != null ? java.net.URLEncoder.encode(key, UTF_8.name()) : ""));
                stringBuilder.append(mapKeyValueSeperator);
                stringBuilder.append(value != null ? java.net.URLEncoder.encode(value, UTF_8.name()) : "");
            } catch (UnsupportedEncodingException e) {
                // ("Exception Occurred when trying to join strings of a map", e);
                return "";
            }
        }

        return stringBuilder.toString();
    }

    public static String join(String separator, final Iterable objects) {
        if (objects == null) {
            return "";
        }

        int numOfObjects = ((Collection<?>) objects).size();
        if (numOfObjects == 0) {
            return "";
        }

        separator = CoreNullnessUtils.firstNonNull(separator, "");

        final StringBuilder result = new StringBuilder(numOfObjects * 16);

        for (Object other : objects) {
            if (other == null || CoreNullnessUtils.isNullOrEmpty(other.toString())) {
                continue;
            }
            result.append(other.toString()).append(separator);
        }

        if (result.length() == 0) {
            return "";
        }

        result.replace(result.length() - separator.length(), result.length(), "");

        return result.toString();
    }

    public static String join(String separator, final String... strings) {
        Iterable<String> iterable = Arrays.asList(strings);
        return join(separator, iterable);
    }

    // -----------------------------------------------------------------------------------
    // Splitter

    public static String splitCamelCase(String s) {
        return s.replaceAll(String.format("%s|%s|%s",
              "(?<=[A-Z])(?=[A-Z][a-z])",
              "(?<=[^A-Z])(?=[A-Z])",
              "(?<=[A-Za-z])(?=[^A-Za-z])"), " ");
    }

    public static List<String> split(String separator, String stringThatNeedsSplitting) {
        return split(separator, stringThatNeedsSplitting, false);
    }

    public static List<String> split(String separator, String stringThatNeedsSplitting, boolean keepOrder) {
        List<String> result = new ArrayList<>();

        if (CoreNullnessUtils.isNullOrEmpty(stringThatNeedsSplitting)) {
            return result;
        }

        if (!stringThatNeedsSplitting.contains(separator)) {
            result.add(stringThatNeedsSplitting);
            return result;
        }

        String delimiter = "[" + separator + "]+";
        result = Arrays.asList(stringThatNeedsSplitting.split(delimiter));

        if (CoreNullnessUtils.isNotNullOrEmpty(result) && CoreNullnessUtils.isNullOrEmpty(result.get(0))) {
            ArrayList<String> tmp = new ArrayList<>(result);
            tmp.remove(0);
            result = tmp;
        }

        if (keepOrder) {
            Collections.reverse(result);
        }

        return result;
    }

    // -----------------------------------------------------------------------------------

    private static StringBuilder _replaceAllTextInString(StringBuilder sourceStringBuilder, String from, String to) {
        int index = sourceStringBuilder.indexOf(from);
        while (index != -1) {
            sourceStringBuilder.replace(index, index + from.length(), to);
            index += to.length(); // Move to the end of the replacement
            index = sourceStringBuilder.indexOf(from, index);
        }
        return sourceStringBuilder;
    }
}
