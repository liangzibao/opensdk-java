package cn.liangzibao.open;

import cn.liangzibao.open.util.PacketProcessTool;
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
                "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDEiwK0MGkAa2iKDHWADefwkYvV" +
                "Hm3oIQlceZUr0Nj3tEifmkDWkQbQv+JUYCgIQ95BAxKYarh6t26NX4cLAQ9uIOav" +
                "tm6HuWvWj3peepTPP6vGfH2xVGL3e8BmZIYCbCV3RpeAEAqsc2z/uK1SFTDk/j1J" +
                "cKzn87wMtU0R3qxE1wIDAQAB";

        String privateKey =
                "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAMSLArQwaQBraIoM" +
                "dYAN5/CRi9UebeghCVx5lSvQ2Pe0SJ+aQNaRBtC/4lRgKAhD3kEDEphquHq3bo1f" +
                "hwsBD24g5q+2boe5a9aPel56lM8/q8Z8fbFUYvd7wGZkhgJsJXdGl4AQCqxzbP+4" +
                "rVIVMOT+PUlwrOfzvAy1TRHerETXAgMBAAECgYEAnvjVcguqEqXOA8M6Ex6sWUBQ" +
                "e0xGl7iJCtxo1OzzYb/X8ghho2vguF5MYfT4zF6g2bLziobfupq99+mpoDP2VrGL" +
                "5BElLtI9C3qH0ov7NB/SRX/fjZ3mganaur6yHfo81mWtjrnl9p4wyXyVnz/fdgE5" +
                "51UU0X53xMzFRu+r0hECQQDnJ6Y5k/ujY6BlFAmxErkcScMs5WtE7fGtWdtQ2KOb" +
                "o6ShUvuT8qn8y8xOzTfoVSnLg6zUVZgNZIgBht7nVk85AkEA2ar/PpPpvDJiDB0B" +
                "q3m7LUuvahfjKs4kIMwfDmswtixoVY3nFDS2gGNMrxHv8x1uOW74nrJy/RPHxRjH" +
                "IuIkjwJAYVZL4+ERzLquFwI6FouI0YWqH2S4J/1+kH3PIZsoQejF8XztHV7I//+d" +
                "l+1IxpfeEqnvBDbK4ZDcyK/Pe2DX2QJAchOfQAGvLxXMswKEvITI511SKq0oPmfZ" +
                "IWM5J4pf9inh6Gy9XaaeuzzPlLU251hWSz1wiWOGxkIWoaJxw0N32wJAJYL5Rr11" +
                "nAT7H68otijYczVWBPl4GIEXfLLnGeUN2sDhTCXkEeQAM2RHUqAMw4Dfe+cP1SPd" +
                "GpIz69ghWGnSxQ==";

        //String bizContent = PacketProcessTool.bizParamsEncrypt(publicKey, j);
        //System.out.println(bizContent);
        //System.out.println(PacketProcessTool.bizParamsDecrypt(privateKey, bizContent));

        HashMap<String, String> params = new HashMap<String, String>();
        params.put("name", "wangbai");
        params.put("age", "11");

        String sign = PacketProcessTool.sign(privateKey, params);
        System.out.println(sign);

        HashMap<String, String> params1 = new HashMap<String, String>();
        params.put("age", "11");
        params.put("name", "wangbai");

        boolean verified = PacketProcessTool.verify(publicKey, sign, params);
        System.out.println("sign :" + verified);

        try {
            PublicKey pub = PacketProcessTool.buildPublicKeyFromString(publicKey);
            PrivateKey pri = PacketProcessTool.buildPrivateKeyFromString(privateKey);

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
            byte[] cipherText = PacketProcessTool.cipherCodecByBlock(encryptCipher,
                    plainText.getBytes(),
                    PacketProcessTool.MAX_ENCRYPT_BLOCK_LENGTH);

            String cText = new String(cipherText, "UTF-8");
            //System.out.println(cText);

            Cipher decriptCipher = Cipher.getInstance("RSA");
            decriptCipher.init(Cipher.DECRYPT_MODE, pri);

            byte[] pText = PacketProcessTool.cipherCodecByBlock(decriptCipher,
                    cipherText,
                    PacketProcessTool.MAX_DECRYPT_BLOCK_LENGTH);

            //System.out.println(new String(pText));
        } catch (Exception e) {
            e.printStackTrace();
        }


        //String url = "http://wangbai.devel.liangzibao.cn/";
        //Map<String, String> params = new HashMap<String, String>();
        //params.put("name", "wangbai");

        //try {
        //    System.out.println(ProtocolHandler.invoke(url, params));
        //} catch (Exception e) {
        //    System.out.println("==========start===========");
        //    e.printStackTrace();
        //   System.out.println("============end===========");
        //}
    }

}
