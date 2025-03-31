package synchronizedEx;

public class SharedCounter {
    private long count;

    public SharedCounter(){
        this(0l);
    }

    public SharedCounter(long count) {
        // 생성자를 초기화 합니다. count < 0 IllegalArgumentException이이 발생 합니다.
        if (count < 0){
            throw new IllegalArgumentException("count > 0 ");
        }

        this.count = count;
    }

    // mehtod 단위 lock을 걸고, count를 반환합니다.
    public synchronized long getCount(){
        return count;
    }

    public long increaseAndGet(){
        // block 단위로 lock을 걸고 
        // count = count + 1 증가시키고 count를 반환합니다.
        synchronized (this) {
            count = count + 1;
            return count;
        }
    }

    public long decreaseAndGet(){
        // block 단위로 lock을 걸고 
        // count = count - 1 감소시키고 count를 반환합니다.
        synchronized (this) {
            count = count - 1;
        }
        return count;
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
         * Syncronized 동기화는 여러 스레드가 동시에 하나의 공유 자원에 접근하지 못하도록 막아주어어 
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