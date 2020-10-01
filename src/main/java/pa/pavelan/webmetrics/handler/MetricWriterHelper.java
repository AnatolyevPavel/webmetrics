package pa.pavelan.webmetrics.handler;

import javax.servlet.ServletOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintWriter;

class MetricWriterHelper {
    final private ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
    final private PrintWriter metricsPrintWriter = new PrintWriter(byteStream);

    PrintWriter getPrintWriter() {
        return metricsPrintWriter;
    }

    byte[] getByteArray() {
        return byteStream.toByteArray();
    }
}
