package semaphore;

import java.util.concurrent.Semaphore;

public class SharedCounter {
    private long count;
    private Semaphore semaphore;

    public SharedCounter(){
        count = 0l;
    }

    public SharedCounter(long count) {
        if (count < 0){
            throw new IllegalArgumentException("count > 0 ");
        }
        this.count = count;
        
        // semaphore를 생성합니다.(동시에 하나의 Thread만 접근할 수 있습니다.), permits prameter를 확인하세요.
        semaphore = new Semaphore(1);
    }

    public long getCount(){
        /*  count를 반환합니다.
            semaphore.acquire()를 호출하여 허가를 획득합니다.
            쓰레드가 작업이 완료되면
            semaphore.release()를 호출하여 허가를 반환 합니다.
         */

        try {
            semaphore.acquire();
            return count;
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            semaphore.release();
        }
    }

    public long increaseAndGet(){
        /* count = count + 1 증가시키고 count를 반환합니다.
           semaphore를 이용해서 동기화할 수 있도록 구현합니다.
        */
        try {
            semaphore.acquire();
            count = count + 1;
            return count;
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            semaphore.release();
        }
    }

    public long decreaseAndGet(){
        /* count = count-1 감소시키고 count를 반환 합니다.
           semaphore를 이용해서 동기화할 수 있도록 구현 합니다.
        */
        try {
            semaphore.acquire();
            count = count - 1;
            return count;
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            semaphore.release();
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
         * 세마포어를 통해
         * 자원을 획득, 반환을 통해 동시에 접근을 막아 
         * 이제는 중첩되는 값 출력이 발생하지 않는다.
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