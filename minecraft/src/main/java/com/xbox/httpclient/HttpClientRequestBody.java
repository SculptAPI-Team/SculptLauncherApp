package com.xbox.httpclient;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.io.IOException;
import java.io.InputStream;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okio.BufferedSink;
import okio.Okio;

public class HttpClientRequestBody extends RequestBody {
    private final long callHandle;
    private final long contentLength;
    private final MediaType contentType;

    public HttpClientRequestBody(long j, String str, long j2) {
        this.callHandle = j;
        this.contentType = str != null ? MediaType.parse(str) : null;
        this.contentLength = j2;
    }

    @Nullable
    @Override
    public MediaType contentType() {
        return this.contentType;
    }

    @Override
    public long contentLength() {
        return this.contentLength;
    }

    @Override
    public void writeTo(@NonNull BufferedSink bufferedSink) throws IOException {
        bufferedSink.writeAll(Okio.source(new NativeInputStream(this.callHandle)));
    }

    private final class NativeInputStream extends InputStream {
        private final long callHandle;
        private long offset = 0;

        private native int nativeRead(long j, long j2, byte[] bArr, long j3, long j4);

        public NativeInputStream(long j) {
            this.callHandle = j;
        }

        @Override // java.io.InputStream
        public int read() {
            byte[] bArr = new byte[1];
            read(bArr);
            return bArr[0];
        }

        @Override // java.io.InputStream
        public int read(byte[] bArr) {
            return read(bArr, 0, bArr.length);
        }

        @Override // java.io.InputStream
        public int read(byte[] bArr, int i, int i2) {
            bArr.getClass();
            if (i < 0 || i2 < 0 || i + i2 > bArr.length) {
                throw new IndexOutOfBoundsException();
            }
            if (i2 == 0) {
                return 0;
            }
            int iNativeRead = nativeRead(this.callHandle, this.offset, bArr, i, i2);
            if (iNativeRead == -1) {
                return -1;
            }
            this.offset += iNativeRead;
            return iNativeRead;
        }

        @Override // java.io.InputStream
        public long skip(long j) {
            this.offset += j;
            return j;
        }
    }
}
