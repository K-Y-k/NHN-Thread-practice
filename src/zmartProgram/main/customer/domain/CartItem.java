package zmartProgram.main.customer.domain;

import java.io.Serializable;
import java.util.Objects;

public class CartItem implements Serializable {

    // 제품 아이디
    private final long productId;
    // 제품 수량
    private final int quantity;

    public CartItem(long productId, int quantity) {
        // productId < 0 or quantity<0 IllegalArgumentException이 발생합니다.
        if (productId <0 || quantity < 0){
            throw new IllegalArgumentException("productId > 0");
        }

        // productId, quantity를 초기화 합니다.
        this.productId = productId;
        this.quantity = quantity;
    }

    public long getProductId() {
        // productId 반환합니다.
        return productId;
    }

    public int getQuantity() {
        // quantity를 반환합니다.
        return quantity;
    }

    // (projectId, quantity)를 기준으로 객체 비교를 하기 위해서 equals() 구현
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CartItem cartItem = (CartItem) o;
        return productId == cartItem.productId && quantity == cartItem.quantity;
    }

    // (projectId, quantity)를 기준으로 hashCode() 구현
    @Override
    public int hashCode() {
        return Objects.hash(productId, quantity);
    }
}