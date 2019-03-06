package tech.sollabs.ragnarok;

import org.springframework.context.annotation.Import;
import tech.sollabs.ragnarok.configuration.RagnarokConfiguration;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Enable Ragnarok Configuration
 *
 * @author Cyan Raphael Yi
 * @since 0.1.0
 * @see RagnarokConfiguration
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Import(RagnarokConfiguration.class)
public @interface EnableRagnarok {
}
