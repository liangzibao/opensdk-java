package cn.liangzibao.open;

import cn.liangzibao.open.util.ProtocolHandler;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by wangbai on 2017/2/17.
 */
public class Main {

    public static void main(String args[]) {
        String url = "http://wangbai.devel.liangzibao.cn/";
        Map<String, String> params = new HashMap<String, String>();
        params.put("name", "wangbai");

        try {
            System.out.println(ProtocolHandler.invoke(url, params));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
