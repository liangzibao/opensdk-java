package cn.liangzibao.open;

import cn.liangzibao.open.util.PacketProcessTool;

import cn.liangzibao.open.util.ProtocolHandler;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.ProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.json.simple.JSONObject;

import java.util.*;

/**
 * Created by wangbai on 2017/2/20.
 */
public class Client {

    private String appKey;
    private String baseUrl;
    private String privateKey;
    private String lzbPublicKey;

    private String version;
    private String format;
    private String signType;
    private String charset;

    public Client(String appKey, String baseUrl, String privateKey, String lzbPublicKey) {
        this(appKey, baseUrl, privateKey, lzbPublicKey, "1.0.0", "JSON", "RSA", "UTF-8");
    }

    public Client(String appKey,
                  String baseUrl,
                  String privateKey,
                  String lzbPublicKey,
                  String version,
                  String format,
                  String signType,
                  String charset) {
        this.appKey = appKey;
        this.baseUrl = baseUrl;
        this.privateKey = privateKey;
        this.lzbPublicKey = lzbPublicKey;
        this.version = version;
        this.format = format;
        this.signType = signType;
        this.charset = charset;
    }

    public JSONObject invoke(String serviceName, JSONObject bizParams) throws ProtocolException {
        String bizContent;

        if (bizParams != null) {
            bizContent = bizParams.toString();
        } else {
            bizContent = new JSONObject().toString();
        }

        bizContent = PacketProcessTool.encrypt(this.lzbPublicKey, bizContent);
        Long requestTime = new Date().getTime()/1000;

        //build the request json packet
        JSONObject signPacket = new JSONObject();
        signPacket.put("app_key", this.appKey);
        signPacket.put("biz_content", bizContent);
        signPacket.put("charset", this.charset);
        signPacket.put("format", this.format);
        signPacket.put("service_name", serviceName);
        signPacket.put("sign_type", this.signType);
        signPacket.put("timestamp", requestTime);
        signPacket.put("version", this.version);

        String sign = PacketProcessTool.sign(this.privateKey, signPacket.toString());

        //build the http request params
        Map<String, String> protocolParams = new HashMap<String, String>();
        protocolParams.put("app_key", this.appKey);
        protocolParams.put("biz_content", bizContent);
        protocolParams.put("charset", this.charset);
        protocolParams.put("format", this.format);
        protocolParams.put("service_name", serviceName);
        protocolParams.put("sign", sign);
        protocolParams.put("sign_type", this.signType);
        protocolParams.put("timestamp", requestTime.toString());
        protocolParams.put("version", this.version);

        Map<String, String> responseParams;
        try {
            responseParams = ProtocolHandler.invoke(this.baseUrl, protocolParams);
        } catch(Exception e) {
            throw new ProtocolException("Protocol runtime error", e);
        }


        return null;
    }

}
