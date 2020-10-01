package pa.pavelan.webmetrics.handler;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;

class MetricHttpServletResponseWrapper extends HttpServletResponseWrapper {
    private final MetricWriterHelper writerHelper;
    private MetricsServletOutputStream outputStream = new MetricsServletOutputStream();

    private enum StreamUsed {
        OUTPUT_STREAM_USED,
        PRINT_WRITER_USED
    }

    private StreamUsed streamUsed;

    MetricHttpServletResponseWrapper(HttpServletResponse response) {
        super(response);
        writerHelper = new MetricWriterHelper();
    }

    @Override
    public PrintWriter getWriter() {
        if (streamUsed == StreamUsed.OUTPUT_STREAM_USED) {
            throw new IllegalStateException("Output stream is already requested");
        }
        streamUsed = StreamUsed.PRINT_WRITER_USED;
        return writerHelper.getPrintWriter();
    }

    @Override
    public ServletOutputStream getOutputStream() throws IOException {
        if (streamUsed == StreamUsed.PRINT_WRITER_USED) {
            throw new IllegalStateException("Print writer is already requested");
        }
        streamUsed = StreamUsed.OUTPUT_STREAM_USED;
        outputStream.setOriginalStream(super.getOutputStream());
        return outputStream;
    }

    long copyBytesToOriginal() throws IOException {
        long responseSize = outputStream.getCounter();
        if (streamUsed == StreamUsed.PRINT_WRITER_USED) {
            byte[] bytes = writerHelper.getByteArray();
            super.getOutputStream().write(bytes);
            responseSize = bytes.length;
        }
        return responseSize;
    }
}
