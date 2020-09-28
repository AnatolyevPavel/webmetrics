package pa.pavelan.webmetrics.handler;

import pa.pavelan.webmetrics.model.MetricsModel;
import pa.pavelan.webmetrics.model.MetricsSummaryModel;

import java.util.*;

class MetricsHandler {
    private static volatile MetricsHandler metricsHandler;
    private final Map<UUID, MetricsModel> metrics;
    private volatile long minProcessingTime;
    private volatile long averageProcessingTime;
    private volatile long maxProcessingTime;
    private volatile long minResponseSize;
    private volatile long averageResponseSize;
    private volatile long maxResponseSize;
    private volatile long numberOfRequests;

    private MetricsHandler() {
        metrics = new LinkedHashMap<>();
        minProcessingTime = Long.MAX_VALUE;
        averageProcessingTime = 0;
        maxProcessingTime = 0;
        minResponseSize = Long.MAX_VALUE;
        averageResponseSize = 0;
        maxResponseSize = 0;
        numberOfRequests = 0;
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
        averageProcessingTime = (averageProcessingTime * (numberOfRequests - 1) + metric.getProcessingTime()) / numberOfRequests;
        averageResponseSize = (averageResponseSize * (numberOfRequests - 1) + metric.getResponseSize()) / numberOfRequests;
        minProcessingTime = metric.getProcessingTime() > minProcessingTime ? minProcessingTime : metric.getProcessingTime();
        maxProcessingTime = metric.getProcessingTime() < maxProcessingTime ? maxProcessingTime : metric.getProcessingTime();
        minResponseSize = metric.getResponseSize() > minResponseSize ? minResponseSize : metric.getResponseSize();
        maxResponseSize = metric.getResponseSize() < maxResponseSize ? maxResponseSize : metric.getResponseSize();
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
