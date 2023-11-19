package com.example.mybookshopapp.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;
import java.util.logging.Logger;

@Aspect
@Component
public class LoggingPathsAspect {
    private final Logger logger = Logger.getLogger(this.getClass().getName());

    @Pointcut(value = "@annotation(org.springframework.web.bind.annotation.GetMapping) || @annotation(org.springframework.web.bind.annotation.PostMapping) || @annotation(org.springframework.web.bind.annotation.PutMapping) || @annotation(org.springframework.web.bind.annotation.DeleteMapping)")
    public void loggingPathsAspectPointcut() {
    }

    @AfterThrowing(pointcut = "loggingPathsAspectPointcut()", throwing = "exception")
    public void logAfterThrowingMapping(JoinPoint joinPoint, Throwable exception) {
        String methodName = joinPoint.getSignature().getName();
        String logMessage = MessageFormat.format("Exception occurred in method {0}: {1}", methodName, exception);
        logger.info(logMessage);
    }
}
