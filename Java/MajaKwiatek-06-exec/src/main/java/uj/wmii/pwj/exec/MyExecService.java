package uj.wmii.pwj.exec;

import java.util.*;
import java.util.concurrent.*;

public class MyExecService implements ExecutorService {
    class WorkerThread extends Thread {
        @Override
        public void run() {
            while (!isTerminated()) {
                try {
                    synchronized (this){
                        if (isTerminated()) {
                            break;
                        }
                        MyFutureTask<?> futureTask = new MyFutureTask<>(tasksQue.take(), null);
                        futureTask.setInProgress();
                        pendingTasksQue.add(futureTask);
                        futureTask.run();
                        if (futureTask.isDone() && !pendingTasksQue.isEmpty()) {
                                pendingTasksQue.remove(futureTask);
                        }
                    }
                } catch (InterruptedException e) {
                    System.out.println("WorkerThread.run() - Thread interrupted: " + this.getName());
                    Thread.currentThread().interrupt();
                    return;
                }
            }
        }
    }

    private int poolSize;
    private List<WorkerThread> workers;
    private volatile BlockingQueue<MyFutureTask<?>> tasksQue;
    private volatile Queue<MyFutureTask<?>> pendingTasksQue;
    private volatile boolean isShutDown;

    synchronized Queue<MyFutureTask<?>>  getPendingTasksQue() {
        return this.pendingTasksQue;
    }

    MyExecService(int poolSize) {
        this.poolSize = poolSize;
        isShutDown = false;
        tasksQue = new LinkedBlockingQueue<>();
        pendingTasksQue = new LinkedList<>();
        workers = new ArrayList<>(poolSize);
        for (int i = 0; i < poolSize; i++) {
            WorkerThread worker = new WorkerThread();
            workers.add(worker);
            worker.start();
        }
    }

    MyExecService() {
        poolSize = 4;
        isShutDown = false;
        tasksQue = new LinkedBlockingQueue<>();
        workers = new ArrayList<>(poolSize);
        pendingTasksQue = new LinkedList<>();
        for (int i = 0; i < poolSize; i++) {
            WorkerThread worker = new WorkerThread();
            workers.add(worker);
            worker.start();
        }
    }

    static MyExecService newInstance() {
        return new MyExecService();
    }

    @Override
    public synchronized void shutdown() {
        if (isShutDown) throw new RejectedExecutionException();
        isShutDown = true;
    }

    @Override
    public synchronized List<Runnable> shutdownNow() {
        if (isShutDown) throw new RejectedExecutionException();
        isShutDown = true;
        List<Runnable> stillPendingTasks = new LinkedList<>();
        while (!pendingTasksQue.isEmpty()) {
            MyFutureTask<?> futureTask = pendingTasksQue.poll();
            if (!futureTask.isDone() && futureTask.isInProgress()) {
                stillPendingTasks.add(futureTask);
            }
        }
        for (WorkerThread worker : workers) {
            worker.interrupt();
        }
        return stillPendingTasks;
    }

    @Override
    public synchronized boolean isShutdown() {
        return isShutDown;
    }

    @Override
    public synchronized boolean isTerminated() {
        return isShutDown && pendingTasksQue.isEmpty() && tasksQue.isEmpty();
    }

    @Override
    public synchronized boolean awaitTermination(long timeout, TimeUnit unit) {
        if(!isShutDown) return false;
        long startTime = System.currentTimeMillis();
        while (!isTerminated()) {
            if(System.currentTimeMillis() - startTime > unit.toMillis(timeout)) return false;
        }
        return isTerminated();
    }

    @Override
    public <T> Future<T> submit(Callable<T> task) {
        if (isShutDown) throw new RejectedExecutionException();
        MyFutureTask<T> future = new MyFutureTask<>(task);
        try {
            tasksQue.put(future);
        } catch (InterruptedException e) {
            System.out.println("Error: submit(Callable<T> task) interrupted task");
        }
        return future;
    }

