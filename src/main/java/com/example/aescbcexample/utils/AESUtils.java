package com.example.aescbcexample.utils;

/**
 * @author guowf
 * @mail guowf_buaa@163.com
 * @description:
 * @data created in 2019-09-04 20:25
 */
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import com.alibaba.fastjson.JSON;
import com.example.aescbcexample.domain.LData;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Hex;
public class AESUtils {

    private static final Charset ASCII = Charset.forName("US-ASCII");
    private static final String AES_KEY = "aes_key_16_bytes";
    private static final String AES_MODE = "AES/CBC/PKCS5PADDING";

    public static void main(String[] args) throws Exception {

        List<String> list = new ArrayList<>();
        list.add("hello");

        LData lData = new LData();
        lData.setStatus("ok");
        lData.setList(list);

        String jsonString = JSON.toJSONString(lData);
//        System.out.println(xxx);

        String base64Cipher = encrypted(jsonString);

//        String base64Cipher = "Y7dJKaXGgrOAAJjiFxqQv8YcQ8ntcnivQwGxOCS88KXg0drfOgbZ1OKnOINDcQA7";
        LData result = decrypted(base64Cipher, LData.class);
        System.out.println(result.toString());
    }

    public static String encrypted(String target) throws Exception{
        byte[] keyBytes = AES_KEY.getBytes(ASCII);
        SecretKey secretKey = new SecretKeySpec(keyBytes, "AES");
        Cipher cipher = Cipher.getInstance(AES_MODE);

        byte[] iv = AES_KEY.getBytes(ASCII);
        IvParameterSpec ivParameterSpec = new IvParameterSpec(iv);
        cipher.init(Cipher.ENCRYPT_MODE, secretKey, ivParameterSpec);

        target = AES_KEY + target;

        byte[] decrypted = cipher.doFinal(target.getBytes());

        return Base64.encodeBase64String(decrypted);
    }

    public static <T> T decrypted(String base64Cipher, Class<T> clazz) throws Exception {
        byte[] cipherBytes = Base64.decodeBase64(base64Cipher);
        byte[] iv = AES_KEY.getBytes(ASCII);
        byte[] keyBytes = AES_KEY.getBytes(ASCII);
        SecretKey secretKey = new SecretKeySpec(keyBytes, "AES");
        IvParameterSpec ivParameterSpec = new IvParameterSpec(iv);

        Cipher cipher = Cipher.getInstance(AES_MODE);
        cipher.init(Cipher.DECRYPT_MODE, secretKey, ivParameterSpec);

        byte[] result = cipher.doFinal(cipherBytes);
        String plainText = new String(Arrays.copyOfRange(result, 16, result.length));
        System.out.println(plainText);

        return JSON.parseObject(plainText, clazz);
    }

}
