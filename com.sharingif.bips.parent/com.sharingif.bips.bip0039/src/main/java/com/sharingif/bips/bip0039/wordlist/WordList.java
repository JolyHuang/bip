package com.sharingif.bips.bip0039.wordlist;

/**
 * WordList
 *
 * @author Joly
 * @version v1.0
 * @since v1.0
 * 2018/6/27 下午3:48
 */
public interface WordList {

    /**
     * 根据位置获得单词
     * @param index
     * @return
     */
    String getWord(final int index);

    /**
     * 根据单词获得位置
     * @param word
     * @return
     */
    int getIndex(final String word);

    /**
     * 获得语言对应的空格
     * @return
     */
    String getSpace();

}
