package cn.iecas.message.service.impl;

import cn.aircas.authentication.common.CommonResult;
import cn.aircas.authentication.entity.UserInfo;
import cn.aircas.authentication.service.UserInfoService;
import cn.aircas.authentication.service.impl.UserInfoServiceImpl;
import cn.iecas.message.domain.*;
import cn.iecas.message.mappers.AirmessageInstanceInfoMapper;
import cn.iecas.message.mappers.AirmessageTemplateInfoMapper;
import cn.iecas.message.service.AirmessageTemplateInfoService;
import cn.iecas.message.service.remote.RemoteUserServiceImpl;
import cn.iecas.message.utils.DateUtils;
import cn.iecas.message.utils.TokenUtils;
import cn.iecas.message.utils.UserUtils;
import cn.iecas.message.utils.resultjson.ResultCode;
import cn.iecas.message.utils.resultjson.ResultGenerator;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service(value = "templateInfoService")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class AirmessageTemplateInfoServiceImpl implements AirmessageTemplateInfoService {

    @Autowired
    private ResultGenerator resultGenerator;

    @Autowired
    private AirmessageTemplateInfoMapper airmessageTemplateInfoMapper;

    @Autowired
    private AirmessageInstanceInfoMapper airmessageInstanceInfoMapper;

    @Autowired
    private HttpServletRequest httpServletRequest;

    @Autowired
    private TokenUtils tokenUtils;

    @Autowired
    private UserUtils userUtils;

    @Autowired
    private DateUtils dateUtils;

    @Autowired
    private RemoteUserServiceImpl userService;

    /**
     * 创建新的消息通知
     * @param messageInfo 通知消息详情
     * @return 创建的消息以及用户id
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public JSONObject createNewMessageExample(AirmessageTemplateInfo messageInfo) {
        AirmessageTemplateInfo latestMessage = null;
        AirmessageInstanceInfo exampleInfo = null;
        try {
            String token = httpServletRequest.getHeader("token");
            if (!tokenUtils.isAdmin(token)) {
                return resultGenerator.getFreeResult(ResultCode.PERMISSION_FAIL);
            }

            // 获取创建时间
            Timestamp timestamp = new Timestamp(new Date().getTime());
            if (messageInfo.getInvalidatedTime().compareTo(timestamp) <= 0) {
                return resultGenerator.getFreeResult(ResultCode.DATA_INVALID_FAIL);
            }
            messageInfo.setCreateTime(new Timestamp(new Date().getTime()));

            // 处理目标等级
            String[] userLevels;
            String userLevel = "";
            if (messageInfo.getTargetUserLevel() != null) {
                userLevel = "$";
                userLevels = messageInfo.getTargetUserLevel().replaceAll(" ", "").split(",");
                for (int i = 0; i < userLevels.length; i++) {
                    userLevel = userLevel.concat(userLevels[i] + "$");
                }
            }
            messageInfo.setTargetUserLevel(userLevel);

            // 处理服务ID
            if (messageInfo.getServiceId() == null && ServiceManager.serviceId != 0) {
                messageInfo.setServiceId(ServiceManager.serviceId);
            } else {
                messageInfo.setServiceId(0);
            }

            if (messageInfo.isProclamation())
                messageInfo.setProclamation(true);

            // 创建消息
            int resultFlag = airmessageTemplateInfoMapper.createNewMessageExample(messageInfo);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("执行异常，异常信息为 {}", e.getMessage());
            return resultGenerator.getFreeResult(ResultCode.CREATE_MESSAGE_FAIL);
        }

        return resultGenerator.getFreeResult(ResultCode.SUCCESS, new Object[]{"result"}, messageInfo);
    }

    /**
     * 批量删除消息通知，获取删除指定通知
     * @param messageInfos
     * @return
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public JSONObject removeManyMessageExample(Integer... messageInfos) {
        try {
            // token
            String token = httpServletRequest.getHeader("token");
            if (!tokenUtils.isAdmin(token)) {
                return resultGenerator.getFreeResult(ResultCode.PERMISSION_FAIL);
            }

            // 获取删除列表
            for (Integer messageId : messageInfos) {
                // 获取删除实例需要的参数
                AirmessageTemplateInfo messageInfo = airmessageTemplateInfoMapper.getMessageInfoById(messageId);
                Integer targetUserId = messageInfo.getTargetUserId();

                // 删除消息
                airmessageTemplateInfoMapper.removeMessageById(messageId);

                // 清除目标实例以及过期实例数据
                airmessageInstanceInfoMapper.removeInstanceByMessageId(messageId);
                airmessageInstanceInfoMapper.removeOverdueInstance(new AirmessageInstanceInfo(messageId));

            }
        } catch (Exception e) {
            log.error("执行异常，异常信息为 {}", e.getMessage());
            return resultGenerator.getFreeResult(ResultCode.DELETE_FAIL);
        }

        return resultGenerator.getFreeResult(ResultCode.SUCCESS, new Object[]{"result"}, messageInfos);
    }

    /**
     * 根据ID获取消息详情
     * @return 消息详情
     */
    @Override
    public JSONObject getMessageInfoById(Integer messageId) {
        AirmessageTemplateInfo messageInfo = null;
        try {
            messageInfo = airmessageTemplateInfoMapper.getMessageInfoById(messageId);
            if (messageInfo == null) {
                return resultGenerator.getFreeResult(ResultCode.GET_NULL_FAIL, new Object[]{"result"}, messageId, messageInfo);
            }
        } catch (Exception e) {
            log.error("执行异常，异常信息为 {}", e.getMessage());
            return resultGenerator.getFreeResult(ResultCode.FAIL);
        }

        return resultGenerator.getFreeResult(ResultCode.SUCCESS, new Object[]{"messageId","result"}, messageId, messageInfo);
    }

    /**
     * 根据ID更新通知以及通知实例
     * @param messageInfo
     * @return 更新后的消息通知
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public JSONObject updateMessageByMessageId(AirmessageTemplateInfo messageInfo) {
        try {
            // token
//            if (!tokenUtils.isAdmin()) {
//                return resultGenerator.getFreeResult(ResultCode.PERMISSION_FAIL);
//            }
            // 获取更新对象
            AirmessageTemplateInfo messageInfoById = airmessageTemplateInfoMapper.getMessageInfoById(messageInfo.getId());
            // 时间
            Timestamp updateTime = new Timestamp(new Date().getTime());
            messageInfo.setCreateTime(updateTime);
            // 有效时间处理
            if (messageInfo.getInvalidatedTime() != null) {
                if (messageInfo.getInvalidatedTime().compareTo(updateTime) <= 0) {
                    return resultGenerator.getFreeResult(ResultCode.DATA_INVALID_FAIL);
                }
            } else {
                if (messageInfoById.getInvalidatedTime().compareTo(updateTime) <= 0) {
                    return resultGenerator.getFreeResult(ResultCode.DATA_INVALID_FAIL);
                }
            }
            // 处理目标等级
            String[] userLevels;
            String userLevel = "";
            if (messageInfo.getTargetUserLevel() != null) {
                userLevel = "$";
                userLevels = messageInfo.getTargetUserLevel().replaceAll(" ", "").split(",");
                for (int i = 0; i < userLevels.length; i++) {
                    userLevel = userLevel.concat(userLevels[i] + "$");
                }
            }
            messageInfo.setTargetUserLevel(userLevel);

            // 更新消息
            airmessageTemplateInfoMapper.updateMessageByMessageId(messageInfo);

            // 清除相关实例
            airmessageInstanceInfoMapper.removeInstanceByMessageId(messageInfo.getId());
            airmessageInstanceInfoMapper.removeOverdueInstance(new AirmessageInstanceInfo(messageInfo.getId()));

        } catch (Exception e) {
            log.error("执行异常，异常信息为 {}", e.getMessage());
            return resultGenerator.getFreeResult(ResultCode.UPDATE_FAIL);
        }

        return resultGenerator.getFreeResult(ResultCode.SUCCESS, new Object[]{"result"}, messageInfo);
    }

    /**
     * 获取所有消息统计信息
     * @return
     */
    @Override
    public JSONObject getAllMessageInfoStatis(SearchParams params) {
        Map<String ,AirmessageTemplateInfoStatis> resultMap = new HashMap<String, AirmessageTemplateInfoStatis>(){{
            put("1", new AirmessageTemplateInfoStatis(1, "普通"));
            put("2", new AirmessageTemplateInfoStatis(2, "警告"));
            put("3", new AirmessageTemplateInfoStatis(3, "严重"));
            put("4", new AirmessageTemplateInfoStatis(4, "致命"));
        }};
        List<AirmessageTemplateInfoStatis> allMessageInfoStatis = null;
        Page<AirmessageTemplateInfoStatis> page = null;
        Integer messageNums = 0;
        try {
            // token
            /*if (!tokenUtils.isAdmin()) {
                return resultGenerator.getFreeResult(ResultCode.PERMISSION_FAIL);
            }*/

            // 获取时间范围内的所有统计信息
            page = new Page<>(params.getPageNum(), params.getPageSize());
            allMessageInfoStatis = airmessageTemplateInfoMapper.getAllMessageInfoStatis(page, params);

            page.setRecords(allMessageInfoStatis);
            log.info("--------------------分页信息----------------------------");
            log.info("--------------------总条数：" + page.getTotal() + "，当前页码：" + page.getCurrent() + "，总页码：" + page.getPages() + "，每页显示条数：" + page.getSize() + "，是否有上一页：" + page.hasPrevious() + "，是否有下一页：" + page.hasNext());

            // 判定执行结果
            if (allMessageInfoStatis == null) {
                return resultGenerator.getFreeResult(ResultCode.GET_NULL_FAIL, new Object[]{"result"}, page);
            }

            // 计算各类型占比
            messageNums = airmessageTemplateInfoMapper.getMessageNums();
            Integer finalMessageNums = messageNums;
            allMessageInfoStatis.forEach(msg->{
                msg.setProportion(new DecimalFormat("#.##").format(((Double.valueOf(msg.getMsgTotal())*100)/ finalMessageNums)));
            });

            for (AirmessageTemplateInfoStatis infoStati : allMessageInfoStatis) {
                resultMap.put(String.valueOf(infoStati.getNotificationLevel()), infoStati);
            }
            allMessageInfoStatis = new ArrayList<AirmessageTemplateInfoStatis>(){
                {
                    add(resultMap.get("1"));add(resultMap.get("2"));add(resultMap.get("3"));add(resultMap.get("4"));
                }
            };

            // 统计维度处理
            /*if (params.getStandard() != null) {
                statisList = this.getStatisByDateStandard(params);
            }*/

        } catch (Exception e) {
            log.error("执行异常，异常信息为 {}", e.getMessage());
            return resultGenerator.getFreeResult(ResultCode.FAIL);
        }

        return resultGenerator.getFreeResult(ResultCode.SUCCESS, new Object[]{"total", "result"},
                messageNums, allMessageInfoStatis);
    }

    /**
     * 获取所有消息以及推送消息
     * @param params
     * @return
     */
    @Override
    public JSONObject getMessageAndstatis(SearchParams params) {
        List<AirmessageTemplateInfo> resultList = null;
        Page<AirmessageTemplateInfo> page = null;
        Set targetIdSets = null;
        Map<String, String> map = null;
        CommonResult<UserInfo> result_users = null;
        try {
            // token
            String token = httpServletRequest.getHeader("token");
            // 分页获取数据处理
            UserInfo userInfoByToken = userUtils.getUserInfoByToken(token);
            page = new Page<>(params.getPageNum(), params.getPageSize());
            resultList = airmessageTemplateInfoMapper.getMessageAndstatis(page, params);
            long start = System.currentTimeMillis();
            if (!tokenUtils.isAdmin(token)) {
                resultList = resultList.stream().filter(r -> {
                    boolean flag = false;
                    if (r.getTargetUserLevel() != null) {
                        flag = r.getTargetUserLevel().contains("$"+userInfoByToken.getAdminLevel()+"$");
                    }
                    if (r.getTargetUserId() != null)
                        flag = r.getTargetUserId() == userInfoByToken.getId();

                    return flag;
                }).collect(Collectors.toList());
                // 设置用户信息
                for (AirmessageTemplateInfo info : resultList) {
                    if (info.getTargetUserId() != null && info.getTargetUserId() != 0) {
                            info.setUserName(userInfoByToken.getName());
                            info.setRealName(userInfoByToken.getRealName());
                    }
                }
                log.info("普通用户耗时： {}", (System.currentTimeMillis()-start));
            } else {
                // 获取target用户信息
                if (resultList != null && resultList.size() != 0) {
                    targetIdSets = new HashSet();
                    for (AirmessageTemplateInfo info : resultList) {
                        if (info.getTargetUserId() != null) {
                            targetIdSets.add(info.getTargetUserId());
                        }
                    }
                } else {
                    return resultGenerator.getFreeResult(ResultCode.SUCCESS, "result", null);
                }
                map = new HashMap();
                map.put("user_ids", JSONObject.toJSONString(new ArrayList<>(targetIdSets)));
                result_users = userService.getUserInfo(token, map);
                List<Map<String, Object>> usersInfo = (List<Map<String, Object>>) result_users.getData();
                // 设置用户信息
                for (AirmessageTemplateInfo info : resultList) {
                    if (info.getTargetUserId() != null && info.getTargetUserId() != 0) {
                        for (Map<String, Object> userInfo : usersInfo) {
                            if (userInfo.get("id") == info.getTargetUserId()) {
                                info.setUserName((String) userInfo.get("name"));
                                info.setRealName((String) userInfo.get("realname"));
                            }
                        }
                    }
                }
                log.info("管理员用户耗时： {}", (System.currentTimeMillis()-start));
            }


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
     * 返回之后的统计信息
     * @param params
     * @return
     */
    @Override
    public JSONObject getBeforeMessageInfoStatis(SearchParams params) {
        return null;
    }

    /**
     * 以时间基准维度统计消息 弃用
     * @param params
     * @return
     * @throws ParseException
     */
    /*private List<Object> getStatisByDateStandard(SearchParams params) throws ParseException {
        boolean flag = true;
        Date lastTime = null;
        List<Object> statis = new Vector<>();
        Page<AirmessageTemplateInfo> page = null;
        page = new Page<>(params.getPageNum(), params.getPageSize());
        // 时间处理
        if (params.getStartTime() == null) {
            params.setStartTime(new Date());
        }
        if (params.getEndTime() == null) {
            params.setEndTime(new Date());
        }
        // 开始处理
        Date startTime = params.getStartTime();
        Date endTime = params.getEndTime();
        String standard = params.getStandard().toUpperCase();
        while (flag) {
            params.setStartTime(startTime);
            if (standard.equals("DAY")) { // 筛选维度
                lastTime = dateUtils.getDayLastTime(startTime);
            } else if (standard.equals("WEEK")) {
                lastTime = dateUtils.getWeekLastTime(startTime);
            } else if (standard.equals("MONTH")) {
                lastTime = dateUtils.getMonthLastTime(startTime);
            }

            if (lastTime.compareTo(endTime) >= 0) {
                lastTime = endTime;
                flag = false;
            }
            params.setEndTime(lastTime);
            // 返回List类型统计数据
            List<AirmessageTemplateInfo> singleStatis = airmessageTemplateInfoMapper.getAllMessageInfoStatis(page, params);
            for (AirmessageTemplateInfo singleStati : singleStatis) {
                String validDate = dateUtils.getValidDate(params.getStartTime(), params.getStandard());
                singleStati.setStartTime(validDate);
            }
            statis.add(singleStatis);
            // 返回map类型统计数据
            //maps.put(dateUtils.getValidDate(params.getStartTime(), params.getStandard()), statis);

            // 通用筛选维度：下一天、周、月开始时间
            startTime = dateUtils.getTomorrowStartTime(lastTime);

            log.info("--------------------分页信息----------------------------");
            log.info("--------------------总条数：" + page.getTotal() + "，当前页码：" + page.getCurrent() + "，总页码：" + page.getPages() + "，每页显示条数：" + page.getSize() + "，是否有上一页：" + page.hasPrevious() + "，是否有下一页：" + page.hasNext());
        }

        return statis;
    }*/

}