    @Override
    public <T> Future<T> submit(Runnable task, T result) {
        if (isShutDown) throw new RejectedExecutionException();
        MyFutureTask<T> future = new MyFutureTask<>(task, result);
        try {
            tasksQue.put(future);
        } catch (InterruptedException e) {
            System.out.println("Error: submit(Runnable task, T result) interrupted task");
        }
        return future;
    }

    @Override
    public Future<?> submit(Runnable task) {
        if (isShutDown) throw new RejectedExecutionException();
        MyFutureTask<?> future = new MyFutureTask<>(task, null);
        try {
            tasksQue.put(future);
        } catch (InterruptedException e) {
            System.out.println("Error: submit(Runnable task, T result) interrupted task");
        }
        return future;
    }

    @Override
    public <T> List<Future<T>> invokeAll(Collection<? extends Callable<T>> tasks) throws InterruptedException {
        if (isShutDown) throw new RejectedExecutionException();
        List<Future<T>> futures = new ArrayList<>();
        for (Callable<T> task : tasks) {
            MyFutureTask<T> future = new MyFutureTask<>(task);
            futures.add(future);
            try {
                tasksQue.put(future);
            } catch (InterruptedException e) {
                throw new InterruptedException("invokeAll interrupted");
            }
        }
        for (Future<T> task : futures) {
            try {
                task.get();
            } catch (ExecutionException e) {
                throw new InterruptedException();
            }
        }
        return futures;
    }

    @Override
    public <T> List<Future<T>> invokeAll(Collection<? extends Callable<T>> tasks, long timeout, TimeUnit unit) throws InterruptedException {
        if (isShutDown) throw new RejectedExecutionException();
        List<Future<T>> futures = new ArrayList<>();
        for (Callable<T> task : tasks) {
            MyFutureTask<T> future = new MyFutureTask<>(task);
            futures.add(future);
            try {
                tasksQue.put(future);
            } catch (InterruptedException e) {
                throw new InterruptedException("invokeAll interrupted");
            }
        }
        for (Future<T> task : futures) {
            try {
                task.get(timeout, unit);
            } catch (TimeoutException | ExecutionException e) {
                throw new InterruptedException();
            }
        }
        return futures;
    }

    @Override
    public <T> T invokeAny(Collection<? extends Callable<T>> tasks) throws InterruptedException, ExecutionException {
        if (isShutDown) throw new RejectedExecutionException();
        List<MyFutureTask<T>> futures = new ArrayList<>();
        for (Callable<T> task : tasks) {
            MyFutureTask<T> future = new MyFutureTask<>(task);
            futures.add(future);
            try {
                tasksQue.put(future);
            } catch (InterruptedException e) {
                throw new InterruptedException("invokeAll interrupted");
            }
        }
        while (true) {
            for (MyFutureTask<T> task : futures) {
                if (task.isDone()) return task.get();
            }
        }
    }

    @Override
    public <T> T invokeAny(Collection<? extends Callable<T>> tasks, long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
        if (isShutDown) throw new RejectedExecutionException();
        List<MyFutureTask<T>> futures = new ArrayList<>();
        long startTime = System.currentTimeMillis();
        for (Callable<T> task : tasks) {
            MyFutureTask<T> future = new MyFutureTask<>(task);
            futures.add(future);
            try {
                tasksQue.put(future);
            } catch (InterruptedException e) {
                throw new InterruptedException("invokeAll interrupted");
            }
        }
        while (System.currentTimeMillis() - startTime < unit.toMillis(timeout)) {
            for (MyFutureTask<T> task : futures) {
                if (System.currentTimeMillis() - startTime > unit.toMillis(timeout)) return null;
                if (task.isDone()) return task.get();
            }
        }
        return null;
    }

    @Override
    public void execute(Runnable command) {
        try {
            tasksQue.put(new MyFutureTask<>(command, null));
        } catch (InterruptedException e) {
            throw new RuntimeException("Error: execute");
        }
    }
}
