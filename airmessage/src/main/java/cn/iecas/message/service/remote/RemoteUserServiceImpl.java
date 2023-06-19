package cn.iecas.message.service.remote;

import cn.aircas.authentication.common.CommonResult;
import cn.aircas.authentication.entity.UserInfo;
import cn.iecas.message.utils.HttpUtils;
import com.alibaba.fastjson.JSONObject;
import org.apache.http.client.HttpClient;
import org.apache.http.client.utils.HttpClientUtils;
import org.apache.http.impl.client.HttpClients;
import org.hibernate.validator.internal.util.privilegedactions.GetMethod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.thymeleaf.util.MapUtils;
import springfox.documentation.spring.web.json.Json;

import javax.xml.ws.spi.http.HttpHandler;
import java.lang.reflect.Array;
import java.util.*;

@Service
public class RemoteUserServiceImpl {

    @Autowired
    private RestTemplate restTemplate;

    @Value("${remote.users.url}")
    private String url;

    @Value("${api.user.get-user-info}")
    private String getUserInfoUrl;

    /**
     * 获取用户信息
     * @param token
     * @return
     */
    public CommonResult<UserInfo> getUserInfo(String token) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("token", token);
        HttpEntity<String> httpEntity = new HttpEntity((Object)null, httpHeaders);
        CommonResult commonResult = (CommonResult)this.restTemplate.exchange(this.url, HttpMethod.GET, httpEntity, CommonResult.class, new Object[0]).getBody();
        return commonResult;
    }

    /**
     * 获取通知目标用户信息
     * @param token
     * @param params URL中的参数
     * @return
     */
    public CommonResult<UserInfo> getUserInfo(String token, Map<String, String> params) {
        // 添加URL参数
        StringBuffer buffer = new StringBuffer();
        Iterator<String> iterator = params.keySet().iterator();
        while (iterator.hasNext()) {
            String key = iterator.next();
            if (buffer.length() <= 0) {
                buffer.append("?");
            } else {
                buffer.append("&");
            }
            buffer.append(key);
            buffer.append("=");
            buffer.append(params.get(key));
        }
        String urlParams = getUserInfoUrl + buffer.toString();

        //设置Header
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("token", token);
        HttpEntity<Map<String, String>> httpEntity = new HttpEntity((Object) null, httpHeaders);

        CommonResult commonResult = (CommonResult)this.restTemplate.exchange(urlParams, HttpMethod.GET, httpEntity, CommonResult.class, new Object[0]).getBody();
        return commonResult;
    }

    /**
     * 获取多个用户信息，不可用
     * @param token
     * @param params
     * @return
     */
    public String getUserInfoById(String token, Map<String, String> params) {
        return HttpUtils.doGet(url, params);
    }

    /**
     * 获取多个用户信息
     * @param token
     * @param params
     * @return
     */
    public Object getUserInfoByIds(String token, Map<String, String> params) {
        return HttpUtils.getRemoteResult("http://192.168.9.64:31161/api/v1/airmessage/message", token, params);
    }

    /**
     * GET请求 有参无参
     * @param url
     * @param token
     * @param params
     * @return
     */
    public JSONObject getMessageInfo(String url, String token, Map<String, String> params) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("token", token);
        headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
        HttpEntity<Map<String, String>> httpEntity = new HttpEntity<>(params, headers);
        ResponseEntity<JSONObject> result = restTemplate.exchange(url, HttpMethod.GET, httpEntity, JSONObject.class);
        return result.getBody();
    }

}
