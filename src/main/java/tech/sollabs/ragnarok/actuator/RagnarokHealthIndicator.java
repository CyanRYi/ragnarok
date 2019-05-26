package tech.sollabs.ragnarok.actuator;

import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.boot.actuate.health.Status;
import tech.sollabs.ragnarok.TaskWatcher;

/**
 * Show status about tasks managed by TaskWatcher
 * IF TaskWatcher bean is not initialized, Health Status is <code>UNKNOWN</code>,
 * IF Application is now shutting down, Health Status is <code>OUT_OF_SERVICE</code>,
 * and all other case, Health Status is <code>UP</code>
 *
 * @author Cyan Raphael Yi
 * @since 0.2.0
 * @see TaskWatcher
 */
public class RagnarokHealthIndicator implements HealthIndicator {

    private TaskWatcher watcher;

    public RagnarokHealthIndicator(TaskWatcher watcher) {
        this.watcher = watcher;
    }

    @Override
    public Health health() {

        if (watcher == null) {
            return Health.unknown().build();
        }

        Status ragnarokStatus = watcher.isShuttingDown() ? Status.OUT_OF_SERVICE : Status.UP;
        return new Health.Builder(ragnarokStatus, watcher.getCounters()).build();
    }
}
