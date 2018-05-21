package com.hexun.zh.datafilter.common.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * emoji 表情过滤
 *
 * @author zhoudong
 */
public class EmojiFilter {
    public static Object filterEmoji(Object source) {
        if (StringUtils.isNotBlank(source)) {
            String str = String.valueOf(source);

            // 1、 过滤特殊字符
            String regEx="[`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}]";
            Pattern p = Pattern.compile(regEx);
            Matcher m = p.matcher(str);
            str = m.replaceAll("").trim();

            str.replaceAll("\\\\","");

            // 2、过滤emoji
            Pattern emoji = Pattern.compile("[\ud83c\udc00-\ud83c\udfff]|[\ud83d\udc00-\ud83d\udfff]|[\u2600-\u27ff]", Pattern.UNICODE_CASE | Pattern.CASE_INSENSITIVE);
            Matcher emojiMatcher = emoji.matcher(str);
            if (emojiMatcher.find()) {
                str = emojiMatcher.replaceAll("*");
                return str;
            }
            return str;
        }
        return source;
    }

    public static void main(String[] args) {
        String s = "有时候搜索\uD84C\uDFB4出来";
        System.out.println(filterEmoji(s));
    }
}
