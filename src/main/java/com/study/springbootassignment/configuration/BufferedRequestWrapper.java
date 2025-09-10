package com.study.springbootassignment.configuration;

import jakarta.servlet.ReadListener;
import jakarta.servlet.ServletInputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;

import java.io.*;

public class BufferedRequestWrapper extends HttpServletRequestWrapper {

    private final byte[] buffer;

    public BufferedRequestWrapper(HttpServletRequest request) throws IOException {
        super(request);
        InputStream is = request.getInputStream();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] tmp = new byte[1024];
        int bytesRead;
        while ((bytesRead = is.read(tmp)) != -1) {
            baos.write(tmp, 0, bytesRead);
        }
        buffer = baos.toByteArray();
    }

    @Override
    public ServletInputStream getInputStream() {
        ByteArrayInputStream bais = new ByteArrayInputStream(buffer);
        return new ServletInputStream() {
            @Override public boolean isFinished() { return bais.available() == 0; }
            @Override public boolean isReady() { return true; }
            @Override public void setReadListener(ReadListener listener) {}
            @Override public int read() { return bais.read(); }
        };
    }

    @Override
    public BufferedReader getReader() {
        return new BufferedReader(new InputStreamReader(getInputStream()));
    }

    public String getRequestBody(String encoding) throws UnsupportedEncodingException {
        return new String(buffer, encoding);
    }
}
