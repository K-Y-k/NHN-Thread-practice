package mutex;

import java.util.concurrent.locks.ReentrantLock;

public class SharedCounter {
    private long count;
    private final ReentrantLock mutex;

    public SharedCounter(){
        this(0l);
    }

    public SharedCounter(long count) {
        if (count < 0){
            throw new IllegalArgumentException("count > 0 ");
        }
        this.count = count;

        /* ReentrantLock 생성 합니다.(mutex는 동시에 하나의 Thread만 접근할 수 있습니다.)
           ReentrantLock은 기본적으로 비공정한 락 입니다. 
           (true)로 공정성을 보장 하도록 초기화 합니다.
         */
        mutex = new ReentrantLock(true);
    }

    public long getCount(){
        /* count를 반환합니다.
            mutex.lock()를 호출하여 다른 thread가 접근할 수 없도록 lock을 걸어줍니다.
            쓰레드가 작업이 완료되면
            mutex.unlock()를 호출하여
            잠금을 해제 합니다. 뮤텍스는 lock을 건 쓰레드만 lock을 해제할 수 있습니다.
         */
        try {
            mutex.lock();
            return count;
        } catch (Exception e){
            throw new RuntimeException(e);
        } finally {
            mutex.unlock();
        }
    }

    public long increaseAndGet(){
        /* count = count + 1 증가시키고 count를 반환합니다.
           mutex를 이용해서 동기화 될 수 있도록 구현합니다.
        */
        try {
            mutex.lock();
            count = count + 1;
            return count;
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            mutex.unlock();
        }
    }

    public long decreaseAndGet(){
        /* count = count-1 감소시키고 count를 반환 합니다.
           mutex를 이용해서 동기화 될 수 있도록 구현 합니다.
        */
        try {
            mutex.lock();
            count = count - 1;
            return count;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }finally {
            mutex.unlock();
        }
    }
}


class App {

    public static void main(String[] args) {
        Thread.currentThread().setPriority(Thread.MAX_PRIORITY);

        // shardCounter 객체를 0으로 초기화합니다.
        SharedCounter sharedCounter = new SharedCounter(0l);

        // counterIncreaseHandler 객체를 생성합니다.
        CounterIncreaseHandler counterIncreaseHandler = new CounterIncreaseHandler(sharedCounter);
        
        // counterIncreaseHandler를 이용해서 threadA를 생성힙니니다.
        Thread threadA = new Thread(counterIncreaseHandler);
        
        // threadA의 thread name을 "thread-A"로 설정합니다.
        threadA.setName("thread-A");
        
        // threadA를 시작 합니다.
        threadA.start();


        // counterIncreaseHandler를 이용해서 threadB를 생성합니다.
        Thread threadB = new Thread(counterIncreaseHandler);

        // threadB의 name을 'thread-B'로 설정합니다.
        threadB.setName("thread-B");

        // threadB를 시작합니다.
        threadB.start();


        /*
         * 공정한 락은 먼저 락을 요청한 쓰레드가 먼저 락을 획득하게 됩니다. 
         * 이를 통해 모든 쓰레드가 공평하게 락을 획득할 기회를 가지게 되어 
         * 순차적으로 접근하여 출력된다.
         */


        // main thread가 실행 후 20초 후 threadA, threadB 종료될 수 있도록 interrupt 발생시킵니다.
        try {
            Thread.sleep(20000);

            threadA.interrupt();
            threadB.interrupt();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        // main Thread는 threadA와 threadB의 상태가 terminated가 될 때 까지 대기합니다. 
        // 즉 threadA, threadB가 종료될 때 까지 대기(양보)합니다.
        while (threadA.isAlive() && threadB.isAlive()){
            Thread.yield();
        }

        System.out.println("System exit!");
    }
}