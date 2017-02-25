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

package cn.liangzibao.open;

import cn.liangzibao.open.exception.ResponseError;
import cn.liangzibao.open.exception.SignVerificationError;
import cn.liangzibao.open.utils.PacketUtil;

import cn.liangzibao.open.utils.ProtocolUtil;
import org.apache.http.ProtocolException;
import org.json.simple.JSONObject;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.*;


final public class Client {

    private String baseUrl;
    private PrivateKey privateKey;
    private PublicKey lzbPublicKey;

    private HashMap<String, String> params;

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
        try {
            this.privateKey = PacketUtil.buildPrivateKeyFromString(privateKey);
            this.lzbPublicKey = PacketUtil.buildPublicKeyFromString(lzbPublicKey);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        this.params.put("app_key", appKey);
        this.params.put("version", version);
        this.params.put("format", format);
        this.params.put("sign_type", signType);
        this.params.put("charset", charset);
    }

    private void init() {
        this.params = new HashMap<>();
    }

    public JSONObject invoke(String serviceName, JSONObject bizParams)
            throws ProtocolException, SignVerificationError, ResponseError {
        String bizContent = PacketUtil.bizParamsEncrypt(this.lzbPublicKey, bizParams);
        Long requestTime = new Date().getTime()/1000;

        //build protocol params
        @SuppressWarnings("unchecked")
        Map<String, String> protocolParams = (HashMap<String, String>) this.params.clone();
        protocolParams.put("biz_content", bizContent);
        protocolParams.put("service_name", serviceName);
        protocolParams.put("timestamp", requestTime.toString());

        //add sign
        String sign = PacketUtil.sign(this.privateKey, protocolParams);
        protocolParams.put("sign", sign);

        //send request
        Map<String, String> responseParams;
        try {
            responseParams = ProtocolUtil.invoke(this.baseUrl, protocolParams);
        } catch(Exception e) {
            throw new ProtocolException("Protocol runtime error", e);
        }

        //check response error
        if (responseParams.get("ret_code") != null
                && !responseParams.get("ret_code").equals("200")) {
            throw new ResponseError(responseParams.get("ret_msg"), Long.valueOf(responseParams.get("ret_code")));
        }

        //verify response sign
        sign = responseParams.get("sign");
        responseParams.remove("sign");

        if (!PacketUtil.verify(sign, this.lzbPublicKey, responseParams)) {
            throw new SignVerificationError("response signature fails to verify");
        }

        return PacketUtil.bizParamsDecrypt(this.privateKey, responseParams.get("biz_content"));
    }

    public String buildRequestUrl(String serviceName, JSONObject bizParams) {
        String bizContent = PacketUtil.bizParamsEncrypt(this.lzbPublicKey, bizParams);
        Long requestTime = new Date().getTime()/1000;

        //build protocol params
        @SuppressWarnings("unchecked")
        Map<String, String> protocolParams = (HashMap<String, String>) this.params.clone();
        protocolParams.put("biz_content", bizContent);
        protocolParams.put("service_name", serviceName);
        protocolParams.put("timestamp", requestTime.toString());

        //add sign
        String sign = PacketUtil.sign(this.privateKey, protocolParams);
        protocolParams.put("sign", sign);

        return ProtocolUtil.buildRequestUrl(this.baseUrl, protocolParams);
    }
}
