package zmartProgram.main.mart.thread.pay;

import zmartProgram.main.customer.domain.Cart;
import zmartProgram.main.customer.domain.CartItem;
import zmartProgram.main.customer.domain.Customer;
import zmartProgram.main.customer.exception.InsufficientFundsException;
import zmartProgram.main.mart.product.domain.Product;
import zmartProgram.main.mart.product.service.ProductService;
import zmartProgram.main.mart.thread.util.Executable;

public class SelfCheckoutRequest implements Executable {

    private final Customer customer;
    private final Cart cart;
    private final ProductService productService;

    public SelfCheckoutRequest(Customer customer, Cart cart, ProductService productService) {
        // customer, cart, productService null 이면 IllegalArgumentException 발생
        if (customer == null || cart == null || productService == null) {
            throw new IllegalArgumentException();
        }

        // customer, cart, productService 초기화
        this.customer = customer;
        this.cart = cart;
        this.productService = productService;
    }

    @Override
    public void execute(){

        /* execute method를 구현
           - getTotalAmountFromCart()로로 결제금액을 계산하고
           - customer.pay() method를 이용해서 결제를 진행합니다.
           - 결제할 총 금액이 < customer.money 이면 모든 제품을 반납합니다.
        */
        try {
            int amount = getTotalAmountFromCart();
            customer.pay(amount);
        } catch (InsufficientFundsException insufficientFundsException) {
            // 결제 fail, 장바구니에 있는 모든 물건들을  다시 회수 합니다.

            for (CartItem cartItem : cart.getCartItems()) {
                productService.returnProduct(cartItem.getProductId(), cartItem.getQuantity());
            }

        } catch (Exception e) {
            if (e instanceof InterruptedException) {
                Thread.currentThread().interrupt();
            }
        }

        // 1초 단위로 결제를 진행합니다.
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public int getTotalAmountFromCart(){
        // 결제 금액을 계산 후 반환합니다.
        int totalPrice = 0;

        for (CartItem cartItem : cart.getCartItems()){
            long id = cartItem.getProductId();
            int quantity = cartItem.getQuantity();

            Product findProduct = productService.getProduct(id);
            int price = findProduct.getPrice();
            
            totalPrice += price * quantity;
        }
        
        return totalPrice;
    }
}

