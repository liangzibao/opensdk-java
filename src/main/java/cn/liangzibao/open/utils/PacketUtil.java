// Copyright 2017 Liangzibao, Inc.
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// See the License for the specific language governing permissions and
// limitations under the License.

package cn.liangzibao.open.utils;

import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import javax.crypto.Cipher;
import javax.xml.bind.DatatypeConverter;
import java.io.ByteArrayOutputStream;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.*;


public class PacketUtil {

    public static final String CHARSET = "UTF-8";
    public static final String CIPHER_ALGORITHM = "RSA";
    public static final String SIGN_ALGORITHM = "SHA1withRSA";

    public static final int KEY_LENGTH = 1024;

    public static final int MAX_ENCRYPT_BLOCK_LENGTH = 117;
    public static final int MAX_DECRYPT_BLOCK_LENGTH = 128;

    public static String bizParamsEncrypt(PublicKey publicKey, JSONObject bizParams) {
        String bizContent;

        if (bizParams != null) {
            bizContent = bizParams.toString();
        } else {
            bizContent = new JSONObject().toString();
        }

        try {
            Cipher c = Cipher.getInstance(CIPHER_ALGORITHM);
            c.init(Cipher.ENCRYPT_MODE, publicKey);

            byte[] encryptedBytes = cipherCodecByBlock(c, bizContent.getBytes(CHARSET), MAX_ENCRYPT_BLOCK_LENGTH);
            return DatatypeConverter.printBase64Binary(encryptedBytes);
        } catch (Exception e) {
        }

        return null;
    }

    public static JSONObject bizParamsDecrypt(PrivateKey privateKey, String bizContent) {
        JSONObject bizParams = null;

        try {
            Cipher d = Cipher.getInstance(CIPHER_ALGORITHM);
            d.init(Cipher.DECRYPT_MODE, privateKey);

            byte[] data = DatatypeConverter.parseBase64Binary(bizContent);
            byte[] encryptedBytes = cipherCodecByBlock(d, data, MAX_DECRYPT_BLOCK_LENGTH);
            bizParams = (JSONObject) JSONValue.parse(new String(encryptedBytes, CHARSET));
        } catch (Exception e) {
        }

        return bizParams;
    }

    public static String sign(PrivateKey privateKey, Map<String, String> params) {
        Collection<String> keySet = params.keySet();
        List<String> keyList= new ArrayList<String>(keySet);
        Collections.sort(keyList);

        StringBuilder jsonStr = new StringBuilder();
        jsonStr.append('{');
        for(String key : keyList) {
            if (params.get(key).length() == 0) {
                continue;
            }

            if (jsonStr.length() > 2) {
                jsonStr.append(",");
            }

            jsonStr.append("\"")
                    .append(JSONValue.escape(key))
                    .append("\":\"")
                    .append(JSONValue.escape(params.get(key)))
                    .append("\"");
        }
        jsonStr.append('}');

        try {
            Signature privateSignature = Signature.getInstance(SIGN_ALGORITHM);
            privateSignature.initSign(privateKey);
            privateSignature.update(jsonStr.toString().replace("\\", "").getBytes(CHARSET));
            byte[] signature = privateSignature.sign();

            return DatatypeConverter.printBase64Binary(signature);
        } catch (Exception e) {
        }

        return null;
    }

    public static boolean verify(String sign, PublicKey publicKey, Map<String, String> params) {
        Collection<String> keySet = params.keySet();
        List<String> keyList= new ArrayList<String>(keySet);
        Collections.sort(keyList);

        StringBuilder jsonStr = new StringBuilder();
        jsonStr.append('{');
        for(String key : keyList) {
            if (params.get(key).length() == 0) {
                continue;
            }

            if (jsonStr.length() > 2) {
                jsonStr.append(",");
            }
            jsonStr.append("\"")
                    .append(key)
                    .append("\":\"")
                    .append(params.get(key))
                    .append("\"");
        }
        jsonStr.append('}');

        try {
            Signature publicSignature = Signature.getInstance(SIGN_ALGORITHM);

            publicSignature.initVerify(publicKey);
            publicSignature.update(jsonStr.toString().replace("\\", "").getBytes(CHARSET));
            byte[] signatureBytes = DatatypeConverter.parseBase64Binary(sign);

            return publicSignature.verify(signatureBytes);
        } catch (Exception e) {
        }

        return false;
    }

    public static byte[] cipherCodecByBlock(Cipher c, byte[] data, int blockLength) throws Exception {
        int inputLength = data.length;
        int offset = 0;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        byte[] cache;
        int i = 0;
        while (inputLength - offset > 0) {
            if (inputLength - offset > blockLength) {
                cache = c.doFinal(data, offset, blockLength);
            } else {
                cache = c.doFinal(data, offset, inputLength - offset);
            }
            out.write(cache, 0, cache.length);
            i++;
            offset = i * blockLength;
        }
        byte[] codecData = out.toByteArray();
        out.close();

        return codecData;
    }

    public static PublicKey buildPublicKeyFromString(String publicKey) throws Exception {
        publicKey = publicKey.replace("-----BEGIN PUBLIC KEY-----\n", "")
                .replace("-----END PUBLIC KEY-----", "")
                .replace("\n", "");

        KeyFactory kf = KeyFactory.getInstance(CIPHER_ALGORITHM);
        byte[] publicKeyByte = DatatypeConverter.parseBase64Binary(publicKey);
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(publicKeyByte);

        RSAPublicKey pk = (RSAPublicKey) kf.generatePublic(keySpec);
        if (pk.getModulus().bitLength() != KEY_LENGTH) {
            throw new cn.liangzibao.open.exception.Exception("Only support the public key of " + KEY_LENGTH + " key size");
        }

        return pk;
    }

    public static PrivateKey buildPrivateKeyFromString(String privateKey) throws Exception {
        privateKey = privateKey.replace("-----BEGIN PRIVATE KEY-----\n", "")
                .replace("-----END PRIVATE KEY-----", "")
                .replace("\n", "");

        KeyFactory kf = KeyFactory.getInstance(CIPHER_ALGORITHM);
        byte[] privateKeyByte = DatatypeConverter.parseBase64Binary(privateKey);
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(privateKeyByte);

        RSAPrivateKey pk = (RSAPrivateKey) kf.generatePrivate(keySpec);
        if (pk.getModulus().bitLength() != KEY_LENGTH) {
            throw new cn.liangzibao.open.exception.Exception("Only support the private key of " + KEY_LENGTH + " key size");
        }

        return pk;
    }

}