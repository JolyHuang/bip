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
public class MnemonicTest {

    @Test
    public void mnemonic() {


        Entropy entropy = new Entropy(new HexCoder().decode("3ee74309d55715669d2649bbf63b9ab9"));
        System.out.println(new HexCoder().encode(entropy.getEntropy()));

        Mnemonic mnemonic = new Mnemonic(Locale.US, entropy);

        System.out.println(mnemonic.getMnemonic());

    }

}
