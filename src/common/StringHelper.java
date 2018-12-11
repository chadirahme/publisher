
import java.math.BigDecimal;
import java.text.Normalizer;
import java.text.SimpleDateFormat;
import java.text.Normalizer.Form;
import java.util.Date;
import java.util.regex.Pattern;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;

public class StringHelper {
    public static char DEFAULT_PAD_CHAR = 32;

    private StringHelper() {
    }

    public static String removeString(String mainString, String tag) {
        return replaceAllStrings(mainString, tag, "", 0, mainString.length());
    }

    public static String replaceString(String mainString, String tag, String value) {
        return replaceString(mainString, tag, value, 0, mainString.length());
    }

    public static String replaceString(String mainString, String tag, String value, int startPos) {
        return replaceString(mainString, tag, value, startPos, mainString.length());
    }

    public static String replaceString(String mainString, String tag, String value, int startPos, int endPos) {
        StringBuffer retVal = null;
        int mainLen = mainString.length();
        int tagLen = tag.length();
        int tagStart = findString(mainString, tag, startPos, endPos);
        if(tagStart != -1) {
            retVal = new StringBuffer(mainLen + tagLen);
            retVal.append(mainString.substring(0, tagStart));
            retVal.append(value);
            retVal.append(mainString.substring(tagStart + tagLen));
        }

        return retVal == null?mainString:retVal.toString();
    }

    public static String replaceAllStrings(String mainString, String tag, String value) {
        return replaceAllStrings(mainString, tag, value, 0, mainString.length());
    }

    public static String replaceAllStrings(String mainString, String tag, String value, int startPos) {
        return replaceAllStrings(mainString, tag, value, startPos, mainString.length());
    }

    public static String replaceAllStrings(String mainString, String tag, String value, int startPos, int endPos) {
        StringBuffer retVal = null;
        int mainLen = mainString.length();
        int tagLen = tag.length();
        int tagStart = findString(mainString, tag, startPos, endPos);
        int startingChar = 0;

        while(tagStart != -1) {
            if(retVal == null) {
                retVal = new StringBuffer(mainLen + tagLen);
            }

            retVal.append(mainString.substring(startingChar, tagStart));
            retVal.append(value);
            startingChar = tagStart + tagLen;
            tagStart = findString(mainString, tag, startingChar, endPos);
            if(tagStart == -1) {
                retVal.append(mainString.substring(startingChar));
            }
        }

        if(retVal == null) {
            return mainString;
        } else {
            return retVal.toString();
        }
    }

    public static String replaceChar(String mainString, char tag, char value) {
        return replaceChar(mainString, tag, value, 0, mainString.length());
    }

    public static String replaceChar(String mainString, char tag, char value, int startPos) {
        return replaceChar(mainString, tag, value, startPos, mainString.length());
    }

    public static String replaceChar(String mainString, char tag, char value, int startPos, int endPos) {
        StringBuffer retVal = null;
        int mainLen = mainString.length();
        int tagStart = findChar(mainString, tag, startPos, endPos);
        if(tagStart != -1) {
            retVal = new StringBuffer(mainLen);
            retVal.append(mainString.substring(0, tagStart));
            retVal.append(value);
            retVal.append(mainString.substring(tagStart + 1));
        }

        return retVal == null?mainString:retVal.toString();
    }

    public static String replaceAllChars(String mainString, char tag, char value) {
        return replaceAllChars(mainString, tag, value, 0, mainString.length());
    }

    public static String replaceAllChars(String mainString, char tag, char value, int startPos) {
        return replaceAllChars(mainString, tag, value, startPos, mainString.length());
    }

    public static String replaceAllChars(String mainString, char tag, char value, int startPos, int endPos) {
        StringBuffer retVal = null;
        int mainLen = mainString.length();
        int tagStart = findChar(mainString, tag, startPos, endPos);
        int startingChar = 0;

        while(tagStart != -1) {
            if(retVal == null) {
                retVal = new StringBuffer(mainLen);
            }

            retVal.append(mainString.substring(startingChar, tagStart));
            retVal.append(value);
            startingChar = tagStart + 1;
            tagStart = findChar(mainString, tag, startingChar, endPos);
            if(tagStart == -1) {
                retVal.append(mainString.substring(startingChar));
            }
        }

        if(retVal == null) {
            return mainString;
        } else {
            return retVal.toString();
        }
    }

    public static int countString(String mainString, String tag) {
        return countString(mainString, tag, 0, mainString.length());
    }

    public static int countString(String mainString, String tag, int startPos) {
        return countString(mainString, tag, startPos, mainString.length());
    }

