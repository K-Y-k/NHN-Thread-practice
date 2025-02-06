package deadlockThreeStarvation;

import java.util.Map;

public class App {
    public static void main(String[] args) {

        // shardCounter 객체를 0으로 초기화 합니다.
        SharedCounter sharedCounter = new SharedCounter(0l);

        // counterIncreaseHandler 객체를 생성합니다.
        CounterIncreaseHandler counterIncreaseHandler = new CounterIncreaseHandler(sharedCounter);

        // counterIncreaseHandler를 이용해서 threadA ~ E를 생성합니다.
        Thread threadA = new Thread(counterIncreaseHandler, "thread-A");
        Thread threadB = new Thread(counterIncreaseHandler, "thread-B");
        Thread threadC = new Thread(counterIncreaseHandler, "thread-C");
        Thread threadD = new Thread(counterIncreaseHandler, "thread-D");
        Thread threadE = new Thread(counterIncreaseHandler, "thread-E");

        // thrad의 우선순위를 지정할 수 있지만, 
        // 실질적인 관리는 운영체제에서 함으로 
        // 동작하는 순서는 다를 수 있습니다.
        threadA.setPriority(10);
        threadB.setPriority(1);
        threadC.setPriority(1);
        threadD.setPriority(1);
        threadE.setPriority(1);

        // threadA ~ E 를 시작합니다.
        threadA.start();
        threadB.start();
        threadC.start();
        threadD.start();
        threadE.start();

        // main thread가 실행 후 10초 후 threadA, threadB 종료될 수 있도록 interrupt 발생 시킵니다.
        try {
            Thread.sleep(10000);

            threadA.interrupt();
            threadB.interrupt();
            threadC.interrupt();
            threadD.interrupt();
            threadE.interrupt();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        //TODO#7 main Thread는 threadA와 threadB의 상태가 terminated가 될 때 까지 대기 합니다. 즉 threadA, threadB가 종료될 때 까지 대기(양보) 합니다.
        while (threadA.isAlive() && threadB.isAlive() && threadC.isAlive() && threadD.isAlive() && threadE.isAlive() ){
            Thread.yield();
        }

        Map<String, Long> result = counterIncreaseHandler.getThreadHistory();
        result.forEach((k,v)->{
            System.out.printf("%s : %ld", k, v);
        });

        System.out.println("System exit!");
    }
}
