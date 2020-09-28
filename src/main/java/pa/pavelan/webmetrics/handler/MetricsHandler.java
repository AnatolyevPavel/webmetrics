package pa.pavelan.webmetrics.handler;

import pa.pavelan.webmetrics.model.MetricsModel;
import pa.pavelan.webmetrics.model.MetricsSummaryModel;

import java.util.*;

class MetricsHandler {
    private static volatile MetricsHandler metricsHandler;
    private final Map<UUID, MetricsModel> metrics;
    private volatile long minProcessingTime;
    private volatile double averageProcessingTime;
    private volatile long maxProcessingTime;
    private volatile long minResponseSize;
    private volatile double averageResponseSize;
    private volatile long maxResponseSize;
    private volatile long numberOfRequests;
    private volatile long totalProcessingTime;
    private volatile long totalResponseSize;

    private MetricsHandler() {
        metrics = new LinkedHashMap<>();
        minProcessingTime = Long.MAX_VALUE;
        averageProcessingTime = 0;
        maxProcessingTime = 0;
        minResponseSize = Long.MAX_VALUE;
        averageResponseSize = 0;
        maxResponseSize = 0;
        numberOfRequests = 0;
        totalProcessingTime = 0;
        totalResponseSize = 0;
    }

    synchronized void addMetric(MetricsModel metric) {
        if (metric == null || metric.getId() == null) {
            return;
        }
        metrics.put(metric.getId(), metric);
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
    }

    MetricsModel getMetricById(UUID id) {
        return metrics.get(id);
    }

    Collection<MetricsModel> getAllMetrics() {
        return metrics.values();
    }

    MetricsSummaryModel getMetricSummary() {
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
