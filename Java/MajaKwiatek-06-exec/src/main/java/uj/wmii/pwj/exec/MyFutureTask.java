package uj.wmii.pwj.exec;

import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;

public class MyFutureTask<V> extends FutureTask<V> {
    private volatile boolean inProgress = false;

    public MyFutureTask(Callable<V> callable) {
        super(callable);
    }

    public MyFutureTask(Runnable runnable, V result) {
        super(runnable, result);
    }

    boolean isInProgress() {
        return this.inProgress;
    }

    void setInProgress() {
        inProgress = true;
    }
}
