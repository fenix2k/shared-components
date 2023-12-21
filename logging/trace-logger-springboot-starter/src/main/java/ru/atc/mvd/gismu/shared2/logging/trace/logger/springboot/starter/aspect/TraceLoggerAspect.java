package ru.atc.mvd.gismu.shared2.logging.trace.logger.springboot.starter.aspect;

import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.LoggerFactory;
import ru.atc.mvd.gismu.shared2.logging.trace.logger.springboot.starter.annotation.TraceLogger;
import ru.atc.mvd.gismu.shared2.logging.trace.logger.springboot.starter.config.properties.TraceLoggerProperties;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Аспект для обработки логики аннотации TraceLogger.
 */
@Aspect
@Slf4j
@RequiredArgsConstructor
public class TraceLoggerAspect {

    private final TraceLoggerProperties configProps;

    @SuppressWarnings({"MissingJavadocMethod", "IllegalThrows"})
    @Around("@annotation(ru.atc.mvd.gismu.shared2.logging.trace.logger.springboot.starter.annotation.TraceLogger)")
    public Object traceLog(ProceedingJoinPoint joinPoint) throws Throwable {
        if (!log.isTraceEnabled()) {
            return joinPoint.proceed(joinPoint.getArgs());
        }
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();

        if (!getClassLoggerLevel(signature.getMethod().getDeclaringClass())) {
            return joinPoint.proceed(joinPoint.getArgs());
        }

        TraceLogger annotation = signature.getMethod().getAnnotation(TraceLogger.class);

        String methodName = getMethodName(signature, annotation);
        List<Object> methodArgs = getAllowedArgs(joinPoint.getArgs(), annotation);

        log.trace("{} executed with args: {}", methodName, methodArgs);

        long start = System.currentTimeMillis();
        Object proceed = joinPoint.proceed(joinPoint.getArgs());
        long executionTime = System.currentTimeMillis() - start;

        if (configProps.isShowReturnValue() && annotation.showReturnValue()) {
            log.trace("{} success (execution time: {} ms) with returning value: {}", methodName, executionTime, proceed);
        } else {
            log.trace("{} success (execution time: {} ms)", methodName, executionTime);
        }

        return proceed;
    }

    @SuppressWarnings({"MissingJavadocMethod", "IllegalThrows"})
    @AfterThrowing(
            pointcut = "@annotation(ru.atc.mvd.gismu.shared2.logging.trace.logger.springboot.starter.annotation.TraceLogger)",
            throwing = "ex")
    public void afterThrowing(JoinPoint joinPoint, Exception ex) {
        if (!log.isTraceEnabled()) {
            return;
        }
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        TraceLogger annotation = signature.getMethod().getAnnotation(TraceLogger.class);
        String methodName = getMethodName(signature, annotation);
        log.trace("{} threw an exception: " + ex, methodName);
    }

    private String getMethodName(MethodSignature signature, TraceLogger annotation) {
        if (configProps.isShowShortClassName()) {
            return signature.toShortString();
        }
        return annotation.showShortClassName() ? signature.toShortString() : signature.toLongString();
    }

    private List<Object> getAllowedArgs(Object[] args, TraceLogger annotation) {
        List<Object> methodArgs = new ArrayList<>();
        if (annotation.excludeArgIndex() != null && annotation.excludeArgIndex().length > 0) {
            Set<Integer> allowedArgIndexes = IntStream.of(annotation.excludeArgIndex())
                    .boxed().collect(Collectors.toSet());

            if (annotation.excludeArgIndex().length == 1 && allowedArgIndexes.contains(-1)) {
                return methodArgs;
            }

            for (int i = 0; i < args.length; i++) {
                if (!allowedArgIndexes.contains(i)) {
                    methodArgs.add(args[i]);
                }
            }
            return methodArgs;
        }
        return methodArgs;
    }

    private boolean getClassLoggerLevel(Class<?> clazz) {
        LoggerContext loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();
        Logger logger = loggerContext.getLogger(clazz);
        return logger.isTraceEnabled();
    }
}
