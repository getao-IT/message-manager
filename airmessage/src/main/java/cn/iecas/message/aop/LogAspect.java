package cn.iecas.message.aop;

import cn.iecas.message.utils.resultjson.ResultGenerator;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component("logAspect")
public class LogAspect {

    @Autowired
    private ResultGenerator resultGenerator;

    @Pointcut("execution(* cn.iecas.message.service.impl.*.*(..))")
    private void pointcut(){};

    /**
     * 环绕通知，只处理前置后置通知
     * @param joinPoint
     * @return 处理成功后的json体
     * @throws Throwable
     */
    @Around("pointcut()")
    public Object aroundLog(ProceedingJoinPoint joinPoint) throws Throwable {
        JSONObject result = null;
        String className = joinPoint.getSignature().getDeclaringTypeName();
        String methodName = joinPoint.getSignature().getName();
        Object[] args = joinPoint.getArgs();

        log.info("进入 {} ，执行方法 {} ", className, methodName);
        for (Object arg : args) {
            log.info("执行参数 {}", arg);
        }

        try {
            result = (JSONObject) joinPoint.proceed(args);
            log.info("方法 {} 执行完毕，返回值为 {}", methodName, result);
//            Set<Map.Entry<String, Object>> entries = result.entrySet();
//            for (Map.Entry<String, Object> entry : entries) {
//                if (entry.getKey().equals("data")) {
//                    Map<String, Object> value = BeanMapUtils.beanToMap(entry.getValue());
//                    if (value.get("current") != null) {
//                        log.info("--------------------分页信息----------------------------");
//                        log.info("--------------------总条数：" + value.get("total") + "，当前页码：" + value.get("current") + "，总页码：" + value.get("pages") + "，每页显示条数：" + value.get("size"));
//                    }
//                }
//            }

            return result;
        } catch (Throwable throwable) {
            log.error("方法 {} 执行异常，异常信息为 {}", methodName, throwable.getMessage());
            throw new RuntimeException(throwable);
        }
    }
}