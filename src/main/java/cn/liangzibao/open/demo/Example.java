package cn.liangzibao.open.demo;

import cn.liangzibao.open.Client;
import org.json.simple.JSONObject;
import java.util.Date;

/**
 * Created by wangbai on 2017/2/23.
 */
public class Example {

    public static void main(String args[]) throws Exception {

        String url = "http://openapi.wangshaolin.devel.liangzibao.cn/getway/do";
        String appKey = "6823490131d9146da878b66119dcd313";

        String lzbPublicKey =
                "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDATSY6n29/weYGpL80lGEm6km/\n" +
                "zOkvJ3KTlIlwwsF7t2KUbqxcDPLYkmPbZBHPyDX9HRg2NB5rqT4Yiq4yZj2lAlho\n" +
                "Za5ojjGbS0u9XAVsVm1+JPeQ9H5qUrVh7lWZZWukePv/mESInxhpFEC9fiDr3IvF\n" +
                "4qdIenuj9eOlDGx7nwIDAQAB\n";

        String publicKey =
                "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQD5esEhJNwqDZ7dgtGYwZp6oiuy\n" +
                "+prg5CJv2uVLZgr/73hnYJO4y76+4yVGhZWFNMUb+yB/2HmwP5uja5gS+hpRdTgw\n" +
                "XDt9nbv93nNbYSNlIxq4wqpF0SSI93RYu/7BP1rJ+7gLle6uQER0ufAPp7nI11YE\n" +
                "hu977kE2S4CXJERd8wIDAQAB\n";

        String privateKey = "-----BEGIN PRIVATE KEY-----\n" +
                "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBAPl6wSEk3CoNnt2C\n" +
                "0ZjBmnqiK7L6muDkIm/a5UtmCv/veGdgk7jLvr7jJUaFlYU0xRv7IH/YebA/m6Nr\n" +
                "mBL6GlF1ODBcO32du/3ec1thI2UjGrjCqkXRJIj3dFi7/sE/Wsn7uAuV7q5ARHS5\n" +
                "8A+nucjXVgSG73vuQTZLgJckRF3zAgMBAAECgYEA1BcSxv2SGkI4E7LaQxYcITNr\n" +
                "GR5JuvTXfeZG1qGqqkhIQQJus0SrJZbvopOAOJ1TEOWejHFZioeY5gZbY46x/W6F\n" +
                "Y600FleDuFF85hbfMUCEzSbrb2qeIHWUHe4ZBIQhxGjGjtunt9P93qNf18YJttvP\n" +
                "bdKqBH1pA88meK2AlikCQQD/Ta53PPYPpYeodsy0Y3f9knaHMpQjf0SeYqWYClHs\n" +
                "H7D0/8g82Ps+qVI2IdsWpp2BKl32uDHSjT5E08pWtJN1AkEA+ikBUKy+ck14vLES\n" +
                "MqHPtqxHaqFvtwVmOOTDxvvOhQ9vc8JqKfsOaYc8hArOPs/Lx8yFdRDipRS8DvQc\n" +
                "v40GxwJBAJKNEP05bTYGGx1FNLTH9HUGwitRiV/nCoiwr8XbBrO4bWf1/AeRtod9\n" +
                "wsd4H7+c3QTsQQwDJ/ZpRblUqe2jspECQFO/LoWHjypM7UKeNO1mZldNTYtRCElJ\n" +
                "MXOSgkg3PGgnRrSGPWxYc/a4I3ZA99LnVd1JhtQuFvIVAvAuoQEvgTkCQA4Y2doC\n" +
                "76KL/M5wPihmeQynx2Uhet7Wh6srIQMrELAn2q9+FVOKn88TI4Putm2bswyun8pC\n" +
                "2bVSu98GmgKiby8=\n" +
                "-----END PRIVATE KEY-----";

        Client client = new Client(url, privateKey, lzbPublicKey, appKey);

        JSONObject bizParams = new JSONObject();
        bizParams.put("product_mask", "184371");
        bizParams.put("mer_order_id","123");

        Long requestTime = new Date().getTime()/1000;

        bizParams.put("start_time", requestTime.toString());
        bizParams.put("end_time", requestTime.toString());
        bizParams.put("mer_order_uuid","123" + requestTime.toString());

        JSONObject user = new JSONObject();
        user.put("user_id", "1111");
        user.put("name", "张三三");
        user.put("idno", "1109211986061130065");

        bizParams.put("insure_user_info", user);

        JSONObject order1 = new JSONObject();
        order1.put("book_user_id", "1111");

        bizParams.put("mer_order_info", order1);

        try {
            JSONObject bizResponse = client.invoke("lzb.flower.policy.create", bizParams);

            System.out.println(bizResponse);
            System.out.println(client.buildRequestUrl("lzb.flower.policy.create", bizParams));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
