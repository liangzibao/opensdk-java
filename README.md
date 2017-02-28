# 量子保开放平台SDK - Java版
对量子保开放平台（http://www.liangzibao.cn/apidoc/）的API接入协议做基础的封装，即做为参考实现也可以直接做为项目的依赖直接引用。

## API接入例子
<pre><code>

    @SuppressWarnings("unchecked")
    public static void main( String[] args ) {

        String baseUrl = "对应环境的API网关URL";

        String appKey = "量子保为开发者分配的app_key";

        //量子保对应环境的公钥
        String lzbPublicKey = "-----BEGIN PUBLIC KEY-----\n" +
                "..." +
                "-----END PUBLIC KEY-----";

        //开发者密钥对的私钥，请私密保管
        //注意格式是：PKCS8格式
        String privateKey = "-----BEGIN PRIVATE KEY-----\n" +
                "..." +
                "-----END PRIVATE KEY-----";

        Client client = new Client(baseUrl, privateKey, lzbPublicKey, appKey);

        try {
            String serviceName = "业务API名称";
            //请参考业务API文档，进行JSON对象的组装
            JSONObject params = new JSONObject();
            params.put("product_mask", "184371");
            JSONObject result = client.invoke(serviceName, params);

            System.out.println(result);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

</code></pre>

## HTML5 URL生成例子
<pre><code>

    @SuppressWarnings("unchecked")
    public static void main( String[] args ) {

		String baseUrl = "对应环境的HTML5网关URL";

        String appKey = "量子保为开发者分配的app_key";

        //量子保对应环境的公钥
        String lzbPublicKey = "-----BEGIN PUBLIC KEY-----\n" +
                "..." +
                "-----END PUBLIC KEY-----";

        //开发者密钥对的私钥，请私密保管
        //注意格式是：PKCS8格式
        String privateKey = "-----BEGIN PRIVATE KEY-----\n" +
                "..." +
                "-----END PRIVATE KEY-----";

        Client client = new Client(baseUrl, privateKey, lzbPublicKey, appKey);

        try {
			//请参看对应HTML5接入的业务文档
            JSONObject params = new JSONObject();
            String result = client.buildRequestUrl(params);

            System.out.println(result);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

</code></pre>
