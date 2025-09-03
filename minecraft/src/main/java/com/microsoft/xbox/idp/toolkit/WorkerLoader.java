package com.microsoft.xbox.idp.toolkit;

import android.content.Context;
import android.content.Loader;
import android.os.Handler;

public abstract class WorkerLoader<D> extends Loader<D> {
    private final Handler dispatcher;
    private final Object lock;
    private D result;
    private ResultListener<D> resultListener;
    private final Worker<D> worker;

    public interface ResultListener<D> {
        void onResult(D d);
    }

    public interface Worker<D> {
        void cancel();

        void start(ResultListener<D> resultListener);
    }

    protected abstract boolean isDataReleased(D d);

    protected abstract void releaseData(D d);

    public WorkerLoader(Context context, Worker<D> worker) {
        super(context);
        this.lock = new Object();
        this.dispatcher = new Handler();
        this.worker = worker;
    }

    @Override // android.content.Loader
    protected void onStartLoading() {
        D d = this.result;
        if (d != null) {
            deliverResult(d);
        }
        if (takeContentChanged() || this.result == null) {
            forceLoad();
        }
    }

    @Override // android.content.Loader
    protected void onStopLoading() {
        cancelLoadCompat();
    }

    public void onCanceled(D d) {
        if (d == null || isDataReleased(d)) {
            return;
        }
        releaseData(d);
    }

    @Override // android.content.Loader
    protected void onForceLoad() {
        super.onForceLoad();
        cancelLoadCompat();
        synchronized (this.lock) {
            ResultListenerImpl resultListenerImpl = new ResultListenerImpl();
            this.resultListener = resultListenerImpl;
            this.worker.start(resultListenerImpl);
        }
    }

    @Override // android.content.Loader
    protected boolean onCancelLoad() {
        synchronized (this.lock) {
            if (this.resultListener == null) {
                return false;
            }
            this.worker.cancel();
            this.resultListener = null;
            return true;
        }
    }

    @Override // android.content.Loader
    public void deliverResult(D d) {
        if (isReset()) {
            if (d != null) {
                releaseData(d);
                return;
            }
            return;
        }
        D d2 = this.result;
        this.result = d;
        if (isStarted()) {
            super.deliverResult(d);
        }
        if (d2 == null || d2 == d || isDataReleased(d2)) {
            return;
        }
        releaseData(d2);
    }

    @Override // android.content.Loader
    protected void onReset() {
        cancelLoadCompat();
        D d = this.result;
        if (d != null && !isDataReleased(d)) {
            releaseData(this.result);
        }
        this.result = null;
    }

    private boolean cancelLoadCompat() {
        return cancelLoad();
    }

    private class ResultListenerImpl implements ResultListener<D> {
        private ResultListenerImpl() {
        }

        @Override // com.microsoft.xbox.idp.toolkit.WorkerLoader.ResultListener
        public void onResult(final D d) {
            synchronized (WorkerLoader.this.lock) {
                final boolean z = this != WorkerLoader.this.resultListener;
                WorkerLoader.this.resultListener = null;
                WorkerLoader.this.dispatcher.post(new Runnable() { // from class: com.microsoft.xbox.idp.toolkit.WorkerLoader.ResultListenerImpl.1
                    /* JADX WARN: Multi-variable type inference failed */
                    @Override // java.lang.Runnable
                    public void run() {
                        if (z) {
                            WorkerLoader.this.onCanceled(d);
                        } else {
                            WorkerLoader.this.deliverResult(d);
                        }
                    }
                });
            }
        }
    }
}
