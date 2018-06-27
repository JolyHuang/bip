package com.sharingif.bips.bip0039;

import com.sharingif.bips.bip0039.wordlist.English;
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
    public void test() {
        English english = new English();

        String entropyStr = "741052b8f51c59dae6d36ab5c5942c1c";
        HexCoder hexCoder = new HexCoder();
        Entropy entropy = new Entropy(Locale.US,hexCoder.decode(entropyStr));
        String mnemonic = new Mnemonic(entropy).getMnemonic();
        System.out.println(mnemonic);

        Mnemonic mnemonicObj = new Mnemonic(Locale.US, mnemonic);
        String entropyStr2 = hexCoder.encode(mnemonicObj.getEntropy().getEntropy());
        System.out.println(entropyStr2);

        System.out.println(entropyStr.equals(entropyStr2));
    }

}
