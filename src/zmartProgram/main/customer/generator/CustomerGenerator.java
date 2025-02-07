package zmartProgram.main.customer.generator;

import com.devskiller.jfairy.Fairy;
import com.devskiller.jfairy.producer.person.Person;

import zmartProgram.main.customer.domain.Customer;
import zmartProgram.main.mart.entering.EnteringQueue;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicLong;

public class CustomerGenerator implements Runnable {

    //NhnMart 입장 대기열
    private final EnteringQueue enteringQueue;

    //회원 번호 Id 생성
    private final AtomicLong atomicId;

    //회원이 보유한 default money
    private final static int DEFAULT_MONEY=100_0000;

    public CustomerGenerator(EnteringQueue enteringQueue) {
        // enteringQueue null 이면 'IllegalArgumentException' 발생하는지 검증합니다.
        if (Objects.isNull(enteringQueue)){
            throw new IllegalArgumentException("enteringQueue is null!");
        }

        // enteringQueue, atomicId를 0으로 초기화 합니다.
        this.enteringQueue = enteringQueue;
        atomicId=new AtomicLong(0);

    }

    @Override
    public void run() {

        /*
        * generate() method를 이용해서 customer를 생성하고 enteringQueue에 등록 합니다.
        */
        while (!Thread.currentThread().isInterrupted()){
            try {
                // 1초 간격으로 회원을 entringQueue의 대기열에 등록합니다.
                Thread.sleep(1000);

                Customer customer = generate();
                enteringQueue.addCustomer(customer);
                System.out.printf("generate-customer: %s", customer);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    private Customer generate(){

        /*Customer 객체를 생성 후 반환 합니다.
            - customer->id 는 atomicId를 사용하여 구현
            - 회원이름은 random으로 생성 됩니다.
            - 회원이름 생성시 https://github.com/Devskiller/jfairy 이용해서 구현
         */

        Fairy fairy = Fairy.create();
        Person person = fairy.person();
        long id = atomicId.incrementAndGet();
        String name = person.getFullName();
        Customer customer = new Customer(id,name,DEFAULT_MONEY);
        return customer;
    }
}
