package tech.sollabs.ragnarok.web;

import org.springframework.web.filter.OncePerRequestFilter;
import tech.sollabs.ragnarok.RagnarokWatcher;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class RagnarokFilter extends OncePerRequestFilter {

    private RagnarokWatcher ragnarokWatcher;

    public RagnarokFilter(RagnarokWatcher ragnarokWatcher) {
        this.ragnarokWatcher = ragnarokWatcher;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest servletRequest, HttpServletResponse servletResponse, FilterChain filterChain) throws ServletException, IOException {

        if (ragnarokWatcher.isShuttingDown()) {
            servletResponse.sendError(HttpServletResponse.SC_SERVICE_UNAVAILABLE);
        } else {
            try {
                ragnarokWatcher.increaseRequestCount();
                filterChain.doFilter(servletRequest, servletResponse);
            } finally {
                ragnarokWatcher.decreaseRequestCount();
            }
        }
    }
}
