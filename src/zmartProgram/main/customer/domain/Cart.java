package zmartProgram.main.customer.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Cart implements Serializable {

    private final List<CartItem> cartItems;

    public Cart() {
        // 장바구니 아이템을 담을 수 있는 cartItems를 초기화 합니다. 
        // 장바구니에 아이템이 추가될 때 동기화 처리를 위해서 Collections.synchronizedList를 이용해서 cartItems 초기화 합니다.
        // 즉 한 번에 한 아이템만 추가될 수 있고, 중복해서 추가 될 수 없습니다.
        // https://docs.oracle.com/javase/6/docs/api/java/util/Collections.html#synchronizedList(java.util.List)
        cartItems = Collections.synchronizedList(new ArrayList<>());
    }

    public void tryAddItem(CartItem cartItem) throws ProductAlreadyExistsException {
        // 장바구니에 아이템이 이미 존재 한다면, ProductAlreadyExistsException 예외가 발생 합니다.
        if (cartItems.contains(cartItem)){
            throw new ProductAlreadyExistsException(cartItem.getProductId());
        }

        // cartItem에 아이템을 추가
        cartItems.add(cartItem);
    }

    public void clear(){
        // 장바구니 cartItems를 초기화 합니다.
        cartItems.clear();
    }

    public List<CartItem> getCartItems() {
        // 장바구니 아이템을 반환합니다.
        return cartItems;
    }
}
