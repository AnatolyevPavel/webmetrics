package pa.pavelan.webmetrics.handler;

import lombok.Getter;
import lombok.Setter;

import javax.servlet.ServletOutputStream;
import javax.servlet.WriteListener;
import java.io.IOException;

@Setter
@Getter
class MetricsServletOutputStream extends ServletOutputStream {
    private ServletOutputStream originalStream;
    private long counter = 0;

    @Override
    public void write(int byteValue) throws IOException {
        counter++;
        if (originalStream != null) {
            originalStream.write(byteValue);
        }
    }

    @Override
    public boolean isReady() {
        if (originalStream != null) {
            return originalStream.isReady();
        }
        return false;
    }

    @Override
    public void setWriteListener(WriteListener writeListener) {
        if (originalStream != null) {
            originalStream.setWriteListener(writeListener);
        }
    }
}
