package pa.pavelan.webmetrics.handler;

import javax.servlet.ServletOutputStream;
import javax.servlet.WriteListener;
import java.io.ByteArrayOutputStream;

class MetricsServletOutputStream extends ServletOutputStream {
    final private ByteArrayOutputStream byteStream;

    MetricsServletOutputStream(ByteArrayOutputStream byteStream) {
        this.byteStream = byteStream;
    }

    @Override
    public void write(int byteValue) {
        byteStream.write(byteValue);
    }

    @Override
    public boolean isReady() {
        return true;
    }

    @Override
    public void setWriteListener(WriteListener writeListener) {
    }
}
