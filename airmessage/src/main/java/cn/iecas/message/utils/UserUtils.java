package cn.iecas.message.utils;

import cn.aircas.authentication.common.CommonResult;
import cn.aircas.authentication.entity.UserInfo;
import cn.aircas.authentication.service.UserInfoService;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


/**
 * 用户工具类
 */
@Component
public class UserUtils {

    @Autowired
    private UserInfoService userInfoService;

    /**
     * 根据用户token获取用户信息
     * @return
     */
    public UserInfo getUserInfoByToken(String token) {
        CommonResult commonResult = userInfoService.getUserInfo(token);
        UserInfo userInfo = JSONObject.parseObject(JSONObject.toJSONString(commonResult.getData()), UserInfo.class);
        return userInfo;
    }
}
