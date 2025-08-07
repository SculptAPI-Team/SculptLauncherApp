package org.fmod;

import android.media.MediaCrypto;
import android.media.MediaDataSource;
import android.media.MediaExtractor;
import android.media.MediaFormat;
import android.util.Log;
import android.view.Surface;
import java.io.IOException;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.nio.ByteBuffer;

/* loaded from: classes2-dex2jar.jar:org/fmod/MediaCodec.class */
public class MediaCodec {
    private long mCodecPtr = 0;
    private long mLength = 0;
    private int mSampleRate = 0;
    private int mChannelCount = 0;
    private boolean mInputFinished = false;
    private boolean mOutputFinished = false;
    private android.media.MediaCodec mDecoder = null;
    private Object mDataSourceProxy = null;
    private MediaExtractor mExtractor = null;
    private ByteBuffer[] mInputBuffers = null;
    private ByteBuffer[] mOutputBuffers = null;
    private int mCurrentOutputBufferIndex = -1;

    /* renamed from: org.fmod.MediaCodec$1DataSource, reason: invalid class name */
    /* loaded from: classes2-dex2jar.jar:org/fmod/MediaCodec$1DataSource.class */
    class C1DataSource implements InvocationHandler {
        final MediaCodec this$0;

        C1DataSource(MediaCodec mediaCodec) {
            this.this$0 = mediaCodec;
        }

