package com.zhkrb.www.olddriverprotocol;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;


/**
 * Encrypt and decrypt messages using AES 256 bit encryption that are compatible with AESCrypt-ObjC and AESCrypt Ruby.
 * <p/>
 * Created by scottab on 04/10/2014.
 */
public final class AESCrypt {
    /*
	 * 生成密钥
	 */
    public static byte[] initKey(String s) throws Exception{
        String pwd = "odpodpodpodpodpodpodpodpodpodpodpodpodp";
        s += pwd;
        s = s.substring(0, 32);
        byte[] pwdbyte = s.getBytes();
        System.out.println(s);
        return pwdbyte;
    }

    /*
     * AES 加密
     */
    public static byte[] encrypt(byte[] data, byte[] key) throws Exception{
        SecretKey secretKey = new SecretKeySpec(key, "AES");

        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);
        byte[] cipherBytes = cipher.doFinal(data);
        return cipherBytes;
    }


    /*
     * AES 解密
     */
    public static byte[] decrypt(byte[] data, byte[] key) throws Exception{
        SecretKey secretKey = new SecretKeySpec(key, "AES");

        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, secretKey);
        byte[] plainBytes = cipher.doFinal(data);
        return plainBytes;
    }

}
