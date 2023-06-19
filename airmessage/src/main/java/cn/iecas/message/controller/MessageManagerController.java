package cn.iecas.message.controller;


import cn.iecas.message.domain.AirmessageTemplateInfo;
import cn.iecas.message.domain.SearchParams;
import cn.iecas.message.service.AirmessageInstanceInfoService;
import cn.iecas.message.service.AirmessageTemplateInfoService;
import cn.iecas.message.utils.DateUtils;
import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

@Api(tags = "Message Manager", description = "全局消息管理-管理所有消息请求")
@Slf4j
@RestController
@RequestMapping("/airmessage")
public class MessageManagerController {

    @Autowired
    private AirmessageInstanceInfoService airmessageInstanceInfoService;

    @Autowired
    AirmessageTemplateInfoService airmessageTemplateInfoService;

    @Autowired
    private HttpServletRequest request;


    /**
     * 获取所有消息以及实例阅读统计
     * @return
     */
    @GetMapping(value = "/message")
    public JSONObject getMessageAndstatis(SearchParams searchParams) {
        return airmessageTemplateInfoService.getMessageAndstatis(searchParams);
    }

    /**
     * 创建新的消息通知
     * @param messageInfo 要创建的消息
     * @return 创建的消息以及消息实例数据详情
     * extemple
     *   {
     *   "notificationContent": "系统坏了",
     *   "notificationLevel": 1,  ** 传参：1 || 2 || 3 || 4
     *   "notificationTitle": "重要通知",
     *   "notificationType": "重要",
     *   "sourceUserId": 2,
     *   "serviceId": 0,
     *   "targetUserLevel": "1,2",
     *   "targetUserId": 1
     * }
     */
    @PostMapping (value = "/message")
    public JSONObject createNewMessageExample(@RequestBody AirmessageTemplateInfo messageInfo) throws IllegalAccessException, IOException, InstantiationException {
        return airmessageTemplateInfoService.createNewMessageExample(messageInfo);
    }

    /**
     * 批量删除消息与实例，或者指定删除
     * @param params 要删除的消息列表
     * @return 删除的消息列表
     */
    @DeleteMapping("/message")
    public JSONObject removeManyMessageExample(@RequestBody SearchParams params) {

        return airmessageTemplateInfoService.removeManyMessageExample(params.getParamsArr());
    }

    /**
     * 根据ID获取消息详情
     * @param messageId
     * @return 消息详情
     */
    @GetMapping("/message/{messageId}")
    public JSONObject getMessageInfoById(@PathVariable(name = "messageId") Integer messageId) {
        return airmessageTemplateInfoService.getMessageInfoById(messageId);
    }

    /**
     * 根据ID更新通知以及通知实例
     * @param messageInfo
     * @return 更新后的消息通知
     */
    @PutMapping("/message")
    public JSONObject updateMessageByMessageId(@RequestBody AirmessageTemplateInfo messageInfo) {
        return airmessageTemplateInfoService.updateMessageByMessageId(messageInfo);
    }

    /**
     * 获取所有消息统计信息
     * @return
     */
    @GetMapping("message/statis")
    public JSONObject getAllMessageInfoStatis(SearchParams searchParams) {
        return airmessageTemplateInfoService.getAllMessageInfoStatis(searchParams);
    }

    /**
     * 获取指定用户的所有消息实例
     * @return pageInfo 返回分页对象，可使用pageInfo.list方式过去分页数据
     */
    @GetMapping("/message/instance/user/{userId}")
    public JSONObject getUserAllExampleById(@PathVariable(name = "userId") Integer getId,
                                                 SearchParams params) {
        return airmessageInstanceInfoService.getInstanceByParams(getId, params);
    }

    /**
     * 获取当前用求户的所有消息实例
     *      * @param params 分页请信息：其中当前用户id（userId）、分页属性当前页（pageNum）、每页大小（pageSize）必须添加，其他筛选条件自选
     * @return pageInfo 返回分页对象，可使用pageInfo.list方式过去分页数据
     */
    @GetMapping("/message/instance/user/current")
    public JSONObject getUserAllExampleById(SearchParams params) {
        return airmessageInstanceInfoService.getInstanceByParams(null, params);
    }

    /**
     * 获取当前用户的所有最新未读实例，给出一个固定数量的显示
     * @param params
     * @return pageInfo 返回分页对象，可使用pageInfo.list方式过去分页数据
     */
    @GetMapping("/message/instance/user/current/latest")
    public JSONObject getUserUnreadExample(SearchParams params) {
        return airmessageInstanceInfoService.getUserUnreadExample(params);
    }

    /**
     * 获取当前用户的所有最新未读实例个数统计
     * @return pageInfo 返回分页对象，可使用pageInfo.list方式过去分页数据
     */
    @GetMapping("/message/instance/user/current/latest/count")
    public JSONObject getUnreadExampleCount(SearchParams params) {
        return airmessageInstanceInfoService.getUnreadExampleCount(params);
    }

    /**
     * 根据实例ID更新实例状态
     * @param instanceId
     * @return
     */
    @PutMapping("/message/instance/{instance_id}/{user_id}")
    public JSONObject updateInstanceStatus(@PathVariable(name = "instance_id") int instanceId,
                                           @PathVariable(name = "user_id") int userId) {
        return airmessageInstanceInfoService.updateInstanceStatus(instanceId, userId);
    }

    /**
     * 根据实例ID删除实例，同时把比删除实例早的实例删除
     * @param params
     * @return
     */
    @DeleteMapping("/message/instance")
    public JSONObject removeInstanceById(@RequestParam(value = "params") List<Integer> params) {
        return airmessageInstanceInfoService.removeInstanceById(params);
    }

    @GetMapping("/message/before/statis")
    public JSONObject getBeforeMessageInfoStatis(SearchParams params) {
        return airmessageTemplateInfoService.getBeforeMessageInfoStatis(params);
    }

}
