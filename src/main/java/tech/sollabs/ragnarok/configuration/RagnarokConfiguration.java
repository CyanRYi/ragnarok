package tech.sollabs.ragnarok.configuration;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import tech.sollabs.ragnarok.RagnarokFilter;
import tech.sollabs.ragnarok.RagnarokWatcher;
import tech.sollabs.ragnarok.RagnarokWebHandler;

import javax.annotation.PreDestroy;

/**
 * Ragnarok Configuration
 *
 * @author Cyan Raphael Yi
 * @since 0.1.0
 * @see RagnarokConfiguration
 */
@Configuration
public class RagnarokConfiguration {

    private Log log = LogFactory.getLog(getClass());
    public static boolean requestedShutdown = false;

    @Bean
    public RagnarokWatcher ragnarokWatcher() {
        return new RagnarokWatcher();
    }

    @ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
    @Bean
    public RagnarokFilter ragnarokFirewallFilter() {
        return new RagnarokFilter(ragnarokWatcher());
    }

    @ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.REACTIVE)
    @Bean
    public RagnarokWebHandler ragnarokFirewallHandler() {
        return new RagnarokWebHandler(ragnarokWatcher());
    }

    @PreDestroy
    public void shutdown() {

        if (!requestedShutdown) {
            requestedShutdown = true;
        }

        log.warn("Application will be shutting down soon");

        do {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {

                log.error("Thread interrupted while processing shutdown");
                e.printStackTrace();
            }
        } while (ragnarokWatcher().needWaiting());

        log.info("Good Bye");
    }
}
