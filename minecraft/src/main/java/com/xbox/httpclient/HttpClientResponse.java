package com.xbox.httpclient;

import java.io.IOException;
import java.io.OutputStream;

import okhttp3.Response;
import okio.Okio;

public class HttpClientResponse {
    private final long callHandle;
    private final Response response;

    private final class NativeOutputStream extends OutputStream {
        private final long callHandle;

        private native void nativeWrite(long j, byte[] bArr, int i, int i2) throws IOException;

        public NativeOutputStream(long j) {
            this.callHandle = j;
        }

        @Override // java.io.OutputStream
        public void write(byte[] bArr) throws IOException {
            write(bArr, 0, bArr.length);
        }

        @Override // java.io.OutputStream
        public void write(byte[] bArr, int i, int i2) throws IOException {
            bArr.getClass();
            if (i < 0 || i2 < 0 || i + i2 > bArr.length) {
                throw new IndexOutOfBoundsException();
            }
            nativeWrite(this.callHandle, bArr, i, i2);
        }

        @Override // java.io.OutputStream
        public void write(int i) throws IOException {
            write(new byte[]{(byte) i});
        }
    }

    public HttpClientResponse(long j, Response response) {
        this.callHandle = j;
        this.response = response;
    }

    public int getNumHeaders() {
        return this.response.headers().size();
    }

    public String getHeaderNameAtIndex(int i) {
        if (i < 0 || i >= this.response.headers().size()) {
            return null;
        }
        return this.response.headers().name(i);
    }

    public String getHeaderValueAtIndex(int i) {
        if (i < 0 || i >= this.response.headers().size()) {
            return null;
        }
        return this.response.headers().value(i);
    }

    public void getResponseBodyBytes() {
        try {
            this.response.body().source().readAll(Okio.sink(new NativeOutputStream(this.callHandle)));
        } catch (IOException unused) {
        } catch (Throwable th) {
            this.response.close();
            throw th;
        }
        this.response.close();
    }

    public int getResponseCode() {
        return this.response.code();
    }
}
