package kz.yossshhhi.aop;

import kz.yossshhhi.model.enums.AuditAction;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation to indicate that a method's execution should be audited.
 * This annotation should be used to automatically log actions taken,
 * typically for security and tracking purposes. It leverages the {@link AuditAction}
 * enum to specify the type of action being audited.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Auditable {
    AuditAction action();
}
