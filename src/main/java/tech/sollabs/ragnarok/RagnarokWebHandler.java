package tech.sollabs.ragnarok;

import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

public class RagnarokWebHandler implements WebFilter {

    private RagnarokWatcher ragnarokWatcher;

    public RagnarokWebHandler(RagnarokWatcher ragnarokWatcher) {
        this.ragnarokWatcher = ragnarokWatcher;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {

        ragnarokWatcher.increaseRequestCount();

        return chain.filter(exchange)
                .doFinally(signalType -> ragnarokWatcher.decreaseRequestCount());
    }
}
