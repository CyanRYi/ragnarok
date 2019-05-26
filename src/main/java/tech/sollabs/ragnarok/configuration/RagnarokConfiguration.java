package tech.sollabs.ragnarok.configuration;

import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import tech.sollabs.ragnarok.TaskWatcher;
import tech.sollabs.ragnarok.actuator.RagnarokHealthIndicator;
import tech.sollabs.ragnarok.web.ServletWebFilter;
import tech.sollabs.ragnarok.web.WebfluxWebFilter;

/**
 * Greceful Shut down Configuration for Ragnarok module
 *
 * @author Cyan Raphael Yi
 * @since 0.1.0
 * @see RagnarokConfiguration
 */
@Configuration
public class RagnarokConfiguration {

    @Bean
    public TaskWatcher taskWatcher() {
        return new TaskWatcher();
    }

    @ConditionalOnClass(HealthIndicator.class)
    @Bean
    public HealthIndicator ragnarokHealthIndicator() {
        return new RagnarokHealthIndicator(taskWatcher());
    }

    @ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
    @Bean
    public ServletWebFilter ragnarokFilter() {
        return new ServletWebFilter(taskWatcher());
    }

    @ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.REACTIVE)
    @Bean
    public WebfluxWebFilter ragnarokWebFilter() {
        return new WebfluxWebFilter(taskWatcher());
    }
}
