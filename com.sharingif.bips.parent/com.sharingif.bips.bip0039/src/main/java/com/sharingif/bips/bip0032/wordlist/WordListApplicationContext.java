package com.sharingif.bips.bip0032.wordlist;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * WordListApplicationContext
 *
 * @author Joly
 * @version v1.0
 * @since v1.0
 * 2018/6/27 下午5:39
 */
public class WordListApplicationContext {
    public static Map<String, WordList> WordListMap;

    static {
        WordListMap = new HashMap<String, WordList>();
        WordListApplicationContext.registWordList(Locale.US, new English());
    }


    public static void registWordList(Locale locale, WordList wordList) {
        WordListMap.put(locale.toString(), wordList);
    }

    public static WordList getWord(Locale locale) {
        return WordListMap.get(locale.toString());
    }

}
