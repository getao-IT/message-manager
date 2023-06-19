package cn.iecas.message.test;

import cn.iecas.message.domain.AirmessageTemplateInfo;
import cn.iecas.message.domain.SearchParams;
import cn.iecas.message.service.AirmessageTemplateInfoService;
import cn.iecas.message.service.impl.AirmessageTemplateInfoServiceImpl;
import cn.iecas.message.utils.CollectionUtils;
import cn.iecas.message.utils.DateUtils;
import com.alibaba.fastjson.JSONObject;
import io.swagger.models.auth.In;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import sun.java2d.loops.RenderLoops;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.ReentrantLock;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@EnableAsync
public class MessageTest {

    @Autowired
    private AirmessageTemplateInfoService templateInfoService;

    @Test
    public void test1() throws ParseException {
        DateUtils dateUtils = new DateUtils();

        Date tomorrowStartTime = dateUtils.getTomorrowStartTime(new Date());
        Timestamp timestamp = new Timestamp(tomorrowStartTime.getTime());
        System.out.println("第二天0点：" + timestamp + "\n---------------------=================");

        tomorrowStartTime = dateUtils.getDayLastTime(new Date());
        timestamp = new Timestamp(tomorrowStartTime.getTime());
        System.out.println("当天最后时间：" + timestamp + "\n---------------------=================");

        tomorrowStartTime = dateUtils.getMonthLastTime(new Date());
        timestamp = new Timestamp(tomorrowStartTime.getTime());
        System.out.println("当月最后时间：" + timestamp + "\n---------------------=================");

        tomorrowStartTime = dateUtils.getNextMonthStartTime(new Date());
        timestamp = new Timestamp(tomorrowStartTime.getTime());
        System.out.println("下个月开始时刻：" + timestamp + "\n---------------------=================");

        tomorrowStartTime = dateUtils.getWeekLastTime(new SimpleDateFormat("yyyy-MM-dd").parse("2021-12-08"));
        timestamp = new Timestamp(tomorrowStartTime.getTime());
        System.out.println("本周最后时刻：" + timestamp + "\n---------------------=================");

        tomorrowStartTime = dateUtils.getTomorrowStartTime(tomorrowStartTime);
        timestamp = new Timestamp(tomorrowStartTime.getTime());
        System.out.println("下周开始时间：" + timestamp + "\n---------------------=================");
    }

    @Test
    public void test2() {
        Map<String, Object> map = new HashMap<>();
        map.put("2021-10-01","aaaaa");
        map.put("2021-18-01","aaaaa");
        map.put("2021-11-01","aaaaa");
        map.put("2021-08-01","aaaaa");
        map.put("2021-10-05","aaaaa");

        List<Map.Entry<String, Object>> entries = CollectionUtils.sortMapByDate(map);
        for (Map.Entry<String, Object> entry : entries) {
            System.out.printf("key：" + entry.getKey() + "-----------value：" + entry.getValue() + "\n");
        }

    }

    @Test
    public void test3() throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date parse = format.parse("2021-10-20");
        System.out.println(parse);
    }

    @Test
    public void test4() throws ParseException {
        AirmessageTemplateInfoService service = new AirmessageTemplateInfoServiceImpl();
        AirmessageTemplateInfo templateInfo = new AirmessageTemplateInfo();
        templateInfo.setNotificationType("普通");
        //service.createNewMessageExample(templateInfo);
    }

    @Test
    public void test5() {
        Map<String, String> map = new HashMap<>();
        map.put(null, "abc");

        Map<String, String> table = new Hashtable<>();
        table.put(null, "abc");
        System.out.println(System.currentTimeMillis());
    }

    @Test
    public void test6() {
        SearchParams params = new SearchParams();
        params.setProclamation(true);
        JSONObject messageAndstatis = templateInfoService.getMessageAndstatis(params);
        System.out.println(messageAndstatis);
    }
}

