package zmartProgram.main.mart.thread.customer;

import java.util.Objects;

import zmartProgram.main.customer.domain.Cart;
import zmartProgram.main.customer.domain.Customer;

public class CartLocal {
    private static final ThreadLocal<Customer> customerLocal = new ThreadLocal<>();
    private static final ThreadLocal<Cart> cartLocal = ThreadLocal.withInitial(()->new Cart());
    
    public static void initialize(Customer customer) {
        customerLocal.set(customer);
    }

    public static void reset() {
        customerLocal.remove();
        Cart cart = getCart();

        if (Objects.nonNull(cart)) {
            cart.clear();
        }
    }

    public static Customer getCustomer() {
        return customerLocal.get();
    }

    public static Cart getCart() {
        return cartLocal.get();
    }
}

