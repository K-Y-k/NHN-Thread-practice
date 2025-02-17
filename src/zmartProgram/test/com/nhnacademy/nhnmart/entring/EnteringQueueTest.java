package zmartProgram.test.com.nhnacademy.nhnmart.entring;

import lombok.extern.slf4j.Slf4j;
import zmartProgram.main.customer.domain.Customer;
import zmartProgram.main.mart.entering.EnteringQueue;

import org.junit.jupiter.api.*;
import org.junit.platform.commons.function.Try;
import org.junit.platform.commons.util.ReflectionUtils;

@Slf4j
class EnteringQueueTest {
    EnteringQueue enteringQueue;

    @BeforeEach
    void setUp() {
        enteringQueue = new EnteringQueue();

        /*
            Customer{id=1, name='NHN아카데미1', money=1000000}
            ~
            Customer{id=99, name='NHN아카데미99', money=1000000}
            1~99 고객을 생성 후 enteringQueue 대기열에 등록 합니다.
         */
        for (int i = 0; i < 99; i++) {
            Customer customer = new Customer(i+1,String.format("NHN아카데미%d", i+1),100_0000);
            
            System.out.printf("%s\n",customer);
            enteringQueue.addCustomer(customer);
        }
    }

    @Test
    @DisplayName("default queue capacity = 100")
    void constructorTest_InitCapacity() throws Exception {
        Try<Object> capacity = ReflectionUtils.tryToReadFieldValue(EnteringQueue.class,"capacity",enteringQueue);
        Assertions.assertEquals(100, (int)capacity.get() );
    }

    @Test
    void addCustomer() throws Exception {
        // id=100인 고객을 대기열에 등록하고 검증합니다.
        Customer customer = new Customer(100l, "NHN아카데미100",100_0000);
        
        enteringQueue.addCustomer(customer);

        int actual = enteringQueue.getQueueSize();
        Assertions.assertEquals(100, actual);
    }

    @Test
    @DisplayName("queue - poll test")
    void getCustomer() {
        // 대기열에서 enteringQueue.getCustomer() 호출시 반환되는 값을 검증합니다.
        Customer actual = enteringQueue.getCustomer();
        Customer excepted = new Customer(1l, "NHN아카데미1",100_0000);
        
        Assertions.assertEquals(excepted, actual);
        System.out.printf("customer: %s\n", actual);
    }

    @Test
    @DisplayName("blocking queue test : queue size : 100, 101번째 customer를 추가 한다면, consumer에 의해서 소비될 때 까지 대기 합니다.")
    void blockingTest() throws Exception {

        Thread producer = new Thread(new Runnable() {
            @Override
            public void run() {
                Customer customer100= new Customer(100l, "NHN아카데미100",100_0000);
                System.out.println("2초 대기 후 101-customer 추가 됨");
                Customer customer101= new Customer(101l, "NHN아카데미101",100_0000);
                
                enteringQueue.addCustomer(customer100);
                enteringQueue.addCustomer(customer101);
            }
        });
        producer.start();

        // 2초 대기후 enteringQueue.getCustomer() 호출해서 소비할 수 있도록 consumer Thread를 구현합니다.
        Thread consumer = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    //2초 대기 후 getCustomer()를 호출합니다.
                    Thread.sleep(2000);
                    enteringQueue.getCustomer();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        consumer.start();

        // producer or consumer 실행 중이라면 대기합니다.
        while (producer.isAlive() || consumer.isAlive()){
            Thread.yield();
        }

        int actual = enteringQueue.getQueueSize();

        Assertions.assertEquals(100,actual);
    }
}