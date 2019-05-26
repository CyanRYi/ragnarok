package tech.sollabs.ragnarok.actuator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.health.ApplicationHealthIndicator;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.boot.actuate.health.Status;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.stereotype.Component;
import tech.sollabs.ragnarok.RagnarokWatcher;

/**
 * Show status about tasks managed by RagnarokWatcher
 * IF RagnarokWatcher bean is not initialized, Health Status is <code>UNKNOWN</code>,
 * IF Application is now shutting down, Health Status is <code>OUT_OF_SERVICE</code>,
 * and all other case, Health Status is <code>UP</code>
 *
 * @author Cyan Raphael Yi
 * @since 0.2.0
 * @see RagnarokWatcher
 */
@ConditionalOnClass(ApplicationHealthIndicator.class)
@Component
public class RagnarokHealthIndicator implements HealthIndicator {

    private RagnarokWatcher watcher;

    @Override
    public Health health() {

        if (watcher == null) {
            return Health.unknown().build();
        }

        Status ragnarokStatus = watcher.isShuttingDown() ? Status.OUT_OF_SERVICE : Status.UP;
        return new Health.Builder(ragnarokStatus, watcher.getCounters()).build();
    }

    @Autowired
    public void setWatcher(RagnarokWatcher watcher) {
        this.watcher = watcher;
    }
}
