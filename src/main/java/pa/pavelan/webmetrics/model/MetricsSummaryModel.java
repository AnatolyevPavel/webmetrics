package pa.pavelan.webmetrics.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
public class MetricsSummaryModel {
    private long minProcessingTime;
    private long averageProcessingTime;
    private long maxProcessingTime;
    private long minResponseSize;
    private long averageResponseSize;
    private long maxResponseSize;
}
