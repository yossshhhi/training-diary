package kz.yossshhhi.aop;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Indicates that the activity of a method or an entire class should be logged.
 * This annotation can be used to automatically log entry and exit points of methods,
 * or other significant runtime events, depending on the aspect-oriented programming
 * setup within the application.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE})
public @interface Loggable {
}
