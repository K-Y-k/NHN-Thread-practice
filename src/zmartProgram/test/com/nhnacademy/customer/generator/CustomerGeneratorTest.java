package zmartProgram.test.com.nhnacademy.customer.generator;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import zmartProgram.main.customer.generator.CustomerGenerator;
import zmartProgram.main.mart.entering.EnteringQueue;

import java.lang.reflect.Constructor;

import static org.junit.jupiter.api.Assertions.*;

class CustomerGeneratorTest {
    CustomerGenerator customerGenerator;
    EnteringQueue enteringQueue;

    @BeforeEach
    void setUp() {
        // 대기열을 capacity = 5로 초기화 합니다.
        enteringQueue = new EnteringQueue(5);

        // 대기열을 이용해서 customerGeneratorr 객체를 생성합니다.
        customerGenerator = new CustomerGenerator(enteringQueue);
    }

    @Test
    @DisplayName("enteringQueue is null")
    void constructorTest(){
        // enteringQueue == null 이면 IllegalArgumentException 발생 하는지 검증합니다.
        Assertions.assertThrows(IllegalArgumentException.class,()->{
            new CustomerGenerator(null);
        });
    }

    @Test
    @DisplayName("10초 동안 customer 객체가 enteringQueue 대기열 등록")
    void generatorTest() throws InterruptedException {
        // customerGenerator를 이용해서 customerGeneratorThread 초기화 하고 실행합니다.
        Thread customerGeneratorThread = new Thread(customerGenerator);
        customerGeneratorThread.start();

        // 10초 대기합니다.
        Thread.sleep(10000);

        // customerGeneratorThread를 종료합니다.
        customerGeneratorThread.interrupt();

        while (customerGeneratorThread.isAlive()) {
            Thread.yield();
        }

        Assertions.assertAll(
            // interrupt발생시 customerGeneratorThread의 상태가  TERMINATED 상태인지 검증
            ()->Assertions.assertEquals(Thread.State.TERMINATED,customerGeneratorThread.getState()),

            // 대기열 최대 Queue Size가 5 <-- 10초 동안 최대 5명의 고객이 대기열에 등록되었는지 검증
            ()->Assertions.assertEquals(5, enteringQueue.getQueueSize())
        );
    }
}