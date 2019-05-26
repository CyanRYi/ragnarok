package tech.sollabs.ragnarok.web;

import org.springframework.web.filter.OncePerRequestFilter;
import tech.sollabs.ragnarok.TaskWatcher;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Servlet filter to manage web request for handle safety when application shut down.
 *
 * @author Cyan Raphael Yi
 * @since 0.1.0
 */
public class ServletWebFilter extends OncePerRequestFilter {

    private TaskWatcher taskWatcher;

    public ServletWebFilter(TaskWatcher taskWatcher) {
        this.taskWatcher = taskWatcher;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest servletRequest, HttpServletResponse servletResponse, FilterChain filterChain) throws ServletException, IOException {

        if (taskWatcher.isShuttingDown()) {
            servletResponse.sendError(HttpServletResponse.SC_SERVICE_UNAVAILABLE);
        } else {
            try {
                taskWatcher.increaseRequestCount();
                filterChain.doFilter(servletRequest, servletResponse);
            } finally {
                taskWatcher.decreaseRequestCount();
            }
        }
    }
}
