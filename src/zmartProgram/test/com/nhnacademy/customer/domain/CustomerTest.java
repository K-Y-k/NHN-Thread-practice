package zmartProgram.test.com.nhnacademy.customer.domain;

import com.nhnacademy.customer.exception.InsufficientFundsException;
import org.checkerframework.checker.units.qual.C;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

//CustomerTest를 통과 해야 합니다.
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class CustomerTest {

    Customer customer;
    @BeforeEach
    void setUp(){
        customer = new Customer(1l,"NHN아카데미",100_0000);
    }

    @Order(1)
    @Test()
    @DisplayName("id < 0")
    void testConstructor1(){
        Assertions.assertThrows(IllegalArgumentException.class,()->{
            customer = new Customer(-1l, "NHN아카데미",10_0000);
        });
    }

    @Order(2)
    @Test()
    @DisplayName("monry < 0")
    void testConstructor3(){
        // customer 생성시 money < 0 면 IllegalArgumentException이 발생하는지 검증합니다.
        Assertions.assertThrows(IllegalArgumentException.class,()->{
            customer = new Customer(1l, "NHN아카데미",-1_0000);
        });
    }

    @Order(3)
    @Test()
    @DisplayName("name is ( empty or null ) ")
    void testConstructor2(){
        // name이 "" or null 이면 IllegalArgumentException.class 예외가 발생하는지 검증합니다.
        Assertions.assertThrows(IllegalArgumentException.class,()->{
            customer = new Customer(1l, "",100_000);
        });
    }

    @Order(4)
    @Test
    void getId() {
        long actual = customer.getId();
        Assertions.assertEquals(1l, actual);
    }

    @Order(5)
    @Test
    void getName() {
        // customer -> getName() 호출시  NHN아카데미 반환하는지 검증합니다.
        String actual = customer.getName();
        Assertions.assertEquals("NHN아카데미",actual);
    }

    @Order(6)
    @Test
    void getMoney() {
        Assertions.assertEquals(100_0000,customer.getMoney());
    }

    @Order(7)
    @Test
    @DisplayName("결제 : 100_0000 - 10_00000 = 90_00000")
    void pay1() throws InsufficientFundsException {
        customer.pay(10_0000);
        int actual = customer.getMoney();
        Assertions.assertEquals(90_0000,actual);
    }

    @Order(8)
    @Test
    @DisplayName("결제 amount < 0 ")
    void pay2() throws InsufficientFundsException {
        Assertions.assertThrows(IllegalArgumentException.class,()->{
            customer.pay(-10_0000);
            int actual = customer.getMoney();
        });
    }

    @Order(9)
    @Test
    @DisplayName("customer money = 100만원, 200만원 결제 시도")
    void pay3(){
        // 200만원 결제시 InsufficientFundsException.class 예외가 발생 하는지 검증합니다.
        Assertions.assertThrows(InsufficientFundsException.class,()->{
            customer.pay(200_0000);
        });
    }

    @Order(10)
    @Test
    @DisplayName("id 와 name, money이 일치하면 동일한 객체로 식별")
    void testEquals1() {
        Customer excepted = new Customer(1l,"NHN아카데미",100_0000);
        Assertions.assertEquals(excepted, customer);
    }

    @Order(11)
    @Test
    @DisplayName("name, money 알치, 아이디는 불일치")
    void testEquals2() {
        Customer excepted = new Customer(2l,"NHN아카데미",100_0000);
        Assertions.assertNotEquals(excepted, customer);
    }
}