package tech.sollabs.ragnarok.aop;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * intercept around method via AOP to manage task count for Ragnarok.
 *
 * @author Cyan Raphael Yi
 * @since 0.2.0
 * @see TaskCounterAspect
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface TaskCounter {

    /**
     * register taskKey for manage task count.
     * if taskKey is null <code>Empty</code>, method name will be use.
     *
     * @return taskKey
     */
    String value() default "";
}
