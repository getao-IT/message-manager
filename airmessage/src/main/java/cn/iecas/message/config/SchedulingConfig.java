package cn.iecas.message.config;

import cn.iecas.message.domain.AirmessageInstanceInfo;
import cn.iecas.message.domain.AirmessageTemplateInfo;
import cn.iecas.message.mappers.AirmessageInstanceInfoMapper;
import cn.iecas.message.mappers.AirmessageTemplateInfoMapper;
import cn.iecas.message.utils.resultjson.ResultCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.event.ApplicationEventMulticaster;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

@Slf4j
@Configuration
@EnableScheduling
@PropertySource(value = "classpath:/application-dev.yml")
public class SchedulingConfig {

    @Autowired
    private AirmessageTemplateInfoMapper airmessageTemplateInfoMapper;

    @Autowired
    private AirmessageInstanceInfoMapper airmessageInstanceInfoMapper;

    @Scheduled(cron = "${configtask.cron}")
    public void excuteScheduleClear() {
        log.info("----------------------start:执行定时任务：清理过期消息数据以及实例-----------------------");

        try {
            // 获取过期实例
            List<AirmessageTemplateInfo> invalidMessage = airmessageTemplateInfoMapper.getInvalidMessage();
            for (AirmessageTemplateInfo message : invalidMessage) {
                // 删除过期消息
                airmessageTemplateInfoMapper.removeMessageById(message.getId());

                // 清除目标实例以及过期实例数据
                airmessageInstanceInfoMapper.removeInstanceByMessageId(message.getId());
                airmessageInstanceInfoMapper.removeOverdueInstance(new AirmessageInstanceInfo(message.getId()));
            }

        } catch (Exception e) {
            log.error("执行异常，异常信息为 {}", e.getMessage());
        }

        log.info("----------------------end:执行定时任务：清理过期消息数据以及实例-----------------------");
    }
}
