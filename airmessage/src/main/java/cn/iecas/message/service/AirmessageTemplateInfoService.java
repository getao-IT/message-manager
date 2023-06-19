package cn.iecas.message.service;

import cn.iecas.message.domain.AirmessageTemplateInfo;
import cn.iecas.message.domain.SearchParams;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import java.util.Date;
import java.util.List;

public interface AirmessageTemplateInfoService {

    JSONObject createNewMessageExample(AirmessageTemplateInfo messageInfo);

    JSONObject removeManyMessageExample(Integer... messageInfos);

    JSONObject getMessageInfoById(Integer messageId);

    JSONObject updateMessageByMessageId(AirmessageTemplateInfo messageInfo);

    JSONObject getAllMessageInfoStatis(SearchParams params);

    JSONObject getMessageAndstatis(SearchParams searchParams);

    JSONObject getBeforeMessageInfoStatis(SearchParams params);
}
