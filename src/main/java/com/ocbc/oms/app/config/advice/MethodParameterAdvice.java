package com.ocbc.oms.app.config.advice;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

/**
 * Print all interface parameters
 *
 * @author hzy
 * @since 2021-10-22
 */
@Component
@Aspect
@Slf4j
public class MethodParameterAdvice {

    private static final String POINT_CUT_EXECUTION = "execution(public * com.ocbc.oms.app.api.*.*(..))";


    @Pointcut(value = POINT_CUT_EXECUTION)
    public void point() {
    }

    /**
     * method print Before
     *
     * @param joinPoint joinPoint
     */
    @Before(value = "point()")
    public void before(JoinPoint joinPoint) {
        Signature signature = joinPoint.getSignature();
        log.info("Method：{},Parameter：{}", signature.toShortString(), JSON.toJSONString(joinPoint.getArgs()));
    }

    @AfterReturning(value = "point()", returning = "rvt")
    public void after(JoinPoint joinPoint, Object rvt) {
        Signature signature = joinPoint.getSignature();
        log.info("Method：{},result：{}", signature.toShortString(), JSON.toJSONString(rvt));
    }
}
