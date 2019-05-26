package tech.sollabs.ragnarok.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import tech.sollabs.ragnarok.RagnarokWatcher;

import java.lang.reflect.Method;

@Aspect
public class RagnarokTaskAspect {

    private RagnarokWatcher watcher;

    @Around("@annotation(RagnarokCounter)")
    public Object countTasks(ProceedingJoinPoint joinPoint) throws Throwable {

        String taskKey = getTaskKey((MethodSignature) joinPoint.getSignature());

        try {
            watcher.increaseCount(taskKey);
            return joinPoint.proceed();
        } finally {
            watcher.decreaseCount(taskKey);
        }
    }

    private String getTaskKey(MethodSignature methodSignature) {

        Method method = methodSignature.getMethod();
        RagnarokCounter counterAnnotation = method.getAnnotation(RagnarokCounter.class);

        String taskKey = counterAnnotation.value();

        if (taskKey.isEmpty()) {
            taskKey = methodSignature.toShortString();
        }

        return taskKey;
    }

    @Autowired
    public void setWatcher(RagnarokWatcher watcher) {
        this.watcher = watcher;
    }
}
