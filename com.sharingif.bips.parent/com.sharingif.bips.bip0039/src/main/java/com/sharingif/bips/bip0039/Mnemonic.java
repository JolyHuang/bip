package com.sharingif.bips.bip0039;

import com.sharingif.bips.bip0039.wordlist.WordList;
import com.sharingif.bips.bip0039.wordlist.WordListApplicationContext;
import com.sharingif.cube.security.confidentiality.encrypt.digest.SHA256Encryptor;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.StringTokenizer;

/**
 * Mnemonic
 *
 * @author Joly
 * @version v1.0
 * @since v1.0
 * 2018/6/27 上午11:50
 */
public class Mnemonic {

    private String mnemonic;
    private Entropy entropy;

    public Mnemonic(Entropy entropy) {

        // 2.取熵哈希后的前n位作为校验和(n= 熵长度/32)
        byte[] sha256Entropy = new SHA256Encryptor().encrypt(entropy.getEntropy());
        // 校验符长度
        int checksumLength = entropy.getLength() / Entropy.ENTROPY_UNIT;

        // 3.把校验和加在随机顺序的后面
        boolean[] bits = new boolean[entropy.getLength() + checksumLength];
        for (int i = 0; i < entropy.getLength()/8; i++) {
            for (int j = 0; j < 8; j++) {
                bits[8 * i + j] = (entropy.getEntropy()[i] & (1 << (7 - j))) > 0;
            }
        }
        for (int i = 0; i < checksumLength; i++) {
            bits[entropy.getLength() + i] = (sha256Entropy[i / 8] & (1 << (7 - (i % 8)))) > 0;
        }

        // 4.把步骤三得到的结果每 11 位切割
        int mnemonicLength = bits.length/11;
        List<Integer> mnemonicIndexList = new ArrayList<Integer>(mnemonicLength);
        for ( int i = 0; i < mnemonicLength; i++ ) {
            int idx = 0;
            for ( int j = 0; j < 11; j++ ) {
                idx += (bits[i * 11 + j] ? 1 : 0) << (10 - j);
            }
            mnemonicIndexList.add(idx);
        }

        // 5.步骤四得到的每 11 位字节匹配词库的一个词
        WordList wordList = WordListApplicationContext.getWord(entropy.getLocale());
        List<String> mnemonicList = new ArrayList<>(mnemonicLength);
        for(int index : mnemonicIndexList) {
            mnemonicList.add(wordList.getWord(index));
        }

        // 6.拼接助记词
        StringBuilder mnemonic = new StringBuilder(mnemonicList.get(0));
        for(int i=1; i< mnemonicLength; i++) {
            mnemonic.append(wordList.getSpace());
            mnemonic.append(mnemonicList.get(i));
        }

        this.mnemonic = mnemonic.toString();
    }

    public Mnemonic(Locale locale, String mnemonic) {
        this.mnemonic = mnemonic;
        WordList wordList = WordListApplicationContext.getWord(locale);

        StringTokenizer tokenizer = new StringTokenizer (mnemonic, wordList.getSpace());

        boolean[] bits = new boolean[11 * tokenizer.countTokens()];

        int i = 0;
        while(tokenizer.hasMoreElements()) {
            int c = wordList.getIndex(tokenizer.nextToken());
            for (int j = 0; j < 11; ++j ) {
                bits[i++] = (c & (1 << (10 - j))) > 0;
            }
        }
        byte[] data = new byte[bits.length / 33 * 4];
        for ( i = 0; i < bits.length / 33 * 32; ++i ) {
            data[i / 8] |= (bits[i] ? 1 : 0) << (7 - (i % 8));
        }

        this.entropy = new Entropy(locale, data);
    }

    public String getMnemonic() {
        return mnemonic;
    }

    public Entropy getEntropy() {
        return entropy;
    }

}
