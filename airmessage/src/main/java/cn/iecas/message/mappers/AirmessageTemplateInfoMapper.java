package cn.iecas.message.mappers;

import cn.iecas.message.domain.AirmessageInstanceInfo;
import cn.iecas.message.domain.AirmessageTemplateInfo;
import cn.iecas.message.domain.AirmessageTemplateInfoStatis;
import cn.iecas.message.domain.SearchParams;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.models.auth.In;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface AirmessageTemplateInfoMapper {

    /**
     * 获取所有通知信息，同时获取消息阅读推送情况
     * @return
     */
    List<AirmessageTemplateInfo> getAllMessageInfo();

    /**
     * 根据消息Id获取消息详情信息
     * @param messageId
     * @return
     */
    AirmessageTemplateInfo getMessageInfoById(int messageId);

    /**
     * 创建新的消息通知
     * @param messageInfo
     * @return
     */
    int createNewMessageExample(AirmessageTemplateInfo messageInfo);

    /**
     * 获取最新的消息
     * @return
     */
    AirmessageTemplateInfo getLatestMessageByTargetUserId(AirmessageTemplateInfo messageInfo);

    /**
     * 根据消息id删除
     * @param messageId
     * @return
     */
    int removeMessageById(Integer messageId);

    /**
     * 根据ID更新通知以及通知实例
     * @param messageInfo
     * @return 更新后的消息通知
     */
    int updateMessageByMessageId(AirmessageTemplateInfo messageInfo);

    /**
     * 获取所有消息统计信息
     * @return
     */
    List<AirmessageTemplateInfoStatis> getAllMessageInfoStatis(Page<AirmessageTemplateInfoStatis> page, SearchParams params);

    /**
     * 获取所有消息以及推送消息
     * @param page
     * @param params
     * @return
     */
    List<AirmessageTemplateInfo> getMessageAndstatis(Page<AirmessageTemplateInfo> page, SearchParams params);

    /**
     * 获取所有过期消息
     * @return
     */
    List<AirmessageTemplateInfo> getInvalidMessage();

    /**
     * 获取消息总数
     * @return
     */
    Integer getMessageNums();
}