        @Override // java.lang.reflect.InvocationHandler
        public Object invoke(Object obj, Method method, Object[] objArr) {
            if (method.getName().equals("readAt")) {
                return Integer.valueOf(MediaCodec.fmodReadAt(this.this$0.mCodecPtr, ((Long) objArr[0]).longValue(), (byte[]) objArr[1], 0, ((Integer) objArr[2]).intValue()));
            }
            if (method.getName().equals("getSize")) {
                return Long.valueOf(MediaCodec.fmodGetSize(this.this$0.mCodecPtr));
            }
            if (method.getName().equals("close")) {
                return null;
            }
            Log.w("fmod", "MediaCodec::DataSource::invoke : Unrecognised method found: " + method.getName());
            return null;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static native long fmodGetSize(long j);

    /* JADX INFO: Access modifiers changed from: private */
    public static native int fmodReadAt(long j, long j2, byte[] bArr, int i, int i2);

    public int getChannelCount() {
        return this.mChannelCount;
    }

    public long getLength() {
        return this.mLength;
    }

    public int getSampleRate() {
        return this.mSampleRate;
    }

    public boolean init(long j) {
        this.mCodecPtr = j;
        try {
            MediaExtractor mediaExtractor = new MediaExtractor();
            this.mExtractor = mediaExtractor;
            mediaExtractor.setDataSource(new MediaDataSource() { // from class: org.fmod.MediaCodec.2DataSource

                @Override // java.io.Closeable, java.lang.AutoCloseable
                public void close() {
                }

                @Override // android.media.MediaDataSource
                public long getSize() {
                    return MediaCodec.fmodGetSize(mCodecPtr);
                }

                @Override // android.media.MediaDataSource
                public int readAt(long j2, byte[] bArr, int i, int i2) {
                    return MediaCodec.fmodReadAt(mCodecPtr, j2, bArr, i, i2);
                }
            });
            int trackCount = this.mExtractor.getTrackCount();
            for (int i = 0; i < trackCount; i++) {
                MediaFormat trackFormat = this.mExtractor.getTrackFormat(i);
                String string = trackFormat.getString("mime");
                Log.d("fmod", "MediaCodec::init : Format " + i + " / " + trackCount + " -- " + trackFormat);
                if (string.equals("audio/mp4a-latm")) {
                    try {
                        this.mDecoder = android.media.MediaCodec.createDecoderByType(string);
                        this.mExtractor.selectTrack(i);
                        this.mDecoder.configure(trackFormat, (Surface) null, (MediaCrypto) null, 0);
                        this.mDecoder.start();
                        this.mInputBuffers = this.mDecoder.getInputBuffers();
                        this.mOutputBuffers = this.mDecoder.getOutputBuffers();
                        int integer = trackFormat.containsKey("encoder-delay") ? trackFormat.getInteger("encoder-delay") : 0;
                        int integer2 = trackFormat.containsKey("encoder-padding") ? trackFormat.getInteger("encoder-padding") : 0;
                        long j2 = trackFormat.getLong("durationUs");
                        this.mChannelCount = trackFormat.getInteger("channel-count");
                        this.mSampleRate = trackFormat.getInteger("sample-rate");
                        this.mLength = (((int) (((j2) + 999999) / 1000000)) - integer) - integer2;
                        return true;
                    } catch (IOException e) {
                        Log.e("fmod", "MediaCodec::init : " + e.toString());
                        return false;
                    }
                }
            }
            return false;
        } catch (IOException e2) {
            Log.w("fmod", "MediaCodec::init : " + e2.toString());
            return false;
        }
    }

    public int read(byte[] bArr, int i) {
        int iDequeueInputBuffer;
        int iMin = (this.mInputFinished && this.mOutputFinished && this.mCurrentOutputBufferIndex == -1) ? -1 : 0;
        while (!this.mInputFinished && (iDequeueInputBuffer = this.mDecoder.dequeueInputBuffer(0L)) >= 0) {
            int sampleData = this.mExtractor.readSampleData(this.mInputBuffers[iDequeueInputBuffer], 0);
            if (sampleData >= 0) {
                this.mDecoder.queueInputBuffer(iDequeueInputBuffer, 0, sampleData, this.mExtractor.getSampleTime(), 0);
                this.mExtractor.advance();
            } else {
                this.mDecoder.queueInputBuffer(iDequeueInputBuffer, 0, 0, 0L, 4);
                this.mInputFinished = true;
            }
        }
//        if (!this.mOutputFinished && this.mCurrentOutputBufferIndex == -1) {
//            MediaCodec.BufferInfo bufferInfo = new MediaCodec.BufferInfo();
//            int iDequeueOutputBuffer = this.mDecoder.dequeueOutputBuffer(bufferInfo, 10000L);
//            if (iDequeueOutputBuffer >= 0) {
//                this.mCurrentOutputBufferIndex = iDequeueOutputBuffer;
//                this.mOutputBuffers[iDequeueOutputBuffer].limit(bufferInfo.size);
//                this.mOutputBuffers[iDequeueOutputBuffer].position(bufferInfo.offset);
//            } else if (iDequeueOutputBuffer == -3) {
//                this.mOutputBuffers = this.mDecoder.getOutputBuffers();
//            } else if (iDequeueOutputBuffer == -2) {
//                Log.d("fmod", "MediaCodec::read : MediaCodec::dequeueOutputBuffer returned MediaCodec.INFO_OUTPUT_FORMAT_CHANGED " + this.mDecoder.getOutputFormat());
//            } else if (iDequeueOutputBuffer == -1) {
//                Log.d("fmod", "MediaCodec::read : MediaCodec::dequeueOutputBuffer returned MediaCodec.INFO_TRY_AGAIN_LATER.");
//            } else {
//                Log.w("fmod", "MediaCodec::read : MediaCodec::dequeueOutputBuffer returned " + iDequeueOutputBuffer);
//            }
//            if ((bufferInfo.flags & 4) != 0) {
//                this.mOutputFinished = true;
//            }
//        }
        int i2 = this.mCurrentOutputBufferIndex;
        if (i2 != -1) {
            ByteBuffer byteBuffer = this.mOutputBuffers[i2];
            iMin = Math.min(byteBuffer.remaining(), i);
            byteBuffer.get(bArr, 0, iMin);
            if (!byteBuffer.hasRemaining()) {
                byteBuffer.clear();
                this.mDecoder.releaseOutputBuffer(this.mCurrentOutputBufferIndex, false);
                this.mCurrentOutputBufferIndex = -1;
            }
        }
        return iMin;
    }

    public void release() {
        android.media.MediaCodec mediaCodec = this.mDecoder;
        if (mediaCodec != null) {
            mediaCodec.stop();
            this.mDecoder.release();
            this.mDecoder = null;
        }
        MediaExtractor mediaExtractor = this.mExtractor;
        if (mediaExtractor != null) {
            mediaExtractor.release();
            this.mExtractor = null;
        }
    }

    public void seek(int i) {
        int i2 = this.mCurrentOutputBufferIndex;
        if (i2 != -1) {
            this.mOutputBuffers[i2].clear();
            this.mCurrentOutputBufferIndex = -1;
        }
        this.mInputFinished = false;
        this.mOutputFinished = false;
        this.mDecoder.flush();
        long j = i;
        this.mExtractor.seekTo((j * 1000000) / this.mSampleRate, MediaExtractor.SEEK_TO_PREVIOUS_SYNC);
        long sampleTime = ((this.mExtractor.getSampleTime() * this.mSampleRate) + 999999) / 1000000;
        int i3 = (int) ((j - sampleTime) * this.mChannelCount * 2);
        if (i3 < 0) {
            Log.w("fmod", "MediaCodec::seek : Seek to " + i + " resulted in position " + sampleTime);
            return;
        }
        byte[] bArr = new byte[1024];
        int i4 = i3;
        while (true) {
            int i5 = i4;
            if (i5 <= 0) {
                return;
            } else {
                i4 = i5 - read(bArr, Math.min(1024, i5));
            }
        }
    }
}
