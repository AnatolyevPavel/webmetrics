package pa.pavelan.webmetrics.handler;

import javax.servlet.ServletOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintWriter;

class MetricWriterHelper {
    final private ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
    final private PrintWriter metricsPrintWriter = new PrintWriter(byteStream);
    final private ServletOutputStream servletOutputStream = new MetricsServletOutputStream(byteStream);

    public PrintWriter getPrintWriter() {
        return metricsPrintWriter;
    }

    public ServletOutputStream getOutputStream() {
        return servletOutputStream;
    }

    public byte[] getByteArray() {
        return byteStream.toByteArray();
    }
}
