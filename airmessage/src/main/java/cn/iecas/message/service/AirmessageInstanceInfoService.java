package cn.iecas.message.service;

import cn.iecas.message.domain.SearchParams;
import com.alibaba.fastjson.JSONObject;

import java.util.List;

public interface AirmessageInstanceInfoService<T> {

    JSONObject getAllMessageInfo(SearchParams params);

    JSONObject getInstanceByParams(Integer getId, SearchParams params);

    JSONObject getUserUnreadExample(SearchParams params);

    JSONObject updateInstanceStatus(int instanceId, int userId);

    JSONObject removeInstanceById(List<Integer> params);

    JSONObject getUnreadExampleCount(SearchParams params);
}
