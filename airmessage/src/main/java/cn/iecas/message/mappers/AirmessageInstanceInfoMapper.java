package cn.iecas.message.mappers;

import cn.iecas.message.domain.AirmessageInstanceInfo;
import cn.iecas.message.domain.SearchParams;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper()
public interface AirmessageInstanceInfoMapper extends BaseMapper<AirmessageInstanceInfo> {

    /**
     * 获取所有通知信息，同时获取消息阅读推送情况
     * @return
     */
    List<AirmessageInstanceInfo> getExampleInfoById();

    /**
     * 获取与消息实例关联的消息详情信息
     * @return
     */
    List<AirmessageInstanceInfo> getAllExampleMessageInfo(Page<AirmessageInstanceInfo> page, @Param(value = "params") SearchParams params);

    /**
     * 返回通知实例以及对应消息详情信息，适用于指定用户获取
     * @return
     */
    List<AirmessageInstanceInfo> getInstanceByParams(Page<AirmessageInstanceInfo> page, @Param(value = "params") SearchParams params);

    /**
     * 创建新消息通知
     * @param AirMessageInstanceInfo
     */
    void createNewMessageExample(AirmessageInstanceInfo AirMessageInstanceInfo);

    /**
     * 根据实例信息删除实例信息
     * @param exampleInfo
     * @return
     */
    int removeExampleByExampInfo(AirmessageInstanceInfo exampleInfo);

    /**
     * 获取当前用户所有未读实例，只显示固定数量数据
     * @param params
     * @return
     */
    List<AirmessageInstanceInfo> getUserUnreadExample(SearchParams params);

    /**
     * 根据实例id更新实例阅读状态
     * @param instanceId
     * @return
     */
    int updateInstanceStatus(int instanceId, int userId);

    /**
     * 根据实例ID删除实例
     * @param instanceId
     * @return
     */
    int removeInstanceById(int instanceId);

    /**
     * 根据消息id删除实例
     * @param messageId
     * @return
     */
    int removeInstanceByMessageId(int messageId);

    /**
     * 清除早于目标对象的实例数据
     * @param exampleInfo
     * @return
     */
    int removeOverdueInstance(AirmessageInstanceInfo exampleInfo);

    /**
     * 根据用户id拉取实例
     * @param userId
     * @return
     */
    int pullInstanceByUserId(int userId);


    /**
     * 根据用户等级拉取
     * @param params
     * @return
     */
    int pullInstanceByUserLevel(@Param(value = "params") SearchParams params);

    /**
     * 获取所有消息以及统计信息
     * @param page
     * @param params
     * @return
     */
    List<AirmessageInstanceInfo> getAllMessageInfo(Page<AirmessageInstanceInfo> page, SearchParams params);
}
