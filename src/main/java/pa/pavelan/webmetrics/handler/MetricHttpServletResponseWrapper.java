package pa.pavelan.webmetrics.handler;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import java.io.PrintWriter;

class MetricHttpServletResponseWrapper extends HttpServletResponseWrapper {
    private final MetricWriterHelper writerHelper;

    MetricHttpServletResponseWrapper(HttpServletResponse response) {
        super(response);
        writerHelper = new MetricWriterHelper();
    }

    @Override
    public PrintWriter getWriter() {
        return writerHelper.getPrintWriter();
    }

    @Override
    public ServletOutputStream getOutputStream() {
        return writerHelper.getOutputStream();
    }

    byte[] getByteArray() {
        return writerHelper.getByteArray();
    }
}
