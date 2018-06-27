package com.sharingif.bips.bip0039.wordlist;

import java.util.Locale;

/**
 * English
 *
 * @author Joly
 * @version v1.0
 * @since v1.0
 * 2018/6/27 下午5:20
 */
public class English extends AbstractWordList {

    static {
        WordListApplicationContext.registWordList(Locale.US, new English());
    }

    @Override
    protected String getFileName() {
        return "WordList_en_US.properties";
    }
}
