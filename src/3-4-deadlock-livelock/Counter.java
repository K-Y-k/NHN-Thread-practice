package deadlockFourLivelock;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Counter {
    private int count = 0;
    private final Lock lock = new ReentrantLock();

    public void increment() {
        while (true) {
            if (lock.tryLock()) {
                try {
                    count++;
                    System.out.printf("%s count++: %d\n", Thread.currentThread().getName(), count );
                    break;
                } finally {
                    lock.unlock();
                }
            } else {
                System.out.printf("%s lock 획득 시도...\n", Thread.currentThread().getName());
            }
        }
    }

    public int getCount() {
        return count;
    }
}
