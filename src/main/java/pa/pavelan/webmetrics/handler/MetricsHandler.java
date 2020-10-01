package pa.pavelan.webmetrics.handler;

import lombok.extern.log4j.Log4j;
import pa.pavelan.webmetrics.model.MetricsModel;
import pa.pavelan.webmetrics.model.MetricsSummaryModel;

import java.util.*;

@Log4j
final class MetricsHandler {
    private final static long DEFAULT_RETENTION_TIME = 60; // in seconds

    private static volatile MetricsHandler metricsHandler;
    private final Map<UUID, MetricsModel> metrics = new LinkedHashMap<>();
    private volatile long minProcessingTime = Long.MAX_VALUE;
    private volatile double averageProcessingTime = 0;
    private volatile long maxProcessingTime = 0;
    private volatile long minResponseSize = Long.MAX_VALUE;
    private volatile double averageResponseSize = 0;
    private volatile long maxResponseSize = 0;
    private volatile long numberOfRequests = 0;
    private volatile long totalProcessingTime = 0;
    private volatile long totalResponseSize = 0;
    final private long retentionTime;
    private volatile long lastMapClearTime;

    private MetricsHandler() {
        String retentionTimeString = new Properties().getProperty("metric.retention.time");
        long retentionTimeProperty = 0L;
        try {
            retentionTimeProperty = Long.parseLong(retentionTimeString);
        } catch (NumberFormatException ex) {
            log.debug(ex.getMessage());
        }
        retentionTime = (retentionTimeProperty == 0 ? DEFAULT_RETENTION_TIME : retentionTimeProperty) * 1000;
        lastMapClearTime = System.currentTimeMillis();
    }

    synchronized void addMetric(MetricsModel metric) {
        if (metric == null || metric.getId() == null) {
            return;
        }

        MetricsModel metricWithTime = new MetricsModel(metric, System.currentTimeMillis());
        metrics.put(metricWithTime.getId(), metricWithTime);

        if (metric.getResponseSize() == 0) {
            return;
        }

        numberOfRequests++;
        totalProcessingTime += metric.getProcessingTime();
        averageProcessingTime = (double)totalProcessingTime / numberOfRequests;
        totalResponseSize += metric.getResponseSize();
        averageResponseSize = (double)totalResponseSize / numberOfRequests;
        minProcessingTime = Math.min(metric.getProcessingTime(),minProcessingTime);
        maxProcessingTime = Math.max(metric.getProcessingTime(), maxProcessingTime);
        minResponseSize = Math.min(metric.getResponseSize(), minResponseSize);
        maxResponseSize = Math.max(metric.getResponseSize(), maxResponseSize);

        final long currentTimeMillis = System.currentTimeMillis();
        if (currentTimeMillis > lastMapClearTime + retentionTime) {
            metrics.values().removeIf(value -> (value.getInsertTimeMillis() + retentionTime) < currentTimeMillis );
            lastMapClearTime = currentTimeMillis;
        }
    }

    synchronized MetricsModel getMetricById(UUID id) {
        return metrics.get(id);
    }

    synchronized Collection<MetricsModel> getAllMetrics() {
        return new ArrayList<>(metrics.values());
    }

    synchronized MetricsSummaryModel getMetricSummary() {
        return MetricsSummaryModel.builder()
                .averageProcessingTime(averageProcessingTime)
                .averageResponseSize(averageResponseSize)
                .maxProcessingTime(maxProcessingTime)
                .maxResponseSize(maxResponseSize)
                .minProcessingTime(numberOfRequests == 0 ? 0 : minProcessingTime)
                .minResponseSize(numberOfRequests == 0 ? 0 : minResponseSize)
                .build();
    }

    static MetricsHandler getInstance() {
        if (metricsHandler == null) {
            synchronized (MetricsHandler.class) {
                if (metricsHandler == null) {
                    metricsHandler = new MetricsHandler();
                }
            }
        }
        return metricsHandler;
    }
}
