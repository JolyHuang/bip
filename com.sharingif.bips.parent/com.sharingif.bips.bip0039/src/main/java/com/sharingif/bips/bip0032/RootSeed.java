package com.sharingif.bips.bip0032;

import com.sharingif.cube.core.exception.CubeRuntimeException;
import com.sharingif.cube.core.util.Charset;
import com.sharingif.cube.security.confidentiality.encrypt.HmacSHA512;
import com.sharingif.cube.security.confidentiality.encrypt.PBKDF2WithHmacSHA512;

import java.io.UnsupportedEncodingException;
import java.text.Normalizer;

/**
 * RootSeed
 *
 * @author Joly
 * @version v1.0
 * @since v1.0
 * 2018/6/28 下午4:53
 */
public class RootSeed {

    private static final byte[] MNEMONIC_SALT = getUtf8Bytes("mnemonic");
    private static final byte[] BITCOIN_SEED = getUtf8Bytes("Bitcoin seed");

    private byte[] rootSeed;
    private byte[] masterExtendedKey;

    public RootSeed(Mnemonic mnemonic, String passphrase) {
        String mnemonicStr  = Normalizer.normalize(mnemonic.getMnemonic(), Normalizer.Form.NFKD);
        passphrase = Normalizer.normalize(passphrase, Normalizer.Form.NFKD);
        final char[] mnemonicByte = mnemonicStr.toCharArray();
        final byte[] passphraseByte = getUtf8Bytes(passphrase);

        final byte[] salt = new byte[MNEMONIC_SALT.length + passphraseByte.length];
        System.arraycopy(MNEMONIC_SALT, 0, salt, 0, MNEMONIC_SALT.length);
        System.arraycopy(passphraseByte, 0, salt, MNEMONIC_SALT.length, passphraseByte.length);


        this.rootSeed = new PBKDF2WithHmacSHA512()
                .createSecretKey(mnemonicByte, salt, 2048, 512)
                .getEncoded();

    }

    public RootSeed(byte[] rootSeed) {
        this.rootSeed = rootSeed;
    }

    protected static byte[] getUtf8Bytes(String text) {
        try {
            return text.getBytes(Charset.UTF8.toString());
        } catch (UnsupportedEncodingException e) {
            throw new CubeRuntimeException("unsupported encoding error", e);
        }
    }

    protected char[] getUtf8Char(byte[] bytes) {
        return new String(bytes).toCharArray();
    }

    public byte[] getRootSeed() {
        return rootSeed;
    }

    public byte[] getMasterExtendedKey() {

        if(this.masterExtendedKey == null) {
            this.masterExtendedKey = new HmacSHA512(BITCOIN_SEED).doFinal(this.rootSeed);
        }

        return this.masterExtendedKey;
    }

}
