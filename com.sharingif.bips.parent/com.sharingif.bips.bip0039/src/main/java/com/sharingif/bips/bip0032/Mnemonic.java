package com.sharingif.bips.bip0032;

import com.sharingif.bips.bip0032.wordlist.WordList;
import com.sharingif.bips.bip0032.wordlist.WordListApplicationContext;
import com.sharingif.cube.security.confidentiality.encrypt.digest.SHA256Encryptor;

import java.text.Normalizer;
import java.util.*;

/**
 * Mnemonic
 *
 * @author Joly
 * @version v1.0
 * @since v1.0
 * 2018/6/27 上午11:50
 */
public class Mnemonic {

    public static final int MNEMONIC_LENGTH_12 = 12;
    public static final int MNEMONIC_LENGTH_24 = 24;

    private Locale locale;
    private String mnemonic;
    private Entropy entropy;
    private RootSeed rootSeed;

    public Mnemonic(Locale locale, int length) {
        this(locale, new Entropy((length*11)-((length*11)%Entropy.ENTROPY_UNIT)));
    }

    public Mnemonic(Locale locale, Entropy entropy) {
        this.locale = locale;
        // 1.创造一个128到256位的随机顺序（熵）
        this.entropy = entropy;

        // 2.取熵哈希后的前n位作为校验和(n= 熵长度/32)
        boolean[] checksum = checksum(entropy);

        // 3.把校验和加在随机顺序的后面
        boolean[] bits = bits(entropy, checksum);

        // 4.把步骤三得到的结果每 11 位切割
        int[] mnemonicIndex = mnemonicIndex(bits);

        // 5.步骤四得到的每 11 位字节匹配词库的一个词
        WordList wordList = WordListApplicationContext.getWord(locale);
        String[] mnemonicList = mnemonicList(wordList, mnemonicIndex);

        // 6.拼接助记词
        this.mnemonic = mnemonic(wordList, mnemonicList);
    }

    public Mnemonic(Locale locale, String mnemonic) {
        mnemonic = Normalizer.normalize(mnemonic, Normalizer.Form.NFKD);

        this.locale = locale;
        this.mnemonic = mnemonic;

    }


    public String getMnemonic() {
        return mnemonic;
    }

    public Entropy getEntropy() {
        if(this.getEntropy() != null) {
            return this.entropy;
        }

        WordList wordList = WordListApplicationContext.getWord(this.locale);
        StringTokenizer tokenizer = new StringTokenizer (this.mnemonic, wordList.getSpace());
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

        this.entropy = new Entropy(data);

        return this.entropy;
    }

    public RootSeed getRootSeed() {

        if(this.rootSeed == null) {
            this.rootSeed = new RootSeed(this, "");
        }

        return rootSeed;
    }

    protected boolean[] checksum(Entropy entropy) {
        int checksumLength = entropy.getLength() / Entropy.ENTROPY_UNIT;
        byte[] sha256Entropy = new SHA256Encryptor().encrypt(entropy.getEntropy());

        boolean[] checksum = new boolean[checksumLength];

        for (int i = 0; i < checksumLength; i++) {
            checksum[i] = (sha256Entropy[i / 8] & (1 << (7 - (i % 8)))) > 0;
        }

        return checksum;
    }

    protected boolean[] bits(Entropy entropy, boolean[] checksum) {
        boolean[] entropyBoolean = new boolean[entropy.getLength()];
        for (int i = 0; i < entropy.getLength()/8; i++) {
            for (int j = 0; j < 8; j++) {
                entropyBoolean[8 * i + j] = (entropy.getEntropy()[i] & (1 << (7 - j))) > 0;
            }
        }

        boolean[] bits = new boolean[entropyBoolean.length+checksum.length];
        System.arraycopy(entropyBoolean, 0, bits, 0, entropyBoolean.length);
        System.arraycopy(checksum, 0, bits, entropyBoolean.length, checksum.length);

        return bits;
    }

    protected int[] mnemonicIndex(boolean[] bits) {
        int mnemonicLength = bits.length/11;

        int[] mnemonicIndex = new int[mnemonicLength];
        for(int i = 0; i < mnemonicLength; i++) {
            int idx = 0;
            for ( int j = 0; j < 11; j++ ) {
                idx += (bits[i * 11 + j] ? 1 : 0) << (10 - j);
            }
            mnemonicIndex[i] = idx;
        }

        return mnemonicIndex;
    }

    protected String[] mnemonicList(WordList wordList, int[] mnemonicIndex) {
        String[] mnemonicList = new String[mnemonicIndex.length];
        for(int i = 0; i < mnemonicList.length; i++) {
            mnemonicList[i] = wordList.getWord(mnemonicIndex[i]);
        }

        return mnemonicList;
    }

    protected String mnemonic(WordList wordList, String[] mnemonicList) {
        StringBuilder mnemonic = new StringBuilder(mnemonicList[0]);
        for(int i=1; i< mnemonicList.length; i++) {
            mnemonic.append(wordList.getSpace());
            mnemonic.append(mnemonicList[i]);
        }

        return mnemonic.toString();
    }

}
