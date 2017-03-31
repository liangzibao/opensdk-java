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

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class ProtocolUtil {

    static final String USER_AGENT = "LZB/Openapi SDK/v1.1.0(Java)";
    static final String CONTENT_TYPE = "application/x-www-form-urlencoded;charset=utf-8";

    static public Map<String, String> invoke(String Url, Map<String, String> params) throws IOException {
        CloseableHttpClient httpclient = HttpClients.createDefault();
        try {
            final HttpPost requestMethod = new HttpPost(Url);
            requestMethod.setHeader("User-Agent", USER_AGENT);
            requestMethod.setHeader("Content-Type", CONTENT_TYPE);
            requestMethod.setEntity(new UrlEncodedFormEntity(ProtocolUtil.buildRequest(params)));

            ResponseHandler<String> responseHandler = new ResponseHandler<String>() {
                public String handleResponse(final HttpResponse response) throws IOException {
                    int status = response.getStatusLine().getStatusCode();
                    HttpEntity entity = response.getEntity();
                    String responseBody = entity != null ? EntityUtils.toString(entity) : null;

                    if (status != 200) {
                        throw new ClientProtocolException("Unexpected http response status: " + status
                                + ", response status message: " + response.getStatusLine()
                                + ", response body: " + responseBody);
                    }

                    return responseBody;
                }
            };

            return parseResponse(httpclient.execute(requestMethod, responseHandler));
        } finally {
            httpclient.close();
        }
    }

    static public String buildRequestUrl(String Url, Map<String, String> params) {
        try {
            String urlParam = EntityUtils.toString(new UrlEncodedFormEntity(ProtocolUtil.buildRequest(params)));

            return Url + "?" + urlParam;
        } catch(Exception e) {
        }

        return null;
    }

    static private List<NameValuePair> buildRequest(Map<String, String> params) {
        List<NameValuePair> postParams = new ArrayList<NameValuePair>();

        for (String key : params.keySet()) {
            postParams.add(new BasicNameValuePair(key, params.get(key)));
        }

        return postParams;
    }

    static private Map<String, String> parseResponse(String responseBody) {
        JSONObject responseJson;
        responseJson = (JSONObject) JSONValue.parse(responseBody);

        Map<String, String> responseMap = new HashMap<String, String>();
        for (Object key : responseJson.keySet()) {
            responseMap.put((String)key, responseJson.get(key).toString());
        }

        return responseMap;
    }

}
