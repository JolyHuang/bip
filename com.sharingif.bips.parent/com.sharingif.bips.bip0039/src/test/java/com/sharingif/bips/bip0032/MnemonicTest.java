package com.sharingif.bips.bip0032;

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

        Mnemonic mnemonic = new Mnemonic(Locale.US, 12);

        System.out.println(mnemonic.getMnemonic());


    }


}
