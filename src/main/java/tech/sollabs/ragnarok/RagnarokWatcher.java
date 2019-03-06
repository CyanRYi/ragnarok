package tech.sollabs.ragnarok;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class RagnarokWatcher {

    private final String HTTP_REQUEST_KEY = "httpRequest";
    private Map<String, AtomicInteger> counters;

    public RagnarokWatcher() {

        counters = new ConcurrentHashMap<>();
        counters.put(HTTP_REQUEST_KEY, new AtomicInteger());
    }

    public void increaseRequestCount() {
        increaseCount(HTTP_REQUEST_KEY);
    }

    public void decreaseRequestCount() {
        decreaseCount(HTTP_REQUEST_KEY);
    }

    public void increaseCount(String counterKey) {
        counters.get(counterKey)
                .incrementAndGet();
    }

    public void decreaseCount(String counterKey) {
        counters.get(counterKey)
                .decrementAndGet();
    }

    public boolean needWaiting() {

        return counters.entrySet()
                .parallelStream()
                .map(Map.Entry::getValue)
                .anyMatch(atomicInteger -> atomicInteger.get() > 0);
    }
}