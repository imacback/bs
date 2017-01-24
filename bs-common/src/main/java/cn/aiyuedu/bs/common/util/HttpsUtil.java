package cn.aiyuedu.bs.common.util;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContextBuilder;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import javax.net.ssl.SSLContext;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public  class HttpsUtil {
    public  static  void  main(String[] args) throws IOException {
        for(;;){
            Long s = System.currentTimeMillis();
            CloseableHttpClient httpClient = HttpClients.createDefault();

            HttpPost httpPost = new HttpPost("http://api.duoquyuedu.com/api/chkupdate.do");
            StringEntity body = new StringEntity("rid=1419928140470&xclient={\"uid\":125,\"site\":\"1\",\"app\":\"1.1.10.6\",\"os\":\"Android\",\"model\":\"MI 4LTE\",\"root\":\"\\/storage\\/emulated\\/0\\/duoquyuedu\\/books\",\"width\":1080,\"imei\":\"866333026899144\",\"sessionid\":\"4d7df511-ea01-4f99-aa05-22cd4f1310a3\",\"cid\":\"5924b921549a71af974351e6ec392677\",\"operator\":\"wifi\",\"ip\":\"172.18.11.79\",\"os_version\":\"4.4.4\",\"height\":1920,\"os_id\":\"bd726f210812f4de\",\"brand\":\"Xiaomi\",\"dpi\":480,\"imsi\":\"460001017046646\",\"channel\":6}&data={\"shelf\":0}&sign=ed4062c8c92c626c3bc459f89a4557a0&");
//        body.setContentType("application/json");
            httpPost.setEntity(body);

//        List nvps = new ArrayList();
//        nvps.add(new BasicNameValuePair("username", "myusername"));
//        nvps.add(new BasicNameValuePair("password", "mypassword"));
//        httpPost.setEntity(new UrlEncodedFormEntity(nvps));
            httpPost.addHeader("Content-Type","application/x-www-form-urlencoded");
            CloseableHttpResponse httppHttpResponse2 = httpClient.execute(httpPost);
            try{
                System.out.println(httppHttpResponse2.getStatusLine());
                System.out.println(EntityUtils.toString(httppHttpResponse2.getEntity()));
                System.out.println((System.currentTimeMillis()-s)+"==============================================");
            }finally{
                httppHttpResponse2.close();
            }
            httpClient.close();

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }
    /**
     * HTTP Post 获取内容
     *
     * @param url     请求的url地址 ?之前的地址
     * @param params  请求的参数
     * @param charset 编码格式
     * @return 页面内容
     */
    public static String doPostHttps(String url, Map<String, String> params, String charset) {
        CloseableHttpResponse response = null;
        try {
            List<NameValuePair> pairs = null;
            if (params != null && !params.isEmpty()) {
                pairs = new ArrayList<NameValuePair>(params.size());
                for (Map.Entry<String, String> entry : params.entrySet()) {
                    String value = entry.getValue();
                    if (value != null) {
                        pairs.add(new BasicNameValuePair(entry.getKey(), value));
                    }
                }
            }
            HttpPost httpPost = new HttpPost(url);
            if (pairs != null && pairs.size() > 0) {
                httpPost.setEntity(new UrlEncodedFormEntity(pairs, charset));
            }
            CloseableHttpClient httpClient = createSSLClientDefault();
            response = httpClient.execute(httpPost);
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode != 200) {
                httpPost.abort();
                throw new RuntimeException("HttpClient,error status code :" + statusCode);
            }
            HttpEntity entity = response.getEntity();
            String result = null;
            if (entity != null) {
                result = EntityUtils.toString(entity, "utf-8");
            }
            EntityUtils.consume(entity);
            response.close();
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (response != null)
                try {
                    response.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
        }
        return null;
    }
    public static CloseableHttpClient createSSLClientDefault() {
        try {
            SSLContext sslContext = new SSLContextBuilder().loadTrustMaterial(null, new TrustStrategy() {
                //信任所有
                public boolean isTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                    return true;
                }
            }).build();
            SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslContext);
            return HttpClients.custom().setSSLSocketFactory(sslsf).build();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (KeyStoreException e) {
            e.printStackTrace();
        }
        return HttpClients.createDefault();
    }
}
