package com.sharingif.bips.bip0032;

import com.sharingif.bips.bip0032.wordlist.English;
import com.sharingif.cube.security.binary.HexCoder;
import org.junit.Test;

import java.util.Locale;

/**
 * MnemonicTest
 *
 * @author Joly
 * @version v1.0
 * @since v1.0
 * 2018/6/27 下午10:51
 */
public class RootSeedTest {

    @Test
    public void rootSeed() {
        English english = new English();
        HexCoder hexCoder = new HexCoder();

        Mnemonic mnemonic = new Mnemonic(Locale.US, "alpha clinic guard apart cabin powder same stool focus useful raccoon erupt");

        System.out.println(hexCoder.encode(mnemonic.getRootSeed().getRootSeed()));

    }

}
