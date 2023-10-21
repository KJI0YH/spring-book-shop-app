package com.example.mybookshopapp.aop;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.servlet.http.HttpServletRequest;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.text.MessageFormat;
import java.util.logging.Logger;

@Aspect
@Component
public class LoggingPathsAspect {
    private final Logger logger = Logger.getLogger(this.getClass().getName());
    private final ObjectMapper mapper;

    public LoggingPathsAspect() {
        mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
    }

    @Pointcut(value = "@annotation(org.springframework.web.bind.annotation.GetMapping) || @annotation(org.springframework.web.bind.annotation.PostMapping)")
    public void loggingPathsAspectPointcut() {
    }

    @Before("loggingPathsAspectPointcut()")
    public void logBeforeMapping(JoinPoint joinPoint) {

//        // Get information about method, host and path of the request
//        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
//        String requestPath = request.getRequestURI();
//        String requestMethod = request.getMethod();
//        String requestHost = request.getHeader("host");
//        StringBuilder builder = new StringBuilder(MessageFormat.format("{0} {1}{2}", requestMethod, requestHost, requestPath));
//
//        // Get query parameters of the request
//        String queryParams = request.getQueryString();
//        if (queryParams != null && !queryParams.isEmpty()) {
//            builder.append('?').append(queryParams);
//        }
//
//        Object[] args = joinPoint.getArgs();
//
//        // Get method arguments of the request
//        if (args != null) {
//            builder.append(" Args: ");
//            for (Object arg : args) {
//                try {
//                    builder.append(mapper.writeValueAsString(arg));
//                } catch (JsonProcessingException ignored) {
//                }
//            }
//        }
//        logger.info(builder.toString());
    }

    @AfterReturning(pointcut = "loggingPathsAspectPointcut()", returning = "result")
    public void logAfterReturningMapping(JoinPoint joinPoint, Object result) {
        try {
            logger.info(("Return: " + mapper.writeValueAsString(result)));
        } catch (JsonProcessingException ignored) {
        }
    }

    @AfterThrowing(pointcut = "loggingPathsAspectPointcut()", throwing = "exception")
    public void logAfterThrowingMapping(JoinPoint joinPoint, Throwable exception) {
        String methodName = joinPoint.getSignature().getName();
        logger.info(MessageFormat.format("Exception occurred in method {0}: {1}", methodName, exception));
    }
}
