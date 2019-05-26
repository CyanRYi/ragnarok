package tech.sollabs.ragnarok.configuration;

import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import tech.sollabs.ragnarok.web.RagnarokFilter;
import tech.sollabs.ragnarok.RagnarokWatcher;
import tech.sollabs.ragnarok.web.RagnarokWebFilter;

/**
 * Ragnarok Configuration
 *
 * @author Cyan Raphael Yi
 * @since 0.1.0
 * @see RagnarokConfiguration
 */
@Configuration
public class RagnarokConfiguration {

    @Bean
    public RagnarokWatcher ragnarokWatcher() {
        return new RagnarokWatcher();
    }

    @ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
    @Bean
    public RagnarokFilter ragnarokFilter() {
        return new RagnarokFilter(ragnarokWatcher());
    }

    @ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.REACTIVE)
    @Bean
    public RagnarokWebFilter ragnarokWebFilter() {
        return new RagnarokWebFilter(ragnarokWatcher());
    }
}
