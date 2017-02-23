package cn.liangzibao.open;

import cn.liangzibao.open.utils.PacketUtil;
import org.json.simple.JSONObject;

import javax.crypto.Cipher;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.HashMap;

/**
 * Created by wangbai on 2017/2/17.
 */
public class Main {

    public static void main(String args[]) {

        JSONObject j = new JSONObject();
        j.put("name", "王柏");

        String publicKey =
                "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQD5esEhJNwqDZ7dgtGYwZp6oiuy" +
                        "+prg5CJv2uVLZgr/73hnYJO4y76+4yVGhZWFNMUb+yB/2HmwP5uja5gS+hpRdTgw" +
                        "XDt9nbv93nNbYSNlIxq4wqpF0SSI93RYu/7BP1rJ+7gLle6uQER0ufAPp7nI11YE" +
                        "hu977kE2S4CXJERd8wIDAQAB";

        String privateKey =
                "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBAPl6wSEk3CoNnt2C" +
                        "0ZjBmnqiK7L6muDkIm/a5UtmCv/veGdgk7jLvr7jJUaFlYU0xRv7IH/YebA/m6Nr" +
                        "mBL6GlF1ODBcO32du/3ec1thI2UjGrjCqkXRJIj3dFi7/sE/Wsn7uAuV7q5ARHS5" +
                        "8A+nucjXVgSG73vuQTZLgJckRF3zAgMBAAECgYEA1BcSxv2SGkI4E7LaQxYcITNr" +
                        "GR5JuvTXfeZG1qGqqkhIQQJus0SrJZbvopOAOJ1TEOWejHFZioeY5gZbY46x/W6F" +
                        "Y600FleDuFF85hbfMUCEzSbrb2qeIHWUHe4ZBIQhxGjGjtunt9P93qNf18YJttvP" +
                        "bdKqBH1pA88meK2AlikCQQD/Ta53PPYPpYeodsy0Y3f9knaHMpQjf0SeYqWYClHs" +
                        "H7D0/8g82Ps+qVI2IdsWpp2BKl32uDHSjT5E08pWtJN1AkEA+ikBUKy+ck14vLES" +
                        "MqHPtqxHaqFvtwVmOOTDxvvOhQ9vc8JqKfsOaYc8hArOPs/Lx8yFdRDipRS8DvQc" +
                        "v40GxwJBAJKNEP05bTYGGx1FNLTH9HUGwitRiV/nCoiwr8XbBrO4bWf1/AeRtod9" +
                        "wsd4H7+c3QTsQQwDJ/ZpRblUqe2jspECQFO/LoWHjypM7UKeNO1mZldNTYtRCElJ" +
                        "MXOSgkg3PGgnRrSGPWxYc/a4I3ZA99LnVd1JhtQuFvIVAvAuoQEvgTkCQA4Y2doC" +
                        "76KL/M5wPihmeQynx2Uhet7Wh6srIQMrELAn2q9+FVOKn88TI4Putm2bswyun8pC" +
                        "2bVSu98GmgKiby8=";

        //String bizContent = PacketUtil.bizParamsEncrypt(publicKey, j);
        //System.out.println(bizContent);
        //System.out.println(PacketUtil.bizParamsDecrypt(privateKey, bizContent));

        HashMap<String, String> params = new HashMap<String, String>();
        params.put("name", "wangbai");
        params.put("age", "11");

        String sign = PacketUtil.sign(privateKey, params);
        System.out.println(sign);

        HashMap<String, String> params1 = new HashMap<String, String>();
        params.put("age", "11");
        params.put("name", "wangbai");

        boolean verified = PacketUtil.verify(publicKey, sign, params);
        System.out.println("sign :" + verified);

        System.exit(1);

        try {
            PublicKey pub = PacketUtil.buildPublicKeyFromString(publicKey);
            PrivateKey pri = PacketUtil.buildPrivateKeyFromString(privateKey);

            String plainText = "1234567890"
                    + "1234567890"
                    + "1234567890"
                    + "1234567890"
                    + "1234567890"
                    + "1234567890"
                    + "1234567890"
                    + "1234567890"
                    + "1234567890"
                    + "1234567890"
                    + "1234567890"
                    + "1234567890"
                    + "1234567890"
                    + "1234567890"
                    + "1234567890"
                    + "1234567890"
                    + "王柏";

            Cipher encryptCipher = Cipher.getInstance("RSA");
            encryptCipher.init(Cipher.ENCRYPT_MODE, pub);
            byte[] cipherText = PacketUtil.cipherCodecByBlock(encryptCipher,
                    plainText.getBytes(),
                    PacketUtil.MAX_ENCRYPT_BLOCK_LENGTH);

            String cText = new String(cipherText, "UTF-8");
            //System.out.println(cText);

            Cipher decriptCipher = Cipher.getInstance("RSA");
            decriptCipher.init(Cipher.DECRYPT_MODE, pri);

            byte[] pText = PacketUtil.cipherCodecByBlock(decriptCipher,
                    cipherText,
                    PacketUtil.MAX_DECRYPT_BLOCK_LENGTH);

            //System.out.println(new String(pText));
        } catch (Exception e) {
            e.printStackTrace();
        }


        //String url = "http://wangbai.devel.liangzibao.cn/";
        //Map<String, String> params = new HashMap<String, String>();
        //params.put("name", "wangbai");

        //try {
        //    System.out.println(ProtocolUtil.invoke(url, params));
        //} catch (Exception e) {
        //    System.out.println("==========start===========");
        //    e.printStackTrace();
        //   System.out.println("============end===========");
        //}
    }

}
