package cn.iecas.message.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpConnection;
import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.tomcat.jni.Buffer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.net.*;
import java.util.Map;
import java.util.Set;

@Slf4j
public class HttpUtils {

    @Autowired
    private static HttpServletRequest servletRequest;

    /**
     * 调用远程接口，可用
     * @param url
     * @param token
     * @param params
     * @return
     */
    public static Object getRemoteResult(String url, String token, Map<String, String> params) {
        JSONObject jsonss = null;
        // params中的参数还是要进行手动拼接到url中
        url = url + "/2022";
        JSONObject json = new JSONObject();
        if (params != null) {
            Set<String> keySet = params.keySet();
            for (String key : keySet) {
                json.put(key, params.get(key));
            }
        }

        try {
            URL url1 = new URL(url);
            // 打开和url之间的连接
            HttpURLConnection conn = (HttpURLConnection) url1.openConnection();
            PrintWriter out =null;

            // 请求方式
            conn.setRequestMethod("GET");
            // 通用属性
            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("token", token);
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.connect();

            // 获取URLConnection对象对应的输入流
            InputStream is = conn.getInputStream();
            // 构造字符缓冲流
            BufferedReader br = new BufferedReader(new InputStreamReader(is));

            String str = "";
            while ((str=br.readLine()) != null) {
                jsonss = JSONObject.parseObject(str);
            }
            log.info("获取报文数据： { }", jsonss);
            // 关闭流
            is.close();
            // 断开连接
            conn.disconnect();
            log.info("接口调用结束！");

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return jsonss;
    }

    /**
     * 带参数的GET请求，暂不可用
     * @param url
     * @param params
     * @return
     */
    public static String doGet(String url, Map<String, String> params) {
        // 创建HttpClient对象
        CloseableHttpClient httpClient = HttpClients.createDefault();

        String resultString = "";
        CloseableHttpResponse response = null;

        // 创建uri
        try {
            URIBuilder uriBuilder = new URIBuilder(url);
            if (params != null) {
                Set<String> keySet = params.keySet();
                for (String key : keySet) {
                    uriBuilder.addParameter(key, params.get(key));
                }
            }
            URI build = uriBuilder.build();
            // 创建http get请求
            HttpGet httpGet = new HttpGet(build);
            // 执行请求
            response = httpClient.execute(httpGet);
            // 是否请求成功
            if (response.getStatusLine().getStatusCode() == 200) {
                response.getAllHeaders();
                response.getEntity();
                resultString = EntityUtils.toString(response.getEntity(), "UTF-8");
            }
            HttpEntity entity = response.getEntity();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (httpClient != null) {
                    httpClient.close();
                }
                if (response != null) {
                    response.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return resultString;
    }
}
