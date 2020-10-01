package pa.pavelan.webmetrics.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.Date;
import java.util.UUID;

@Getter
@Builder
@AllArgsConstructor
final public class MetricsModel {
    private UUID id;
    private Date date;
    private long processingTime;
    private long responseSize;
    private long insertTimeMillis;

    public MetricsModel(MetricsModel model, long insertTimeMillis) {
        this.id = model.id;
        this.date = model.date;
        this.processingTime = model.processingTime;
        this.responseSize = model.responseSize;
        this.insertTimeMillis = insertTimeMillis;
    }
}
