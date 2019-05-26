package tech.sollabs.ragnarok.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import tech.sollabs.ragnarok.TaskWatcher;

import java.lang.reflect.Method;

@Aspect
public class TaskCounterAspect {

    private TaskWatcher watcher;

    @Around("@annotation(tech.sollabs.ragnarok.aop.TaskCounter)")
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
        TaskCounter counterAnnotation = method.getAnnotation(TaskCounter.class);

        String taskKey = counterAnnotation.value();

        if (taskKey.isEmpty()) {
            taskKey = methodSignature.toShortString();
        }

        return taskKey;
    }

    @Autowired
    public void setWatcher(TaskWatcher watcher) {
        this.watcher = watcher;
    }
}
