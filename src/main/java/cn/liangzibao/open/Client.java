package cn.liangzibao.open;

import cn.liangzibao.open.util.PacketProcessTool;

import cn.liangzibao.open.util.ProtocolHandler;
import org.apache.http.ProtocolException;
import org.apache.http.message.BasicNameValuePair;
import org.json.simple.JSONObject;

import java.util.*;

/**
 * Created by wangbai on 2017/2/20.
 */
public class Client {

    private String baseUrl;
    private String privateKey;
    private String lzbPublicKey;

    HashMap<String, String> params;

    public Client(String baseUrl, String privateKey, String lzbPublicKey, String appKey) {
        this(baseUrl, privateKey, lzbPublicKey, appKey, "1.0.0", "JSON", "RSA", "UTF-8");
    }

    public Client(String baseUrl,
                  String privateKey,
                  String lzbPublicKey,
                  String appKey,
                  String version,
                  String format,
                  String signType,
                  String charset) {
        this.init();

        this.baseUrl = baseUrl;
        this.privateKey = privateKey;
        this.lzbPublicKey = lzbPublicKey;

        this.params.put("app_key", appKey);
        this.params.put("version", version);
        this.params.put("format", format);
        this.params.put("sign_type", signType);
        this.params.put("charset", charset);
    }

    protected void init() {
        this.params = new HashMap<String, String>();
    }

    public JSONObject invoke(String serviceName, JSONObject bizParams) throws ProtocolException {
        String bizContent = PacketProcessTool.bizParamsEncrypt(this.lzbPublicKey, bizParams);
        Long requestTime = new Date().getTime()/1000;

        //build protocol params
        Map<String, String> protocolParams = new HashMap<String, String>();
        protocolParams = (Map<String, String>) this.params.clone();
        protocolParams.put("biz_content", bizContent);
        protocolParams.put("service_name", serviceName);
        protocolParams.put("timestamp", requestTime.toString());

        //add sign
        String sign = PacketProcessTool.sign(this.privateKey, protocolParams);
        protocolParams.put("sign", sign);

        //send request
        Map<String, String> responseParams;
        try {
            responseParams = ProtocolHandler.invoke(this.baseUrl, protocolParams);
        } catch(Exception e) {
            throw new ProtocolException("Protocol runtime error", e);
        }

        //verify response sign
        sign = responseParams.get("sign");
        responseParams.remove("sign");
        if (!PacketProcessTool.verify(responseParams.get("sign"), this.lzbPublicKey, responseParams)) {

        }

        //check response error

        
        return PacketProcessTool.bizParamsDecrypt(this.privateKey, responseParams.get("biz_content"));
    }

}