    public static int countString(String mainString, String tag, int startPos, int endPos) {
        int count = 0;
        int tagStart = findString(mainString, tag, startPos, endPos);

        for(int tagLen = tag.length(); tagStart != -1 && tagStart <= endPos; tagStart = findString(mainString, tag, tagStart + tagLen, endPos)) {
            ++count;
        }

        return count;
    }

    public static int countChar(String mainString, char tag) {
        return countChar(mainString, tag, 0, mainString.length());
    }

    public static int countChar(String mainString, char tag, int startPos) {
        return countChar(mainString, tag, startPos, mainString.length());
    }

    public static int countChar(String mainString, char tag, int startPos, int endPos) {
        int count = 0;

        for(int tagStart = findChar(mainString, tag, startPos, endPos); tagStart != -1 && tagStart <= endPos; tagStart = findChar(mainString, tag, tagStart + 1, endPos)) {
            ++count;
        }

        return count;
    }

    public static String padRight(String mainString, int size) {
        return padRight(mainString, size, DEFAULT_PAD_CHAR);
    }

    public static String padRight(String mainString, int size, char padChar) {
        if(size < 0) {
            size = 0;
        }

        if(mainString.length() == size) {
            return mainString;
        } else if(mainString.length() > size) {
            return mainString.substring(0, size);
        } else {
            int spacer = size - mainString.length();
            StringBuffer retVal = new StringBuffer(mainString);
            retVal.append(getPadString(spacer, padChar));
            return retVal.toString();
        }
    }

    public static String padLeft(String mainString, int size) {
        return padLeft(mainString, size, DEFAULT_PAD_CHAR);
    }

    public static String padLeft(String mainString, int size, char padChar) {
        if(size < 0) {
            size = 0;
        }

        if(mainString.length() == size) {
            return mainString;
        } else if(mainString.length() > size) {
            return mainString.substring(0, size);
        } else {
            int spacer = size - mainString.length();
            StringBuffer retVal = new StringBuffer(getPadString(spacer, padChar));
            retVal.append(mainString);
            return retVal.toString();
        }
    }

    public static String getPadString(int padCount) {
        return getPadString(padCount, DEFAULT_PAD_CHAR);
    }

    public static String getPadString(int padCount, char padChar) {
        StringBuffer retVal = new StringBuffer(padCount);

        for(int x = 0; x < padCount; ++x) {
            retVal.append(padChar);
        }

        return retVal.toString();
    }

    public static int findString(String mainString, String tag, int startPos, int endPos) {
        int retVal = mainString.indexOf(tag, startPos);
        if(retVal >= endPos) {
            retVal = -1;
        }

        return retVal;
    }

    public static int findChar(String mainString, char tag) {
        return findChar(mainString, tag, 0, mainString.length());
    }

    public static int findChar(String mainString, char tag, int startPos) {
        return findChar(mainString, tag, startPos, mainString.length());
    }

    public static int findChar(String mainString, char tag, int startPos, int endPos) {
        int retVal = mainString.indexOf(tag, startPos);
        if(retVal >= endPos) {
            retVal = -1;
        }

        return retVal;
    }

    public static String reverse(String mainString) {
        if(mainString != null && mainString.length() >= 2) {
            StringBuffer buffer = new StringBuffer(mainString.length());

            for(int x = mainString.length(); x > 0; --x) {
                buffer.append(mainString.substring(x - 1, x));
            }

            return buffer.toString();
        } else {
            return mainString;
        }
    }

    public static boolean equalsIgnoreCaseAndBlank(String str1, String str2) {
        return StringUtils.isBlank(str1) && StringUtils.isBlank(str2) || StringUtils.equalsIgnoreCase(str1, str2);
    }

    public static String getValue(String inputString, String defaultString) {
        return inputString == null?defaultString:inputString;
    }

    public static String getTrimValue(String inputString, String defaultString) {
        return inputString == null?defaultString:inputString.trim();
    }

    public static String getValue(Integer inputInteger, String defaultString) {
        return inputInteger == null?defaultString:inputInteger.toString();
    }

    public static String getValue(Short inputShort, String defaultString) {
        return inputShort == null?defaultString:inputShort.toString();
    }

    public static String getValue(BigDecimal inputBigDecimal, String defaultString) {
        return inputBigDecimal == null?defaultString:inputBigDecimal.toString();
    }

    public static String getValue(Date inputDate, String pattern, String defaultString) {
        return inputDate == null?defaultString:(new SimpleDateFormat(pattern)).format(inputDate);
    }

    public static String stripAccents(String inputString) {
        if(inputString == null) {
            return null;
        } else {
            Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
            String decomposed = Normalizer.normalize(inputString, Form.NFD);
            return pattern.matcher(decomposed).replaceAll("");
        }
    }

    public static String escapeHTMLText(String escapeHTMLText) {
        return StringEscapeUtils.escapeHtml(StringEscapeUtils.unescapeHtml(escapeHTMLText));
    }
}
