package pa.pavelan.webmetrics.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.UUID;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class MetricsModel {
    private UUID id;
    private Date date;
    private long processingTime;
    private long responseSize;
}
