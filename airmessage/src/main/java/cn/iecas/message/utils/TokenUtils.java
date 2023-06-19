package cn.iecas.message.utils;

import cn.aircas.authentication.common.CommonResult;
import cn.aircas.authentication.entity.UserInfo;
import cn.aircas.authentication.service.UserInfoService;
import cn.iecas.message.utils.resultjson.ResultGenerator;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@Component
public class TokenUtils {

    @Autowired
    private ResultGenerator resultGenerator;

    @Autowired
    private HttpServletRequest httpServletRequest;

    @Autowired
    private HttpServletResponse httpServletResponse;

    @Autowired
    private UserInfoService userInfoService;

    public boolean isAdmin(String token) throws InstantiationException, IllegalAccessException, IOException {
        // token
        CommonResult commonResult = userInfoService.getUserInfo(token);
        UserInfo userInfo = JSONObject.parseObject(JSONObject.toJSONString(commonResult.getData()), UserInfo.class);
        //UserInfo userInfo = BeanMapUtils.mapToBean((Map<String, Object>) userInfoService.getUserInfo(token).getData(), UserInfo.class);
        if (!userInfo.isAdmin()) {
            return false;
        }
        return true;
    }
}
