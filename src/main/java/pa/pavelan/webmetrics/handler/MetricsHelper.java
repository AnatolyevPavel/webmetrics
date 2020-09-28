package pa.pavelan.webmetrics.handler;

import pa.pavelan.webmetrics.model.MetricsModel;
import pa.pavelan.webmetrics.model.MetricsSummaryModel;

import java.util.Collection;
import java.util.UUID;

public class MetricsHelper {
    public static MetricsModel getMetricById(UUID id) {
        return MetricsHandler.getInstance().getMetricById(id);
    }

    public static Collection<MetricsModel> getAllMetrics() {
        return MetricsHandler.getInstance().getAllMetrics();
    }

    public static MetricsSummaryModel getMetricSummary() {
        return MetricsHandler.getInstance().getMetricSummary();
    }
}
