package pa.pavelan.webmetrics.handler;

import lombok.extern.log4j.Log4j;
import pa.pavelan.webmetrics.model.MetricsModel;

import java.io.IOException;
import java.util.Date;
import java.util.UUID;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Log4j
public class MetricsFilter implements Filter {
    private static final String DEFAULT_ID_HEADER_NAME = "PAVELAN-METRICS-ID";
    private final String headerName;

    public MetricsFilter(String headerName) {
        super();
        this.headerName = headerName;
    }

    public MetricsFilter() {
        super();
        this.headerName = DEFAULT_ID_HEADER_NAME;
    }

    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse,
                         FilterChain filterChain) throws IOException, ServletException {

        final HttpServletRequest httpRequest = (HttpServletRequest) servletRequest;
        final HttpServletResponse response = (HttpServletResponse) servletResponse;

        UUID id = UUID.randomUUID();
        response.addHeader(headerName, id.toString());

        MetricHttpServletResponseWrapper wrappedResponse = new MetricHttpServletResponseWrapper(response);

        long startTime = System.currentTimeMillis();

        filterChain.doFilter(httpRequest, wrappedResponse);

        long requestProcessingTime = System.currentTimeMillis() - startTime;

        long responseSize = wrappedResponse.copyBytesToOriginal();

        log.debug(String.format("%s %dms %db", id.toString(), requestProcessingTime, responseSize));

        MetricsModel metric = MetricsModel.builder()
                .id(id)
                .processingTime(requestProcessingTime)
                .responseSize(responseSize)
                .date(new Date())
                .build();

        MetricsHandler.getInstance().addMetric(metric);
    }

    public void destroy() {
    }

}