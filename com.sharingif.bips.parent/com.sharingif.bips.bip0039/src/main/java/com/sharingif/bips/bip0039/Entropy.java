package com.sharingif.bips.bip0039;

import com.sharingif.cube.core.exception.validation.ValidationCubeException;

import java.security.SecureRandom;
import java.util.Locale;

/**
 * Entropy
 *
 * @author Joly
 * @version v1.0
 * @since v1.0
 * 2018/6/27 下午7:03
 */
public class Entropy {

    /**
     * 熵的最小值
     */
    public static final int ENTROPY_MINIMUM_VALUE = 128;
    /**
     * 熵的最大值
     */
    public static final int ENTROPY_MAXIMUM_VALUE = 256;
    /**
     * 熵的单位
     */
    public static final int ENTROPY_UNIT = 32;

    private int length;
    private byte[] entropy;
    private Locale locale;

    public Entropy() {
        this(Locale.US, ENTROPY_MINIMUM_VALUE);
    }

    public Entropy(Locale locale, int length) {
        this(locale, new SecureRandom().generateSeed(length/8));
    }

    public Entropy(Locale locale, byte[] entropy) {
        this.locale = locale;
        this.length = entropy.length*8;

        // 规定熵的位数必须是 32 的整数倍，所以熵的长度取值位 128 到 256 之间取 32 的整数倍的值，
        // 分别为 128, 160, 192, 224, 256；
        if (this.length < ENTROPY_MINIMUM_VALUE || this.length > ENTROPY_MAXIMUM_VALUE || ((this.length % ENTROPY_UNIT) != 0)) {
            throw new ValidationCubeException("Illegal length");
        }

        this.entropy = entropy;
    }

    public int getLength() {
        return length;
    }

    public byte[] getEntropy() {
        return entropy;
    }

    public Locale getLocale() {
        return locale;
    }
}
