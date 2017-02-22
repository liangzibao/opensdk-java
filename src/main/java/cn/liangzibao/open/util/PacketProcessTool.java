package cn.liangzibao.open.util;

import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import javax.crypto.Cipher;
import javax.crypto.SealedObject;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.*;

/**
 * Created by wangbai on 2017/2/17.
 */
public class PacketProcessTool {

    public static final String CHARSET = "UTF-8";
    public static final String CIPHER_ALGORITHM = "RSA";
    public static final String SIGN_ALGORITHM = "SHA256withRSA";

    public static final int KEY_LENGTH = 1024;

    public static final int MAX_ENCRYPT_BLOCK_LENGTH = 117;
    public static final int MAX_DECRYPT_BLOCK_LENGTH = 128;

    public static String bizParamsEncrypt(String publicKey, JSONObject bizParams) {
        String bizContent;

        if (bizParams != null) {
            bizContent = bizParams.toString();
        } else {
            bizContent = new JSONObject().toString();
        }

        try {
            PublicKey publicKeyStore = buildPublicKeyFromString(publicKey);

            Cipher c = Cipher.getInstance(CIPHER_ALGORITHM);
            c.init(Cipher.ENCRYPT_MODE, publicKeyStore);

            byte[] encryptedBytes = cipherCodecByBlock(c, bizContent.getBytes(CHARSET), MAX_ENCRYPT_BLOCK_LENGTH);
            return new BASE64Encoder().encode(encryptedBytes);
        } catch (Exception e) {
        }

        return null;
    }

    public static JSONObject bizParamsDecrypt(String privateKey, String bizContent) {
        JSONObject bizParams = null;

        try {
            PrivateKey privateKeyStore = buildPrivateKeyFromString(privateKey);

            Cipher d = Cipher.getInstance(CIPHER_ALGORITHM);
            d.init(Cipher.DECRYPT_MODE, privateKeyStore);

            byte[] data = new BASE64Decoder().decodeBuffer(bizContent);
            byte[] encryptedBytes = cipherCodecByBlock(d, data, MAX_DECRYPT_BLOCK_LENGTH);
            bizParams = (JSONObject) JSONValue.parse(new String(encryptedBytes, CHARSET));
        } catch (Exception e) {
            e.printStackTrace();
        }

        return bizParams;
    }

    public static String sign(String privateKey, Map<String, String> params) {
        Collection<String> keySet = params.keySet();
        List<String> keyList= new ArrayList<String>(keySet);
        Collections.sort(keyList);

        JSONObject signPacket = new JSONObject();
        for(String key : keyList) {
            signPacket.put(key, params.get(key));
        }

        try {
            PrivateKey privateKeyStore = buildPrivateKeyFromString(privateKey);
            Signature privateSignature = Signature.getInstance(SIGN_ALGORITHM);
            privateSignature.initSign(privateKeyStore);
            privateSignature.update(signPacket.toString().getBytes(CHARSET));
            byte[] signature = privateSignature.sign();

            return new BASE64Encoder().encode(signature);
        } catch (Exception e) {
        }


        return null;
    }

    public static boolean verify(String publicKey, String sign, Map<String, String> params) {
        Collection<String> keySet = params.keySet();
        List<String> keyList= new ArrayList<String>(keySet);
        Collections.sort(keyList);

        JSONObject signPacket = new JSONObject();
        for(String key : keyList) {
            signPacket.put(key, params.get(key));
        }

        try {
            PublicKey publicKeyStore = buildPublicKeyFromString(publicKey);
            Signature publicSignature = Signature.getInstance(SIGN_ALGORITHM);

            publicSignature.initVerify(publicKeyStore);
            publicSignature.update(signPacket.toString().getBytes(CHARSET));
            byte[] signatureBytes = new BASE64Decoder().decodeBuffer(sign);

            return publicSignature.verify(signatureBytes);
        } catch (Exception e) {
        }
        return false;
    }

    public static byte[] cipherCodecByBlock(Cipher c, byte[] data, int blockLength) throws Exception {
        int inputLen = data.length;
        int offSet = 0;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        byte[] cache;
        int i = 0;
        while (inputLen - offSet > 0) {
            if (inputLen - offSet > blockLength) {
                cache = c.doFinal(data, offSet, blockLength);
            } else {
                cache = c.doFinal(data, offSet, inputLen - offSet);
            }
            out.write(cache, 0, cache.length);
            i++;
            offSet = i * blockLength;
        }
        byte[] codecData = out.toByteArray();
        out.close();

        return codecData;
    }

    public static PublicKey buildPublicKeyFromString(String publicKey) throws Exception {
        KeyFactory kf = KeyFactory.getInstance(CIPHER_ALGORITHM);
        byte[] publicKeyByte = new BASE64Decoder().decodeBuffer(publicKey);
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(publicKeyByte);
        return kf.generatePublic(keySpec);
    }

    public static PrivateKey buildPrivateKeyFromString(String privateKey) throws Exception {
        KeyFactory kf = KeyFactory.getInstance(CIPHER_ALGORITHM);
        byte[] privateKeyByte = new BASE64Decoder().decodeBuffer(privateKey);
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(privateKeyByte);
        return kf.generatePrivate(keySpec);
    }
}