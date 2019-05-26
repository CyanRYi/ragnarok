package tech.sollabs.ragnarok;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.lang.NonNull;

import javax.annotation.PreDestroy;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Manage task count and shutting down status.
 * When before Destroy this object, waiting process till all of registered tasks end
 *
 * @author Cyan Raphael Yi
 * @since 0.1.0
 */
public class TaskWatcher {

    private Log log = LogFactory.getLog(getClass());

    private final String HTTP_REQUEST_KEY = "httpRequest";

    private AtomicBoolean shuttingDown = new AtomicBoolean(false);
    private Map<String, AtomicInteger> counters = new ConcurrentHashMap<>();

    public TaskWatcher() {
        counters.put(HTTP_REQUEST_KEY, new AtomicInteger());
    }

    private void setShuttingDown() {
        if (shuttingDown.get()) {
            shuttingDown.set(true);
            log.warn("Application will be shutting down soon");
        } else {
            log.error("Shut down Process already processing...");
        }
    }

    public boolean isShuttingDown() {
        return shuttingDown.get();
    }

    public void increaseRequestCount() {
        increaseCount(HTTP_REQUEST_KEY);
    }

    public void decreaseRequestCount() {
        decreaseCount(HTTP_REQUEST_KEY);
    }

    public void increaseCount(@NonNull String counterKey) {
        counters.get(counterKey)
                .incrementAndGet();
    }

    public void decreaseCount(@NonNull String counterKey) {
        counters.get(counterKey)
                .decrementAndGet();
    }

    public boolean needWaiting() {
        return counters.entrySet()
                .parallelStream()
                .map(Map.Entry::getValue)
                .anyMatch(atomicInteger -> atomicInteger.get() > 0);
    }

    @NonNull
    public Map<String, AtomicInteger> getCounters() {
        return counters;
    }

    @PreDestroy
    public void shutdown() {

        setShuttingDown();

        do {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                log.error("Thread interrupted while processing shutdown");
                e.printStackTrace();
            }
        } while (needWaiting());

        log.info("Good Bye");
    }
}