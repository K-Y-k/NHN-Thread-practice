package zmartProgram.test.com.nhnacademy.customer.cart;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import zmartProgram.main.customer.domain.Cart;
import zmartProgram.main.customer.domain.CartItem;
import zmartProgram.main.mart.product.exception.ProductAlreadyExistsException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

class CartTest {

    Cart cart;
    @BeforeEach
    void setUp() throws ProductAlreadyExistsException {
        cart = new Cart();
        cart.tryAddItem(new CartItem(1l,1));
    }

    @Test
    @DisplayName("Serializable implements check")
    void constructorTest(){
        Assertions.assertInstanceOf(Serializable.class,cart);
    }

    @Test
    @DisplayName("장바구니(cart)에 제품(CartItem) 추가")
    void tryAddItem1() throws ProductAlreadyExistsException {
        CartItem cartItem = new CartItem(2l,1);

        cart.tryAddItem(cartItem);

        Assertions.assertAll(
                ()-> Assertions.assertEquals(cart.getCartItems().size(), 2),
                ()-> Assertions.assertEquals(cart.getCartItems().getLast(),cartItem)
        );
    }

    @Test
    @DisplayName("장바구니에 제품이 이미 추가되어 있따면 - ProductAlreadyExistsException 발생")
    void tryAddItem2() throws ProductAlreadyExistsException {
        // DisplayName에 작성된 요구사항이 만족하도록 검증합니다.
        CartItem cartItem = new CartItem(1l,1);

        Assertions.assertThrows(ProductAlreadyExistsException.class,()->{
            cart.tryAddItem(cartItem);
        });
    }

    @Test
    @DisplayName("Cart 비우기 - 초기화")
    void clear() {
        //DisplayName에 작성된 요구사항이 만족 하도록 검증 합니다.
        cart.clear();

        int actual = cart.getCartItems().size();
        Assertions.assertEquals(0,actual);
    }

    @Test
    @DisplayName("Cart item 조회")
    void getCartItems() {
        //productId : 2 장바구니 추가
        cart.getCartItems().add(new CartItem(2l,1));

        List<CartItem> excepted = new ArrayList<>();
        excepted.add(new CartItem(1l,1));
        excepted.add(new CartItem(2l,1));

        Assertions.assertEquals(excepted,cart.getCartItems());
    }
}