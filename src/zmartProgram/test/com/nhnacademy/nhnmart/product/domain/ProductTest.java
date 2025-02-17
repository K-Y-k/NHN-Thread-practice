package zmartProgram.test.com.nhnacademy.nhnmart.entring;

import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(value = MethodOrderer.OrderAnnotation.class)
class ProductTest {
    static Product product;
    static final long id = 1l;
    static final String item = "주방세제";
    static final String maker = "LG";
    static final String specification ="(750㎖) 자연퐁 스팀워시 레몬";
    static final String unit = "개";
    static final int price = 9900;
    static final int quantity = 100;

    @BeforeAll
    static void setUp(){
        product = new Product(id,item,maker,specification,unit,price,quantity);
    }

    @Test
    @Order(1)
    @DisplayName("null or empty check : item,maker,specification,unit")
    void constructorTest1(){

        Assertions.assertAll(
                //null check
                ()->Assertions.assertThrows(IllegalArgumentException.class,()->{
                    new Product(id,null,maker,specification,unit,price,quantity);
                }),
                ()->Assertions.assertThrows(IllegalArgumentException.class,()->{
                    new Product(id,item,null,specification,unit,price,quantity);
                }),
                ()->Assertions.assertThrows(IllegalArgumentException.class,()->{
                    new Product(id,item,maker,null,unit,price,quantity);
                }),
                ()->Assertions.assertThrows(IllegalArgumentException.class,()->{
                    new Product(id,item,maker,specification,null,price,quantity);
                }),

                //empty check
                ()->Assertions.assertThrows(IllegalArgumentException.class,()->{
                    new Product(id,"",maker,specification,unit,price,quantity);
                }),
                ()->Assertions.assertThrows(IllegalArgumentException.class,()->{
                    new Product(id,item,"",specification,unit,price,quantity);
                }),
                ()->Assertions.assertThrows(IllegalArgumentException.class,()->{
                    new Product(id,item,maker,"",unit,price,quantity);
                }),
                ()->Assertions.assertThrows(IllegalArgumentException.class,()->{
                    new Product(id,item,maker,specification,"",price,quantity);
                })
        );
    }

    @Test
    @Order(2)
    @DisplayName("{ id, price, quantity } < 0 ")
    void constructorTest2(){
        Assertions.assertAll(
                //null check
                ()->Assertions.assertThrows(IllegalArgumentException.class,()->{
                    new Product(-1,item,maker,specification,unit,price,quantity);
                }),
                ()->Assertions.assertThrows(IllegalArgumentException.class,()->{
                    new Product(id,item,maker,specification,unit,-100,quantity);
                }),
                ()->Assertions.assertThrows(IllegalArgumentException.class,()->{
                    new Product(id,item,maker,specification,unit,price,-5);
                })
        );
    }

    @Test
    @Order(3)
    void getId() {
        Assertions.assertEquals(id,product.getId());
    }

    @Test
    @Order(4)
    void getItem() {
        Assertions.assertEquals(item,product.getItem());
    }

    @Test
    @Order(5)
    void getMaker() {
        Assertions.assertEquals(maker,product.getMaker());
    }

    @Test
    @Order(6)
    void getSpecification() {
        Assertions.assertEquals(specification,product.getSpecification());
    }

    @Test
    @Order(7)
    void getUnit() {
        Assertions.assertEquals(unit,product.getUnit());
    }

    @Test
    @Order(8)
    void getPrice() {
        Assertions.assertEquals(price,product.getPrice());
    }

    @Test
    @Order(9)
    void getQuantity() {
        Assertions.assertEquals(quantity,product.getQuantity());
    }

    @Test
    @Order(10)
    void setQuantity() {
        //product의 quantity를 50으로 변경하고, 검증 합니다.

        product.setQuantity(50);
        Assertions.assertEquals(50,product.getQuantity());
    }

    @Test
    @Order(11)
    void setQuantity_negativeParam(){
        //product의 quantity를 -1으로 변경하고, IllegalArgumentException 발생하는지 검증 합니다.
        Assertions.assertThrows(IllegalArgumentException.class,()->{
            product.setQuantity(-1);
        });
    }
}