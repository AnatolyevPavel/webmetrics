package pa.pavelan;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockFilterChain;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import pa.pavelan.webmetrics.handler.MetricsFilter;
import pa.pavelan.webmetrics.handler.MetricsHelper;

import java.io.IOException;

class TestMetricFilter {

    @Test
    void testDoFilter() throws ServletException, IOException {

        final String ID_HEADER_NAME = "PAVELAN-METRICS-ID-TEST";

        MetricsFilter filter = new MetricsFilter(ID_HEADER_NAME);

        HttpServletRequest mockReq = new MockHttpServletRequest("GET", "/");// Mockito.mock(HttpServletRequest.class);
        HttpServletResponse mockResp = new MockHttpServletResponse();
        MockFilterChain filterChain = new MockFilterChain();

        filter.doFilter(mockReq, mockResp, filterChain);

        assertNotNull(ID_HEADER_NAME);

        assertEquals(1, MetricsHelper.getAllMetrics().size());

        filter.destroy();
    }

}
