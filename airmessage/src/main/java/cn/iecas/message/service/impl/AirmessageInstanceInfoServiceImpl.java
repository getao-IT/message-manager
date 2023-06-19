package cn.iecas.message.service.impl;


import cn.aircas.authentication.entity.UserInfo;
import cn.aircas.authentication.service.UserInfoService;
import cn.iecas.message.domain.AirmessageInstanceInfo;
import cn.iecas.message.domain.SearchParams;
import cn.iecas.message.mappers.AirmessageInstanceInfoMapper;
import cn.iecas.message.service.AirmessageInstanceInfoService;
import cn.iecas.message.utils.TokenUtils;
import cn.iecas.message.utils.resultjson.ResultCode;
import cn.iecas.message.utils.resultjson.ResultGenerator;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class AirmessageInstanceInfoServiceImpl implements AirmessageInstanceInfoService<AirmessageInstanceInfo> {

    @Autowired
    private ResultGenerator resultGenerator;

    @Autowired
    private AirmessageInstanceInfoMapper airmessageInstanceInfoMapper;

    @Autowired
    private HttpServletRequest httpServletRequest;

    @Autowired
    private UserInfoService userInfoService;

    @Autowired
    private TokenUtils tokenUtils;


    /**
     * 获取所有消息及其实例信息
     * @param params
     * @return
     */
    @Override
    public JSONObject getAllMessageInfo(SearchParams params) {
        List<AirmessageInstanceInfo> resultList = null;
        Page<AirmessageInstanceInfo> page = null;
        try {
            // token
            String token = httpServletRequest.getHeader("token");
            if (!tokenUtils.isAdmin(token)) {
                return resultGenerator.getFreeResult(ResultCode.PERMISSION_FAIL);
            }

            // 分页处理
            page = new Page<>(params.getPageNum(), params.getPageSize());
            // 数据获取
            resultList = airmessageInstanceInfoMapper.getAllMessageInfo(page, params);
            page.setRecords(resultList);
            log.info("--------------------分页信息----------------------------");
            log.info("--------------------总条数：" + page.getTotal() + "，当前页码：" + page.getCurrent() + "，总页码：" + page.getPages() + "，每页显示条数：" + page.getSize() + "，是否有上一页：" + page.hasPrevious() + "，是否有下一页：" + page.hasNext());
        } catch (Exception e) {
            e.printStackTrace();
            return resultGenerator.getFreeResult(ResultCode.FAIL);
        }

        return resultGenerator.getFreeResult(ResultCode.SUCCESS, new Object[]{"result"}, page);
    }

    /**
     * 返回通知实例以及对应消息详情信息，适用于指定用户获取
     * @param params 前端的的传参，应该为分页参数，筛选条件，传参形式json
     * @return
     */
    @Override
    public JSONObject getInstanceByParams(Integer getId, SearchParams params) {
        List<AirmessageInstanceInfo> resultList = null;
        Page<AirmessageInstanceInfo> page = null;
        try {
            // token
            String token = httpServletRequest.getHeader("token");
            if (!tokenUtils.isAdmin(token) && getId != null) {
                if (getId != params.getUserId()) {
                    return resultGenerator.getFreeResult(ResultCode.PERMISSION_FAIL);
                }
            }

            // 根据用户id拉取实例
            if (getId != null) {
                params.setUserId(getId);
            }

            int flag = airmessageInstanceInfoMapper.pullInstanceByUserId(params.getUserId());

            // 根据用户等级拉取实例
            UserInfo userInfo = JSONObject.parseObject(JSONObject.toJSONString(userInfoService.getUserInfo(token).getData()), UserInfo.class);
            params.setAdminLevel(userInfo.getAdminLevel());
            flag = airmessageInstanceInfoMapper.pullInstanceByUserLevel(params);

            // 分页处理
            page = new Page<>(params.getPageNum(), params.getPageSize());
            // 数据获取
            resultList = airmessageInstanceInfoMapper.getAllExampleMessageInfo(page, params);
            page.setRecords(resultList);
            log.info("--------------------分页信息----------------------------");
            log.info("--------------------总条数：" + page.getTotal() + "，当前页码：" + page.getCurrent() + "，总页码：" + page.getPages() + "，每页显示条数：" + page.getSize() + "，是否有上一页：" + page.hasPrevious() + "，是否有下一页：" + page.hasNext());
        } catch (Exception e) {
            e.printStackTrace();
            return resultGenerator.getFreeResult(ResultCode.FAIL);
        }

        return resultGenerator.getFreeResult(ResultCode.SUCCESS, new Object[]{"result"}, page);
    }

    /**
     * 获取用户所有未读实例，只显示固定数量的数据
     * @param params
     * @return
     */
    @Override
    public JSONObject getUserUnreadExample(SearchParams params) {
        List<AirmessageInstanceInfo> resultList = new ArrayList<>();
        try {
            if (params != null) {
                // 根据用户id拉取实例
                airmessageInstanceInfoMapper.pullInstanceByUserId(params.getUserId());

                // 根据用户等级拉取实例
                String token = httpServletRequest.getHeader("token");
                UserInfo userInfo = JSONObject.parseObject(JSONObject.toJSONString(userInfoService.getUserInfo(token).getData()), UserInfo.class);
                params.setAdminLevel(userInfo.getAdminLevel());
                int flag = airmessageInstanceInfoMapper.pullInstanceByUserLevel(params);

                // 数据获取
                resultList = airmessageInstanceInfoMapper.getUserUnreadExample(params);
                if (resultList.size() < 4) {
                    params.setShowNum(resultList.size());
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            return resultGenerator.getFreeResult(ResultCode.FAIL);
        }

        return resultGenerator.getFreeResult(ResultCode.SUCCESS, new Object[]{"result", "total"},
                resultList.subList(0, params.getShowNum()), resultList.size());
    }

    /**
     * 根据实例id更新阅读状态
     * @param instanceId
     * @return
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public JSONObject updateInstanceStatus(int instanceId, int userId) {

        try {
            airmessageInstanceInfoMapper.updateInstanceStatus(instanceId, userId);
        } catch (Exception e) {
            e.printStackTrace();
            return resultGenerator.getFreeResult(ResultCode.UPDATE_FAIL);
        }

        return resultGenerator.getFreeResult(ResultCode.SUCCESS);
    }

    /**
     * 根据实例Id，删除实例，同时删除过期实例
     * @param params
     * @return
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public JSONObject removeInstanceById(List<Integer> params) {

        try {
            // 清除目标实例以及过期实例数据
            for (Integer instanceId : params) {
                AirmessageInstanceInfo exampleInfo = airmessageInstanceInfoMapper.selectById(instanceId);
                if (exampleInfo == null) {
                    return resultGenerator.getFreeResult(ResultCode.DELETE_NULL_FAIL);
                }
                airmessageInstanceInfoMapper.removeInstanceById(instanceId);
                airmessageInstanceInfoMapper.removeOverdueInstance(exampleInfo);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return resultGenerator.getFreeResult(ResultCode.DELETE_FAIL);
        }

        return resultGenerator.getFreeResult(ResultCode.SUCCESS, new Object[]{"result"}, params);
    }

    /**
     * 获取当前用户未读实例个数
     * @return
     */
    @Override
    public JSONObject getUnreadExampleCount(SearchParams params) {
        int total = 0;
        try {
            // 数据获取
            List<AirmessageInstanceInfo> userUnreadExample = airmessageInstanceInfoMapper.getUserUnreadExample(params);
            if (userUnreadExample != null) {
                total = userUnreadExample.size();
            }

        } catch (Exception e) {
            e.printStackTrace();
            return resultGenerator.getFreeResult(ResultCode.FAIL);
        }
        return resultGenerator.getFreeResult(ResultCode.SUCCESS, new Object[]{"result"}, total);
    }
}
