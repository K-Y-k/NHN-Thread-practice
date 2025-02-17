package ztestExam.quiz04;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.TimeoutException;

public class SharedQueue {
    // 필드를 선언하세요.
    private final Queue<Integer> queue = new LinkedList<>();
    private final int maxSize;

    public SharedQueue(int maxSize) {
        // 인수 검증과 필드 초기화를 하세요.
        if (maxSize <= 0) {
            throw new IllegalArgumentException("maxSize > 0");
        }

        this.maxSize = maxSize;
    }

    public synchronized void enqueue(int value) {
        while (queue.size() >= maxSize) {
            try {
                wait();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        queue.add(value);
        notifyAll();

        // synchronized (queue) {
        //     while (queue.size() >= maxSize) {
        //         try {
        //             // 락의 플래그는 객체에 남기는데
        //             // 현재 synchronized를 (queue) 객체에 lock을 걸었으므로
        //             // queue만 wait을 걸어야 한다.
        //             // 메소드 키워드로 썼거나 synchronized (this)라면 SharedQueue 객체에 락이 걸린다.
        //             queue.wait();
        //         } catch (InterruptedException e) {
        //             throw new RuntimeException(e);
        //         }
        //     }

        //     queue.add(value);
        // 
        //     // 현재 synchronized를 (queue) 객체에 lock을 걸었으므로
        //     // queue만 notifyAll을 깨워야 한다.
        //     queue.notifyAll();
        // }
    }

    public synchronized void enqueue(int value, long timeout) throws TimeoutException {
        if (timeout < 0) {
            throw new IllegalArgumentException();
        }

        long startTime = System.currentTimeMillis();

        synchronized (queue) {
            while (maxSize <= queue.size()) {
                timeout -= (System.currentTimeMillis() - startTime);
                if (timeout < 0) {
                    throw new TimeoutException();
                }

                try {
                    queue.wait(timeout);
                } catch (InterruptedException e) {
                    throw new RuntimeException();
                }
            }

            queue.add(value);
            queue.notifyAll();
        }
        
        
        // try {
        //     boolean isTimeOut = false;
        //     long startTime = System.currentTimeMillis();
            
        //     while (!isTimeOut && queue.size() >= maxSize) {
        //         long currentTime = System.currentTimeMillis();
        //         long diffTime = currentTime - startTime;
        //         long restTime = timeout - diffTime;
                
        //         if (restTime <= 0) {
        //             // Timeout인 경우는 TimeoutException 발생
        //             throw new TimeoutException();
        //         }
                
        //         // Timeout이 아니면 잔여 시간만큼 다시 대기걸기
        //         wait(restTime);
        //     }
        // } catch (InterruptedException e) {
        //     throw new RuntimeException();
        // }

        // notifyAll();
        // queue.add(value);
    }

    public synchronized int dequeue() {
        while (queue.isEmpty()) {
            try {
                wait();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        notifyAll();
        return queue.poll();
    }

    public synchronized int dequeue(long timeout) throws TimeoutException {
        // try {
        //     wait(timeout);

        //     if (queue.isEmpty()) {
        //         throw new TimeoutException();
        //     }
        // } catch (InterruptedException e) {
        //     throw new RuntimeException();
        // }

        try {
            boolean isTimeOut = false;
            long startTime = System.currentTimeMillis();
            
            while (!isTimeOut && queue.isEmpty()) {
                long currentTime = System.currentTimeMillis();
                long diffTime = currentTime - startTime;
                long restTime = timeout - diffTime;
                
                if (restTime <= 0) {
                    // Timeout인 경우는 TimeoutException 발생
                    throw new TimeoutException();
                }
                
                // Timeout이 아니면 잔여 시간만큼 다시 대기걸기
                wait(restTime);
            }
        } catch (InterruptedException e) {
            throw new RuntimeException();
        }
        
        return queue.poll();
    }

    public synchronized int size() {
        return queue.size();
    }
}
