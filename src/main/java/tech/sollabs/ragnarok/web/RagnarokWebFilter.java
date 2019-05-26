package tech.sollabs.ragnarok.web;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;
import tech.sollabs.ragnarok.RagnarokWatcher;

public class RagnarokWebFilter implements WebFilter {

    private RagnarokWatcher ragnarokWatcher;

    public RagnarokWebFilter(RagnarokWatcher ragnarokWatcher) {
        this.ragnarokWatcher = ragnarokWatcher;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {

        if (ragnarokWatcher.isShuttingDown()) {
            return Mono.error(new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "Application Shutting Down"));
        }

        ragnarokWatcher.increaseRequestCount();

        return chain.filter(exchange)
                .doFinally(signalType -> ragnarokWatcher.decreaseRequestCount());
    }
}
