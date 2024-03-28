package uj.wmii.pwj.exec;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.*;

import static org.junit.jupiter.api.Assertions.*;


public class ExecServiceTest {
    List<Long> numbers;
    long computeSum;

    ExecServiceTest() {
        numbers = new ArrayList<>();
        long el = 1;
        for (int i = 0; i < 4; ++i) {
            el *= 100;
            for (int j = 0; j < 1000; ++j)
                numbers.add(el);
        }
        computeSum = compute(0, 1000) + compute(1001, 2000) + compute(2001, 3000) + compute(3001, 4000);
    }

    @Test
    public void testInvokeAny() throws ExecutionException, InterruptedException {
        MyExecService executorService = MyExecService.newInstance();
        Collection<Callable<Long>> tasks = new ArrayList<>();
        tasks.add(doSleepAndShowTime(10000));
        tasks.add(doSleepAndShowTime(10000));
        tasks.add(() -> compute(0, 1000));
        assertEquals(executorService.invokeAny(tasks), 100 * 1000);
    }

    @Test
    void testInvokeAnyTimeoutSuccess() throws ExecutionException, InterruptedException, TimeoutException {
        MyExecService executorService = MyExecService.newInstance();
        Collection<Callable<Long>> tasks = new ArrayList<>();
        tasks.add(doSleepAndShowTime(10000));
        tasks.add(doSleepAndShowTime(10000));
        tasks.add(() -> compute(0, 1000));
        assertEquals(executorService.invokeAny(tasks, 1, TimeUnit.SECONDS), 100 * 1000);
    }

    @Test
    void testInvokeAnyTimeoutFail() throws ExecutionException, InterruptedException, TimeoutException {
        MyExecService executorService = MyExecService.newInstance();
        Collection<Callable<Long>> tasks = new ArrayList<>();
        tasks.add(doSleepAndShowTime(10000));
        tasks.add(doSleepAndShowTime(10000));
        assertNull(executorService.invokeAny(tasks, 1, TimeUnit.SECONDS));
    }

    @Test
    public void testAwaitTerminationTrue() {
        MyExecService executorService = MyExecService.newInstance();

        executorService.submit(doSleepAndShowTime(2000));
        executorService.submit(doSleepAndShowTime(2000));
        executorService.submit(doSleepAndShowTime(2000));
        executorService.shutdown();
        doSleep(500);

        boolean terminated = executorService.awaitTermination(5, TimeUnit.SECONDS);
        assertTrue(terminated);
    }

    @Test
    public void testAwaitTerminationFalse() {
        MyExecService executorService = MyExecService.newInstance();

        executorService.submit(doSleepAndShowTime(1000));
        doSleep(500);
        executorService.shutdown();
        boolean terminated = executorService.awaitTermination(1, TimeUnit.MILLISECONDS);
        assertFalse(terminated);
    }

    @Test
    void observeParallel() throws ExecutionException, InterruptedException {
        MyExecService executor = MyExecService.newInstance();

        Callable<Long> task = doSleepAndShowTime(1000);
        Future<Long> future_1 = executor.submit(task);
        Future<Long> future_2 = executor.submit(task);

        assertEquals(future_1.get(), future_2.get());
    }

    @Test
    void observeNotParallel() throws ExecutionException, InterruptedException {
        MyExecService executor = new MyExecService(1);

        Callable<Long> task = doSleepAndShowTime(2000);
        Future<Long> future_1 = executor.submit(task);
        Future<Long> future_2 = executor.submit(task);
        Future<Long> future_3 = executor.submit(task);
        Future<Long> future_4 = executor.submit(task);

        assertNotEquals(future_1.get(), future_2.get());
        assertNotEquals(future_2.get(), future_3.get());
        assertNotEquals(future_3.get(), future_4.get());
    }

    @Test
    void submitCallable() {
        MyExecService executor = MyExecService.newInstance();
        Callable<Long> task = () -> compute(0, 1000);
        Future<Long> futureResult = executor.submit(task);
        Long result = 0L;
        try {
            result = futureResult.get();
        } catch (Exception e) {
            System.out.println("Interrupted future.get() or sth similar");
        }
        assertEquals(result, compute(0, 1000));
    }

