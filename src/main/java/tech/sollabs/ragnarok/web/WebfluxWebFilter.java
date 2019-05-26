package tech.sollabs.ragnarok.web;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;
import tech.sollabs.ragnarok.TaskWatcher;

/**
 * implementation WebFilter to manage web request for handle safety when application shut down.
 * not support Flux yet
 *
 * @author Cyan Raphael Yi
 * @since 0.1.0
 */
public class WebfluxWebFilter implements WebFilter {

    private TaskWatcher taskWatcher;

    public WebfluxWebFilter(TaskWatcher taskWatcher) {
        this.taskWatcher = taskWatcher;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {

        if (taskWatcher.isShuttingDown()) {
            return Mono.error(new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "Application Shutting Down"));
        }

        taskWatcher.increaseRequestCount();

        return chain.filter(exchange)
                .doFinally(signalType -> taskWatcher.decreaseRequestCount());
    }
}
