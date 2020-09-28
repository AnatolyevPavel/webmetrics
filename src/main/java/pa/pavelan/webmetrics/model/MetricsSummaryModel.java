package pa.pavelan.webmetrics.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
public class MetricsSummaryModel {
    private long minProcessingTime;
    private double averageProcessingTime;
    private long maxProcessingTime;
    private long minResponseSize;
    private double averageResponseSize;
    private long maxResponseSize;
}