    @Test
    void invokeAllCallable() {
        MyExecService executor = MyExecService.newInstance();
        List<Callable<Long>> listOfTasks = new ArrayList<>();
        listOfTasks.add(() -> compute(0, 1000));
        listOfTasks.add(() -> compute(1001, 2000));
        listOfTasks.add(() -> compute(2001, 3000));
        listOfTasks.add(() -> compute(3001, 4000));
        Long result = 0L;
        try {
            List<Future<Long>> futures = executor.invokeAll(listOfTasks);
            for (Future<Long> val : futures) {
                result += val.get();
            }
        } catch (Exception e) {
            System.out.println("Interrupted future.get() or sth similar");
        }
        assertEquals(result, computeSum);
    }

    @Test
    void isTerminated() {
        MyExecService executor = MyExecService.newInstance();
        executor.submit(doSleepAndShowTime(1000));
        doSleep(500);
        assertEquals(1, executor.getPendingTasksQue().size());
        executor.shutdown();
        doSleep(1000);
        assertTrue(executor.isTerminated());
    }


    @Test
    void testExecute() {
        MyExecService s = MyExecService.newInstance();
        TestRunnable r = new TestRunnable();
        s.execute(r);
        doSleep(10);
        assertTrue(r.wasRun);
    }

    @Test
    void testScheduleRunnable() {
        MyExecService s = MyExecService.newInstance();
        TestRunnable r = new TestRunnable();
        s.submit(r);
        doSleep(10);
        assertTrue(r.wasRun);
    }

    @Test
    void testScheduleRunnableWithResult() throws Exception {
        MyExecService s = MyExecService.newInstance();
        TestRunnable r = new TestRunnable();
        Object expected = new Object();
        Future<Object> f = s.submit(r, expected);
        doSleep(10);
        assertTrue(r.wasRun);
        assertTrue(f.isDone());
        assertEquals(expected, f.get());
    }

    @Test
    void testScheduleCallable() throws Exception {
        MyExecService s = MyExecService.newInstance();
        StringCallable c = new StringCallable("X", 10);
        Future<String> f = s.submit(c);
        doSleep(20);
        assertTrue(f.isDone());
        assertEquals("X", f.get());
    }

    @Test
    void testShutdown() {
        ExecutorService s = MyExecService.newInstance();
        s.execute(new TestRunnable());
        doSleep(10);
        s.shutdown();
        assertThrows(
                RejectedExecutionException.class,
                () -> s.submit(new TestRunnable()));
    }

    @Test
    void testShutdownNow() {
        MyExecService executorService = new MyExecService();
        executorService.submit(() -> {
            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        List<Runnable> pendingTasks = executorService.shutdownNow();
        assertEquals(1, pendingTasks.size());
        assertTrue(executorService.isShutdown());
    }


    static void doSleep(int milis) {
        try {
            Thread.sleep(milis);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    long compute(int begin, int end) {
        int sum = 0;
        for (int i = begin; i < end; ++i)
            sum += numbers.get(i);
        return sum;
    }

    Callable<Long> doSleepAndShowTime(int milis) {
        return () -> {
            long currentTimeMillis = 0;
            currentTimeMillis = System.currentTimeMillis();
            System.out.println("Thread " + Thread.currentThread().getName() + " starts at " + currentTimeMillis);
            doSleep(milis);
            System.out.println("Thread " + Thread.currentThread().getName() + " finishes at " + System.currentTimeMillis());
            return currentTimeMillis;
        };
    }
}


class StringCallable implements Callable<String> {

    private final String result;
    private final int milis;

    StringCallable(String result, int milis) {
        this.result = result;
        this.milis = milis;
    }

    @Override
    public String call() throws Exception {
        ExecServiceTest.doSleep(milis);
        return result;
    }
}

class TestRunnable implements Runnable {
    boolean wasRun;

    @Override
    public void run() {
        wasRun = true;
    }
}
