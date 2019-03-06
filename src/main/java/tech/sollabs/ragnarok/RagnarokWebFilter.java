package tech.sollabs.ragnarok;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;
import tech.sollabs.ragnarok.configuration.RagnarokConfiguration;

public class RagnarokWebFilter implements WebFilter {

    private RagnarokWatcher ragnarokWatcher;

    public RagnarokWebFilter(RagnarokWatcher ragnarokWatcher) {
        this.ragnarokWatcher = ragnarokWatcher;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {

        if (RagnarokConfiguration.requestedShutdown) {
            return Mono.error(new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "shut down"));
        }

        ragnarokWatcher.increaseRequestCount();

        return chain.filter(exchange)
                .doFinally(signalType -> ragnarokWatcher.decreaseRequestCount());
    }
}
