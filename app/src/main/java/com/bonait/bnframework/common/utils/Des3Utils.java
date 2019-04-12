package com.bonait.bnframework.common.utils;


import android.text.TextUtils;
import android.util.Base64;

import java.security.Key;

import javax.crypto.Cipher;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESedeKeySpec;
import javax.crypto.spec.IvParameterSpec;

/**
 * Created by LY on 2019/4/10.
 * 3DES 加密/解密
 */
public class Des3Utils {

    // 密钥 长度等于24，且不得小于24
    private final static String secretKey = "CW0Y&S7l1BVU2zwKop@syT92";
    // 向量 可有可无 终端后台也要约定
    private final static String iv = "01234567";

    /**
     * 3DES转变
     * <p>法算法名称/加密模式/填充方式</p>
     * <p>加密模式有：电子密码本模式ECB、加密块链模式CBC、加密反馈模式CFB、输出反馈模式OFB</p>
     * <p>填充方式有：NoPadding、ZerosPadding、PKCS5Padding</p>
     */
    private static String TripleDES_Transformation = "desede/CBC/PKCS5Padding";

    // 加密算法类型
    private static final String TripleDES_Algorithm = "desede";

    // 加解密统一使用的编码方式
    private final static String encoding = "utf-8";

    /**
     * 3DES加密
     *
     * @param plainText 普通文本
     * @return 加密Str
     */
    public static String encode(String plainText) {
        if (TextUtils.isEmpty(plainText)) {
            return null;
        }

        try {
            DESedeKeySpec spec = new DESedeKeySpec(secretKey.getBytes());
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(TripleDES_Algorithm);
            Key desKey = keyFactory.generateSecret(spec);

            Cipher cipher = Cipher.getInstance(TripleDES_Transformation);
            IvParameterSpec ips = new IvParameterSpec(iv.getBytes());
            cipher.init(Cipher.ENCRYPT_MODE, desKey, ips);

            byte[] encryptData = cipher.doFinal(plainText.getBytes(encoding));

            return Base64.encodeToString(encryptData, Base64.DEFAULT);

        }catch (Exception e ) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 3DES解密
     *
     * @param encryptText 加密文本
     * @return 解密Str
     */
    public static String decode(String encryptText) {
        if (TextUtils.isEmpty(encryptText)) {
            return null;
        }

        try {
            DESedeKeySpec spec = new DESedeKeySpec(secretKey.getBytes());
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(TripleDES_Algorithm);
            Key desKey = keyFactory.generateSecret(spec);

            Cipher cipher = Cipher.getInstance(TripleDES_Transformation);
            IvParameterSpec ips = new IvParameterSpec(iv.getBytes());
            cipher.init(Cipher.DECRYPT_MODE, desKey, ips);

            byte[] decryptData = cipher.doFinal(Base64.decode(encryptText, Base64.DEFAULT));

            return new String(decryptData, encoding);

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
