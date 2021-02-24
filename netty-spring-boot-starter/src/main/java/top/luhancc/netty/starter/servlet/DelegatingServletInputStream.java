package top.luhancc.netty.starter.servlet;

import org.springframework.util.Assert;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * @author luHan
 * @create 2021/2/24 10:53
 * @since 1.0.0
 */
public class DelegatingServletInputStream extends ServletInputStream {

    private final InputStream sourceStream;

    private boolean finished = false;


    /**
     * Create a DelegatingServletInputStream for the given source stream.
     *
     * @param sourceStream the source stream (never {@code null})
     */
    public DelegatingServletInputStream(InputStream sourceStream) {
        Assert.notNull(sourceStream, "Source InputStream must not be null");
        this.sourceStream = sourceStream;
    }

    /**
     * Return the underlying source stream (never {@code null}).
     */
    public final InputStream getSourceStream() {
        return this.sourceStream;
    }


    @Override
    public int read() throws IOException {
        int data = this.sourceStream.read();
        if (data == -1) {
            this.finished = true;
        }
        return data;
    }

    @Override
    public int available() throws IOException {
        return this.sourceStream.available();
    }

    @Override
    public void close() throws IOException {
        super.close();
        this.sourceStream.close();
    }

    @Override
    public boolean isFinished() {
        return this.finished;
    }

    @Override
    public boolean isReady() {
        return true;
    }

    @Override
    public void setReadListener(ReadListener readListener) {
        throw new UnsupportedOperationException();
    }

}
