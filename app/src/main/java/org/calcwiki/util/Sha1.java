package org.calcwiki.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @author Haruue Icymoon haruue@caoyue.com.cn
 */
public class Sha1 {
    public static String sha1(String val) throws NoSuchAlgorithmException {
        MessageDigest sha1 = MessageDigest.getInstance("SHA-1");
        sha1.update(val.getBytes());
        byte[] m = sha1.digest();//加密
        return byte2String(m);
    }

    private static String byte2String(byte[] b) {
        StringBuilder sb = new StringBuilder();
        for (byte aB : b) {
            sb.append(aB);
        }
        return sb.toString();
    }
}
